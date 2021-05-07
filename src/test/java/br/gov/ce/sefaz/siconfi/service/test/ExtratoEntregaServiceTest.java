package br.gov.ce.sefaz.siconfi.service.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
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
import java.util.Calendar;
import java.util.Date;
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

import br.gov.ce.sefaz.siconfi.entity.ExtratoEntrega;
import br.gov.ce.sefaz.siconfi.enums.Entregavel;
import br.gov.ce.sefaz.siconfi.enums.Esfera;
import br.gov.ce.sefaz.siconfi.enums.OpcaoSalvamentoDados;
import br.gov.ce.sefaz.siconfi.enums.Periodicidade;
import br.gov.ce.sefaz.siconfi.helper.DbUnitHelper;
import br.gov.ce.sefaz.siconfi.opcoes.OpcoesCargaDadosExtratoEntrega;
import br.gov.ce.sefaz.siconfi.service.EnteService;
import br.gov.ce.sefaz.siconfi.service.ExtratoEntregaService;
import br.gov.ce.sefaz.siconfi.util.APIQueryParamUtil;
import br.gov.ce.sefaz.siconfi.util.Constantes;
import br.gov.ce.sefaz.siconfi.util.ConsultaApiUtil;
import br.gov.ce.sefaz.siconfi.util.CsvUtil;
import br.gov.ce.sefaz.siconfi.util.LoggerUtil;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore( {"javax.management.*", "javax.script.*" })
public class ExtratoEntregaServiceTest {

	private static final String[] COLUNAS_ARQUIVO_CSV = new String[] { "exercicio", "cod_ibge", "populacao", "instituicao",
			"entregavel", "periodo", "periodicidade", "status_relatorio", "dataFormatada", "forma_envio", "tipo_relatorio" };	
	private static final String NOME_PADRAO_ARQUIVO_CSV = "extrato-entrega.csv";
	
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;
	
	@Mock
	public ConsultaApiUtil<ExtratoEntrega> consultaApiUtil;

	@Mock
	public CsvUtil<ExtratoEntrega> csvUtil;

	@Mock
	public EnteService enteService;

	@InjectMocks
	public ExtratoEntregaService extratoEntregaService;
	
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
		ExtratoEntregaService extratoService = new ExtratoEntregaService();
		assertNotNull(extratoService);
	}

	@Test
	public void testeCarregarDadosArquivoSemNome() {
		OpcoesCargaDadosExtratoEntrega opcoes = new OpcoesCargaDadosExtratoEntrega.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.ARQUIVO)
				.build();
		ExtratoEntrega extrato = obterExtratoEntrega();
		when(enteService.obterListaCodigosIbgeNaAPI(opcoes)).thenReturn(Arrays.asList("22","23"));
		when(consultaApiUtil.lerEntidades(any(), eq(ExtratoEntrega.class))).thenReturn(Arrays.asList(extrato));

		extratoEntregaService.carregarDados(opcoes);
		verify(enteService).obterListaCodigosIbgeNaAPI(opcoes);
		verify(consultaApiUtil, times(2)).lerEntidades(any(), eq(ExtratoEntrega.class));
		
		try {
			verify(csvUtil).writeHeader(COLUNAS_ARQUIVO_CSV, NOME_PADRAO_ARQUIVO_CSV);
			verify(csvUtil,times(2)).writeToFile(Arrays.asList(extrato), COLUNAS_ARQUIVO_CSV, NOME_PADRAO_ARQUIVO_CSV);
		} catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testeExcluir() {
		OpcoesCargaDadosExtratoEntrega opcoes = new OpcoesCargaDadosExtratoEntrega.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.BANCO)
				.codigosIbge(Arrays.asList("22","23"))
				.exercicios(Arrays.asList(2019))
				.build();
		iniciarDbUnit();
		when(enteService.obterListaCodigosIbgeNaAPI(opcoes)).thenReturn(Arrays.asList("22","23"));
		assertEquals(33, extratoEntregaService.excluir(opcoes));
	}

	@Test
	@PrepareForTest({LoggerUtil.class})
	public void testeCarregarDadosBanco() {
		OpcoesCargaDadosExtratoEntrega opcoes = new OpcoesCargaDadosExtratoEntrega.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.BANCO)
				.build();
		
		Logger logger = mockLogger();
		ExtratoEntrega extrato = obterExtratoEntrega();
		
		when(enteService.obterListaCodigosIbgeNaAPI(opcoes)).thenReturn(Arrays.asList("22","23"));
		when(consultaApiUtil.lerEntidades(any(), eq(ExtratoEntrega.class))).thenReturn(Arrays.asList(extrato));

		extratoEntregaService.carregarDados(opcoes);
		verify(enteService).obterListaCodigosIbgeNaAPI(opcoes);
		verify(consultaApiUtil, times(2)).lerEntidades(any(), eq(ExtratoEntrega.class));
		verify(logger,times(2)).info("Excluindo dados do banco de dados...");
		verify(logger,times(2)).info("Fazendo commit...");
	}

	@Test
	public void testeExcluirTodos() {
		iniciarDbUnit();
		int qtdeRegistros = extratoEntregaService.excluirTodos();
		assertEquals(145, qtdeRegistros);
	}

	@Test
	public void testeConsultarNaApiComExercicio() {
		OpcoesCargaDadosExtratoEntrega opcoes = new OpcoesCargaDadosExtratoEntrega.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.CONSOLE)
				.exercicios(Arrays.asList(2019))
				.codigosIbge(Arrays.asList("23"))
				.build();
		APIQueryParamUtil apiQueryParam = new APIQueryParamUtil().addParamAnReferencia(2019).addParamIdEnte("23");
		ExtratoEntrega extratoentrega = obterExtratoEntrega();
		
		when(enteService.obterListaCodigosIbgeNaAPI(opcoes)).thenReturn(Arrays.asList("23"));
		when(consultaApiUtil.lerEntidades(eq(apiQueryParam), eq(ExtratoEntrega.class))).thenReturn(Arrays.asList(extratoentrega));
		
		List<ExtratoEntrega> listaExtrato = extratoEntregaService.consultarNaApi(opcoes);
		assertArrayEquals(new ExtratoEntrega [] {extratoentrega}, listaExtrato.toArray());
	}

	@Test
	public void testeConsultarNaApiSemExercicio() {
		OpcoesCargaDadosExtratoEntrega opcoes = new OpcoesCargaDadosExtratoEntrega.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.CONSOLE)
				.codigosIbge(Arrays.asList("23"))
				.build();
		APIQueryParamUtil apiQueryParam = new APIQueryParamUtil().addParamAnReferencia(Constantes.EXERCICIOS_DISPONIVEIS.get(0)).addParamIdEnte("23");
		ExtratoEntrega extratoentrega = obterExtratoEntrega();
		
		when(enteService.obterListaCodigosIbgeNaAPI(opcoes)).thenReturn(Arrays.asList("23"));
		when(consultaApiUtil.lerEntidades(eq(apiQueryParam), eq(ExtratoEntrega.class))).thenReturn(Arrays.asList(extratoentrega));
		
		List<ExtratoEntrega> listaExtrato = extratoEntregaService.consultarNaApi(opcoes);
		assertArrayEquals(new ExtratoEntrega [] {extratoentrega}, listaExtrato.toArray());
	}

	@Test
	public void testeConsultarNaBaseSemEsferaPeriodoDataMinima() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2019, 1, 1);
		OpcoesCargaDadosExtratoEntrega opcoes = new OpcoesCargaDadosExtratoEntrega.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.CONSOLE)
				.exercicios(Arrays.asList(2019))
				.codigosIbge(Arrays.asList("23"))
				.dataMinimaEntrega(calendar.getTime())
				.build();
		iniciarDbUnit();
		
		when(enteService.obterListaCodigosIbgeNaBase(opcoes)).thenReturn(Arrays.asList("23"));
		
		List<ExtratoEntrega> listaExtrato = extratoEntregaService.consultarNaBase(opcoes, 2019, Entregavel.DCA);
		assertEquals(1, listaExtrato.size());
	}

	@Test
	public void testeConsultarNaBaseComEsferaPeriodoDataMinima() {
		OpcoesCargaDadosExtratoEntrega opcoes = new OpcoesCargaDadosExtratoEntrega.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.CONSOLE)
				.exercicios(Arrays.asList(2019))
				.periodos(Arrays.asList(1))
				.dataMinimaEntrega(getDataMinima())
				.esfera(Esfera.MUNICIPIO)
				.build();
		iniciarDbUnit();
		
		when(enteService.obterListaCodigosIbgeNaBase(opcoes)).thenReturn(Arrays.asList("2303709","2306405","2305506"));
		
		List<ExtratoEntrega> listaExtrato = extratoEntregaService.consultarNaBase(opcoes, 2019, Entregavel.RREO);
		assertEquals(3, listaExtrato.size());
	}

	private Date getDataMinima() {
		Calendar data = Calendar.getInstance();
		data.set(2019, 1, 1);
		return data.getTime();
	}

	public void iniciarDbUnit() {
		dbUnitHelper = new DbUnitHelper(extratoEntregaService.getEntityManager(), "DbUnitXml");
		dbUnitHelper.execute(DatabaseOperation.DELETE_ALL, "ExtratoEntrega.xml");		 
	    dbUnitHelper.execute(DatabaseOperation.INSERT, "ExtratoEntrega.xml");
	}

	private Logger mockLogger() {
		Logger logger = mock(Logger.class);
		mockStatic(LoggerUtil.class);
		when(LoggerUtil.createLogger(any())).thenReturn(logger);
		return logger;
	}

	private ExtratoEntrega obterExtratoEntrega() {
		ExtratoEntrega extrato = new ExtratoEntrega();
		extrato.setCod_ibge("23");
		extrato.setExercicio(2019);
		extrato.setPeriodo(1);
		extrato.setPeriodicidade(Periodicidade.BIMESTRAL.getCodigo());
		extrato.setInstituicao("Governo");
		extrato.setId(1);
		extrato.setData_status(Calendar.getInstance().getTime());
		extrato.setEntregavel("RREO");
		extrato.setForma_envio("P");
		extrato.setTipo_relatorio("CSV");
		extrato.setStatus_relatorio("HO");
		return extrato;
	}
}
