package br.gov.ce.sefaz.siconfi.service;

import java.util.List;

import org.apache.logging.log4j.Logger;

import br.gov.ce.sefaz.siconfi.entity.MatrizSaldoContabeisOrcamentaria;
import br.gov.ce.sefaz.siconfi.util.Constantes;
import br.gov.ce.sefaz.siconfi.util.LoggerUtil;

public class MSCOrcamentariaService extends MSCService<MatrizSaldoContabeisOrcamentaria> {

	private Logger logger = null;

	private static final String[] COLUNAS_ARQUIVO_CSV = new String[] { "exercicio", "mesReferencia", "codigoIbge", "poderOrgao", "tipoMatriz",
			"classeConta", "naturezaConta", "contaContabil", "funcao", "subfuncao", "educacaoSaude", 
			"naturezaDespesa", "anoInscricao", "naturezaReceita","anoFonteRecursos", "fonteRecursos", 
			"dataReferencia", "entradaMsc", "tipoValor", "valorFormatado"};
	
	private static final String NOME_PADRAO_ARQUIVO_CSV = "msc_orcamentaria.csv";

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
	protected String getEntityName() {
		return MatrizSaldoContabeisOrcamentaria.class.getSimpleName();
	}

	@Override
	protected List<Integer> getClassContas() {
		return Constantes.CLASSES_CONTAS_ORCAMENTARIAS;
	}
	
	@Override
	protected Logger getLogger() {
		if(logger == null) {
			logger = LoggerUtil.createLogger(MSCOrcamentariaService.class);
		}
		return logger;
	}

	@Override
	protected String getApiPath() {
		return API_PATH_MSC_ORCAMENTARIA;
	}
}
