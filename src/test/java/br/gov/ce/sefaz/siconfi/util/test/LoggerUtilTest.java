package br.gov.ce.sefaz.siconfi.util.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.logging.log4j.Logger;
import org.junit.Test;

import br.gov.ce.sefaz.siconfi.util.LoggerUtil;

public class LoggerUtilTest {

	@Test
	public void testeConstrutor() {
		LoggerUtil loggerUtil = new LoggerUtil();
		assertNotNull(loggerUtil);
	}

	@Test
	public void testeCreateLogger() {
		Logger logger = LoggerUtil.createLogger(DummyClass.class);
		assertEquals("br.gov.ce.sefaz.siconfi.util.test.LoggerUtilTest.DummyClass",logger.getName());
	}
	
	private class DummyClass{
		
	}
}
