package br.gov.ce.sefaz.siconfi.util.test;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import br.gov.ce.sefaz.siconfi.util.Constantes;

public class ConstantesTest {

	@Test
	public void testeConstrutor() {
		Constantes constantes = new Constantes();
		assertNotNull(constantes);
	}

}
