package br.gov.ce.sefaz.siconfi.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.gov.ce.sefaz.siconfi.entity.AnexoRelatorio;
import br.gov.ce.sefaz.siconfi.opcoes.OpcoesCargaDados;
import br.gov.ce.sefaz.siconfi.util.SiconfiResponse;

public class AnexoRelatorioService extends SiconfiService<AnexoRelatorio, OpcoesCargaDados> {

	private static final Logger logger = LogManager.getLogger(AnexoRelatorioService.class);
	
	private static String[] COLUNAS_ARQUIVO_CSV = new String[]{"esfera","demonstrativo","anexo"};
	
	private static String NOME_PADRAO_ARQUIVO_CSV = "anexos-relatorios.csv";
	
	private static String API_PATH_ANEXO_RELATORIO= "anexos-relatorios";
	
	public AnexoRelatorioService () {
		super();
	}
	
	@Override
	protected List<AnexoRelatorio> lerEntidades(Response response) {
		SiconfiResponse<AnexoRelatorio> anexoRelatorioResponse = response
				.readEntity(new GenericType<SiconfiResponse<AnexoRelatorio>>() {
				});
		return anexoRelatorioResponse != null ? anexoRelatorioResponse.getItems() : new ArrayList<AnexoRelatorio>();
	}

	@Override
	protected String getEntityName() {
		return AnexoRelatorio.class.getSimpleName();
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

	@Override
	protected String getApiPath() {
		return API_PATH_ANEXO_RELATORIO;
	}	
}
