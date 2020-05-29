package br.gov.ce.sefaz.siconfi.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import br.gov.ce.sefaz.siconfi.util.Utils;

@Entity
public class MatrizSaldoContabeisPatrimonial {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer exercicio;
	private Integer mes_referencia;
	private String cod_ibge;
	private String poder_orgao;
	private String tipo_matriz;	
    private Integer classe_conta;
    private String natureza_conta;
    private String conta_contabil;
    private Integer financeiro_permanente;
    private Integer ano_fonte_recursos;
    private String fonte_recursos;
    private Integer divida_consolidada;
    private String data_referencia;
    private Integer entrada_msc;
    private String tipo_valor;
    private Double valor;

	@Transient
	private String valorFormatado;

	public String getValorFormatado() {
		if(valorFormatado == null) valorFormatado = Utils.getValorFormatado(valor);
		return valorFormatado;
	}

	@Override
	public String toString() {
		return "MatrizSaldoContabeisPatrimonial [id=" + id + ", exercicio=" + exercicio + ", mes_referencia="
				+ mes_referencia + ", cod_ibge=" + cod_ibge + ", poder_orgao=" + poder_orgao + ", tipo_matriz="
				+ tipo_matriz + ", classe_conta=" + classe_conta + ", natureza_conta=" + natureza_conta
				+ ", conta_contabil=" + conta_contabil + ", financeiro_permanente=" + financeiro_permanente
				+ ", ano_fonte_recursos=" + ano_fonte_recursos + ", fonte_recursos=" + fonte_recursos
				+ ", divida_consolidada=" + divida_consolidada + ", data_referencia=" + data_referencia
				+ ", entrada_msc=" + entrada_msc + ", tipo_valor=" + tipo_valor + ", valor=" + valor + "]";
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
	public String getPoder_orgao() {
		return poder_orgao;
	}
	public void setPoder_orgao(String poder_orgao) {
		this.poder_orgao = poder_orgao;
	}
	public String getTipo_matriz() {
		return tipo_matriz;
	}
	public void setTipo_matriz(String tipo_matriz) {
		this.tipo_matriz = tipo_matriz;
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
	public Integer getFinanceiro_permanente() {
		return financeiro_permanente;
	}
	public void setFinanceiro_permanente(Integer financeiro_permanente) {
		this.financeiro_permanente = financeiro_permanente;
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
	public Integer getDivida_consolidada() {
		return divida_consolidada;
	}
	public void setDivida_consolidada(Integer divida_consolidada) {
		this.divida_consolidada = divida_consolidada;
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
