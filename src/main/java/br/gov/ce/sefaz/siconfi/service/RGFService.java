package br.gov.ce.sefaz.siconfi.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.gov.ce.sefaz.siconfi.entity.RelatorioGestaoFiscal;
import br.gov.ce.sefaz.siconfi.enums.Periodicidade;
import br.gov.ce.sefaz.siconfi.enums.Poder;
import br.gov.ce.sefaz.siconfi.enums.TipoDemonstrativoRGF;
import br.gov.ce.sefaz.siconfi.response.RelatorioGestaoFiscalResponse;
import br.gov.ce.sefaz.siconfi.util.CsvUtil;
import br.gov.ce.sefaz.siconfi.util.FiltroRGF;

public class RGFService extends SiconfiService <RelatorioGestaoFiscal>{
	
	private static final Logger logger = LogManager.getLogger(RGFService.class);

	public static List<String> ANEXOS_RGF = Arrays.asList("RGF-Anexo 01", "RGF-Anexo 02", "RGF-Anexo 03",
			"RGF-Anexo 04", "RGF-Anexo 05", "RGF-Anexo 06");

	private static String[] COLUNAS_ARQUIVO_CSV = new String[] { "exercicio", "periodicidade", "periodo", "uf", "cod_ibge", "cod_poder", "instituicao", "anexo",
			"cod_conta", "conta", "coluna", "rotulo", "populacao", "valorFormatado" };
	
	private static String NOME_PADRAO_ARQUIVO_CSV = "rgf.csv";

	private EnteService enteService;

	public RGFService() {
		super();
	}

	public void carregarDados(FiltroRGF filtroRGF) {
		
		List<RelatorioGestaoFiscal> listaRGF = consultarNaApi(filtroRGF);	
		
		switch (filtroRGF.getOpcaoSalvamento()) {
		case CONSOLE:
			exibirDadosNaConsole(listaRGF);
			break;
		case ARQUIVO:
			String nomeArquivo = (filtroRGF.getNomeArquivo() != null && !filtroRGF.getNomeArquivo().trim().isEmpty())
					? filtroRGF.getNomeArquivo()
					: NOME_PADRAO_ARQUIVO_CSV;
			salvarArquivoCsv(listaRGF, nomeArquivo);
			break;
		case BANCO:
			salvarNoBancoDeDados(filtroRGF, listaRGF);
			break;
		}
	}

	protected void salvarArquivoCsv(List<RelatorioGestaoFiscal> listaRGF, String nomeArquivo) {
		logger.info("Salvando dados no arquivo CSV...");
		CsvUtil<RelatorioGestaoFiscal> csvUtil = new CsvUtil<RelatorioGestaoFiscal>(RelatorioGestaoFiscal.class);
		csvUtil.writeToCsvFile(listaRGF, COLUNAS_ARQUIVO_CSV, nomeArquivo);
	}

	protected void salvarNoBancoDeDados(FiltroRGF filtro, List<RelatorioGestaoFiscal> listaEntidades) {
		if(listaEntidades == null || listaEntidades.isEmpty()) return;
		getEntityManager().getTransaction().begin();		
		excluirRelatorioGestaoFiscal(filtro);
		persistir(listaEntidades);
		commitTransaction();
		fecharContextoPersistencia();		
	}

	protected void excluirTodos() {
		logger.info("Excluindo dados do banco de dados...");		
		int i = getEntityManager().createQuery("DELETE FROM RelatorioGestaoFiscal rgf").executeUpdate();
		logger.info("Linhas excluídas:" + i);
	}

	public void excluirRGF(Integer exercicio) {
		logger.info("Excluindo dados do banco de dados...");
		int i = getEntityManager().createQuery("DELETE FROM RelatorioGestaoFiscal rgf WHERE rgf.exercicio="+exercicio).executeUpdate();
		logger.info("Linhas excluídas:" + i);
	}
	
	private void excluirRelatorioGestaoFiscal(FiltroRGF filtro) {
		logger.info("Excluindo dados do banco de dados...");
		
		StringBuilder queryBuilder = new StringBuilder("DELETE FROM RelatorioGestaoFiscal rgf WHERE rgf.exercicio IN (:exercicios) ");

		if (!filtro.isListaQuadrimestresVazia()) {
			queryBuilder.append(" AND rgf.periodo IN (:periodos)");
		}

		if (filtro.isExisteCodigosIbge()) {
			queryBuilder.append(" AND rgf.cod_ibge IN (:codigosIbge)");
		}

		if (!filtro.isListaPoderesVazia()) {
			queryBuilder.append(" AND rgf.co_poder IN (:listaPoderes)");
		}

		if (!filtro.isListaAnexosVazia()) {
			queryBuilder.append(" AND rgf.anexo IN (:listaAnexos)");
		}
		
		Query query = getEntityManager().createQuery(queryBuilder.toString());		
		query.setParameter("exercicios", filtro.getExercicios());
		
		if (!filtro.isListaQuadrimestresVazia()) {
			query.setParameter("periodos", filtro.getQuadrimestres());
		}
		if(filtro.isExisteCodigosIbge()) {
			query.setParameter("codigosIbge", filtro.getCodigosIBGE());
		}
		if (!filtro.isListaPoderesVazia()) {
			query.setParameter("listaPoderes", Poder.getListaCodigoPoder(filtro.getListaPoderes()));
		}
		if(!filtro.isListaAnexosVazia()) {
			query.setParameter("listaAnexos", filtro.getListaAnexos());
		}

		int i = query.executeUpdate();
		logger.info("Linhas excluídas:" + i);
	}

	public List<RelatorioGestaoFiscal> consultarNaApi(){
		return new ArrayList<RelatorioGestaoFiscal>();
	}

	public List<RelatorioGestaoFiscal> consultarNaApi(FiltroRGF filtroRGF){
		
		List<Integer> listaExercicios = !filtroRGF.isListaExerciciosVazia() ? filtroRGF.getExercicios()
				: EXERCICIOS_DISPONIVEIS;					
		List<RelatorioGestaoFiscal> listaRGF = new ArrayList<>();
		
		for (Integer exercicio : listaExercicios) {
			listaRGF.addAll(consultarNaApi(filtroRGF, exercicio));
		}
		return listaRGF;
	}

	private List<RelatorioGestaoFiscal> consultarNaApi(FiltroRGF filtroRGF, Integer exercicio){

		List<Integer> listaQuadrimestres = !filtroRGF.isListaQuadrimestresVazia() ? filtroRGF.getQuadrimestres()
				: QUADRIMESTRES;
		List<RelatorioGestaoFiscal> listaRGF = new ArrayList<>();
		
		for (Integer quadrimestre : listaQuadrimestres) {
			listaRGF.addAll(consultarNaApi(filtroRGF, exercicio, quadrimestre));
		}
		return listaRGF;
	}
	
	private List<RelatorioGestaoFiscal> consultarNaApi(FiltroRGF filtroRGF, Integer exercicio, Integer quadrimestre){

		List<String> listaCodigoIbge = getEnteService().obterListaCodigosIbge(filtroRGF);
		List<RelatorioGestaoFiscal> listaRGF = new ArrayList<>();
		
		for (String codigoIbge : listaCodigoIbge) {
			listaRGF.addAll(consultarNaApi(filtroRGF, exercicio, quadrimestre, codigoIbge));
		}
		return listaRGF;		
	}

	private List<RelatorioGestaoFiscal> consultarNaApi(FiltroRGF filtroRGF, Integer exercicio, Integer quadrimestre, String codigoIbge){
		
		List<Poder> listaPoder = !filtroRGF.isListaPoderesVazia() ? filtroRGF.getListaPoderes()
				: Arrays.asList(Poder.values());
		List<RelatorioGestaoFiscal> listaRGF = new ArrayList<>();
		
		for (Poder poder : listaPoder) {
			listaRGF.addAll(consultarNaApi(filtroRGF, exercicio, quadrimestre, codigoIbge, poder));
		}
		return listaRGF;		
	}

	private List<RelatorioGestaoFiscal> consultarNaApi(FiltroRGF filtroRGF, Integer exercicio, Integer quadrimestre, String codigoIbge, Poder poder){

		List<String> listaAnexos = !filtroRGF.isListaAnexosVazia() ? filtroRGF.getListaAnexos() : ANEXOS_RGF;
		List<RelatorioGestaoFiscal> listaRGF = new ArrayList<>();
		
		for (String anexo : listaAnexos) {
			List<RelatorioGestaoFiscal> listaRGFParcial = consultarNaApi(exercicio,
					Periodicidade.QUADRIMESTRAL.getCodigo(), quadrimestre, TipoDemonstrativoRGF.RGF.getCodigo(),
					anexo, poder.getCodigo(), codigoIbge);
			listaRGF.addAll(listaRGFParcial);
		}
		return listaRGF;		
	}

	public List<RelatorioGestaoFiscal> consultarNaApi(Integer exercicio, String indicadorPeriodicidade,
			Integer periodo, String codigoTipoDemonstrativo, String anexo, String codigoPoder, String codigoIbge) {
		
		List<RelatorioGestaoFiscal> listaRGF = null;
		
		try {

			RelatorioGestaoFiscalResponse relatorioResponse = obterResponseDaApi(exercicio, indicadorPeriodicidade,
					periodo, codigoTipoDemonstrativo, anexo, codigoPoder, codigoIbge);
			listaRGF = relatorioResponse != null ? relatorioResponse.getItems() : new ArrayList<RelatorioGestaoFiscal>();

		} catch (Exception e) {
			logger.error("Erro para os parâmetros: exercicio: " + exercicio + ", periodicidade: "
					+ indicadorPeriodicidade + ", período: " + periodo
					+ ", codigoTipoDemonstrativo:" + codigoTipoDemonstrativo
					+ ", anexo: " + anexo
					+ ", codigoPoder: " + codigoPoder 
					+ ", codigoIbge: " + codigoIbge);
			e.printStackTrace();
			listaRGF = new ArrayList<>();
		}
		
		logger.debug("Tamanho da lista para os paramêtros: exercicio: " + exercicio + ", periodicidade: "
				+ indicadorPeriodicidade + ", período: " + periodo
				+ ", codigoTipoDemonstrativo:" + codigoTipoDemonstrativo
				+ ", anexo: " + anexo
				+ ", codigoPoder: " + codigoPoder 
				+ ", codigoIbge: " + codigoIbge + ": " + listaRGF.size());
		return listaRGF;
	}

	private RelatorioGestaoFiscalResponse obterResponseDaApi(Integer exercicio, String indicadorPeriodicidade,
			Integer periodo, String codigoTipoDemonstrativo, String anexo, String codigoPoder, String codigoIbge) {

		RelatorioGestaoFiscalResponse relatorioResponse = null;
		long ini = System.currentTimeMillis();
		
		try {
			this.webTarget = this.client.target(URL_SERVICE).path("rgf")
					.queryParam("an_exercicio", exercicio)
					.queryParam("in_periodicidade", indicadorPeriodicidade)
					.queryParam("nr_periodo", periodo)
					.queryParam("co_tipo_demonstrativo", codigoTipoDemonstrativo)
					.queryParam("no_anexo", anexo.replaceAll(" ", "%20")) //Caso, não haja esse tratamento para o caractere espaço, a API apresenta erro.
					.queryParam("co_poder", codigoPoder)
					.queryParam("id_ente", codigoIbge);
			Invocation.Builder invocationBuilder = this.webTarget.request("application/json;charset=UTF-8");
			logger.info ("Get na API: " + this.webTarget.getUri().toString());
			Response response = invocationBuilder.get();
			relatorioResponse = response.readEntity(RelatorioGestaoFiscalResponse.class);			
		} catch(Exception e) {
			logger.error("Erro ao consultar a API para os seguintes parâmetros: exercicio: " + exercicio + ", periodicidade: "
					+ indicadorPeriodicidade + ", período: " + periodo
					+ ", codigoTipoDemonstrativo:" + codigoTipoDemonstrativo
					+ ", anexo: " + anexo
					+ ", codigoPoder: " + codigoPoder 
					+ ", codigoIbge: " + codigoIbge);
			e.printStackTrace();			
		} finally {			
			long fim = System.currentTimeMillis();
			logger.debug("Tempo para consultar os RGF na API:" + (fim - ini));
		}
		return relatorioResponse;			
	}
	
	private EnteService getEnteService() {
		if(enteService == null) {
			enteService = new EnteService();
		}
		return enteService;
	}

}
