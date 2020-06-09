package br.gov.ce.sefaz.siconfi.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.gov.ce.sefaz.siconfi.util.Utils;

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
	private String cod_ibge;
	
	@Column(name = "COD_UF")
	private String uf;
	
	@Column(name = "TXT_ANEXO")
	private String anexo;
	
	@Column(name = "TXT_ROTULO")
	private String rotulo;
	
	@Column(name = "TXT_COLUNA")
	private String coluna;
	
	@Column(name = "COD_CONTA")
	private String cod_conta;
	
	@Column(name = "DSC_CONTA")
	private String conta;
	
	@Column(name = "VLR_DECLARACAO_ANUAL")
	private Double valor;
	
	@Column(name = "QTD_POPULACAO")
	private Long populacao;
	
	@Column(name = "DAT_ALTERACAO")
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date dataHoraAlteracao;

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
	public String getCod_ibge() {
		return cod_ibge;
	}
	public void setCod_ibge(String cod_ibge) {
		this.cod_ibge = cod_ibge;
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
	public String getCod_conta() {
		return cod_conta;
	}
	public void setCod_conta(String cod_conta) {
		this.cod_conta = cod_conta;
	}
	public String getConta() {
		return conta;
	}
	public void setConta(String conta) {
		this.conta = Utils.removerQuebrasLinha(conta);
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
	
	public Date getDataHoraAlteracao() {
		return dataHoraAlteracao;
	}
	public void setDataHoraAlteracao(Date dataHoraAlteracao) {
		this.dataHoraAlteracao = dataHoraAlteracao;
	}
	@Override
	public String toString() {
		return "DeclaracaoContasAnuais [id=" + id + ", exercicio=" + exercicio + ", instituicao=" + instituicao
				+ ", cod_ibge=" + cod_ibge + ", uf=" + uf + ", anexo=" + anexo + ", rotulo=" + rotulo + ", coluna="
				+ coluna + ", cod_conta=" + cod_conta + ", conta=" + conta + ", valor=" + getValorFormatado() + ", populacao="
				+ populacao + "]";
	}
}
