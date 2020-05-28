package br.gov.ce.sefaz.siconfi.util;

import java.util.List;

public class FiltroRREO extends FiltroBase {
	
	private List<Integer> semestres;
	
	private List<String> listaAnexos;
	
	public boolean isListaAnexosVazia() {
		return Utils.isEmptyCollection(getListaAnexos());
	}

	public boolean isListaSemestresVazia() {
		return Utils.isEmptyCollection(getSemestres());
	}

	public List<String> getListaAnexos() {
		return listaAnexos;
	}

	public void setListaAnexos(List<String> listaAnexos) {
		this.listaAnexos = listaAnexos;
	}

	public List<Integer> getSemestres() {
		return semestres;
	}

	public void setSemestres(List<Integer> semestres) {
		this.semestres = semestres;
	}
}
