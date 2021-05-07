package br.gov.ce.sefaz.siconfi.opcoes;

public class OpcoesCargaDadosAnexoRelatorio extends OpcoesCargaDados{

	public OpcoesCargaDadosAnexoRelatorio() {
		super();
	}

	public OpcoesCargaDadosAnexoRelatorio(Builder builder) {
		super(builder);
	}

	public static class Builder extends OpcoesCargaDados.Builder<Builder> {
		
		@Override
		public OpcoesCargaDadosAnexoRelatorio build() {
			return new OpcoesCargaDadosAnexoRelatorio(this);
		}
	}

}
