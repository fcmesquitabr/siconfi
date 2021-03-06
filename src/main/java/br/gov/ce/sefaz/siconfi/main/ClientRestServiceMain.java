package br.gov.ce.sefaz.siconfi.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.gov.ce.sefaz.siconfi.util.Constantes;

public class ClientRestServiceMain {

	private static final Logger logger = LogManager.getLogger(ClientRestServiceMain.class);
	
	public static void main(String[] args) {
		try {
			BuscadorDadosSiconfiAPI buscadorDadosApi = new BuscadorDadosSiconfiAPI();
			buscadorDadosApi.buscarDados();
		} catch(IllegalArgumentException iae) {
			logger.error(iae.getMessage());
			exibirMensagemAjuda();
		}

		logger.info("Fim.");
	}

	private static void exibirMensagemAjuda() {
		System.out.println("Escolha os seguintes valores para as op��es do programa:");
		System.out.println("");
		System.out.println("Op��o -Drelatorio (Obrigat�rio) ");
		System.out.println("\t Valores poss�veis: anexo_relatorio, ente, extrato_entrega, rreo, rgf, dca, msc_patrimonial, msc_orcamentaria, msc_controle. Exemplo: -Drelatorio=rreo");
		System.out.println("Op��o -DsaidaDados (Opcional. Valor padr�o ARQUIVO) ");
		System.out.println("\t Valores poss�veis: BANCO, ARQUIVO, CONSOLE");
		System.out.println("Op��o -DcaminhoArquivo (Opcional. Considerada apenas quando a op��o de sa�da de dados � ARQUIVO) ");
		System.out.println("\t Exemplo: -DcaminhoArquivo=\"C:\\nome_do_arquivo.csv\"");		
		System.out.println("Op��o -Dexercicios (Opcional. Valor padr�o: " + Constantes.EXERCICIOS_DISPONIVEIS + ") ");
		System.out.println("\t Valores poss�veis: lista de exerc�cio separados por v�rgula. Exemplo: -Dexercicios=2020,2019");
		System.out.println("Op��o -Dperiodos (Opcional. Considerada apenas para RREO, RGF e MSC. Valor Padr�o para RREO: "
				+ Constantes.BIMESTRES + ". Valor Padr�o para RGF: " + Constantes.QUADRIMESTRES + ". Valor Padr�o para MSC: " + Constantes.MESES +") ");
		System.out.println("\t Valores poss�veis: lista de periodos (bimestres, quadrimestres) separados por v�rgula. Exemplo: -Dperiodos=1,2,3");
		System.out.println("Op��o -Desfera (Opcional). Valor padr�o: ED. Caso seja utilizada a op��o -Dente, essa op��o � desconsiderada");
		System.out.println("\t Valores poss�veis: Esfera dos entes da Federa��o. Valores: U - Uni�o, E - Estados, D - Distrito Federal, "
				+ "ED - Estados e Distrito Federal, M - Munic�pios. Exemplo: -Desfera=E");
		System.out.println("Op��o -Dentes (Opcional)");
		System.out.println("\t Valores poss�veis: lista de c�digos ibge dos entes (Pode ser obtido com a op��o -Drelatorio=entes) separados por v�rgula. Exemplo (Piau�, Cear�): -Dentes=22,23");
		System.out.println("Op��o -Dcapital (Opcional)");
		System.out.println("\t Valores poss�veis: 0 para munic�pios que n�o s�o capitais, e 1 para capitais. Exemplo: -Dcapital=1");
		System.out.println("Op��o -Duf (Opcional)");
		System.out.println("\t Valores poss�veis: Lista de siglas de UFs, pode ser utilizado para filtrar munic�pios. Exemplo: -Duf=CE,PI");
		System.out.println("Op��o -DpopulacaoMinima (Opcional)");
		System.out.println("\t Valores poss�veis: consultar mun�cipios que possuem no m�nimo a popula��o indicada. Exemplo (para munic�pios com mais (inclusive) de 500.000 habitantes): -DpopulacaoMinima=500000");		
		System.out.println("Op��o -DpopulacaoMaxima (Opcional)");
		System.out.println("\t Valores poss�veis: consultar mun�cipios que possuem no m�ximo a popula��o indicada. Exemplo (para munic�pios com menos (inclusive) de 500.000 habitantes): -DpopulacaoMaxima=500000");		
		System.out.println("Op��o -Dpoder (Opcional. V�lido para apenas RGF. Caso n�o especificado, ser�o consultados todos os poderes)");
		System.out.println("\t Valores poss�veis: lista de poderes separados por v�rgula. Valores E - Executivo, L - Legislativo, "
				+ "J - Juci�rio, M - Minist�rio P�blico, D - Defensoria P�blica. Exemplo (Executivo, Judici�rio): -Dpoder=E,J");
		System.out.println("Op��o -DclassesConta (Classes da Conta Cont�bil. Opcional e v�lida apenas para os relat�rios MSC. Caso n�o especificado, ser�o consultados todas as classes");
		System.out.println("\t Valores poss�veis: lista de anexos separados por v�rgula.");
		System.out.println("\t\t Para MSC Patrimonial: Classes 1, 2, 3 e 4");
		System.out.println("\t\t Para MSC Or�ament�ria: Classes 5 e 6");
		System.out.println("\t\t Para MSC Controle: Classes 7 e 8");
		System.out.println("\t Exemplo: -DclassesConta=1,3");
		System.out.println("Op��o -DtipoMatriz (Tipo de Matriz. Opcional e v�lida apenas para os relat�rios MSC. Valor padr�o: MSCC) ");
		System.out.println("\t Valores poss�veis: MSCC - Matriz de Saldo Cont�beis Mensal ou Agregada e MSCE - Matriz de Saldo Cont�beis de Encerramento do Exerc�cio.");
		System.out.println("\t Exemplo: -DtipoMatriz=MSCC");
		System.out.println("Op��o -DtiposValorMatriz (Tipo de Valor da Matriz. Opcional e v�lida apenas para os relat�rios MSC. Caso n�o especificado, ser�o consultados todos os tipos de valores) ");
		System.out.println("\t Valores poss�veis: beginning_balance, ending_balance e period_change.");
		System.out.println("\t Exemplo: -DtiposValorMatriz=ending_balance,period_change");
		System.out.println("Op��o -Danexos (Opcional e v�lida apenas para os relat�rios DCA, RGF e RREO Caso n�o especificada, ser�o consultados todos os anexos) ");
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
