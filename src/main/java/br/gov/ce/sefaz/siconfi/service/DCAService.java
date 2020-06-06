package br.gov.ce.sefaz.siconfi.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.gov.ce.sefaz.siconfi.entity.DeclaracaoContasAnuais;
import br.gov.ce.sefaz.siconfi.opcoes.OpcoesCargaDadosDCA;
import br.gov.ce.sefaz.siconfi.util.APIQueryParamUtil;
import br.gov.ce.sefaz.siconfi.util.Constantes;
import br.gov.ce.sefaz.siconfi.util.SiconfiResponse;
import br.gov.ce.sefaz.siconfi.util.Utils;

public class DCAService extends SiconfiService<DeclaracaoContasAnuais>{

	private static final Logger logger = LogManager.getLogger(DCAService.class);

	public static List<String> ANEXOS_DCA = Arrays.asList("Anexo I-AB", "Anexo I-C", "Anexo I-D", "Anexo I-E", 
			"Anexo I-F", "Anexo I-G", "Anexo I-HI", "DCA-Anexo I-AB", "DCA-Anexo I-C", "DCA-Anexo I-D", 
			"DCA-Anexo I-E", "DCA-Anexo I-F", "DCA-Anexo I-G", "DCA-Anexo I-HI");
	
	private static String[] COLUNAS_ARQUIVO_CSV = new String[] { "exercicio", "uf", "cod_ibge", "instituicao", "anexo",
			"cod_conta", "conta", "coluna", "rotulo", "populacao", "valorFormatado" };
	
	private static String NOME_PADRAO_ARQUIVO_CSV = "dca.csv";

	private static final String API_PATH_DCA = "dca";

	private EnteService enteService;

	public DCAService() {
		super();
	}

	public void carregarDados(OpcoesCargaDadosDCA opcoesCargaDados) {
		
		List<DeclaracaoContasAnuais> listaDCA = consultarNaApi(opcoesCargaDados);	
		
		switch (opcoesCargaDados.getOpcaoSalvamento()) {
		case CONSOLE:
			exibirDadosNaConsole(listaDCA);
			break;
		case ARQUIVO:
			String nomeArquivo = definirNomeArquivoCSV(opcoesCargaDados);
			escreverCabecalhoArquivoCsv(nomeArquivo);
			salvarArquivoCsv(listaDCA, nomeArquivo);
			break;
		case BANCO:
			salvarNoBancoDeDados(opcoesCargaDados, listaDCA);
			break;
		}
	}

	protected void salvarNoBancoDeDados(OpcoesCargaDadosDCA filtro, List<DeclaracaoContasAnuais> listaEntidades) {
		if(Utils.isEmptyCollection(listaEntidades)) return;
		getEntityManager().getTransaction().begin();		
		excluirDeclaracaoContasAnuais(filtro);
		persistir(listaEntidades);
		commitTransaction();
		fecharContextoPersistencia();		
	}

	private void excluirDeclaracaoContasAnuais(OpcoesCargaDadosDCA filtro) {
		logger.info("Excluindo dados do banco de dados...");
		
		StringBuilder queryBuilder = new StringBuilder("DELETE FROM DeclaracaoContasAnuais dca WHERE dca.exercicio IN (:exercicios) ");

		List<String> listaCodigoIbge = getEnteService().obterListaCodigosIbge(filtro);
		if(!Utils.isEmptyCollection(listaCodigoIbge)) {
			queryBuilder.append(" AND dca.cod_ibge IN (:codigosIbge)");
		}
		if(!filtro.isListaAnexosVazia()) {
			queryBuilder.append(" AND dca.anexo IN (:listaAnexos)");
		}
		
		Query query = getEntityManager().createQuery(queryBuilder.toString());
		query.setParameter("exercicios", filtro.getExercicios());
		
		if(!Utils.isEmptyCollection(listaCodigoIbge)) {
			query.setParameter("codigosIbge", listaCodigoIbge);
		}
		if(!filtro.isListaAnexosVazia()) {
			query.setParameter("listaAnexos", filtro.getListaAnexos());
		}

		int i = query.executeUpdate();
		logger.info("Linhas excluídas:" + i);
	}

	public void excluirDCA(Integer exercicio) {
		logger.info("Excluindo dados do banco de dados...");		
		int i = getEntityManager().createQuery("DELETE FROM DeclaracaoContasAnuais dca WHERE dca.exercicio="+exercicio).executeUpdate();
		logger.info("Linhas excluídas:" + i);
	}

	@Override
	public void excluirTodos() {
		logger.info("Excluindo dados do banco de dados...");		
		int i = getEntityManager().createQuery("DELETE FROM DeclaracaoContasAnuais dca").executeUpdate();
		logger.info("Linhas excluídas:" + i);
	}

	public List<DeclaracaoContasAnuais> consultarNaApi(){
		return new ArrayList<DeclaracaoContasAnuais>();
	}

	public List<DeclaracaoContasAnuais> consultarNaApi(OpcoesCargaDadosDCA filtroDCA){
		
		List<Integer> listaExercicios = (filtroDCA.getExercicios()!=null?filtroDCA.getExercicios(): Constantes.EXERCICIOS_DISPONIVEIS);
		List<String> listaAnexos = !filtroDCA.isListaAnexosVazia() ? filtroDCA.getListaAnexos() : ANEXOS_DCA;
		List<String> listaCodigoIbge = getEnteService().obterListaCodigosIbge(filtroDCA);
		
		List<DeclaracaoContasAnuais> listaDCA = new ArrayList<>();
		
		for (Integer exercicio: listaExercicios) {
			for(String codigoIbge: listaCodigoIbge) {
				for (String anexo: listaAnexos) {
					
					APIQueryParamUtil apiQueryParamUtil = new APIQueryParamUtil();
					apiQueryParamUtil.addParamAnExercicio(exercicio)
						.addParamIdEnte(codigoIbge)
						.addParamAnexo(anexo);
					
					List<DeclaracaoContasAnuais> listaDCAParcial = consultarNaApi(apiQueryParamUtil);	
					listaDCA.addAll(listaDCAParcial);
					aguardarUmSegundo();
				}
			}				
		}
		return listaDCA;
	}

	@Override
	protected List<DeclaracaoContasAnuais> lerEntidades(Response response) {
		SiconfiResponse<DeclaracaoContasAnuais> dcaResponse = response
				.readEntity(new GenericType<SiconfiResponse<DeclaracaoContasAnuais>>() {
				});
		return dcaResponse != null ? dcaResponse.getItems() : new ArrayList<DeclaracaoContasAnuais>();
	}

	private EnteService getEnteService() {
		if(enteService == null) {
			enteService = new EnteService();
		}
		return enteService;
	}
	
	@Override
	protected String[] getColunasArquivoCSV() {
		return COLUNAS_ARQUIVO_CSV;
	}

	@Override
	protected Class<DeclaracaoContasAnuais> getEntityClass() {
		return DeclaracaoContasAnuais.class;
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
		return API_PATH_DCA;
	}
}
