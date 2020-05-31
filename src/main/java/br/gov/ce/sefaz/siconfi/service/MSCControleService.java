package br.gov.ce.sefaz.siconfi.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.gov.ce.sefaz.siconfi.entity.MatrizSaldoContabeisControle;
import br.gov.ce.sefaz.siconfi.enums.TipoMatrizSaldoContabeis;
import br.gov.ce.sefaz.siconfi.enums.TipoValorMatrizSaldoContabeis;
import br.gov.ce.sefaz.siconfi.response.MatrizSaldoContabeisControleResponse;

public class MSCControleService extends SiconfiService<MatrizSaldoContabeisControle>{

	private static final Logger logger = LogManager.getLogger(MSCControleService.class);

	private static String[] COLUNAS_ARQUIVO_CSV = new String[] { "exercicio", "mes_referencia", "cod_ibge", "poder_orgao", "tipo_matriz",
			"class_conta", "natureza_conta", "conta_contabil", "funcao", "subfuncao", "educacao_saude", 
			"natureza_despesa", "ano_inscricao", "natureza_receita","ano_fonte_recursos", "fonte_recursos", 
			"data_referencia", "entrada_msc", "tipo_valor", "valorFormatado"};
	
	private static String NOME_PADRAO_ARQUIVO_CSV = "msc_orcamentaria.csv";

	private static final String API_PATH_MSC_ORCAMENTARIA = "msc_orcamentaria";

	private EnteService enteService;


	@Override
	public void excluirTodos() {
		logger.info("Excluindo dados do banco de dados...");
		int i = getEntityManager().createQuery("DELETE FROM MatrizSaldoContabeisControle msc")
				.executeUpdate();
		logger.info("Linhas excluídas:" + i);
	}

	public List<MatrizSaldoContabeisControle> consultarNaApi() {
		return new ArrayList<MatrizSaldoContabeisControle>();
	}

	private MatrizSaldoContabeisControleResponse obterResponseDaApi(Integer exercicio, Integer mes, String codigoIbge,
			TipoMatrizSaldoContabeis tipoMatriz, Integer classeConta, TipoValorMatrizSaldoContabeis tipoValorMatriz) {

		long ini = System.currentTimeMillis();

		this.webTarget = this.client.target(URL_SERVICE).path(API_PATH_MSC_ORCAMENTARIA)
				.queryParam(API_QUERY_PARAM_AN_REFERENCIA, exercicio)
				.queryParam(API_QUERY_PARAM_ME_REFERENCIA, mes)
				.queryParam(API_QUERY_PARAM_ID_ENTE, codigoIbge)
				.queryParam(API_QUERY_PARAM_CO_TIPO_MATRIZ, tipoMatriz.getCodigo())
				.queryParam(API_QUERY_PARAM_CLASSE_CONTA, classeConta)
				.queryParam(API_QUERY_PARAM_ID_TV, tipoValorMatriz.getCodigo());
		Invocation.Builder invocationBuilder = this.webTarget.request(API_RESPONSE_TYPE);
		logger.info("Get na API: " + this.webTarget.getUri().toString());
		Response response = invocationBuilder.get();
		MatrizSaldoContabeisControleResponse mscResponse = response.readEntity(MatrizSaldoContabeisControleResponse.class);
		
		long fim = System.currentTimeMillis();
		logger.debug("Tempo para consultar a MSC na API:" + (fim - ini));
		return mscResponse;
	}
	
	private EnteService getEnteService() {
		if(enteService == null) {
			enteService = new EnteService();
		}
		return enteService;
	}

	@Override
	protected String[] getColunasArquivoCSV() {
		return COLUNAS_ARQUIVO_CSV;
	}

	@Override
	protected Class<MatrizSaldoContabeisControle> getClassType() {
		return MatrizSaldoContabeisControle.class;
	}
	
	@Override
	protected String getNomePadraoArquivoCSV() {
		return NOME_PADRAO_ARQUIVO_CSV;
	}

}
