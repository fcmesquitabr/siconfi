package br.gov.ce.sefaz.siconfi.enums;

public enum Periodicidade {

	MENSAL("M","Mensal"),
	BIMESTRAL ("B","Bimestral"),
	QUADRIMESTRAL ("Q","Quadrimestral"),
	SEMESTRAL ("S","Semestral"),
	ANUAL ("A","Anual");
	
	private String codigo;
	private String descricao;
	
	Periodicidade(String codigo, String descricao){
		this.codigo=codigo;
		this.descricao=descricao;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}				
}
