package br.gov.ce.sefaz.siconfi.enums;

public enum Esfera {

	MUNICIPIO ("M"),
	ESTADO ("E"),
	UNIAO ("U"),
	DISTRITO_FEDERAL ("D"),
	ESTADOS_E_DISTRITO_FEDERAL ("ED");
	
	private String codigo;
	
	private Esfera (String codigo) {
		this.codigo = codigo;
	}

	public String getCodigo() {
		return codigo;
	}
	
	public static Esfera getEsfera(String codigo) {
		for(Esfera esfera:Esfera.values()) {
			if(esfera.getCodigo().equals(codigo)) return esfera;
		}
		return null;
	}
}
