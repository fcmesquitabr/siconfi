package br.gov.ce.sefaz.siconfi.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.gov.ce.sefaz.siconfi.entity.DeclaracaoContasAnuais;
import br.gov.ce.sefaz.siconfi.response.DeclaracaoContasAnuaisResponse;
import br.gov.ce.sefaz.siconfi.util.CsvUtil;
import br.gov.ce.sefaz.siconfi.util.FiltroDCA;
import br.gov.ce.sefaz.siconfi.util.Utils;

public class DCAService extends SiconfiService<DeclaracaoContasAnuais>{

	private static final Logger logger = LogManager.getLogger(DCAService.class);

	public static List<String> ANEXOS_DCA = Arrays.asList("Anexo I-AB", "Anexo I-C", "Anexo I-D", "Anexo I-E", 
			"Anexo I-F", "Anexo I-G", "Anexo I-HI", "DCA-Anexo I-AB", "DCA-Anexo I-C", "DCA-Anexo I-D", 
			"DCA-Anexo I-E", "DCA-Anexo I-F", "DCA-Anexo I-G", "DCA-Anexo I-HI");
	
	private static String[] COLUNAS_ARQUIVO_CSV = new String[] { "exercicio", "uf", "cod_ibge", "instituicao", "anexo",
			"cod_conta", "conta", "coluna", "rotulo", "populacao", "valorFormatado" };
	
	private static String NOME_PADRAO_ARQUIVO_CSV = "dca.csv";

	private EnteService enteService;

	public DCAService() {
		super();
	}

	public void carregarDados(FiltroDCA filtroDCA) {
		
		List<DeclaracaoContasAnuais> listaDCA = consultarNaApi(filtroDCA);	
		
		switch (filtroDCA.getOpcaoSalvamento()) {
		case CONSOLE:
			exibirDadosNaConsole(listaDCA);
			break;
		case ARQUIVO:
			String nomeArquivo = definirNomeArquivoCSV(filtroDCA);
			salvarArquivoCsv(listaDCA, nomeArquivo);
			break;
		case BANCO:
			salvarNoBancoDeDados(filtroDCA, listaDCA);
			break;
		}
	}

	private String definirNomeArquivoCSV(FiltroDCA filtroDCA) {
		return (filtroDCA.getNomeArquivo() != null && !filtroDCA.getNomeArquivo().trim().isEmpty())
				? filtroDCA.getNomeArquivo()
				: NOME_PADRAO_ARQUIVO_CSV;
	}

	protected void salvarArquivoCsv(List<DeclaracaoContasAnuais> listaDCA, String nomeArquivo) {
		logger.info("Salvando dados no arquivo CSV...");
		CsvUtil<DeclaracaoContasAnuais> csvUtil = new CsvUtil<DeclaracaoContasAnuais>(DeclaracaoContasAnuais.class);
		csvUtil.writeToCsvFile(listaDCA, COLUNAS_ARQUIVO_CSV, nomeArquivo);
	}

	protected void salvarNoBancoDeDados(FiltroDCA filtro, List<DeclaracaoContasAnuais> listaEntidades) {
		if(Utils.isEmptyCollection(listaEntidades)) return;
		getEntityManager().getTransaction().begin();		
		excluirDeclaracaoContasAnuais(filtro);
		persistir(listaEntidades);
		commitTransaction();
		fecharContextoPersistencia();		
	}

	private void excluirDeclaracaoContasAnuais(FiltroDCA filtro) {
		logger.info("Excluindo dados do banco de dados...");
		
		StringBuilder queryBuilder = new StringBuilder("DELETE FROM DeclaracaoContasAnuais dca WHERE dca.exercicio IN (:exercicios) ");
		if(filtro.isExisteCodigosIbge()) {
			queryBuilder.append(" AND dca.cod_ibge IN (:codigosIbge)");
		}
		if(!filtro.isListaAnexosVazia()) {
			queryBuilder.append(" AND dca.anexo IN (:listaAnexos)");
		}
		
		Query query = getEntityManager().createQuery(queryBuilder.toString());
		query.setParameter("exercicios", filtro.getExercicios());
		if(filtro.isExisteCodigosIbge()) {
			query.setParameter("codigosIbge", filtro.getCodigosIBGE());
		}
		if(!filtro.isListaAnexosVazia()) {
			query.setParameter("listaAnexos", filtro.getCodigosIBGE());
		}

		int i = query.executeUpdate();
		logger.info("Linhas excluídas:" + i);
	}

	public void excluirDCA(Integer exercicio) {
		logger.info("Excluindo dados do banco de dados...");		
		int i = getEntityManager().createQuery("DELETE FROM DeclaracaoContasAnuais dca WHERE dca.exercicio="+exercicio).executeUpdate();
		logger.info("Linhas excluídas:" + i);
	}

	protected void excluirTodos() {
		logger.info("Excluindo dados do banco de dados...");		
		int i = getEntityManager().createQuery("DELETE FROM DeclaracaoContasAnuais dca").executeUpdate();
		logger.info("Linhas excluídas:" + i);
	}

	public List<DeclaracaoContasAnuais> consultarNaApi(){
		return new ArrayList<DeclaracaoContasAnuais>();
	}

	public List<DeclaracaoContasAnuais> consultarNaApi(FiltroDCA filtroDCA){
		
		List<Integer> listaExercicios = (filtroDCA.getExercicios()!=null?filtroDCA.getExercicios():EXERCICIOS_DISPONIVEIS);
		List<String> listaAnexos = !filtroDCA.isListaAnexosVazia() ? filtroDCA.getListaAnexos() : ANEXOS_DCA;
		List<String> listaCodigoIbge = getEnteService().obterListaCodigosIbge(filtroDCA);
		
		List<DeclaracaoContasAnuais> listaDCA = new ArrayList<>();
		
		for (Integer exercicio: listaExercicios) {
			for(String codigoIbge: listaCodigoIbge) {
				for (String anexo: listaAnexos) {
					List<DeclaracaoContasAnuais> listaDCAParcial = consultarNaApi(exercicio,anexo, codigoIbge);	
					listaDCA.addAll(listaDCAParcial);											
				}
			}				
		}
		return listaDCA;
	}

	public List<DeclaracaoContasAnuais> consultarNaApi(Integer exercicio, String anexo, String codigoIbge) {
		
		List<DeclaracaoContasAnuais> listaDCA = null;
		
		try {

			DeclaracaoContasAnuaisResponse dcaResponse = obterResponseDaApi(exercicio, anexo, codigoIbge);
			listaDCA = dcaResponse != null ? dcaResponse.getItems() : new ArrayList<DeclaracaoContasAnuais>();

		} catch (Exception e) {
			logger.error("Erro para os parâmetros: exercicio: " + exercicio 
					+ ", anexo: " + anexo 
					+ ", codigoIbge: " + codigoIbge);
			e.printStackTrace();
			listaDCA = new ArrayList<>();
		}
		
		logger.debug("Tamanho da lista para os paramêtros: exercicio: " + exercicio
				+ ", anexo: " + anexo 
				+ ", codigoIbge: " + codigoIbge + ": " + listaDCA.size());
		return listaDCA;
	}

	private DeclaracaoContasAnuaisResponse obterResponseDaApi(Integer exercicio, String anexo, String codigoIbge) {

		long ini = System.currentTimeMillis();

		this.webTarget = this.client.target(URL_SERVICE).path("dca")
				.queryParam("an_exercicio", exercicio)
				.queryParam("no_anexo", anexo.replaceAll(" ", "%20"))
				.queryParam("id_ente", codigoIbge);
		Invocation.Builder invocationBuilder = this.webTarget.request("application/json;charset=UTF-8");
		logger.info("Get na API: " + this.webTarget.getUri().toString());
		Response response = invocationBuilder.get();
		DeclaracaoContasAnuaisResponse dcaResponse = response.readEntity(DeclaracaoContasAnuaisResponse.class);
		
		long fim = System.currentTimeMillis();
		logger.debug("Tempo para consultar as DCA na API:" + (fim - ini));
		return dcaResponse;
	}
	
	private EnteService getEnteService() {
		if(enteService == null) {
			enteService = new EnteService();
		}
		return enteService;
	}
}
