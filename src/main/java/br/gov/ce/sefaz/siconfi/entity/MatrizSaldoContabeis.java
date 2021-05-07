package br.gov.ce.sefaz.siconfi.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.gov.ce.sefaz.siconfi.util.Utils;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class MatrizSaldoContabeis {

	@Column(name = "NUM_EXERCICIO")
	protected Integer exercicio;
	
	@Column(name = "NUM_MES_REFERENCIA")
	protected Integer mes_referencia;
	
	@Column(name = "COD_IBGE")
	protected String cod_ibge;
	
	@Column(name = "DSC_PODER_ORGAO")
	protected String poder_orgao;
	
	@Column(name = "TIP_MATRIZ")
	protected String tipo_matriz;	

	@Column(name = "NUM_CLASSE_CONTA")
	protected Integer classe_conta;
	
	@Column(name = "DSC_NATUREZA_CONTA")
	protected String natureza_conta;
	
	@Column(name = "COD_CONTA_CONTABIL")
	protected String conta_contabil;
	
	@Column(name = "NUM_ANO_FONTE_RECURSOS")
    protected Integer ano_fonte_recursos;
	
	@Column(name = "DSC_FONTE_RECURSOS")
    protected String fonte_recursos;

	@Column(name = "DAT_REFERENCIA")
	@Temporal(value = TemporalType.TIMESTAMP)
	protected Date data_referencia;
	
	@Column(name = "NUM_ENTRADA_MSC")
	protected Integer entrada_msc;
	
	@Column(name = "TIP_VALOR")
	protected String tipo_valor;

	public abstract Double getValor();

	@Transient
	protected String valorFormatado;

	public String getValorFormatado() {
		if(valorFormatado == null) valorFormatado = Utils.getValorFormatado(getValor());
		return valorFormatado;
	}

//	public Integer getId() {
//		return id;
//	}
//
//	public void setId(Integer id) {
//		this.id = id;
//	}

	public Integer getExercicio() {
		return exercicio;
	}

	public void setExercicio(Integer exercicio) {
		this.exercicio = exercicio;
	}

	public Integer getMes_referencia() {
		return mes_referencia;
	}

	public void setMes_referencia(Integer mes_referencia) {
		this.mes_referencia = mes_referencia;
	}

	public String getCod_ibge() {
		return cod_ibge;
	}

	public void setCod_ibge(String cod_ibge) {
		this.cod_ibge = cod_ibge;
	}

	public String getTipo_matriz() {
		return tipo_matriz;
	}

	public void setTipo_matriz(String tipo_matriz) {
		this.tipo_matriz = tipo_matriz;
	}

	public String getPoder_orgao() {
		return poder_orgao;
	}

	public void setPoder_orgao(String poder_orgao) {
		this.poder_orgao = poder_orgao;
	}

	public Integer getAno_fonte_recursos() {
		return ano_fonte_recursos;
	}

	public void setAno_fonte_recursos(Integer ano_fonte_recursos) {
		this.ano_fonte_recursos = ano_fonte_recursos;
	}

	public String getFonte_recursos() {
		return fonte_recursos;
	}

	public void setFonte_recursos(String fonte_recursos) {
		this.fonte_recursos = fonte_recursos;
	}

	public Integer getClasse_conta() {
		return classe_conta;
	}

	public void setClasse_conta(Integer classe_conta) {
		this.classe_conta = classe_conta;
	}

	public String getNatureza_conta() {
		return natureza_conta;
	}

	public void setNatureza_conta(String natureza_conta) {
		this.natureza_conta = natureza_conta;
	}

	public String getConta_contabil() {
		return conta_contabil;
	}

	public void setConta_contabil(String conta_contabil) {
		this.conta_contabil = conta_contabil;
	}

	public Date getData_referencia() {
		return data_referencia;
	}

	public void setData_referencia(Date data_referencia) {
		this.data_referencia = data_referencia;
	}

	public Integer getEntrada_msc() {
		return entrada_msc;
	}

	public void setEntrada_msc(Integer entrada_msc) {
		this.entrada_msc = entrada_msc;
	}

	public String getTipo_valor() {
		return tipo_valor;
	}

	public void setTipo_valor(String tipo_valor) {
		this.tipo_valor = tipo_valor;
	}
}
