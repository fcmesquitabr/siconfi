package br.gov.ce.sefaz.siconfi.main.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.gov.ce.sefaz.siconfi.enums.OpcaoSalvamentoDados;
import br.gov.ce.sefaz.siconfi.enums.Relatorio;
import br.gov.ce.sefaz.siconfi.main.ClientRestServiceMain;
import br.gov.ce.sefaz.siconfi.util.LeitorParametrosPrograma;

public class ClientRestServiceMainTest {
	
	@Test
	public void testeConstrutor() {
		ClientRestServiceMain client = new ClientRestServiceMain();
		assertNotNull(client);
	}

	@Test
	public void testeRelatorioNaoSelecionado() {
		System.clearProperty(LeitorParametrosPrograma.OPCAO_RELATORIO);			
		ClientRestServiceMain.main(null);
		assertTrue(true);
	}

	@Test
	public void testeRelatorioSelecionado() {
		System.setProperty(LeitorParametrosPrograma.OPCAO_RELATORIO, Relatorio.ente.name());			
		System.setProperty(LeitorParametrosPrograma.OPCAO_SALVAMENTO, OpcaoSalvamentoDados.CONSOLE.name());			
		ClientRestServiceMain.main(null);
		assertTrue(true);
	}
	
}
