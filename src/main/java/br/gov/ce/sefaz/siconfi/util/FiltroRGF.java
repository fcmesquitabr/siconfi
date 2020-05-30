package br.gov.ce.sefaz.siconfi.util;

import java.util.List;

import br.gov.ce.sefaz.siconfi.enums.Poder;

public class FiltroRGF extends FiltroBase {
	
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
	
	public FiltroRGF() {
		super();
	}

	public FiltroRGF(Builder builder) {
		super(builder);
		this.listaAnexos = builder.listaAnexos;
		this.listaPoderes = builder.listaPoderes;
	}


	public static class Builder extends FiltroBase.Builder<Builder> {
		
		private List<String> listaAnexos;		
		private List<Poder> listaPoderes;
		
		public Builder() {}
		
		public Builder listaAnexos(List<String> listaAnexos) {
			this.listaAnexos = listaAnexos;
			return this;
		}

		public Builder listaPoderes(List<Poder> listaPoderes) {
			this.listaPoderes= listaPoderes;
			return this;
		}

		public FiltroRGF build() {
			return new FiltroRGF(this);
		}
	}
}
