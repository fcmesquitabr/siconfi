package br.gov.ce.sefaz.siconfi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.ce.sefaz.siconfi.enums.Esfera;
import br.gov.ce.sefaz.siconfi.util.Utils;

@JsonIgnoreProperties(ignoreUnknown = true)
@Table(schema = "TAB_SICONFI", name = "ENTE")
@Entity
public class Ente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SEQ_ENTE")
	private Long id;

	@Column(name = "COD_IBGE")
	@JsonProperty("cod_ibge")
	private String codigoIbge;
	
	@Column(name = "TXT_ENTE")
	@JsonProperty("ente")
	private String descricaoEnte;
	
	@Column(name = "NUM_CAPITAL")
	private Integer capital;
	
	@Column(name = "DSC_REGIAO")
	private String regiao;
	
	@Column(name = "COD_UF")
	private String uf;
	
	@Column(name = "TXT_ESFERA")
	private String esfera;
	
	@Column(name = "NUM_EXERCICIO")
	private Integer exercicio;
	
	@Column(name = "QTD_POPULACAO")
	private Long populacao;

	@Column(name = "NUM_CNPJ")
	@JsonProperty("co_cnpj")
	private Long numCnpj;
	
	@Transient
	private String cnpj;
	
	public Ente() {
		super();
	}
		
	public Ente(String codigoIbge, String esfera) {
		super();
		this.codigoIbge = codigoIbge;
		this.esfera = esfera;
	}

	public boolean isMunicipio() {
		return getEsfera() != null && getEsfera().equals(Esfera.MUNICIPIO.getCodigo());
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigoIbge() {
		return codigoIbge;
	}

	public void setCodigoIbge(String codigoIbge) {
		this.codigoIbge = codigoIbge;
	}

	public String getDescricaoEnte() {
		return descricaoEnte;
	}

	public void setDescricaoEnte(String descricaoEnte) {
		this.descricaoEnte = descricaoEnte;
	}

	public Integer getCapital() {
		return capital;
	}
	public void setCapital(Integer capital) {
		this.capital = capital;
	}
	public String getRegiao() {
		return regiao;
	}
	public void setRegiao(String regiao) {
		this.regiao = regiao;
	}
	public String getUf() {
		return uf;
	}
	public void setUf(String uf) {
		this.uf = uf;
	}
	public String getEsfera() {
		return esfera;
	}
	public void setEsfera(String esfera) {
		this.esfera = esfera;
	}
	public Integer getExercicio() {
		return exercicio;
	}
	public void setExercicio(Integer exercicio) {
		this.exercicio = exercicio;
	}
	public Long getPopulacao() {
		return populacao;
	}
	public void setPopulacao(Long populacao) {
		this.populacao = populacao;
	}
	
	public Long getNumCnpj() {
		return numCnpj;
	}
	public void setNumCnpj(Long numCnpj) {
		this.numCnpj = numCnpj;
	}
	
	public String getCnpj() {
		return cnpj;
	}
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
		if(!Utils.isStringVazia(cnpj)) {
			setNumCnpj(Long.valueOf(cnpj));
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoIbge == null) ? 0 : codigoIbge.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ente other = (Ente) obj;
		if (codigoIbge == null) {
			if (other.codigoIbge != null)
				return false;
		} else if (!codigoIbge.equals(other.codigoIbge))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Ente [cod_ibge=" + codigoIbge + ", ente=" + descricaoEnte + ", capital=" + capital + ", regiao=" + regiao + ", uf="
				+ uf + ", esfera=" + esfera + ", exercicio=" + exercicio + ", populacao=" + populacao + ", cnpj=" + cnpj
				+ "]";
	}	
}
