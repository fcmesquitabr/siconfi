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
	public static final String OPCAO_CAPITAL = "capital";
	public static final String OPCAO_UF = "uf";
	public static final String OPCAO_POPULACAO_MINIMA = "populacaoMinima";
	public static final String OPCAO_POPULACAO_MAXIMA = "populacaoMaxima";
	
	private static Relatorio relatorioSelecionado;
	private static OpcaoSalvamentoDados opcaoSalvamentoSelecionada;
	private static String opcaoCaminhoArquivoSelecionado;
	private static Esfera opcaoEsferaSelecionada;
	private static List<Integer> opcaoExerciciosSelecionados;
	private static List<Integer> opcaoPeriodosSelecionados;
	private static List<String> opcaoCodigosIbgeSelecionados;
	private static List<String> opcaoCodigosUFSelecionados;
	private static List<String> opcaoAnexosSelecionados;
	private static List<Poder> opcaoPoderesSelecionados;
	private static Integer opcaoCapitalSelecionada;
	private static Long opcaoPopulacaoMinimaSelecionada;
	private static Long opcaoPopulacaoMaximaSelecionada;

	private static TipoMatrizSaldoContabeis opcaoTipoMatrizSelecionado;
	private static List<TipoValorMatrizSaldoContabeis> opcaoTiposValorMatrizSelecionado;
	private static List<Integer> opcaoClassesContasSelecionadas;

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
		lerArgumentoOpcaoTipoMatriz();
		lerArgumentoOpcaoTiposValorMatriz();
		lerArgumentoOpcaoClassesContaContabil();
		lerArgumentoOpcaoCapital();
		lerArgumentoOpcaoUF();
		lerArgumentoOpcaoPopulacaoMinima();
		lerArgumentoOpcaoPopulacaoMaxima();
	}
	
	private static void lerArgumentoOpcaoRelatorio() {
		
		String opcaoRelatorio = System.getProperty(OPCAO_RELATORIO);
		if(Utils.isStringVazia(opcaoRelatorio)) {
			throw new IllegalArgumentException("Op��o de Relat�rio n�o escolhida");
		}
		try {
			relatorioSelecionado = Relatorio.valueOf(opcaoRelatorio.trim());
			logger.info("Op��o de Relat�rio Selecionado: {}", relatorioSelecionado);
		} catch(IllegalArgumentException iae) {
			throw new IllegalArgumentException("Op��o de Relat�rio inv�lida");
		}
		
	}
	
	private static void lerArgumentoOpcaoSalvamento() {
		
		try {
			String opcaoSalvamento = System.getProperty(OPCAO_SALVAMENTO);

			opcaoSalvamentoSelecionada = (Utils.isStringVazia(opcaoSalvamento))
					? OpcaoSalvamentoDados.ARQUIVO
					: OpcaoSalvamentoDados.valueOf(opcaoSalvamento.trim());

			logger.info("Op��o de sa�da de dados selecionada: {}", opcaoSalvamentoSelecionada);
		} catch(IllegalArgumentException iae) {
			throw new IllegalArgumentException("Op��o de Sa�da de dados inv�lida");
		}	
		
	}

	private static void lerArgumentoOpcaoCaminhoArquivo() {
		opcaoCaminhoArquivoSelecionado = System.getProperty(OPCAO_CAMINHO_ARQUIVO);
		logger.info("Op��o de caminho de arquivo selecionado: {}", opcaoCaminhoArquivoSelecionado);
	}

	private static void lerArgumentoOpcaoEsfera() {
		
		try {			
			String opcaoEsfera = System.getProperty(OPCAO_ESFERA);
			if (Utils.isStringVazia(opcaoEsfera)) {
				opcaoEsferaSelecionada = Esfera.ESTADOS_E_DISTRITO_FEDERAL;
			} else {
				opcaoEsferaSelecionada = Esfera.getEsfera(opcaoEsfera.trim());			
				if (opcaoEsferaSelecionada == null) {
					throw new IllegalArgumentException("Op��o de Esfera inv�lida");
				}
			}
			logger.info("Op��o de Esfera selecionada: {}", opcaoEsfera);	
			
		} catch (Exception e) {
			logger.error("Erro ao ler par�metro Esfera");
			logger.error(e);
			throw new IllegalArgumentException("Op��o de Esfera inv�lida");
		}
	}

	private static void lerArgumentoOpcaoPoder() throws IllegalArgumentException{
		try {
			String opcao = System.getProperty(OPCAO_PODER);
			if (Utils.isStringVazia(opcao)) {
				opcaoPoderesSelecionados = Arrays.asList(Poder.values());
			} else {
				String[] poderes = opcao.split(",");
				opcaoPoderesSelecionados = new ArrayList<>();
				for (String codigoPoder: poderes) {
					Poder poder = Poder.getPoder(codigoPoder.trim());
					if(poder!=null) {
						opcaoPoderesSelecionados.add(poder);
					}
				}
				if (Utils.isEmptyCollection(opcaoPoderesSelecionados)) {
					throw new IllegalArgumentException("Op��o de Poder inv�lida");
				}
			}
			logger.info("Op��es de Poderes selecionados: {}", opcao);			
		} catch (Exception e) {
			logger.error("Erro ao ler par�metro Poderes");
			logger.error(e);
			throw new IllegalArgumentException("Op��o de Poder inv�lida");
		}
	}

	private static void lerArgumentoOpcaoExercicios() {

		try {
			String opcao = System.getProperty(OPCAO_EXERCICIOS);
			if (Utils.isStringVazia(opcao)) {
				opcaoExerciciosSelecionados = Constantes.EXERCICIOS_DISPONIVEIS;
			} else {
				opcaoExerciciosSelecionados = getListaOpcoesInteger(opcao, "Op��o de Exerc�cio inv�lida");
			}
			logger.info("Op��es de Exerc�cios selecionados: {}", opcao);			
		} catch (Exception e) {
			logger.error("Erro ao ler par�metro exerc�cio");
			logger.error(e);
			throw new IllegalArgumentException("Op��o de Exerc�cio inv�lida");
		}
	}

	private static void lerArgumentoOpcaoPeriodos() {
		
		try {
			
			String opcao = System.getProperty(OPCAO_PERIODOS);
			
			if (Utils.isStringVazia(opcao)) {
				opcaoPeriodosSelecionados = getOpcoesPeriodoPadrao();
			} else {
				opcaoPeriodosSelecionados = getListaOpcoesInteger(opcao, "Op��o de Per�odos inv�lida");
			}
			
			logger.info("Op��o de Per�odos selecionados: {}", opcao);			
		} catch (Exception e) {
			logger.error("Erro ao ler par�metro per�odo");
			logger.error(e);
			throw new IllegalArgumentException("Op��o de Per�odos inv�lida");
		}
	}

	private static List<Integer> getOpcoesPeriodoPadrao(){
		List<Integer> opcoesPeriodoPadraoMSC = relatorioSelecionado.equals(Relatorio.msc_patrimonial)
				? Constantes.MESES
				: null;
		List<Integer> opcoesPeriodoPadraoRGF = relatorioSelecionado.equals(Relatorio.rgf)
				? Constantes.QUADRIMESTRES
				: opcoesPeriodoPadraoMSC;
		return relatorioSelecionado.equals(Relatorio.rreo) ? Constantes.BIMESTRES
				: opcoesPeriodoPadraoRGF;
	}
	
	private static List<Integer> getListaOpcoesInteger(String opcoes, String mensagemErro){
		String[] opcoesArray = opcoes.split(",");
		List<Integer> listaOpcoes = new ArrayList<>();
		for (String opcao: opcoesArray) {
			listaOpcoes.add(Integer.valueOf(opcao.trim()));
		}
		if (Utils.isEmptyCollection(listaOpcoes)) {
			throw new IllegalArgumentException(mensagemErro);
		}			
		return listaOpcoes;		
	}
	
	private static void lerArgumentoOpcaoCodigosIBGE() {
		try {
			String opcao = System.getProperty(OPCAO_CODIGOS_IBGE);

			if (!Utils.isStringVazia(opcao)) {
				opcaoCodigosIbgeSelecionados = getListaOpcoesString(opcao, "Op��o de C�digos IBGE inv�lida");
			} 
			
			logger.info("Op��o de C�digos IBGE selecionados: {}", opcao);			
		} catch (Exception e) {
			logger.error("Erro ao ler par�metro C�digos IBGE");
			logger.error(e);
			throw new IllegalArgumentException("Op��o de C�digos IBGE inv�lida");
		}
	}

	private static List<String> getListaOpcoesString(String opcoes, String mensagemErro){
		String[] opcoesArray = opcoes.split(",");
		List<String> listaOpcoes = new ArrayList<>();
		for (String opcao: opcoesArray) {
			if(!opcao.trim().isEmpty()) {
				listaOpcoes.add(opcao.trim());
			}
		}
		if (Utils.isEmptyCollection(listaOpcoes)) {
			throw new IllegalArgumentException(mensagemErro);
		}			
		return listaOpcoes;		
	}

	private static void lerArgumentoOpcaoAnexos() {

		try {
			String opcao = System.getProperty(OPCAO_ANEXOS);
			if (!Utils.isStringVazia(opcao)) {			
				opcaoAnexosSelecionados = getListaOpcoesString(opcao, "Op��o de Anexos inv�lida");new ArrayList<>();
			}
			logger.info("Op��o de Anexos selecionados: {}", opcao);						
		} catch (Exception e) {
			logger.error("Erro ao ler par�metro Anexos");
			logger.error(e);
			throw new IllegalArgumentException("Op��o de Anexos inv�lida");
		}
	}

	private static void lerArgumentoOpcaoTipoMatriz() {
		
		try {
			String opcaoTipoMatriz = System.getProperty(OPCAO_TIPO_MATRIZ);
			if (Utils.isStringVazia(opcaoTipoMatriz)) {
				opcaoTipoMatrizSelecionado = TipoMatrizSaldoContabeis.MSCC;
			} else {
				opcaoTipoMatrizSelecionado = TipoMatrizSaldoContabeis.valueOf(opcaoTipoMatriz.trim());			
			}
			logger.info("Op��o de Tipo de Matriz de Saldo selecionado: {}", opcaoTipoMatriz);			
		} catch(Exception e) {
			logger.error("Erro ao ler par�metro Tipo Matriz");
			logger.error(e);
			throw new IllegalArgumentException("Op��o de Tipo Matriz inv�lida");			
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
					TipoValorMatrizSaldoContabeis tipoValorMatriz = TipoValorMatrizSaldoContabeis.valueOf(tipoValorCodigo.trim());
					opcaoTiposValorMatrizSelecionado.add(tipoValorMatriz);
				}
				if (Utils.isEmptyCollection(opcaoTiposValorMatrizSelecionado)) {
					throw new IllegalArgumentException("Op��o de Tipo de Valor Matriz inv�lida");
				}
			}
			logger.info("Op��es de Tipos Valores selecionados: {}", opcao);			
		} catch(Exception e) {
			logger.error("Erro ao ler par�metro Tipo de Valor de MSC");
			logger.error(e);
			throw new IllegalArgumentException("Op��o de Tipo de Valor Matriz inv�lida");				
		}
	}

	private static void lerArgumentoOpcaoClassesContaContabil() {
		
		try {
			String opcao = System.getProperty(OPCAO_CLASSES_CONTA_CONTABIL);
			if (Utils.isStringVazia(opcao)) {
				opcaoClassesContasSelecionadas = getOpcoesClassesContaContabilPadrao();
			} else {
				opcaoClassesContasSelecionadas = getListaOpcoesInteger(opcao, "Op��o de Classe de Conta Cont�bil inv�lida");
			}
			logger.info("Op��es de Classes de Conta Cont�bil selecionadas: {}", opcao);			
		} catch(Exception e) {
			logger.error("Erro ao ler par�metro Classes de Conta");
			logger.error(e);
			throw new IllegalArgumentException("Op��o de Classe de Conta Cont�bil inv�lida");				
		}
	}

	private static List<Integer> getOpcoesClassesContaContabilPadrao(){

		List<Integer> opcoesPadraoControle = getRelatorioSelecionado().equals(Relatorio.msc_controle)
				? Constantes.CLASSES_CONTAS_CONTROLE
				: null;
		List<Integer> opcoesPadraoOrcamentaria = getRelatorioSelecionado().equals(Relatorio.msc_orcamentaria)
				? Constantes.CLASSES_CONTAS_ORCAMENTARIAS
				: opcoesPadraoControle;
		
		return getRelatorioSelecionado().equals(Relatorio.msc_patrimonial) ? Constantes.CLASSES_CONTAS_PATRIMONIAIS
				: opcoesPadraoOrcamentaria;
	}

	private static void lerArgumentoOpcaoCapital() {
		try {
			String opcao = System.getProperty(OPCAO_CAPITAL);
			if(!Utils.isStringVazia(opcao)) {
				opcaoCapitalSelecionada = Integer.valueOf(opcao);				
			}
		} catch(Exception e) {
			logger.error("Erro ao ler par�metro Capital");
			logger.error(e);
			throw new IllegalArgumentException("Op��o de Capital inv�lida");				
		}
	}

	private static void lerArgumentoOpcaoUF() {
		try {
			String opcao = System.getProperty(OPCAO_UF);
			if (!Utils.isStringVazia(opcao)) {
				opcaoCodigosUFSelecionados= getListaOpcoesString(opcao, "Op��o de C�digos UF inv�lida");
			} 
		} catch(Exception e) {
			logger.error("Erro ao ler par�metro UF ");
			logger.error(e);
			throw new IllegalArgumentException("Op��o de UF inv�lida");				
		}
	}

	private static void lerArgumentoOpcaoPopulacaoMinima() {
		try {
			String opcao = System.getProperty(OPCAO_POPULACAO_MINIMA);
			if(!Utils.isStringVazia(opcao)) {
				opcaoPopulacaoMinimaSelecionada = Long.valueOf(opcao);				
			}
		} catch(Exception e) {
			logger.error("Erro ao ler par�metro Popula��o M�nima");
			logger.error(e);
			throw new IllegalArgumentException("Op��o de Popula��o M�nima inv�lida");				
		}
	}
	
	private static void lerArgumentoOpcaoPopulacaoMaxima() {
		try {			
			String opcao = System.getProperty(OPCAO_POPULACAO_MAXIMA);
			if(!Utils.isStringVazia(opcao)) {
				opcaoPopulacaoMaximaSelecionada = Long.valueOf(opcao);				
			}
		} catch(Exception e) {
			logger.error("Erro ao ler par�metro Popula��o M�xima");
			logger.error(e);
			throw new IllegalArgumentException("Op��o de Popula��o M�xima inv�lida");				
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

	public static List<String> getOpcaoCodigosUFSelecionados() {
		return opcaoCodigosUFSelecionados;
	}

	public static Integer getOpcaoCapitalSelecionada() {
		return opcaoCapitalSelecionada;
	}

	public static Long getOpcaoPopulacaoMinimaSelecionada() {
		return opcaoPopulacaoMinimaSelecionada;
	}

	public static Long getOpcaoPopulacaoMaximaSelecionada() {
		return opcaoPopulacaoMaximaSelecionada;
	}
}
