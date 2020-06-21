package br.gov.ce.sefaz.siconfi.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import br.gov.ce.sefaz.siconfi.enums.OpcaoSalvamentoDados;
import br.gov.ce.sefaz.siconfi.opcoes.OpcoesCargaDados;
import br.gov.ce.sefaz.siconfi.util.APIQueryParamUtil;
import br.gov.ce.sefaz.siconfi.util.Constantes;
import br.gov.ce.sefaz.siconfi.util.CsvUtil;
import br.gov.ce.sefaz.siconfi.util.Utils;

public abstract class SiconfiService <T, O extends OpcoesCargaDados> {

	protected Client client;
	 
	protected WebTarget webTarget;
 
	public static final String URL_SERVICE = "http://apidatalake.tesouro.gov.br/ords/siconfi/tt/";

	public static final String API_RESPONSE_TYPE = "application/json;charset=UTF-8";

	private EntityManagerFactory emf;
	
	private EntityManager em;
	
	public SiconfiService(){
		ClientConfig clientConfig = new ClientConfig();
		clientConfig.register(JacksonJsonProvider.class);
		this.client = ClientBuilder.newClient(clientConfig);
	}
	
	protected abstract String getNomePadraoArquivoCSV();

	protected abstract String[] getColunasArquivoCSV();

	protected abstract Class<T> getEntityClass();

	protected abstract String getEntityName();

	protected abstract Logger getLogger();
	
	protected abstract List<T> lerEntidades(Response response);	

	protected abstract String getApiPath();
	
	protected abstract void excluir(O opcoes);

	public void carregarDados(O opcoes) {
		escreverCabecalhoArquivoCsv(opcoes);			
		consultarNaApiEGerarSaidaDados(opcoes);
		fecharContextoPersistencia();
	}

	protected void exibirDadosNaConsole (List<T> listaEntidades) {
		for (T entidade: listaEntidades) {
			System.out.println(entidade.toString());
		}
	}

	protected String definirNomeArquivoCSV(OpcoesCargaDados opcoes) {
		return !opcoes.isNomeArquivoVazio() ? opcoes.getNomeArquivo() : getNomePadraoArquivoCSV();
	}

	protected void escreverCabecalhoArquivoCsv(OpcoesCargaDados opcoes) { 
		if(OpcaoSalvamentoDados.ARQUIVO.equals(opcoes.getOpcaoSalvamento())) {
			getLogger().info("Escrevendo o cabeçalho no arquivo CSV...");
			CsvUtil<T> csvUtil = new CsvUtil<T>(getEntityClass());
			csvUtil.writeHeader(getColunasArquivoCSV(), definirNomeArquivoCSV(opcoes));
		}
	}

	protected void salvarArquivoCsv(List<T> listaObjetos, String nomeArquivo) {
		if(!Utils.isEmptyCollection(listaObjetos)) {
			
			getLogger().info("Salvando " + listaObjetos.size() + " registro(s) no arquivo CSV...");
			CsvUtil<T> csvUtil = new CsvUtil<T>(getEntityClass());
			csvUtil.writeToFile(listaObjetos, getColunasArquivoCSV(), nomeArquivo);
			
		} else {
			getLogger().info("Lista de registro fazia. Nada a salvar no arquivo CSV...");
		}
	}

	protected void consultarNaApiEGerarSaidaDados(O opcoes){
		for (Integer exercicio : getExercicios(opcoes)) {
			consultarNaApiEGerarSaidaDados(opcoes, exercicio);
		}
	}
	
	private List<Integer> getExercicios (O opcoes){
		return (opcoes.getExercicios() != null ? opcoes.getExercicios() : Constantes.EXERCICIOS_DISPONIVEIS);
	}
	
	protected void consultarNaApiEGerarSaidaDados(O opcoes, Integer exercicio) {	
		List<T> listaEntidades = consultarNaApi(new APIQueryParamUtil());
		gerarSaidaDados(opcoes, listaEntidades);			
	}
	
	protected void gerarSaidaDados(O opcoes, List<T> listaEntidades) {
		switch (opcoes.getOpcaoSalvamento()) {
		case CONSOLE:
			exibirDadosNaConsole(listaEntidades);
			break;
		case ARQUIVO:
			salvarArquivoCsv(listaEntidades, definirNomeArquivoCSV(opcoes));
			break;
		case BANCO:
			salvarNoBancoDeDados(opcoes, listaEntidades);
			break;
		}
	}

	/**
	 * Segundo documentação da API, existe o limite de 1 requisição por segundo
	 */
	protected void aguardarUmSegundo() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			getLogger().error(e);
		}
	}
		
	protected void salvarNoBancoDeDados(O opcoes, List<T> listaEntidades) {
		if(Utils.isEmptyCollection(listaEntidades)) return;
	
		getEntityManager().getTransaction().begin();		
		excluir(opcoes);
		persistir(listaEntidades);
		getEntityManager().flush();
		commitTransaction();
	}

	public void excluirTodos() {
		getLogger().info("Excluindo dados do banco de dados...");
		int i = getEntityManager().createQuery("DELETE FROM " + getEntityName()).executeUpdate();
		getLogger().info("Linhas excluídas:" + i);
	}

	protected EntityManager getEntityManager () {
		if(em == null && getEntityManagerFactory()!=null) {
			getLogger().debug("Criando EntityManager");
			em = getEntityManagerFactory().createEntityManager();
		}
		return em;
	}
	
	private EntityManagerFactory getEntityManagerFactory() {
		if(this.emf == null) {
			getLogger().debug("Criando EntityManagerFactory");
			this.emf = Persistence.createEntityManagerFactory("siconfiUnit");
		}
		return emf;
	}
	
	protected void fecharContextoPersistencia() {
		if (em != null) {
			em.close();
		}
		if (emf != null) {
			emf.close();
		}
	}
	
	protected void commitTransaction() {
		getLogger().info("Fazendo commit...");
		long ini = System.currentTimeMillis();
		getEntityManager().getTransaction().commit();
		long fim = System.currentTimeMillis();
		getLogger().info("Tempo para commit:" + (fim -ini));
	}

	protected void persistir(List<T> lista) {
		if(Utils.isEmptyCollection(lista)) {
			getLogger().info("Sem dados para persistir");
			return;
		}
		int i=1;
		getLogger().info("Persistindo os dados obtidos (" + lista.size() + " registro(s))...");
		for(T entity: lista) {
			getLogger().debug("Inserindo registro " + (i++) + ":" + entity.toString());
			entity = getEntityManager().merge(entity);
		}
	}

	public List<T> consultarNaApi (APIQueryParamUtil apiQueryParamUtil) {

		List<T> listaEntidades = null;		
		try {

			long ini = System.currentTimeMillis();			
			Response response = obterResponseAPI(apiQueryParamUtil);				
			listaEntidades = lerEntidades(response);
			mensagemTempoConsultaAPI(ini);	
			
		} catch (Exception e) {
			mensagemLogErroConsultaNaAPI(apiQueryParamUtil);
			e.printStackTrace();
			listaEntidades =  new ArrayList<>();
		}
		
		mensagemLogTamanhoDaLista(apiQueryParamUtil, listaEntidades);		
		return listaEntidades;
	}

	private void mensagemLogErroConsultaNaAPI(APIQueryParamUtil apiQueryParamUtil) {
		StringBuilder mensagemLog = new StringBuilder("Erro de consulta na API para os parâmetros:");
		apiQueryParamUtil.getMapQueryParam().forEach((chave, valor) -> mensagemLog.append(chave + ": " + valor.toString() + ", "));
		getLogger().info(mensagemLog.toString());
	}

	private Response obterResponseAPI(APIQueryParamUtil apiQueryParamUtil) {
		
		
		this.webTarget = this.client.target(URL_SERVICE).path(getApiPath());
		apiQueryParamUtil.getMapQueryParam().forEach((chave, valor) -> inserirAPIQueryParam(chave, valor));
		Invocation.Builder invocationBuilder =  this.webTarget.request(API_RESPONSE_TYPE); 	

		getLogger().info("Fazendo get na API: " + webTarget.getUri().toString());			
		Response response = invocationBuilder.get();
		
		return response;		
	}

	private void mensagemTempoConsultaAPI(long ini) {
		long fim = System.currentTimeMillis();			
		getLogger().debug("Tempo para consultar na API:" + (fim -ini));
	} 
	
	private void mensagemLogTamanhoDaLista(APIQueryParamUtil apiQueryParamUtil, List<T> listaEntidades) {
		StringBuilder mensagemLog = new StringBuilder("Tamanho da lista de entidades para os parâmetros ");
		apiQueryParamUtil.getMapQueryParam().forEach((chave, valor) -> mensagemLog.append(chave + ": " + valor.toString() + ", "));
		mensagemLog.append(": " + listaEntidades.size());
		getLogger().debug(mensagemLog.toString());
	}
	
	private void inserirAPIQueryParam(String chave, Object valor) {
		this.webTarget = this.webTarget.queryParam(chave, valor);
	}
}
