package br.gov.ce.sefaz.siconfi.util;

import java.util.HashMap;
import java.util.Map;

public class APIQueryParamUtil {

	public static final String API_QUERY_PARAM_AN_REFERENCIA = "an_referencia";

	public static final String API_QUERY_PARAM_ME_REFERENCIA = "me_referencia";

	public static final String API_QUERY_PARAM_AN_EXERCICIO = "an_exercicio";

	public static final String API_QUERY_PARAM_ID_ENTE = "id_ente"; 

	public static final String API_QUERY_PARAM_NO_ANEXO = "no_anexo"; 

	public static final String API_QUERY_PARAM_IN_PERIODICIDADE = "in_periodicidade"; 

	public static final String API_QUERY_PARAM_NR_PERIODO = "nr_periodo"; 

	public static final String API_QUERY_PARAM_CO_TIPO_DEMONSTRATIVO = "co_tipo_demonstrativo"; 

	public static final String API_QUERY_PARAM_CO_TIPO_MATRIZ = "co_tipo_matriz"; 

	public static final String API_QUERY_PARAM_CO_PODER = "co_poder"; 

	public static final String API_QUERY_PARAM_CLASSE_CONTA = "classe_conta"; 

	public static final String API_QUERY_PARAM_ID_TV = "id_tv"; 

	private Map<String, Object> mapQueryParam = new HashMap<String, Object>();
	
	public APIQueryParamUtil addParamAnExercicio(Integer exercicio) {
		if(exercicio!=null) {
			mapQueryParam.put(API_QUERY_PARAM_AN_EXERCICIO, exercicio);			
		}
		return this;
	}

	public APIQueryParamUtil addParamAnReferencia(Integer anoReferencia) {
		if(anoReferencia!=null) {
			mapQueryParam.put(API_QUERY_PARAM_AN_REFERENCIA, anoReferencia);			
		}
		return this;
	}

	public APIQueryParamUtil addParamIdEnte(String codIbge) {
		if(!Utils.isStringVazia(codIbge)) {
			mapQueryParam.put(API_QUERY_PARAM_ID_ENTE, codIbge);			
		}
		return this;
	}

	public APIQueryParamUtil addParamPeriodo(Integer periodo) {
		if(periodo!=null) {
			mapQueryParam.put(API_QUERY_PARAM_NR_PERIODO, periodo);			
		}
		return this;
	}

	public APIQueryParamUtil addParamMesReferencia(Integer mes) {
		if(mes!=null) {
			mapQueryParam.put(API_QUERY_PARAM_ME_REFERENCIA, mes);			
		}
		return this;
	}

	public APIQueryParamUtil addParamIndicadorPeriodiciadade(String indicadorPeriodicidade) {
		if(!Utils.isStringVazia(indicadorPeriodicidade)) {
			mapQueryParam.put(API_QUERY_PARAM_IN_PERIODICIDADE, indicadorPeriodicidade);			
		}
		return this;
	}

	public APIQueryParamUtil addParamPoder(String poder) {
		if(!Utils.isStringVazia(poder)) {
			mapQueryParam.put(API_QUERY_PARAM_CO_PODER, poder);			
		}
		return this;
	}

	public APIQueryParamUtil addParamTipoDemonstrativo(String tipoDemonstrativo) {
		if(!Utils.isStringVazia(tipoDemonstrativo)) {
			mapQueryParam.put(API_QUERY_PARAM_CO_TIPO_DEMONSTRATIVO, tipoDemonstrativo);			
		}
		return this;
	}

	public APIQueryParamUtil addParamAnexo(String anexo) {
		if(!Utils.isStringVazia(anexo)) {
			mapQueryParam.put(API_QUERY_PARAM_NO_ANEXO, anexo.replaceAll(" ", "%20"));			
		}
		return this;
	}

	public APIQueryParamUtil addParamClasseConta(Integer classeConta) {
		if(classeConta!=null) {
			mapQueryParam.put(API_QUERY_PARAM_CLASSE_CONTA, classeConta);			
		}
		return this;
	}

	public APIQueryParamUtil addParamTipoMatriz(String tipoMatriz) {
		if(!Utils.isStringVazia(tipoMatriz)) {
			mapQueryParam.put(API_QUERY_PARAM_CO_TIPO_MATRIZ, tipoMatriz);			
		}
		return this;
	}

	public APIQueryParamUtil addParamTipoValorMatriz(String tipoValorMatriz) {
		if(!Utils.isStringVazia(tipoValorMatriz)) {
			mapQueryParam.put(API_QUERY_PARAM_ID_TV, tipoValorMatriz);			
		}
		return this;
	}

	public Map<String, Object> getMapQueryParam() {
		return mapQueryParam;
	}
}
