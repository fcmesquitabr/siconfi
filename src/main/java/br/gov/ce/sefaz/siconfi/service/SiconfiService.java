package br.gov.ce.sefaz.siconfi.service;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class SiconfiService <T> {

	private static final Logger logger = LogManager.getLogger(SiconfiService.class);
	
	public static final List<Integer> EXERCICIOS_DISPONIVEIS = Arrays.asList(2020);

	public static final List<Integer> MESES = Arrays.asList(1); //1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12

	public static final List<Integer> BIMESTRES = Arrays.asList(1); //1, 2, 3, 4, 5, 6
	
	public static final List<Integer> QUADRIMESTRES = Arrays.asList(1); //1, 2, 3
	
	public static final List<Integer> SEMESTRES = Arrays.asList(1, 2);

	public static final List<Integer> CLASSES_CONTAS_PATRIMONIAIS = Arrays.asList(1, 2, 3, 4);

	public static final List<Integer> CLASSES_CONTAS_ORCAMENTARIAS = Arrays.asList(5, 6);

	public static final List<Integer> CLASSES_CONTAS_CONTROLE = Arrays.asList(7, 8);

	protected Client client;
	 
	protected WebTarget webTarget;
 
	public static final String URL_SERVICE = "http://apidatalake.tesouro.gov.br/ords/siconfi/tt/";

	public static final String API_RESPONSE_TYPE = "application/json;charset=UTF-8";

	public static final String API_QUERY_PARAM_AN_REFERENCIA = "an_referencia";

	public static final String API_QUERY_PARAM_ME_REFERENCIA = "me_referencia";

	public static final String API_QUERY_PARAM_AN_EXERCICIO = "an_exercicio";

	public static final String API_QUERY_PARAM_ID_ENTE = "id_ente"; 

	public static final String API_QUERY_PARAM_NO_ANEXO = "no_anexo"; 

	public static final String API_QUERY_PARAM_IN_PERIODICIDADE = "in_periodicidade"; 

	public static final String API_QUERY_PARAM_NR_PERIODO = "nr_periodo"; 

	public static final String API_QUERY_PARAM_CO_TIPO_DEMONSTRATIVO = "co_tipo_demonstrativo"; 

	public static final String API_QUERY_PARAM_CO_TIPO_MATRIZ = "co_tipo_matriz"; 

	public static final String API_QUERY_PARAM_CLASSE_CONTA = "classe_conta"; 

	public static final String API_QUERY_PARAM_ID_TV = "id_tv"; 

	private EntityManagerFactory emf;
	
	private EntityManager em;
	
	public SiconfiService(){
		this.client = ClientBuilder.newClient();  		
	}
	
	public abstract List<T> consultarNaApi();

	protected abstract void excluirTodos();
	
	protected void exibirDadosNaConsole (List<T> listaEntidades) {
		for (T entidade: listaEntidades) {
			System.out.println(entidade.toString());
		}
	}

	protected void salvarNoBancoDeDados(List<T> listaEntidades) {
		getEntityManager().getTransaction().begin();		
		excluirTodos();
		persistir(listaEntidades);
		commitTransaction();
		fecharContextoPersistencia();		
	}

	protected EntityManager getEntityManager () {
		logger.debug("Criando EntityManager");
		if(em == null && getEntityManagerFactory()!=null) {
			em = getEntityManagerFactory().createEntityManager();
		}
		return em;
	}
	
	private EntityManagerFactory getEntityManagerFactory() {
		logger.debug("Criando EntityManagerFactory");
		if(this.emf == null) {
			this.emf = Persistence.createEntityManagerFactory("siconfiUnit");
		}
		return emf;
	}
	
	protected void fecharContextoPersistencia() {
		if(em!=null) {
			em.close();			
		}
		if(emf!=null) {
			emf.close();			
		}
	}
	
	protected void commitTransaction() {
		logger.debug("Fazendo commit...");
		long ini = System.currentTimeMillis();
		getEntityManager().getTransaction().commit();
		long fim = System.currentTimeMillis();
		logger.debug("Tempo para commit:" + (fim -ini));
	}

	protected void persistir(List<T> lista) {
		int i=1;
		logger.info("Persistindo os dados obtidos...");
		for(T entity: lista) {
			logger.debug("Inserindo registro " + (i++) + ":" + entity.toString());
			getEntityManager().persist(entity);
		}
	}
}
