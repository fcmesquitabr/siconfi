package br.gov.ce.sefaz.siconfi.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ExtratoEntrega {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private Integer exercicio;
	private String cod_ibge;
	private Long populacao;
	private String instituicao;
	private String entregavel;
	private Integer periodo;
	private String periodicidade;
	private String status_relatorio;
	private String data_status;
	private String forma_envio;
	private String tipo_relatorio;
	
	
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
	public String getCod_ibge() {
		return cod_ibge;
	}
	public void setCod_ibge(String cod_ibge) {
		this.cod_ibge = cod_ibge;
	}
	public Long getPopulacao() {
		return populacao;
	}
	public void setPopulacao(Long populacao) {
		this.populacao = populacao;
	}
	public String getInstituicao() {
		return instituicao;
	}
	public void setInstituicao(String instituicao) {
		this.instituicao = instituicao;
	}
	public String getEntregavel() {
		return entregavel;
	}
	public void setEntregavel(String entregavel) {
		this.entregavel = entregavel;
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
	public String getStatus_relatorio() {
		return status_relatorio;
	}
	public void setStatus_relatorio(String status_relatorio) {
		this.status_relatorio = status_relatorio;
	}
	public String getData_status() {
		return data_status;
	}
	public void setData_status(String data_status) {
		this.data_status = data_status;
	}
	public String getForma_envio() {
		return forma_envio;
	}
	public void setForma_envio(String forma_envio) {
		this.forma_envio = forma_envio;
	}
	public String getTipo_relatorio() {
		return tipo_relatorio;
	}
	public void setTipo_relatorio(String tipo_relatorio) {
		this.tipo_relatorio = tipo_relatorio;
	}
	@Override
	public String toString() {
		return "ExtratoEntrega [exercicio=" + exercicio + ", cod_ibge=" + cod_ibge + ", populacao=" + populacao
				+ ", instituicao=" + instituicao + ", entregavel=" + entregavel + ", periodo=" + periodo
				+ ", periodicidade=" + periodicidade + ", status_relatorio=" + status_relatorio + ", data_status="
				+ data_status + ", forma_envio=" + forma_envio + ", tipo_relatorio=" + tipo_relatorio + "]";
	}
}
