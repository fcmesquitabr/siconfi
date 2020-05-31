package br.gov.ce.sefaz.siconfi.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.gov.ce.sefaz.siconfi.entity.MatrizSaldoContabeisControle;
import br.gov.ce.sefaz.siconfi.response.MatrizSaldoContabeisControleResponse;
import br.gov.ce.sefaz.siconfi.response.MatrizSaldoContabeisResponse;

public class MSCControleService extends MSCService<MatrizSaldoContabeisControle>{

	private static final Logger logger = LogManager.getLogger(MSCControleService.class);

	private static String[] COLUNAS_ARQUIVO_CSV = new String[] { "exercicio", "mes_referencia", "cod_ibge", "poder_orgao", "tipo_matriz",
			"class_conta", "natureza_conta", "conta_contabil", "funcao", "subfuncao", "educacao_saude", 
			"natureza_despesa", "ano_inscricao", "ano_fonte_recursos", "fonte_recursos", 
			"data_referencia", "entrada_msc", "tipo_valor", "valorFormatado"};
	
	private static String NOME_PADRAO_ARQUIVO_CSV = "msc_controle.csv";

	private static final String API_PATH_MSC_CONTROLE = "msc_controle";


	@Override
	protected String[] getColunasArquivoCSV() {
		return COLUNAS_ARQUIVO_CSV;
	}

	@Override
	protected Class<MatrizSaldoContabeisControle> getEntityClass() {
		return MatrizSaldoContabeisControle.class;
	}
	
	@Override
	protected String getNomePadraoArquivoCSV() {
		return NOME_PADRAO_ARQUIVO_CSV;
	}

	@Override
	protected String getAPIPath() {
		return API_PATH_MSC_CONTROLE;
	}

	@Override
	protected Class<? extends MatrizSaldoContabeisResponse<MatrizSaldoContabeisControle>> getResponseClassType() {
		return MatrizSaldoContabeisControleResponse.class;
	}

	@Override
	protected String getEntityName() {
		return MatrizSaldoContabeisControle.class.getSimpleName();
	}

	@Override
	protected List<Integer> getClassContas() {
		return CLASSES_CONTAS_CONTROLE;
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}
}
