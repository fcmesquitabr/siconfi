package br.gov.ce.sefaz.siconfi.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.gov.ce.sefaz.siconfi.entity.DeclaracaoContasAnuais;
import br.gov.ce.sefaz.siconfi.entity.ExtratoEntrega;
import br.gov.ce.sefaz.siconfi.enums.Entregavel;
import br.gov.ce.sefaz.siconfi.opcoes.OpcoesCargaDadosDCA;
import br.gov.ce.sefaz.siconfi.util.APIQueryParamUtil;
import br.gov.ce.sefaz.siconfi.util.SiconfiResponse;
import br.gov.ce.sefaz.siconfi.util.Utils;

public class DCAService extends SiconfiService<DeclaracaoContasAnuais, OpcoesCargaDadosDCA>{

	private static final Logger logger = LogManager.getLogger(DCAService.class);

	public static final List<String> ANEXOS_DCA = Arrays.asList("Anexo I-AB", "Anexo I-C", "Anexo I-D", "Anexo I-E", 
			"Anexo I-F", "Anexo I-G", "Anexo I-HI", "DCA-Anexo I-AB", "DCA-Anexo I-C", "DCA-Anexo I-D", 
			"DCA-Anexo I-E", "DCA-Anexo I-F", "DCA-Anexo I-G", "DCA-Anexo I-HI");
	
	private static final String[] COLUNAS_ARQUIVO_CSV = new String[] { "exercicio", "uf", "cod_ibge", "instituicao", "anexo",
			"cod_conta", "conta", "coluna", "rotulo", "populacao", "valorFormatado" };
	
	private static final String NOME_PADRAO_ARQUIVO_CSV = "dca.csv";

	private static final String API_PATH_DCA = "dca";

	private EnteService enteService;

	private ExtratoEntregaService extratoEntregaService;

	public DCAService() {
		super();
	}
	
	@Override
	protected void excluir(OpcoesCargaDadosDCA filtro) {
		logger.info("Excluindo dados do banco de dados...");
		
		StringBuilder queryBuilder = new StringBuilder("DELETE FROM DeclaracaoContasAnuais dca WHERE dca.exercicio IN (:exercicios) ");

		List<String> listaCodigoIbge = getEnteService().obterListaCodigosIbgeNaAPI(filtro);
		if(!Utils.isEmptyCollection(listaCodigoIbge)) {
			queryBuilder.append(" AND dca.cod_ibge IN (:codigosIbge)");
		}
		if(!filtro.isListaAnexosVazia()) {
			queryBuilder.append(" AND dca.anexo IN (:listaAnexos)");
		}
		
		Query query = getEntityManager().createQuery(queryBuilder.toString());
		query.setParameter("exercicios", filtro.getExercicios());
		
		if(!Utils.isEmptyCollection(listaCodigoIbge)) {
			query.setParameter("codigosIbge", listaCodigoIbge);
		}
		if(!filtro.isListaAnexosVazia()) {
			query.setParameter("listaAnexos", filtro.getListaAnexos());
		}

		int i = query.executeUpdate();
		logger.info("Linhas exclu�das:" + i);
	}

	@Override
	protected void consultarNaApiEGerarSaidaDados(OpcoesCargaDadosDCA opcoesCargaDados,
			Integer exercicio) {
		
		if (opcoesCargaDados.isConsiderarExtratoEntrega()) {

			List<ExtratoEntrega> listaExtrato = getExtratoEntregaService().consultarNaBase(opcoesCargaDados, exercicio, Entregavel.DCA);
			for(ExtratoEntrega extrato: listaExtrato) {
				consultarNaApiEGerarSaidaDados(opcoesCargaDados, exercicio, extrato.getCod_ibge());
			}
			
		} else {
			
			List<String> listaCodigoIbge = getEnteService().obterListaCodigosIbgeNaAPI(opcoesCargaDados);
			for (String codigoIbge : listaCodigoIbge) {
				consultarNaApiEGerarSaidaDados(opcoesCargaDados, exercicio, codigoIbge);
			}	
			
		}		
	}

	private void consultarNaApiEGerarSaidaDados (OpcoesCargaDadosDCA opcoes,
			Integer exercicio, String codigoIbge) {

		List<String> listaAnexos = !opcoes.isListaAnexosVazia() ? opcoes.getListaAnexos()
				: ANEXOS_DCA;

		for (String anexo : listaAnexos) {
			APIQueryParamUtil apiQueryParamUtil = new APIQueryParamUtil().addParamAnExercicio(exercicio)
					.addParamIdEnte(codigoIbge).addParamAnexo(anexo);
			List<DeclaracaoContasAnuais> listaDcaParcial = consultarNaApi(apiQueryParamUtil);
			gerarSaidaDados(getOpcoesParcial(opcoes, exercicio, codigoIbge,anexo), listaDcaParcial);
			aguardarUmSegundo();
		}
	}

	private OpcoesCargaDadosDCA getOpcoesParcial(OpcoesCargaDadosDCA opcoes, Integer exercicio,
			String codigoIbge, String anexo) {
		OpcoesCargaDadosDCA opcoesParcial = new OpcoesCargaDadosDCA();
		opcoesParcial.setOpcaoSalvamento(opcoes.getOpcaoSalvamento());
		opcoesParcial.setNomeArquivo(opcoes.getNomeArquivo());
		opcoesParcial.setExercicios(Arrays.asList(exercicio));
		opcoesParcial.setCodigosIBGE(Arrays.asList(codigoIbge));
		opcoesParcial.setListaAnexos(Arrays.asList(anexo));
		return opcoesParcial;
	}

//	@Override
	protected List<DeclaracaoContasAnuais> lerEntidades(Response response) {
		SiconfiResponse<DeclaracaoContasAnuais> dcaResponse = response
				.readEntity(new GenericType<SiconfiResponse<DeclaracaoContasAnuais>>() {
				});
		return dcaResponse != null ? dcaResponse.getItems() : new ArrayList<DeclaracaoContasAnuais>();
	}

	private EnteService getEnteService() {
		if(enteService == null) {
			enteService = new EnteService();
		}
		return enteService;
	}
	
	private ExtratoEntregaService getExtratoEntregaService() {
		if(extratoEntregaService == null) {
			extratoEntregaService = new ExtratoEntregaService();
		}
		return extratoEntregaService;
	}

	@Override
	protected String getEntityName() {
		return DeclaracaoContasAnuais.class.getSimpleName();
	}

	@Override
	protected String[] getColunasArquivoCSV() {
		return COLUNAS_ARQUIVO_CSV;
	}

	@Override
	protected Class<DeclaracaoContasAnuais> getEntityClass() {
		return DeclaracaoContasAnuais.class;
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
		return API_PATH_DCA;
	}
}
