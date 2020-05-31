package br.gov.ce.sefaz.siconfi.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.gov.ce.sefaz.siconfi.entity.MatrizSaldoContabeisOrcamentaria;
import br.gov.ce.sefaz.siconfi.response.MatrizSaldoContabeisOrcamentariaResponse;
import br.gov.ce.sefaz.siconfi.response.MatrizSaldoContabeisResponse;

public class MSCOrcamentariaService extends MSCService<MatrizSaldoContabeisOrcamentaria> {

	private static final Logger logger = LogManager.getLogger(MSCOrcamentariaService.class);

	private static String[] COLUNAS_ARQUIVO_CSV = new String[] { "exercicio", "mes_referencia", "cod_ibge", "poder_orgao", "tipo_matriz",
			"class_conta", "natureza_conta", "conta_contabil", "funcao", "subfuncao", "educacao_saude", 
			"natureza_despesa", "ano_inscricao", "natureza_receita","ano_fonte_recursos", "fonte_recursos", 
			"data_referencia", "entrada_msc", "tipo_valor", "valorFormatado"};
	
	private static String NOME_PADRAO_ARQUIVO_CSV = "msc_orcamentaria.csv";

	private static final String API_PATH_MSC_ORCAMENTARIA = "msc_orcamentaria";

	public MSCOrcamentariaService() {
		super();
	}

	@Override
	protected String[] getColunasArquivoCSV() {
		return COLUNAS_ARQUIVO_CSV;
	}

	@Override
	protected Class<MatrizSaldoContabeisOrcamentaria> getEntityClass() {
		return MatrizSaldoContabeisOrcamentaria.class;
	}
	
	@Override
	protected String getNomePadraoArquivoCSV() {
		return NOME_PADRAO_ARQUIVO_CSV;
	}

	@Override
	protected String getAPIPath() {
		return API_PATH_MSC_ORCAMENTARIA;
	}

	@Override
	protected Class<? extends MatrizSaldoContabeisResponse<MatrizSaldoContabeisOrcamentaria>> getResponseClassType() {
		return MatrizSaldoContabeisOrcamentariaResponse.class;
	}

	@Override
	protected String getEntityName() {
		return MatrizSaldoContabeisOrcamentaria.class.getSimpleName();
	}

	@Override
	protected List<Integer> getClassContas() {
		return CLASSES_CONTAS_ORCAMENTARIAS;
	}
	
	@Override
	protected Logger getLogger() {
		return logger;
	}
}
