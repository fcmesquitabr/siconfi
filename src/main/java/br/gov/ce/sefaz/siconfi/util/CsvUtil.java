package br.gov.ce.sefaz.siconfi.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

public class CsvUtil<T> {

	private static final String NOME_PADRAO_ARQUIVO = "relatorio.csv";

	private static final char separador = '|';
	private Class<T> type;
	
	public CsvUtil(Class<T> type) {
		this.type=type;
	}
	
	public String writeHeader(String[] columns, String nomeArquivo) throws IOException {	
		FileWriter writer = new FileWriter(definirNomeArquivo(nomeArquivo));
		String cabecalho = String.join(String.valueOf(separador), columns).concat(CSVWriter.RFC4180_LINE_END);
		writer.write(cabecalho);
		writer.close();
		return cabecalho;
	}
	
	public int writeToFile(List<T> listaEntidades, String[] columns, String nomeArquivo)
			throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

			if(Utils.isEmptyCollection(listaEntidades)) return 0;
			
			FileWriter writer = new FileWriter(definirNomeArquivo(nomeArquivo),true);

			ColumnPositionMappingStrategy<T> mappingStrategy = new ColumnPositionMappingStrategy<>();
			mappingStrategy.setType(type);
			mappingStrategy.setColumnMapping(columns);

			StatefulBeanToCsvBuilder<T> builder = new StatefulBeanToCsvBuilder<>(writer);
			StatefulBeanToCsv<T> beanWriter = builder.withMappingStrategy(mappingStrategy)
					.withSeparator(separador)
					.withLineEnd(CSVWriter.RFC4180_LINE_END)
					.withEscapechar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
					.withQuotechar(CSVWriter.DEFAULT_QUOTE_CHARACTER)
					.build();

			beanWriter.write(listaEntidades);
			writer.close();
			return listaEntidades.size();
	}

	private String definirNomeArquivo(String nomeArquivo) {
		return !Utils.isStringVazia(nomeArquivo) ? nomeArquivo : NOME_PADRAO_ARQUIVO;
	}
}
