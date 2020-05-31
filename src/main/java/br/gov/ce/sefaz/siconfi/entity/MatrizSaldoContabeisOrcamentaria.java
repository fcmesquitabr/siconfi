package br.gov.ce.sefaz.siconfi.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import br.gov.ce.sefaz.siconfi.util.Utils;

@Entity
public class MatrizSaldoContabeisOrcamentaria {

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
    private String funcao;
    private String subfuncao;
    private Integer educacao_saude;
    private String natureza_despesa;
    private Integer ano_inscricao;
    private String natureza_receita;
    private Integer ano_fonte_recursos;
    private String fonte_recursos;
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
		return "MatrizSaldoContabeisOrcamentaria [id=" + id + ", exercicio=" + exercicio + ", mes_referencia="
				+ mes_referencia + ", cod_ibge=" + cod_ibge + ", poder_orgao=" + poder_orgao + ", tipo_matriz="
				+ tipo_matriz + ", classe_conta=" + classe_conta + ", natureza_conta=" + natureza_conta
				+ ", conta_contabil=" + conta_contabil + ", funcao=" + funcao + ", subfuncao=" + subfuncao
				+ ", educacao_saude=" + educacao_saude + ", natureza_despesa=" + natureza_despesa + ", ano_inscricao="
				+ ano_inscricao + ", natureza_receita=" + natureza_receita + ", ano_fonte_recursos="
				+ ano_fonte_recursos + ", fonte_recursos=" + fonte_recursos + ", data_referencia=" + data_referencia
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

	public String getFuncao() {
		return funcao;
	}

	public void setFuncao(String funcao) {
		this.funcao = funcao;
	}

	public String getSubfuncao() {
		return subfuncao;
	}

	public void setSubfuncao(String subfuncao) {
		this.subfuncao = subfuncao;
	}

	public Integer getEducacao_saude() {
		return educacao_saude;
	}

	public void setEducacao_saude(Integer educacao_saude) {
		this.educacao_saude = educacao_saude;
	}

	public String getNatureza_despesa() {
		return natureza_despesa;
	}

	public void setNatureza_despesa(String natureza_despesa) {
		this.natureza_despesa = natureza_despesa;
	}

	public Integer getAno_inscricao() {
		return ano_inscricao;
	}

	public void setAno_inscricao(Integer ano_inscricao) {
		this.ano_inscricao = ano_inscricao;
	}

	public String getNatureza_receita() {
		return natureza_receita;
	}

	public void setNatureza_receita(String natureza_receita) {
		this.natureza_receita = natureza_receita;
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
