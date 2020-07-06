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

import br.gov.ce.sefaz.siconfi.entity.RelatorioResumidoExecucaoOrcamentaria;
import br.gov.ce.sefaz.siconfi.enums.OpcaoSalvamentoDados;
import br.gov.ce.sefaz.siconfi.enums.Periodicidade;
import br.gov.ce.sefaz.siconfi.enums.TipoDemonstrativoRREO;
import br.gov.ce.sefaz.siconfi.helper.DbUnitHelper;
import br.gov.ce.sefaz.siconfi.opcoes.OpcoesCargaDadosRREO;
import br.gov.ce.sefaz.siconfi.service.EnteService;
import br.gov.ce.sefaz.siconfi.service.RREOService;
import br.gov.ce.sefaz.siconfi.util.Constantes;
import br.gov.ce.sefaz.siconfi.util.ConsultaApiUtil;
import br.gov.ce.sefaz.siconfi.util.CsvUtil;
import br.gov.ce.sefaz.siconfi.util.LoggerUtil;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({ "javax.management.*", "javax.script.*" })
public class RREOServiceTest {

	private static final String[] COLUNAS_ARQUIVO_CSV = new String[] { "exercicio", "periodicidade", "periodo", "uf",
			"cod_ibge", "instituicao", "demonstrativo", "anexo", "cod_conta", "conta", "coluna", "rotulo", "populacao",
			"valorFormatado" };
	private static final String NOME_PADRAO_ARQUIVO_CSV = "rreo.csv";

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;

	@Mock
	public ConsultaApiUtil<RelatorioResumidoExecucaoOrcamentaria> consultaApiUtil;

	@Mock
	public CsvUtil<RelatorioResumidoExecucaoOrcamentaria> csvUtil;

	@Mock
	public EnteService enteService;

	@InjectMocks
	private RREOService rreoService;

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
		RREOService rreoService = new RREOService();
		assertNotNull(rreoService);
	}

	public void iniciarDbUnit() {
		dbUnitHelper = new DbUnitHelper(rreoService.getEntityManager(), "DbUnitXml");
		dbUnitHelper.execute(DatabaseOperation.DELETE_ALL, "RelatorioResumidoExecucaoOrcamentaria.xml");
		dbUnitHelper.execute(DatabaseOperation.INSERT, "RelatorioResumidoExecucaoOrcamentaria.xml");
	}

	@Test
	public void testeCarregarDadosArquivoSemNomeComListaAnexos() {
		List<String> listaAnexos = Arrays.asList("RREO-Anexo 01", "RREO-Anexo 02", "RREO-Anexo 03");
		OpcoesCargaDadosRREO opcoes = new OpcoesCargaDadosRREO.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.ARQUIVO).listaAnexos(listaAnexos).build();
		RelatorioResumidoExecucaoOrcamentaria rreo = obterRelatorioResumidoExecucaoOrcamentaria();
		List<String> listaCodigoIbge = Arrays.asList("21", "22", "23");
		when(enteService.obterListaCodigosIbgeNaAPI(opcoes)).thenReturn(listaCodigoIbge);
		when(consultaApiUtil.lerEntidades(any(), eq(RelatorioResumidoExecucaoOrcamentaria.class)))
				.thenReturn(Arrays.asList(rreo));

		rreoService.carregarDados(opcoes);
		verify(enteService).obterListaCodigosIbgeNaAPI(opcoes);
		verify(consultaApiUtil, times(listaAnexos.size() * listaCodigoIbge.size() * Constantes.BIMESTRES.size()))
				.lerEntidades(any(), eq(RelatorioResumidoExecucaoOrcamentaria.class));

		try {
			verify(csvUtil).writeHeader(COLUNAS_ARQUIVO_CSV, NOME_PADRAO_ARQUIVO_CSV);
			verify(csvUtil, times(listaAnexos.size() * listaCodigoIbge.size() * Constantes.BIMESTRES.size()))
					.writeToFile(Arrays.asList(rreo), COLUNAS_ARQUIVO_CSV, NOME_PADRAO_ARQUIVO_CSV);
		} catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testeCarregarDadosArquivoComNomeSemListaAnexos() {
		OpcoesCargaDadosRREO opcoes = new OpcoesCargaDadosRREO.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.ARQUIVO).nomeArquivo("relatorio.csv").build();
		RelatorioResumidoExecucaoOrcamentaria rreo = obterRelatorioResumidoExecucaoOrcamentaria();
		List<String> listaCodigoIbge = Arrays.asList("22", "23");
		when(enteService.obterListaCodigosIbgeNaAPI(opcoes)).thenReturn(listaCodigoIbge);
		when(consultaApiUtil.lerEntidades(any(), eq(RelatorioResumidoExecucaoOrcamentaria.class)))
				.thenReturn(Arrays.asList(rreo));

		rreoService.carregarDados(opcoes);
		verify(enteService).obterListaCodigosIbgeNaAPI(opcoes);
		verify(consultaApiUtil,
				times(RREOService.ANEXOS_RREO.size() * listaCodigoIbge.size() * Constantes.BIMESTRES.size()))
						.lerEntidades(any(), eq(RelatorioResumidoExecucaoOrcamentaria.class));

		try {
			verify(csvUtil).writeHeader(COLUNAS_ARQUIVO_CSV, "relatorio.csv");
			verify(csvUtil,
					times(RREOService.ANEXOS_RREO.size() * listaCodigoIbge.size() * Constantes.BIMESTRES.size()))
							.writeToFile(Arrays.asList(rreo), COLUNAS_ARQUIVO_CSV, "relatorio.csv");
		} catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
			e.printStackTrace();
		}
	}

	@Test
	@PrepareForTest({LoggerUtil.class})
	public void testeCarregarDadosNaBase() {
		Logger logger = mockLogger();
		OpcoesCargaDadosRREO opcoes = new OpcoesCargaDadosRREO.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.BANCO)
				.exercicios(Arrays.asList(2020))
				.periodos(Arrays.asList(1))
				.build();
		iniciarDbUnit();

		when(enteService.obterListaCodigosIbgeNaAPI(any())).thenReturn(Arrays.asList("23"));
		when(consultaApiUtil.lerEntidades(any(), eq(RelatorioResumidoExecucaoOrcamentaria.class))).thenReturn(Arrays.asList(obterRelatorioResumidoExecucaoOrcamentaria()));

		rreoService.carregarDados(opcoes);
		
		verify(enteService).obterListaCodigosIbgeNaAPI(opcoes);
		verify(consultaApiUtil, times(RREOService.ANEXOS_RREO.size())).lerEntidades(any(), eq(RelatorioResumidoExecucaoOrcamentaria.class));
		
		verify(logger, times(RREOService.ANEXOS_RREO.size())).info("Excluindo dados do banco de dados...");
		verify(logger, times(2)).info("Linhas excluídas:10");	//10 linhas para o anexo 01 e 10 linhas para o anexo 02	
		verify(logger, times(RREOService.ANEXOS_RREO.size() - 2)).info("Linhas excluídas:0");		
	}

	private RelatorioResumidoExecucaoOrcamentaria obterRelatorioResumidoExecucaoOrcamentaria() {
		RelatorioResumidoExecucaoOrcamentaria rreo = new RelatorioResumidoExecucaoOrcamentaria();
		rreo.setAnexo("RREO-Anexo 01");
		rreo.setCod_ibge("23");
		rreo.setExercicio(2019);
		rreo.setPeriodo(1);
		rreo.setPeriodicidade(Periodicidade.BIMESTRAL.getCodigo());
		rreo.setUf("CE");
		rreo.setDemonstrativo(TipoDemonstrativoRREO.RREO.getCodigo());
		return rreo;
	}

	private Logger mockLogger() {
		Logger logger = mock(Logger.class);
		mockStatic(LoggerUtil.class);
		when(LoggerUtil.createLogger(any())).thenReturn(logger);
		return logger;
	}
}
