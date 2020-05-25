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
import br.gov.ce.sefaz.siconfi.entity.Ente;
import br.gov.ce.sefaz.siconfi.enums.Esfera;
import br.gov.ce.sefaz.siconfi.response.DeclaracaoContasAnuaisResponse;
import br.gov.ce.sefaz.siconfi.util.CsvUtil;
import br.gov.ce.sefaz.siconfi.util.FiltroDCA;

public class DCAService extends SiconfiService<DeclaracaoContasAnuais>{

	private static final Logger logger = LogManager.getLogger(DCAService.class);

	public static List<String> ANEXOS_DCA = Arrays.asList("Anexo I-AB", "Anexo I-C", "Anexo I-D", "Anexo I-E", 
			"Anexo I-F", "Anexo I-G", "Anexo I-HI", "DCA-Anexo I-AB", "DCA-Anexo I-C", "DCA-Anexo I-D", 
			"DCA-Anexo I-E", "DCA-Anexo I-F", "DCA-Anexo I-G", "DCA-Anexo I-HI");
	
	private static String[] COLUNAS_ARQUIVO_CSV = new String[] { "exercicio", "uf", "cod_ibge", "instituicao", "anexo",
			"cod_conta", "conta", "coluna", "rotulo", "populacao", "valorFormatado" };
	
	private static String NOME_PADRAO_ARQUIVO_CSV = "dca.csv";
			
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
			String nomeArquivo = (filtroDCA.getNomeArquivo() != null && !filtroDCA.getNomeArquivo().trim().isEmpty())
					? filtroDCA.getNomeArquivo()
					: NOME_PADRAO_ARQUIVO_CSV;
			salvarArquivoCsv(listaDCA, nomeArquivo);
			break;
		case BANCO:
			salvarNoBancoDeDados(filtroDCA, listaDCA);
			break;
		}
	}

	protected void salvarArquivoCsv(List<DeclaracaoContasAnuais> listaDCA, String nomeArquivo) {
		logger.info("Salvando dados no arquivo CSV...");
		CsvUtil<DeclaracaoContasAnuais> csvUtil = new CsvUtil<DeclaracaoContasAnuais>(DeclaracaoContasAnuais.class);
		csvUtil.writeToCsvFile(listaDCA, COLUNAS_ARQUIVO_CSV, nomeArquivo);
	}

	protected void salvarNoBancoDeDados(FiltroDCA filtro, List<DeclaracaoContasAnuais> listaEntidades) {
		if(listaEntidades == null || listaEntidades.isEmpty()) return;
		getEntityManager().getTransaction().begin();		
		excluirDeclaracaoContasAnuais(filtro);
		persistir(listaEntidades);
		commitTransaction();
		fecharContextoPersistencia();		
	}

	private void excluirDeclaracaoContasAnuais(FiltroDCA filtro) {
		logger.info("Excluindo dados do banco de dados...");
		
		StringBuilder queryBuilder = new StringBuilder("DELETE FROM DeclaracaoContasAnuais dca WHERE dca.exercicio IN (:exercicios) ");
		if(filtro.getCodigosIBGE()!=null && !filtro.getCodigosIBGE().isEmpty()) {
			queryBuilder.append(" AND dca.cod_ibge IN (:codigosIbge)");
		}
		if(filtro.getListaAnexos()!=null && !filtro.getListaAnexos().isEmpty()) {
			queryBuilder.append(" AND dca.anexo IN (:listaAnexos)");
		}
		
		Query query = getEntityManager().createQuery(queryBuilder.toString());
		query.setParameter("exercicios", filtro.getExercicios());
		if(filtro.getCodigosIBGE()!=null && !filtro.getCodigosIBGE().isEmpty()) {
			query.setParameter("codigosIbge", filtro.getCodigosIBGE());
		}
		if(filtro.getListaAnexos()!=null && !filtro.getListaAnexos().isEmpty()) {
			query.setParameter("listaAnexos", filtro.getCodigosIBGE());
		}

		int i = query.executeUpdate();
		logger.info("Linhas excluídas:" + i);
	}

	public void carregarDeclaracaoContasAnuaisNaBaseDeDados(boolean apagarDadosExistentes) {
		
		for (Integer exercicio: EXERCICIOS_DISPONIVEIS) {
			carregarDadosDosAnexosNaBaseDeDados(apagarDadosExistentes, exercicio);
		}		
		fecharContextoPersistencia();
	}

	private void carregarDadosDosAnexosNaBaseDeDados(boolean apagarDadosExistentes, Integer exercicio) {
		
		EnteService enteService = new EnteService();
		List<Ente> listaEntes = enteService.consultarEntesNaBase(Arrays.asList((Esfera.ESTADO.getCodigo())));

		for (Ente ente : listaEntes) {
			for(String anexo: ANEXOS_DCA) {
				List<DeclaracaoContasAnuais> listaDCA = consultarNaApi(exercicio, anexo, ente.getCod_ibge());
							
				getEntityManager().getTransaction().begin();
				
				if(apagarDadosExistentes) {
					excluirDCA(exercicio);
				}
				
				persistir(listaDCA);
				commitTransaction();	
			}
		}
	}
	
	private void excluirDCA(Integer exercicio) {
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
		List<String> listaAnexos = filtroDCA.getListaAnexos() != null && !filtroDCA.getListaAnexos().isEmpty()
				? filtroDCA.getListaAnexos()
				: ANEXOS_DCA;

		List<DeclaracaoContasAnuais> listaDCA = new ArrayList<>();
		
		for (Integer exercicio: listaExercicios) {
			if(filtroDCA.getCodigosIBGE()!=null && !filtroDCA.getCodigosIBGE().isEmpty()) {
				for(String codigoIbge: filtroDCA.getCodigosIBGE()) {
					for (String anexo: listaAnexos) {
						List<DeclaracaoContasAnuais> listaDCAParcial = consultarNaApi(exercicio,anexo, codigoIbge);	
						listaDCA.addAll(listaDCAParcial);											
					}
				}				
			} else {
				// TODO
				// Consultar baseado na Esfera e se não selecionada, consultar todos os extratos para Estados e Distrito
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
}
