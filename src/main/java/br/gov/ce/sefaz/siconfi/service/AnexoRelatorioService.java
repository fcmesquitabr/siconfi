package br.gov.ce.sefaz.siconfi.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.gov.ce.sefaz.siconfi.entity.AnexoRelatorio;
import br.gov.ce.sefaz.siconfi.response.AnexoRelatorioResponse;

public class AnexoRelatorioService extends SiconfiService<AnexoRelatorio> {

	private static final Logger logger = LogManager.getLogger(AnexoRelatorioService.class);
	
	private static String[] COLUNAS_ARQUIVO_CSV = new String[]{"esfera","demonstrativo","anexo"};
	
	private static String NOME_PADRAO_ARQUIVO_CSV = "anexos-relatorios.csv";
	
	private static String API_PATH_ANEXO_RELATORIO= "anexos-relatorios";
	
	public AnexoRelatorioService () {
		super();
	}
	
	@Override
	public void excluirTodos() {
		logger.info("Excluindo dados do banco de dados...");
		int i = getEntityManager().createQuery("DELETE FROM AnexoRelatorio ar").executeUpdate();
		logger.info("Linhas excluídas:" + i);
	}

	public List<AnexoRelatorio> consultarNaApi(){
		
		List<AnexoRelatorio> listaAnexos = null;		
		try {
			
			AnexoRelatorioResponse anexoRelatorioResponse = obterResponseDaApi();
			listaAnexos = anexoRelatorioResponse != null ? anexoRelatorioResponse.getItems() : new ArrayList<AnexoRelatorio>();
			
		} catch (Exception e) {
			logger.error(e);
			listaAnexos =  new ArrayList<>();
		}
		
		logger.debug("Tamanho da lista de Anexos:" + listaAnexos.size());
		return listaAnexos;
	}

	private AnexoRelatorioResponse obterResponseDaApi() {
		long ini = System.currentTimeMillis();
		
		this.webTarget = this.client.target(URL_SERVICE).path(API_PATH_ANEXO_RELATORIO);
		Invocation.Builder invocationBuilder = this.webTarget.request(API_RESPONSE_TYPE);
		
		logger.info("Fazendo Get na API: " + this.webTarget.getUri().toString());
		Response response = invocationBuilder.get();
		AnexoRelatorioResponse anexoRelatorioResponse = response.readEntity( AnexoRelatorioResponse.class );
		
		long fim = System.currentTimeMillis();			
		logger.debug("Tempo para consultar os anexos dos relatórios na API:" + (fim -ini));
		return anexoRelatorioResponse;
	}

	@Override
	protected String[] getColunasArquivoCSV() {
		return COLUNAS_ARQUIVO_CSV;
	}

	@Override
	protected Class<AnexoRelatorio> getEntityClass() {
		return AnexoRelatorio.class;
	}
	
	@Override
	protected String getNomePadraoArquivoCSV() {
		return NOME_PADRAO_ARQUIVO_CSV;
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}	

}
