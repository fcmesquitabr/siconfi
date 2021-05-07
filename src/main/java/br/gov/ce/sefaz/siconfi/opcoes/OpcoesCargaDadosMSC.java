package br.gov.ce.sefaz.siconfi.opcoes;

import java.util.ArrayList;
import java.util.List;

import br.gov.ce.sefaz.siconfi.enums.TipoMatrizSaldoContabeis;
import br.gov.ce.sefaz.siconfi.enums.TipoValorMatrizSaldoContabeis;
import br.gov.ce.sefaz.siconfi.util.Utils;

public class OpcoesCargaDadosMSC extends OpcoesCargaDados {

	private TipoMatrizSaldoContabeis tipoMatrizSaldoContabeis;
	
	private List<Integer> listaClasseConta;
	
	private List<TipoValorMatrizSaldoContabeis> listaTipoValor;

	public OpcoesCargaDadosMSC() {
		super();
	}

	public boolean isListaClassesContaVazia() {
		return Utils.isEmptyCollection(getListaClasseConta());
	}

	public boolean isListaTipoValorVazia() {
		return Utils.isEmptyCollection(getListaTipoValor());
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
		if(listaTipoValor == null) return new ArrayList<>();
		
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
	
	protected OpcoesCargaDadosMSC (Builder builder) {
		super(builder);
		this.tipoMatrizSaldoContabeis =  builder.tipoMatrizSaldoContabeis;
		this.listaClasseConta = builder.listaClasseConta;
		this.listaTipoValor = builder.listaTipoValor;
	}
	
	public static class Builder extends OpcoesCargaDados.Builder<Builder> {
		
		private TipoMatrizSaldoContabeis tipoMatrizSaldoContabeis;		
		private List<Integer> listaClasseConta;		
		private List<TipoValorMatrizSaldoContabeis> listaTipoValor;
		
		public Builder() {
			super();
		}
		
		public Builder listaClasseConta (List<Integer> listaClasseConta) {
			this.listaClasseConta = listaClasseConta;
			return this;
		}

		public Builder listaTipoValor(List<TipoValorMatrizSaldoContabeis> listaTipoValor) {
			this.listaTipoValor = listaTipoValor;
			return this;
		}

		public Builder tipoMatrizSaldoContabeis(TipoMatrizSaldoContabeis tipoMatrizSaldoContabeis) {
			this.tipoMatrizSaldoContabeis = tipoMatrizSaldoContabeis;
			return this;
		}

		@Override
		public OpcoesCargaDadosMSC build() {
			return new OpcoesCargaDadosMSC(this);
		}
	}
}
