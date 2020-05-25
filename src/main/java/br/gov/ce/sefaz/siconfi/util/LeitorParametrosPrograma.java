package br.gov.ce.sefaz.siconfi.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.gov.ce.sefaz.siconfi.enums.Esfera;
import br.gov.ce.sefaz.siconfi.enums.OpcaoSalvamentoDados;
import br.gov.ce.sefaz.siconfi.enums.Poder;
import br.gov.ce.sefaz.siconfi.enums.Relatorio;
import br.gov.ce.sefaz.siconfi.service.DCAService;
import br.gov.ce.sefaz.siconfi.service.RGFService;
import br.gov.ce.sefaz.siconfi.service.RREOService;
import br.gov.ce.sefaz.siconfi.service.SiconfiService;

public class LeitorParametrosPrograma {

	private static final Logger logger = LogManager.getLogger(LeitorParametrosPrograma.class);
	
	public static final String OPCAO_RELATORIO = "relatorio";
	public static final String OPCAO_SALVAMENTO = "saidaDados";
	public static final String OPCAO_ESFERA = "esfera";
	public static final String OPCAO_PODER = "poder";
	public static final String OPCAO_EXERCICIOS = "exercicios";
	public static final String OPCAO_PERIODOS = "periodos";
	public static final String OPCAO_CODIGOS_IBGE = "entes";
	public static final String OPCAO_ANEXOS = "anexos";
	public static final String OPCAO_CAMINHO_ARQUIVO = "caminhoArquivo";
	
	private static Relatorio relatorioSelecionado;
	private static OpcaoSalvamentoDados opcaoSalvamentoSelecionada;
	private static String opcaoCaminhoArquivoSelecionado;
	private static Esfera opcaoEsferaSelecionada;
	private static List<Integer> opcaoExerciciosSelecionados;
	private static List<Integer> opcaoPeriodosSelecionados;
	private static List<String> opcaoCodigosIbgeSelecionados;
	private static List<String> opcaoAnexosSelecionados;
	private static List<Poder> opcaoPoderesSelecionados;

	public static void lerArgumentos() throws IllegalArgumentException {
		logger.info("Lendo as opções seleciondas...");
		lerArgumentoOpcaoRelatorio();
		lerArgumentoOpcaoSalvamento();
		lerArgumentoOpcaoCaminhoArquivo();
		lerArgumentoOpcaoEsfera();
		lerArgumentoOpcaoExercicios();
		lerArgumentoOpcaoPeriodos();
		lerArgumentoOpcaoPoder();		
		lerArgumentoOpcaoCodigosIBGE();
		lerArgumentoOpcaoAnexos();
	}
	
	private static void lerArgumentoOpcaoRelatorio() {
		String opcaoRelatorio = System.getProperty(OPCAO_RELATORIO);
		if(opcaoRelatorio == null) {
			throw new IllegalArgumentException("Opção de Relatório não escolhida");
		}
		try {
			relatorioSelecionado = Relatorio.valueOf(opcaoRelatorio.trim());
			logger.info("Opção de Relatório Selecionado:" + relatorioSelecionado);
		} catch(IllegalArgumentException iae) {
			throw new IllegalArgumentException("Opção de Relatório inválida");
		}
	}
	
	private static void lerArgumentoOpcaoSalvamento() {		
		try {
			String opcaoSalvamento = System.getProperty(OPCAO_SALVAMENTO);

			opcaoSalvamentoSelecionada = (opcaoSalvamento == null)
					? opcaoSalvamentoSelecionada = OpcaoSalvamentoDados.ARQUIVO
					: OpcaoSalvamentoDados.valueOf(opcaoSalvamento.trim());

			logger.info("Opção de saída de dados selecionada:" + opcaoSalvamentoSelecionada);
		} catch(IllegalArgumentException iae) {
			throw new IllegalArgumentException("Opção de Saída de dados inválida");
		}		
	}

	private static void lerArgumentoOpcaoCaminhoArquivo() {
		opcaoCaminhoArquivoSelecionado = System.getProperty(OPCAO_CAMINHO_ARQUIVO);
		logger.info("Opção de caminho de arquivo selecionado:" + opcaoCaminhoArquivoSelecionado);
	}

	private static void lerArgumentoOpcaoEsfera() {
		String opcaoEsfera = System.getProperty(OPCAO_ESFERA);
		if (opcaoEsfera == null) {
			opcaoEsferaSelecionada = Esfera.ESTADOS_E_DISTRITO_FEDERAL;
		} else {
			opcaoEsferaSelecionada = Esfera.getEsfera(opcaoEsfera.trim());			
			if (opcaoEsferaSelecionada == null) {
				throw new IllegalArgumentException("Opção de Esfera inválida");
			}
		}
		logger.info("Opção de Esfera selecionada:" + opcaoEsfera);
	}

	private static void lerArgumentoOpcaoPoder() {
		String opcao = System.getProperty(OPCAO_PODER);
		if (opcao == null) {
			opcaoPoderesSelecionados = Arrays.asList(Poder.EXECUTIVO);
		} else {
			String[] poderes = opcao.split(",");
			opcaoPoderesSelecionados = new ArrayList<Poder>();
			for (String codigoPoder: poderes) {
				Poder poder = Poder.getPoder(codigoPoder.trim());
				if(poder!=null) {
					opcaoPoderesSelecionados.add(poder);
				}
			}
			if (opcaoPoderesSelecionados == null || opcaoPoderesSelecionados.isEmpty()) {
				throw new IllegalArgumentException("Opção de Poder inválida");
			}
		}
		logger.info("Opções de Poderes selecionados:" + opcao);
	}

	private static void lerArgumentoOpcaoExercicios() {
		String opcao = System.getProperty(OPCAO_EXERCICIOS);
		try {
			if (opcao == null || opcao.trim().isEmpty()) {
				opcaoExerciciosSelecionados = SiconfiService.EXERCICIOS_DISPONIVEIS;
			} else {
				String[] exercicios = opcao.split(",");
				opcaoExerciciosSelecionados = new ArrayList<Integer>();
				for (String exercicio: exercicios) {
					opcaoExerciciosSelecionados.add(Integer.valueOf(exercicio.trim()));
				}
				if (opcaoExerciciosSelecionados == null || opcaoExerciciosSelecionados.isEmpty()) {
					throw new IllegalArgumentException("Opção de Exercício inválida");
				}			
			}
			logger.info("Opções de Exercícios selecionados:" + opcao);			
		} catch (Exception e) {
			logger.error("Erro ao ler parâmetro exercício");
			logger.error(e);
			throw new IllegalArgumentException("Opção de Exercício inválida");
		}
	}

	private static void lerArgumentoOpcaoPeriodos() {
		String opcao = System.getProperty(OPCAO_PERIODOS);
		
		try {
			
			if (opcao == null || opcao.trim().isEmpty()) {
				opcaoPeriodosSelecionados = (relatorioSelecionado.equals(Relatorio.rreo)) ? SiconfiService.BIMESTRES
						: (relatorioSelecionado.equals(Relatorio.rgf)) ? SiconfiService.QUADRIMESTRES : null;
			} else {
				String[] periodos = opcao.split(",");
				opcaoPeriodosSelecionados = new ArrayList<Integer>();
				for (String periodo: periodos) {
					opcaoPeriodosSelecionados.add(Integer.valueOf(periodo.trim()));
				}
				if (opcaoPeriodosSelecionados == null || opcaoPeriodosSelecionados.isEmpty()) {
					throw new IllegalArgumentException("Opção de Exercício inválida");
				}			
			}
			
			logger.info("Opção de Períodos selecionados:" + opcao);			
		} catch (Exception e) {
			logger.error("Erro ao ler parâmetro período");
			logger.error(e);
			throw new IllegalArgumentException("Opção de Exercício inválida");
		}
	}

	private static void lerArgumentoOpcaoCodigosIBGE() {
		String opcao = System.getProperty(OPCAO_CODIGOS_IBGE);
		try {
			if (opcao != null && !opcao.trim().isEmpty()) {
				String[] codigosIbge = opcao.split(",");
				opcaoCodigosIbgeSelecionados = Arrays.asList(codigosIbge);
			} 
			logger.info("Opção de Códigos IBGE selecionados:" + opcao);			
		} catch (Exception e) {
			logger.error("Erro ao ler parâmetro Códigos IBGE");
			logger.error(e);
			throw new IllegalArgumentException("Opção de Códigos IBGE inválida");
		}
	}

	private static void lerArgumentoOpcaoAnexos() {
		String opcao = System.getProperty(OPCAO_ANEXOS);
		try {
			if (opcao != null && !opcao.trim().isEmpty()) {
				String[] anexos = opcao.split(",");
				opcaoAnexosSelecionados = Arrays.asList(anexos);
				if(opcaoAnexosSelecionados == null || opcaoAnexosSelecionados.isEmpty()) {
					throw new IllegalArgumentException("Opção de Anexos inválida");
				}
			} else {
				opcaoAnexosSelecionados = (relatorioSelecionado.equals(Relatorio.rreo)) ? RREOService.ANEXOS_RREO
						: (relatorioSelecionado.equals(Relatorio.rgf)) ? RGFService.ANEXOS_RGF
								: (relatorioSelecionado.equals(Relatorio.dca)) ? DCAService.ANEXOS_DCA : null;
			}
			logger.info("Opção de Anexos selecionados:" + opcao);						
		} catch (Exception e) {
			logger.error("Erro ao ler parâmetro Anexos");
			logger.error(e);
			throw new IllegalArgumentException("Opção de Anexos inválida");
		}
	}

	public static Relatorio getRelatorioSelecionado() {
		return relatorioSelecionado;
	}

	public static OpcaoSalvamentoDados getOpcaoSalvamentoSelecionada() {
		return opcaoSalvamentoSelecionada;
	}
	
	public static String getOpcaoCaminhoArquivoSelecionado() {
		return opcaoCaminhoArquivoSelecionado;
	}

	public static Esfera getOpcaoEsferaSelecionada() {
		return opcaoEsferaSelecionada;
	}

	public static List<Integer> getOpcaoExerciciosSelecionados() {
		return opcaoExerciciosSelecionados;
	}

	public static List<Integer> getOpcaoPeriodosSelecionados() {
		return opcaoPeriodosSelecionados;
	}

	public static List<String> getOpcaoCodigosIbgeSelecionados() {
		return opcaoCodigosIbgeSelecionados;
	}

	public static List<String> getOpcaoAnexosSelecionados() {
		return opcaoAnexosSelecionados;
	}

	public static List<Poder> getOpcaoPoderesSelecionados() {
		return opcaoPoderesSelecionados;
	}	
}
