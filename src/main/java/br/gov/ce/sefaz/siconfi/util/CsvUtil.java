package br.gov.ce.sefaz.siconfi.util;

import java.io.FileWriter;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

public class CsvUtil<T> {

	private static final String NOME_PADRAO_ARQUIVO = "relatorio.csv";

	private static final Logger logger = LogManager.getLogger(CsvUtil.class);

	private static final char separador = '|';
	private Class<T> type;
	
	public CsvUtil(Class<T> type) {
		this.type=type;
	}
	
	public void writeHeader(String[] columns, String nomeArquivo) {
		
		try {
			FileWriter writer = new FileWriter(definirNomeArquivo(nomeArquivo));
			writer.write(String.join(String.valueOf(separador), columns));
			writer.write(CSVWriter.RFC4180_LINE_END);
			writer.close();
		} catch (Exception e) {
			logger.error(e);
		} 		
	}
	
	public void writeToFile(List<T> listaEntidades, String[] columns, String nomeArquivo) {
		
		try {

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
		} catch (Exception e) {
			logger.error(e);
		} 
	}

	private String definirNomeArquivo(String nomeArquivo) {
		return !Utils.isStringVazia(nomeArquivo) ? nomeArquivo : NOME_PADRAO_ARQUIVO;
	}
}
