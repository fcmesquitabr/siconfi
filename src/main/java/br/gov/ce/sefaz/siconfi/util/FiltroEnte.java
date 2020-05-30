package br.gov.ce.sefaz.siconfi.util;

public class FiltroEnte extends FiltroBase{

	
	public FiltroEnte() {
		super();
}

	public FiltroEnte(br.gov.ce.sefaz.siconfi.util.FiltroBase.Builder<?> builder) {
		super(builder);
	}

	public static class Builder extends FiltroBase.Builder<Builder> {

		public FiltroEnte build() {
			return new FiltroEnte(this);
		}
	}
}
