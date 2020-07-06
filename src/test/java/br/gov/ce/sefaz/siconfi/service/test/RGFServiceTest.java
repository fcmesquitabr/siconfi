package br.gov.ce.sefaz.siconfi.service.test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
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

import br.gov.ce.sefaz.siconfi.entity.RelatorioGestaoFiscal;
import br.gov.ce.sefaz.siconfi.enums.OpcaoSalvamentoDados;
import br.gov.ce.sefaz.siconfi.enums.Periodicidade;
import br.gov.ce.sefaz.siconfi.enums.Poder;
import br.gov.ce.sefaz.siconfi.helper.DbUnitHelper;
import br.gov.ce.sefaz.siconfi.opcoes.OpcoesCargaDadosRGF;
import br.gov.ce.sefaz.siconfi.service.EnteService;
import br.gov.ce.sefaz.siconfi.service.RGFService;
import br.gov.ce.sefaz.siconfi.util.ConsultaApiUtil;
import br.gov.ce.sefaz.siconfi.util.CsvUtil;
import br.gov.ce.sefaz.siconfi.util.LoggerUtil;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore( {"javax.management.*", "javax.script.*" })
public class RGFServiceTest {

	private static final String[] COLUNAS_ARQUIVO_CSV = new String[] { "exercicio", "periodicidade", "periodo", "uf",
			"cod_ibge", "co_poder", "instituicao", "anexo", "cod_conta", "conta", "coluna", "rotulo", "populacao",
			"valorFormatado" };
	private static final String NOME_PADRAO_ARQUIVO_CSV = "rgf.csv";
	
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;
	
	@Mock
	public ConsultaApiUtil<RelatorioGestaoFiscal> consultaApiUtil;

	@Mock
	public CsvUtil<RelatorioGestaoFiscal> csvUtil;

	@Mock
	public EnteService enteService;

	@InjectMocks
	private RGFService rgfService;
	
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
		RGFService rgfService = new RGFService();
		assertNotNull(rgfService);
	}

	@Test
	public void testeCarregarDadosArquivoSemNomeComListaAnexos() {
		List<String> listaAnexos = Arrays.asList("RGF-Anexo 01", "RGF-Anexo 01", "RGF-Anexo 01");
		OpcoesCargaDadosRGF opcoes = new OpcoesCargaDadosRGF.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.ARQUIVO)
				.listaAnexos(listaAnexos)
				.build();
		RelatorioGestaoFiscal rgf = obterRelatorioGestaoFiscal();
		List<String> listaCodigoIbge = Arrays.asList("21","22","23");
		when(enteService.obterListaCodigosIbgeNaAPI(opcoes)).thenReturn(listaCodigoIbge);
		when(consultaApiUtil.lerEntidades(any(), eq(RelatorioGestaoFiscal.class))).thenReturn(Arrays.asList(rgf));

		rgfService.carregarDados(opcoes);
		verify(enteService).obterListaCodigosIbgeNaAPI(opcoes);
		verify(consultaApiUtil, times(listaAnexos.size()*listaCodigoIbge.size()*Poder.values().length)).lerEntidades(any(), eq(RelatorioGestaoFiscal.class));
		
		try {
			verify(csvUtil).writeHeader(COLUNAS_ARQUIVO_CSV, NOME_PADRAO_ARQUIVO_CSV);
			verify(csvUtil,times(listaAnexos.size()*listaCodigoIbge.size()*Poder.values().length)).writeToFile(Arrays.asList(rgf), COLUNAS_ARQUIVO_CSV, NOME_PADRAO_ARQUIVO_CSV);
		} catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testeCarregarDadosArquivoComNomePoderSemListaAnexos() {
		List<Poder> listaPoderes = Arrays.asList(Poder.EXECUTIVO, Poder.JUDICIARIO);
		OpcoesCargaDadosRGF opcoes = new OpcoesCargaDadosRGF.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.ARQUIVO)
				.listaPoderes(listaPoderes)
				.nomeArquivo("relatorio.csv")
				.build();
		RelatorioGestaoFiscal rgf = obterRelatorioGestaoFiscal();
		List<String> listaCodigoIbge = Arrays.asList("22","23");
		when(enteService.obterListaCodigosIbgeNaAPI(opcoes)).thenReturn(listaCodigoIbge);
		when(consultaApiUtil.lerEntidades(any(), eq(RelatorioGestaoFiscal.class))).thenReturn(Arrays.asList(rgf));

		rgfService.carregarDados(opcoes);
		verify(enteService).obterListaCodigosIbgeNaAPI(opcoes);
		verify(consultaApiUtil, times(RGFService.ANEXOS_RGF.size()*listaCodigoIbge.size()*listaPoderes.size())).lerEntidades(any(), eq(RelatorioGestaoFiscal.class));
		
		try {
			verify(csvUtil).writeHeader(COLUNAS_ARQUIVO_CSV, "relatorio.csv");
			verify(csvUtil,times(RGFService.ANEXOS_RGF.size()*listaCodigoIbge.size()*listaPoderes.size())).writeToFile(Arrays.asList(rgf), COLUNAS_ARQUIVO_CSV, "relatorio.csv");
		} catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
			e.printStackTrace();
		}
	}

	@Test
	@PrepareForTest({LoggerUtil.class})
	public void testeCarregarDadosNaBase() {
		Logger logger = mockLogger();
		OpcoesCargaDadosRGF opcoes = new OpcoesCargaDadosRGF.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.BANCO)
				.exercicios(Arrays.asList(2020))
				.periodos(Arrays.asList(1))
				.build();
		iniciarDbUnit();

		when(enteService.obterListaCodigosIbgeNaAPI(any())).thenReturn(Arrays.asList("23"));
		when(consultaApiUtil.lerEntidades(any(), eq(RelatorioGestaoFiscal.class))).thenReturn(Arrays.asList(obterRelatorioGestaoFiscal()));

		rgfService.carregarDados(opcoes);
		
		verify(enteService).obterListaCodigosIbgeNaAPI(opcoes);
		verify(consultaApiUtil, times(RGFService.ANEXOS_RGF.size() * Poder.values().length)).lerEntidades(any(), eq(RelatorioGestaoFiscal.class));
		
		verify(logger, times(RGFService.ANEXOS_RGF.size() * Poder.values().length)).info("Excluindo dados do banco de dados...");
		verify(logger, times(2)).info("Linhas excluídas:10");	//10 linhas para o anexo 01 e 10 linhas para o anexo 02
		verify(logger, times( RGFService.ANEXOS_RGF.size() * Poder.values().length - 2 )).info("Linhas excluídas:0");
	}

	private RelatorioGestaoFiscal obterRelatorioGestaoFiscal() {
		RelatorioGestaoFiscal rgf = new RelatorioGestaoFiscal();
		rgf.setAnexo("RGF-Anexo 01");
		rgf.setCo_poder(Poder.EXECUTIVO.getCodigo());
		rgf.setCod_ibge("23");
		rgf.setExercicio(2019);
		rgf.setPeriodicidade(Periodicidade.QUADRIMESTRAL.getCodigo());
		rgf.setPeriodo(1);
		rgf.setUf("CE");
		return rgf;
	}

	public void iniciarDbUnit() {
		dbUnitHelper = new DbUnitHelper(rgfService.getEntityManager(), "DbUnitXml");
		dbUnitHelper.execute(DatabaseOperation.DELETE_ALL, "RelatorioGestaoFiscal.xml");		 
	    dbUnitHelper.execute(DatabaseOperation.INSERT, "RelatorioGestaoFiscal.xml");
	}

	private Logger mockLogger() {
		Logger logger = mock(Logger.class);
		mockStatic(LoggerUtil.class);
		when(LoggerUtil.createLogger(any())).thenReturn(logger);
		return logger;
	}

}
