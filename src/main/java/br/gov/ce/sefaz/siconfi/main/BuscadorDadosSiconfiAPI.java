package br.gov.ce.sefaz.siconfi.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.gov.ce.sefaz.siconfi.opcoes.OpcoesCargaDadosAnexoRelatorio;
import br.gov.ce.sefaz.siconfi.opcoes.OpcoesCargaDadosDCA;
import br.gov.ce.sefaz.siconfi.opcoes.OpcoesCargaDadosEnte;
import br.gov.ce.sefaz.siconfi.opcoes.OpcoesCargaDadosExtratoEntrega;
import br.gov.ce.sefaz.siconfi.opcoes.OpcoesCargaDadosMSC;
import br.gov.ce.sefaz.siconfi.opcoes.OpcoesCargaDadosRGF;
import br.gov.ce.sefaz.siconfi.opcoes.OpcoesCargaDadosRREO;
import br.gov.ce.sefaz.siconfi.service.AnexoRelatorioService;
import br.gov.ce.sefaz.siconfi.service.DCAService;
import br.gov.ce.sefaz.siconfi.service.EnteService;
import br.gov.ce.sefaz.siconfi.service.ExtratoEntregaService;
import br.gov.ce.sefaz.siconfi.service.MSCControleService;
import br.gov.ce.sefaz.siconfi.service.MSCOrcamentariaService;
import br.gov.ce.sefaz.siconfi.service.MSCPatrimonialService;
import br.gov.ce.sefaz.siconfi.service.RGFService;
import br.gov.ce.sefaz.siconfi.service.RREOService;
import br.gov.ce.sefaz.siconfi.util.LeitorParametrosPrograma;

public class BuscadorDadosSiconfiAPI {

	private static final Logger logger = LogManager.getLogger(BuscadorDadosSiconfiAPI.class);

	private EnteService enteService;
	
	private AnexoRelatorioService anexoRelatorioService;
	
	private ExtratoEntregaService extratoEntregaService;

	private DCAService dcaService;
	
	private RGFService rgfService;
	
	private RREOService rreoService;
	
	private MSCPatrimonialService mscPatrimonialService;

	private MSCOrcamentariaService mscOrcamentariaService;

	private MSCControleService mscControleService;

	public void buscarDados() throws IllegalArgumentException {

		LeitorParametrosPrograma.lerArgumentos();
		
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
		}	
	}
	
	private void carregarEntes() {

		OpcoesCargaDadosEnte opcoes = new OpcoesCargaDadosEnte.Builder()
				.opcaoSalvamentoDados(LeitorParametrosPrograma.getOpcaoSalvamentoSelecionada())
				.nomeArquivo(LeitorParametrosPrograma.getOpcaoCaminhoArquivoSelecionado())
				.esfera(LeitorParametrosPrograma.getOpcaoEsferaSelecionada())
				.codigosIbge(LeitorParametrosPrograma.getOpcaoCodigosIbgeSelecionados())
				.codigosUF(LeitorParametrosPrograma.getOpcaoCodigosUFSelecionados())
				.capital(LeitorParametrosPrograma.getOpcaoCapitalSelecionada())
				.populacaoMinima(LeitorParametrosPrograma.getOpcaoPopulacaoMinimaSelecionada())
				.populacaoMaxima(LeitorParametrosPrograma.getOpcaoPopulacaoMaximaSelecionada())
				.build();

		getEnteService().carregarDados(opcoes);
	}

	private void carregarAnexosRelatorios() {
		
		OpcoesCargaDadosAnexoRelatorio opcoes = new OpcoesCargaDadosAnexoRelatorio.Builder()
				.opcaoSalvamentoDados(LeitorParametrosPrograma.getOpcaoSalvamentoSelecionada())
				.nomeArquivo(LeitorParametrosPrograma.getOpcaoCaminhoArquivoSelecionado())
				.build();
		getAnexoRelatorioService().carregarDados(opcoes);
	}

	private void carregarExtratosEntregas() {
		
		OpcoesCargaDadosExtratoEntrega opcoes = new OpcoesCargaDadosExtratoEntrega.Builder()
				.opcaoSalvamentoDados(LeitorParametrosPrograma.getOpcaoSalvamentoSelecionada())
				.nomeArquivo(LeitorParametrosPrograma.getOpcaoCaminhoArquivoSelecionado())
				.esfera(LeitorParametrosPrograma.getOpcaoEsferaSelecionada())
				.exercicios(LeitorParametrosPrograma.getOpcaoExerciciosSelecionados())
				.periodos(LeitorParametrosPrograma.getOpcaoPeriodosSelecionados())
				.codigosIbge(LeitorParametrosPrograma.getOpcaoCodigosIbgeSelecionados())
				.codigosUF(LeitorParametrosPrograma.getOpcaoCodigosUFSelecionados())
				.capital(LeitorParametrosPrograma.getOpcaoCapitalSelecionada())
				.populacaoMinima(LeitorParametrosPrograma.getOpcaoPopulacaoMinimaSelecionada())
				.populacaoMaxima(LeitorParametrosPrograma.getOpcaoPopulacaoMaximaSelecionada())
				.build();
		
		getExtratoEntregaService().carregarDados(opcoes);
	}

	private void carregarDadosDCA() {
		
		OpcoesCargaDadosDCA opcoes = new OpcoesCargaDadosDCA.Builder()
				.opcaoSalvamentoDados(LeitorParametrosPrograma.getOpcaoSalvamentoSelecionada())
				.nomeArquivo(LeitorParametrosPrograma.getOpcaoCaminhoArquivoSelecionado())
				.esfera(LeitorParametrosPrograma.getOpcaoEsferaSelecionada())
				.exercicios(LeitorParametrosPrograma.getOpcaoExerciciosSelecionados())
				.codigosIbge(LeitorParametrosPrograma.getOpcaoCodigosIbgeSelecionados())
				.codigosUF(LeitorParametrosPrograma.getOpcaoCodigosUFSelecionados())
				.listaAnexos(LeitorParametrosPrograma.getOpcaoAnexosSelecionados())				
				.capital(LeitorParametrosPrograma.getOpcaoCapitalSelecionada())
				.populacaoMinima(LeitorParametrosPrograma.getOpcaoPopulacaoMinimaSelecionada())
				.populacaoMaxima(LeitorParametrosPrograma.getOpcaoPopulacaoMaximaSelecionada())
				.build();
		
		getDCAService().carregarDados(opcoes);		
	}

	private void carregarDadosRGF() {
		
		OpcoesCargaDadosRGF opcoes = new OpcoesCargaDadosRGF.Builder()
				.opcaoSalvamentoDados(LeitorParametrosPrograma.getOpcaoSalvamentoSelecionada())
				.nomeArquivo(LeitorParametrosPrograma.getOpcaoCaminhoArquivoSelecionado())
				.esfera(LeitorParametrosPrograma.getOpcaoEsferaSelecionada())
				.listaPoderes(LeitorParametrosPrograma.getOpcaoPoderesSelecionados())
				.exercicios(LeitorParametrosPrograma.getOpcaoExerciciosSelecionados())
				.periodos(LeitorParametrosPrograma.getOpcaoPeriodosSelecionados())
				.codigosIbge(LeitorParametrosPrograma.getOpcaoCodigosIbgeSelecionados())
				.codigosUF(LeitorParametrosPrograma.getOpcaoCodigosUFSelecionados())
				.listaAnexos(LeitorParametrosPrograma.getOpcaoAnexosSelecionados())
				.capital(LeitorParametrosPrograma.getOpcaoCapitalSelecionada())
				.populacaoMinima(LeitorParametrosPrograma.getOpcaoPopulacaoMinimaSelecionada())
				.populacaoMaxima(LeitorParametrosPrograma.getOpcaoPopulacaoMaximaSelecionada())				
				.build();		
		
		getRgfService().carregarDados(opcoes);		
	}
	
	private void carregarDadosRREO() {
		
		OpcoesCargaDadosRREO opcoes = new OpcoesCargaDadosRREO.Builder()
				.opcaoSalvamentoDados(LeitorParametrosPrograma.getOpcaoSalvamentoSelecionada())
				.nomeArquivo(LeitorParametrosPrograma.getOpcaoCaminhoArquivoSelecionado())
				.esfera(LeitorParametrosPrograma.getOpcaoEsferaSelecionada())
				.exercicios(LeitorParametrosPrograma.getOpcaoExerciciosSelecionados())
				.periodos(LeitorParametrosPrograma.getOpcaoPeriodosSelecionados())
				.codigosIbge(LeitorParametrosPrograma.getOpcaoCodigosIbgeSelecionados())
				.codigosUF(LeitorParametrosPrograma.getOpcaoCodigosUFSelecionados())
				.listaAnexos(LeitorParametrosPrograma.getOpcaoAnexosSelecionados())
				.capital(LeitorParametrosPrograma.getOpcaoCapitalSelecionada())
				.populacaoMinima(LeitorParametrosPrograma.getOpcaoPopulacaoMinimaSelecionada())
				.populacaoMaxima(LeitorParametrosPrograma.getOpcaoPopulacaoMaximaSelecionada())				
				.build();		
		
		getRreoService().carregarDados(opcoes);		
	}
	
	private void carregarDadosMSCPatrimonial() {		
		getMscPatrimonialService().carregarDados(getOpcoesCargaDadosMSC());		
	}

	private void carregarDadosMSCOrcamentaria() {
		getMscOrcamentariaService().carregarDados(getOpcoesCargaDadosMSC());		
	}

	private void carregarDadosMSCControle() {
		getMscControleService().carregarDados(getOpcoesCargaDadosMSC());		
	}

	private OpcoesCargaDadosMSC getOpcoesCargaDadosMSC() {
		return  new OpcoesCargaDadosMSC.Builder()
				.opcaoSalvamentoDados(LeitorParametrosPrograma.getOpcaoSalvamentoSelecionada())
				.nomeArquivo(LeitorParametrosPrograma.getOpcaoCaminhoArquivoSelecionado())
				.exercicios(LeitorParametrosPrograma.getOpcaoExerciciosSelecionados())
				.periodos(LeitorParametrosPrograma.getOpcaoPeriodosSelecionados())
				.esfera(LeitorParametrosPrograma.getOpcaoEsferaSelecionada())
				.codigosIbge(LeitorParametrosPrograma.getOpcaoCodigosIbgeSelecionados())
				.codigosUF(LeitorParametrosPrograma.getOpcaoCodigosUFSelecionados())
				.tipoMatrizSaldoContabeis(LeitorParametrosPrograma.getOpcaoTipoMatrizSelecionado())
				.listaClasseConta(LeitorParametrosPrograma.getOpcaoClassesContasSelecionadas())
				.listaTipoValor(LeitorParametrosPrograma.getOpcaoTiposValorMatrizSelecionado())
				.capital(LeitorParametrosPrograma.getOpcaoCapitalSelecionada())
				.populacaoMinima(LeitorParametrosPrograma.getOpcaoPopulacaoMinimaSelecionada())
				.populacaoMaxima(LeitorParametrosPrograma.getOpcaoPopulacaoMaximaSelecionada())				
				.build();
	}

	private EnteService getEnteService() {
		if(enteService == null) {
			enteService = new EnteService();
		}
		return enteService;
	}

	private AnexoRelatorioService getAnexoRelatorioService() {
		if(anexoRelatorioService == null) {
			anexoRelatorioService = new AnexoRelatorioService();
		}
		return anexoRelatorioService;
	}

	private ExtratoEntregaService getExtratoEntregaService() {
		if(extratoEntregaService == null) {
			extratoEntregaService = new ExtratoEntregaService();
		}
		return extratoEntregaService;
	}

	private DCAService getDCAService() {
		if(dcaService == null) {
			dcaService = new DCAService();
		}
		return dcaService;
	}

	private RGFService getRgfService() {
		if(rgfService == null) {
			rgfService = new RGFService();
		}
		return rgfService;
	}

	private RREOService getRreoService() {
		if(rreoService == null) {
			rreoService = new RREOService();
		}
		return rreoService;
	}

	private MSCPatrimonialService getMscPatrimonialService() {
		if(mscPatrimonialService == null) {
			mscPatrimonialService = new MSCPatrimonialService();
		}
		return mscPatrimonialService;
	}

	private MSCOrcamentariaService getMscOrcamentariaService() {
		if(mscOrcamentariaService == null) {
			mscOrcamentariaService = new MSCOrcamentariaService();
		}
		return mscOrcamentariaService;
	}

	private MSCControleService getMscControleService() {
		if(mscControleService == null) {
			mscControleService = new MSCControleService();
		}
		return mscControleService;
	}
}
