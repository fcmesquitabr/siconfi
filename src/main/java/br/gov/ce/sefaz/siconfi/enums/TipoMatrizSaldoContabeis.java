package br.gov.ce.sefaz.siconfi.enums;

public enum TipoMatrizSaldoContabeis {
	
	MSCC ("MSCC","Matriz de Saldo Cont�beis Mensal ou Agregada"), 
	MSCE ("MSCE","Matriz de Saldo Cont�beis de Encerramento do Exerc�cio");
	
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
