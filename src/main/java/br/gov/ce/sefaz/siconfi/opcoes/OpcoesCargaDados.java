package br.gov.ce.sefaz.siconfi.opcoes;

import java.util.ArrayList;
import java.util.List;

import br.gov.ce.sefaz.siconfi.enums.Esfera;
import br.gov.ce.sefaz.siconfi.enums.OpcaoSalvamentoDados;
import br.gov.ce.sefaz.siconfi.util.APIQueryParamUtil;
import br.gov.ce.sefaz.siconfi.util.Constantes;
import br.gov.ce.sefaz.siconfi.util.Utils;

public class OpcoesCargaDados {
	
	private List<Integer> exercicios;
	
	private List<Integer> periodos;

	private Esfera esfera;
	
	private List<String> codigosIBGE;
	
	private OpcaoSalvamentoDados opcaoSalvamento;

	private String nomeArquivo;

	public OpcoesCargaDados() {
		super();
	}

	public List<APIQueryParamUtil> obterListaAPIQueryParamUtil(){
		List<Integer> listaExercicios = !Utils.isEmptyCollection(getExercicios()) ? getExercicios()
				: Constantes.EXERCICIOS_DISPONIVEIS;
		
		List<APIQueryParamUtil> listaAPIQueryParamUtil = new ArrayList<>();				
		for (Integer exercicio : listaExercicios) {
			listaAPIQueryParamUtil.addAll(obterListaAPIQueryParamUtil(exercicio));
		}
		return listaAPIQueryParamUtil;		
	}

	public List<APIQueryParamUtil> obterListaAPIQueryParamUtil(Integer exercicio){		
		List<APIQueryParamUtil> listaAPIQueryParamUtil = new ArrayList<>();				
		if(!Utils.isEmptyCollection(getPeriodos())) {
			for (Integer periodo:getPeriodos()) {
				listaAPIQueryParamUtil.addAll(obterListaAPIQueryParamUtil(exercicio, periodo));
			}
		} else {
			for (String codigoIbge : getCodigosIBGE()) {
				APIQueryParamUtil apiQueryParamUtil = new APIQueryParamUtil();
				apiQueryParamUtil.addParamAnReferencia(exercicio).addParamIdEnte(codigoIbge);
				listaAPIQueryParamUtil.add(apiQueryParamUtil);
			}			
		}
		return listaAPIQueryParamUtil;		
	}

	public List<APIQueryParamUtil> obterListaAPIQueryParamUtil(Integer exercicio, Integer periodo){		
		List<APIQueryParamUtil> listaAPIQueryParamUtil = new ArrayList<>();				
		for (String codigoIbge : getCodigosIBGE()) {
			APIQueryParamUtil apiQueryParamUtil = new APIQueryParamUtil();
			apiQueryParamUtil.addParamAnReferencia(exercicio).addParamPeriodo(periodo).addParamIdEnte(codigoIbge);
			listaAPIQueryParamUtil.add(apiQueryParamUtil);
		}
		return listaAPIQueryParamUtil;		
	}

	public boolean isNomeArquivoVazio() {
		return Utils.isStringVazia(getNomeArquivo());
	}

	public boolean isListaExerciciosVazia() {
		return Utils.isEmptyCollection(getExercicios());
	}

	public boolean isListaPeriodosVazia() {
		return Utils.isEmptyCollection(getPeriodos());
	}

	public boolean isExisteCodigosIbge() {
		return !Utils.isEmptyCollection(getCodigosIBGE());
	}
	
	public List<Integer> getExercicios() {
		return exercicios;
	}

	public void setExercicios(List<Integer> exercicios) {
		this.exercicios = exercicios;
	}

	public List<Integer> getPeriodos() {
		return periodos;
	}

	public void setPeriodos(List<Integer> periodos) {
		this.periodos = periodos;
	}

	public Esfera getEsfera() {
		return esfera;
	}

	public void setEsfera(Esfera esfera) {
		this.esfera = esfera;
	}

	public List<String> getCodigosIBGE() {
		return codigosIBGE;
	}

	public void setCodigosIBGE(List<String> codigosIBGE) {
		this.codigosIBGE = codigosIBGE;
	}

	public OpcaoSalvamentoDados getOpcaoSalvamento() {
		return opcaoSalvamento;
	}

	public void setOpcaoSalvamento(OpcaoSalvamentoDados opcaoSalvamento) {
		this.opcaoSalvamento = opcaoSalvamento;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public static class Builder<T extends Builder<T>> {

		private List<Integer> exercicios;
		private List<Integer> periodos;
		private Esfera esfera;
		private List<String> codigosIBGE;
		private OpcaoSalvamentoDados opcaoSalvamento;
		private String nomeArquivo;
		
		@SuppressWarnings("unchecked")
		public T exercicios (List<Integer> exercicios) {
			this.exercicios = exercicios;
			return (T) this;
		}
		
		@SuppressWarnings("unchecked")
		public T periodos (List<Integer> periodos) {
			this.periodos = periodos;
			return (T) this;
		}

		@SuppressWarnings("unchecked")
		public T esfera (Esfera esfera) {
			this.esfera = esfera;
			return (T) this;
		}

		@SuppressWarnings("unchecked")
		public T codigosIbge (List<String> codigosIbge) {
			this.codigosIBGE = codigosIbge;
			return (T) this;
		}

		@SuppressWarnings("unchecked")
		public T opcaoSalvamentoDados (OpcaoSalvamentoDados opcaoSalvamento) {
			this.opcaoSalvamento = opcaoSalvamento;
			return (T) this;
		}
		
		@SuppressWarnings("unchecked")
		public T nomeArquivo (String nomeArquivo) {
			this.nomeArquivo = nomeArquivo;
			return (T) this;
		}
		
		public OpcoesCargaDados build() {
			return new OpcoesCargaDados(this);
		}
	}
	
	protected OpcoesCargaDados (Builder<?> builder) {
		this.codigosIBGE = builder.codigosIBGE;
		this.esfera = builder.esfera;
		this.exercicios = builder.exercicios;
		this.periodos = builder.periodos;
		this.nomeArquivo = builder.nomeArquivo;
		this.opcaoSalvamento = builder.opcaoSalvamento;
	}

}
