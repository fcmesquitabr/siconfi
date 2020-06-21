package br.gov.ce.sefaz.siconfi.enums;

public enum Entregavel {

	RREO ("RREO", "Relatório Resumido de Execução Orçamentária"),
	RGF ("RGF", "Relatório de Gestão Fiscal"),
	DCA ("DCA", "Balanço Anual (DCA)"),
	MSC_AGREGADA ("MSC_AGREGADA", "MSC Agregada"),
	MSC_ENCERRAMENTO ("MSC_ENCERRAMENTO", "MSC Encerramento");
	
	private String codigo; 
	private String descricao;
	
	private Entregavel (String codigo, String descricao) {
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
