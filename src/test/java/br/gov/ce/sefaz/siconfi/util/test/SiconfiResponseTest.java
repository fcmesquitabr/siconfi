package br.gov.ce.sefaz.siconfi.util.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.gov.ce.sefaz.siconfi.util.SiconfiResponse;

public class SiconfiResponseTest {

	private SiconfiResponse<Integer> siconfiResponse = new SiconfiResponse<Integer>();
	
	@Test
	public void testeConstrutor() {
		SiconfiResponse<Integer> siconfiResponse = new SiconfiResponse<Integer>();
		assertNotNull(siconfiResponse);
	}

	@Test 
	public void testeGetSetHasMore() {
		siconfiResponse.setHasMore(true);
		assertTrue(siconfiResponse.getHasMore());
		
		siconfiResponse.setHasMore(false);
		assertFalse(siconfiResponse.getHasMore());		
	}

	@Test 
	public void testeGetSetCount() {
		siconfiResponse.setCount(155);
		assertEquals(Integer.valueOf(155), siconfiResponse.getCount());
		
		siconfiResponse.setCount(null);
		assertNull(siconfiResponse.getCount());
	}

	@Test 
	public void testeGetSetLimit() {
		siconfiResponse.setLimit(200);
		assertEquals(Integer.valueOf(200), siconfiResponse.getLimit());
		
		siconfiResponse.setLimit(null);
		assertNull(siconfiResponse.getLimit());
	}

	@Test 
	public void testeGetSetLinks() {
		siconfiResponse.setLinks("http://www.sefaz.ce.gov.br");
		assertEquals("http://www.sefaz.ce.gov.br", siconfiResponse.getLinks());
		
		siconfiResponse.setLinks(null);
		assertNull(siconfiResponse.getLinks());
	}

	@Test 
	public void testeGetSetOffset() {
		siconfiResponse.setOffset(100);
		assertEquals(Integer.valueOf(100), siconfiResponse.getOffset());
		
		siconfiResponse.setOffset(null);
		assertNull(siconfiResponse.getOffset());
	}

}
