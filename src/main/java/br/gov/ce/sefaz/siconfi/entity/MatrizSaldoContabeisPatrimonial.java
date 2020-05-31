package br.gov.ce.sefaz.siconfi.entity;

import javax.persistence.Entity;

@Entity
public class MatrizSaldoContabeisPatrimonial extends MatrizSaldoContabeis {

    private Integer financeiro_permanente;
    private Integer divida_consolidada;

	@Override
	public String toString() {
		return "MatrizSaldoContabeisPatrimonial [id=" + id + ", exercicio=" + exercicio + ", mes_referencia="
				+ mes_referencia + ", cod_ibge=" + cod_ibge + ", poder_orgao=" + poder_orgao + ", tipo_matriz="
				+ tipo_matriz + ", classe_conta=" + classe_conta + ", natureza_conta=" + natureza_conta
				+ ", conta_contabil=" + conta_contabil + ", financeiro_permanente=" + financeiro_permanente
				+ ", ano_fonte_recursos=" + ano_fonte_recursos + ", fonte_recursos=" + fonte_recursos
				+ ", divida_consolidada=" + divida_consolidada + ", data_referencia=" + data_referencia
				+ ", entrada_msc=" + entrada_msc + ", tipo_valor=" + tipo_valor + ", valor=" + valor + "]";
	}
	
	public String getPoder_orgao() {
		return poder_orgao;
	}
	public void setPoder_orgao(String poder_orgao) {
		this.poder_orgao = poder_orgao;
	}
	public Integer getFinanceiro_permanente() {
		return financeiro_permanente;
	}
	public void setFinanceiro_permanente(Integer financeiro_permanente) {
		this.financeiro_permanente = financeiro_permanente;
	}
	public Integer getAno_fonte_recursos() {
		return ano_fonte_recursos;
	}
	public void setAno_fonte_recursos(Integer ano_fonte_recursos) {
		this.ano_fonte_recursos = ano_fonte_recursos;
	}
	public String getFonte_recursos() {
		return fonte_recursos;
	}
	public void setFonte_recursos(String fonte_recursos) {
		this.fonte_recursos = fonte_recursos;
	}
	public Integer getDivida_consolidada() {
		return divida_consolidada;
	}
	public void setDivida_consolidada(Integer divida_consolidada) {
		this.divida_consolidada = divida_consolidada;
	}
}
