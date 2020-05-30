package br.gov.ce.sefaz.siconfi.util;

public class FiltroExtratoEntrega extends FiltroBase {

	
	public FiltroExtratoEntrega() {
		super();
	}

	public FiltroExtratoEntrega(Builder builder) {
		super(builder);
	}

	public static class Builder extends FiltroBase.Builder<Builder> {
		
		public Builder() {}
				
		public FiltroExtratoEntrega build() {
			return new FiltroExtratoEntrega(this);
		}

	}
}
