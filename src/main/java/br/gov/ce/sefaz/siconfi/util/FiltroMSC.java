package br.gov.ce.sefaz.siconfi.util;

import java.util.ArrayList;
import java.util.List;

import br.gov.ce.sefaz.siconfi.enums.TipoMatrizSaldoContabeis;
import br.gov.ce.sefaz.siconfi.enums.TipoValorMatrizSaldoContabeis;

public class FiltroMSC extends FiltroBase {

	private List<Integer> meses;
	
	private TipoMatrizSaldoContabeis tipoMatrizSaldoContabeis;
	
	private List<Integer> listaClasseConta;
	
	private List<TipoValorMatrizSaldoContabeis> listaTipoValor;
	
	public boolean isListaMesesVazia() {
		return Utils.isEmptyCollection(getMeses());
	}

	public boolean isListaClassesContaVazia() {
		return Utils.isEmptyCollection(getListaClasseConta());
	}

	public boolean isListaTipoValorVazia() {
		return Utils.isEmptyCollection(getListaTipoValor());
	}

	public List<Integer> getMeses() {
		return meses;
	}

	public void setMeses(List<Integer> meses) {
		this.meses = meses;
	}

	public TipoMatrizSaldoContabeis getTipoMatrizSaldoContabeis() {
		return tipoMatrizSaldoContabeis;
	}

	public void setTipoMatrizSaldoContabeis(TipoMatrizSaldoContabeis tipoMatrizSaldoContabeis) {
		this.tipoMatrizSaldoContabeis = tipoMatrizSaldoContabeis;
	}

	public List<Integer> getListaClasseConta() {
		return listaClasseConta;
	}

	public void setListaClasseConta(List<Integer> listaClasseConta) {
		this.listaClasseConta = listaClasseConta;
	}

	public List<String> getListaCodigoTipoValor() {
		if(listaTipoValor == null) return new ArrayList<String>();
		
		List<String> listaCodigoTipoValor = new ArrayList<>();
		for(TipoValorMatrizSaldoContabeis tipoValor: listaTipoValor) {
			listaCodigoTipoValor.add(tipoValor.getCodigo());
		}
		return listaCodigoTipoValor;
	}

	public List<TipoValorMatrizSaldoContabeis> getListaTipoValor() {
		return listaTipoValor;
	}

	public void setListaTipoValor(List<TipoValorMatrizSaldoContabeis> listaTipoValor) {
		this.listaTipoValor = listaTipoValor;
	}
}
