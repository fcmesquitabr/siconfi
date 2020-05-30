package br.gov.ce.sefaz.siconfi.util;

import java.util.List;

import br.gov.ce.sefaz.siconfi.enums.Esfera;
import br.gov.ce.sefaz.siconfi.enums.OpcaoSalvamentoDados;

public class FiltroBase {
	
	private List<Integer> exercicios;

	private Esfera esfera;
	
	private List<String> codigosIBGE;
	
	private OpcaoSalvamentoDados opcaoSalvamento;

	private String nomeArquivo;

	public FiltroBase() {
		super();
	}

	public boolean isNomeArquivoVazio() {
		return Utils.isStringVazia(getNomeArquivo());
	}

	public boolean isListaExerciciosVazia() {
		return Utils.isEmptyCollection(getExercicios());
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
		
		public FiltroBase build() {
			return new FiltroBase(this);
		}
	}
	
	protected FiltroBase (Builder<?> builder) {
		this.codigosIBGE = builder.codigosIBGE;
		this.esfera = builder.esfera;
		this.exercicios = builder.exercicios;
		this.nomeArquivo = builder.nomeArquivo;
		this.opcaoSalvamento = builder.opcaoSalvamento;
	}

}
