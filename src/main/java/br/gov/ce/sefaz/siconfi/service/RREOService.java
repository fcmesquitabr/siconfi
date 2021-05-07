package br.gov.ce.sefaz.siconfi.service;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;

import org.apache.logging.log4j.Logger;

import br.gov.ce.sefaz.siconfi.entity.Ente;
import br.gov.ce.sefaz.siconfi.entity.RelatorioResumidoExecucaoOrcamentaria;
import br.gov.ce.sefaz.siconfi.enums.TipoDemonstrativoRREO;
import br.gov.ce.sefaz.siconfi.opcoes.OpcoesCargaDadosRREO;
import br.gov.ce.sefaz.siconfi.util.APIQueryParamUtil;
import br.gov.ce.sefaz.siconfi.util.Constantes;
import br.gov.ce.sefaz.siconfi.util.LoggerUtil;
import br.gov.ce.sefaz.siconfi.util.Utils;

public class RREOService extends SiconfiService<RelatorioResumidoExecucaoOrcamentaria, OpcoesCargaDadosRREO> {

	private Logger logger = null;

	private static final long POPULACAO_MAXIMA_QUE_PODE_ENVIAR_RREO_SIMPLIFICADO = 50000;
	
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
	public int excluir(OpcoesCargaDadosRREO filtro) {
		getLogger().info("Excluindo dados do banco de dados...");

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

		boolean transacaoAtiva = getEntityManager().getTransaction().isActive(); 
		if(!transacaoAtiva) {
			getEntityManager().getTransaction().begin();			
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
		getLogger().info("Linhas excluídas: {}", i);
		
		if(!transacaoAtiva) {
			getEntityManager().getTransaction().commit();			
		}		

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

		List<Ente> listaEntes = getEnteService().obterListaEntesNaAPI(opcoes);
		
		for (Ente ente: listaEntes) {
			consultarNaApiEGerarSaidaDados(opcoes, exercicio, bimestre, ente);
		}
	}

	private void consultarNaApiEGerarSaidaDados(OpcoesCargaDadosRREO opcoes,
			Integer exercicio, Integer bimestre, Ente ente) {

		 		
		for(TipoDemonstrativoRREO tipoDemonstrativo: getListaTipoDemonstrativoASerConsiderado(ente)) {
			
			if (opcoes.isListaAnexosVazia()) {
				List<RelatorioResumidoExecucaoOrcamentaria> listaRREO = consultarNaApi(
						getAPIQueryParamUtil(exercicio, bimestre, ente, tipoDemonstrativo, null));
				gerarSaidaDados(opcoes, listaRREO);

			} else {
				for (String anexo : opcoes.getListaAnexos()) {
					List<RelatorioResumidoExecucaoOrcamentaria> listaRREOParcial = consultarNaApi(
							getAPIQueryParamUtil(exercicio, bimestre, ente, tipoDemonstrativo, anexo));
					gerarSaidaDados(getOpcoesParcial(opcoes, exercicio, bimestre, ente.getCod_ibge(), anexo),
							listaRREOParcial);
				}
			}
		}
	}

	private List<TipoDemonstrativoRREO> getListaTipoDemonstrativoASerConsiderado(Ente ente) {
		if(ente.isMunicipio() && ente.getPopulacao().longValue() < POPULACAO_MAXIMA_QUE_PODE_ENVIAR_RREO_SIMPLIFICADO) {
			return Arrays.asList(TipoDemonstrativoRREO.values());
		} else {
			return Arrays.asList(TipoDemonstrativoRREO.RREO);
		}
	}

	private APIQueryParamUtil getAPIQueryParamUtil(Integer exercicio, Integer bimestre, Ente ente,
			TipoDemonstrativoRREO tipoDemonstrativo, String anexo) {
		APIQueryParamUtil apiQueryParamUtil = new APIQueryParamUtil();
		apiQueryParamUtil.addParamAnExercicio(exercicio)
				.addParamPeriodo(bimestre)
				.addParamIdEnte(ente.getCod_ibge())
				.addParamTipoDemonstrativo(tipoDemonstrativo.getCodigo());
		
		if (!Utils.isStringVazia(anexo)) {
			apiQueryParamUtil.addParamAnexo(anexo);
		}
		return apiQueryParamUtil;
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
		if(logger == null) {
			logger = LoggerUtil.createLogger(RREOService.class);
		}
		return logger;
	}

	@Override
	protected String getApiPath() {
		return API_PATH_RREO;
	}
}
