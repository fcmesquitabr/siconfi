package br.gov.ce.sefaz.siconfi.util;

import java.util.List;

import br.gov.ce.sefaz.siconfi.enums.Esfera;
import br.gov.ce.sefaz.siconfi.enums.OpcaoSalvamentoDados;
import br.gov.ce.sefaz.siconfi.enums.Poder;

public class FiltroRGF {
	
	private List<Integer> exercicios;
	
	private List<Integer> quadrimestres;
	
	private List<String> listaAnexos;
	
	private List<Poder> listaPoderes;
	
	private Esfera esfera;
	
	private List<String> codigosIBGE;

	private String nomeArquivo;

	private OpcaoSalvamentoDados opcaoSalvamento;

	public List<Integer> getExercicios() {
		return exercicios;
	}

	public void setExercicios(List<Integer> exercicios) {
		this.exercicios = exercicios;
	}

	public List<Integer> getQuadrimestres() {
		return quadrimestres;
	}

	public void setQuadrimestres(List<Integer> quadrimestres) {
		this.quadrimestres = quadrimestres;
	}

	public List<String> getListaAnexos() {
		return listaAnexos;
	}

	public void setListaAnexos(List<String> listaAnexos) {
		this.listaAnexos = listaAnexos;
	}

	public List<Poder> getListaPoderes() {
		return listaPoderes;
	}

	public void setListaPoderes(List<Poder> listaPoderes) {
		this.listaPoderes = listaPoderes;
	}

	public Esfera getEsfera() {
		return esfera;
	}

	public void setEsfera(Esfera esfera) {
		this.esfera = esfera;
	}

	public List<String> getCodigosIBGE() {
		return codigosIBGE;
	}

	public void setCodigosIBGE(List<String> codigosIBGE) {
		this.codigosIBGE = codigosIBGE;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public OpcaoSalvamentoDados getOpcaoSalvamento() {
		return opcaoSalvamento;
	}

	public void setOpcaoSalvamento(OpcaoSalvamentoDados opcaoSalvamento) {
		this.opcaoSalvamento = opcaoSalvamento;
	}
}
