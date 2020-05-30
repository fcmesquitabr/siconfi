package br.gov.ce.sefaz.siconfi.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.gov.ce.sefaz.siconfi.entity.Ente;
import br.gov.ce.sefaz.siconfi.enums.Esfera;
import br.gov.ce.sefaz.siconfi.enums.OpcaoSalvamentoDados;
import br.gov.ce.sefaz.siconfi.response.EnteResponse;
import br.gov.ce.sefaz.siconfi.util.FiltroBase;
import br.gov.ce.sefaz.siconfi.util.Utils;

public class EnteService extends SiconfiService <Ente>{

	private static final Logger logger = LogManager.getLogger(EnteService.class);
	
	private static String[] COLUNAS_ARQUIVO_CSV = new String[]{"cod_ibge","ente","capital","regiao","uf","esfera","exercicio","populacao","cnpj"};
	
	private static String NOME_PADRAO_ARQUIVO_CSV = "entes.csv";
	
	private static String API_PATH_ENTES = "entes";
	
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

	public void carregarDados(OpcaoSalvamentoDados opcaoSalvamento, String nomeArquivo) {
		
		List<Ente> listaEntes = consultarNaApi();	
		
		switch (opcaoSalvamento) {
		case CONSOLE:
			exibirDadosNaConsole(listaEntes);
			break;
		case ARQUIVO:
			escreverCabecalhoArquivoCsv(definirNomeArquivoCSV(nomeArquivo));
			salvarArquivoCsv(listaEntes, definirNomeArquivoCSV(nomeArquivo));
			break;
		case BANCO:
			salvarNoBancoDeDados(listaEntes);
			break;
		}
	}

	private String definirNomeArquivoCSV(String nomeArquivo) {
		return !Utils.isStringVazia(nomeArquivo) ? nomeArquivo : NOME_PADRAO_ARQUIVO_CSV;
	}

	@Override
	public void excluirTodos() {
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
		
		this.webTarget = this.client.target(URL_SERVICE).path(API_PATH_ENTES);
		Invocation.Builder invocationBuilder =  this.webTarget.request(API_RESPONSE_TYPE); 
		
		logger.info("Fazendo get na API: " + this.webTarget.getUri().toString());
		Response response = invocationBuilder.get();
		EnteResponse enteResponse = response.readEntity(EnteResponse.class);
		
		long fim = System.currentTimeMillis();			
		logger.debug("Tempo para consultar os entes na API:" + (fim -ini));
		return enteResponse;
	}
	
	public List<String> obterListaCodigosIbge(FiltroBase filtro) {
		List<String> listaCodigoIbge = null;
		
		if(filtro.isExisteCodigosIbge()) {			
			listaCodigoIbge = filtro.getCodigosIBGE();
		} else {			
			listaCodigoIbge = obterListaCodigoIbgePelaEsfera(filtro);
		}
		return listaCodigoIbge;
	}

	private List<String> obterListaCodigoIbgePelaEsfera(FiltroBase filtro) {
		
		List<Ente> listaEntes = null;
	
		if(filtro.getEsfera() == null || filtro.getEsfera().equals(Esfera.ESTADOS_E_DISTRITO_FEDERAL)) {
			listaEntes = consultarEntesNaBase(Arrays.asList(Esfera.ESTADO.getCodigo(),Esfera.DISTRITO_FEDERAL.getCodigo()));				
		} else {
			listaEntes = consultarEntesNaBase(Arrays.asList(filtro.getEsfera().getCodigo()));
		}
		
		return obterListaCodigoIbge(listaEntes);
	}
	
	private List<String> obterListaCodigoIbge (List<Ente> listaEntes){
		List<String> listaCodigoIbge = new ArrayList<String>();		
		for(Ente ente: listaEntes) {
			listaCodigoIbge.add(ente.getCod_ibge());
		}
		return listaCodigoIbge;
	}

	@Override
	protected String[] getColunasArquivoCSV() {
		return COLUNAS_ARQUIVO_CSV;
	}

	@Override
	protected Class<Ente> getClassType() {
		return Ente.class;
	}
	
	@Override
	protected String getNomePadraoArquivoCSV() {
		return NOME_PADRAO_ARQUIVO_CSV;
	}
}
