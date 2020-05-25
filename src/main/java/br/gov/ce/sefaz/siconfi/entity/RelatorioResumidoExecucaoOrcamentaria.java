package br.gov.ce.sefaz.siconfi.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import br.gov.ce.sefaz.siconfi.util.Utils;

@Entity
public class RelatorioResumidoExecucaoOrcamentaria {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private Integer exercicio;
	private String demonstrativo;
	private Integer periodo;
	private String periodicidade;
	private String instituicao;
	private String cod_ibge;
	private String uf;
	private Long populacao;
	private String anexo;
	private String rotulo;
	private String coluna;
	private String cod_conta;
	private String conta;
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
	@Override
	public String toString() {
		return "RelatorioResumidoExecucaoOrcamentaria [exercicio=" + exercicio + ", demonstrativo=" + demonstrativo
				+ ", periodo=" + periodo + ", periodicidade=" + periodicidade + ", instituicao=" + instituicao
				+ ", cod_ibge=" + cod_ibge + ", uf=" + uf + ", populacao=" + populacao + ", anexo=" + anexo
				+ ", rotulo=" + rotulo + ", coluna=" + coluna + ", cod_conta=" + cod_conta + ", conta=" + conta
				+ ", valor=" + getValorFormatado() + "]";
	}	  	  
}
