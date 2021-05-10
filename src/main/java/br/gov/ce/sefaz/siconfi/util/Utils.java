package br.gov.ce.sefaz.siconfi.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

public class Utils {

	public static String getValorFormatado(Double valor) {
		return getValorFormatado(valor, Locale.US);
	}

	public static String getValorFormatado(Double valor, Locale locale) {
		DecimalFormatSymbols dfs = new DecimalFormatSymbols(locale);
		DecimalFormat df = new DecimalFormat("##,###,###,###,###.##",dfs);
		df.setGroupingUsed(false);
		df.setDecimalSeparatorAlwaysShown(true);
		df.setMinimumFractionDigits(2);
		df.setMaximumFractionDigits(2);

		return valor != null ? df.format(valor): df.format(0);
	}

	public static boolean isStringVazia(String texto) {
		return texto == null || texto.trim().isEmpty();
	}
	
	public static String removerQuebrasLinha(String texto) {
		if(texto == null) return null;
		return texto.replace("\n", "").replace("\r", "");
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean isEmptyCollection(Collection collection) {
		return collection == null || collection.isEmpty();
	}

	public static String getDataFormatada(Date data) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return dateFormat.format(data);
	}
}
