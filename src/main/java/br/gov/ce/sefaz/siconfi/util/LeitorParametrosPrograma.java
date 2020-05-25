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
		logger.info("Lendo as op��es seleciondas...");
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
			throw new IllegalArgumentException("Op��o de Relat�rio n�o escolhida");
		}
		try {
			relatorioSelecionado = Relatorio.valueOf(opcaoRelatorio.trim());
			logger.info("Op��o de Relat�rio Selecionado:" + relatorioSelecionado);
		} catch(IllegalArgumentException iae) {
			throw new IllegalArgumentException("Op��o de Relat�rio inv�lida");
		}
	}
	
	private static void lerArgumentoOpcaoSalvamento() {		
		try {
			String opcaoSalvamento = System.getProperty(OPCAO_SALVAMENTO);

			opcaoSalvamentoSelecionada = (opcaoSalvamento == null)
					? opcaoSalvamentoSelecionada = OpcaoSalvamentoDados.ARQUIVO
					: OpcaoSalvamentoDados.valueOf(opcaoSalvamento.trim());

			logger.info("Op��o de sa�da de dados selecionada:" + opcaoSalvamentoSelecionada);
		} catch(IllegalArgumentException iae) {
			throw new IllegalArgumentException("Op��o de Sa�da de dados inv�lida");
		}		
	}

	private static void lerArgumentoOpcaoCaminhoArquivo() {
		opcaoCaminhoArquivoSelecionado = System.getProperty(OPCAO_CAMINHO_ARQUIVO);
		logger.info("Op��o de caminho de arquivo selecionado:" + opcaoCaminhoArquivoSelecionado);
	}

	private static void lerArgumentoOpcaoEsfera() {
		String opcaoEsfera = System.getProperty(OPCAO_ESFERA);
		if (opcaoEsfera == null) {
			opcaoEsferaSelecionada = Esfera.ESTADOS_E_DISTRITO_FEDERAL;
		} else {
			opcaoEsferaSelecionada = Esfera.getEsfera(opcaoEsfera.trim());			
			if (opcaoEsferaSelecionada == null) {
				throw new IllegalArgumentException("Op��o de Esfera inv�lida");
			}
		}
		logger.info("Op��o de Esfera selecionada:" + opcaoEsfera);
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
				throw new IllegalArgumentException("Op��o de Poder inv�lida");
			}
		}
		logger.info("Op��es de Poderes selecionados:" + opcao);
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
					throw new IllegalArgumentException("Op��o de Exerc�cio inv�lida");
				}			
			}
			logger.info("Op��es de Exerc�cios selecionados:" + opcao);			
		} catch (Exception e) {
			logger.error("Erro ao ler par�metro exerc�cio");
			logger.error(e);
			throw new IllegalArgumentException("Op��o de Exerc�cio inv�lida");
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
					throw new IllegalArgumentException("Op��o de Exerc�cio inv�lida");
				}			
			}
			
			logger.info("Op��o de Per�odos selecionados:" + opcao);			
		} catch (Exception e) {
			logger.error("Erro ao ler par�metro per�odo");
			logger.error(e);
			throw new IllegalArgumentException("Op��o de Exerc�cio inv�lida");
		}
	}

	private static void lerArgumentoOpcaoCodigosIBGE() {
		String opcao = System.getProperty(OPCAO_CODIGOS_IBGE);
		try {
			if (opcao != null && !opcao.trim().isEmpty()) {
				String[] codigosIbge = opcao.split(",");
				opcaoCodigosIbgeSelecionados = Arrays.asList(codigosIbge);
			} 
			logger.info("Op��o de C�digos IBGE selecionados:" + opcao);			
		} catch (Exception e) {
			logger.error("Erro ao ler par�metro C�digos IBGE");
			logger.error(e);
			throw new IllegalArgumentException("Op��o de C�digos IBGE inv�lida");
		}
	}

	private static void lerArgumentoOpcaoAnexos() {
		String opcao = System.getProperty(OPCAO_ANEXOS);
		try {
			if (opcao != null && !opcao.trim().isEmpty()) {
				String[] anexos = opcao.split(",");
				opcaoAnexosSelecionados = Arrays.asList(anexos);
				if(opcaoAnexosSelecionados == null || opcaoAnexosSelecionados.isEmpty()) {
					throw new IllegalArgumentException("Op��o de Anexos inv�lida");
				}
			} else {
				opcaoAnexosSelecionados = (relatorioSelecionado.equals(Relatorio.rreo)) ? RREOService.ANEXOS_RREO
						: (relatorioSelecionado.equals(Relatorio.rgf)) ? RGFService.ANEXOS_RGF
								: (relatorioSelecionado.equals(Relatorio.dca)) ? DCAService.ANEXOS_DCA : null;
			}
			logger.info("Op��o de Anexos selecionados:" + opcao);						
		} catch (Exception e) {
			logger.error("Erro ao ler par�metro Anexos");
			logger.error(e);
			throw new IllegalArgumentException("Op��o de Anexos inv�lida");
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
