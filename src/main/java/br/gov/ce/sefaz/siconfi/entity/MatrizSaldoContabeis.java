package br.gov.ce.sefaz.siconfi.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.ce.sefaz.siconfi.util.Utils;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class MatrizSaldoContabeis {

	@Column(name = "NUM_EXERCICIO")
	protected Integer exercicio;
	
	@Column(name = "NUM_MES_REFERENCIA")
	@JsonProperty("mes_referencia")
	protected Integer mesReferencia;
	
	@Column(name = "COD_IBGE")
	@JsonProperty("cod_ibge")
	protected String codigoIbge;
	
	@Column(name = "DSC_PODER_ORGAO")
	@JsonProperty("poder_orgao")
	protected String poderOrgao;
	
	@Column(name = "TIP_MATRIZ")
	@JsonProperty("tipo_matriz")
	protected String tipoMatriz;	

	@Column(name = "NUM_CLASSE_CONTA")
	@JsonProperty("classe_conta")
	protected Integer classeConta;
	
	@Column(name = "DSC_NATUREZA_CONTA")
	@JsonProperty("natureza_conta")
	protected String naturezaConta;
	
	@Column(name = "COD_CONTA_CONTABIL")
	@JsonProperty("conta_contabil")
	protected String contaContabil;
	
	@Column(name = "NUM_ANO_FONTE_RECURSOS")
	@JsonProperty("ano_fonte_recursos")
    protected Integer anoFonteRecursos;
	
	@Column(name = "DSC_FONTE_RECURSOS")
	@JsonProperty("fonte_recursos")
    protected String fonteRecursos;

	@Column(name = "DAT_REFERENCIA")
	@Temporal(value = TemporalType.TIMESTAMP)
	@JsonProperty("data_referencia")
	protected Date dataReferencia;
	
	@Column(name = "NUM_ENTRADA_MSC")
	@JsonProperty("entrada_msc")
	protected Integer entradaMsc;
	
	@Column(name = "TIP_VALOR")
	@JsonProperty("tipo_valor")
	protected String tipoValor;

	public abstract Double getValor();

	@Transient
	protected String valorFormatado;

	public String getValorFormatado() {
		if(valorFormatado == null) valorFormatado = Utils.getValorFormatado(getValor());
		return valorFormatado;
	}

	public Integer getExercicio() {
		return exercicio;
	}

	public void setExercicio(Integer exercicio) {
		this.exercicio = exercicio;
	}

	public Integer getMesReferencia() {
		return mesReferencia;
	}

	public void setMesReferencia(Integer mesReferencia) {
		this.mesReferencia = mesReferencia;
	}

	public String getCodigoIbge() {
		return codigoIbge;
	}

	public void setCodigoIbge(String codigIbge) {
		this.codigoIbge = codigIbge;
	}

	public String getTipoMatriz() {
		return tipoMatriz;
	}

	public void setTipoMatriz(String tipoMatriz) {
		this.tipoMatriz = tipoMatriz;
	}

	public String getPoderOrgao() {
		return poderOrgao;
	}

	public void setPoderOrgao(String poderOrgao) {
		this.poderOrgao = poderOrgao;
	}

	public Integer getAnoFonteRecursos() {
		return anoFonteRecursos;
	}

	public void setAnoFonteRecursos(Integer anoFonteRecursos) {
		this.anoFonteRecursos = anoFonteRecursos;
	}

	public String getFonteRecursos() {
		return fonteRecursos;
	}

	public void setFonteRecursos(String fonteRecursos) {
		this.fonteRecursos = fonteRecursos;
	}

	public Integer getClasseConta() {
		return classeConta;
	}

	public void setClasseConta(Integer classeConta) {
		this.classeConta = classeConta;
	}

	public String getNaturezaConta() {
		return naturezaConta;
	}

	public void setNaturezaConta(String naturezaConta) {
		this.naturezaConta = naturezaConta;
	}

	public String getContaContabil() {
		return contaContabil;
	}

	public void setContaContabil(String contaContabil) {
		this.contaContabil = contaContabil;
	}

	public Date getDataReferencia() {
		return dataReferencia;
	}

	public void setDataReferencia(Date dataReferencia) {
		this.dataReferencia = dataReferencia;
	}

	public Integer getEntradaMsc() {
		return entradaMsc;
	}

	public void setEntradaMsc(Integer entradaMsc) {
		this.entradaMsc = entradaMsc;
	}

	public String getTipoValor() {
		return tipoValor;
	}

	public void setTipoValor(String tipoValor) {
		this.tipoValor = tipoValor;
	}
}
