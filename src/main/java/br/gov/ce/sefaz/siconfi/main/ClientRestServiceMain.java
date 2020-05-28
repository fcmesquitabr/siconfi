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
			logger.info("Carregando dados dos Entes da Federação...");
			carregarEntes();
			break;
		case extrato_entrega:
			logger.info("Carregando dados dos Extratos de Entregas dos Relatórios...");
			carregarExtratosEntregas();
			break;
		case rreo:
			logger.info("Carregando dados de Relatórios RREO...");
			carregarDadosRREO();
			break;
		case rgf:
			logger.info("Carregando dados de Relatórios RGF...");			
			carregarDadosRGF();
			break;
		case dca:
			logger.info("Carregando dados do relatório DCA...");
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
		System.out.println("Escolha os seguintes valores para as opções do programa:");
		System.out.println("");
		System.out.println("Opção -Drelatorio (Obrigatório) ");
		System.out.println("\t Valores possíveis: anexo_relatorio, ente, extrato_entrega, rreo, rgf, dca. Exemplo: -Drelatorio=rreo");
		System.out.println("Opção -DsaidaDados (Opcional. Valor padrão ARQUIVO) ");
		System.out.println("\t Valores possíveis: BANCO, ARQUIVO, CONSOLE");
		System.out.println("Opção -DcaminhoArquivo (Opcional. Válido quando a opção de saída de dados é ARQUIVO) ");
		System.out.println("\t Exemplo: -DcaminhoArquivo=\"C:\\nome_do_arquivo.csv\"");		
		System.out.println("Opção -Dexercicios (Opcional. Valor padrão " + SiconfiService.EXERCICIOS_DISPONIVEIS + ") ");
		System.out.println("\t Valores possíveis: lista de exercício separados por vírgula. Exemplo: -Dexercicios=2020,2019");
		System.out.println("Opção -Dperiodos (Opcional. Usada apenas para RREO e DCA. Valor Padrão para RREO: "
				+ SiconfiService.BIMESTRES + ". Valor Padrão para RGF: " + SiconfiService.QUADRIMESTRES + ") ");
		System.out.println("\t Valores possíveis: lista de periodos (bimestres, quadrimestres) separados por vírgula. Exemplo: -Dperiodos=1,2,3");
		System.out.println("Opção -Desfera (Opcional). Caso seja utilizada a opção -Dente, essa opção é desconsiderada");
		System.out.println("\t Valores possíveis: Esfera dos entes da Federação. Valores: U - União, E - Estados, D - Distrito Federal, "
				+ "ED - Estados e Distrito Federal, M - Municípios. Exemplo: -Desfera=E");
		System.out.println("Opção -Dentes (Opcional)");
		System.out.println("\t Valores possíveis: lista de códigos ibge dos entes (Pode ser obtido com a opção -Drelatorio=entes) seprados por vírgula. Exemplo (Piauí, Ceará): -Dentes=22,23");
		System.out.println("Opção -Dpoder (Opcional. Válido para RGF. Caso não especificado, serão consultados todos os poderes)");
		System.out.println("\t Valores possíveis: lista de poderes separados por vírgula. Valores E - Executivo, L - Legislativo, "
				+ "J - Juciário, M - Ministério Público, D - Defensoria Pública. Exemplo (Executivo, Judiciário): -Dpoder=E,J");
		System.out.println("Opção -Danexos (Opcional. Caso não especificado, serão consultados todos os anexos) ");
		System.out.println("\t Valores possíveis: lista de anexos separados por vírgula e ENTRE ASPAS.");
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
