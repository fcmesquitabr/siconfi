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

import br.gov.ce.sefaz.siconfi.entity.Ente;
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
			"codigoIbge", "codigoPoder", "instituicao", "anexo", "codigoConta", "descricaoConta", "coluna", "rotulo", "populacao",
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
		List<Ente> listaEntes = obterListaEntes();
		when(enteService.obterListaEntesNaAPI(opcoes)).thenReturn(listaEntes);
		when(consultaApiUtil.lerEntidades(any(), eq(RelatorioGestaoFiscal.class))).thenReturn(Arrays.asList(rgf));

		rgfService.carregarDados(opcoes);
		verify(enteService).obterListaEntesNaAPI(opcoes);
		verify(consultaApiUtil, times(listaAnexos.size()*listaEntes.size()*Poder.values().length)).lerEntidades(any(), eq(RelatorioGestaoFiscal.class));
		
		try {
			verify(csvUtil).writeHeader(COLUNAS_ARQUIVO_CSV, NOME_PADRAO_ARQUIVO_CSV);
			verify(csvUtil,times(listaAnexos.size()*listaEntes.size()*Poder.values().length)).writeToFile(Arrays.asList(rgf), COLUNAS_ARQUIVO_CSV, NOME_PADRAO_ARQUIVO_CSV);
		} catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
			e.printStackTrace();
		}
	}

	private List<Ente> obterListaEntes(){
		List<Ente> listaEntes =  new ArrayList<>();
		listaEntes.add(new Ente("21","E"));
		listaEntes.add(new Ente("22","E"));
		listaEntes.add(new Ente("23","E"));		
		return listaEntes;
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
		List<Ente> listaEntes = obterListaEntes();
		when(enteService.obterListaEntesNaAPI(opcoes)).thenReturn(listaEntes);
		when(consultaApiUtil.lerEntidades(any(), eq(RelatorioGestaoFiscal.class))).thenReturn(Arrays.asList(rgf));

		rgfService.carregarDados(opcoes);
		verify(enteService).obterListaEntesNaAPI(opcoes);
		verify(consultaApiUtil, times(listaEntes.size()*listaPoderes.size())).lerEntidades(any(), eq(RelatorioGestaoFiscal.class));
		
		try {
			verify(csvUtil).writeHeader(COLUNAS_ARQUIVO_CSV, "relatorio.csv");
			verify(csvUtil,times(listaEntes.size()*listaPoderes.size())).writeToFile(Arrays.asList(rgf), COLUNAS_ARQUIVO_CSV, "relatorio.csv");
		} catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testeExcluir() {
		OpcoesCargaDadosRGF opcoes = new OpcoesCargaDadosRGF.Builder()
				.opcaoSalvamentoDados(OpcaoSalvamentoDados.BANCO)
				.exercicios(Arrays.asList(2020))
				.periodos(Arrays.asList(1))
				.build();
		iniciarDbUnit();
		assertEquals(40, rgfService.excluir(opcoes));
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

		when(enteService.obterListaEntesNaAPI(any())).thenReturn(Arrays.asList(new Ente("23","E")));
		when(consultaApiUtil.lerEntidades(any(), eq(RelatorioGestaoFiscal.class))).thenReturn(Arrays.asList(obterRelatorioGestaoFiscal()));

		rgfService.carregarDados(opcoes);
		
		verify(enteService).obterListaEntesNaAPI(opcoes);
		verify(enteService, times(Poder.values().length)).obterListaCodigosIbgeNaAPI(opcoes); //Método chamado 1 vez por Poder para cada exclusão
		verify(consultaApiUtil, times(Poder.values().length)).lerEntidades(any(), eq(RelatorioGestaoFiscal.class));
		
		verify(logger, times(Poder.values().length)).info("Excluindo dados do banco de dados...");
	}

	private RelatorioGestaoFiscal obterRelatorioGestaoFiscal() {
		RelatorioGestaoFiscal rgf = new RelatorioGestaoFiscal();
		rgf.setAnexo("RGF-Anexo 01");
		rgf.setCodigoPoder(Poder.EXECUTIVO.getCodigo());
		rgf.setCodigoIbge("23");
		rgf.setExercicio(2019);
		rgf.setCodigoConta("101");
		rgf.setDescricaoConta("conta");
		rgf.setPopulacao(1000000l);
		rgf.setInstituicao("Governo");
		rgf.setRotulo("padrão");
		rgf.setValor(123.4);
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
