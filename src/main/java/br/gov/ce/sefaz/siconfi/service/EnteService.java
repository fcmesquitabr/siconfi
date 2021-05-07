package br.gov.ce.sefaz.siconfi.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.gov.ce.sefaz.siconfi.entity.Ente;
import br.gov.ce.sefaz.siconfi.enums.Esfera;
import br.gov.ce.sefaz.siconfi.opcoes.OpcoesCargaDados;
import br.gov.ce.sefaz.siconfi.util.APIQueryParamUtil;

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
	public List<Ente> consultarEntesNaBase(OpcoesCargaDados opcoes){		
		logger.debug("Consultando a base com parâmetro: {}", opcoes);
		
		StringBuilder queryBuilder = new StringBuilder("SELECT e FROM Ente e WHERE e.esfera IN (:listaEsfera) ");
		if(opcoes.isExisteCodigosIbge()) {
			queryBuilder.append(" AND e.cod_ibge IN (:codigosIbge) ");
		}

		if(!opcoes.isListaCodigosUfVazia()) {
			queryBuilder.append(" AND e.uf IN (:codigosUf) ");
		}

		if(opcoes.getCapital() != null) {
			queryBuilder.append(" AND e.capital = :capital ");
		}

		if(opcoes.getPopulacaoMinima() != null) {
			queryBuilder.append(" AND e.populacao >= :populacaoMinima ");
		}

		if(opcoes.getPopulacaoMaxima() != null) {
			queryBuilder.append(" AND e.populacao <= :populacaoMaxima ");
		}
		
		Query query = getEntityManager().createQuery(queryBuilder.toString());
		
		if(opcoes.getEsfera() == null || opcoes.getEsfera().equals(Esfera.ESTADOS_E_DISTRITO_FEDERAL)) {
			query.setParameter("listaEsfera",
					Arrays.asList(Esfera.ESTADO.getCodigo(), Esfera.DISTRITO_FEDERAL.getCodigo()));
		} else {
			query.setParameter("listaEsfera",
					Arrays.asList(opcoes.getEsfera().getCodigo()));
		}
		
		if(opcoes.isExisteCodigosIbge()) {
			query.setParameter("codigosIbge", opcoes.getCodigosIBGE());
		}

		if(!opcoes.isListaCodigosUfVazia()) {
			query.setParameter("codigosUf", opcoes.getCodigosUF());
		}

		if(opcoes.getCapital() != null) {
			query.setParameter("capital", opcoes.getCapital());
		}

		if(opcoes.getPopulacaoMinima() != null) {
			query.setParameter("populacaoMinima", opcoes.getPopulacaoMinima());
		}

		if(opcoes.getPopulacaoMaxima() != null) {
			query.setParameter("populacaoMaxima", opcoes.getPopulacaoMaxima());
		}

		return query.getResultList();		 
	}

	public List<Ente> consultarEntesNaAPI(){
		logger.debug("Consultando a API...");
		return consultarNaApi(new APIQueryParamUtil());		 
	}

	@Override
	protected void consultarNaApiEGerarSaidaDados(OpcoesCargaDados opcoes){
		List<Ente> listaEntes = obterListaEntesNaAPI(opcoes);
		gerarSaidaDados(opcoes, listaEntes);
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
		return obterListaCodigoIbge(consultarEntesNaBase(opcoes));
	}

	private List<String> obterListaCodigoIbge (List<Ente> listaEntes){		
		return listaEntes.parallelStream().map(Ente::getCod_ibge).collect(Collectors.toList());
	}

	@Override
	public int excluir(OpcoesCargaDados opcoes) {
		return excluirTodos();
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
