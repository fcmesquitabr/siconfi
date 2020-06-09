package br.gov.ce.sefaz.siconfi.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Table(schema = "TAB_SICONFI", name = "ANEXO_RELATORIO")
@Entity
public class AnexoRelatorio {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SEQ_ANEXO")
	private Integer id;

	@Column(name = "TXT_ESFERA")
	private String esfera;
	
	@Column(name = "TXT_DEMONSTRATIVO")
	private String demonstrativo;
	
	@Column(name = "TXT_ANEXO")
	private String anexo;
	
	@Column(name = "DAT_ALTERACAO")
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date dataHoraAlteracao;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEsfera() {
		return esfera;
	}
	public void setEsfera(String esfera) {
		this.esfera = esfera;
	}
	public String getDemonstrativo() {
		return demonstrativo;
	}
	public void setDemonstrativo(String demonstrativo) {
		this.demonstrativo = demonstrativo;
	}
	public String getAnexo() {
		return anexo;
	}
	public void setAnexo(String anexo) {
		this.anexo = anexo;
	}
	@Override
	public String toString() {
		return "AnexoRelatorio [esfera=" + esfera + ", demonstrativo=" + demonstrativo + ", anexo=" + anexo + "]";
	}

	public Date getDataHoraAlteracao() {
		return dataHoraAlteracao;
	}

	public void setDataHoraAlteracao(Date dataHoraAlteracao) {
		this.dataHoraAlteracao = dataHoraAlteracao;
	}
}
