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
	@JsonProperty("cod_ibge")
	private String codigoIbge;
	
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
	@JsonProperty("cod_conta")
	private String codigoConta;
	
	@Column(name = "DSC_CONTA")
	@JsonProperty("conta")
	private String descricaoConta;
	
	@Column(name = "VLR_EXECUCAO_ORCAMENTARIA")
	private Double valor; 
	
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
	public String getCodigoConta() {
		return codigoConta;
	}
	public void setCodigoConta(String codigoConta) {
		this.codigoConta = codigoConta;
	}
	public String getDescricaoConta() {
		return descricaoConta;
	}
	public void setDescricaoConta(String descricaoConta) {
		this.descricaoConta = descricaoConta;
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
	
	@Override
	public String toString() {
		return "RelatorioResumidoExecucaoOrcamentaria [exercicio=" + exercicio + ", demonstrativo=" + demonstrativo
				+ ", periodo=" + periodo + ", periodicidade=" + periodicidade + ", instituicao=" + instituicao
				+ ", cod_ibge=" + codigoIbge + ", uf=" + uf + ", populacao=" + populacao + ", anexo=" + anexo
				+ ", rotulo=" + rotulo + ", coluna=" + coluna + ", cod_conta=" + codigoConta + ", conta=" + descricaoConta
				+ ", valor=" + getValorFormatado() + "]";
	}	  	  
}
