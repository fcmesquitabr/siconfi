package br.gov.ce.sefaz.siconfi.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collection;
import java.util.Locale;

public class Utils {

	public static String getValorFormatado(Double valor) {
		if(valor == null || valor.equals(0.0)) {
			return "0,00";
		}
		DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.GERMAN);
		DecimalFormat df = new DecimalFormat("##,###,###,###,###.##",dfs);
		df.setGroupingUsed(true);
		df.setDecimalSeparatorAlwaysShown(true);
		return df.format(valor);

	}
	
	public static boolean isStringVazia(String texto) {
		return texto == null || texto.trim().isEmpty();
	}
	
	public static String removerQuebrasLinha(String texto) {
		if(texto == null) return null;
		return texto.replaceAll("\n", "").replaceAll("\r", "");
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean isEmptyCollection(Collection collection) {
		return collection == null || collection.isEmpty();
	}
}
