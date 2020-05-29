package br.gov.ce.sefaz.siconfi.enums;

public enum TipoMatrizSaldoContabeis {
	
	MSCC ("MSCC","Matriz de Saldo Contábeis Mensal ou Agregada"), 
	MSCE ("MSCE","Matriz de Saldo Contábeis de Encerramento do Exercício");
	
	private String codigo;
	private String descricao;
	
	private TipoMatrizSaldoContabeis(String codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}
	
	public String getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}
}
