package br.gov.ce.sefaz.siconfi.enums;

public enum FormaEnvioRelatorio {

	PLANILHA("P","Planilha"),
	INSTANCIA_XML("I","Instância XML"),
	MATRIZ("M","Gerado a partir da matriz"),
	FORMULARIO_WEB("F","Formulário Web"),
	MATRIZ_CSV("CSV","Matriz em formato CSV"),
	MATRIZ_XML("XML","Matriz em formato XML");
	
	private String codigo;
	private String descricao;
	
	FormaEnvioRelatorio(String codigo, String descricao){
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
