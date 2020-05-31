package br.gov.ce.sefaz.siconfi.response;

import br.gov.ce.sefaz.siconfi.entity.MatrizSaldoContabeisPatrimonial;

public class MatrizSaldoContabeisPatrimonialResponse extends MatrizSaldoContabeisResponse<MatrizSaldoContabeisPatrimonial> {

	@Override
	public Class<MatrizSaldoContabeisPatrimonialResponse> getClassType() {
		return MatrizSaldoContabeisPatrimonialResponse.class;
	}

}
