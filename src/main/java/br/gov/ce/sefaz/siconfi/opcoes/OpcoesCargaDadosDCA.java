package br.gov.ce.sefaz.siconfi.opcoes;

import java.util.List;

import br.gov.ce.sefaz.siconfi.util.Utils;

public class OpcoesCargaDadosDCA extends OpcoesCargaDados {

	private List<String> listaAnexos;

	public OpcoesCargaDadosDCA() {
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
	
	protected OpcoesCargaDadosDCA(Builder builder) {
		super(builder);
		this.listaAnexos = builder.listaAnexos;
	}
	
	public static class Builder extends OpcoesCargaDados.Builder<Builder> {
		
		private List<String> listaAnexos;
		
		public Builder() {}
		
		public Builder listaAnexos(List<String> listaAnexos) {
			this.listaAnexos = listaAnexos;
			return this;
		}
		
		public OpcoesCargaDadosDCA build() {
			return new OpcoesCargaDadosDCA(this);
		}
	}
}
