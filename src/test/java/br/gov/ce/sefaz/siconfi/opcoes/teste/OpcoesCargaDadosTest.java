package br.gov.ce.sefaz.siconfi.opcoes.teste;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.gov.ce.sefaz.siconfi.enums.Esfera;
import br.gov.ce.sefaz.siconfi.enums.OpcaoSalvamentoDados;
import br.gov.ce.sefaz.siconfi.opcoes.OpcoesCargaDados;
import br.gov.ce.sefaz.siconfi.util.APIQueryParamUtil;

public class OpcoesCargaDadosTest {

	private OpcoesCargaDados opcoesCargaDados;
	
	@Test
	public void testeConstrutor() {
		assertNotNull(opcoesCargaDados);
	}

	@Before
	public void init() {
		opcoesCargaDados = new OpcoesCargaDados();
	}
	
	@Test
	public void testeObterListaAPIQueryParamUtilExercicioPeriodo() {
		List<String> listaCodigosIbge = Arrays.asList("22","23");
		opcoesCargaDados.setCodigosIBGE(listaCodigosIbge);
		List<APIQueryParamUtil> lista = opcoesCargaDados.obterListaAPIQueryParamUtil(2019,1);
		assertEquals(listaCodigosIbge.size(), lista.size());
	}

	@Test
	public void testeObterListaAPIQueryParamUtilExercicioComPeriodos() {
		List<String> listaCodigosIbge = Arrays.asList("21","22","23");
		List<Integer> listaPeriodos = Arrays.asList(1,2);
		opcoesCargaDados.setCodigosIBGE(listaCodigosIbge);
		opcoesCargaDados.setPeriodos(listaPeriodos);
		List<APIQueryParamUtil> lista = opcoesCargaDados.obterListaAPIQueryParamUtil();
		assertEquals(listaCodigosIbge.size() * listaPeriodos.size(), lista.size());
	}

	@Test
	public void testeObterListaAPIQueryParamUtilExercicioSemPeriodos() {
		List<String> listaCodigosIbge = Arrays.asList("21","22","23");
		opcoesCargaDados.setCodigosIBGE(listaCodigosIbge);
		opcoesCargaDados.setPeriodos(null);
		List<APIQueryParamUtil> lista = opcoesCargaDados.obterListaAPIQueryParamUtil();
		assertEquals(listaCodigosIbge.size(), lista.size());
	}

	@Test
	public void testeObterListaAPIQueryParamUtil() {
		List<String> listaCodigosIbge = Arrays.asList("21","22","23");
		List<Integer> listaPeriodos = Arrays.asList(1,2,3);
		List<Integer> listaExercicios = Arrays.asList(2019,2020);

		opcoesCargaDados.setCodigosIBGE(listaCodigosIbge);
		opcoesCargaDados.setExercicios(listaExercicios);
		opcoesCargaDados.setPeriodos(listaPeriodos);
		
		List<APIQueryParamUtil> lista = opcoesCargaDados.obterListaAPIQueryParamUtil();
		assertEquals(listaExercicios.size() * listaCodigosIbge.size() * listaPeriodos.size(), lista.size());
	}

	@Test
	public void testeListaExerciciosVazia() {
		opcoesCargaDados.setExercicios(Arrays.asList(2020));
		assertFalse(opcoesCargaDados.isListaExerciciosVazia());
		
		opcoesCargaDados.setExercicios(null);
		assertTrue(opcoesCargaDados.isListaExerciciosVazia());

		opcoesCargaDados.setExercicios(new ArrayList<Integer>());
		assertTrue(opcoesCargaDados.isListaExerciciosVazia());
	}

	@Test
	public void testeListaCodigosUFVazia() {
		opcoesCargaDados.setCodigosUF(Arrays.asList("CE"));
		assertFalse(opcoesCargaDados.isListaCodigosUfVazia());
		
		opcoesCargaDados.setCodigosUF(null);
		assertTrue(opcoesCargaDados.isListaCodigosUfVazia());

		opcoesCargaDados.setCodigosUF(new ArrayList<String>());
		assertTrue(opcoesCargaDados.isListaCodigosUfVazia());
	}

	@Test
	public void testeGetSetEsfera() {
		opcoesCargaDados.setEsfera(Esfera.ESTADOS_E_DISTRITO_FEDERAL);
		assertEquals(Esfera.ESTADOS_E_DISTRITO_FEDERAL, opcoesCargaDados.getEsfera());

		opcoesCargaDados.setEsfera(null);
		assertNull(opcoesCargaDados.getEsfera());
	}

	@Test
	public void testeGetSetCapital() {
		opcoesCargaDados.setCapital(1);
		assertEquals(Integer.valueOf(1), opcoesCargaDados.getCapital());
		
		opcoesCargaDados.setCapital(null);
		assertNull(opcoesCargaDados.getCapital());
	}

	@Test
	public void testeGetSetConsiderarExtratoEntrega() {
		opcoesCargaDados.setConsiderarExtratoEntrega(true);
		assertTrue(opcoesCargaDados.isConsiderarExtratoEntrega());

		opcoesCargaDados.setConsiderarExtratoEntrega(false);
		assertFalse(opcoesCargaDados.isConsiderarExtratoEntrega());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testeBuilder() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2020, 1, 1);
		opcoesCargaDados = new OpcoesCargaDados.Builder()
				.exercicios(Arrays.asList(2019,2020))
				.periodos(Arrays.asList(1,2))
				.codigosIbge(Arrays.asList("22","23"))
				.codigosUF(Arrays.asList("CE","PI"))
				.esfera(Esfera.ESTADOS_E_DISTRITO_FEDERAL)	
				.capital(0)
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.BANCO)
				.nomeArquivo("arquivo.csv")
				.dataMinimaEntrega(calendar.getTime())
				.populacaoMinima(100000l)
				.populacaoMaxima(500000l)
				.considerarExtratoEntrega(true)
				.build();
		
		assertArrayEquals(new Integer[] {2019, 2020 }, opcoesCargaDados.getExercicios().toArray() );
		assertArrayEquals(new Integer[] {1, 2}, opcoesCargaDados.getPeriodos().toArray() );
		assertArrayEquals(new String[] {"22", "23"}, opcoesCargaDados.getCodigosIBGE().toArray() );
		assertArrayEquals(new String[] {"CE", "PI"}, opcoesCargaDados.getCodigosUF().toArray() );
		assertEquals(Esfera.ESTADOS_E_DISTRITO_FEDERAL, opcoesCargaDados.getEsfera());
		assertEquals(Integer.valueOf(0), opcoesCargaDados.getCapital());
		assertEquals(OpcaoSalvamentoDados.BANCO, opcoesCargaDados.getOpcaoSalvamento());
		assertEquals("arquivo.csv", opcoesCargaDados.getNomeArquivo());
		assertEquals(calendar.getTime(), opcoesCargaDados.getDataMinimaEntrega());
		assertEquals(Long.valueOf(100000l), opcoesCargaDados.getPopulacaoMinima());
		assertEquals(Long.valueOf(500000l), opcoesCargaDados.getPopulacaoMaxima());
		assertTrue(opcoesCargaDados.isConsiderarExtratoEntrega());
	}

}
