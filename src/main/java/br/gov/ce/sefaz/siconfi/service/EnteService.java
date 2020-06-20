package br.gov.ce.sefaz.siconfi.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Query;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.gov.ce.sefaz.siconfi.entity.Ente;
import br.gov.ce.sefaz.siconfi.enums.Esfera;
import br.gov.ce.sefaz.siconfi.opcoes.OpcoesCargaDados;
import br.gov.ce.sefaz.siconfi.util.APIQueryParamUtil;
import br.gov.ce.sefaz.siconfi.util.SiconfiResponse;

public class EnteService extends SiconfiService <Ente, OpcoesCargaDados>{

	private static final Logger logger = LogManager.getLogger(EnteService.class);
	
	private static final String[] COLUNAS_ARQUIVO_CSV = new String[] { "cod_ibge", "ente", "capital", "regiao", "uf",
			"esfera", "exercicio", "populacao", "cnpj" };
	
	private static final String NOME_PADRAO_ARQUIVO_CSV = "entes.csv";
	
	private static final String API_PATH_ENTES = "entes";
	
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

	public List<Ente> consultarEntesNaAPI(){
		logger.debug("Consultando a API...");
		List<Ente> listaTodosEntes = consultarNaApi(new APIQueryParamUtil());
		return listaTodosEntes;		 
	}

	protected void consultarNaApiEGerarSaidaDados(OpcoesCargaDados opcoes){
		List<Ente> listaEntes = obterListaEntesNaAPI(opcoes);
		gerarSaidaDados(opcoes, listaEntes);
	}

	@Override
	protected List<Ente> lerEntidades(Response response) {
		SiconfiResponse<Ente> enteResponse = response
				.readEntity(new GenericType<SiconfiResponse<Ente>>() {
				});
		return enteResponse != null ? enteResponse.getItems() : new ArrayList<Ente>();
	}

	public List<String> obterListaCodigosIbgeNaBase(OpcoesCargaDados opcoes) {
		List<String> listaCodigoIbge = null;
		
		if(opcoes.isExisteCodigosIbge()) {			
			listaCodigoIbge = opcoes.getCodigosIBGE();
		} else {			
			listaCodigoIbge = obterListaCodigoIbgePelaEsferaNaBase(opcoes);
		}
		return listaCodigoIbge;
	}

	public List<Ente> obterListaEntesNaAPI(OpcoesCargaDados opcoes) {
				
		List<Ente> listaEntes = consultarEntesNaAPI();		
		listaEntes = filtrarEsfera(listaEntes, opcoes);
		listaEntes = filtrarCodigoIbge(listaEntes, opcoes);
		listaEntes = filtrarCodigoUF(listaEntes, opcoes);
		listaEntes = filtrarCapital(listaEntes, opcoes);
		listaEntes = filtrarPopulacaoMinima(listaEntes, opcoes);
		listaEntes = filtrarPopulacaoMaxima(listaEntes, opcoes);
		
		return listaEntes;
	}

	public List<String> obterListaCodigosIbgeNaAPI(OpcoesCargaDados opcoes) {
		
		if(opcoes.isExisteCodigosIbge()) {
			return opcoes.getCodigosIBGE();
		} 
				
		return obterListaCodigoIbge(obterListaEntesNaAPI(opcoes));
	}

	private List<Ente> filtrarEsfera (List<Ente> listaEntes, OpcoesCargaDados opcoes){
		final List<String> listaCodigoEsfera = (opcoes.getEsfera() == null
				|| opcoes.getEsfera().equals(Esfera.ESTADOS_E_DISTRITO_FEDERAL))
						? Arrays.asList(Esfera.ESTADO.getCodigo(), Esfera.DISTRITO_FEDERAL.getCodigo())
						: Arrays.asList(opcoes.getEsfera().getCodigo());
		
		return listaEntes.parallelStream().filter(ente -> listaCodigoEsfera.contains(ente.getEsfera()))
				.collect(Collectors.toList());	
	}

	private List<Ente> filtrarCodigoIbge(List<Ente> listaEntes, OpcoesCargaDados opcoes) {
		if (opcoes.isExisteCodigosIbge()) {
			return listaEntes.parallelStream().filter(ente -> opcoes.getCodigosIBGE().contains(ente.getCod_ibge()))
					.collect(Collectors.toList());
		} else {
			return listaEntes;
		}
	}

	private List<Ente> filtrarCodigoUF(List<Ente> listaEntes, OpcoesCargaDados opcoes) {
		if (!opcoes.isListaCodigosUfVazia()) {
			return listaEntes.parallelStream().filter(ente -> opcoes.getCodigosUF().contains(ente.getUf()))
					.collect(Collectors.toList());
		} else {
			return listaEntes;
		}
	}

	private List<Ente> filtrarCapital(List<Ente> listaEntes, OpcoesCargaDados opcoes) {
		if(opcoes.getCapital() != null) {
			return listaEntes.parallelStream().filter(ente -> ente.getCapital().equals(opcoes.getCapital()))
					.collect(Collectors.toList());					
		} else {
			return listaEntes;
		}
	}

	private List<Ente> filtrarPopulacaoMinima(List<Ente> listaEntes, OpcoesCargaDados opcoes) {
		if(opcoes.getPopulacaoMinima() != null) {
			return listaEntes.parallelStream().filter(ente -> ente.getPopulacao() >= opcoes.getPopulacaoMinima())
					.collect(Collectors.toList());								
		} else {
			return listaEntes;
		}
	}

	private List<Ente> filtrarPopulacaoMaxima(List<Ente> listaEntes, OpcoesCargaDados opcoes) {
		if(opcoes.getPopulacaoMaxima() != null) {
			return listaEntes.parallelStream().filter(ente -> ente.getPopulacao() <= opcoes.getPopulacaoMaxima())
					.collect(Collectors.toList());								
		} else {
			return listaEntes;
		}
	}
	
	private List<String> obterListaCodigoIbgePelaEsferaNaBase(OpcoesCargaDados opcoes) {
		
		List<Ente> listaEntes = null;
	
		if(opcoes.getEsfera() == null || opcoes.getEsfera().equals(Esfera.ESTADOS_E_DISTRITO_FEDERAL)) {
			listaEntes = consultarEntesNaBase(Arrays.asList(Esfera.ESTADO.getCodigo(),Esfera.DISTRITO_FEDERAL.getCodigo()));				
		} else {
			listaEntes = consultarEntesNaBase(Arrays.asList(opcoes.getEsfera().getCodigo()));
		}
		
		return obterListaCodigoIbge(listaEntes);
	}

	private List<String> obterListaCodigoIbge (List<Ente> listaEntes){
		List<String> listaCodigoIbge = listaEntes.parallelStream().map(Ente::getCod_ibge).collect(Collectors.toList());
		/*
		for(Ente ente: listaEntes) {
			listaCodigoIbge.add(ente.getCod_ibge());
		}*/
		return listaCodigoIbge;
	}

	@Override
	protected void excluir(OpcoesCargaDados opcoes) {
		excluirTodos();
	}

	@Override
	protected String getEntityName() {
		return Ente.class.getSimpleName();
	}

	@Override
	protected String[] getColunasArquivoCSV() {
		return COLUNAS_ARQUIVO_CSV;
	}

	@Override
	protected Class<Ente> getEntityClass() {
		return Ente.class;
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
		return API_PATH_ENTES;
	}
}
