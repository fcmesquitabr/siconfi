package br.gov.ce.sefaz.siconfi.enums;

public enum TipoDemonstrativoRREO {

	RREO("RREO","RREO"),
	RREO_SIMPLIFICADO("RREO Simplificado","RREO Simplificado");
	
	private String codigo;
	private String descricao;
	
	TipoDemonstrativoRREO(String codigo, String descricao){
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
