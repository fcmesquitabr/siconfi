package br.gov.ce.sefaz.siconfi.entity;

import javax.persistence.Entity;

@Entity
public class MatrizSaldoContabeisOrcamentaria extends MatrizSaldoContabeis {

    private String funcao;
    private String subfuncao;
    private Integer educacao_saude;
    private String natureza_despesa;
    private Integer ano_inscricao;
    private String natureza_receita;

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

	public String getPoder_orgao() {
		return poder_orgao;
	}

	public void setPoder_orgao(String poder_orgao) {
		this.poder_orgao = poder_orgao;
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
}
