package br.gov.ce.sefaz.siconfi.util.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.sefaz.siconfi.enums.Esfera;
import br.gov.ce.sefaz.siconfi.enums.OpcaoSalvamentoDados;
import br.gov.ce.sefaz.siconfi.enums.Poder;
import br.gov.ce.sefaz.siconfi.enums.Relatorio;
import br.gov.ce.sefaz.siconfi.enums.TipoMatrizSaldoContabeis;
import br.gov.ce.sefaz.siconfi.enums.TipoValorMatrizSaldoContabeis;
import br.gov.ce.sefaz.siconfi.util.Constantes;
import br.gov.ce.sefaz.siconfi.util.LeitorParametrosPrograma;

public class LeitorParametrosProgramaTest {

	private LeitorParametrosPrograma leitorParametrosPrograma = new LeitorParametrosPrograma();
	
	@Test
	public void testeConstrutor() {
		assertNotNull(leitorParametrosPrograma);
	}

	@Before
	public void definirValoresValidosArgumentos() {
		System.setProperty(LeitorParametrosPrograma.OPCAO_RELATORIO, Relatorio.dca.name());
		System.setProperty(LeitorParametrosPrograma.OPCAO_SALVAMENTO, OpcaoSalvamentoDados.CONSOLE.name());
		System.setProperty(LeitorParametrosPrograma.OPCAO_ESFERA, Esfera.MUNICIPIO.getCodigo());
		System.setProperty(LeitorParametrosPrograma.OPCAO_CODIGOS_IBGE, "23");		
		System.setProperty(LeitorParametrosPrograma.OPCAO_PODER, Poder.EXECUTIVO.getCodigo());
		System.setProperty(LeitorParametrosPrograma.OPCAO_EXERCICIOS, "2020");
		System.setProperty(LeitorParametrosPrograma.OPCAO_PERIODOS, "1");
		System.setProperty(LeitorParametrosPrograma.OPCAO_CAPITAL, "1");
		System.setProperty(LeitorParametrosPrograma.OPCAO_UF, "23");
		System.setProperty(LeitorParametrosPrograma.OPCAO_ANEXOS, "Anexo 1");
		System.setProperty(LeitorParametrosPrograma.OPCAO_TIPO_MATRIZ, TipoMatrizSaldoContabeis.MSCC.getCodigo());
		System.setProperty(LeitorParametrosPrograma.OPCAO_TIPOS_VALOR_MATRIZ, TipoValorMatrizSaldoContabeis.period_change.name());
		System.setProperty(LeitorParametrosPrograma.OPCAO_POPULACAO_MINIMA, "100000");
		System.setProperty(LeitorParametrosPrograma.OPCAO_POPULACAO_MAXIMA, "500000");
	}

	@Test
	public void testeLerOpcaoRelatorioNaoDefinida() {
		System.clearProperty(LeitorParametrosPrograma.OPCAO_RELATORIO);
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> LeitorParametrosPrograma.lerArgumentos());
		assertEquals("Opção de Relatório não escolhida", exception.getMessage());
	}
	
	@Test
	public void testeLerOpcaoRelatorioVazia() {
		System.setProperty(LeitorParametrosPrograma.OPCAO_RELATORIO, "  ");
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> LeitorParametrosPrograma.lerArgumentos());
		assertEquals("Opção de Relatório não escolhida", exception.getMessage());
	}

	@Test
	public void testeLerOpcaoRelatorioInvalida() {
		System.setProperty(LeitorParametrosPrograma.OPCAO_RELATORIO, "relatorio");
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> LeitorParametrosPrograma.lerArgumentos());
		assertEquals("Opção de Relatório inválida", exception.getMessage());
	}

	@Test
	public void testeLerOpcaoRelatorioValida() {
		System.setProperty(LeitorParametrosPrograma.OPCAO_RELATORIO, Relatorio.dca.name());
		LeitorParametrosPrograma.lerArgumentos();
		assertEquals(Relatorio.dca, LeitorParametrosPrograma.getRelatorioSelecionado());
	}

	@Test
	public void testeLerOpcaoSalvamentoNaoDefinida() {
		System.clearProperty(LeitorParametrosPrograma.OPCAO_SALVAMENTO);
		LeitorParametrosPrograma.lerArgumentos();
		assertEquals(OpcaoSalvamentoDados.ARQUIVO, LeitorParametrosPrograma.getOpcaoSalvamentoSelecionada());
	}

	@Test
	public void testeLerOpcaoSalvamentoVazia() {
		System.setProperty(LeitorParametrosPrograma.OPCAO_SALVAMENTO, "  ");
		LeitorParametrosPrograma.lerArgumentos();
		assertEquals(OpcaoSalvamentoDados.ARQUIVO, LeitorParametrosPrograma.getOpcaoSalvamentoSelecionada());
	}

	@Test
	public void testeLerOpcaoSalvamentoInvalida() {
		System.setProperty(LeitorParametrosPrograma.OPCAO_SALVAMENTO, "FILA");
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> LeitorParametrosPrograma.lerArgumentos());
		assertEquals("Opção de Saída de dados inválida", exception.getMessage());
	}

	@Test
	public void testeLerOpcaoSalvamentoValida() {
		System.setProperty(LeitorParametrosPrograma.OPCAO_SALVAMENTO, OpcaoSalvamentoDados.CONSOLE.name());
		LeitorParametrosPrograma.lerArgumentos();
		assertEquals(OpcaoSalvamentoDados.CONSOLE, LeitorParametrosPrograma.getOpcaoSalvamentoSelecionada());
	}

	@Test
	public void testeLerOpcaoEsferaNaoDefinida() {
		System.clearProperty(LeitorParametrosPrograma.OPCAO_ESFERA);
		LeitorParametrosPrograma.lerArgumentos();
		assertEquals(Esfera.ESTADOS_E_DISTRITO_FEDERAL, LeitorParametrosPrograma.getOpcaoEsferaSelecionada());
	}

	@Test
	public void testeLerOpcaoEsferaVazia() {
		System.setProperty(LeitorParametrosPrograma.OPCAO_ESFERA, "  ");
		LeitorParametrosPrograma.lerArgumentos();
		assertEquals(Esfera.ESTADOS_E_DISTRITO_FEDERAL, LeitorParametrosPrograma.getOpcaoEsferaSelecionada());
	}

	@Test
	public void testeLerOpcaoEsferaInvalida() {
		System.setProperty(LeitorParametrosPrograma.OPCAO_ESFERA, "Z");
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> LeitorParametrosPrograma.lerArgumentos());
		assertEquals("Opção de Esfera inválida", exception.getMessage());		
	}

	@Test
	public void testeLerOpcaoEsferaValida() {
		System.setProperty(LeitorParametrosPrograma.OPCAO_ESFERA, Esfera.MUNICIPIO.getCodigo());
		LeitorParametrosPrograma.lerArgumentos();
		assertEquals(Esfera.MUNICIPIO, LeitorParametrosPrograma.getOpcaoEsferaSelecionada());
	}
	
	@Test
	public void testeLerOpcaoPoderNaoDefinida() {
		System.clearProperty(LeitorParametrosPrograma.OPCAO_PODER);
		LeitorParametrosPrograma.lerArgumentos();
		assertTrue(LeitorParametrosPrograma.getOpcaoPoderesSelecionados().containsAll(Arrays.asList(Poder.values())));
	}

	@Test
	public void testeLerOpcaoPoderInvalida() {
		System.setProperty(LeitorParametrosPrograma.OPCAO_PODER, " Y , Z ");
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> LeitorParametrosPrograma.lerArgumentos());
		assertEquals("Opção de Poder inválida", exception.getMessage());		
	}

	@Test
	public void testeLerOpcaoPoderValida() {
		System.setProperty(LeitorParametrosPrograma.OPCAO_PODER, " E , J , L , D , M ");
		LeitorParametrosPrograma.lerArgumentos();
		assertTrue(LeitorParametrosPrograma.getOpcaoPoderesSelecionados().containsAll(Arrays.asList(Poder.values())));
	}

	@Test
	public void testeLerOpcaoExerciciosNaoDefinida() {
		System.clearProperty(LeitorParametrosPrograma.OPCAO_EXERCICIOS);
		LeitorParametrosPrograma.lerArgumentos();
		assertTrue(LeitorParametrosPrograma.getOpcaoExerciciosSelecionados().containsAll(Constantes.EXERCICIOS_DISPONIVEIS));
	}

	@Test
	public void testeLerOpcaoExerciciosInvalida() {
		System.setProperty(LeitorParametrosPrograma.OPCAO_EXERCICIOS, " 2010 , 2010.1 ");
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> LeitorParametrosPrograma.lerArgumentos());
		assertEquals("Opção de Exercício inválida", exception.getMessage());		
	}

	@Test
	public void testeLerOpcaoExercicioValida() {
		System.setProperty(LeitorParametrosPrograma.OPCAO_EXERCICIOS, " 2018 , 2019 , 2020 ");
		LeitorParametrosPrograma.lerArgumentos();
		assertTrue(LeitorParametrosPrograma.getOpcaoExerciciosSelecionados().containsAll(Arrays.asList(2018,2019,2020)));
	}

	@Test
	public void testeLerOpcaoPeriodoNaoDefinidaRREO() {
		System.clearProperty(LeitorParametrosPrograma.OPCAO_PERIODOS);
		System.setProperty(LeitorParametrosPrograma.OPCAO_RELATORIO, Relatorio.rreo.name());
		LeitorParametrosPrograma.lerArgumentos();
		assertArrayEquals(LeitorParametrosPrograma.getOpcaoPeriodosSelecionados().toArray(), Constantes.BIMESTRES.toArray());
	}

	@Test
	public void testeLerOpcaoPeriodoNaoDefinidaMSCPatrimonial() {
		System.clearProperty(LeitorParametrosPrograma.OPCAO_PERIODOS);
		System.setProperty(LeitorParametrosPrograma.OPCAO_RELATORIO, Relatorio.msc_patrimonial.name());
		LeitorParametrosPrograma.lerArgumentos();
		assertArrayEquals(LeitorParametrosPrograma.getOpcaoPeriodosSelecionados().toArray(), Constantes.MESES.toArray());
	}

	@Test
	public void testeLerOpcaoPeriodoNaoDefinidaOutrosRelatorios() {
		System.clearProperty(LeitorParametrosPrograma.OPCAO_PERIODOS);
		System.setProperty(LeitorParametrosPrograma.OPCAO_RELATORIO, Relatorio.dca.name());
		LeitorParametrosPrograma.lerArgumentos();
		assertNull(LeitorParametrosPrograma.getOpcaoPeriodosSelecionados());
	}

	@Test
	public void testeLerOpcaoPeriodosInvalida() {
		System.setProperty(LeitorParametrosPrograma.OPCAO_PERIODOS, " A, B ");
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> LeitorParametrosPrograma.lerArgumentos());
		assertEquals("Opção de Períodos inválida", exception.getMessage());		
	}

	@Test
	public void testeLerOpcaoPeriodosValida() {
		System.setProperty(LeitorParametrosPrograma.OPCAO_PERIODOS, " 1 , 2 , 3 ");
		LeitorParametrosPrograma.lerArgumentos();
		assertArrayEquals(LeitorParametrosPrograma.getOpcaoPeriodosSelecionados().toArray(), new Integer[] {1,2,3} );
	}

	@Test
	public void testeLerOpcaoCodigosValida() {
		System.setProperty(LeitorParametrosPrograma.OPCAO_CODIGOS_IBGE, " 22 , 23  ");
		LeitorParametrosPrograma.lerArgumentos();
		assertArrayEquals(LeitorParametrosPrograma.getOpcaoCodigosIbgeSelecionados().toArray(), new String[] {"22","23"} );
	}

	@Test
	public void testeLerOpcaoCodigosStringsVazias() {
		System.setProperty(LeitorParametrosPrograma.OPCAO_CODIGOS_IBGE, "  ,  ");
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> LeitorParametrosPrograma.lerArgumentos());
		assertEquals("Opção de Códigos IBGE inválida", exception.getMessage());		
	}

	@Test
	public void testeLerOpcaoAnexosValida() {
		System.setProperty(LeitorParametrosPrograma.OPCAO_ANEXOS, " Anexo 1 , Anexo 2 ");
		LeitorParametrosPrograma.lerArgumentos();
		assertArrayEquals(LeitorParametrosPrograma.getOpcaoAnexosSelecionados().toArray(), new String[] {"Anexo 1","Anexo 2"} );	
	}

	@Test
	public void testeLerOpcaoAnexosStringsVazias() {
		System.setProperty(LeitorParametrosPrograma.OPCAO_ANEXOS, "  ,  ");
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> LeitorParametrosPrograma.lerArgumentos());
		assertEquals("Opção de Anexos inválida", exception.getMessage());		
	}

	@Test
	public void testeLerOpcaoTipoMatrizValida() {
		System.setProperty(LeitorParametrosPrograma.OPCAO_TIPO_MATRIZ, TipoMatrizSaldoContabeis.MSCC.getCodigo());
		LeitorParametrosPrograma.lerArgumentos();
		assertEquals(TipoMatrizSaldoContabeis.MSCC,LeitorParametrosPrograma.getOpcaoTipoMatrizSelecionado());	
	}

	@Test
	public void testeLerOpcaoTipoMatrizInvalida() {
		System.setProperty(LeitorParametrosPrograma.OPCAO_TIPO_MATRIZ, " ABCD ");
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> LeitorParametrosPrograma.lerArgumentos());
		assertEquals("Opção de Tipo Matriz inválida", exception.getMessage());		
	}

	@Test
	public void testeLerOpcaoTipoValorMatrizValida() {
		System.setProperty(LeitorParametrosPrograma.OPCAO_TIPOS_VALOR_MATRIZ, "period_change, ending_balance");
		LeitorParametrosPrograma.lerArgumentos();
		assertArrayEquals(
				new TipoValorMatrizSaldoContabeis[] { TipoValorMatrizSaldoContabeis.period_change,
						TipoValorMatrizSaldoContabeis.ending_balance },
				LeitorParametrosPrograma.getOpcaoTiposValorMatrizSelecionado().toArray());
	}

	@Test
	public void testeLerOpcaoTipoValorMatrizInvalida() {
		System.setProperty(LeitorParametrosPrograma.OPCAO_TIPOS_VALOR_MATRIZ, " ABCD ");
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> LeitorParametrosPrograma.lerArgumentos());
		assertEquals("Opção de Tipo de Valor Matriz inválida", exception.getMessage());		
	}

	@Test
	public void testeLerOpcaoPopulacaoMinimaInvalida() {
		System.setProperty(LeitorParametrosPrograma.OPCAO_POPULACAO_MINIMA, "A");
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> LeitorParametrosPrograma.lerArgumentos());
		assertEquals("Opção de População Mínima inválida", exception.getMessage());		

		System.setProperty(LeitorParametrosPrograma.OPCAO_POPULACAO_MINIMA, "12.5");
		exception = assertThrows(IllegalArgumentException.class, () -> LeitorParametrosPrograma.lerArgumentos());
		assertEquals("Opção de População Mínima inválida", exception.getMessage());		

	}

	@Test
	public void testeLerOpcaoPopulacaoMinimaValida() {
		System.setProperty(LeitorParametrosPrograma.OPCAO_POPULACAO_MINIMA, "200000");
		LeitorParametrosPrograma.lerArgumentos();
		assertEquals(200000, LeitorParametrosPrograma.getOpcaoPopulacaoMinimaSelecionada().intValue() );
	}

	@Test
	public void testeLerOpcaoPopulacaoMaximaInvalida() {
		System.setProperty(LeitorParametrosPrograma.OPCAO_POPULACAO_MAXIMA, "A");
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> LeitorParametrosPrograma.lerArgumentos());
		assertEquals("Opção de População Máxima inválida", exception.getMessage());		

		System.setProperty(LeitorParametrosPrograma.OPCAO_POPULACAO_MAXIMA, "12.5");
		exception = assertThrows(IllegalArgumentException.class, () -> LeitorParametrosPrograma.lerArgumentos());
		assertEquals("Opção de População Máxima inválida", exception.getMessage());		
	}

	@Test
	public void testeLerOpcaoPopulacaoMaximaValida() {
		System.setProperty(LeitorParametrosPrograma.OPCAO_POPULACAO_MAXIMA, "500000");
		LeitorParametrosPrograma.lerArgumentos();
		assertEquals(500000, LeitorParametrosPrograma.getOpcaoPopulacaoMaximaSelecionada().intValue() );
	}

	@Test
	public void testeLerOpcaoCapitalInvalida() {
		System.setProperty(LeitorParametrosPrograma.OPCAO_CAPITAL, "A");
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> LeitorParametrosPrograma.lerArgumentos());
		assertEquals("Opção de Capital inválida", exception.getMessage());		

		System.setProperty(LeitorParametrosPrograma.OPCAO_CAPITAL, "12.5");
		exception = assertThrows(IllegalArgumentException.class, () -> LeitorParametrosPrograma.lerArgumentos());
		assertEquals("Opção de Capital inválida", exception.getMessage());		
	}

	@Test
	public void testeLerOpcaoCapitalValida() {
		System.setProperty(LeitorParametrosPrograma.OPCAO_CAPITAL, "1");
		LeitorParametrosPrograma.lerArgumentos();
		assertEquals(1, LeitorParametrosPrograma.getOpcaoCapitalSelecionada().intValue() );
	}

	@Test
	public void testeLerOpcaoUFsValida() {
		System.setProperty(LeitorParametrosPrograma.OPCAO_UF, " CE , PI ");
		LeitorParametrosPrograma.lerArgumentos();
		assertArrayEquals(LeitorParametrosPrograma.getOpcaoCodigosUFSelecionados().toArray(), new String[] {"CE","PI"} );
	}

	@Test
	public void testeLerOpcaoUFsStringsVazias() {
		System.setProperty(LeitorParametrosPrograma.OPCAO_UF, "  ,  ");
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> LeitorParametrosPrograma.lerArgumentos());
		assertEquals("Opção de UF inválida", exception.getMessage());		
	}

}
