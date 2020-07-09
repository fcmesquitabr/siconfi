package br.gov.ce.sefaz.siconfi.service;

import java.util.List;

import org.apache.logging.log4j.Logger;

import br.gov.ce.sefaz.siconfi.entity.MatrizSaldoContabeisPatrimonial;
import br.gov.ce.sefaz.siconfi.util.Constantes;
import br.gov.ce.sefaz.siconfi.util.LoggerUtil;

public class MSCPatrimonialService extends MSCService<MatrizSaldoContabeisPatrimonial> {

	private static Logger logger = null;

	private static final String[] COLUNAS_ARQUIVO_CSV = new String[] { "exercicio", "mes_referencia", "cod_ibge", "poder_orgao", "tipo_matriz",
			"classe_conta", "natureza_conta", "conta_contabil", "financeiro_permanente", "ano_fonte_recursos", "fonte_recursos", 
			"divida_consolidada", "data_referencia", "entrada_msc", "tipo_valor", "valorFormatado"};
	
	private static final String NOME_PADRAO_ARQUIVO_CSV = "msc_patrimonial.csv";

	private static final String API_PATH_MSC_PATRIMONIAL = "msc_patrimonial";

	public MSCPatrimonialService() {
		super();
	}

	@Override
	protected String[] getColunasArquivoCSV() {
		return COLUNAS_ARQUIVO_CSV;
	}

	@Override
	protected Class<MatrizSaldoContabeisPatrimonial> getEntityClass() {
		return MatrizSaldoContabeisPatrimonial.class;
	}
	
	@Override
	protected String getNomePadraoArquivoCSV() {
		return NOME_PADRAO_ARQUIVO_CSV;
	}

	@Override
	protected String getEntityName() {
		return MatrizSaldoContabeisPatrimonial.class.getSimpleName();
	}

	@Override
	protected List<Integer> getClassContas() {
		return Constantes.CLASSES_CONTAS_PATRIMONIAIS;
	}
	
	@Override
	protected Logger getLogger() {
		if(logger == null) {
			logger = LoggerUtil.createLogger(MSCPatrimonialService.class);
		}
		return logger;
	}

	@Override
	protected String getApiPath() {
		return API_PATH_MSC_PATRIMONIAL;
	}

}
