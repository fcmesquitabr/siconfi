package br.gov.ce.sefaz.siconfi.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;

import br.gov.ce.sefaz.siconfi.util.Utils;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class MatrizSaldoContabeis {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Integer id;

	protected Integer exercicio;
	protected Integer mes_referencia;
	protected String cod_ibge;
	protected String poder_orgao;
	protected String tipo_matriz;	
	protected Integer classe_conta;
	protected String natureza_conta;
	protected String conta_contabil;

    protected Integer ano_fonte_recursos;
    protected String fonte_recursos;

	protected String data_referencia;
	protected Integer entrada_msc;
	protected String tipo_valor;
	protected Double valor;

	@Transient
	protected String valorFormatado;

	public String getValorFormatado() {
		if(valorFormatado == null) valorFormatado = Utils.getValorFormatado(valor);
		return valorFormatado;
	}

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

	public String getData_referencia() {
		return data_referencia;
	}

	public void setData_referencia(String data_referencia) {
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

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}
}
