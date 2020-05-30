package br.gov.ce.sefaz.siconfi.util;

import java.util.List;

public class FiltroRREO extends FiltroBase {
	
	private List<String> listaAnexos;
	
	public FiltroRREO() {
		super();
	}

	public boolean isListaAnexosVazia() {
		return Utils.isEmptyCollection(getListaAnexos());
	}

	public List<String> getListaAnexos() {
		return listaAnexos;
	}

	public void setListaAnexos(List<String> listaAnexos) {
		this.listaAnexos = listaAnexos;
	}
	
	protected FiltroRREO(Builder builder) {
		super(builder);
		this.listaAnexos = builder.listaAnexos;
	}
	
	public static class Builder extends FiltroBase.Builder<Builder> {
		
		private List<String> listaAnexos;
		
		public Builder() {}
		
		public Builder listaAnexos(List<String> listaAnexos) {
			this.listaAnexos = listaAnexos;
			return this;
		}
		
		public FiltroRREO build() {
			return new FiltroRREO(this);
		}
	}

}
