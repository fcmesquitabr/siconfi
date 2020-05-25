package br.gov.ce.sefaz.siconfi.util;

import java.util.List;

import br.gov.ce.sefaz.siconfi.enums.Esfera;
import br.gov.ce.sefaz.siconfi.enums.OpcaoSalvamentoDados;

public class FiltroDCA {

	private List<Integer> exercicios;

	private OpcaoSalvamentoDados opcaoSalvamento;

	private Esfera esfera;
	
	private List<String> codigosIBGE;

	private List<String> listaAnexos;

	private String nomeArquivo;
	
	public List<Integer> getExercicios() {
		return exercicios;
	}

	public void setExercicios(List<Integer> exercicios) {
		this.exercicios = exercicios;
	}

	public OpcaoSalvamentoDados getOpcaoSalvamento() {
		return opcaoSalvamento;
	}

	public void setOpcaoSalvamento(OpcaoSalvamentoDados opcaoSalvamento) {
		this.opcaoSalvamento = opcaoSalvamento;
	}

	public Esfera getEsfera() {
		return esfera;
	}

	public void setEsfera(Esfera esfera) {
		this.esfera = esfera;
	}

	public List<String> getCodigosIBGE() {
		return codigosIBGE;
	}

	public void setCodigosIBGE(List<String> codigosIBGE) {
		this.codigosIBGE = codigosIBGE;
	}

	public List<String> getListaAnexos() {
		return listaAnexos;
	}

	public void setListaAnexos(List<String> listaAnexos) {
		this.listaAnexos = listaAnexos;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}
	
}
