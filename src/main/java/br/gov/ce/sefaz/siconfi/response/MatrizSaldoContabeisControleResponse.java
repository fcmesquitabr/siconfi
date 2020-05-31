package br.gov.ce.sefaz.siconfi.response;

import br.gov.ce.sefaz.siconfi.entity.MatrizSaldoContabeisControle;

public class MatrizSaldoContabeisControleResponse extends MatrizSaldoContabeisResponse<MatrizSaldoContabeisControle> {

	@Override
	public Class<MatrizSaldoContabeisControleResponse> getClassType() {
		return MatrizSaldoContabeisControleResponse.class;
	}

}
