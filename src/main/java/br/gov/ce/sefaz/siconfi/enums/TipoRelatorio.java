package br.gov.ce.sefaz.siconfi.enums;

public enum TipoRelatorio {

	PADRAO("P","Padrao"),
	SIMPLIFICADO("S","Simplificado");
	
	private String codigo;
	private String descricao;
	
	TipoRelatorio(String codigo, String descricao){
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
