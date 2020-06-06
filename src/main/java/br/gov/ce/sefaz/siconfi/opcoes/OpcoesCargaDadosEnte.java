package br.gov.ce.sefaz.siconfi.opcoes;

public class OpcoesCargaDadosEnte extends OpcoesCargaDados {

	
	public OpcoesCargaDadosEnte() {
		super();
}

	public OpcoesCargaDadosEnte(br.gov.ce.sefaz.siconfi.opcoes.OpcoesCargaDados.Builder<?> builder) {
		super(builder);
	}

	public static class Builder extends OpcoesCargaDados.Builder<Builder> {

		public OpcoesCargaDadosEnte build() {
			return new OpcoesCargaDadosEnte(this);
		}
	}
}
