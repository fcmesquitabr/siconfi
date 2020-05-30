package br.gov.ce.sefaz.siconfi.util;

public class FiltroAnexoRelatorio extends FiltroBase{

	public FiltroAnexoRelatorio() {
		super();
	}

	public FiltroAnexoRelatorio(Builder builder) {
		super(builder);
	}

	public static class Builder extends FiltroBase.Builder<Builder> {

		public FiltroAnexoRelatorio build() {
			return new FiltroAnexoRelatorio(this);
		}
	}

}
