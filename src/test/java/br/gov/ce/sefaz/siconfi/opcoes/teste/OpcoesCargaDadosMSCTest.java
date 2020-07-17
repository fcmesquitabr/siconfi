package br.gov.ce.sefaz.siconfi.opcoes.teste;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.sefaz.siconfi.enums.TipoValorMatrizSaldoContabeis;
import br.gov.ce.sefaz.siconfi.opcoes.OpcoesCargaDadosMSC;

public class OpcoesCargaDadosMSCTest {

	private OpcoesCargaDadosMSC opcoesCargaDadosMSC;
	
	@Test
	public void testeConstrutor() {
		assertNotNull(opcoesCargaDadosMSC);
	}

	@Before
	public void init() {
		opcoesCargaDadosMSC = new OpcoesCargaDadosMSC();
	}
	
	@Test
	public void testeGetListaCodigoTipoValorListaVazia() {
		opcoesCargaDadosMSC.setListaTipoValor(null);
		assertEquals(0, opcoesCargaDadosMSC.getListaCodigoTipoValor().size());
	}

	@Test
	public void testeGetListaCodigoTipoValorListaNaoVazia() {
		opcoesCargaDadosMSC.setListaTipoValor(Arrays.asList(TipoValorMatrizSaldoContabeis.beginning_balance,TipoValorMatrizSaldoContabeis.period_change));
		assertArrayEquals(
				new String[] { TipoValorMatrizSaldoContabeis.beginning_balance.getCodigo(),
						TipoValorMatrizSaldoContabeis.period_change.getCodigo() },
				opcoesCargaDadosMSC.getListaCodigoTipoValor().toArray());
	}

}
