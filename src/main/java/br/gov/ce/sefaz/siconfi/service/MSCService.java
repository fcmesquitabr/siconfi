package br.gov.ce.sefaz.siconfi.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;

import br.gov.ce.sefaz.siconfi.entity.MatrizSaldoContabeis;
import br.gov.ce.sefaz.siconfi.enums.TipoMatrizSaldoContabeis;
import br.gov.ce.sefaz.siconfi.enums.TipoValorMatrizSaldoContabeis;
import br.gov.ce.sefaz.siconfi.opcoes.OpcoesCargaDadosMSC;
import br.gov.ce.sefaz.siconfi.util.APIQueryParamUtil;
import br.gov.ce.sefaz.siconfi.util.Constantes;
import br.gov.ce.sefaz.siconfi.util.Utils;

public abstract class MSCService<T extends MatrizSaldoContabeis> extends SiconfiService<T> {

	private EnteService enteService;
	
	public MSCService() {
		super();
	}

	protected abstract String getEntityName();

	protected abstract List<Integer> getClassContas();

	public void carregarDados(OpcoesCargaDadosMSC opcoes) {
		
		List<T> listaMSC = consultarNaApi(opcoes);	
		
		switch (opcoes.getOpcaoSalvamento()) {
		case CONSOLE:
			exibirDadosNaConsole(listaMSC);
			break;
		case ARQUIVO:
			String nomeArquivo = definirNomeArquivoCSV(opcoes);
			escreverCabecalhoArquivoCsv(nomeArquivo);
			salvarArquivoCsv(listaMSC, nomeArquivo);
			break;
		case BANCO:
			salvarNoBancoDeDados(opcoes, listaMSC);
			break;
		}
	}

	protected void salvarNoBancoDeDados(OpcoesCargaDadosMSC opcoes, List<T> listaEntidades) {
		
		if(Utils.isEmptyCollection(listaEntidades)) return;
		
		getEntityManager().getTransaction().begin();		
		excluirMatrizSaldosContabeis(opcoes);
		persistir(listaEntidades);
		commitTransaction();
		fecharContextoPersistencia();		
	}

	private void excluirMatrizSaldosContabeis(OpcoesCargaDadosMSC filtro) {
		getLogger().info("Excluindo dados do banco de dados...");
		
		StringBuilder queryBuilder = new StringBuilder("DELETE FROM " + getEntityName() + " msc WHERE msc.exercicio IN (:exercicios) ");

		List<String> listaCodigoIbge = getEnteService().obterListaCodigosIbge(filtro);
		if(!Utils.isEmptyCollection(listaCodigoIbge)) {
			queryBuilder.append(" AND msc.cod_ibge IN (:codigosIbge)");
		}
		if(!filtro.isListaPeriodosVazia()) {
			queryBuilder.append(" AND msc.mes_referencia IN (:listaMeses)");
		}
		if(filtro.getTipoMatrizSaldoContabeis()!=null){
			queryBuilder.append(" AND msc.tipo_matriz=:codigoTipoMatriz");
		}
		if(!filtro.isListaClassesContaVazia()) {
			queryBuilder.append(" AND msc.classe_conta IN (:listaClasseConta)");
		}
		if(!filtro.isListaTipoValorVazia()) {
			queryBuilder.append(" AND msc.tipo_valor IN (:listaTipoValor)");
		}
		
		Query query = getEntityManager().createQuery(queryBuilder.toString());
		query.setParameter("exercicios", filtro.getExercicios());
		
		if(!Utils.isEmptyCollection(listaCodigoIbge)) {
			query.setParameter("codigosIbge", listaCodigoIbge);
		}
		if(!filtro.isListaPeriodosVazia()) {
			query.setParameter("listaMeses", filtro.getPeriodos());
		}
		if(filtro.getTipoMatrizSaldoContabeis()!=null){
			query.setParameter("codigoTipoMatriz", filtro.getTipoMatrizSaldoContabeis().getCodigo());
		}
		if(!filtro.isListaClassesContaVazia()) {
			query.setParameter("listaClasseConta", filtro.getListaClasseConta());
		}
		if(!filtro.isListaTipoValorVazia()) {
			query.setParameter("listaTipoValor", filtro.getListaCodigoTipoValor());
		}

		int i = query.executeUpdate();
		getLogger().info("Linhas excluídas:" + i);
	}

	@Override
	public void excluirTodos() {
		getLogger().info("Excluindo dados do banco de dados...");
		int i = getEntityManager().createQuery("DELETE FROM " + getEntityName() + " mscp")
				.executeUpdate();
		getLogger().info("Linhas excluídas:" + i);
	}

	public List<T> consultarNaApi() {
		return new ArrayList<T>();
	}

	public List<T> consultarNaApi(OpcoesCargaDadosMSC opcoes) {

		List<Integer> listaExercicios = !opcoes.isListaExerciciosVazia() ? opcoes.getExercicios()
				: Constantes.EXERCICIOS_DISPONIVEIS;
		List<T> listaMSC = new ArrayList<>();

		for (Integer exercicio : listaExercicios) {
			listaMSC.addAll(consultarNaApi(opcoes, exercicio));
		}
		return listaMSC;
	}

	private List<T> consultarNaApi(OpcoesCargaDadosMSC opcoes, Integer exercicio) {

		List<Integer> listaMeses = !opcoes.isListaPeriodosVazia() ? opcoes.getPeriodos() : Constantes.MESES;
		List<T> listaMSC = new ArrayList<>();

		for (Integer mes: listaMeses) {
			listaMSC.addAll(consultarNaApi(opcoes, exercicio, mes));
		}
		return listaMSC;
	}

	private List<T> consultarNaApi(OpcoesCargaDadosMSC filtro, Integer exercicio, Integer mes) {

		List<String> listaCodigoIbge = getEnteService().obterListaCodigosIbge(filtro);
		TipoMatrizSaldoContabeis tipoMatriz = filtro.getTipoMatrizSaldoContabeis() != null
				? filtro.getTipoMatrizSaldoContabeis()
				: TipoMatrizSaldoContabeis.MSCC;

		List<T> listaMSC = new ArrayList<>();		
		for (String codigoIbge : listaCodigoIbge) {
			listaMSC.addAll(consultarNaApi(filtro, exercicio, mes, codigoIbge, tipoMatriz));
		}
		return listaMSC;
	}

	private List<T> consultarNaApi(OpcoesCargaDadosMSC filtro, Integer exercicio, Integer mes,
			String codigoIbge, TipoMatrizSaldoContabeis tipoMatriz) {

		List<Integer> listaClassesConta = !filtro.isListaClassesContaVazia()?filtro.getListaClasseConta():getClassContas();
		
		List<T> listaMSC = new ArrayList<>();
		for (Integer classe: listaClassesConta) {
			listaMSC.addAll(consultarNaApi(filtro, exercicio, mes, codigoIbge, tipoMatriz, classe));
		}
		return listaMSC;
	}

	private List<T> consultarNaApi(OpcoesCargaDadosMSC filtro, Integer exercicio, Integer mes,
			String codigoIbge, TipoMatrizSaldoContabeis tipoMatriz, Integer classeConta) {

		List<TipoValorMatrizSaldoContabeis> listaTipoValor = !filtro.isListaTipoValorVazia()
				? filtro.getListaTipoValor()
				: Arrays.asList(TipoValorMatrizSaldoContabeis.values());
		
		List<T> listaMSC = new ArrayList<>();
		for (TipoValorMatrizSaldoContabeis tipoValor: listaTipoValor) {
			APIQueryParamUtil apiQueryParamUtil = new APIQueryParamUtil();
			apiQueryParamUtil.addParamAnReferencia(exercicio)
					.addParamMesReferencia(mes)
					.addParamIdEnte(codigoIbge)
					.addParamClasseConta(classeConta)
					.addParamTipoMatriz(tipoMatriz.getCodigo())
					.addParamTipoValorMatriz(tipoValor.getCodigo());

			listaMSC.addAll(consultarNaApi(apiQueryParamUtil));
			aguardarUmSegundo();
		}
		return listaMSC;
	}
		
	protected EnteService getEnteService() {
		if(enteService == null) {
			enteService = new EnteService();
		}
		return enteService;
	}
}
