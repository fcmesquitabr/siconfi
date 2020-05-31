package br.gov.ce.sefaz.siconfi.response;

import br.gov.ce.sefaz.siconfi.entity.MatrizSaldoContabeisOrcamentaria;

public class MatrizSaldoContabeisOrcamentariaResponse extends MatrizSaldoContabeisResponse<MatrizSaldoContabeisOrcamentaria>{

	@Override
	public Class<MatrizSaldoContabeisOrcamentariaResponse> getClassType() {
		return MatrizSaldoContabeisOrcamentariaResponse.class;
	}

}
