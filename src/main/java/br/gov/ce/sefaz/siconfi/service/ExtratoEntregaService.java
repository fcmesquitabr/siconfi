package br.gov.ce.sefaz.siconfi.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.logging.log4j.Logger;

import br.gov.ce.sefaz.siconfi.entity.ExtratoEntrega;
import br.gov.ce.sefaz.siconfi.enums.Entregavel;
import br.gov.ce.sefaz.siconfi.opcoes.OpcoesCargaDados;
import br.gov.ce.sefaz.siconfi.opcoes.OpcoesCargaDadosExtratoEntrega;
import br.gov.ce.sefaz.siconfi.util.APIQueryParamUtil;
import br.gov.ce.sefaz.siconfi.util.Constantes;
import br.gov.ce.sefaz.siconfi.util.LoggerUtil;
import br.gov.ce.sefaz.siconfi.util.Utils;

public class ExtratoEntregaService extends SiconfiService <ExtratoEntrega, OpcoesCargaDadosExtratoEntrega>{

	private static Logger logger = null;

	private static final String[] COLUNAS_ARQUIVO_CSV = new String[] { "exercicio", "cod_ibge", "populacao", "instituicao",
			"entregavel", "periodo", "periodicidade", "status_relatorio", "data_status", "forma_envio", "tipo_relatorio" };
	
	private static final String NOME_PADRAO_ARQUIVO_CSV = "extrato-entrega.csv";
	
	private static final String API_PATH_EXTRATO_ENTREGA = "extrato_entregas";

	private EnteService enteService;
	
	public ExtratoEntregaService () {
		super();
	}
	
	@Override
	protected int excluir(OpcoesCargaDadosExtratoEntrega opcoes) {
		getLogger().info("Excluindo dados do banco de dados...");
		
		List<String> listaCodigoIbge = getEnteService().obterListaCodigosIbgeNaAPI(opcoes);

		StringBuilder queryBuilder = new StringBuilder("DELETE FROM ExtratoEntrega ee WHERE ee.exercicio IN (:exercicios) ");
		if(!Utils.isEmptyCollection(listaCodigoIbge)) {
			queryBuilder.append(" AND ee.cod_ibge IN (:codigosIbge)");
		}
		
		Query query = getEntityManager().createQuery(queryBuilder.toString());
		query.setParameter("exercicios", opcoes.getExercicios());
		if(!Utils.isEmptyCollection(listaCodigoIbge)) {
			query.setParameter("codigosIbge", listaCodigoIbge);
		}

		int i = query.executeUpdate();
		getLogger().info("Linhas excluídas:" + i);
		return i;
	}

	@SuppressWarnings("unchecked")
	public List<ExtratoEntrega> consultarNaBase (OpcoesCargaDados opcoes, Integer exercicio, Entregavel entregavel) {
		getLogger().info("Consultando extrato entregas no banco de dados...");
		
		List<String> listaCodigoIbge = getEnteService().obterListaCodigosIbgeNaBase(opcoes);		
		
		StringBuilder queryBuilder = new StringBuilder("SELECT ee FROM ExtratoEntrega ee WHERE ee.status_relatorio='HO' AND ee.exercicio = :exercicio ");
		queryBuilder.append(" AND ee.data_status >= :dataMinima ");
		queryBuilder.append(" AND ee.entregavel = :entregavel");
		if(!Utils.isEmptyCollection(listaCodigoIbge)) {
			queryBuilder.append(" AND ee.cod_ibge IN (:codigosIbge) ");
		}

		if(!opcoes.isListaPeriodosVazia()) {
			queryBuilder.append(" AND ee.periodo IN (:periodos) ");
		}
		
		Query query = getEntityManager().createQuery(queryBuilder.toString());
		query.setParameter("exercicio", exercicio);
		query.setParameter("entregavel", entregavel.getDescricao());
		query.setParameter("dataMinima", getDataMinimaEntrega(opcoes));

		if(!Utils.isEmptyCollection(listaCodigoIbge)) {
			query.setParameter("codigosIbge", listaCodigoIbge);
		}

		if(!opcoes.isListaPeriodosVazia()) {
			query.setParameter("periodos", opcoes.getPeriodos());
		}

		List<ExtratoEntrega> listaExtratoEntrega = query.getResultList();
		getLogger().info("Tamanho da lista de extrato entrega:" + listaExtratoEntrega.size());		
		return listaExtratoEntrega;
	}

	private Date getDataMinimaEntrega(OpcoesCargaDados opcoes) {
		if(opcoes.getDataMinimaEntrega() != null) {
			return opcoes.getDataMinimaEntrega();
		}
		
		Calendar dataMinimaEntrega = Calendar.getInstance();
		dataMinimaEntrega.add(Calendar.MONTH, -1);
		return dataMinimaEntrega.getTime();
	}
	protected void consultarNaApiEGerarSaidaDados(OpcoesCargaDadosExtratoEntrega opcoes, Integer exercicio){
		
		List<String> listaCodigoIbge = getEnteService().obterListaCodigosIbgeNaAPI(opcoes);
		
		for (String codigoIbge : listaCodigoIbge) {
			APIQueryParamUtil apiQueryParamUtil = new APIQueryParamUtil().addParamAnReferencia(exercicio)
					.addParamIdEnte(codigoIbge);
			List<ExtratoEntrega> listaExtratosParcial = consultarNaApi(apiQueryParamUtil);
			gerarSaidaDados(getOpcoesParcial(opcoes, exercicio, codigoIbge), listaExtratosParcial);
			aguardarUmSegundo();
		}
	}

	private OpcoesCargaDadosExtratoEntrega getOpcoesParcial(OpcoesCargaDadosExtratoEntrega opcoes, Integer exercicio,
			String codigoIbge) {
		OpcoesCargaDadosExtratoEntrega opcoesParcial = new OpcoesCargaDadosExtratoEntrega();
		opcoesParcial.setOpcaoSalvamento(opcoes.getOpcaoSalvamento());
		opcoesParcial.setNomeArquivo(opcoes.getNomeArquivo());
		opcoesParcial.setExercicios(Arrays.asList(exercicio));
		opcoesParcial.setCodigosIBGE(Arrays.asList(codigoIbge));
		return opcoesParcial;
	}
	
	public List<ExtratoEntrega> consultarNaApi(OpcoesCargaDadosExtratoEntrega opcoes){
		
		List<Integer> listaExercicios = (opcoes.getExercicios()!=null?opcoes.getExercicios():Constantes.EXERCICIOS_DISPONIVEIS);
		
		List<ExtratoEntrega> listaExtratos = new ArrayList<>();				
		for (Integer exercicio : listaExercicios) {
			listaExtratos.addAll(consultarNaApiNoExercicio(opcoes, exercicio));
		}
		return listaExtratos;
	}

	private List<ExtratoEntrega> consultarNaApiNoExercicio(OpcoesCargaDadosExtratoEntrega opcoes, Integer exercicio) {

		List<String> listaCodigoIbge = getEnteService().obterListaCodigosIbgeNaAPI(opcoes);

		List<ExtratoEntrega> listaExtratos = new ArrayList<>();
		for (String codigoIbge : listaCodigoIbge) {
			APIQueryParamUtil apiQueryParamUtil = new APIQueryParamUtil().addParamAnReferencia(exercicio)
					.addParamIdEnte(codigoIbge);
			listaExtratos.addAll(consultarNaApi(apiQueryParamUtil));
			aguardarUmSegundo();
		}
		return listaExtratos;
	}
	
	private EnteService getEnteService() {
		if(enteService == null) {
			enteService = new EnteService();
		}
		return enteService;
	}
	
	@Override
	protected String getEntityName() {
		return ExtratoEntrega.class.getSimpleName();
	}

	@Override
	protected String[] getColunasArquivoCSV() {
		return COLUNAS_ARQUIVO_CSV;
	}

	@Override
	protected Class<ExtratoEntrega> getEntityClass() {
		return ExtratoEntrega.class;
	}

	@Override
	protected String getNomePadraoArquivoCSV() {
		return NOME_PADRAO_ARQUIVO_CSV;
	}	
	
	@Override
	protected String getApiPath() {
		return API_PATH_EXTRATO_ENTREGA;
	}
	
	@Override
	protected Logger getLogger() {
		if(logger == null) {
			logger = LoggerUtil.createLogger(ExtratoEntregaService.class);
		}
		return logger;
	}
}
