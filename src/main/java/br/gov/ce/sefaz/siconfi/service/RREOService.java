package br.gov.ce.sefaz.siconfi.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.gov.ce.sefaz.siconfi.entity.Ente;
import br.gov.ce.sefaz.siconfi.entity.RelatorioResumidoExecucaoOrcamentaria;
import br.gov.ce.sefaz.siconfi.enums.Esfera;
import br.gov.ce.sefaz.siconfi.enums.TipoDemonstrativoRREO;
import br.gov.ce.sefaz.siconfi.response.RelatorioResumidoExecucaoOrcamentariaResponse;

public class RREOService extends SiconfiService <RelatorioResumidoExecucaoOrcamentaria>{

	private static final Logger logger = LogManager.getLogger(RREOService.class);

	public static List<String> ANEXOS_RREO = Arrays.asList("RREO-Anexo 01", "RREO-Anexo 02", "RREO-Anexo 03",
			"RREO-Anexo 04", "RREO-Anexo 04 - RGPS", "RREO-Anexo 04 - RPPS", "RREO-Anexo 04.0 - RGPS",
			"RREO-Anexo 04.1", "RREO-Anexo 04.2", "RREO-Anexo 04.3 - RGPS", "RREO-Anexo 05", "RREO-Anexo 06",
			"RREO-Anexo 07", "RREO-Anexo 09", "RREO-Anexo 10 - RGPS", "RREO-Anexo 10 - RPPS", "RREO-Anexo 11",
			"RREO-Anexo 13", "RREO-Anexo 14");
	
	public RREOService() {
		super();
	}
	
	public void carregarRelatorioResumidoExecucaoOrcamentariaNaBaseDeDados(boolean apagarDadosExistentes) {
		
		for (Integer exercicio: EXERCICIOS_DISPONIVEIS) {
			carregarDadosBimestraisNaBaseDeDados(apagarDadosExistentes, exercicio);
		}		
		fecharContextoPersistencia();
	}
	
	private void carregarDadosBimestraisNaBaseDeDados(boolean apagarDadosExistentes, Integer exercicio) {
		for(Integer bimestre: BIMESTRES) {
			carregarDadosDosAnexosNaBaseDeDados(apagarDadosExistentes, exercicio, bimestre);
		}			
	}

	private void carregarDadosDosAnexosNaBaseDeDados(boolean apagarDadosExistentes, Integer exercicio,
			Integer bimestre) {
		
		EnteService enteService = new EnteService();
		List<Ente> listaEntes =  enteService.consultarEntesNaBase(Arrays.asList(Esfera.ESTADO.getCodigo()));
		
		for(Ente ente: listaEntes) {
			for(String anexo: ANEXOS_RREO) {
				List<RelatorioResumidoExecucaoOrcamentaria> listaRelatorioResumidoExecucaoOrcamentaria = consultarNaApi(
						exercicio, bimestre, TipoDemonstrativoRREO.RREO.getCodigo(), anexo, ente.getCod_ibge());					
				
				getEntityManager().getTransaction().begin();
				
				if(apagarDadosExistentes) {
					excluirRREO(exercicio);
				}
				
				persistir(listaRelatorioResumidoExecucaoOrcamentaria);
				commitTransaction();	
			}			
		}
	}
	
	private void excluirRREO(Integer exercicio) {
		logger.info("Excluindo dados do banco de dados...");
		int i = getEntityManager().createQuery("DELETE FROM RelatorioResumidoExecucaoOrcamentaria rreo WHERE rreo.exercicio="+exercicio).executeUpdate();
		logger.info("Linhas excluídas:" + i);
	}

	protected void excluirTodos() {
		logger.info("Excluindo dados do banco de dados...");
		int i = getEntityManager().createQuery("DELETE FROM RelatorioResumidoExecucaoOrcamentaria rreo").executeUpdate();
		logger.info("Linhas excluídas:" + i);
	}

	public List<RelatorioResumidoExecucaoOrcamentaria> consultarNaApi(){
		return new ArrayList<RelatorioResumidoExecucaoOrcamentaria>();
	}

	public List<RelatorioResumidoExecucaoOrcamentaria> consultarNaApi(
			Integer exercicio, Integer periodo, String codigoTipoDemonstrativo, String anexo, String codigoIbge) {

		List<RelatorioResumidoExecucaoOrcamentaria> listaRREO = null;
		try {

			RelatorioResumidoExecucaoOrcamentariaResponse relatorioResponse = obterResponseDaApi(exercicio, periodo,
					codigoTipoDemonstrativo, anexo, codigoIbge);


			listaRREO = relatorioResponse != null ? relatorioResponse.getItems()
					: new ArrayList<RelatorioResumidoExecucaoOrcamentaria>();
		} catch (Exception e) {
			logger.error("Erro para os parâmetros: exercicio: " + exercicio 
					+ ", período: " + periodo
					+ ", codigoTipoDemonstrativo:" + codigoTipoDemonstrativo
					+ ", anexo: " + anexo
					+ ", codigoIbge: " + codigoIbge);
			e.printStackTrace();
			listaRREO = new ArrayList<>();
		}
		
		logger.debug("Tamanho da lista de RREO os parâmetros: exercicio: " + exercicio + 
				", período: " + periodo + 
				", codigoTipoDemonstrativo:" + codigoTipoDemonstrativo + 
				", anexo: " + anexo + 
				", codigoIbge: " + codigoIbge +": " + listaRREO.size());		
		return listaRREO;
	}

	private RelatorioResumidoExecucaoOrcamentariaResponse obterResponseDaApi(Integer exercicio, Integer periodo,
			String codigoTipoDemonstrativo, String anexo, String codigoIbge) {
		
		long ini = System.currentTimeMillis();
		
		this.webTarget = this.client.target(URL_SERVICE).path("rreo")
				.queryParam("an_exercicio", exercicio)
				.queryParam("nr_periodo", periodo)
				.queryParam("co_tipo_demonstrativo", codigoTipoDemonstrativo)
				.queryParam("no_anexo", anexo.replaceAll(" ", "%20"))
				.queryParam("id_ente", codigoIbge);
		Invocation.Builder invocationBuilder = this.webTarget.request("application/json;charset=UTF-8");
		logger.info("Fazendo get na API: " + this.webTarget.getUri().toString());
		Response response = invocationBuilder.get();
		RelatorioResumidoExecucaoOrcamentariaResponse relatorioResponse = response
				.readEntity(RelatorioResumidoExecucaoOrcamentariaResponse.class);

		long fim = System.currentTimeMillis();
		logger.debug("Tempo para consultar os entes na API:" + (fim - ini));
		return relatorioResponse;
	}
	
}
