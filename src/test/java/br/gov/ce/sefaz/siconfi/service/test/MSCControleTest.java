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

import br.gov.ce.sefaz.siconfi.entity.Ente;
import br.gov.ce.sefaz.siconfi.entity.MatrizSaldoContabeisControle;
import br.gov.ce.sefaz.siconfi.enums.OpcaoSalvamentoDados;
import br.gov.ce.sefaz.siconfi.enums.TipoMatrizSaldoContabeis;
import br.gov.ce.sefaz.siconfi.enums.TipoValorMatrizSaldoContabeis;
import br.gov.ce.sefaz.siconfi.helper.DbUnitHelper;
import br.gov.ce.sefaz.siconfi.opcoes.OpcoesCargaDadosMSC;
import br.gov.ce.sefaz.siconfi.service.EnteService;
import br.gov.ce.sefaz.siconfi.service.MSCControleService;
import br.gov.ce.sefaz.siconfi.util.Constantes;
import br.gov.ce.sefaz.siconfi.util.ConsultaApiUtil;
import br.gov.ce.sefaz.siconfi.util.CsvUtil;
import br.gov.ce.sefaz.siconfi.util.LoggerUtil;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({ "javax.management.*", "javax.script.*" })
public class MSCControleTest {

	private static final String[] COLUNAS_ARQUIVO_CSV = new String[] { "exercicio", "mesReferencia", "codigoIbge", "poderOrgao", "tipoMatriz",
			"classeConta", "naturezaConta", "contaContabil", "funcao", "subfuncao", "educacaoSaude", 
			"naturezaDespesa", "anoInscricao", "anoFonteRecursos", "fonteRecursos", 
			"dataReferencia", "entradaMsc", "tipoValor", "valorFormatado" };	
	private static final String NOME_PADRAO_ARQUIVO_CSV = "msc_controle.csv";

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;

	@Mock
	public ConsultaApiUtil<MatrizSaldoContabeisControle> consultaApiUtil;

	@Mock
	public CsvUtil<MatrizSaldoContabeisControle> csvUtil;

	@Mock
	public EnteService enteService;

	@InjectMocks
	private MSCControleService mscService;

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
		MSCControleService mscService = new MSCControleService();
		assertNotNull(mscService);
	}

	public void iniciarDbUnit() {
		dbUnitHelper = new DbUnitHelper(mscService.getEntityManager(), "DbUnitXml");
		dbUnitHelper.execute(DatabaseOperation.DELETE_ALL, "MSCControle.xml");
		dbUnitHelper.execute(DatabaseOperation.INSERT, "MSCControle.xml");
	}

	@Test
	public void testeCarregarDadosArquivoSemNomeTipoValorComClasses() {
		List<Integer> listaClassesConta = Arrays.asList(7 , 8);
		OpcoesCargaDadosMSC opcoes = new OpcoesCargaDadosMSC.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.ARQUIVO)
				.listaClasseConta(listaClassesConta).build();
		MatrizSaldoContabeisControle mscc = obterMSCControle();
		List<Ente> listaEntes = Arrays.asList(new Ente("21","E"), new Ente("22","E"), new Ente("23","E"));
		when(enteService.obterListaEntesNaAPI(opcoes)).thenReturn(listaEntes);
		when(consultaApiUtil.lerEntidades(any(), eq(MatrizSaldoContabeisControle.class)))
				.thenReturn(Arrays.asList(mscc));

		mscService.carregarDados(opcoes);
		verify(enteService).obterListaEntesNaAPI(opcoes);
		verify(consultaApiUtil, times(listaClassesConta.size() * listaEntes.size() * TipoValorMatrizSaldoContabeis.values().length * Constantes.MESES.size()))
				.lerEntidades(any(), eq(MatrizSaldoContabeisControle.class));

		try {
			verify(csvUtil).writeHeader(COLUNAS_ARQUIVO_CSV, NOME_PADRAO_ARQUIVO_CSV);
			verify(csvUtil, times(listaClassesConta.size() * listaEntes.size() * TipoValorMatrizSaldoContabeis.values().length * Constantes.BIMESTRES.size()))
					.writeToFile(Arrays.asList(mscc), COLUNAS_ARQUIVO_CSV, NOME_PADRAO_ARQUIVO_CSV);
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
		MatrizSaldoContabeisControle mscc = obterMSCControle();
		List<Ente> listaCodigoIbge = Arrays.asList(new Ente("22","E"), new Ente("23","E"));
		when(enteService.obterListaEntesNaAPI(opcoes)).thenReturn(listaCodigoIbge);
		when(consultaApiUtil.lerEntidades(any(), eq(MatrizSaldoContabeisControle.class)))
				.thenReturn(Arrays.asList(mscc));

		mscService.carregarDados(opcoes);
		verify(enteService).obterListaEntesNaAPI(opcoes);
		verify(consultaApiUtil,
				times(Constantes.CLASSES_CONTAS_CONTROLE.size() * listaTipoValor.size() * listaCodigoIbge.size() * Constantes.BIMESTRES.size()))
						.lerEntidades(any(), eq(MatrizSaldoContabeisControle.class));

		try {
			verify(csvUtil).writeHeader(COLUNAS_ARQUIVO_CSV, "relatorio.csv");
			verify(csvUtil,
					times(Constantes.CLASSES_CONTAS_CONTROLE.size() * listaTipoValor.size() * listaCodigoIbge.size() * Constantes.BIMESTRES.size()))
							.writeToFile(Arrays.asList(mscc), COLUNAS_ARQUIVO_CSV, "relatorio.csv");
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

		when(enteService.obterListaEntesNaAPI(any())).thenReturn(Arrays.asList(new Ente("23","E")));
		when(consultaApiUtil.lerEntidades(any(), eq(MatrizSaldoContabeisControle.class))).thenReturn(Arrays.asList(obterMSCControle()));

		mscService.carregarDados(opcoes);
		
		verify(enteService).obterListaEntesNaAPI(opcoes);
		verify(consultaApiUtil, times(Constantes.CLASSES_CONTAS_CONTROLE.size() * TipoValorMatrizSaldoContabeis.values().length)).lerEntidades(any(), eq(MatrizSaldoContabeisControle.class));
		
		verify(logger, times(Constantes.CLASSES_CONTAS_CONTROLE.size() * TipoValorMatrizSaldoContabeis.values().length)).info("Excluindo dados do banco de dados...");
		verify(logger, times(Constantes.CLASSES_CONTAS_CONTROLE.size() * TipoValorMatrizSaldoContabeis.values().length)).info("Fazendo commit...");
	}

	private MatrizSaldoContabeisControle obterMSCControle() {
		MatrizSaldoContabeisControle mscc = new MatrizSaldoContabeisControle();
		mscc.setExercicio(2019);
		mscc.setMesReferencia(1);
		mscc.setCodigoIbge("23");
		mscc.setTipoMatriz(TipoMatrizSaldoContabeis.MSCC.getCodigo());
		mscc.setTipoValor(TipoValorMatrizSaldoContabeis.period_change.getCodigo());
		mscc.setValor(10.1);
		return mscc;
	}
	
	private Logger mockLogger() {
		Logger logger = mock(Logger.class);
		mockStatic(LoggerUtil.class);
		when(LoggerUtil.createLogger(any())).thenReturn(logger);
		return logger;
	}

}
