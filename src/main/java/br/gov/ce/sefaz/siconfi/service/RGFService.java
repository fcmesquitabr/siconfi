package br.gov.ce.sefaz.siconfi.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.gov.ce.sefaz.siconfi.entity.RelatorioGestaoFiscal;
import br.gov.ce.sefaz.siconfi.enums.Periodicidade;
import br.gov.ce.sefaz.siconfi.enums.Poder;
import br.gov.ce.sefaz.siconfi.enums.TipoDemonstrativoRGF;
import br.gov.ce.sefaz.siconfi.opcoes.OpcoesCargaDadosRGF;
import br.gov.ce.sefaz.siconfi.util.APIQueryParamUtil;
import br.gov.ce.sefaz.siconfi.util.Constantes;
import br.gov.ce.sefaz.siconfi.util.SiconfiResponse;
import br.gov.ce.sefaz.siconfi.util.Utils;

public class RGFService extends SiconfiService<RelatorioGestaoFiscal, OpcoesCargaDadosRGF> {

	private static final Logger logger = LogManager.getLogger(RGFService.class);

	public static List<String> ANEXOS_RGF = Arrays.asList("RGF-Anexo 01", "RGF-Anexo 02", "RGF-Anexo 03",
			"RGF-Anexo 04", "RGF-Anexo 05", "RGF-Anexo 06");

	private static String[] COLUNAS_ARQUIVO_CSV = new String[] { "exercicio", "periodicidade", "periodo", "uf",
			"cod_ibge", "cod_poder", "instituicao", "anexo", "cod_conta", "conta", "coluna", "rotulo", "populacao",
			"valorFormatado" };

	private static String NOME_PADRAO_ARQUIVO_CSV = "rgf.csv";

	private static final String API_PATH_RGF = "rgf";

	private EnteService enteService;

	public RGFService() {
		super();
	}

	public void carregarDados(OpcoesCargaDadosRGF filtroRGF) {

		List<RelatorioGestaoFiscal> listaRGF = consultarNaApi(filtroRGF);

		switch (filtroRGF.getOpcaoSalvamento()) {
		case CONSOLE:
			exibirDadosNaConsole(listaRGF);
			break;
		case ARQUIVO:
			escreverCabecalhoArquivoCsv(definirNomeArquivoCSV(filtroRGF));
			salvarArquivoCsv(listaRGF, definirNomeArquivoCSV(filtroRGF));
			break;
		case BANCO:
			salvarNoBancoDeDados(filtroRGF, listaRGF);
			break;
		}
	}

	protected void salvarNoBancoDeDados(OpcoesCargaDadosRGF filtro, List<RelatorioGestaoFiscal> listaEntidades) {
		if (listaEntidades == null || listaEntidades.isEmpty())
			return;
		getEntityManager().getTransaction().begin();
		excluirRelatorioGestaoFiscal(filtro);
		persistir(listaEntidades);
		commitTransaction();
		fecharContextoPersistencia();
	}

	public void excluirRGF(Integer exercicio) {
		logger.info("Excluindo dados do banco de dados...");
		int i = getEntityManager().createQuery("DELETE FROM RelatorioGestaoFiscal rgf WHERE rgf.exercicio=" + exercicio)
				.executeUpdate();
		logger.info("Linhas excluídas:" + i);
	}

	private void excluirRelatorioGestaoFiscal(OpcoesCargaDadosRGF filtro) {
		logger.info("Excluindo dados do banco de dados...");

		StringBuilder queryBuilder = new StringBuilder(
				"DELETE FROM RelatorioGestaoFiscal rgf WHERE rgf.exercicio IN (:exercicios) ");

		if (!filtro.isListaPeriodosVazia()) {
			queryBuilder.append(" AND rgf.periodo IN (:periodos)");
		}

		List<String> listaCodigoIbge = getEnteService().obterListaCodigosIbgeNaAPI(filtro);
		if (!Utils.isEmptyCollection(listaCodigoIbge)) {
			queryBuilder.append(" AND rgf.cod_ibge IN (:codigosIbge)");
		}

		if (!filtro.isListaPoderesVazia()) {
			queryBuilder.append(" AND rgf.co_poder IN (:listaPoderes)");
		}

		if (!filtro.isListaAnexosVazia()) {
			queryBuilder.append(" AND rgf.anexo IN (:listaAnexos)");
		}

		Query query = getEntityManager().createQuery(queryBuilder.toString());
		query.setParameter("exercicios", filtro.getExercicios());

		if (!filtro.isListaPeriodosVazia()) {
			query.setParameter("periodos", filtro.getPeriodos());
		}
		if (!Utils.isEmptyCollection(listaCodigoIbge)) {
			query.setParameter("codigosIbge", listaCodigoIbge);
		}
		if (!filtro.isListaPoderesVazia()) {
			query.setParameter("listaPoderes", Poder.getListaCodigoPoder(filtro.getListaPoderes()));
		}
		if (!filtro.isListaAnexosVazia()) {
			query.setParameter("listaAnexos", filtro.getListaAnexos());
		}

		int i = query.executeUpdate();
		logger.info("Linhas excluídas:" + i);
	}

	public List<RelatorioGestaoFiscal> consultarNaApi(OpcoesCargaDadosRGF filtroRGF) {

		List<Integer> listaExercicios = !filtroRGF.isListaExerciciosVazia() ? filtroRGF.getExercicios()
				: Constantes.EXERCICIOS_DISPONIVEIS;
		List<RelatorioGestaoFiscal> listaRGF = new ArrayList<>();

		for (Integer exercicio : listaExercicios) {
			listaRGF.addAll(consultarNaApi(filtroRGF, exercicio));
		}
		return listaRGF;
	}

	private List<RelatorioGestaoFiscal> consultarNaApi(OpcoesCargaDadosRGF filtroRGF, Integer exercicio) {

		List<Integer> listaQuadrimestres = !filtroRGF.isListaPeriodosVazia() ? filtroRGF.getPeriodos()
				: Constantes.QUADRIMESTRES;
		List<RelatorioGestaoFiscal> listaRGF = new ArrayList<>();

		for (Integer quadrimestre : listaQuadrimestres) {
			listaRGF.addAll(consultarNaApi(filtroRGF, exercicio, quadrimestre));
		}
		return listaRGF;
	}

	private List<RelatorioGestaoFiscal> consultarNaApi(OpcoesCargaDadosRGF filtroRGF, Integer exercicio, Integer quadrimestre) {

		List<String> listaCodigoIbge = getEnteService().obterListaCodigosIbgeNaAPI(filtroRGF);
		List<RelatorioGestaoFiscal> listaRGF = new ArrayList<>();

		for (String codigoIbge : listaCodigoIbge) {
			listaRGF.addAll(consultarNaApi(filtroRGF, exercicio, quadrimestre, codigoIbge));
		}
		return listaRGF;
	}

	private List<RelatorioGestaoFiscal> consultarNaApi(OpcoesCargaDadosRGF filtroRGF, Integer exercicio, Integer quadrimestre,
			String codigoIbge) {

		List<Poder> listaPoder = !filtroRGF.isListaPoderesVazia() ? filtroRGF.getListaPoderes()
				: Arrays.asList(Poder.values());
		List<RelatorioGestaoFiscal> listaRGF = new ArrayList<>();

		for (Poder poder : listaPoder) {
			listaRGF.addAll(consultarNaApi(filtroRGF, exercicio, quadrimestre, codigoIbge, poder));
		}
		return listaRGF;
	}

	private List<RelatorioGestaoFiscal> consultarNaApi(OpcoesCargaDadosRGF filtroRGF, Integer exercicio, Integer quadrimestre,
			String codigoIbge, Poder poder) {

		List<String> listaAnexos = !filtroRGF.isListaAnexosVazia() ? filtroRGF.getListaAnexos() : ANEXOS_RGF;
		List<RelatorioGestaoFiscal> listaRGF = new ArrayList<>();

		for (String anexo : listaAnexos) {

			APIQueryParamUtil apiQueryParamUtil = new APIQueryParamUtil();
			apiQueryParamUtil.addParamAnExercicio(exercicio)
					.addParamIndicadorPeriodiciadade(Periodicidade.QUADRIMESTRAL.getCodigo())
					.addParamPeriodo(quadrimestre)
					.addParamIdEnte(codigoIbge)
					.addParamTipoDemonstrativo(TipoDemonstrativoRGF.RGF.getCodigo())
					.addParamPoder(poder.getCodigo())
					.addParamAnexo(anexo);
			listaRGF.addAll(consultarNaApi(apiQueryParamUtil));
			aguardarUmSegundo();
		}
		return listaRGF;
	}

	@Override
	protected List<RelatorioGestaoFiscal> lerEntidades(Response response) {
		SiconfiResponse<RelatorioGestaoFiscal> rgfResponse = response
				.readEntity(new GenericType<SiconfiResponse<RelatorioGestaoFiscal>>() {
				});
		return rgfResponse != null ? rgfResponse.getItems() : new ArrayList<RelatorioGestaoFiscal>();
	}

	private EnteService getEnteService() {
		if (enteService == null) {
			enteService = new EnteService();
		}
		return enteService;
	}

	@Override
	protected String getEntityName() {
		return RelatorioGestaoFiscal.class.getSimpleName();
	}

	@Override
	protected String[] getColunasArquivoCSV() {
		return COLUNAS_ARQUIVO_CSV;
	}

	@Override
	protected Class<RelatorioGestaoFiscal> getEntityClass() {
		return RelatorioGestaoFiscal.class;
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
		return API_PATH_RGF;
	}
}
