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
import br.gov.ce.sefaz.siconfi.enums.TipoMatrizSaldoContabeis;
import br.gov.ce.sefaz.siconfi.enums.TipoValorMatrizSaldoContabeis;
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
	public static final String OPCAO_TIPO_MATRIZ = "tipoMatriz";
	public static final String OPCAO_CLASSES_CONTA_CONTABIL = "classesConta";	
	public static final String OPCAO_TIPOS_VALOR_MATRIZ = "tiposValorMatriz";	
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

	private static TipoMatrizSaldoContabeis opcaoTipoMatrizSelecionado;
	private static List<TipoValorMatrizSaldoContabeis> opcaoTiposValorMatrizSelecionado;
	private static List<Integer> opcaoClassesContasSelecionadas;

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
		lerArgumentoOpcaoTipoMatriz();
		lerArgumentoOpcaoTiposValorMatriz();
		lerArgumentoOpcaoClassesContaContabil();
	}
	
	private static void lerArgumentoOpcaoRelatorio() {
		
		String opcaoRelatorio = System.getProperty(OPCAO_RELATORIO);
		if(Utils.isStringVazia(opcaoRelatorio)) {
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

			opcaoSalvamentoSelecionada = (Utils.isStringVazia(opcaoSalvamento))
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
		
		try {			
			String opcaoEsfera = System.getProperty(OPCAO_ESFERA);
			if (Utils.isStringVazia(opcaoEsfera)) {
				opcaoEsferaSelecionada = Esfera.ESTADOS_E_DISTRITO_FEDERAL;
			} else {
				opcaoEsferaSelecionada = Esfera.getEsfera(opcaoEsfera.trim());			
				if (opcaoEsferaSelecionada == null) {
					throw new IllegalArgumentException("Opção de Esfera inválida");
				}
			}
			logger.info("Opção de Esfera selecionada:" + opcaoEsfera);	
			
		} catch (Exception e) {
			logger.error("Erro ao ler parâmetro Esfera");
			logger.error(e);
			throw new IllegalArgumentException("Opção de Esfera inválida");
		}
	}

	private static void lerArgumentoOpcaoPoder() {
		try {
			String opcao = System.getProperty(OPCAO_PODER);
			if (Utils.isStringVazia(opcao)) {
				opcaoPoderesSelecionados = Arrays.asList(Poder.values());
			} else {
				String[] poderes = opcao.split(",");
				opcaoPoderesSelecionados = new ArrayList<Poder>();
				for (String codigoPoder: poderes) {
					Poder poder = Poder.getPoder(codigoPoder.trim());
					if(poder!=null) {
						opcaoPoderesSelecionados.add(poder);
					}
				}
				if (Utils.isEmptyCollection(opcaoPoderesSelecionados)) {
					throw new IllegalArgumentException("Opção de Poder inválida");
				}
			}
			logger.info("Opções de Poderes selecionados:" + opcao);			
		} catch (Exception e) {
			logger.error("Erro ao ler parâmetro Poderes");
			logger.error(e);
			throw new IllegalArgumentException("Opção de Poderes inválida");
		}
	}

	private static void lerArgumentoOpcaoExercicios() {

		try {
			String opcao = System.getProperty(OPCAO_EXERCICIOS);
			if (Utils.isStringVazia(opcao)) {
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
		
		try {
			String opcao = System.getProperty(OPCAO_PERIODOS);
			
			if (Utils.isStringVazia(opcao)) {
				opcaoPeriodosSelecionados = (relatorioSelecionado.equals(Relatorio.rreo)) ? SiconfiService.BIMESTRES
						: (relatorioSelecionado.equals(Relatorio.rgf)) ? SiconfiService.QUADRIMESTRES
								: (relatorioSelecionado.equals(Relatorio.msc_patrimonial)) ? SiconfiService.MESES : null;
			} else {
				String[] periodos = opcao.split(",");
				opcaoPeriodosSelecionados = new ArrayList<Integer>();
				for (String periodo: periodos) {
					opcaoPeriodosSelecionados.add(Integer.valueOf(periodo.trim()));
				}
				if (Utils.isEmptyCollection(opcaoPeriodosSelecionados)) {
					throw new IllegalArgumentException("Opção de Períodos inválida");
				}			
			}
			
			logger.info("Opção de Períodos selecionados:" + opcao);			
		} catch (Exception e) {
			logger.error("Erro ao ler parâmetro período");
			logger.error(e);
			throw new IllegalArgumentException("Opção de Períodos inválida");
		}
	}

	private static void lerArgumentoOpcaoCodigosIBGE() {
		String opcao = System.getProperty(OPCAO_CODIGOS_IBGE);
		try {
			if (!Utils.isStringVazia(opcao)) {
				String[] codigosIbge = opcao.split(",");
				opcaoCodigosIbgeSelecionados = new ArrayList<String>();
				for (String codigo: codigosIbge) {
					if(!codigo.trim().isEmpty()) {
						opcaoCodigosIbgeSelecionados.add(codigo.trim());						
					}
				}
				if (Utils.isEmptyCollection(opcaoCodigosIbgeSelecionados)) {
					throw new IllegalArgumentException("Opção de Códigos IBGE inválida");
				}			
			} 
			logger.info("Opção de Códigos IBGE selecionados:" + opcao);			
		} catch (Exception e) {
			logger.error("Erro ao ler parâmetro Códigos IBGE");
			logger.error(e);
			throw new IllegalArgumentException("Opção de Códigos IBGE inválida");
		}
	}

	private static void lerArgumentoOpcaoAnexos() {

		try {
			String opcao = System.getProperty(OPCAO_ANEXOS);
			if (!Utils.isStringVazia(opcao)) {
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

	private static void lerArgumentoOpcaoTipoMatriz() {
		
		try {
			String opcaoTipoMatriz = System.getProperty(OPCAO_TIPO_MATRIZ);
			if (Utils.isStringVazia(opcaoTipoMatriz)) {
				opcaoTipoMatrizSelecionado = TipoMatrizSaldoContabeis.MSCC;
			} else {
				opcaoTipoMatrizSelecionado = TipoMatrizSaldoContabeis.valueOf(opcaoTipoMatriz);			
			}
			logger.info("Opção de Tipo de Matriz de Saldo selecionado:" + opcaoTipoMatriz);			
		} catch(Exception e) {
			logger.error("Erro ao ler parâmetro Tipo Matriz");
			logger.error(e);
			throw new IllegalArgumentException("Opção de Anexos inválida");			
		}
	}

	private static void lerArgumentoOpcaoTiposValorMatriz() {
		
		try {
			String opcao = System.getProperty(OPCAO_TIPOS_VALOR_MATRIZ);
			if (Utils.isStringVazia(opcao)) {
				opcaoTiposValorMatrizSelecionado = Arrays.asList(TipoValorMatrizSaldoContabeis.values());
			} else {
				String[] tiposValorMatriz = opcao.split(",");
				opcaoTiposValorMatrizSelecionado = new ArrayList<>();
				for (String tipoValorCodigo: tiposValorMatriz) {
					TipoValorMatrizSaldoContabeis tipoValorMatriz = TipoValorMatrizSaldoContabeis.valueOf(tipoValorCodigo);
					opcaoTiposValorMatrizSelecionado.add(tipoValorMatriz);
				}
				if (Utils.isEmptyCollection(opcaoTiposValorMatrizSelecionado)) {
					throw new IllegalArgumentException("Opção de Tipo de Valor Matriz inválida");
				}
			}
			logger.info("Opções de Tipos Valores selecionados:" + opcao);			
		} catch(Exception e) {
			logger.error("Erro ao ler parâmetro Tipo de Valor de MSC");
			logger.error(e);
			throw new IllegalArgumentException("Opção de Tipo de Valor Matriz inválida");				
		}
	}

	private static void lerArgumentoOpcaoClassesContaContabil() {
		
		try {
			String opcao = System.getProperty(OPCAO_CLASSES_CONTA_CONTABIL);
			if (Utils.isStringVazia(opcao)) {
				opcaoClassesContasSelecionadas = getRelatorioSelecionado().equals(Relatorio.msc_patrimonial)
						? SiconfiService.CLASSES_CONTAS_PATRIMONIAIS
						: getRelatorioSelecionado().equals(Relatorio.msc_orcamentaria)
								? SiconfiService.CLASSES_CONTAS_ORCAMENTARIAS
								: getRelatorioSelecionado().equals(Relatorio.msc_controle)
										? SiconfiService.CLASSES_CONTAS_CONTROLE
										: null;
			} else {
				String[] classesContaContabil = opcao.split(",");
				opcaoClassesContasSelecionadas = new ArrayList<>();
				for (String classe: classesContaContabil) {
					opcaoClassesContasSelecionadas.add(Integer.valueOf(classe.trim()));
				}
				if (Utils.isEmptyCollection(opcaoClassesContasSelecionadas)) {
					throw new IllegalArgumentException("Opção de Classe de Conta Contábil inválida");
				}
			}
			logger.info("Opções de Classes de Conta Contábil selecionadas:" + opcao);			
		} catch(Exception e) {
			logger.error("Erro ao ler parâmetro Classes de Conta");
			logger.error(e);
			throw new IllegalArgumentException("Opção de Classe de Conta Contábil inválida");				
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

	public static TipoMatrizSaldoContabeis getOpcaoTipoMatrizSelecionado() {
		return opcaoTipoMatrizSelecionado;
	}

	public static List<TipoValorMatrizSaldoContabeis> getOpcaoTiposValorMatrizSelecionado() {
		return opcaoTiposValorMatrizSelecionado;
	}

	public static List<Integer> getOpcaoClassesContasSelecionadas() {
		return opcaoClassesContasSelecionadas;
	}	
}
