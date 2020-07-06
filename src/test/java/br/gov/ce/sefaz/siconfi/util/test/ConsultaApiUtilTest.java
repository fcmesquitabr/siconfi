package br.gov.ce.sefaz.siconfi.util.test;

import static org.junit.Assert.assertNotNull;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import br.gov.ce.sefaz.siconfi.util.ConsultaApiUtil;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({ "javax.management.*", "javax.script.*" })
public class ConsultaApiUtilTest {

	@Mock
	public Client client;

	@Mock
	public WebTarget webTarget;

	@InjectMocks
	public ConsultaApiUtil<Integer> consultaApiUtil;
	
	@Test
	public void testeConstrutor() {
		ConsultaApiUtil<Integer> consultaAPIUtil = new ConsultaApiUtil<Integer>("http://www.sefaz.ce.gov.br/", "siconfi", "application/json");
		assertNotNull(consultaAPIUtil);
	}	
}
