package br.gov.ce.sefaz.siconfi.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.gov.ce.sefaz.siconfi.entity.MatrizSaldoContabeisPatrimonial;
import br.gov.ce.sefaz.siconfi.enums.TipoMatrizSaldoContabeis;
import br.gov.ce.sefaz.siconfi.enums.TipoValorMatrizSaldoContabeis;
import br.gov.ce.sefaz.siconfi.response.MatrizSaldoContabeisPatrimonialResponse;
import br.gov.ce.sefaz.siconfi.util.CsvUtil;
import br.gov.ce.sefaz.siconfi.util.FiltroMSC;
import br.gov.ce.sefaz.siconfi.util.Utils;

public class MSCService extends SiconfiService<MatrizSaldoContabeisPatrimonial>{

	private static final Logger logger = LogManager.getLogger(MSCService.class);

	private static String[] COLUNAS_ARQUIVO_CSV = new String[] { "exercicio", "mes_referencia", "cod_ibge", "poder_orgao", "tipo_matriz",
			"class_conta", "natureza_conta", "conta_contabil", "financeiro_permanente", "ano_fonte_recursos", "fonte_recursos", 
			"divida_consolidada", "data_referencia", "tipo_valor", "valorFormatado"};
	
	private static String NOME_PADRAO_ARQUIVO_CSV = "msc_patrimonial.csv";

	private static final String API_PATH_MSC_PATRIMONIAL = "msc_patrimonial";

	private EnteService enteService;

	public MSCService() {
		super();
	}

	public void carregarDados(FiltroMSC filtro) {
		
		List<MatrizSaldoContabeisPatrimonial> listaMSC = consultarNaApi(filtro);	
		
		switch (filtro.getOpcaoSalvamento()) {
		case CONSOLE:
			exibirDadosNaConsole(listaMSC);
			break;
		case ARQUIVO:
			String nomeArquivo = definirNomeArquivoCSV(filtro);
			salvarArquivoCsv(listaMSC, nomeArquivo);
			break;
		case BANCO:
			salvarNoBancoDeDados(filtro, listaMSC);
			break;
		}
	}

	private String definirNomeArquivoCSV(FiltroMSC filtro) {
		return (filtro.getNomeArquivo() != null && !filtro.getNomeArquivo().trim().isEmpty())
				? filtro.getNomeArquivo()
				: NOME_PADRAO_ARQUIVO_CSV;
	}

	protected void salvarArquivoCsv(List<MatrizSaldoContabeisPatrimonial> listaMSC, String nomeArquivo) {
		logger.info("Salvando dados no arquivo CSV...");
		CsvUtil<MatrizSaldoContabeisPatrimonial> csvUtil = new CsvUtil<>(MatrizSaldoContabeisPatrimonial.class);
		csvUtil.writeToCsvFile(listaMSC, COLUNAS_ARQUIVO_CSV, nomeArquivo);
	}

	protected void salvarNoBancoDeDados(FiltroMSC filtro, List<MatrizSaldoContabeisPatrimonial> listaEntidades) {
		if(Utils.isEmptyCollection(listaEntidades)) return;
		getEntityManager().getTransaction().begin();		
		excluirMatrizSaldosContabeis(filtro);
		persistir(listaEntidades);
		commitTransaction();
		fecharContextoPersistencia();		
	}

	private void excluirMatrizSaldosContabeis(FiltroMSC filtro) {
		logger.info("Excluindo dados do banco de dados...");
		
		StringBuilder queryBuilder = new StringBuilder("DELETE FROM MatrizSaldoContabeisPatrimonial mscp WHERE mscp.exercicio IN (:exercicios) ");

		List<String> listaCodigoIbge = getEnteService().obterListaCodigosIbge(filtro);
		if(!Utils.isEmptyCollection(listaCodigoIbge)) {
			queryBuilder.append(" AND mscp.cod_ibge IN (:codigosIbge)");
		}
		if(!filtro.isListaMesesVazia()) {
			queryBuilder.append(" AND mscp.mes_referencia IN (:listaMeses)");
		}
		if(filtro.getTipoMatrizSaldoContabeis()!=null){
			queryBuilder.append(" AND mscp.tipo_matriz=:codigoTipoMatriz");
		}
		if(!filtro.isListaClassesContaVazia()) {
			queryBuilder.append(" AND mscp.classe_conta IN (:listaClasseConta)");
		}
		if(!filtro.isListaTipoValorVazia()) {
			queryBuilder.append(" AND mscp.tipo_valor IN (:listaTipoValor)");
		}
		
		Query query = getEntityManager().createQuery(queryBuilder.toString());
		query.setParameter("exercicios", filtro.getExercicios());
		
		if(!Utils.isEmptyCollection(listaCodigoIbge)) {
			query.setParameter("codigosIbge", listaCodigoIbge);
		}
		if(!filtro.isListaMesesVazia()) {
			query.setParameter("listaMeses", filtro.getMeses());
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

	public void excluirTodos() {
		logger.info("Excluindo dados do banco de dados...");
		int i = getEntityManager().createQuery("DELETE FROM MatrizSaldoContabeisPatrimonial mscp")
				.executeUpdate();
		logger.info("Linhas excluídas:" + i);
	}

	public List<MatrizSaldoContabeisPatrimonial> consultarNaApi() {
		return new ArrayList<MatrizSaldoContabeisPatrimonial>();
	}

	public List<MatrizSaldoContabeisPatrimonial> consultarNaApi(FiltroMSC filtro) {

		List<Integer> listaExercicios = !filtro.isListaExerciciosVazia() ? filtro.getExercicios()
				: EXERCICIOS_DISPONIVEIS;
		List<MatrizSaldoContabeisPatrimonial> listaMSC = new ArrayList<>();

		for (Integer exercicio : listaExercicios) {
			listaMSC.addAll(consultarNaApi(filtro, exercicio));
		}
		return listaMSC;
	}

	private List<MatrizSaldoContabeisPatrimonial> consultarNaApi(FiltroMSC filtro, Integer exercicio) {

		List<Integer> listaMeses = !filtro.isListaMesesVazia() ? filtro.getMeses()
				: MESES;
		List<MatrizSaldoContabeisPatrimonial> listaMSC = new ArrayList<>();

		for (Integer mes: listaMeses) {
			listaMSC.addAll(consultarNaApi(filtro, exercicio, mes));
		}
		return listaMSC;
	}

	private List<MatrizSaldoContabeisPatrimonial> consultarNaApi(FiltroMSC filtro, Integer exercicio, Integer mes) {

		List<String> listaCodigoIbge = getEnteService().obterListaCodigosIbge(filtro);
		TipoMatrizSaldoContabeis tipoMatriz = filtro.getTipoMatrizSaldoContabeis() != null
				? filtro.getTipoMatrizSaldoContabeis()
				: TipoMatrizSaldoContabeis.MSCC;

		List<MatrizSaldoContabeisPatrimonial> listaRREO = new ArrayList<>();		
		for (String codigoIbge : listaCodigoIbge) {
			listaRREO.addAll(consultarNaApi(filtro, exercicio, mes, codigoIbge, tipoMatriz));
		}
		return listaRREO;
	}

	private List<MatrizSaldoContabeisPatrimonial> consultarNaApi(FiltroMSC filtro, Integer exercicio, Integer mes,
			String codigoIbge, TipoMatrizSaldoContabeis tipoMatriz) {

		List<Integer> listaClassesConta = !filtro.isListaClassesContaVazia()?filtro.getListaClasseConta():Arrays.asList(1,2,3,4);
		
		List<MatrizSaldoContabeisPatrimonial> listaMSC = new ArrayList<>();
		for (Integer classe: listaClassesConta) {
			listaMSC.addAll(consultarNaApi(filtro, exercicio, mes, codigoIbge, tipoMatriz, classe));
		}
		return listaMSC;
	}

	private List<MatrizSaldoContabeisPatrimonial> consultarNaApi(FiltroMSC filtro, Integer exercicio, Integer mes,
			String codigoIbge, TipoMatrizSaldoContabeis tipoMatriz, Integer classeConta) {

		List<TipoValorMatrizSaldoContabeis> listaTipoValor = !filtro.isListaTipoValorVazia()
				? filtro.getListaTipoValor()
				: Arrays.asList(TipoValorMatrizSaldoContabeis.values());
		
		List<MatrizSaldoContabeisPatrimonial> listaMSC = new ArrayList<>();
		for (TipoValorMatrizSaldoContabeis tipoValor: listaTipoValor) {
			listaMSC.addAll(consultarNaApi(exercicio, mes, codigoIbge, tipoMatriz, classeConta, tipoValor));
		}
		return listaMSC;
	}

	public List<MatrizSaldoContabeisPatrimonial> consultarNaApi(Integer exercicio, Integer mes, String codigoIbge,
			TipoMatrizSaldoContabeis tipoMatriz, Integer classeConta, TipoValorMatrizSaldoContabeis tipoValorMatriz) {

		List<MatrizSaldoContabeisPatrimonial> listaMSC = null;
		try {

			MatrizSaldoContabeisPatrimonialResponse mscResponse = obterResponseDaApi(exercicio, mes,
					codigoIbge, tipoMatriz, classeConta, tipoValorMatriz);

			listaMSC = mscResponse != null ? mscResponse.getItems()
					: new ArrayList<MatrizSaldoContabeisPatrimonial>();
		} catch (Exception e) {
			logger.error("Erro para os parâmetros: exercicio: " + exercicio + ", mes: " + mes
					+ ", classeConta:" + classeConta 
					+ ", codigoTipoMatriz:" + tipoMatriz.getCodigo() + ", tipoValorMatriz: " + tipoValorMatriz.getCodigo() 
					+ ", codigoIbge: " + codigoIbge);
			e.printStackTrace();
			listaMSC = new ArrayList<>();
		}

		logger.debug("Tamanho da lista de RREO os parâmetros: exercicio: "+ exercicio + ", mes: " + mes
				+ ", classeConta:" + classeConta 
				+ ", codigoTipoMatriz:" + tipoMatriz.getCodigo() + ", tipoValorMatriz: " + tipoValorMatriz.getCodigo() 
				+ ", codigoIbge: " + codigoIbge + ": " + listaMSC.size());
		return listaMSC;
	}

	private MatrizSaldoContabeisPatrimonialResponse obterResponseDaApi(Integer exercicio, Integer mes, String codigoIbge,
			TipoMatrizSaldoContabeis tipoMatriz, Integer classeConta, TipoValorMatrizSaldoContabeis tipoValorMatriz) {

		long ini = System.currentTimeMillis();

		this.webTarget = this.client.target(URL_SERVICE).path(API_PATH_MSC_PATRIMONIAL)
				.queryParam(API_QUERY_PARAM_AN_REFERENCIA, exercicio)
				.queryParam(API_QUERY_PARAM_ME_REFERENCIA, mes)
				.queryParam(API_QUERY_PARAM_ID_ENTE, codigoIbge)
				.queryParam(API_QUERY_PARAM_CO_TIPO_MATRIZ, tipoMatriz.getCodigo())
				.queryParam(API_QUERY_PARAM_CLASSE_CONTA, classeConta)
				.queryParam(API_QUERY_PARAM_ID_TV, tipoValorMatriz.getCodigo());
		Invocation.Builder invocationBuilder = this.webTarget.request(API_RESPONSE_TYPE);
		logger.info("Get na API: " + this.webTarget.getUri().toString());
		Response response = invocationBuilder.get();
		MatrizSaldoContabeisPatrimonialResponse mscResponse = response.readEntity(MatrizSaldoContabeisPatrimonialResponse.class);
		
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

}
