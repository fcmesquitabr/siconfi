package br.gov.ce.sefaz.siconfi.util.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import br.gov.ce.sefaz.siconfi.util.CsvUtil; 

public class CsvUtilTest {

	@Test
	public void testeConstrutor() {
		CsvUtil<String> csvUtil = new CsvUtil<String>(String.class);
		assertNotNull(csvUtil);
	}
	
	@Test
	public void testeEscreverCabecalhoArquivoNomeVazio() {
		try {
			CsvUtil<String> csvUtil = new CsvUtil<String>(String.class);
			String cabecalho = csvUtil.writeHeader(new String[] {"coluna1","coluna2"}, null);
			assertEquals("coluna1|coluna2\r\n", cabecalho);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testeEscreverCabecalhoArquivoNomeNaoVazio() {
		try {
			CsvUtil<String> csvUtil = new CsvUtil<String>(String.class);
			String cabecalho = csvUtil.writeHeader(new String[] {"coluna1","coluna2"}, "arquivo.csv");
			assertEquals("coluna1|coluna2\r\n", cabecalho);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testeEscreverConteudoArquivoNomeVazio() {
		try {
			CsvUtil<EntidadeTeste> csvUtil = new CsvUtil<EntidadeTeste>(EntidadeTeste.class);
			List<EntidadeTeste> listaEntidades = Arrays.asList(new EntidadeTeste("a", "b"),new EntidadeTeste("c", "d"));
			int qtdeLinhasEscritas = csvUtil.writeToFile(listaEntidades, new String[] {"coluna1","coluna2"}, null);
			assertEquals(2, qtdeLinhasEscritas);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testeEscreverConteudoListaVazia() {
		try {
			CsvUtil<EntidadeTeste> csvUtil = new CsvUtil<EntidadeTeste>(EntidadeTeste.class);
			int qtdeLinhasEscritas = csvUtil.writeToFile(null, new String[] {"coluna1","coluna2"}, null);
			assertEquals(0, qtdeLinhasEscritas);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class EntidadeTeste {
		private String coluna1;
		private String coluna2;
		public EntidadeTeste(String coluna1, String coluna2) {
			super();
			this.coluna1 = coluna1;
			this.coluna2 = coluna2;
		}
		public String getColuna1() {
			return coluna1;
		}
		public void setColuna1(String coluna1) {
			this.coluna1 = coluna1;
		}
		public String getColuna2() {
			return coluna2;
		}
		public void setColuna2(String coluna2) {
			this.coluna2 = coluna2;
		}
	}
}
