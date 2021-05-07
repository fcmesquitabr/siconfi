package br.gov.ce.sefaz.siconfi.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.gov.ce.sefaz.siconfi.util.Utils;

@JsonIgnoreProperties(ignoreUnknown = true)
@Table(schema = "TAB_SICONFI", name = "EXTRATO_ENTREGA")
@Entity
public class ExtratoEntrega {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SEQ_EXTRATO_ENTREGA")
	private Integer id;
	
	@Column(name = "NUM_EXERCICIO")
	private Integer exercicio;
	
	@Column(name = "COD_IBGE")
	private String cod_ibge;
	
	@Column(name = "QTD_POPULACAO")
	private Long populacao;
	
	@Column(name = "DSC_INSTITUICAO")
	private String instituicao;
	
	@Column(name = "DSC_ENTREGAVEL")
	private String entregavel;
	
	@Column(name = "NUM_PERIODO")
	private Integer periodo;
	
	@Column(name = "DSC_PERIODICIDADE")
	private String periodicidade;
	
	@Column(name = "STA_RELATORIO")
	private String status_relatorio;
	
	@Column(name = "DAT_STATUS")
	private Date data_status;
	
	@Column(name = "DSC_FORMA_ENVIO")
	private String forma_envio;
	
	@Column(name = "TIP_RELATORIO")
	private String tipo_relatorio;
	
	@Transient
	private String dataFormatada;

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
	
	public Date getData_status() {
		return data_status;
	}
	public void setData_status(Date data_status) {
		this.data_status = data_status;
	}
	public String getDataFormatada() {
		if(dataFormatada == null) dataFormatada = Utils.getDataFormatada(data_status);
		return dataFormatada;
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
