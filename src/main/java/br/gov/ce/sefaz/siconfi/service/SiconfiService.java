package br.gov.ce.sefaz.siconfi.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.logging.log4j.Logger;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import br.gov.ce.sefaz.siconfi.enums.OpcaoSalvamentoDados;
import br.gov.ce.sefaz.siconfi.opcoes.OpcoesCargaDados;
import br.gov.ce.sefaz.siconfi.util.APIQueryParamUtil;
import br.gov.ce.sefaz.siconfi.util.Constantes;
import br.gov.ce.sefaz.siconfi.util.ConsultaApiUtil;
import br.gov.ce.sefaz.siconfi.util.CsvUtil;
import br.gov.ce.sefaz.siconfi.util.Utils;

public abstract class SiconfiService <T, O extends OpcoesCargaDados> {
		
	public static final String URL_SERVICE = "http://apidatalake.tesouro.gov.br/ords/siconfi/tt/";

	public static final String API_RESPONSE_TYPE = "application/json;charset=UTF-8";
	
	protected CsvUtil<T> csvUtil;

	protected ConsultaApiUtil<T> consultaApiUtil = new ConsultaApiUtil<>(URL_SERVICE, getApiPath(), API_RESPONSE_TYPE);

	private EntityManagerFactory emf;
	
	private EntityManager em;
	
	public SiconfiService(){
		csvUtil = new CsvUtil<>(getEntityClass());
	}
	
	protected abstract String getNomePadraoArquivoCSV();

	protected abstract String[] getColunasArquivoCSV();

	protected abstract Class<T> getEntityClass();

	protected abstract String getEntityName();

	protected abstract Logger getLogger();
	
	protected abstract String getApiPath();
	
	public abstract int excluir(O opcoes);

	protected ConsultaApiUtil<T> getConsultaApiUtil(){
		return consultaApiUtil;
	}

	public void carregarDados(O opcoes) {
		try {
			escreverCabecalhoArquivoCsv(opcoes);
		} catch (IOException e) {
			getLogger().error("Erro ao escrever o cabeçalho do arquivo");
			e.printStackTrace();
		}			
		consultarNaApiEGerarSaidaDados(opcoes);
		fecharContextoPersistencia();
	}

	protected void exibirDadosNaConsole (List<T> listaEntidades) {
		for (T entidade: listaEntidades) {
			System.out.println(entidade.toString());
		}
	}

	protected String definirNomeArquivoCSV(OpcoesCargaDados opcoes) {
		return !opcoes.isNomeArquivoVazio() ? opcoes.getNomeArquivo() : getNomePadraoArquivoCSV();
	}

	protected void escreverCabecalhoArquivoCsv(OpcoesCargaDados opcoes) throws IOException { 
		if(OpcaoSalvamentoDados.ARQUIVO.equals(opcoes.getOpcaoSalvamento())) {
			getLogger().info("Escrevendo o cabeçalho no arquivo CSV...");
			csvUtil.writeHeader(getColunasArquivoCSV(), definirNomeArquivoCSV(opcoes));
		}
	}

	protected void salvarArquivoCsv(List<T> listaObjetos, String nomeArquivo) {
		if(Utils.isEmptyCollection(listaObjetos)) {
			getLogger().info("Lista de registro fazia. Nada a salvar no arquivo CSV...");
			return;
		}

		try {
			getLogger().info("Salvando {} registro(s) no arquivo CSV...", listaObjetos.size());
			csvUtil.writeToFile(listaObjetos, getColunasArquivoCSV(), nomeArquivo);
		} catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException | IOException e) {
			getLogger().error("Erro ao salvar conteúdo no arquivo");
			getLogger().error(e);
		}			
	}

	protected void consultarNaApiEGerarSaidaDados(O opcoes){
		for (Integer exercicio : getExercicios(opcoes)) {
			getLogger().info("Consultando dados para o exercício: {}", exercicio);
			consultarNaApiEGerarSaidaDados(opcoes, exercicio);
		}
	}
	
	private List<Integer> getExercicios (O opcoes){
		return (opcoes.getExercicios() != null ? opcoes.getExercicios() : Constantes.EXERCICIOS_DISPONIVEIS);
	}
	
	protected void consultarNaApiEGerarSaidaDados(O opcoes, Integer exercicio) {	
		List<T> listaEntidades = consultarNaApi(new APIQueryParamUtil());
		gerarSaidaDados(opcoes, listaEntidades);			
	}
	
	protected void gerarSaidaDados(O opcoes, List<T> listaEntidades) {
		switch (opcoes.getOpcaoSalvamento()) {
		case CONSOLE:
			exibirDadosNaConsole(listaEntidades);
			break;
		case ARQUIVO:
			salvarArquivoCsv(listaEntidades, definirNomeArquivoCSV(opcoes));
			break;
		case BANCO:
			salvarNoBancoDeDados(opcoes, listaEntidades);
			break;
		}
	}

	protected void salvarNoBancoDeDados(O opcoes, List<T> listaEntidades) {
		if(Utils.isEmptyCollection(listaEntidades)) {
			getLogger().info("Sem dados para persistir");
			return;
		}
	
		getEntityManager().getTransaction().begin();
		excluir(opcoes);
		persistir(listaEntidades);
		getEntityManager().flush();
		commitTransaction();
	}

	public int excluirTodos() {
		getLogger().info("Excluindo dados do banco de dados...");
		boolean transacaoAtiva = getEntityManager().getTransaction().isActive();
		if(!transacaoAtiva) {
			getEntityManager().getTransaction().begin();
		}
		int i = getEntityManager().createQuery("DELETE FROM " + getEntityName()).executeUpdate();
		if(!transacaoAtiva) {
			getEntityManager().getTransaction().commit();
		}
		getLogger().info("Linhas excluídas: {}", i);
		return i;
	}

	public EntityManager getEntityManager () {
		if((em == null || !em.isOpen()) && getEntityManagerFactory()!=null) {
			getLogger().info("Criando EntityManager {}", em);
			em = getEntityManagerFactory().createEntityManager();
		}
		return em;
	}
	
	private EntityManagerFactory getEntityManagerFactory() {
		if(this.emf == null || !this.emf.isOpen()) {
			getLogger().info("Criando EntityManagerFactory {}", emf);
			this.emf = Persistence.createEntityManagerFactory("siconfiUnit");
		}
		return emf;
	}
	
	protected void fecharContextoPersistencia() {
		if (em != null) {
			getLogger().info("Fechando o EntityManager");
			em.close();
		}
		if (emf != null) {
			getLogger().info("Fechando o EntityManagerFactory");
			emf.close();
		}
	}
	
	protected void commitTransaction() {
		getLogger().info("Fazendo commit...");
		long ini = System.currentTimeMillis();
		getEntityManager().getTransaction().commit();
		long fim = System.currentTimeMillis();
		getLogger().info("Tempo para commit: {}", (fim -ini));
	}

	protected void persistir(List<T> lista) {
		if(Utils.isEmptyCollection(lista)) {
			getLogger().info("Sem dados para persistir");
			return;
		}
		int i=1;
		getLogger().info("Persistindo os dados obtidos ({}  registro(s))...", lista.size());
		for(T entity: lista) {
			getLogger().debug("Inserindo registro {}: {}", (i++), entity);
			getEntityManager().merge(entity);
		}
	}

	public List<T> consultarNaApi (APIQueryParamUtil apiQueryParamUtil) {

		List<T> listaEntidades = null;		
		try {

			listaEntidades = getConsultaApiUtil().lerEntidades(apiQueryParamUtil, getEntityClass());
			getLogger().info("Quantidade de registros retornados da API: {}", listaEntidades.size());
			
		} catch (Exception e) {
			mensagemLogErroConsultaNaAPI(apiQueryParamUtil);
			e.printStackTrace();
			listaEntidades =  new ArrayList<>();
		}
		
		mensagemLogTamanhoDaLista(apiQueryParamUtil, listaEntidades);		
		return listaEntidades;
	}

	private void mensagemLogErroConsultaNaAPI(APIQueryParamUtil apiQueryParamUtil) {
		StringBuilder mensagemLogBuilder = new StringBuilder("Erro de consulta na API para os parâmetros: ");
		apiQueryParamUtil.getMapQueryParam().forEach((chave, valor) -> mensagemLogBuilder.append(chave + ": " + valor.toString() + ", "));
		String mensagemLog = mensagemLogBuilder.toString();
		getLogger().error(mensagemLog);
	}
	
	private void mensagemLogTamanhoDaLista(APIQueryParamUtil apiQueryParamUtil, List<T> listaEntidades) {
		StringBuilder mensagemLog = new StringBuilder("Tamanho da lista de entidades para os parâmetros ");
		apiQueryParamUtil.getMapQueryParam().forEach((chave, valor) -> mensagemLog.append(chave + ": " + valor.toString() + ", "));
		mensagemLog.append(": " + listaEntidades.size());
		getLogger().debug(mensagemLog);
	}	
}
