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

import br.gov.ce.sefaz.siconfi.entity.DeclaracaoContasAnuais;
import br.gov.ce.sefaz.siconfi.entity.ExtratoEntrega;
import br.gov.ce.sefaz.siconfi.enums.Entregavel;
import br.gov.ce.sefaz.siconfi.enums.OpcaoSalvamentoDados;
import br.gov.ce.sefaz.siconfi.enums.Periodicidade;
import br.gov.ce.sefaz.siconfi.helper.DbUnitHelper;
import br.gov.ce.sefaz.siconfi.opcoes.OpcoesCargaDadosDCA;
import br.gov.ce.sefaz.siconfi.service.DCAService;
import br.gov.ce.sefaz.siconfi.service.EnteService;
import br.gov.ce.sefaz.siconfi.service.ExtratoEntregaService;
import br.gov.ce.sefaz.siconfi.util.ConsultaApiUtil;
import br.gov.ce.sefaz.siconfi.util.CsvUtil;
import br.gov.ce.sefaz.siconfi.util.LoggerUtil;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore( {"javax.management.*", "javax.script.*" })
public class DCAServiceTest {

	private static final String[] COLUNAS_ARQUIVO_CSV = new String[] { "exercicio", "uf", "cod_ibge", "instituicao", "anexo",
			"cod_conta", "conta", "coluna", "rotulo", "populacao", "valorFormatado" };	
	private static final String NOME_PADRAO_ARQUIVO_CSV = "dca.csv";

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;
	
	@Mock
	public ConsultaApiUtil<DeclaracaoContasAnuais> consultaApiUtil;

	@Mock
	public CsvUtil<DeclaracaoContasAnuais> csvUtil;

	@Mock
	public EnteService enteService;

	@Mock
	public ExtratoEntregaService extratoEntregaService;

	@InjectMocks
	public DCAService dcaService;
	
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
		DCAService dcaService = new DCAService();
		assertNotNull(dcaService);
	}

	@Test
	public void testeCarregarDadosArquivoSemNomeComListaAnexos() {
		OpcoesCargaDadosDCA opcoes = new OpcoesCargaDadosDCA.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.ARQUIVO)
				.listaAnexos(Arrays.asList("DCA-Anexo I-C", "DCA-Anexo I-D", "DCA-Anexo I-E"))
				.build();
		DeclaracaoContasAnuais dca = obterDeclaracaoContasAnuais();
		when(enteService.obterListaCodigosIbgeNaAPI(opcoes)).thenReturn(Arrays.asList("21","22","23"));
		when(consultaApiUtil.lerEntidades(any(), eq(DeclaracaoContasAnuais.class))).thenReturn(Arrays.asList(dca));

		dcaService.carregarDados(opcoes);
		verify(enteService).obterListaCodigosIbgeNaAPI(opcoes);
		verify(consultaApiUtil, times(9)).lerEntidades(any(), eq(DeclaracaoContasAnuais.class));
		
		try {
			verify(csvUtil).writeHeader(COLUNAS_ARQUIVO_CSV, NOME_PADRAO_ARQUIVO_CSV);
			verify(csvUtil,times(9)).writeToFile(Arrays.asList(dca), COLUNAS_ARQUIVO_CSV, NOME_PADRAO_ARQUIVO_CSV);
		} catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testeCarregarDadosArquivoComNomeSemListaAnexos() {
		OpcoesCargaDadosDCA opcoes = new OpcoesCargaDadosDCA.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.ARQUIVO)
				.nomeArquivo("relatorio.csv")
				.build();
		DeclaracaoContasAnuais dca = obterDeclaracaoContasAnuais();
		when(enteService.obterListaCodigosIbgeNaAPI(opcoes)).thenReturn(Arrays.asList("22","23"));
		when(consultaApiUtil.lerEntidades(any(), eq(DeclaracaoContasAnuais.class))).thenReturn(Arrays.asList(dca));

		dcaService.carregarDados(opcoes);
		verify(enteService).obterListaCodigosIbgeNaAPI(opcoes);
		verify(consultaApiUtil, times(DCAService.ANEXOS_DCA.size()*2)).lerEntidades(any(), eq(DeclaracaoContasAnuais.class));
		
		try {
			verify(csvUtil).writeHeader(COLUNAS_ARQUIVO_CSV, "relatorio.csv");
			verify(csvUtil,times(DCAService.ANEXOS_DCA.size()*2)).writeToFile(Arrays.asList(dca), COLUNAS_ARQUIVO_CSV, "relatorio.csv");
		} catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testeCarregarDadosConsiderandoExtratoEntrega() {
		OpcoesCargaDadosDCA opcoes = new OpcoesCargaDadosDCA.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.CONSOLE)
				.exercicios(Arrays.asList(2019))
				.listaAnexos(Arrays.asList("DCA-Anexo I-C", "DCA-Anexo I-D", "DCA-Anexo I-E"))
				.considerarExtratoEntrega(true)
				.build();
		DeclaracaoContasAnuais dca = obterDeclaracaoContasAnuais();
		when(extratoEntregaService.consultarNaBase(opcoes, 2019, Entregavel.DCA)).thenReturn(Arrays.asList(obterExtratoEntrega()));
		when(consultaApiUtil.lerEntidades(any(), eq(DeclaracaoContasAnuais.class))).thenReturn(Arrays.asList(dca));

		dcaService.carregarDados(opcoes);
		verify(extratoEntregaService).consultarNaBase(opcoes, 2019, Entregavel.DCA);
		verify(consultaApiUtil, times(3)).lerEntidades(any(), eq(DeclaracaoContasAnuais.class));
	}

	@Test
	@PrepareForTest({LoggerUtil.class})
	public void testeCarregarDadosNaBase() {
		Logger logger = mockLogger();
		OpcoesCargaDadosDCA opcoes = new OpcoesCargaDadosDCA.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.BANCO)
				.exercicios(Arrays.asList(2019))
				.build();
		iniciarDbUnit();

		when(enteService.obterListaCodigosIbgeNaAPI(opcoes)).thenReturn(Arrays.asList("23"));
		when(consultaApiUtil.lerEntidades(any(), eq(DeclaracaoContasAnuais.class))).thenReturn(Arrays.asList(obterDeclaracaoContasAnuais()));

		dcaService.carregarDados(opcoes);
		
		verify(enteService).obterListaCodigosIbgeNaAPI(opcoes);
		verify(consultaApiUtil, times(DCAService.ANEXOS_DCA.size())).lerEntidades(any(), eq(DeclaracaoContasAnuais.class));
		
		verify(logger, times(DCAService.ANEXOS_DCA.size())).info("Excluindo dados do banco de dados...");
		verify(logger).info("Linhas excluídas:50");		
	}

	public void iniciarDbUnit() {
		dbUnitHelper = new DbUnitHelper(dcaService.getEntityManager(), "DbUnitXml");
		dbUnitHelper.execute(DatabaseOperation.DELETE_ALL, "DeclaracaoContasAnuais.xml");		 
	    dbUnitHelper.execute(DatabaseOperation.INSERT, "DeclaracaoContasAnuais.xml");
	}

	private Logger mockLogger() {
		Logger logger = mock(Logger.class);
		mockStatic(LoggerUtil.class);
		when(LoggerUtil.createLogger(any())).thenReturn(logger);
		return logger;
	}

	private DeclaracaoContasAnuais obterDeclaracaoContasAnuais() {
		DeclaracaoContasAnuais dca = new DeclaracaoContasAnuais();
		dca.setAnexo("anexo");
		dca.setExercicio(2019);
		dca.setCod_ibge("23");
		dca.setCod_conta("100");
		dca.setColuna("coluna");
		dca.setConta("conta");
		dca.setInstituicao("Governo");
		dca.setRotulo("rotulo");
		dca.setUf("CE");
		dca.setPopulacao(1000000l);
		dca.setValor(200.0);
		return dca;
	}
	
	private ExtratoEntrega obterExtratoEntrega() {
		ExtratoEntrega extrato = new ExtratoEntrega();
		extrato.setCod_ibge("23");
		extrato.setExercicio(2019);
		extrato.setPeriodo(1);
		extrato.setPeriodicidade(Periodicidade.ANUAL.getCodigo());
		extrato.setInstituicao("Governo");
		extrato.setId(1);
		return extrato;
	}

}
