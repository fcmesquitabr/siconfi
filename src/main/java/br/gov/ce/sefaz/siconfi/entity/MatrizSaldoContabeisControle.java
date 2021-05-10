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
@Table(schema = "TAB_SICONFI", name = "MATRIZ_SAL_CONT_CONTROLE")
@Entity
public class MatrizSaldoContabeisControle extends MatrizSaldoContabeis {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SEQ_MATRIZ_CONTROLE")
	private Integer id;

	@Column(name = "DSC_FUNCAO")
    private String funcao;
	
	@Column(name = "DSC_SUBFUNCAO")
    private String subfuncao;
	
	@Column(name = "NUM_EDUCACAO_SAUDE")
	@JsonProperty("educacao_saude")
    private Integer educacaoSaude;
	
	@Column(name = "DSC_NATUREZA_DESPESA")
	@JsonProperty("natureza_despesa")
    private String naturezaDespesa;
	
	@Column(name = "NUM_ANO_INSCRICAO")
	@JsonProperty("ano_inscricao")
    private Integer anoInscricao;

	@Column(name = "VLR_SALDO_CONTROLE")
	private Double valor;

	@Override
	public String toString() {
		return "MatrizSaldoContabeisControle [id=" + id + ", exercicio=" + exercicio + ", mes_referencia="
				+ mesReferencia + ", cod_ibge=" + codigoIbge + ", poder_orgao=" + poderOrgao + ", tipo_matriz="
				+ tipoMatriz + ", classe_conta=" + classeConta + ", natureza_conta=" + naturezaConta
				+ ", conta_contabil=" + contaContabil + ", ano_fonte_recursos=" + anoFonteRecursos
				+ ", fonte_recursos=" + fonteRecursos + ", funcao=" + funcao + ", subfuncao=" + subfuncao
				+ ", educacao_saude=" + educacaoSaude + ", natureza_despesa=" + naturezaDespesa + ", ano_inscricao="
				+ anoInscricao + ", data_referencia=" + dataReferencia + ", entrada_msc=" + entradaMsc
				+ ", tipo_valor=" + tipoValor + ", valor=" + valor + "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFuncao() {
		return funcao;
	}

	public void setFuncao(String funcao) {
		this.funcao = funcao;
	}

	public String getSubfuncao() {
		return subfuncao;
	}

	public void setSubfuncao(String subfuncao) {
		this.subfuncao = subfuncao;
	}

	public Integer getEducacaoSaude() {
		return educacaoSaude;
	}

	public void setEducacaoSaude(Integer educacaoSaude) {
		this.educacaoSaude = educacaoSaude;
	}

	public String getNaturezaDespesa() {
		return naturezaDespesa;
	}

	public void setNaturezaDespesa(String naturezaDespesa) {
		this.naturezaDespesa = naturezaDespesa;
	}

	public Integer getAnoInscricao() {
		return anoInscricao;
	}

	public void setAnoInscricao(Integer anoInscricao) {
		this.anoInscricao = anoInscricao;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}
}
