package br.gov.ce.sefaz.siconfi.enums;

public enum StatusRelatorio {

	HOMOLOGADO("HO","Homologado"),
	RETIFICADO("RE","Retificado");
	
	private String codigo;
	private String descricao;
	
	StatusRelatorio(String codigo, String descricao){
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
