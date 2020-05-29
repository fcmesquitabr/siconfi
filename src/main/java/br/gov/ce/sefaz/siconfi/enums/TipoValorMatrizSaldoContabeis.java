package br.gov.ce.sefaz.siconfi.enums;

public enum TipoValorMatrizSaldoContabeis {

	beginning_balance ("beginning_balance", "Saldo Inicial"),
	ending_balance ("ending_balance", "Saldo Final"),
	period_change ("period_change", "Movimento");
	
	private String codigo;
	private String descricao;
	
	private TipoValorMatrizSaldoContabeis(String codigo, String descricao) {
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
