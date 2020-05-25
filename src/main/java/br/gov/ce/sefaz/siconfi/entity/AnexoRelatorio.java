package br.gov.ce.sefaz.siconfi.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AnexoRelatorio {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String esfera;
	private String demonstrativo;
	private String anexo;
	
	
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

	
}
