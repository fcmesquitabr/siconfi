package br.gov.ce.sefaz.siconfi.service.test;

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

import br.gov.ce.sefaz.siconfi.entity.MatrizSaldoContabeisPatrimonial;
import br.gov.ce.sefaz.siconfi.enums.OpcaoSalvamentoDados;
import br.gov.ce.sefaz.siconfi.enums.TipoMatrizSaldoContabeis;
import br.gov.ce.sefaz.siconfi.enums.TipoValorMatrizSaldoContabeis;
import br.gov.ce.sefaz.siconfi.helper.DbUnitHelper;
import br.gov.ce.sefaz.siconfi.opcoes.OpcoesCargaDadosMSC;
import br.gov.ce.sefaz.siconfi.service.EnteService;
import br.gov.ce.sefaz.siconfi.service.MSCPatrimonialService;
import br.gov.ce.sefaz.siconfi.util.Constantes;
import br.gov.ce.sefaz.siconfi.util.ConsultaApiUtil;
import br.gov.ce.sefaz.siconfi.util.CsvUtil;
import br.gov.ce.sefaz.siconfi.util.LoggerUtil;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({ "javax.management.*", "javax.script.*" })
public class MSCPatrimonialServiceTest {

	private static final String[] COLUNAS_ARQUIVO_CSV = new String[] { "exercicio", "mes_referencia", "cod_ibge", "poder_orgao", "tipo_matriz",
			"classe_conta", "natureza_conta", "conta_contabil", "financeiro_permanente", "ano_fonte_recursos", "fonte_recursos", 
			"divida_consolidada", "data_referencia", "entrada_msc", "tipo_valor", "valorFormatado"};	
	private static final String NOME_PADRAO_ARQUIVO_CSV = "msc_patrimonial.csv";

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;

	@Mock
	public ConsultaApiUtil<MatrizSaldoContabeisPatrimonial> consultaApiUtil;

	@Mock
	public CsvUtil<MatrizSaldoContabeisPatrimonial> csvUtil;

	@Mock
	public EnteService enteService;

	@InjectMocks
	private MSCPatrimonialService mscService;

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
		MSCPatrimonialService mscService = new MSCPatrimonialService();
		assertNotNull(mscService);
	}

	public void iniciarDbUnit() {
		dbUnitHelper = new DbUnitHelper(mscService.getEntityManager(), "DbUnitXml");
		dbUnitHelper.execute(DatabaseOperation.DELETE_ALL, "MSCPatrimonial.xml");
		dbUnitHelper.execute(DatabaseOperation.INSERT, "MSCPatrimonial.xml");
	}
	
	@Test
	public void testeCarregarDadosArquivoSemNomeTipoValorComClasses() {
		List<Integer> listaClassesConta = Arrays.asList(1 , 2);
		OpcoesCargaDadosMSC opcoes = new OpcoesCargaDadosMSC.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.ARQUIVO)
				.listaClasseConta(listaClassesConta).build();
		MatrizSaldoContabeisPatrimonial mscp = obterMSCPatrimonial();
		List<String> listaCodigoIbge = Arrays.asList("21", "22", "23");
		when(enteService.obterListaCodigosIbgeNaAPI(opcoes)).thenReturn(listaCodigoIbge);
		when(consultaApiUtil.lerEntidades(any(), eq(MatrizSaldoContabeisPatrimonial.class)))
				.thenReturn(Arrays.asList(mscp));

		mscService.carregarDados(opcoes);
		verify(enteService).obterListaCodigosIbgeNaAPI(opcoes);
		verify(consultaApiUtil,
				times(listaClassesConta.size() * listaCodigoIbge.size() * TipoValorMatrizSaldoContabeis.values().length
						* Constantes.MESES.size())).lerEntidades(any(), eq(MatrizSaldoContabeisPatrimonial.class));

		try {
			verify(csvUtil).writeHeader(COLUNAS_ARQUIVO_CSV, NOME_PADRAO_ARQUIVO_CSV);
			verify(csvUtil, times(listaClassesConta.size() * listaCodigoIbge.size() * TipoValorMatrizSaldoContabeis.values().length * Constantes.BIMESTRES.size()))
					.writeToFile(Arrays.asList(mscp), COLUNAS_ARQUIVO_CSV, NOME_PADRAO_ARQUIVO_CSV);
		} catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testeCarregarDadosArquivoComNomeTipoValorSemClasses() {
		List<TipoValorMatrizSaldoContabeis> listaTipoValor = Arrays.asList(TipoValorMatrizSaldoContabeis.period_change,
				TipoValorMatrizSaldoContabeis.ending_balance);
		OpcoesCargaDadosMSC opcoes = new OpcoesCargaDadosMSC.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.ARQUIVO)
				.nomeArquivo("relatorio.csv")
				.listaTipoValor(listaTipoValor)
				.build();
		MatrizSaldoContabeisPatrimonial mscp = obterMSCPatrimonial();
		List<String> listaCodigoIbge = Arrays.asList("22", "23");
		when(enteService.obterListaCodigosIbgeNaAPI(opcoes)).thenReturn(listaCodigoIbge);
		when(consultaApiUtil.lerEntidades(any(), eq(MatrizSaldoContabeisPatrimonial.class)))
				.thenReturn(Arrays.asList(mscp));

		mscService.carregarDados(opcoes);
		verify(enteService).obterListaCodigosIbgeNaAPI(opcoes);
		verify(consultaApiUtil,
				times(Constantes.CLASSES_CONTAS_PATRIMONIAIS.size() * listaTipoValor.size() * listaCodigoIbge.size() * Constantes.BIMESTRES.size()))
						.lerEntidades(any(), eq(MatrizSaldoContabeisPatrimonial.class));

		try {
			verify(csvUtil).writeHeader(COLUNAS_ARQUIVO_CSV, "relatorio.csv");
			verify(csvUtil,
					times(Constantes.CLASSES_CONTAS_PATRIMONIAIS.size() * listaTipoValor.size() * listaCodigoIbge.size() * Constantes.BIMESTRES.size()))
							.writeToFile(Arrays.asList(mscp), COLUNAS_ARQUIVO_CSV, "relatorio.csv");
		} catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testeExcluir() {
		OpcoesCargaDadosMSC opcoes = new OpcoesCargaDadosMSC.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.BANCO)
				.exercicios(Arrays.asList(2020))
				.periodos(Arrays.asList(1))
				.codigosIbge(Arrays.asList("23"))
				.build();
		iniciarDbUnit();
		when(enteService.obterListaCodigosIbgeNaAPI(any())).thenReturn(Arrays.asList("23"));
		assertEquals(20, mscService.excluir(opcoes)); //10 linhas para o period_change e 10 linhas para o ending_balance
	}

	@Test
	@PrepareForTest({LoggerUtil.class})
	public void testeCarregarDadosNaBase() {
		Logger logger = mockLogger();
		OpcoesCargaDadosMSC opcoes = new OpcoesCargaDadosMSC.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.BANCO)
				.exercicios(Arrays.asList(2020))
				.periodos(Arrays.asList(1))
				.build();
		iniciarDbUnit();

		when(enteService.obterListaCodigosIbgeNaAPI(any())).thenReturn(Arrays.asList("23"));
		when(consultaApiUtil.lerEntidades(any(), eq(MatrizSaldoContabeisPatrimonial.class))).thenReturn(Arrays.asList(obterMSCPatrimonial()));

		mscService.carregarDados(opcoes);
		
		verify(enteService).obterListaCodigosIbgeNaAPI(opcoes);
		verify(consultaApiUtil, times(Constantes.CLASSES_CONTAS_PATRIMONIAIS.size() * TipoValorMatrizSaldoContabeis.values().length)).lerEntidades(any(), eq(MatrizSaldoContabeisPatrimonial.class));		
		verify(logger, times(Constantes.CLASSES_CONTAS_PATRIMONIAIS.size() * TipoValorMatrizSaldoContabeis.values().length)).info("Excluindo dados do banco de dados...");
	}

	private MatrizSaldoContabeisPatrimonial obterMSCPatrimonial() {
		MatrizSaldoContabeisPatrimonial mscp = new MatrizSaldoContabeisPatrimonial();
		mscp.setExercicio(2019);
		mscp.setMesReferencia(1);
		mscp.setCodigoIbge("23");
		mscp.setAnoFonteRecursos(2019);
		mscp.setClasseConta(1);
		mscp.setContaContabil("10000");
		mscp.setDividaConsolidada(0);
		mscp.setFinanceiroPermanente(0);
		mscp.setNaturezaConta("C");
		mscp.setFonteRecursos("");
		mscp.setTipoMatriz(TipoMatrizSaldoContabeis.MSCC.getCodigo());
		mscp.setTipoValor(TipoValorMatrizSaldoContabeis.period_change.getCodigo());
		mscp.setValor(10.1);
		mscp.setPoderOrgao("1000");
		return mscp;
	}

	private Logger mockLogger() {
		Logger logger = mock(Logger.class);
		mockStatic(LoggerUtil.class);
		when(LoggerUtil.createLogger(any())).thenReturn(logger);
		return logger;
	}
}
