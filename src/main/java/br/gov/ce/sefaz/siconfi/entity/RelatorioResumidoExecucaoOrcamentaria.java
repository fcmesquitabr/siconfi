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

@Table(schema = "TAB_SICONFI", name = "RELATORIO_EXE_ORCAMEN")
@Entity
public class RelatorioResumidoExecucaoOrcamentaria {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SEQ_RELATORIO_EXE_ORCAMEN")
	private Integer id;

	@Column(name = "NUM_EXERCICIO")
	private Integer exercicio;
	
	@Column(name = "TXT_DEMONSTRATIVO")
	private String demonstrativo;
	
	@Column(name = "NUM_PERIODO")
	private Integer periodo;
	
	@Column(name = "DSC_PERIODICIDADE")
	private String periodicidade;
	
	@Column(name = "DSC_INSTITUICAO")
	private String instituicao;
		
	@Column(name = "COD_IBGE")
	private String cod_ibge;
	
	@Column(name = "COD_UF")
	private String uf;
	
	@Column(name = "QTD_POPULACAO")
	private Long populacao;
	
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
	
	@Column(name = "VLR_EXECUCAO_ORCAMENTARIA")
	private Double valor; 
	
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
	public String getDemonstrativo() {
		return demonstrativo;
	}
	public void setDemonstrativo(String demonstrativo) {
		this.demonstrativo = demonstrativo;
	}
	public Integer getPeriodo() {
		return periodo;
	}
	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}
	public String getPeriodicidade() {
		return periodicidade;
	}
	public void setPeriodicidade(String periodicidade) {
		this.periodicidade = periodicidade;
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
	public Long getPopulacao() {
		return populacao;
	}
	public void setPopulacao(Long populacao) {
		this.populacao = populacao;
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
		this.coluna = coluna;
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
		this.conta = conta;
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
	
	public Date getDataHoraAlteracao() {
		return dataHoraAlteracao;
	}
	public void setDataHoraAlteracao(Date dataHoraAlteracao) {
		this.dataHoraAlteracao = dataHoraAlteracao;
	}
	@Override
	public String toString() {
		return "RelatorioResumidoExecucaoOrcamentaria [exercicio=" + exercicio + ", demonstrativo=" + demonstrativo
				+ ", periodo=" + periodo + ", periodicidade=" + periodicidade + ", instituicao=" + instituicao
				+ ", cod_ibge=" + cod_ibge + ", uf=" + uf + ", populacao=" + populacao + ", anexo=" + anexo
				+ ", rotulo=" + rotulo + ", coluna=" + coluna + ", cod_conta=" + cod_conta + ", conta=" + conta
				+ ", valor=" + getValorFormatado() + "]";
	}	  	  
}
