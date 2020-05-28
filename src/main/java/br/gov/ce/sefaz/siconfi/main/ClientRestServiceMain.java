package br.gov.ce.sefaz.siconfi.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.gov.ce.sefaz.siconfi.service.AnexoRelatorioService;
import br.gov.ce.sefaz.siconfi.service.DCAService;
import br.gov.ce.sefaz.siconfi.service.EnteService;
import br.gov.ce.sefaz.siconfi.service.ExtratoEntregaService;
import br.gov.ce.sefaz.siconfi.service.RGFService;
import br.gov.ce.sefaz.siconfi.service.RREOService;
import br.gov.ce.sefaz.siconfi.service.SiconfiService;
import br.gov.ce.sefaz.siconfi.util.FiltroDCA;
import br.gov.ce.sefaz.siconfi.util.FiltroExtratoEntrega;
import br.gov.ce.sefaz.siconfi.util.FiltroRGF;
import br.gov.ce.sefaz.siconfi.util.FiltroRREO;
import br.gov.ce.sefaz.siconfi.util.LeitorParametrosPrograma;

public class ClientRestServiceMain {

	private static final Logger logger = LogManager.getLogger(ClientRestServiceMain.class);
	
	public static void main(String[] args) {

		try {
			LeitorParametrosPrograma.lerArgumentos();
		} catch(IllegalArgumentException iae) {
			logger.error(iae.getMessage());
			exibirMensagemAjuda();
			return;
		}
		
		switch (LeitorParametrosPrograma.getRelatorioSelecionado()) {
		case anexo_relatorio:
			logger.info("Carregando dados dos anexos de relatorios...");
			carregarAnexosRelatorios();
			break;
		case ente:
			logger.info("Carregando dados dos Entes da Federa��o...");
			carregarEntes();
			break;
		case extrato_entrega:
			logger.info("Carregando dados dos Extratos de Entregas dos Relat�rios...");
			carregarExtratosEntregas();
			break;
		case rreo:
			logger.info("Carregando dados de Relat�rios RREO...");
			carregarDadosRREO();
			break;
		case rgf:
			logger.info("Carregando dados de Relat�rios RGF...");			
			carregarDadosRGF();
			break;
		case dca:
			logger.info("Carregando dados do relat�rio DCA...");
			carregarDadosDCA();
			break;
		default:
			exibirMensagemAjuda();
			break;
		}	
		logger.info("Fim.");
		System.exit(0);
	}

	private static void carregarAnexosRelatorios() {
		AnexoRelatorioService anexoRelatorioService = new AnexoRelatorioService();
		anexoRelatorioService.carregarDados(LeitorParametrosPrograma.getOpcaoSalvamentoSelecionada(),
				LeitorParametrosPrograma.getOpcaoCaminhoArquivoSelecionado());
	}

	private static void carregarEntes() {
		EnteService enteService = new EnteService();
		enteService.carregarDados(LeitorParametrosPrograma.getOpcaoSalvamentoSelecionada(),
				LeitorParametrosPrograma.getOpcaoCaminhoArquivoSelecionado());
	}

	private static void carregarExtratosEntregas() {
		
		FiltroExtratoEntrega filtroExtratoEntrega = new FiltroExtratoEntrega();
		filtroExtratoEntrega.setOpcaoSalvamento(LeitorParametrosPrograma.getOpcaoSalvamentoSelecionada());
		filtroExtratoEntrega.setNomeArquivo(LeitorParametrosPrograma.getOpcaoCaminhoArquivoSelecionado());
		filtroExtratoEntrega.setEsfera(LeitorParametrosPrograma.getOpcaoEsferaSelecionada());
		filtroExtratoEntrega.setExercicios(LeitorParametrosPrograma.getOpcaoExerciciosSelecionados());
		filtroExtratoEntrega.setCodigosIBGE(LeitorParametrosPrograma.getOpcaoCodigosIbgeSelecionados());
		
		ExtratoEntregaService extratoEntregaService = new ExtratoEntregaService();
		extratoEntregaService.carregarDados(filtroExtratoEntrega);
	}
	
	private static void carregarDadosRREO() {

		FiltroRREO filtro = new FiltroRREO();
		filtro.setOpcaoSalvamento(LeitorParametrosPrograma.getOpcaoSalvamentoSelecionada());
		filtro.setNomeArquivo(LeitorParametrosPrograma.getOpcaoCaminhoArquivoSelecionado());
		filtro.setEsfera(LeitorParametrosPrograma.getOpcaoEsferaSelecionada());
		filtro.setExercicios(LeitorParametrosPrograma.getOpcaoExerciciosSelecionados());
		filtro.setSemestres(LeitorParametrosPrograma.getOpcaoPeriodosSelecionados());
		filtro.setCodigosIBGE(LeitorParametrosPrograma.getOpcaoCodigosIbgeSelecionados());		
		filtro.setListaAnexos(LeitorParametrosPrograma.getOpcaoAnexosSelecionados());		

		RREOService rreoService = new RREOService();
		rreoService.carregarDados(filtro);		
	}
	
	private static void carregarDadosRGF() {
		
		FiltroRGF filtro = new FiltroRGF();
		filtro.setOpcaoSalvamento(LeitorParametrosPrograma.getOpcaoSalvamentoSelecionada());
		filtro.setNomeArquivo(LeitorParametrosPrograma.getOpcaoCaminhoArquivoSelecionado());
		filtro.setEsfera(LeitorParametrosPrograma.getOpcaoEsferaSelecionada());
		filtro.setListaPoderes(LeitorParametrosPrograma.getOpcaoPoderesSelecionados());
		filtro.setExercicios(LeitorParametrosPrograma.getOpcaoExerciciosSelecionados());
		filtro.setQuadrimestres(LeitorParametrosPrograma.getOpcaoPeriodosSelecionados());
		filtro.setCodigosIBGE(LeitorParametrosPrograma.getOpcaoCodigosIbgeSelecionados());		
		filtro.setListaAnexos(LeitorParametrosPrograma.getOpcaoAnexosSelecionados());		
		
		RGFService rgfService = new RGFService();
		rgfService.carregarDados(filtro);		
	}
	
	private static void carregarDadosDCA() {
		
		FiltroDCA filtro = new FiltroDCA();
		filtro.setOpcaoSalvamento(LeitorParametrosPrograma.getOpcaoSalvamentoSelecionada());
		filtro.setNomeArquivo(LeitorParametrosPrograma.getOpcaoCaminhoArquivoSelecionado());
		filtro.setEsfera(LeitorParametrosPrograma.getOpcaoEsferaSelecionada());
		filtro.setExercicios(LeitorParametrosPrograma.getOpcaoExerciciosSelecionados());
		filtro.setCodigosIBGE(LeitorParametrosPrograma.getOpcaoCodigosIbgeSelecionados());		
		filtro.setListaAnexos(LeitorParametrosPrograma.getOpcaoAnexosSelecionados());		
		
		DCAService dcaService = new DCAService();
		dcaService.carregarDados(filtro);		
	}

	private static void exibirMensagemAjuda() {
		System.out.println("Escolha os seguintes valores para as op��es do programa:");
		System.out.println("");
		System.out.println("Op��o -Drelatorio (Obrigat�rio) ");
		System.out.println("\t Valores poss�veis: anexo_relatorio, ente, extrato_entrega, rreo, rgf, dca. Exemplo: -Drelatorio=rreo");
		System.out.println("Op��o -DsaidaDados (Opcional. Valor padr�o ARQUIVO) ");
		System.out.println("\t Valores poss�veis: BANCO, ARQUIVO, CONSOLE");
		System.out.println("Op��o -DcaminhoArquivo (Opcional. V�lido quando a op��o de sa�da de dados � ARQUIVO) ");
		System.out.println("\t Exemplo: -DcaminhoArquivo=\"C:\\nome_do_arquivo.csv\"");		
		System.out.println("Op��o -Dexercicios (Opcional. Valor padr�o " + SiconfiService.EXERCICIOS_DISPONIVEIS + ") ");
		System.out.println("\t Valores poss�veis: lista de exerc�cio separados por v�rgula. Exemplo: -Dexercicios=2020,2019");
		System.out.println("Op��o -Dperiodos (Opcional. Usada apenas para RREO e DCA. Valor Padr�o para RREO: "
				+ SiconfiService.BIMESTRES + ". Valor Padr�o para RGF: " + SiconfiService.QUADRIMESTRES + ") ");
		System.out.println("\t Valores poss�veis: lista de periodos (bimestres, quadrimestres) separados por v�rgula. Exemplo: -Dperiodos=1,2,3");
		System.out.println("Op��o -Desfera (Opcional). Caso seja utilizada a op��o -Dente, essa op��o � desconsiderada");
		System.out.println("\t Valores poss�veis: Esfera dos entes da Federa��o. Valores: U - Uni�o, E - Estados, D - Distrito Federal, "
				+ "ED - Estados e Distrito Federal, M - Munic�pios. Exemplo: -Desfera=E");
		System.out.println("Op��o -Dentes (Opcional)");
		System.out.println("\t Valores poss�veis: lista de c�digos ibge dos entes (Pode ser obtido com a op��o -Drelatorio=entes) seprados por v�rgula. Exemplo (Piau�, Cear�): -Dentes=22,23");
		System.out.println("Op��o -Dpoder (Opcional. V�lido para RGF. Caso n�o especificado, ser�o consultados todos os poderes)");
		System.out.println("\t Valores poss�veis: lista de poderes separados por v�rgula. Valores E - Executivo, L - Legislativo, "
				+ "J - Juci�rio, M - Minist�rio P�blico, D - Defensoria P�blica. Exemplo (Executivo, Judici�rio): -Dpoder=E,J");
		System.out.println("Op��o -Danexos (Opcional. Caso n�o especificado, ser�o consultados todos os anexos) ");
		System.out.println("\t Valores poss�veis: lista de anexos separados por v�rgula e ENTRE ASPAS.");
		System.out.println("\t\t Para RREO: RREO-Anexo 01, RREO-Anexo 02, RREO-Anexo 03, \n" + 
				"\t\t\t RREO-Anexo 04, RREO-Anexo 04 - RGPS, RREO-Anexo 04 - RPPS, RREO-Anexo 04.0 - RGPS,\n" + 
				"\t\t\t RREO-Anexo 04.1, RREO-Anexo 04.2, RREO-Anexo 04.3 - RGPS, RREO-Anexo 05, RREO-Anexo 06,\n" + 
				"\t\t\t RREO-Anexo 07, RREO-Anexo 09, RREO-Anexo 10 - RGPS, RREO-Anexo 10 - RPPS, RREO-Anexo 11,\n" + 
				"\t\t\t RREO-Anexo 13, RREO-Anexo 14");
		System.out.println("\t\t Para RGF: RGF-Anexo 01, RGF-Anexo 02, RGF-Anexo 03,\n" + 
				"\t\t\t RGF-Anexo 04, RGF-Anexo 05, RGF-Anexo 06");
		System.out.println("\t\t Para DCA: Anexo I-AB, Anexo I-C, Anexo I-D, Anexo I-E,\n " + 
				"\t\t\t  Anexo I-F, Anexo I-G, Anexo I-HI, DCA-Anexo I-AB, DCA-Anexo I-C, DCA-Anexo I-D,\n " + 
				"\t\t\t DCA-Anexo I-E, DCA-Anexo I-F, DCA-Anexo I-G, DCA-Anexo I-HI");
		System.out.println("\t\tPor exmplo: -Danexos=\"RGF-Anexo 01, RGF-Anexo 02\"");
	}
}
