package br.gov.ce.sefaz.siconfi.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.gov.ce.sefaz.siconfi.entity.RelatorioResumidoExecucaoOrcamentaria;
import br.gov.ce.sefaz.siconfi.enums.TipoDemonstrativoRREO;
import br.gov.ce.sefaz.siconfi.response.RelatorioResumidoExecucaoOrcamentariaResponse;
import br.gov.ce.sefaz.siconfi.util.FiltroRREO;
import br.gov.ce.sefaz.siconfi.util.Utils;

public class RREOService extends SiconfiService<RelatorioResumidoExecucaoOrcamentaria> {

	private static final Logger logger = LogManager.getLogger(RREOService.class);

	public static List<String> ANEXOS_RREO = Arrays.asList("RREO-Anexo 01", "RREO-Anexo 02", "RREO-Anexo 03",
			"RREO-Anexo 04", "RREO-Anexo 04 - RGPS", "RREO-Anexo 04 - RPPS", "RREO-Anexo 04.0 - RGPS",
			"RREO-Anexo 04.1", "RREO-Anexo 04.2", "RREO-Anexo 04.3 - RGPS", "RREO-Anexo 05", "RREO-Anexo 06",
			"RREO-Anexo 07", "RREO-Anexo 09", "RREO-Anexo 10 - RGPS", "RREO-Anexo 10 - RPPS", "RREO-Anexo 11",
			"RREO-Anexo 13", "RREO-Anexo 14");

	private static String[] COLUNAS_ARQUIVO_CSV = new String[] { "exercicio", "periodicidade", "periodo", "uf",
			"cod_ibge", "instituicao", "demonstrativo", "anexo", "cod_conta", "conta", "coluna", "rotulo", "populacao",
			"valorFormatado" };

	private EnteService enteService;

	private static String NOME_PADRAO_ARQUIVO_CSV = "rreo.csv";

	private static final String API_PATH_RREO = "rreo";

	public RREOService() {
		super();
	}

	public void carregarDados(FiltroRREO filtro) {

		List<RelatorioResumidoExecucaoOrcamentaria> listaRREO = consultarNaApi(filtro);

		switch (filtro.getOpcaoSalvamento()) {
		case CONSOLE:
			exibirDadosNaConsole(listaRREO);
			break;
		case ARQUIVO:
			String nomeArquivo = definirNomeArquivoCSV(filtro);
			escreverCabecalhoArquivoCsv(nomeArquivo);
			salvarArquivoCsv(listaRREO, nomeArquivo);
			break;
		case BANCO:
			salvarNoBancoDeDados(filtro, listaRREO);
			break;
		}
	}

	protected void salvarNoBancoDeDados(FiltroRREO filtro, List<RelatorioResumidoExecucaoOrcamentaria> listaEntidades) {
		if (listaEntidades == null || listaEntidades.isEmpty())
			return;
		getEntityManager().getTransaction().begin();
		excluirRelatorioResumidoExecucaoOrcamentaria(filtro);
		persistir(listaEntidades);
		commitTransaction();
		fecharContextoPersistencia();
	}

	private void excluirRelatorioResumidoExecucaoOrcamentaria(FiltroRREO filtro) {
		logger.info("Excluindo dados do banco de dados...");

		StringBuilder queryBuilder = new StringBuilder(
				"DELETE FROM RelatorioResumidoExecucaoOrcamentaria rreo WHERE rreo.exercicio IN (:exercicios) ");

		if (!filtro.isListaPeriodosVazia()) {
			queryBuilder.append(" AND rreo.periodo IN (:periodos)");
		}

		List<String> listaCodigoIbge = getEnteService().obterListaCodigosIbge(filtro);
		if (!Utils.isEmptyCollection(listaCodigoIbge)) {
			queryBuilder.append(" AND rreo.cod_ibge IN (:codigosIbge)");
		}

		if (!filtro.isListaAnexosVazia()) {
			queryBuilder.append(" AND rreo.anexo IN (:listaAnexos)");
		}

		Query query = getEntityManager().createQuery(queryBuilder.toString());
		query.setParameter("exercicios", filtro.getExercicios());

		if (!filtro.isListaPeriodosVazia()) {
			query.setParameter("periodos", filtro.getPeriodos());
		}
		if (!Utils.isEmptyCollection(listaCodigoIbge)) {
			query.setParameter("codigosIbge", listaCodigoIbge);
		}
		if (!filtro.isListaAnexosVazia()) {
			query.setParameter("listaAnexos", filtro.getListaAnexos());
		}

		int i = query.executeUpdate();
		logger.info("Linhas excluídas:" + i);
	}

	public void excluirRREO(Integer exercicio) {
		logger.info("Excluindo dados do banco de dados...");
		int i = getEntityManager()
				.createQuery("DELETE FROM RelatorioResumidoExecucaoOrcamentaria rreo WHERE rreo.exercicio=" + exercicio)
				.executeUpdate();
		logger.info("Linhas excluídas:" + i);
	}

	@Override
	public void excluirTodos() {
		logger.info("Excluindo dados do banco de dados...");
		int i = getEntityManager().createQuery("DELETE FROM RelatorioResumidoExecucaoOrcamentaria rreo")
				.executeUpdate();
		logger.info("Linhas excluídas:" + i);
	}

	public List<RelatorioResumidoExecucaoOrcamentaria> consultarNaApi() {
		return new ArrayList<RelatorioResumidoExecucaoOrcamentaria>();
	}

	public List<RelatorioResumidoExecucaoOrcamentaria> consultarNaApi(FiltroRREO filtro) {

		List<Integer> listaExercicios = !filtro.isListaExerciciosVazia() ? filtro.getExercicios()
				: EXERCICIOS_DISPONIVEIS;
		List<RelatorioResumidoExecucaoOrcamentaria> listaRREO = new ArrayList<>();

		for (Integer exercicio : listaExercicios) {
			listaRREO.addAll(consultarNaApi(filtro, exercicio));
		}
		return listaRREO;
	}

	private List<RelatorioResumidoExecucaoOrcamentaria> consultarNaApi(FiltroRREO filtro, Integer exercicio) {

		List<Integer> listaSemestres = !filtro.isListaPeriodosVazia() ? filtro.getPeriodos() : SEMESTRES;
		List<RelatorioResumidoExecucaoOrcamentaria> listaRREO = new ArrayList<>();

		for (Integer semestre: listaSemestres) {
			listaRREO.addAll(consultarNaApi(filtro, exercicio, semestre));
		}
		return listaRREO;
	}

	private List<RelatorioResumidoExecucaoOrcamentaria> consultarNaApi(FiltroRREO filtro, Integer exercicio, Integer semestre) {

		List<String> listaCodigoIbge = getEnteService().obterListaCodigosIbge(filtro);
		List<RelatorioResumidoExecucaoOrcamentaria> listaRREO = new ArrayList<>();

		for (String codigoIbge : listaCodigoIbge) {
			listaRREO.addAll(consultarNaApi(filtro, exercicio, semestre, codigoIbge));
		}
		return listaRREO;
	}

	private List<RelatorioResumidoExecucaoOrcamentaria> consultarNaApi(FiltroRREO filtro, Integer exercicio, Integer semestre,
			String codigoIbge) {

		List<String> listaAnexos = !filtro.isListaAnexosVazia() ? filtro.getListaAnexos() : ANEXOS_RREO;
		List<RelatorioResumidoExecucaoOrcamentaria> listaRREO = new ArrayList<>();

		for (String anexo : listaAnexos) {
			List<RelatorioResumidoExecucaoOrcamentaria> listaRREOParcial = consultarNaApi(exercicio,
					semestre, TipoDemonstrativoRREO.RREO.getCodigo(), anexo,
					codigoIbge);
			listaRREO.addAll(listaRREOParcial);
			aguardarUmSegundo();
		}
		return listaRREO;
	}

	public List<RelatorioResumidoExecucaoOrcamentaria> consultarNaApi(Integer exercicio, Integer periodo,
			String codigoTipoDemonstrativo, String anexo, String codigoIbge) {

		List<RelatorioResumidoExecucaoOrcamentaria> listaRREO = null;
		try {

			RelatorioResumidoExecucaoOrcamentariaResponse relatorioResponse = obterResponseDaApi(exercicio, periodo,
					codigoTipoDemonstrativo, anexo, codigoIbge);

			listaRREO = relatorioResponse != null ? relatorioResponse.getItems()
					: new ArrayList<RelatorioResumidoExecucaoOrcamentaria>();
		} catch (Exception e) {
			logger.error("Erro para os parâmetros: exercicio: " + exercicio + ", período: " + periodo
					+ ", codigoTipoDemonstrativo:" + codigoTipoDemonstrativo + ", anexo: " + anexo + ", codigoIbge: "
					+ codigoIbge);
			e.printStackTrace();
			listaRREO = new ArrayList<>();
		}

		logger.debug("Tamanho da lista de RREO os parâmetros: exercicio: " + exercicio + ", período: " + periodo
				+ ", codigoTipoDemonstrativo:" + codigoTipoDemonstrativo + ", anexo: " + anexo + ", codigoIbge: "
				+ codigoIbge + ": " + listaRREO.size());
		return listaRREO;
	}

	private RelatorioResumidoExecucaoOrcamentariaResponse obterResponseDaApi(Integer exercicio, Integer periodo,
			String codigoTipoDemonstrativo, String anexo, String codigoIbge) {

		long ini = System.currentTimeMillis();

		this.webTarget = this.client.target(URL_SERVICE).path(API_PATH_RREO)
				.queryParam(API_QUERY_PARAM_AN_EXERCICIO, exercicio)
				.queryParam(API_QUERY_PARAM_NR_PERIODO, periodo)
				.queryParam(API_QUERY_PARAM_CO_TIPO_DEMONSTRATIVO, codigoTipoDemonstrativo)
				.queryParam(API_QUERY_PARAM_NO_ANEXO, anexo.replaceAll(" ", "%20"))
				.queryParam(API_QUERY_PARAM_ID_ENTE, codigoIbge);
		Invocation.Builder invocationBuilder = this.webTarget.request(API_RESPONSE_TYPE);
		logger.info("Fazendo Get na API: " + this.webTarget.getUri().toString());
		Response response = invocationBuilder.get();
		RelatorioResumidoExecucaoOrcamentariaResponse relatorioResponse = response
				.readEntity(RelatorioResumidoExecucaoOrcamentariaResponse.class);

		long fim = System.currentTimeMillis();
		logger.debug("Tempo para consultar o RREO na API:" + (fim - ini));
		return relatorioResponse;
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
	protected Class<RelatorioResumidoExecucaoOrcamentaria> getClassType() {
		return RelatorioResumidoExecucaoOrcamentaria.class;
	}
	
	@Override
	protected String getNomePadraoArquivoCSV() {
		return NOME_PADRAO_ARQUIVO_CSV;
	}

}
