package br.gov.ce.sefaz.siconfi.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.gov.ce.sefaz.siconfi.service.AnexoRelatorioService;
import br.gov.ce.sefaz.siconfi.service.DCAService;
import br.gov.ce.sefaz.siconfi.service.EnteService;
import br.gov.ce.sefaz.siconfi.service.ExtratoEntregaService;
import br.gov.ce.sefaz.siconfi.service.MSCControleService;
import br.gov.ce.sefaz.siconfi.service.MSCOrcamentariaService;
import br.gov.ce.sefaz.siconfi.service.MSCService;
import br.gov.ce.sefaz.siconfi.service.RGFService;
import br.gov.ce.sefaz.siconfi.service.RREOService;
import br.gov.ce.sefaz.siconfi.service.SiconfiService;
import br.gov.ce.sefaz.siconfi.util.FiltroAnexoRelatorio;
import br.gov.ce.sefaz.siconfi.util.FiltroDCA;
import br.gov.ce.sefaz.siconfi.util.FiltroEnte;
import br.gov.ce.sefaz.siconfi.util.FiltroExtratoEntrega;
import br.gov.ce.sefaz.siconfi.util.FiltroMSC;
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
		case msc_patrimonial:
			logger.info("Carregando dados do relatório MSC Patrimonial...");
			carregarDadosMSCPatrimonial();
			break;
		case msc_orcamentaria:
			logger.info("Carregando dados do relatório MSC Orcamentaria...");
			carregarDadosMSCOrcamentaria();
			break;
		case msc_controle:
			logger.info("Carregando dados do relatório MSC Controle...");
			carregarDadosMSCControle();
			break;
		default:
			exibirMensagemAjuda();
			break;
		}	
		logger.info("Fim.");
		System.exit(0);
	}

	private static void carregarEntes() {

		FiltroEnte filtroEnte = new FiltroEnte.Builder()
				.opcaoSalvamentoDados(LeitorParametrosPrograma.getOpcaoSalvamentoSelecionada())
				.nomeArquivo(LeitorParametrosPrograma.getOpcaoCaminhoArquivoSelecionado())
				.build();

		EnteService enteService = new EnteService();
		enteService.carregarDados(filtroEnte);
	}

	private static void carregarAnexosRelatorios() {
		
		FiltroAnexoRelatorio filtroAnexoRelatorio = new FiltroAnexoRelatorio.Builder()
				.opcaoSalvamentoDados(LeitorParametrosPrograma.getOpcaoSalvamentoSelecionada())
				.nomeArquivo(LeitorParametrosPrograma.getOpcaoCaminhoArquivoSelecionado())
				.build();
		AnexoRelatorioService anexoRelatorioService = new AnexoRelatorioService();		
		anexoRelatorioService.carregarDados(filtroAnexoRelatorio);
	}

	private static void carregarExtratosEntregas() {
		
		FiltroExtratoEntrega filtroExtratoEntrega = new FiltroExtratoEntrega.Builder()
				.opcaoSalvamentoDados(LeitorParametrosPrograma.getOpcaoSalvamentoSelecionada())
				.nomeArquivo(LeitorParametrosPrograma.getOpcaoCaminhoArquivoSelecionado())
				.esfera(LeitorParametrosPrograma.getOpcaoEsferaSelecionada())
				.exercicios(LeitorParametrosPrograma.getOpcaoExerciciosSelecionados())
				.codigosIbge(LeitorParametrosPrograma.getOpcaoCodigosIbgeSelecionados())
				.build();
		
		ExtratoEntregaService extratoEntregaService = new ExtratoEntregaService();
		extratoEntregaService.carregarDados(filtroExtratoEntrega);
	}

	private static void carregarDadosDCA() {
		
		FiltroDCA filtro = new FiltroDCA.Builder()
				.opcaoSalvamentoDados(LeitorParametrosPrograma.getOpcaoSalvamentoSelecionada())
				.nomeArquivo(LeitorParametrosPrograma.getOpcaoCaminhoArquivoSelecionado())
				.esfera(LeitorParametrosPrograma.getOpcaoEsferaSelecionada())
				.exercicios(LeitorParametrosPrograma.getOpcaoExerciciosSelecionados())
				.codigosIbge(LeitorParametrosPrograma.getOpcaoCodigosIbgeSelecionados())
				.listaAnexos(LeitorParametrosPrograma.getOpcaoAnexosSelecionados())				
				.build();
		
		DCAService dcaService = new DCAService();
		dcaService.carregarDados(filtro);		
	}

	private static void carregarDadosRGF() {
		
		FiltroRGF filtro = new FiltroRGF.Builder()
				.opcaoSalvamentoDados(LeitorParametrosPrograma.getOpcaoSalvamentoSelecionada())
				.nomeArquivo(LeitorParametrosPrograma.getOpcaoCaminhoArquivoSelecionado())
				.esfera(LeitorParametrosPrograma.getOpcaoEsferaSelecionada())
				.listaPoderes(LeitorParametrosPrograma.getOpcaoPoderesSelecionados())
				.exercicios(LeitorParametrosPrograma.getOpcaoExerciciosSelecionados())
				.periodos(LeitorParametrosPrograma.getOpcaoPeriodosSelecionados())
				.codigosIbge(LeitorParametrosPrograma.getOpcaoCodigosIbgeSelecionados())
				.listaAnexos(LeitorParametrosPrograma.getOpcaoAnexosSelecionados())
				.build();		
		
		RGFService rgfService = new RGFService();
		rgfService.carregarDados(filtro);		
	}
	
	private static void carregarDadosRREO() {
		
		FiltroRREO filtro = new FiltroRREO.Builder()
				.opcaoSalvamentoDados(LeitorParametrosPrograma.getOpcaoSalvamentoSelecionada())
				.nomeArquivo(LeitorParametrosPrograma.getOpcaoCaminhoArquivoSelecionado())
				.esfera(LeitorParametrosPrograma.getOpcaoEsferaSelecionada())
				.exercicios(LeitorParametrosPrograma.getOpcaoExerciciosSelecionados())
				.periodos(LeitorParametrosPrograma.getOpcaoPeriodosSelecionados())
				.codigosIbge(LeitorParametrosPrograma.getOpcaoCodigosIbgeSelecionados())
				.listaAnexos(LeitorParametrosPrograma.getOpcaoAnexosSelecionados())
				.build();		
		
		RREOService rreoService = new RREOService();
		rreoService.carregarDados(filtro);		
	}
	
	private static void carregarDadosMSCPatrimonial() {		
		MSCService mscService = new MSCService();
		mscService.carregarDados(getFiltroMSC());		
	}

	private static void carregarDadosMSCOrcamentaria() {
		MSCOrcamentariaService mscService = new MSCOrcamentariaService();
		mscService.carregarDados(getFiltroMSC());		
	}

	private static void carregarDadosMSCControle() {
		MSCControleService mscService = new MSCControleService();
		mscService.carregarDados(getFiltroMSC());		
	}

	private static FiltroMSC getFiltroMSC() {
		FiltroMSC filtro = new FiltroMSC.Builder()
				.opcaoSalvamentoDados(LeitorParametrosPrograma.getOpcaoSalvamentoSelecionada())
				.nomeArquivo(LeitorParametrosPrograma.getOpcaoCaminhoArquivoSelecionado())
				.exercicios(LeitorParametrosPrograma.getOpcaoExerciciosSelecionados())
				.periodos(LeitorParametrosPrograma.getOpcaoPeriodosSelecionados())
				.codigosIbge(LeitorParametrosPrograma.getOpcaoCodigosIbgeSelecionados())
				.tipoMatrizSaldoContabeis(LeitorParametrosPrograma.getOpcaoTipoMatrizSelecionado())
				.listaClasseConta(LeitorParametrosPrograma.getOpcaoClassesContasSelecionadas())
				.listaTipoValor(LeitorParametrosPrograma.getOpcaoTiposValorMatrizSelecionado())
				.build();
		return filtro;
	}

	private static void exibirMensagemAjuda() {
		System.out.println("Escolha os seguintes valores para as opções do programa:");
		System.out.println("");
		System.out.println("Opção -Drelatorio (Obrigatório) ");
		System.out.println("\t Valores possíveis: anexo_relatorio, ente, extrato_entrega, rreo, rgf, dca, msc_patrimonial, msc_orcamentaria, msc_controle. Exemplo: -Drelatorio=rreo");
		System.out.println("Opção -DsaidaDados (Opcional. Valor padrão ARQUIVO) ");
		System.out.println("\t Valores possíveis: BANCO, ARQUIVO, CONSOLE");
		System.out.println("Opção -DcaminhoArquivo (Opcional. Considerada apenas quando a opção de saída de dados é ARQUIVO) ");
		System.out.println("\t Exemplo: -DcaminhoArquivo=\"C:\\nome_do_arquivo.csv\"");		
		System.out.println("Opção -Dexercicios (Opcional. Valor padrão: " + SiconfiService.EXERCICIOS_DISPONIVEIS + ") ");
		System.out.println("\t Valores possíveis: lista de exercício separados por vírgula. Exemplo: -Dexercicios=2020,2019");
		System.out.println("Opção -Dperiodos (Opcional. Considerada apenas para RREO, RGF e MSC. Valor Padrão para RREO: "
				+ SiconfiService.BIMESTRES + ". Valor Padrão para RGF: " + SiconfiService.QUADRIMESTRES + ". Valor Padrão para MSC: " + SiconfiService.MESES +") ");
		System.out.println("\t Valores possíveis: lista de periodos (bimestres, quadrimestres) separados por vírgula. Exemplo: -Dperiodos=1,2,3");
		System.out.println("Opção -Desfera (Opcional). Valor padrão: ED. Caso seja utilizada a opção -Dente, essa opção é desconsiderada");
		System.out.println("\t Valores possíveis: Esfera dos entes da Federação. Valores: U - União, E - Estados, D - Distrito Federal, "
				+ "ED - Estados e Distrito Federal, M - Municípios. Exemplo: -Desfera=E");
		System.out.println("Opção -Dentes (Opcional)");
		System.out.println("\t Valores possíveis: lista de códigos ibge dos entes (Pode ser obtido com a opção -Drelatorio=entes) separados por vírgula. Exemplo (Piauí, Ceará): -Dentes=22,23");
		System.out.println("Opção -Dpoder (Opcional. Válido para apenas RGF. Caso não especificado, serão consultados todos os poderes)");
		System.out.println("\t Valores possíveis: lista de poderes separados por vírgula. Valores E - Executivo, L - Legislativo, "
				+ "J - Juciário, M - Ministério Público, D - Defensoria Pública. Exemplo (Executivo, Judiciário): -Dpoder=E,J");
		System.out.println("Opção -DclassesConta (Classes da Conta Contábil. Opcional e válida apenas para os relatórios MSC. Caso não especificado, serão consultados todas as classes");
		System.out.println("\t Valores possíveis: lista de anexos separados por vírgula.");
		System.out.println("\t\t Para MSC Patrimonial: Classes 1, 2, 3 e 4");
		System.out.println("\t\t Para MSC Orçamentária: Classes 5 e 6");
		System.out.println("\t\t Para MSC Controle: Classes 7 e 8");
		System.out.println("\t Exemplo: -DclassesConta=1,3");
		System.out.println("Opção -DtipoMatriz (Tipo de Matriz. Opcional e válida apenas para os relatórios MSC. Valor padrão: MSCC) ");
		System.out.println("\t Valores possíveis: MSCC - Matriz de Saldo Contábeis Mensal ou Agregada e MSCE - Matriz de Saldo Contábeis de Encerramento do Exercício.");
		System.out.println("\t Exemplo: -DtipoMatriz=MSCC");
		System.out.println("Opção -DtiposValorMatriz (Tipo de Valor da Matriz. Opcional e válida apenas para os relatórios MSC. Caso não especificado, serão consultados todos os tipos de valores) ");
		System.out.println("\t Valores possíveis: beginning_balance, ending_balance e period_change.");
		System.out.println("\t Exemplo: -DtiposValorMatriz=ending_balance,period_change");
		System.out.println("Opção -Danexos (Opcional e válida apenas para os relatórios DCA, RGF e RREO Caso não especificada, serão consultados todos os anexos) ");
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
