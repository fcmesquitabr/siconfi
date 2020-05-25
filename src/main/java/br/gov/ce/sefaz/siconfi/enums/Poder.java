package br.gov.ce.sefaz.siconfi.enums;

import java.util.ArrayList;
import java.util.List;

public enum Poder {
	
	EXECUTIVO ("E", "Executivo"),
	LEGISLATIVO ("L", "Legislativo"),
	JUDICIARIO ("J", "Judiciário"),
	MINISTERIO_PUBLICO ("M", "Ministério Público"),
	DEFENSORIA_PUBLICA ("D", "Defensoria Pública");
	
	private String codigo;
	private String descricao;
	
	private Poder(String codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static Poder getPoder(String codigo) {
		for(Poder poder:Poder.values()) {
			if(poder.getCodigo().equals(codigo)) return poder;
		}
		return null;
	}

	public static List<String> getListaCodigoPoder(List<Poder> listaPoder) {
		if(listaPoder == null) return new ArrayList<String>();
		List<String> listaCodigoPoder = new ArrayList<String>();
		for(Poder poder:listaPoder) {
			listaCodigoPoder.add(poder.getCodigo());
		}
		return listaCodigoPoder;
	}

}
