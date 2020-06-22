package br.gov.ce.sefaz.siconfi.util.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import br.gov.ce.sefaz.siconfi.enums.Esfera;
import br.gov.ce.sefaz.siconfi.enums.OpcaoSalvamentoDados;
import br.gov.ce.sefaz.siconfi.enums.Poder;
import br.gov.ce.sefaz.siconfi.enums.Relatorio;
import br.gov.ce.sefaz.siconfi.util.LeitorParametrosPrograma;

public class LeitorParametrosProgramaTest {

	private LeitorParametrosPrograma leitorParametrosPrograma = new LeitorParametrosPrograma();
	
	@Test
	public void testeConstrutor() {
		assertNotNull(leitorParametrosPrograma);
	}

	private void definirValoresValidosArgumentos() {
		System.setProperty(LeitorParametrosPrograma.OPCAO_RELATORIO, Relatorio.dca.name());
		System.setProperty(LeitorParametrosPrograma.OPCAO_SALVAMENTO, OpcaoSalvamentoDados.CONSOLE.name());
		System.setProperty(LeitorParametrosPrograma.OPCAO_ESFERA, Esfera.MUNICIPIO.getCodigo());
		System.setProperty(LeitorParametrosPrograma.OPCAO_PODER, Poder.EXECUTIVO.getCodigo());
	}

	@Test
	public void testeLerOpcaoRelatorioNaoDefinida() {
		definirValoresValidosArgumentos();
		System.clearProperty(LeitorParametrosPrograma.OPCAO_RELATORIO);
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> LeitorParametrosPrograma.lerArgumentos());
		assertEquals("Opção de Relatório não escolhida", exception.getMessage());
	}
	
	@Test
	public void testeLerOpcaoRelatorioVazia() {
		definirValoresValidosArgumentos();
		System.setProperty(LeitorParametrosPrograma.OPCAO_RELATORIO, "  ");
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> LeitorParametrosPrograma.lerArgumentos());
		assertEquals("Opção de Relatório não escolhida", exception.getMessage());
	}

	@Test
	public void testeLerOpcaoRelatorioInvalida() {
		definirValoresValidosArgumentos();
		System.setProperty(LeitorParametrosPrograma.OPCAO_RELATORIO, "relatorio");
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> LeitorParametrosPrograma.lerArgumentos());
		assertEquals("Opção de Relatório inválida", exception.getMessage());
	}

	@Test
	public void testeLerOpcaoRelatorioValida() {
		definirValoresValidosArgumentos();
		System.setProperty(LeitorParametrosPrograma.OPCAO_RELATORIO, Relatorio.dca.name());
		LeitorParametrosPrograma.lerArgumentos();
		assertEquals(Relatorio.dca, LeitorParametrosPrograma.getRelatorioSelecionado());
	}

	@Test
	public void testeLerOpcaoSalvamentoNaoDefinida() {
		definirValoresValidosArgumentos();
		System.clearProperty(LeitorParametrosPrograma.OPCAO_SALVAMENTO);
		LeitorParametrosPrograma.lerArgumentos();
		assertEquals(OpcaoSalvamentoDados.ARQUIVO, LeitorParametrosPrograma.getOpcaoSalvamentoSelecionada());
	}

	@Test
	public void testeLerOpcaoSalvamentoVazia() {
		definirValoresValidosArgumentos();
		System.setProperty(LeitorParametrosPrograma.OPCAO_SALVAMENTO, "  ");
		LeitorParametrosPrograma.lerArgumentos();
		assertEquals(OpcaoSalvamentoDados.ARQUIVO, LeitorParametrosPrograma.getOpcaoSalvamentoSelecionada());
	}

	@Test
	public void testeLerOpcaoSalvamentoInvalida() {
		definirValoresValidosArgumentos();
		System.setProperty(LeitorParametrosPrograma.OPCAO_SALVAMENTO, "FILA");
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> LeitorParametrosPrograma.lerArgumentos());
		assertEquals("Opção de Saída de dados inválida", exception.getMessage());
	}

	@Test
	public void testeLerOpcaoSalvamentoValida() {
		definirValoresValidosArgumentos();
		System.setProperty(LeitorParametrosPrograma.OPCAO_SALVAMENTO, OpcaoSalvamentoDados.CONSOLE.name());
		LeitorParametrosPrograma.lerArgumentos();
		assertEquals(OpcaoSalvamentoDados.CONSOLE, LeitorParametrosPrograma.getOpcaoSalvamentoSelecionada());
	}

	@Test
	public void testeLerOpcaoEsferaNaoDefinida() {
		definirValoresValidosArgumentos();
		System.clearProperty(LeitorParametrosPrograma.OPCAO_ESFERA);
		LeitorParametrosPrograma.lerArgumentos();
		assertEquals(Esfera.ESTADOS_E_DISTRITO_FEDERAL, LeitorParametrosPrograma.getOpcaoEsferaSelecionada());
	}

	@Test
	public void testeLerOpcaoEsferaVazia() {
		definirValoresValidosArgumentos();
		System.setProperty(LeitorParametrosPrograma.OPCAO_ESFERA, "  ");
		LeitorParametrosPrograma.lerArgumentos();
		assertEquals(Esfera.ESTADOS_E_DISTRITO_FEDERAL, LeitorParametrosPrograma.getOpcaoEsferaSelecionada());
	}

	@Test
	public void testeLerOpcaoEsferaInvalida() {
		definirValoresValidosArgumentos();
		System.setProperty(LeitorParametrosPrograma.OPCAO_ESFERA, "Z");
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> LeitorParametrosPrograma.lerArgumentos());
		assertEquals("Opção de Esfera inválida", exception.getMessage());		
	}

	@Test
	public void testeLerOpcaoEsferaValida() {
		definirValoresValidosArgumentos();
		System.setProperty(LeitorParametrosPrograma.OPCAO_ESFERA, Esfera.MUNICIPIO.getCodigo());
		LeitorParametrosPrograma.lerArgumentos();
		assertEquals(Esfera.MUNICIPIO, LeitorParametrosPrograma.getOpcaoEsferaSelecionada());
	}
	
	@Test
	public void testeLerOpcaoPoderNaoDefinida() {
		definirValoresValidosArgumentos();
		System.clearProperty(LeitorParametrosPrograma.OPCAO_PODER);
		LeitorParametrosPrograma.lerArgumentos();
		assertTrue(LeitorParametrosPrograma.getOpcaoPoderesSelecionados().containsAll(Arrays.asList(Poder.values())));
	}

	@Test
	public void testeLerOpcaoPoderInvalida() {
		definirValoresValidosArgumentos();
		System.setProperty(LeitorParametrosPrograma.OPCAO_PODER, " Y , Z ");
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> LeitorParametrosPrograma.lerArgumentos());
		assertEquals("Opção de Poder inválida", exception.getMessage());		
	}

	@Test
	public void testeLerOpcaoPoderValida() {
		definirValoresValidosArgumentos();
		System.setProperty(LeitorParametrosPrograma.OPCAO_PODER, " E , J , L , D , M ");
		LeitorParametrosPrograma.lerArgumentos();
		assertTrue(LeitorParametrosPrograma.getOpcaoPoderesSelecionados().containsAll(Arrays.asList(Poder.values())));
	}

}
