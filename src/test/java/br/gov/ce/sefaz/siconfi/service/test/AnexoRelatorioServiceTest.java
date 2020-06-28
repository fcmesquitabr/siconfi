package br.gov.ce.sefaz.siconfi.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import br.gov.ce.sefaz.siconfi.entity.AnexoRelatorio;
import br.gov.ce.sefaz.siconfi.enums.OpcaoSalvamentoDados;
import br.gov.ce.sefaz.siconfi.helper.DbUnitHelper;
import br.gov.ce.sefaz.siconfi.opcoes.OpcoesCargaDadosAnexoRelatorio;
import br.gov.ce.sefaz.siconfi.service.AnexoRelatorioService;
import br.gov.ce.sefaz.siconfi.util.APIQueryParamUtil;
import br.gov.ce.sefaz.siconfi.util.ConsultaApiUtil;
import br.gov.ce.sefaz.siconfi.util.CsvUtil;
import br.gov.ce.sefaz.siconfi.util.LoggerUtil;

//@RunWith(MockitoJUnitRunner.class)
@RunWith(PowerMockRunner.class)
@PowerMockIgnore( {"javax.management.*", "javax.script.*" })
public class AnexoRelatorioServiceTest {

	private static String[] COLUNAS_ARQUIVO_CSV = new String[] {"esfera","demonstrativo","anexo"};	
	private static String NOME_PADRAO_ARQUIVO_CSV = "anexos-relatorios.csv";

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;
	
	@Mock
	public ConsultaApiUtil<AnexoRelatorio> consultaApiUtil;

	@Mock
	public CsvUtil<AnexoRelatorio> csvUtil;

	@InjectMocks
	public AnexoRelatorioService anexoRelatorioService;
	
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
		AnexoRelatorioService anexoRelatorioService = new AnexoRelatorioService();
		assertNotNull(anexoRelatorioService);
	}

	@Test
	public void testeCarregarDadosConsole() {
		OpcoesCargaDadosAnexoRelatorio opcoes = new OpcoesCargaDadosAnexoRelatorio.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.CONSOLE)
				.build();
		
		AnexoRelatorio anexoRelatorio = obterAnexoRelatorio();
		when(consultaApiUtil.lerEntidades(any(), eq(AnexoRelatorio.class))).thenReturn(Arrays.asList(anexoRelatorio));

		anexoRelatorioService.carregarDados(opcoes);
		verify(consultaApiUtil).lerEntidades(any(), eq(AnexoRelatorio.class));
		assertEquals(anexoRelatorio.toString().trim(), outContent.toString().trim());
	}

	@Test
	public void testeCarregarDadosArquivoSemNome() {
		OpcoesCargaDadosAnexoRelatorio opcoes = new OpcoesCargaDadosAnexoRelatorio.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.ARQUIVO)
				.build();
		AnexoRelatorio anexoRelatorio = obterAnexoRelatorio();
		when(consultaApiUtil.lerEntidades(any(), eq(AnexoRelatorio.class))).thenReturn(Arrays.asList(anexoRelatorio));
		anexoRelatorioService.carregarDados(opcoes);
		verify(consultaApiUtil).lerEntidades(any(), eq(AnexoRelatorio.class));
		
		try {
			verify(csvUtil).writeHeader(COLUNAS_ARQUIVO_CSV, NOME_PADRAO_ARQUIVO_CSV);
			verify(csvUtil).writeToFile(Arrays.asList(anexoRelatorio), COLUNAS_ARQUIVO_CSV, NOME_PADRAO_ARQUIVO_CSV);
		} catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testeCarregarDadosArquivoComNome() {
		OpcoesCargaDadosAnexoRelatorio opcoes = new OpcoesCargaDadosAnexoRelatorio.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.ARQUIVO)
				.nomeArquivo("nome_do_arquivo.csv")
				.build();
		AnexoRelatorio anexoRelatorio = obterAnexoRelatorio();
		when(consultaApiUtil.lerEntidades(any(), eq(AnexoRelatorio.class))).thenReturn(Arrays.asList(anexoRelatorio));
		anexoRelatorioService.carregarDados(opcoes);
		verify(consultaApiUtil).lerEntidades(any(), eq(AnexoRelatorio.class));
		
		try {
			verify(csvUtil).writeHeader(COLUNAS_ARQUIVO_CSV, "nome_do_arquivo.csv");
			verify(csvUtil).writeToFile(Arrays.asList(anexoRelatorio), COLUNAS_ARQUIVO_CSV, "nome_do_arquivo.csv");
		} catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@PrepareForTest({LoggerUtil.class})
	public void testeCarregarDadosArquivoListaVazia() {
		OpcoesCargaDadosAnexoRelatorio opcoes = new OpcoesCargaDadosAnexoRelatorio.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.ARQUIVO)
				.exercicios(Arrays.asList(2020))
				.build();
		
		when(consultaApiUtil.lerEntidades(any(), eq(AnexoRelatorio.class))).thenReturn(new ArrayList<AnexoRelatorio>());		
		Logger logger = mockLogger();
		try {
			anexoRelatorioService.carregarDados(opcoes);
			verify(consultaApiUtil).lerEntidades(any(), eq(AnexoRelatorio.class));
			verify(csvUtil).writeHeader(COLUNAS_ARQUIVO_CSV, NOME_PADRAO_ARQUIVO_CSV);
			verify(logger).info("Lista de registro fazia. Nada a salvar no arquivo CSV...");
		} catch (IOException e) {
			e.printStackTrace();
		}
		verifyNoMoreInteractions(csvUtil);
	}

	@Test
	@PrepareForTest({LoggerUtil.class})
	public void testeCarregarDadosIOException() {
		OpcoesCargaDadosAnexoRelatorio opcoes = new OpcoesCargaDadosAnexoRelatorio.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.ARQUIVO)
				.build();
		Logger logger = mockLogger();
		
		try {
			when(consultaApiUtil.lerEntidades(any(), eq(AnexoRelatorio.class))).thenReturn(new ArrayList<AnexoRelatorio>());		
			when(csvUtil.writeHeader(COLUNAS_ARQUIVO_CSV, NOME_PADRAO_ARQUIVO_CSV)).thenThrow(IOException.class);
			anexoRelatorioService.carregarDados(opcoes);
			verify(logger).error("Erro ao escrever o cabeçalho do arquivo");

		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	@Test
	@PrepareForTest({LoggerUtil.class})
	public void testeCarregarDadosCsvDataTypeMismatchException() {
		OpcoesCargaDadosAnexoRelatorio opcoes = new OpcoesCargaDadosAnexoRelatorio.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.ARQUIVO)
				.build();
		Logger logger = mockLogger();
		
		try {
			List<AnexoRelatorio> listaAnexoRelatorio = Arrays.asList(obterAnexoRelatorio());
			when(consultaApiUtil.lerEntidades(any(), eq(AnexoRelatorio.class))).thenReturn(listaAnexoRelatorio);		
			when(csvUtil.writeToFile(listaAnexoRelatorio, COLUNAS_ARQUIVO_CSV, NOME_PADRAO_ARQUIVO_CSV)).thenThrow(CsvDataTypeMismatchException.class);
			anexoRelatorioService.carregarDados(opcoes);
			verify(logger).error(anyString());
			verify(logger).error(any(CsvDataTypeMismatchException.class));

		} catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException | IOException e) {
			e.printStackTrace();
		}		
	}

	@Test
	@PrepareForTest({LoggerUtil.class})
	public void testeSalvarBancoDados() {
		OpcoesCargaDadosAnexoRelatorio opcoes = new OpcoesCargaDadosAnexoRelatorio.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.BANCO)
				.build();
		Logger logger = mockLogger();
		iniciarDbUnit();
		List<AnexoRelatorio> listaAnexoRelatorio = Arrays.asList(obterAnexoRelatorio());
		when(consultaApiUtil.lerEntidades(any(), eq(AnexoRelatorio.class))).thenReturn(listaAnexoRelatorio);		
		anexoRelatorioService.carregarDados(opcoes);
		verify(logger).info("Persistindo os dados obtidos (" + listaAnexoRelatorio.size() + " registro(s))...");
	}

	@Test
	@PrepareForTest({LoggerUtil.class})
	public void testeSalvarBancoDadosListaVazia() {
		OpcoesCargaDadosAnexoRelatorio opcoes = new OpcoesCargaDadosAnexoRelatorio.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.BANCO)
				.build();
		Logger logger = mockLogger();
		iniciarDbUnit();
		List<AnexoRelatorio> listaAnexoRelatorio = new ArrayList<AnexoRelatorio>();
		when(consultaApiUtil.lerEntidades(any(), eq(AnexoRelatorio.class))).thenReturn(listaAnexoRelatorio);		
		anexoRelatorioService.carregarDados(opcoes);
		verify(logger).info("Sem dados para persistir");
	}

	@Test
	@PrepareForTest({LoggerUtil.class})
	public void testeExceptionEmConsultaApi() {
		Logger logger = mockLogger();
		
		when(consultaApiUtil.lerEntidades(any(), eq(AnexoRelatorio.class))).thenThrow(RuntimeException.class);		
		List<AnexoRelatorio> listaAnexoRelatorio = anexoRelatorioService.consultarNaApi(new APIQueryParamUtil());
		assertEquals(0, listaAnexoRelatorio.size());
		verify(logger).error(anyString());
	}

	@Test
	public void testeExcluirTodos() {
		iniciarDbUnit();
		int qtdeLinhas = anexoRelatorioService.excluirTodos();
		assertEquals(161, qtdeLinhas);
	}
	
	private void iniciarDbUnit() {
		dbUnitHelper = new DbUnitHelper(anexoRelatorioService.getEntityManager(), "DbUnitXml");
		//dbUnitHelper.execute(DatabaseOperation.DELETE_ALL, "AnexoRelatorio.xml");		 
	    dbUnitHelper.execute(DatabaseOperation.INSERT, "AnexoRelatorio.xml");
	}
	
	private Logger mockLogger() {
		Logger logger = mock(Logger.class);
		mockStatic(LoggerUtil.class);
		when(LoggerUtil.createLogger(any())).thenReturn(logger);
		return logger;
	}

	private AnexoRelatorio obterAnexoRelatorio() {
		AnexoRelatorio anexoRelatorio = new AnexoRelatorio();
		anexoRelatorio.setId(1);
		anexoRelatorio.setAnexo("anexo");
		anexoRelatorio.setDemonstrativo("demonstrativo");
		anexoRelatorio.setEsfera("esfera");
		return anexoRelatorio;
	}
}
