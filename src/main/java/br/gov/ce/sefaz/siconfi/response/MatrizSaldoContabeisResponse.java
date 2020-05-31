package br.gov.ce.sefaz.siconfi.response;

import br.gov.ce.sefaz.siconfi.entity.MatrizSaldoContabeis;

public abstract class MatrizSaldoContabeisResponse<T extends MatrizSaldoContabeis> extends SiconfiResponse<T> {

	public abstract Class<? extends MatrizSaldoContabeisResponse<T>> getClassType();	

}
