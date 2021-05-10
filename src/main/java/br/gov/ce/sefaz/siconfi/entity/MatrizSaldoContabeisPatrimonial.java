package br.gov.ce.sefaz.siconfi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@Table(schema = "TAB_SICONFI", name = "MATRIZ_SAL_CONT_PATRIMON")
@Entity
public class MatrizSaldoContabeisPatrimonial extends MatrizSaldoContabeis {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SEQ_MATRIZ_PATRIMONIAL")
	private Integer id;

	@Column(name = "NUM_FINANCEIRO_PERMANENTE")
	@JsonProperty("financeiro_permanente")
    private Integer financeiroPermanente;
	
	@Column(name = "NUM_DIVIDA_CONSOLIDADA")
	@JsonProperty("divida_consolidada")
    private Integer dividaConsolidada;

	@Column(name = "VLR_SALDO_PATRIMONIAL")
	private Double valor;
	
	@Override
	public String toString() {
		return "MatrizSaldoContabeisPatrimonial [id=" + id + ", exercicio=" + exercicio + ", mes_referencia="
				+ mesReferencia + ", cod_ibge=" + codigoIbge + ", poder_orgao=" + poderOrgao + ", tipo_matriz="
				+ tipoMatriz + ", classe_conta=" + classeConta + ", natureza_conta=" + naturezaConta
				+ ", conta_contabil=" + contaContabil + ", financeiro_permanente=" + financeiroPermanente
				+ ", ano_fonte_recursos=" + anoFonteRecursos + ", fonte_recursos=" + fonteRecursos
				+ ", divida_consolidada=" + dividaConsolidada + ", data_referencia=" + dataReferencia
				+ ", entrada_msc=" + entradaMsc + ", tipo_valor=" + tipoValor + ", valor=" + valor + "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFinanceiroPermanente() {
		return financeiroPermanente;
	}
	public void setFinanceiroPermanente(Integer financeiroPermanente) {
		this.financeiroPermanente = financeiroPermanente;
	}

	public Integer getDividaConsolidada() {
		return dividaConsolidada;
	}
	public void setDividaConsolidada(Integer dividaConsolidada) {
		this.dividaConsolidada = dividaConsolidada;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}
}
