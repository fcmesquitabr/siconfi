package br.gov.ce.sefaz.siconfi.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

import br.gov.ce.sefaz.siconfi.entity.MatrizSaldoContabeis;
import br.gov.ce.sefaz.siconfi.enums.TipoMatrizSaldoContabeis;
import br.gov.ce.sefaz.siconfi.enums.TipoValorMatrizSaldoContabeis;
import br.gov.ce.sefaz.siconfi.response.MatrizSaldoContabeisResponse;
import br.gov.ce.sefaz.siconfi.util.FiltroMSC;
import br.gov.ce.sefaz.siconfi.util.Utils;

public abstract class MSCService<T extends MatrizSaldoContabeis> extends SiconfiService<T> {

	//private static final Logger logger = LogManager.getLogger(MSCService.class);

	private EnteService enteService;
	
	public MSCService() {
		super();
	}

	protected abstract String getAPIPath();

	protected abstract Class<? extends MatrizSaldoContabeisResponse<T>> getResponseClassType();

	protected abstract String getEntityName();

	protected abstract List<Integer> getClassContas();

	public void carregarDados(FiltroMSC filtro) {
		
		List<T> listaMSC = consultarNaApi(filtro);	
		
		switch (filtro.getOpcaoSalvamento()) {
		case CONSOLE:
			exibirDadosNaConsole(listaMSC);
			break;
		case ARQUIVO:
			String nomeArquivo = definirNomeArquivoCSV(filtro);
			escreverCabecalhoArquivoCsv(nomeArquivo);
			salvarArquivoCsv(listaMSC, nomeArquivo);
			break;
		case BANCO:
			salvarNoBancoDeDados(filtro, listaMSC);
			break;
		}
	}

	protected void salvarNoBancoDeDados(FiltroMSC filtro, List<T> listaEntidades) {
		
		if(Utils.isEmptyCollection(listaEntidades)) return;
		
		getEntityManager().getTransaction().begin();		
		excluirMatrizSaldosContabeis(filtro);
		persistir(listaEntidades);
		commitTransaction();
		fecharContextoPersistencia();		
	}

	private void excluirMatrizSaldosContabeis(FiltroMSC filtro) {
		getLogger().info("Excluindo dados do banco de dados...");
		
		StringBuilder queryBuilder = new StringBuilder("DELETE FROM " + getEntityName() + " msc WHERE msc.exercicio IN (:exercicios) ");

		List<String> listaCodigoIbge = getEnteService().obterListaCodigosIbge(filtro);
		if(!Utils.isEmptyCollection(listaCodigoIbge)) {
			queryBuilder.append(" AND msc.cod_ibge IN (:codigosIbge)");
		}
		if(!filtro.isListaPeriodosVazia()) {
			queryBuilder.append(" AND msc.mes_referencia IN (:listaMeses)");
		}
		if(filtro.getTipoMatrizSaldoContabeis()!=null){
			queryBuilder.append(" AND msc.tipo_matriz=:codigoTipoMatriz");
		}
		if(!filtro.isListaClassesContaVazia()) {
			queryBuilder.append(" AND msc.classe_conta IN (:listaClasseConta)");
		}
		if(!filtro.isListaTipoValorVazia()) {
			queryBuilder.append(" AND msc.tipo_valor IN (:listaTipoValor)");
		}
		
		Query query = getEntityManager().createQuery(queryBuilder.toString());
		query.setParameter("exercicios", filtro.getExercicios());
		
		if(!Utils.isEmptyCollection(listaCodigoIbge)) {
			query.setParameter("codigosIbge", listaCodigoIbge);
		}
		if(!filtro.isListaPeriodosVazia()) {
			query.setParameter("listaMeses", filtro.getPeriodos());
		}
		if(filtro.getTipoMatrizSaldoContabeis()!=null){
			query.setParameter("codigoTipoMatriz", filtro.getTipoMatrizSaldoContabeis().getCodigo());
		}
		if(!filtro.isListaClassesContaVazia()) {
			query.setParameter("listaClasseConta", filtro.getListaClasseConta());
		}
		if(!filtro.isListaTipoValorVazia()) {
			query.setParameter("listaTipoValor", filtro.getListaCodigoTipoValor());
		}

		int i = query.executeUpdate();
		getLogger().info("Linhas excluídas:" + i);
	}

	@Override
	public void excluirTodos() {
		getLogger().info("Excluindo dados do banco de dados...");
		int i = getEntityManager().createQuery("DELETE FROM " + getEntityName() + " mscp")
				.executeUpdate();
		getLogger().info("Linhas excluídas:" + i);
	}

	public List<T> consultarNaApi() {
		return new ArrayList<T>();
	}

	public List<T> consultarNaApi(FiltroMSC filtro) {

		List<Integer> listaExercicios = !filtro.isListaExerciciosVazia() ? filtro.getExercicios()
				: EXERCICIOS_DISPONIVEIS;
		List<T> listaMSC = new ArrayList<>();

		for (Integer exercicio : listaExercicios) {
			listaMSC.addAll(consultarNaApi(filtro, exercicio));
		}
		return listaMSC;
	}

	private List<T> consultarNaApi(FiltroMSC filtro, Integer exercicio) {

		List<Integer> listaMeses = !filtro.isListaPeriodosVazia() ? filtro.getPeriodos() : MESES;
		List<T> listaMSC = new ArrayList<>();

		for (Integer mes: listaMeses) {
			listaMSC.addAll(consultarNaApi(filtro, exercicio, mes));
		}
		return listaMSC;
	}

	private List<T> consultarNaApi(FiltroMSC filtro, Integer exercicio, Integer mes) {

		List<String> listaCodigoIbge = getEnteService().obterListaCodigosIbge(filtro);
		TipoMatrizSaldoContabeis tipoMatriz = filtro.getTipoMatrizSaldoContabeis() != null
				? filtro.getTipoMatrizSaldoContabeis()
				: TipoMatrizSaldoContabeis.MSCC;

		List<T> listaMSC = new ArrayList<>();		
		for (String codigoIbge : listaCodigoIbge) {
			listaMSC.addAll(consultarNaApi(filtro, exercicio, mes, codigoIbge, tipoMatriz));
		}
		return listaMSC;
	}

	private List<T> consultarNaApi(FiltroMSC filtro, Integer exercicio, Integer mes,
			String codigoIbge, TipoMatrizSaldoContabeis tipoMatriz) {

		List<Integer> listaClassesConta = !filtro.isListaClassesContaVazia()?filtro.getListaClasseConta():getClassContas();
		
		List<T> listaMSC = new ArrayList<>();
		for (Integer classe: listaClassesConta) {
			listaMSC.addAll(consultarNaApi(filtro, exercicio, mes, codigoIbge, tipoMatriz, classe));
		}
		return listaMSC;
	}

	private List<T> consultarNaApi(FiltroMSC filtro, Integer exercicio, Integer mes,
			String codigoIbge, TipoMatrizSaldoContabeis tipoMatriz, Integer classeConta) {

		List<TipoValorMatrizSaldoContabeis> listaTipoValor = !filtro.isListaTipoValorVazia()
				? filtro.getListaTipoValor()
				: Arrays.asList(TipoValorMatrizSaldoContabeis.values());
		
		List<T> listaMSC = new ArrayList<>();
		for (TipoValorMatrizSaldoContabeis tipoValor: listaTipoValor) {
			listaMSC.addAll(consultarNaApi(exercicio, mes, codigoIbge, tipoMatriz, classeConta, tipoValor));
			aguardarUmSegundo();
		}
		return listaMSC;
	}

	public List<T> consultarNaApi(Integer exercicio, Integer mes, String codigoIbge,
			TipoMatrizSaldoContabeis tipoMatriz, Integer classeConta, TipoValorMatrizSaldoContabeis tipoValorMatriz) {

		List<T> listaMSC = null;
		try {

			MatrizSaldoContabeisResponse<T> mscResponse = obterResponseDaApi(exercicio, mes,
					codigoIbge, tipoMatriz, classeConta, tipoValorMatriz);

			listaMSC = mscResponse != null ? mscResponse.getItems()
					: new ArrayList<T>();
		} catch (Exception e) {
			getLogger().error("Erro para os parâmetros: exercicio: " + exercicio + ", mes: " + mes
					+ ", classeConta:" + classeConta 
					+ ", codigoTipoMatriz:" + tipoMatriz.getCodigo() + ", tipoValorMatriz: " + tipoValorMatriz.getCodigo() 
					+ ", codigoIbge: " + codigoIbge);
			e.printStackTrace();
			listaMSC = new ArrayList<>();
		}

		getLogger().debug("Tamanho da lista de MSC os parâmetros: exercicio: "+ exercicio + ", mes: " + mes
				+ ", classeConta:" + classeConta 
				+ ", codigoTipoMatriz:" + tipoMatriz.getCodigo() + ", tipoValorMatriz: " + tipoValorMatriz.getCodigo() 
				+ ", codigoIbge: " + codigoIbge + ": " + listaMSC.size());
		return listaMSC;
	}

	private MatrizSaldoContabeisResponse<T> obterResponseDaApi(Integer exercicio, Integer mes, String codigoIbge,
			TipoMatrizSaldoContabeis tipoMatriz, Integer classeConta, TipoValorMatrizSaldoContabeis tipoValorMatriz) {

		long ini = System.currentTimeMillis();

		this.webTarget = this.client.target(URL_SERVICE).path(getAPIPath())
				.queryParam(API_QUERY_PARAM_AN_REFERENCIA, exercicio)
				.queryParam(API_QUERY_PARAM_ME_REFERENCIA, mes)
				.queryParam(API_QUERY_PARAM_ID_ENTE, codigoIbge)
				.queryParam(API_QUERY_PARAM_CO_TIPO_MATRIZ, tipoMatriz.getCodigo())
				.queryParam(API_QUERY_PARAM_CLASSE_CONTA, classeConta)
				.queryParam(API_QUERY_PARAM_ID_TV, tipoValorMatriz.getCodigo());
		Invocation.Builder invocationBuilder = this.webTarget.request(API_RESPONSE_TYPE);
		getLogger().info("Get na API: " + this.webTarget.getUri().toString());
		Response response = invocationBuilder.get();
		MatrizSaldoContabeisResponse<T> mscResponse = response.readEntity(getResponseClassType());
		
		long fim = System.currentTimeMillis();
		getLogger().debug("Tempo para consultar a MSC na API:" + (fim - ini));
		return mscResponse;
	}	
	
	protected EnteService getEnteService() {
		if(enteService == null) {
			enteService = new EnteService();
		}
		return enteService;
	}
}
