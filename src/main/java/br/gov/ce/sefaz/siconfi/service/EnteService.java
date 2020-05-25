package br.gov.ce.sefaz.siconfi.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.gov.ce.sefaz.siconfi.entity.Ente;
import br.gov.ce.sefaz.siconfi.enums.OpcaoSalvamentoDados;
import br.gov.ce.sefaz.siconfi.response.EnteResponse;
import br.gov.ce.sefaz.siconfi.util.CsvUtil;

public class EnteService extends SiconfiService <Ente>{

	private static final Logger logger = LogManager.getLogger(EnteService.class);
	
	public EnteService() {
		super();
	}
	
	@SuppressWarnings("unchecked")
	public List<Ente> consultarEntesNaBase(List<String> listaEsfera){
		logger.debug("Consultando a base com parâmetro: " + listaEsfera);
		Query query = getEntityManager().createQuery("SELECT e FROM Ente e WHERE e.esfera IN (:listaEsfera) ORDER BY e.ente");
		query.setParameter("listaEsfera", listaEsfera);
		return query.getResultList();		 
	}

	public void carregarDados(OpcaoSalvamentoDados opcaoSalvamento) {
		
		List<Ente> listaEntes = consultarNaApi();	
		
		switch (opcaoSalvamento) {
		case CONSOLE:
			exibirDadosNaConsole(listaEntes);
			break;
		case ARQUIVO:
			salvarArquivoCsv(listaEntes);
			break;
		case BANCO:
			salvarNoBancoDeDados(listaEntes);
			break;
		}
	}

	private void salvarArquivoCsv(List<Ente> listaEntes) {
		logger.info("Salvando dados no arquivo CSV...");
		CsvUtil<Ente> csvUtil = new CsvUtil<>(Ente.class);
		csvUtil.writeToCsvFile(listaEntes, new String[]{"cod_ibge","ente","capital","regiao","uf","esfera","exercicio","populacao","cnpj"} , "D:\\entes.csv");
	}
	
	protected void excluirTodos() {
		logger.info("Excluindo dados do banco de dados...");
		int i = getEntityManager().createQuery("DELETE FROM Ente e").executeUpdate();
		logger.info("Linhas excluídas:" + i);
	}
	
	public List<Ente> consultarNaApi(){
		
		List<Ente> listaEntes;
		try {
			
			EnteResponse enteResponse = obterResponseDaApi();
			listaEntes = enteResponse!=null?enteResponse.getItems():new ArrayList<Ente>();	
			
		} catch(Exception e) {
			logger.error(e);
			listaEntes = new ArrayList<>();
		}
		
		logger.debug("Tamanho da lista de entes:" + listaEntes.size());
		return listaEntes;
	}

	private EnteResponse obterResponseDaApi() {
		long ini = System.currentTimeMillis();
		
		this.webTarget = this.client.target(URL_SERVICE).path("entes");
		Invocation.Builder invocationBuilder =  this.webTarget.request("application/json;charset=UTF-8"); 
		logger.info("Fazendo get na API: " + this.webTarget.getUri().toString());
		Response response = invocationBuilder.get();			
		EnteResponse enteResponse = response.readEntity(EnteResponse.class);
		
		long fim = System.currentTimeMillis();			
		logger.debug("Tempo para consultar os entes na API:" + (fim -ini));
		return enteResponse;
	}
}
