package br.gov.ce.sefaz.siconfi.opcoes;

import java.util.List;

import br.gov.ce.sefaz.siconfi.enums.Poder;
import br.gov.ce.sefaz.siconfi.util.Utils;

public class OpcoesCargaDadosRGF extends OpcoesCargaDados {
	
	private List<String> listaAnexos;
	
	private List<Poder> listaPoderes;
	
	public boolean isListaAnexosVazia() {
		return Utils.isEmptyCollection(getListaAnexos());
	}

	public boolean isListaPoderesVazia() {
		return Utils.isEmptyCollection(getListaPoderes());
	}

	public List<String> getListaAnexos() {
		return listaAnexos;
	}

	public void setListaAnexos(List<String> listaAnexos) {
		this.listaAnexos = listaAnexos;
	}

	public List<Poder> getListaPoderes() {
		return listaPoderes;
	}

	public void setListaPoderes(List<Poder> listaPoderes) {
		this.listaPoderes = listaPoderes;
	}
	
	public OpcoesCargaDadosRGF() {
		super();
	}

	public OpcoesCargaDadosRGF(Builder builder) {
		super(builder);
		this.listaAnexos = builder.listaAnexos;
		this.listaPoderes = builder.listaPoderes;
	}


	public static class Builder extends OpcoesCargaDados.Builder<Builder> {
		
		private List<String> listaAnexos;		
		private List<Poder> listaPoderes;
		
		public Builder() {
			super();
		}
		
		public Builder listaAnexos(List<String> listaAnexos) {
			this.listaAnexos = listaAnexos;
			return this;
		}

		public Builder listaPoderes(List<Poder> listaPoderes) {
			this.listaPoderes= listaPoderes;
			return this;
		}

		@Override
		public OpcoesCargaDadosRGF build() {
			return new OpcoesCargaDadosRGF(this);
		}
	}
}
