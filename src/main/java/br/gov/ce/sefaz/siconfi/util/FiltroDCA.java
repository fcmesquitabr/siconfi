package br.gov.ce.sefaz.siconfi.util;

import java.util.List;

public class FiltroDCA extends FiltroBase {

	private List<String> listaAnexos;

	public boolean isListaAnexosVazia() {
		return Utils.isEmptyCollection(getListaAnexos());
	}

	public List<String> getListaAnexos() {
		return listaAnexos;
	}

	public void setListaAnexos(List<String> listaAnexos) {
		this.listaAnexos = listaAnexos;
	}

}
