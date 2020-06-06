package br.gov.ce.sefaz.siconfi.opcoes;

public class OpcoesCargaDadosExtratoEntrega extends OpcoesCargaDados {

	
	public OpcoesCargaDadosExtratoEntrega() {
		super();
	}

	public OpcoesCargaDadosExtratoEntrega(Builder builder) {
		super(builder);
	}

	public static class Builder extends OpcoesCargaDados.Builder<Builder> {
		
		public Builder() {}
				
		public OpcoesCargaDadosExtratoEntrega build() {
			return new OpcoesCargaDadosExtratoEntrega(this);
		}

	}
}
