package br.gov.ce.sefaz.siconfi.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.gov.ce.sefaz.siconfi.entity.MatrizSaldoContabeisPatrimonial;
import br.gov.ce.sefaz.siconfi.response.MatrizSaldoContabeisPatrimonialResponse;
import br.gov.ce.sefaz.siconfi.response.MatrizSaldoContabeisResponse;

public class MSCPatrimonialService extends MSCService<MatrizSaldoContabeisPatrimonial> {

	private static final Logger logger = LogManager.getLogger(MSCPatrimonialService.class);

	private static String[] COLUNAS_ARQUIVO_CSV = new String[] { "exercicio", "mes_referencia", "cod_ibge", "poder_orgao", "tipo_matriz",
			"class_conta", "natureza_conta", "conta_contabil", "financeiro_permanente", "ano_fonte_recursos", "fonte_recursos", 
			"divida_consolidada", "data_referencia", "entrada_msc", "tipo_valor", "valorFormatado"};
	
	private static String NOME_PADRAO_ARQUIVO_CSV = "msc_patrimonial.csv";

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
	protected String getAPIPath() {
		return API_PATH_MSC_PATRIMONIAL;
	}

	@Override
	protected Class<? extends MatrizSaldoContabeisResponse<MatrizSaldoContabeisPatrimonial>> getResponseClassType() {
		return MatrizSaldoContabeisPatrimonialResponse.class;
	}

	@Override
	protected String getEntityName() {
		return MatrizSaldoContabeisPatrimonial.class.getSimpleName();
	}

	@Override
	protected List<Integer> getClassContas() {
		return CLASSES_CONTAS_PATRIMONIAIS;
	}
	
	@Override
	protected Logger getLogger() {
		return logger;
	}
}
