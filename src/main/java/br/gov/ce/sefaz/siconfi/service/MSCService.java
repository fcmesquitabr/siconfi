package br.gov.ce.sefaz.siconfi.service;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;

import br.gov.ce.sefaz.siconfi.entity.Ente;
import br.gov.ce.sefaz.siconfi.entity.MatrizSaldoContabeis;
import br.gov.ce.sefaz.siconfi.enums.TipoMatrizSaldoContabeis;
import br.gov.ce.sefaz.siconfi.enums.TipoValorMatrizSaldoContabeis;
import br.gov.ce.sefaz.siconfi.opcoes.OpcoesCargaDadosMSC;
import br.gov.ce.sefaz.siconfi.util.APIQueryParamUtil;
import br.gov.ce.sefaz.siconfi.util.Constantes;
import br.gov.ce.sefaz.siconfi.util.Utils;

public abstract class MSCService<T extends MatrizSaldoContabeis> extends SiconfiService<T, OpcoesCargaDadosMSC> {

	private EnteService enteService;
	
	protected abstract List<Integer> getClassContas();
	
	@Override
	public int excluir(OpcoesCargaDadosMSC filtro) {
		getLogger().info("Excluindo dados do banco de dados...");
		
		StringBuilder queryBuilder = new StringBuilder("DELETE FROM " + getEntityName() + " msc WHERE msc.exercicio IN (:exercicios) ");

		List<String> listaCodigoIbge = getEnteService().obterListaCodigosIbgeNaAPI(filtro);
		if(!Utils.isEmptyCollection(listaCodigoIbge)) {
			queryBuilder.append(" AND msc.codigoIbge IN (:codigosIbge)");
		}
		if(!filtro.isListaPeriodosVazia()) {
			queryBuilder.append(" AND msc.mesReferencia IN (:listaMeses)");
		}
		if(filtro.getTipoMatrizSaldoContabeis()!=null){
			queryBuilder.append(" AND msc.tipoMatriz=:codigoTipoMatriz");
		}
		if(!filtro.isListaClassesContaVazia()) {
			queryBuilder.append(" AND msc.classeConta IN (:listaClasseConta)");
		}
		if(!filtro.isListaTipoValorVazia()) {
			queryBuilder.append(" AND msc.tipoValor IN (:listaTipoValor)");
		}
		
		boolean transacaoAtiva = getEntityManager().getTransaction().isActive(); 
		if(!transacaoAtiva) {
			getEntityManager().getTransaction().begin();			
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
		getLogger().info("Linhas exclu�das: {}", i);
		
		if(!transacaoAtiva) {
			getEntityManager().getTransaction().commit();			
		}		

		return i;
	}

	@Override
	protected void consultarNaApiEGerarSaidaDados(OpcoesCargaDadosMSC opcoes, Integer exercicio) {

		List<Integer> listaMeses = !opcoes.isListaPeriodosVazia() ? opcoes.getPeriodos() : Constantes.MESES;

		for (Integer mes: listaMeses) {
			consultarNaApiEGerarSaidaDados(opcoes, exercicio, mes);
		}
	}

	private void consultarNaApiEGerarSaidaDados(OpcoesCargaDadosMSC filtro, Integer exercicio, Integer mes) {

		TipoMatrizSaldoContabeis tipoMatriz = filtro.getTipoMatrizSaldoContabeis() != null
				? filtro.getTipoMatrizSaldoContabeis()
						: TipoMatrizSaldoContabeis.MSCC;

		List<Ente> listaEntes = getEnteService().obterListaEntesNaAPI(filtro);		

		int contadorEntes = 1;
		for (Ente ente: listaEntes) {
			getLogger().info("Consultando dados para o Ente: {} ({} de {} a serem consultados)", ente.getDescricaoEnte(), contadorEntes, listaEntes.size());
			consultarNaApiEGerarSaidaDados(filtro, exercicio, mes, ente, tipoMatriz);
			contadorEntes++;
		}
	}

	private void consultarNaApiEGerarSaidaDados(OpcoesCargaDadosMSC filtro, Integer exercicio, Integer mes,
			Ente ente, TipoMatrizSaldoContabeis tipoMatriz) {

		List<Integer> listaClassesConta = !filtro.isListaClassesContaVazia()?filtro.getListaClasseConta():getClassContas();
		
		for (Integer classe: listaClassesConta) {
			consultarNaApiEGerarSaidaDados(filtro, exercicio, mes, ente, tipoMatriz, classe);
		}
	}

	private void consultarNaApiEGerarSaidaDados(OpcoesCargaDadosMSC opcoes, Integer exercicio, Integer mes,
			Ente ente, TipoMatrizSaldoContabeis tipoMatriz, Integer classeConta) {

		List<TipoValorMatrizSaldoContabeis> listaTipoValor = !opcoes.isListaTipoValorVazia()
				? opcoes.getListaTipoValor()
				: Arrays.asList(TipoValorMatrizSaldoContabeis.values());
		
		for (TipoValorMatrizSaldoContabeis tipoValor: listaTipoValor) {
			APIQueryParamUtil apiQueryParamUtil = new APIQueryParamUtil();
			apiQueryParamUtil.addParamAnReferencia(exercicio)
					.addParamMesReferencia(mes)
					.addParamIdEnte(ente.getCodigoIbge())
					.addParamClasseConta(classeConta)
					.addParamTipoMatriz(tipoMatriz.getCodigo())
					.addParamTipoValorMatriz(tipoValor.getCodigo());

			List<T> listaMSCParcial = consultarNaApi(apiQueryParamUtil);
			gerarSaidaDados(getOpcoesParcial(opcoes, exercicio, mes, ente, classeConta, tipoMatriz, tipoValor),
					listaMSCParcial);
		}
	}

	private OpcoesCargaDadosMSC getOpcoesParcial(OpcoesCargaDadosMSC opcoes, Integer exercicio, Integer mes,
			Ente ente, Integer classeConta, TipoMatrizSaldoContabeis tipoMatriz,
			TipoValorMatrizSaldoContabeis tipoValor) {
		OpcoesCargaDadosMSC opcoesParcial = new OpcoesCargaDadosMSC();
		opcoesParcial.setOpcaoSalvamento(opcoes.getOpcaoSalvamento());
		opcoesParcial.setNomeArquivo(opcoes.getNomeArquivo());
		opcoesParcial.setExercicios(Arrays.asList(exercicio));
		opcoesParcial.setPeriodos(Arrays.asList(mes));
		opcoesParcial.setCodigosIBGE(Arrays.asList(ente.getCodigoIbge()));
		opcoesParcial.setListaClasseConta(Arrays.asList(classeConta));
		opcoesParcial.setTipoMatrizSaldoContabeis(tipoMatriz);
		opcoesParcial.setListaTipoValor(Arrays.asList(tipoValor));
		return opcoesParcial;
	}

	protected EnteService getEnteService() {
		if(enteService == null) {
			enteService = new EnteService();
		}
		return enteService;
	}
}
