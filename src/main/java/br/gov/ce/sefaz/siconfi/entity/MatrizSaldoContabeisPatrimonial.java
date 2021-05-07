package br.gov.ce.sefaz.siconfi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Table(schema = "TAB_SICONFI", name = "MATRIZ_SAL_CONT_PATRIMON")
@Entity
public class MatrizSaldoContabeisPatrimonial extends MatrizSaldoContabeis {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SEQ_MATRIZ_PATRIMONIAL")
	private Integer id;

	@Column(name = "NUM_FINANCEIRO_PERMANENTE")
    private Integer financeiro_permanente;
	
	@Column(name = "NUM_DIVIDA_CONSOLIDADA")
    private Integer divida_consolidada;

	@Column(name = "VLR_SALDO_PATRIMONIAL")
	private Double valor;
	
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFinanceiro_permanente() {
		return financeiro_permanente;
	}
	public void setFinanceiro_permanente(Integer financeiro_permanente) {
		this.financeiro_permanente = financeiro_permanente;
	}

	public Integer getDivida_consolidada() {
		return divida_consolidada;
	}
	public void setDivida_consolidada(Integer divida_consolidada) {
		this.divida_consolidada = divida_consolidada;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}
}
