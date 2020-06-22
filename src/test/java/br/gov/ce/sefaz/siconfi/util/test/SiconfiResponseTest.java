package br.gov.ce.sefaz.siconfi.util.test;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import br.gov.ce.sefaz.siconfi.util.SiconfiResponse;

public class SiconfiResponseTest {

	@Test
	public void testeConstrutor() {
		SiconfiResponse<Integer> siconfiResponse = new SiconfiResponse<Integer>();
		assertNotNull(siconfiResponse);
	}
}
