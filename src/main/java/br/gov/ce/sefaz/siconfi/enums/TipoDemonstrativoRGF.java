package br.gov.ce.sefaz.siconfi.enums;

public enum TipoDemonstrativoRGF {

	RGF("RGF","RGF"),
	RGF_SIMPLIFICADO("RGF Simplificado","RGF Simplificado");
	
	private String codigo;
	private String descricao;
	
	TipoDemonstrativoRGF(String codigo, String descricao){
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
