package br.gov.ce.sefaz.siconfi.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.gov.ce.sefaz.siconfi.entity.ExtratoEntrega;
import br.gov.ce.sefaz.siconfi.response.ExtratoEntregaResponse;
import br.gov.ce.sefaz.siconfi.util.CsvUtil;
import br.gov.ce.sefaz.siconfi.util.FiltroExtratoEntrega;
import br.gov.ce.sefaz.siconfi.util.Utils;

public class ExtratoEntregaService extends SiconfiService <ExtratoEntrega>{

	private static final Logger logger = LogManager.getLogger(ExtratoEntregaService.class);

	private static String[] COLUNAS_ARQUIVO_CSV = new String[] { "exercicio", "cod_ibge", "populacao", "instituicao",
			"entregavel", "periodo", "periodicidade", "status_relatorio", "data_status", "forma_envio", "tipo_relatorio" };
	
	private static String NOME_PADRAO_ARQUIVO_CSV = "D:\\extrato-entrega.csv";
	
	private EnteService enteService;
	
	public ExtratoEntregaService () {
		super();
	}
	
	public void carregarDados(FiltroExtratoEntrega filtroExtratoEntrega) {
		
		List<ExtratoEntrega> listaExtratoEntrega = consultarNaApi(filtroExtratoEntrega);	
		
		switch (filtroExtratoEntrega.getOpcaoSalvamento()) {
		case CONSOLE:
			exibirDadosNaConsole(listaExtratoEntrega);
			break;
		case ARQUIVO:
			String nomeArquivo = definirNomeArquivoCSV(filtroExtratoEntrega);
			salvarArquivoCsv(listaExtratoEntrega, nomeArquivo);
			break;
		case BANCO:
			salvarNoBancoDeDados(filtroExtratoEntrega, listaExtratoEntrega);
			break;
		}
	}

	private String definirNomeArquivoCSV(FiltroExtratoEntrega filtroExtratoEntrega) {
		return !filtroExtratoEntrega.isNomeArquivoVazio() ? filtroExtratoEntrega.getNomeArquivo()
						: NOME_PADRAO_ARQUIVO_CSV;
	}

	protected void salvarArquivoCsv(List<ExtratoEntrega> listaExtratoEntrega, String nomeArquivo) {
		logger.info("Salvando dados no arquivo CSV...");
		CsvUtil<ExtratoEntrega> csvUtil = new CsvUtil<ExtratoEntrega>(ExtratoEntrega.class);
		csvUtil.writeToCsvFile(listaExtratoEntrega, COLUNAS_ARQUIVO_CSV , NOME_PADRAO_ARQUIVO_CSV);
	}

	protected void salvarNoBancoDeDados(FiltroExtratoEntrega filtroExtratoEntrega, List<ExtratoEntrega> listaEntidades) {
		if(Utils.isEmptyCollection(listaEntidades)) return;
		
		getEntityManager().getTransaction().begin();		
		excluirExtratosEntrega(filtroExtratoEntrega);
		persistir(listaEntidades);
		commitTransaction();
		fecharContextoPersistencia();		
	}


	private void excluirExtratosEntrega(FiltroExtratoEntrega filtroExtratoEntrega) {
		logger.info("Excluindo dados do banco de dados...");
		
		StringBuilder queryBuilder = new StringBuilder("DELETE FROM ExtratoEntrega ee WHERE ee.exercicio IN (:exercicios) ");
		if(filtroExtratoEntrega.isExisteCodigosIbge()) {
			queryBuilder.append(" AND ee.cod_ibge IN (:codigosIbge)");
		}
		
		Query query = getEntityManager().createQuery(queryBuilder.toString());
		query.setParameter("exercicios", filtroExtratoEntrega.getExercicios());
		if(filtroExtratoEntrega.isExisteCodigosIbge()) {
			query.setParameter("codigosIbge", filtroExtratoEntrega.getCodigosIBGE());
		}

		int i = query.executeUpdate();
		logger.info("Linhas excluídas:" + i);
	}
	
	protected void excluirTodos() {
		logger.info("Excluindo dados do banco de dados...");
		int i = getEntityManager().createQuery("DELETE FROM ExtratoEntrega ee").executeUpdate();
		logger.info("Linhas excluídas:" + i);
	}

	public List<ExtratoEntrega> consultarNaApi(){
		return new ArrayList<ExtratoEntrega>();
	}

	public List<ExtratoEntrega> consultarNaApi(FiltroExtratoEntrega filtroExtratoEntrega){
		
		List<Integer> listaExercicios = (filtroExtratoEntrega.getExercicios()!=null?filtroExtratoEntrega.getExercicios():EXERCICIOS_DISPONIVEIS);
		List<String> listaCodigoIbge = getEnteService().obterListaCodigosIbge(filtroExtratoEntrega);
		
		List<ExtratoEntrega> listaExtratos = new ArrayList<>();				
		for (Integer exercicio : listaExercicios) {
			for (String codigoIbge : listaCodigoIbge) {
				List<ExtratoEntrega> listaExtratosParcial = consultarNaApi(exercicio, codigoIbge);
				listaExtratos.addAll(listaExtratosParcial);
			}
		}
		return listaExtratos;
	}

	public List<ExtratoEntrega> consultarNaApi (Integer exercicio, String codigoIbge) {

		List<ExtratoEntrega> listaExtratos = null;		
		try {
			
			ExtratoEntregaResponse extratoEntregaResponse = obterResponseDaApi(exercicio, codigoIbge);
			listaExtratos = extratoEntregaResponse != null ? extratoEntregaResponse.getItems() : new ArrayList<ExtratoEntrega>();
			
		} catch (Exception e) {
			e.printStackTrace();
			listaExtratos =  new ArrayList<>();
		}
		
		logger.debug("Tamanho da lista de extratos para o exercicio " + exercicio + ": " + listaExtratos.size());		
		return listaExtratos;
	}

	private ExtratoEntregaResponse obterResponseDaApi(Integer exercicio, String codigoIbge) {
		
		long ini = System.currentTimeMillis();
		
		this.webTarget = this.client.target(URL_SERVICE)
				.path("extrato_entregas")
				.queryParam("an_referencia", exercicio)
				.queryParam("id_ente", codigoIbge);
		Invocation.Builder invocationBuilder =  this.webTarget.request("application/json;charset=UTF-8"); 
		logger.info("Fazendo get na API: " + this.webTarget.getUri().toString());
		
		Response response = invocationBuilder.get();				
		ExtratoEntregaResponse extratoEntregaResponse = response.readEntity(ExtratoEntregaResponse.class);
		
		long fim = System.currentTimeMillis();			
		logger.debug("Tempo para consultar os entes na API:" + (fim -ini));	
		return extratoEntregaResponse;
	}
	
	private EnteService getEnteService() {
		if(enteService == null) {
			enteService = new EnteService();
		}
		return enteService;
	}
}
