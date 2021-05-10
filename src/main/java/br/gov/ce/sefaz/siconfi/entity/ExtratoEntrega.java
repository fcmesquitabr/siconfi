package br.gov.ce.sefaz.siconfi.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
	@JsonProperty("cod_ibge")
	private String codigoIbge;
	
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
	@JsonAlias("status_relatorio")
	private String statusRelatorio;
	
	@Column(name = "DAT_STATUS")
	@JsonAlias("data_status")
	private Date statusData;
	
	@Column(name = "DSC_FORMA_ENVIO")
	@JsonAlias("forma_envio")
	private String formaEnvio;
	
	@Column(name = "TIP_RELATORIO")
	@JsonAlias("tipo_relatorio")
	private String tipoRelatorio;
	
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
	public String getCodigoIbge() {
		return codigoIbge;
	}
	public void setCodigoIbge(String codIbge) {
		this.codigoIbge = codIbge;
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
	public String getStatusRelatorio() {
		return statusRelatorio;
	}
	public void setStatusRelatorio(String statusRelatorio) {
		this.statusRelatorio = statusRelatorio;
	}
	
	public Date getStatusData() {
		return statusData;
	}
	public void setStatusData(Date statusData) {
		this.statusData = statusData;
	}
	public String getDataFormatada() {
		if(dataFormatada == null) dataFormatada = Utils.getDataFormatada(statusData);
		return dataFormatada;
	}

	public String getFormaEnvio() {
		return formaEnvio;
	}
	public void setFormaEnvio(String formaEnvio) {
		this.formaEnvio = formaEnvio;
	}
	public String getTipoRelatorio() {
		return tipoRelatorio;
	}
	public void setTipoRelatorio(String tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}
	
	@Override
	public String toString() {
		return "ExtratoEntrega [exercicio=" + exercicio + ", cod_ibge=" + codigoIbge + ", populacao=" + populacao
				+ ", instituicao=" + instituicao + ", entregavel=" + entregavel + ", periodo=" + periodo
				+ ", periodicidade=" + periodicidade + ", status_relatorio=" + statusRelatorio + ", data_status="
				+ statusData + ", forma_envio=" + formaEnvio + ", tipo_relatorio=" + tipoRelatorio + "]";
	}
}
