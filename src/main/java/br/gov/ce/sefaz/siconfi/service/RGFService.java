package br.gov.ce.sefaz.siconfi.service;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;

import org.apache.logging.log4j.Logger;

import br.gov.ce.sefaz.siconfi.entity.Ente;
import br.gov.ce.sefaz.siconfi.entity.RelatorioGestaoFiscal;
import br.gov.ce.sefaz.siconfi.enums.Periodicidade;
import br.gov.ce.sefaz.siconfi.enums.Poder;
import br.gov.ce.sefaz.siconfi.enums.TipoDemonstrativoRGF;
import br.gov.ce.sefaz.siconfi.opcoes.OpcoesCargaDadosRGF;
import br.gov.ce.sefaz.siconfi.util.APIQueryParamUtil;
import br.gov.ce.sefaz.siconfi.util.Constantes;
import br.gov.ce.sefaz.siconfi.util.LoggerUtil;
import br.gov.ce.sefaz.siconfi.util.Utils;

public class RGFService extends SiconfiService<RelatorioGestaoFiscal, OpcoesCargaDadosRGF> {

	private Logger logger = null;

	private static final long POPULACAO_MAXIMA_QUE_PODE_ENVIAR_RREO_SIMPLIFICADO = 50000;
	
	public static final List<String> ANEXOS_RGF = Arrays.asList("RGF-Anexo 01", "RGF-Anexo 02", "RGF-Anexo 03",
			"RGF-Anexo 04", "RGF-Anexo 05", "RGF-Anexo 06");

	private static final String[] COLUNAS_ARQUIVO_CSV = new String[] { "exercicio", "periodicidade", "periodo", "uf",
			"cod_ibge", "co_poder", "instituicao", "anexo", "cod_conta", "conta", "coluna", "rotulo", "populacao",
			"valorFormatado" };

	private static final String NOME_PADRAO_ARQUIVO_CSV = "rgf.csv";

	private static final String API_PATH_RGF = "rgf";

	private EnteService enteService;

	public RGFService() {
		super();
	}

	@Override
	public int excluir(OpcoesCargaDadosRGF filtro) {
		getLogger().info("Excluindo dados do banco de dados...");

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
		if (!filtro.isListaPoderesVazia()) {
			query.setParameter("listaPoderes", Poder.getListaCodigoPoder(filtro.getListaPoderes()));
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
	protected void consultarNaApiEGerarSaidaDados (OpcoesCargaDadosRGF opcoes, Integer exercicio) {

		List<Integer> listaQuadrimestres = !opcoes.isListaPeriodosVazia() ? opcoes.getPeriodos()
				: Constantes.QUADRIMESTRES;

		for (Integer quadrimestre : listaQuadrimestres) {
			consultarNaApiEGerarSaidaDados(opcoes, exercicio, quadrimestre);
		}
	}

	private void consultarNaApiEGerarSaidaDados(OpcoesCargaDadosRGF filtroRGF, Integer exercicio,
			Integer quadrimestre) {

		List<Ente> listaEntes = getEnteService().obterListaEntesNaAPI(filtroRGF);

		for (Ente ente: listaEntes) {
			consultarNaApiEGerarSaidaDados(filtroRGF, exercicio, quadrimestre, ente);
		}
	}

	private void consultarNaApiEGerarSaidaDados(OpcoesCargaDadosRGF filtroRGF, Integer exercicio, Integer quadrimestre,
			Ente ente) {

		List<Poder> listaPoder = !filtroRGF.isListaPoderesVazia() ? filtroRGF.getListaPoderes()
				: Arrays.asList(Poder.values());

		for (Poder poder : listaPoder) {
			consultarNaApiEGerarSaidaDados(filtroRGF, exercicio, quadrimestre, ente, poder);
		}
	}

	private void consultarNaApiEGerarSaidaDados(OpcoesCargaDadosRGF opcoes, Integer exercicio, Integer quadrimestre,
			Ente ente, Poder poder) {
		
		for(TipoDemonstrativoRGF tipoDemonstrativo: getListaTipoDemonstrativoASerConsiderado(ente)) {
			
			if (opcoes.isListaAnexosVazia()) {
				
				List<RelatorioGestaoFiscal> listaRGF = consultarNaApi(
						getAPIQueryParamUtil(exercicio, tipoDemonstrativo, quadrimestre, ente, poder, null));
				gerarSaidaDados(opcoes, listaRGF);
				
			} else {
				
				for (String anexo : opcoes.getListaAnexos()) {					
					List<RelatorioGestaoFiscal> listaRGFParcial = consultarNaApi(
							getAPIQueryParamUtil(exercicio, tipoDemonstrativo, quadrimestre, ente, poder, anexo));
					gerarSaidaDados(getOpcoesParcial(opcoes, exercicio, quadrimestre, ente.getCod_ibge(), poder, anexo),
							listaRGFParcial);
				}			
			}			
		}
	}

	private List<TipoDemonstrativoRGF> getListaTipoDemonstrativoASerConsiderado(Ente ente) {
		if(ente.isMunicipio() && ente.getPopulacao().longValue() < POPULACAO_MAXIMA_QUE_PODE_ENVIAR_RREO_SIMPLIFICADO) {
			return Arrays.asList(TipoDemonstrativoRGF.values());
		} else {
			return Arrays.asList(TipoDemonstrativoRGF.RGF);
		}
	}

	private APIQueryParamUtil getAPIQueryParamUtil(Integer exercicio, TipoDemonstrativoRGF tipoDemonstrativo,
			Integer quadrimestre, Ente ente, Poder poder, String anexo) {
		APIQueryParamUtil apiQueryParamUtil = new APIQueryParamUtil();
		apiQueryParamUtil.addParamAnExercicio(exercicio)
				.addParamIndicadorPeriodiciadade(tipoDemonstrativo.equals(TipoDemonstrativoRGF.RGF)
						? Periodicidade.QUADRIMESTRAL.getCodigo()
						: Periodicidade.SEMESTRAL
								.getCodigo())
				.addParamPeriodo(quadrimestre)
				.addParamIdEnte(ente.getCod_ibge())
				.addParamTipoDemonstrativo(tipoDemonstrativo.getCodigo())
				.addParamPoder(poder.getCodigo());
		
		if(!Utils.isStringVazia(anexo)) {
			apiQueryParamUtil.addParamAnexo(anexo);
		}
		return apiQueryParamUtil;
	}
		
	private OpcoesCargaDadosRGF getOpcoesParcial(OpcoesCargaDadosRGF opcoes, Integer exercicio, Integer quadrimestre, String codigoIbge,
			Poder poder, String anexo) {
		OpcoesCargaDadosRGF opcoesParcial = new OpcoesCargaDadosRGF();
		opcoesParcial.setOpcaoSalvamento(opcoes.getOpcaoSalvamento());
		opcoesParcial.setNomeArquivo(opcoes.getNomeArquivo());
		opcoesParcial.setExercicios(Arrays.asList(exercicio));
		opcoesParcial.setPeriodos(Arrays.asList(quadrimestre));
		opcoesParcial.setCodigosIBGE(Arrays.asList(codigoIbge));
		opcoesParcial.setListaPoderes(Arrays.asList(poder));
		if(anexo != null) {
			opcoesParcial.setListaAnexos(Arrays.asList(anexo));			
		}
		return opcoesParcial;
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
		if(logger == null) {
			logger = LoggerUtil.createLogger(RGFService.class);
		}
		return logger;
	}

	@Override
	protected String getApiPath() {
		return API_PATH_RGF;
	}
}
