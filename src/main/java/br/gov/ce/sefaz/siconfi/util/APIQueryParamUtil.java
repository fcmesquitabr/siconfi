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
	
	public static final String API_QUERY_PARAM_OFF_SET = "offset";

	private Map<String, Object> mapQueryParam = new HashMap<>();
	
	public APIQueryParamUtil addParamAnExercicio(Integer exercicio) {
		if(exercicio!=null) {
			mapQueryParam.put(API_QUERY_PARAM_AN_EXERCICIO, exercicio);			
		}
		return this;
	}

	public Integer getParamAnExercicio() {
		return (Integer) mapQueryParam.get(API_QUERY_PARAM_AN_EXERCICIO);			
	}

	public APIQueryParamUtil addParamAnReferencia(Integer anoReferencia) {
		if(anoReferencia!=null) {
			mapQueryParam.put(API_QUERY_PARAM_AN_REFERENCIA, anoReferencia);			
		}
		return this;
	}

	public Integer getParamAnReferencia() {
		return (Integer) mapQueryParam.get(API_QUERY_PARAM_AN_REFERENCIA);			
	}

	public APIQueryParamUtil addParamIdEnte(String codIbge) {
		if(!Utils.isStringVazia(codIbge)) {
			mapQueryParam.put(API_QUERY_PARAM_ID_ENTE, codIbge);			
		}
		return this;
	}

	public String getParamIdEnte() {
		return (String) mapQueryParam.get(API_QUERY_PARAM_ID_ENTE);			
	}

	public APIQueryParamUtil addParamPeriodo(Integer periodo) {
		if(periodo!=null) {
			mapQueryParam.put(API_QUERY_PARAM_NR_PERIODO, periodo);			
		}
		return this;
	}

	public Integer getParamPeriodo() {
		return (Integer) mapQueryParam.get(API_QUERY_PARAM_NR_PERIODO);			
	}

	public APIQueryParamUtil addParamMesReferencia(Integer mes) {
		if(mes!=null) {
			mapQueryParam.put(API_QUERY_PARAM_ME_REFERENCIA, mes);			
		}
		return this;
	}

	public Integer getParamMesReferencia() {
		return (Integer) mapQueryParam.get(API_QUERY_PARAM_ME_REFERENCIA);			
	}

	public APIQueryParamUtil addParamIndicadorPeriodiciadade(String indicadorPeriodicidade) {
		if(!Utils.isStringVazia(indicadorPeriodicidade)) {
			mapQueryParam.put(API_QUERY_PARAM_IN_PERIODICIDADE, indicadorPeriodicidade);			
		}
		return this;
	}

	public String getParamIndicadorPeriodiciadade() {
		return (String) mapQueryParam.get(API_QUERY_PARAM_IN_PERIODICIDADE);			
	}

	public APIQueryParamUtil addParamPoder(String poder) {
		if(!Utils.isStringVazia(poder)) {
			mapQueryParam.put(API_QUERY_PARAM_CO_PODER, poder);			
		}
		return this;
	}

	public String getParamPoder() {
		return (String) mapQueryParam.get(API_QUERY_PARAM_CO_PODER);			
	}

	public APIQueryParamUtil addParamTipoDemonstrativo(String tipoDemonstrativo) {
		if(!Utils.isStringVazia(tipoDemonstrativo)) {
			mapQueryParam.put(API_QUERY_PARAM_CO_TIPO_DEMONSTRATIVO, tipoDemonstrativo);			
		}
		return this;
	}

	public String getParamTipoDemonstrativo() {
		return (String) mapQueryParam.get(API_QUERY_PARAM_CO_TIPO_DEMONSTRATIVO);			
	}

	public APIQueryParamUtil addParamAnexo(String anexo) {
		if(!Utils.isStringVazia(anexo)) {
			mapQueryParam.put(API_QUERY_PARAM_NO_ANEXO, anexo.replace(" ", "%20"));			
		}
		return this;
	}

	public String getParamAnexo() {
		return (String) mapQueryParam.get(API_QUERY_PARAM_NO_ANEXO);			
	}

	public APIQueryParamUtil addParamClasseConta(Integer classeConta) {
		if(classeConta!=null) {
			mapQueryParam.put(API_QUERY_PARAM_CLASSE_CONTA, classeConta);			
		}
		return this;
	}

	public Integer getParamClasseConta() {
		return (Integer) mapQueryParam.get(API_QUERY_PARAM_CLASSE_CONTA);			
	}

	public APIQueryParamUtil addParamTipoMatriz(String tipoMatriz) {
		if(!Utils.isStringVazia(tipoMatriz)) {
			mapQueryParam.put(API_QUERY_PARAM_CO_TIPO_MATRIZ, tipoMatriz);			
		}
		return this;
	}

	public String getParamTipoMatriz() {
		return (String) mapQueryParam.get(API_QUERY_PARAM_CO_TIPO_MATRIZ);			
	}

	public APIQueryParamUtil addParamTipoValorMatriz(String tipoValorMatriz) {
		if(!Utils.isStringVazia(tipoValorMatriz)) {
			mapQueryParam.put(API_QUERY_PARAM_ID_TV, tipoValorMatriz);			
		}
		return this;
	}

	public String getParamTipoValorMatriz() {
		return (String) mapQueryParam.get(API_QUERY_PARAM_ID_TV);			
	}

	public APIQueryParamUtil addParamOffset(Integer offset) {
		if(offset != null) {
			mapQueryParam.put(API_QUERY_PARAM_OFF_SET, offset);			
		}
		return this;
	}

	public Integer getParamOffset() {
		return (Integer) mapQueryParam.get(API_QUERY_PARAM_OFF_SET);			
	}

	public Map<String, Object> getMapQueryParam() {
		return mapQueryParam;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mapQueryParam == null) ? 0 : mapQueryParam.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		APIQueryParamUtil other = (APIQueryParamUtil) obj;
		if (mapQueryParam == null) {
			return other.mapQueryParam == null;
		} else if (mapQueryParam != null && other.mapQueryParam == null)
			return false;
		
		if (getParamAnExercicio() == null) {
			if (other.getParamAnExercicio() != null)
				return false;
		} else if (!getParamAnExercicio().equals(other.getParamAnExercicio()))
			return false;

		if (getParamAnReferencia() == null) {
			if (other.getParamAnReferencia() != null)
				return false;
		} else if (!getParamAnReferencia().equals(other.getParamAnReferencia()))
			return false;

		if (getParamIdEnte() == null) {
			if (other.getParamIdEnte() != null)
				return false;
		} else if (!getParamIdEnte().equals(other.getParamIdEnte()))
			return false;

		if (getParamPeriodo() == null) {
			if (other.getParamPeriodo() != null)
				return false;
		} else if (!getParamPeriodo().equals(other.getParamPeriodo()))
			return false;

		if (getParamMesReferencia() == null) {
			if (other.getParamMesReferencia() != null)
				return false;
		} else if (!getParamMesReferencia().equals(other.getParamMesReferencia()))
			return false;

		if (getParamIndicadorPeriodiciadade() == null) {
			if (other.getParamIndicadorPeriodiciadade() != null)
				return false;
		} else if (!getParamIndicadorPeriodiciadade().equals(other.getParamIndicadorPeriodiciadade()))
			return false;

		if (getParamAnexo() == null) {
			if (other.getParamAnexo() != null)
				return false;
		} else if (!getParamAnexo().equals(other.getParamAnexo()))
			return false;

		if (getParamPoder() == null) {
			if (other.getParamPoder() != null)
				return false;
		} else if (!getParamPoder().equals(other.getParamPoder()))
			return false;

		if (getParamTipoDemonstrativo() == null) {
			if (other.getParamTipoDemonstrativo() != null)
				return false;
		} else if (!getParamTipoDemonstrativo().equals(other.getParamTipoDemonstrativo()))
			return false;

		if (getParamClasseConta() == null) {
			if (other.getParamClasseConta() != null)
				return false;
		} else if (!getParamClasseConta().equals(other.getParamClasseConta()))
			return false;

		if (getParamTipoMatriz() == null) {
			if (other.getParamTipoMatriz() != null)
				return false;
		} else if (!getParamTipoMatriz().equals(other.getParamTipoMatriz()))
			return false;

		if (getParamTipoValorMatriz() == null) {
			if (other.getParamTipoValorMatriz() != null)
				return false;
		} else if (!getParamTipoValorMatriz().equals(other.getParamTipoValorMatriz()))
			return false;

		return true;
	}	
}
