package br.gov.ce.sefaz.siconfi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.ce.sefaz.siconfi.util.Utils;

@JsonIgnoreProperties(ignoreUnknown = true)
@Table(schema = "TAB_SICONFI", name = "DECLARACAO_CONTA_ANUAL")
@Entity
public class DeclaracaoContasAnuais {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SEQ_DECLARACAO")
	private Integer id;

	@Column(name = "NUM_EXERCICIO")
	private Integer exercicio;
	
	@Column(name = "DSC_INSTITUICAO")
	private String instituicao;
	
	@Column(name = "COD_IBGE")
	@JsonProperty("cod_ibge")
	private String codigoIbge;
	
	@Column(name = "COD_UF")
	private String uf;
	
	@Column(name = "TXT_ANEXO")
	private String anexo;
	
	@Column(name = "TXT_ROTULO")
	private String rotulo;
	
	@Column(name = "TXT_COLUNA")
	private String coluna;
	
	@Column(name = "COD_CONTA")
	@JsonProperty("cod_conta")
	private String codigoConta;
	
	@Column(name = "DSC_CONTA")
	@JsonProperty("conta")
	private String descricaoConta;
	
	@Column(name = "VLR_DECLARACAO_ANUAL")
	private Double valor;
	
	@Column(name = "QTD_POPULACAO")
	private Long populacao;
	
	@Transient
	private String valorFormatado;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getExercicio() {
		return exercicio;
	}
	public void setExercicio(Integer exercicio) {
		this.exercicio = exercicio;
	}
	public String getInstituicao() {
		return instituicao;
	}
	public void setInstituicao(String instituicao) {
		this.instituicao = instituicao;
	}
	public String getCodigoIbge() {
		return codigoIbge;
	}
	public void setCodigoIbge(String codigoIbge) {
		this.codigoIbge = codigoIbge;
	}
	public String getUf() {
		return uf;
	}
	public void setUf(String uf) {
		this.uf = uf;
	}
	public String getAnexo() {
		return anexo;
	}
	public void setAnexo(String anexo) {
		this.anexo = anexo;
	}
	public String getRotulo() {
		return rotulo;
	}
	public void setRotulo(String rotulo) {
		this.rotulo = rotulo;
	}
	public String getColuna() {
		return coluna;
	}
	public void setColuna(String coluna) {
		this.coluna = Utils.removerQuebrasLinha(coluna);
	}
	public String getCodigoConta() {
		return codigoConta;
	}
	public void setCodigoConta(String codigoConta) {
		this.codigoConta = codigoConta;
	}
	public String getDescricaoConta() {
		return descricaoConta;
	}
	public void setDescricaoConta(String conta) {
		this.descricaoConta = Utils.removerQuebrasLinha(conta);
	}
	public Double getValor() {
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}
	public String getValorFormatado() {
		if(valorFormatado == null) valorFormatado = Utils.getValorFormatado(valor);
		return valorFormatado;
	}
	public void setValorFormatado(String valorFormatado) {
		this.valorFormatado = valorFormatado;
	}
	public Long getPopulacao() {
		return populacao;
	}
	public void setPopulacao(Long populacao) {
		this.populacao = populacao;
	}

	@Override
	public String toString() {
		return "DeclaracaoContasAnuais [id=" + id + ", exercicio=" + exercicio + ", instituicao=" + instituicao
				+ ", cod_ibge=" + codigoIbge + ", uf=" + uf + ", anexo=" + anexo + ", rotulo=" + rotulo + ", coluna="
				+ coluna + ", cod_conta=" + codigoConta + ", conta=" + descricaoConta + ", valor=" + getValorFormatado() + ", populacao="
				+ populacao + "]";
	}
}
