package br.gov.ce.sefaz.siconfi.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.gov.ce.sefaz.siconfi.entity.RelatorioResumidoExecucaoOrcamentaria;
import br.gov.ce.sefaz.siconfi.enums.TipoDemonstrativoRREO;
import br.gov.ce.sefaz.siconfi.opcoes.OpcoesCargaDadosRREO;
import br.gov.ce.sefaz.siconfi.util.APIQueryParamUtil;
import br.gov.ce.sefaz.siconfi.util.Constantes;
import br.gov.ce.sefaz.siconfi.util.SiconfiResponse;
import br.gov.ce.sefaz.siconfi.util.Utils;

public class RREOService extends SiconfiService<RelatorioResumidoExecucaoOrcamentaria, OpcoesCargaDadosRREO> {

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

	public void carregarDados(OpcoesCargaDadosRREO filtro) {

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

	protected void salvarNoBancoDeDados(OpcoesCargaDadosRREO filtro, List<RelatorioResumidoExecucaoOrcamentaria> listaEntidades) {
		if (listaEntidades == null || listaEntidades.isEmpty())
			return;
		getEntityManager().getTransaction().begin();
		excluirRelatorioResumidoExecucaoOrcamentaria(filtro);
		persistir(listaEntidades);
		commitTransaction();
		fecharContextoPersistencia();
	}

	private void excluirRelatorioResumidoExecucaoOrcamentaria(OpcoesCargaDadosRREO filtro) {
		logger.info("Excluindo dados do banco de dados...");

		StringBuilder queryBuilder = new StringBuilder(
				"DELETE FROM RelatorioResumidoExecucaoOrcamentaria rreo WHERE rreo.exercicio IN (:exercicios) ");

		if (!filtro.isListaPeriodosVazia()) {
			queryBuilder.append(" AND rreo.periodo IN (:periodos)");
		}

		List<String> listaCodigoIbge = getEnteService().obterListaCodigosIbgeNaAPI(filtro);
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

	public List<RelatorioResumidoExecucaoOrcamentaria> consultarNaApi(OpcoesCargaDadosRREO filtro) {

		List<Integer> listaExercicios = !filtro.isListaExerciciosVazia() ? filtro.getExercicios()
				: Constantes.EXERCICIOS_DISPONIVEIS;
		List<RelatorioResumidoExecucaoOrcamentaria> listaRREO = new ArrayList<>();

		for (Integer exercicio : listaExercicios) {
			listaRREO.addAll(consultarNaApi(filtro, exercicio));
		}
		return listaRREO;
	}

	private List<RelatorioResumidoExecucaoOrcamentaria> consultarNaApi(OpcoesCargaDadosRREO filtro, Integer exercicio) {

		List<Integer> listaSemestres = !filtro.isListaPeriodosVazia() ? filtro.getPeriodos() : Constantes.BIMESTRES;
		List<RelatorioResumidoExecucaoOrcamentaria> listaRREO = new ArrayList<>();

		for (Integer semestre: listaSemestres) {
			listaRREO.addAll(consultarNaApi(filtro, exercicio, semestre));
		}
		return listaRREO;
	}

	private List<RelatorioResumidoExecucaoOrcamentaria> consultarNaApi(OpcoesCargaDadosRREO filtro, Integer exercicio, Integer bimestre) {

		List<String> listaCodigoIbge = getEnteService().obterListaCodigosIbgeNaAPI(filtro);
		List<RelatorioResumidoExecucaoOrcamentaria> listaRREO = new ArrayList<>();

		for (String codigoIbge : listaCodigoIbge) {
			listaRREO.addAll(consultarNaApi(filtro, exercicio, bimestre, codigoIbge));
		}
		return listaRREO;
	}

	private List<RelatorioResumidoExecucaoOrcamentaria> consultarNaApi(OpcoesCargaDadosRREO filtro, Integer exercicio, Integer bimestre,
			String codigoIbge) {

		List<String> listaAnexos = !filtro.isListaAnexosVazia() ? filtro.getListaAnexos() : ANEXOS_RREO;
		List<RelatorioResumidoExecucaoOrcamentaria> listaRREO = new ArrayList<>();

		for (String anexo : listaAnexos) {
			APIQueryParamUtil apiQueryParamUtil = new APIQueryParamUtil();
			apiQueryParamUtil.addParamAnExercicio(exercicio)
					.addParamPeriodo(bimestre)
					.addParamIdEnte(codigoIbge)
					.addParamTipoDemonstrativo(TipoDemonstrativoRREO.RREO.getCodigo())
					.addParamAnexo(anexo);
			List<RelatorioResumidoExecucaoOrcamentaria> listaRREOParcial = consultarNaApi(apiQueryParamUtil);
			listaRREO.addAll(listaRREOParcial);
			aguardarUmSegundo();
		}
		return listaRREO;
	}

	@Override
	protected List<RelatorioResumidoExecucaoOrcamentaria> lerEntidades(Response response) {
		SiconfiResponse<RelatorioResumidoExecucaoOrcamentaria> rreoResponse = response
				.readEntity(new GenericType<SiconfiResponse<RelatorioResumidoExecucaoOrcamentaria>>() {
				});
		return rreoResponse != null ? rreoResponse.getItems() : new ArrayList<RelatorioResumidoExecucaoOrcamentaria>();
	}

	private EnteService getEnteService() {
		if(enteService == null) {
			enteService = new EnteService();
		}
		return enteService;
	}

	@Override
	protected String getEntityName() {
		return RelatorioResumidoExecucaoOrcamentaria.class.getSimpleName();
	}

	@Override
	protected String[] getColunasArquivoCSV() {
		return COLUNAS_ARQUIVO_CSV;
	}

	@Override
	protected Class<RelatorioResumidoExecucaoOrcamentaria> getEntityClass() {
		return RelatorioResumidoExecucaoOrcamentaria.class;
	}
	
	@Override
	protected String getNomePadraoArquivoCSV() {
		return NOME_PADRAO_ARQUIVO_CSV;
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Override
	protected String getApiPath() {
		return API_PATH_RREO;
	}
}
