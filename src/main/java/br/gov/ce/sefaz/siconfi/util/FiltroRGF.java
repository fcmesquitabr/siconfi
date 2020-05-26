package br.gov.ce.sefaz.siconfi.util;

import java.util.List;

import br.gov.ce.sefaz.siconfi.enums.Poder;

public class FiltroRGF extends FiltroBase {
	
	private List<Integer> quadrimestres;
	
	private List<String> listaAnexos;
	
	private List<Poder> listaPoderes;
	
	public boolean isListaAnexosVazia() {
		return Utils.isEmptyCollection(getListaAnexos());
	}

	public boolean isListaPoderesVazia() {
		return Utils.isEmptyCollection(getListaPoderes());
	}

	public boolean isListaQuadrimestresVazia() {
		return Utils.isEmptyCollection(getQuadrimestres());
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
}
