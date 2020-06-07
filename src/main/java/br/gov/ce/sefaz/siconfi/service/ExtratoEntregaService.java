package br.gov.ce.sefaz.siconfi.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.gov.ce.sefaz.siconfi.entity.ExtratoEntrega;
import br.gov.ce.sefaz.siconfi.opcoes.OpcoesCargaDadosExtratoEntrega;
import br.gov.ce.sefaz.siconfi.util.APIQueryParamUtil;
import br.gov.ce.sefaz.siconfi.util.Constantes;
import br.gov.ce.sefaz.siconfi.util.SiconfiResponse;
import br.gov.ce.sefaz.siconfi.util.Utils;

public class ExtratoEntregaService extends SiconfiService <ExtratoEntrega, OpcoesCargaDadosExtratoEntrega>{

	private static final Logger logger = LogManager.getLogger(ExtratoEntregaService.class);

	private static final String[] COLUNAS_ARQUIVO_CSV = new String[] { "exercicio", "cod_ibge", "populacao", "instituicao",
			"entregavel", "periodo", "periodicidade", "status_relatorio", "data_status", "forma_envio", "tipo_relatorio" };
	
	private static final String NOME_PADRAO_ARQUIVO_CSV = "extrato-entrega.csv";
	
	private static final String API_PATH_EXTRATO_ENTREGA = "extrato_entregas";

	private EnteService enteService;
	
	public ExtratoEntregaService () {
		super();
	}
	
	protected void excluir(OpcoesCargaDadosExtratoEntrega opcoes) {
		logger.info("Excluindo dados do banco de dados...");
		
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
		logger.info("Linhas excluídas:" + i);
	}
	
	protected void consultarNaApiESalvarArquivoCsv(OpcoesCargaDadosExtratoEntrega opcoes, Integer exercicio){
		
		List<String> listaCodigoIbge = getEnteService().obterListaCodigosIbgeNaAPI(opcoes);
		String nomearquivo = definirNomeArquivoCSV(opcoes);
		
		for (String codigoIbge : listaCodigoIbge) {
			APIQueryParamUtil apiQueryParamUtil = new APIQueryParamUtil().addParamAnReferencia(exercicio)
					.addParamIdEnte(codigoIbge);
			List<ExtratoEntrega> listaExtratosParcial = consultarNaApi(apiQueryParamUtil);
			salvarArquivoCsv(listaExtratosParcial, nomearquivo);
			aguardarUmSegundo();
		}
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
	protected List<ExtratoEntrega> lerEntidades(Response response){
		SiconfiResponse<ExtratoEntrega> extratoEntregaResponse = response
				.readEntity(new GenericType<SiconfiResponse<ExtratoEntrega>>() {
				});
		return extratoEntregaResponse != null ? extratoEntregaResponse.getItems() : new ArrayList<ExtratoEntrega>();
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
	protected Logger getLogger() {
		return logger;
	}

	@Override
	protected String getApiPath() {
		return API_PATH_EXTRATO_ENTREGA;
	}
}
