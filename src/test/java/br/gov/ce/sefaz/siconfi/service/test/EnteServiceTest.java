package br.gov.ce.sefaz.siconfi.service.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import br.gov.ce.sefaz.siconfi.entity.Ente;
import br.gov.ce.sefaz.siconfi.enums.Esfera;
import br.gov.ce.sefaz.siconfi.enums.OpcaoSalvamentoDados;
import br.gov.ce.sefaz.siconfi.helper.DbUnitHelper;
import br.gov.ce.sefaz.siconfi.opcoes.OpcoesCargaDadosEnte;
import br.gov.ce.sefaz.siconfi.service.EnteService;
import br.gov.ce.sefaz.siconfi.util.ConsultaApiUtil;
import br.gov.ce.sefaz.siconfi.util.CsvUtil;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore( {"javax.management.*", "javax.script.*" })
public class EnteServiceTest {

	private static final String[] COLUNAS_ARQUIVO_CSV = new String[] { "cod_ibge", "ente", "capital", "regiao", "uf",
			"esfera", "exercicio", "populacao", "cnpj" };	
	private static final String NOME_PADRAO_ARQUIVO_CSV = "entes.csv";

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;
	
	@Mock
	public ConsultaApiUtil<Ente> consultaApiUtil;

	@Mock
	public CsvUtil<Ente> csvUtil;

	@InjectMocks
	public EnteService enteService;
	
	private static DbUnitHelper dbUnitHelper;
		
	@Before
	public void init() {
		System.setOut(new PrintStream(outContent));
	}

	@After
	public void finish() {
		System.setOut(originalOut);
	}

	@Test
	public void testeConstrutor() {
		EnteService enteService = new EnteService();
		assertNotNull(enteService);
	}

	@Test
	public void testeCarregarDadosArquivoSemNome() {
		OpcoesCargaDadosEnte opcoes = new OpcoesCargaDadosEnte();
		opcoes.setOpcaoSalvamento(OpcaoSalvamentoDados.ARQUIVO);
		Ente ente = obterEnte();
		when(consultaApiUtil.lerEntidades(any(), eq(Ente.class))).thenReturn(Arrays.asList(ente));
		enteService.carregarDados(opcoes);
		verify(consultaApiUtil).lerEntidades(any(), eq(Ente.class));
		
		try {
			verify(csvUtil).writeHeader(COLUNAS_ARQUIVO_CSV, NOME_PADRAO_ARQUIVO_CSV);
			verify(csvUtil).writeToFile(Arrays.asList(ente), COLUNAS_ARQUIVO_CSV, NOME_PADRAO_ARQUIVO_CSV);
		} catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testeCarregarDadosArquivoComNome() {
		OpcoesCargaDadosEnte opcoes = new OpcoesCargaDadosEnte.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.ARQUIVO)
				.nomeArquivo("nome_do_arquivo.csv")
				.build();
		Ente ente = obterEnte();
		when(consultaApiUtil.lerEntidades(any(), eq(Ente.class))).thenReturn(Arrays.asList(ente));
		enteService.carregarDados(opcoes);
		verify(consultaApiUtil).lerEntidades(any(), eq(Ente.class));
		
		try {
			verify(csvUtil).writeHeader(COLUNAS_ARQUIVO_CSV, "nome_do_arquivo.csv");
			verify(csvUtil).writeToFile(Arrays.asList(ente), COLUNAS_ARQUIVO_CSV, "nome_do_arquivo.csv");
		} catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testeCarregarDadosNaBase() {
		OpcoesCargaDadosEnte opcoes = new OpcoesCargaDadosEnte.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.BANCO)
				.build();
		Ente ente = obterEnte();
		when(consultaApiUtil.lerEntidades(any(), eq(Ente.class))).thenReturn(Arrays.asList(ente));
		enteService.carregarDados(opcoes);
		verify(consultaApiUtil).lerEntidades(any(), eq(Ente.class));
		List<Ente> listaEntes = enteService.consultarEntesNaBase(opcoes);
		assertArrayEquals(new Ente[] {ente}, listaEntes.toArray());
	}

	@Test
	public void testeObterListaEntesNaApiComOpcoesCompletas() {
		OpcoesCargaDadosEnte opcoes = new OpcoesCargaDadosEnte.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.BANCO)
				.capital(0)
				.codigosIbge(Arrays.asList("23"))
				.codigosUF(Arrays.asList("CE"))
				.esfera(Esfera.ESTADO)
				.populacaoMinima(100000l)
				.populacaoMaxima(100000000l)
				.build();
		Ente ente = obterEnte();
		when(consultaApiUtil.lerEntidades(any(), eq(Ente.class))).thenReturn(Arrays.asList(ente));
		List<Ente> listaEntes = enteService.obterListaEntesNaAPI(opcoes);
		assertArrayEquals(new Ente[] {ente}, listaEntes.toArray());
		verify(consultaApiUtil).lerEntidades(any(), eq(Ente.class));		
	}

	@Test
	public void testeObterListaEntesNaApiFiltroPopulacaoMinima() {
		OpcoesCargaDadosEnte opcoes = new OpcoesCargaDadosEnte.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.BANCO)
				.populacaoMinima(100000l)
				.build();
		Ente ente = obterEnte();
		when(consultaApiUtil.lerEntidades(any(), eq(Ente.class))).thenReturn(Arrays.asList(ente));
		List<Ente> listaEntes = enteService.obterListaEntesNaAPI(opcoes);
		assertArrayEquals(new Ente[] {ente}, listaEntes.toArray());
		verify(consultaApiUtil).lerEntidades(any(), eq(Ente.class));	
		
		opcoes.setPopulacaoMinima(2000001l);
		listaEntes = enteService.obterListaEntesNaAPI(opcoes);
		assertEquals(0, listaEntes.size());
	}

	@Test
	public void testeObterListaEntesNaApiFiltroPopulacaoMaxima() {
		OpcoesCargaDadosEnte opcoes = new OpcoesCargaDadosEnte.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.BANCO)
				.populacaoMaxima(200000l)
				.build();
		Ente ente = obterEnte();
		when(consultaApiUtil.lerEntidades(any(), eq(Ente.class))).thenReturn(Arrays.asList(ente));
		List<Ente> listaEntes = enteService.obterListaEntesNaAPI(opcoes);
		assertArrayEquals(new Ente[] {ente}, listaEntes.toArray());
		verify(consultaApiUtil).lerEntidades(any(), eq(Ente.class));	
		
		opcoes.setPopulacaoMaxima(199999l);
		listaEntes = enteService.obterListaEntesNaAPI(opcoes);
		assertEquals(0, listaEntes.size());
	}

	@Test
	public void testeObterListaCodigosIbgeNaAPISemCodigoIbge() {
		OpcoesCargaDadosEnte opcoes = new OpcoesCargaDadosEnte.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.BANCO)
				.esfera(Esfera.ESTADOS_E_DISTRITO_FEDERAL)
				.build();
		Ente ente = obterEnte();
		when(consultaApiUtil.lerEntidades(any(), eq(Ente.class))).thenReturn(Arrays.asList(ente));
		List<String> listaCodigoIbge = enteService.obterListaCodigosIbgeNaAPI(opcoes);
		assertArrayEquals(new String[] {ente.getCod_ibge()}, listaCodigoIbge.toArray());
		verify(consultaApiUtil).lerEntidades(any(), eq(Ente.class));		
	}

	@Test
	public void testeObterListaCodigosIbgeNaAPIComCodigoIbge() {
		OpcoesCargaDadosEnte opcoes = new OpcoesCargaDadosEnte.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.BANCO)
				.codigosIbge(Arrays.asList("23"))
				.build();

		List<String> listaCodigoIbge = enteService.obterListaCodigosIbgeNaAPI(opcoes);
		assertArrayEquals(new String[] {"23"}, listaCodigoIbge.toArray());
		verify(consultaApiUtil, never()).lerEntidades(any(), eq(Ente.class));
	}
	
	@Test
	public void testeObterListaEntesNaBaseSemCodigoIbge() {
		OpcoesCargaDadosEnte opcoes = new OpcoesCargaDadosEnte.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.BANCO)
				.capital(0)
				.codigosUF(Arrays.asList("BR"))
				.esfera(Esfera.ESTADOS_E_DISTRITO_FEDERAL)
				.populacaoMinima(100000l)
				.populacaoMaxima(100000000l)
				.build();
		iniciarDbUnit();
		List<String> listaEntes = enteService.obterListaCodigosIbgeNaBase(opcoes);
		assertEquals(27, listaEntes.size());
	}

	@Test
	public void testeObterListaCodigoIbgeComCodigoIbge() {
		OpcoesCargaDadosEnte opcoes = new OpcoesCargaDadosEnte.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.BANCO)
				.codigosIbge(Arrays.asList("23"))
				.build();
		List<String> listaEntes = enteService.obterListaCodigosIbgeNaBase(opcoes);
		assertArrayEquals(new String[] {"23"}, listaEntes.toArray());
	}

	@Test
	public void testeObterListaEnteComCodigoIbge() {
		OpcoesCargaDadosEnte opcoes = new OpcoesCargaDadosEnte.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.BANCO)
				.codigosIbge(Arrays.asList("21","22","23","24"))
				.build();
		iniciarDbUnit();
		List<Ente> listaEntes = enteService.consultarEntesNaBase(opcoes);
		assertEquals(4, listaEntes.size());
	}

	@Test
	public void testeObterListaEntesNaBaseCapital() {
		OpcoesCargaDadosEnte opcoes = new OpcoesCargaDadosEnte.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.BANCO)
				.capital(1)
				.codigosUF(Arrays.asList("CE","PI"))
				.esfera(Esfera.MUNICIPIO)
				.build();
		iniciarDbUnit();
		List<String> listaEntes = enteService.obterListaCodigosIbgeNaBase(opcoes);
		assertEquals(2, listaEntes.size());
	}

	public void iniciarDbUnit() {
		dbUnitHelper = new DbUnitHelper(enteService.getEntityManager(), "DbUnitXml");
		dbUnitHelper.execute(DatabaseOperation.DELETE_ALL, "Ente.xml");		 
	    dbUnitHelper.execute(DatabaseOperation.INSERT, "Ente.xml");
	}

	private Ente obterEnte() {
		Ente ente = new Ente();
		ente.setCapital(0);
		ente.setId(1l);
		ente.setEnte("Ceará");
		ente.setUf("CE");
		ente.setCod_ibge("23");
		ente.setExercicio(2020);
		ente.setRegiao("NE");
		ente.setEsfera("E");
		ente.setPopulacao(200000l);
		return ente;
	}
}
