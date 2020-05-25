package br.gov.ce.sefaz.siconfi.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.gov.ce.sefaz.siconfi.entity.AnexoRelatorio;
import br.gov.ce.sefaz.siconfi.enums.OpcaoSalvamentoDados;
import br.gov.ce.sefaz.siconfi.response.AnexoRelatorioResponse;
import br.gov.ce.sefaz.siconfi.util.CsvUtil;

public class AnexoRelatorioService extends SiconfiService<AnexoRelatorio> {

	private static final Logger logger = LogManager.getLogger(AnexoRelatorioService.class);
	
	public AnexoRelatorioService () {
		super();
	}
	
	public void carregarDados(OpcaoSalvamentoDados opcaoSalvamento) {
		
		List<AnexoRelatorio> listaAnexos = consultarNaApi();	
		
		switch (opcaoSalvamento) {
		case CONSOLE:
			exibirDadosNaConsole(listaAnexos);
			break;
		case ARQUIVO:
			salvarArquivoCsv(listaAnexos);
			break;
		case BANCO:
			salvarNoBancoDeDados(listaAnexos);
			break;
		}
	}

	protected void salvarArquivoCsv(List<AnexoRelatorio> listaAnexos) {
		logger.info("Salvando dados no arquivo CSV...");
		CsvUtil<AnexoRelatorio> csvUtil = new CsvUtil<AnexoRelatorio>(AnexoRelatorio.class);
		csvUtil.writeToCsvFile(listaAnexos, new String[]{"esfera","demonstrativo","anexo"} , "D:\\anexos-relatorios.csv");
	}
		
	protected void excluirTodos() {
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
		
		this.webTarget = this.client.target(URL_SERVICE).path("anexos-relatorios");
		Invocation.Builder invocationBuilder = this.webTarget.request("application/json;charset=UTF-8");
		logger.info("Fazendo get na API: " + this.webTarget.getUri().toString());
		Response response = invocationBuilder.get();
		AnexoRelatorioResponse anexoRelatorioResponse = response.readEntity(AnexoRelatorioResponse.class);
		
		long fim = System.currentTimeMillis();			
		logger.debug("Tempo para consultar os entes na API:" + (fim -ini));
		return anexoRelatorioResponse;
	}

}
