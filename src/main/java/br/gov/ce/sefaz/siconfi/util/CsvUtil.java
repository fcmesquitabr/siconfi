package br.gov.ce.sefaz.siconfi.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

public class CsvUtil<T> {

	private static final Logger logger = LogManager.getLogger(CsvUtil.class);
	
	private Class<T> type;
	
	public CsvUtil(Class<T> type) {
		this.type=type;
	}
	
	public void writeToCsvFile(List<T> listaEntidades, String[] columns, String nomeArquivo) {
		
		FileWriter writer = null;
		String nomeArquivoCsv = definirNomeArquivo(nomeArquivo);

		try {

			writer = new FileWriter(nomeArquivoCsv);

			ColumnPositionMappingStrategy<T> mappingStrategy = new ColumnPositionMappingStrategy<>();
			mappingStrategy.setType(type);
			mappingStrategy.setColumnMapping(columns);

			StatefulBeanToCsvBuilder<T> builder = new StatefulBeanToCsvBuilder<>(writer);
			StatefulBeanToCsv<T> beanWriter = builder.withMappingStrategy(mappingStrategy)
					.withSeparator("|".charAt(0))
					.withLineEnd(CSVWriter.RFC4180_LINE_END)
					.withEscapechar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
					.withQuotechar(CSVWriter.DEFAULT_QUOTE_CHARACTER)
					.build();

			writer.write(String.join("|", columns));
			writer.write("\r\n");
			beanWriter.write(listaEntidades);
		} catch (Exception e) {
			logger.error(e);
		} 

		try {
			if (writer != null) {
				writer.close();
			}
		} catch (IOException e) {
			logger.error(e);
		}		
	}

	private String definirNomeArquivo(String nomeArquivo) {
		String nomeArquivoCsv;
		if (nomeArquivo != null && nomeArquivo.trim().length() > 0) {
			nomeArquivoCsv = nomeArquivo;
		} else {
			nomeArquivoCsv = "relatorio.csv";
		}
		return nomeArquivoCsv;
	}
}
