package br.gov.ce.sefaz.siconfi.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.gov.ce.sefaz.siconfi.entity.MatrizSaldoContabeisOrcamentaria;
import br.gov.ce.sefaz.siconfi.enums.TipoMatrizSaldoContabeis;
import br.gov.ce.sefaz.siconfi.enums.TipoValorMatrizSaldoContabeis;
import br.gov.ce.sefaz.siconfi.response.MatrizSaldoContabeisOrcamentariaResponse;
import br.gov.ce.sefaz.siconfi.util.FiltroMSC;
import br.gov.ce.sefaz.siconfi.util.Utils;

public class MSCOrcamentariaService extends SiconfiService<MatrizSaldoContabeisOrcamentaria> {

	private static final Logger logger = LogManager.getLogger(MSCOrcamentariaService.class);

	private static String[] COLUNAS_ARQUIVO_CSV = new String[] { "exercicio", "mes_referencia", "cod_ibge", "poder_orgao", "tipo_matriz",
			"class_conta", "natureza_conta", "conta_contabil", "funcao", "subfuncao", "educacao_saude", 
			"natureza_despesa", "ano_inscricao", "natureza_receita","ano_fonte_recursos", "fonte_recursos", 
			"data_referencia", "entrada_msc", "tipo_valor", "valorFormatado"};
	
	private static String NOME_PADRAO_ARQUIVO_CSV = "msc_orcamentaria.csv";

	private static final String API_PATH_MSC_ORCAMENTARIA = "msc_orcamentaria";

	private EnteService enteService;

	public MSCOrcamentariaService() {
		super();
	}

	public void carregarDados(FiltroMSC filtro) {
		
		List<MatrizSaldoContabeisOrcamentaria> listaMSC = consultarNaApi(filtro);	
		
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

	protected void salvarNoBancoDeDados(FiltroMSC filtro, List<MatrizSaldoContabeisOrcamentaria> listaEntidades) {
		
		if(Utils.isEmptyCollection(listaEntidades)) return;
		
		getEntityManager().getTransaction().begin();		
		excluirMatrizSaldosContabeis(filtro);
		persistir(listaEntidades);
		commitTransaction();
		fecharContextoPersistencia();		
	}

	private void excluirMatrizSaldosContabeis(FiltroMSC filtro) {
		logger.info("Excluindo dados do banco de dados...");
		
		StringBuilder queryBuilder = new StringBuilder("DELETE FROM MatrizSaldoContabeisOrcamentaria msc WHERE msc.exercicio IN (:exercicios) ");

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
		logger.info("Linhas excluídas:" + i);
	}
	
	@Override
	public void excluirTodos() {
		logger.info("Excluindo dados do banco de dados...");
		int i = getEntityManager().createQuery("DELETE FROM MatrizSaldoContabeisOrcamentaria msc")
				.executeUpdate();
		logger.info("Linhas excluídas:" + i);
	}

	@Override
	public List<MatrizSaldoContabeisOrcamentaria> consultarNaApi() {
		return new ArrayList<MatrizSaldoContabeisOrcamentaria>();
	}

	public List<MatrizSaldoContabeisOrcamentaria> consultarNaApi(FiltroMSC filtro) {

		List<Integer> listaExercicios = !filtro.isListaExerciciosVazia() ? filtro.getExercicios()
				: EXERCICIOS_DISPONIVEIS;
		List<MatrizSaldoContabeisOrcamentaria> listaMSC = new ArrayList<>();

		for (Integer exercicio : listaExercicios) {
			listaMSC.addAll(consultarNaApi(filtro, exercicio));
		}
		return listaMSC;
	}

	private List<MatrizSaldoContabeisOrcamentaria> consultarNaApi(FiltroMSC filtro, Integer exercicio) {

		List<Integer> listaMeses = !filtro.isListaPeriodosVazia() ? filtro.getPeriodos() : MESES;
		List<MatrizSaldoContabeisOrcamentaria> listaMSC = new ArrayList<>();

		for (Integer mes: listaMeses) {
			listaMSC.addAll(consultarNaApi(filtro, exercicio, mes));
		}
		return listaMSC;
	}

	private List<MatrizSaldoContabeisOrcamentaria> consultarNaApi(FiltroMSC filtro, Integer exercicio, Integer mes) {

		List<String> listaCodigoIbge = getEnteService().obterListaCodigosIbge(filtro);
		TipoMatrizSaldoContabeis tipoMatriz = filtro.getTipoMatrizSaldoContabeis() != null
				? filtro.getTipoMatrizSaldoContabeis()
				: TipoMatrizSaldoContabeis.MSCC;

		List<MatrizSaldoContabeisOrcamentaria> listaMSC = new ArrayList<>();		
		for (String codigoIbge : listaCodigoIbge) {
			listaMSC.addAll(consultarNaApi(filtro, exercicio, mes, codigoIbge, tipoMatriz));
		}
		return listaMSC;
	}

	private List<MatrizSaldoContabeisOrcamentaria> consultarNaApi(FiltroMSC filtro, Integer exercicio, Integer mes,
			String codigoIbge, TipoMatrizSaldoContabeis tipoMatriz) {

		List<Integer> listaClassesConta = !filtro.isListaClassesContaVazia()?filtro.getListaClasseConta():CLASSES_CONTAS_ORCAMENTARIAS;
		
		List<MatrizSaldoContabeisOrcamentaria> listaMSC = new ArrayList<>();
		for (Integer classe: listaClassesConta) {
			listaMSC.addAll(consultarNaApi(filtro, exercicio, mes, codigoIbge, tipoMatriz, classe));
		}
		return listaMSC;
	}

	private List<MatrizSaldoContabeisOrcamentaria> consultarNaApi(FiltroMSC filtro, Integer exercicio, Integer mes,
			String codigoIbge, TipoMatrizSaldoContabeis tipoMatriz, Integer classeConta) {

		List<TipoValorMatrizSaldoContabeis> listaTipoValor = !filtro.isListaTipoValorVazia()
				? filtro.getListaTipoValor()
				: Arrays.asList(TipoValorMatrizSaldoContabeis.values());
		
		List<MatrizSaldoContabeisOrcamentaria> listaMSC = new ArrayList<>();
		for (TipoValorMatrizSaldoContabeis tipoValor: listaTipoValor) {
			listaMSC.addAll(consultarNaApi(exercicio, mes, codigoIbge, tipoMatriz, classeConta, tipoValor));
			aguardarUmSegundo();
		}
		return listaMSC;
	}

	public List<MatrizSaldoContabeisOrcamentaria> consultarNaApi(Integer exercicio, Integer mes, String codigoIbge,
			TipoMatrizSaldoContabeis tipoMatriz, Integer classeConta, TipoValorMatrizSaldoContabeis tipoValorMatriz) {

		List<MatrizSaldoContabeisOrcamentaria> listaMSC = null;
		try {

			MatrizSaldoContabeisOrcamentariaResponse mscResponse = obterResponseDaApi(exercicio, mes,
					codigoIbge, tipoMatriz, classeConta, tipoValorMatriz);

			listaMSC = mscResponse != null ? mscResponse.getItems()
					: new ArrayList<MatrizSaldoContabeisOrcamentaria>();
		} catch (Exception e) {
			logger.error("Erro para os parâmetros: exercicio: " + exercicio + ", mes: " + mes
					+ ", classeConta:" + classeConta 
					+ ", codigoTipoMatriz:" + tipoMatriz.getCodigo() + ", tipoValorMatriz: " + tipoValorMatriz.getCodigo() 
					+ ", codigoIbge: " + codigoIbge);
			e.printStackTrace();
			listaMSC = new ArrayList<>();
		}

		logger.debug("Tamanho da lista de MSC os parâmetros: exercicio: "+ exercicio + ", mes: " + mes
				+ ", classeConta:" + classeConta 
				+ ", codigoTipoMatriz:" + tipoMatriz.getCodigo() + ", tipoValorMatriz: " + tipoValorMatriz.getCodigo() 
				+ ", codigoIbge: " + codigoIbge + ": " + listaMSC.size());
		return listaMSC;
	}

	private MatrizSaldoContabeisOrcamentariaResponse obterResponseDaApi(Integer exercicio, Integer mes, String codigoIbge,
			TipoMatrizSaldoContabeis tipoMatriz, Integer classeConta, TipoValorMatrizSaldoContabeis tipoValorMatriz) {

		long ini = System.currentTimeMillis();

		this.webTarget = this.client.target(URL_SERVICE).path(API_PATH_MSC_ORCAMENTARIA)
				.queryParam(API_QUERY_PARAM_AN_REFERENCIA, exercicio)
				.queryParam(API_QUERY_PARAM_ME_REFERENCIA, mes)
				.queryParam(API_QUERY_PARAM_ID_ENTE, codigoIbge)
				.queryParam(API_QUERY_PARAM_CO_TIPO_MATRIZ, tipoMatriz.getCodigo())
				.queryParam(API_QUERY_PARAM_CLASSE_CONTA, classeConta)
				.queryParam(API_QUERY_PARAM_ID_TV, tipoValorMatriz.getCodigo());
		Invocation.Builder invocationBuilder = this.webTarget.request(API_RESPONSE_TYPE);
		logger.info("Get na API: " + this.webTarget.getUri().toString());
		Response response = invocationBuilder.get();
		MatrizSaldoContabeisOrcamentariaResponse mscResponse = response.readEntity(MatrizSaldoContabeisOrcamentariaResponse.class);
		
		long fim = System.currentTimeMillis();
		logger.debug("Tempo para consultar a MSC na API:" + (fim - ini));
		return mscResponse;
	}
	
	private EnteService getEnteService() {
		if(enteService == null) {
			enteService = new EnteService();
		}
		return enteService;
	}

	@Override
	protected String[] getColunasArquivoCSV() {
		return COLUNAS_ARQUIVO_CSV;
	}

	@Override
	protected Class<MatrizSaldoContabeisOrcamentaria> getClassType() {
		return MatrizSaldoContabeisOrcamentaria.class;
	}
	
	@Override
	protected String getNomePadraoArquivoCSV() {
		return NOME_PADRAO_ARQUIVO_CSV;
	}
}
