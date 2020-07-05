package br.gov.ce.sefaz.siconfi.service;

import org.apache.logging.log4j.Logger;

import br.gov.ce.sefaz.siconfi.entity.AnexoRelatorio;
import br.gov.ce.sefaz.siconfi.opcoes.OpcoesCargaDados;
import br.gov.ce.sefaz.siconfi.util.LoggerUtil;

public class AnexoRelatorioService extends SiconfiService<AnexoRelatorio, OpcoesCargaDados> {

	private static Logger logger = null;
	
	private static String[] COLUNAS_ARQUIVO_CSV = new String[]{"esfera","demonstrativo","anexo"};
	
	private static String NOME_PADRAO_ARQUIVO_CSV = "anexos-relatorios.csv";
	
	private static String API_PATH_ANEXO_RELATORIO= "anexos-relatorios";
	
	public AnexoRelatorioService () {
		super();
	}
	
	@Override
	protected int excluir(OpcoesCargaDados opcoes) {
		return excluirTodos();
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
		if(logger == null) {
			logger = LoggerUtil.createLogger(AnexoRelatorioService.class);
		}
		return logger;
	}

	@Override
	protected String getApiPath() {
		return API_PATH_ANEXO_RELATORIO;
	}
}
