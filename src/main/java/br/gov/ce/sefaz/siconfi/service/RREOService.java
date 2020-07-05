package br.gov.ce.sefaz.siconfi.service;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.gov.ce.sefaz.siconfi.entity.RelatorioResumidoExecucaoOrcamentaria;
import br.gov.ce.sefaz.siconfi.enums.TipoDemonstrativoRREO;
import br.gov.ce.sefaz.siconfi.opcoes.OpcoesCargaDadosRREO;
import br.gov.ce.sefaz.siconfi.util.APIQueryParamUtil;
import br.gov.ce.sefaz.siconfi.util.Constantes;
import br.gov.ce.sefaz.siconfi.util.Utils;

public class RREOService extends SiconfiService<RelatorioResumidoExecucaoOrcamentaria, OpcoesCargaDadosRREO> {

	private static final Logger logger = LogManager.getLogger(RREOService.class);

	public static final List<String> ANEXOS_RREO = Arrays.asList("RREO-Anexo 01", "RREO-Anexo 02", "RREO-Anexo 03",
			"RREO-Anexo 04", "RREO-Anexo 04 - RGPS", "RREO-Anexo 04 - RPPS", "RREO-Anexo 04.0 - RGPS",
			"RREO-Anexo 04.1", "RREO-Anexo 04.2", "RREO-Anexo 04.3 - RGPS", "RREO-Anexo 05", "RREO-Anexo 06",
			"RREO-Anexo 07", "RREO-Anexo 09", "RREO-Anexo 10 - RGPS", "RREO-Anexo 10 - RPPS", "RREO-Anexo 11",
			"RREO-Anexo 13", "RREO-Anexo 14");

	private static final String[] COLUNAS_ARQUIVO_CSV = new String[] { "exercicio", "periodicidade", "periodo", "uf",
			"cod_ibge", "instituicao", "demonstrativo", "anexo", "cod_conta", "conta", "coluna", "rotulo", "populacao",
			"valorFormatado" };

	private static final String NOME_PADRAO_ARQUIVO_CSV = "rreo.csv";
	
	private static final String API_PATH_RREO = "rreo";
	
	private EnteService enteService;

	public RREOService() {
		super();
	}

	@Override
	protected int excluir(OpcoesCargaDadosRREO filtro) {
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
		return i;
	}

	@Override
	protected void consultarNaApiEGerarSaidaDados (OpcoesCargaDadosRREO opcoes, Integer exercicio) {

		List<Integer> listaSemestres = !opcoes.isListaPeriodosVazia() ? opcoes.getPeriodos() : Constantes.BIMESTRES;

		for (Integer semestre: listaSemestres) {
			consultarNaApiEGerarSaidaDados(opcoes, exercicio, semestre);
		}
	}

	private void consultarNaApiEGerarSaidaDados(OpcoesCargaDadosRREO opcoes, Integer exercicio, Integer bimestre) {

		List<String> listaCodigoIbge = getEnteService().obterListaCodigosIbgeNaAPI(opcoes);

		for (String codigoIbge : listaCodigoIbge) {
			consultarNaApiEGerarSaidaDados(opcoes, exercicio, bimestre, codigoIbge);
		}
	}

	private void consultarNaApiEGerarSaidaDados(OpcoesCargaDadosRREO opcoes,
			Integer exercicio, Integer bimestre, String codigoIbge) {

		List<String> listaAnexos = !opcoes.isListaAnexosVazia() ? opcoes.getListaAnexos() : ANEXOS_RREO;

		for (String anexo : listaAnexos) {
			APIQueryParamUtil apiQueryParamUtil = new APIQueryParamUtil();
			apiQueryParamUtil.addParamAnExercicio(exercicio)
					.addParamPeriodo(bimestre)
					.addParamIdEnte(codigoIbge)
					.addParamTipoDemonstrativo(TipoDemonstrativoRREO.RREO.getCodigo())
					.addParamAnexo(anexo);
			List<RelatorioResumidoExecucaoOrcamentaria> listaRREOParcial = consultarNaApi(apiQueryParamUtil);
			gerarSaidaDados(getOpcoesParcial(opcoes, exercicio, bimestre, codigoIbge, anexo), listaRREOParcial);
			aguardarUmSegundo();
		}
	}

	private OpcoesCargaDadosRREO getOpcoesParcial(OpcoesCargaDadosRREO opcoes, Integer exercicio, Integer bimestre,
			String codigoIbge, String anexo) {
		OpcoesCargaDadosRREO opcoesParcial = new OpcoesCargaDadosRREO();
		opcoesParcial.setOpcaoSalvamento(opcoes.getOpcaoSalvamento());
		opcoesParcial.setNomeArquivo(opcoes.getNomeArquivo());
		opcoesParcial.setExercicios(Arrays.asList(exercicio));
		opcoesParcial.setPeriodos(Arrays.asList(bimestre));
		opcoesParcial.setCodigosIBGE(Arrays.asList(codigoIbge));
		opcoesParcial.setListaAnexos(Arrays.asList(anexo));
		return opcoesParcial;
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
