package br.gov.ce.sefaz.siconfi.util.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.junit.Test;

import br.gov.ce.sefaz.siconfi.util.Utils;

public class UtilsTest {

	@Test
	public void testeConstrutor() {
		Utils utils = new Utils();
		assertNotNull(utils);
	}
	
	@Test
	public void testeValorFormatadoNulo() {
		String valorFormatado = Utils.getValorFormatado(null);
		assertEquals("0,00", valorFormatado);
	}

	@Test
	public void testeValorFormatadoZero() {
		String valorFormatado = Utils.getValorFormatado(0.0);
		assertEquals("0,00", valorFormatado);
	}

	@Test
	public void testeValorFormatadoValorMenorQueMil() {
		String valorFormatado = Utils.getValorFormatado(123.0);
		assertEquals("123,00", valorFormatado);		
	}

	@Test
	public void testeValorFormatadoValorMaiorQueMil() {
		String valorFormatado = Utils.getValorFormatado(123456789.0);
		assertEquals("123.456.789,00", valorFormatado);
	}

	@Test
	public void testeStringVaziaNull() {
		assertTrue(Utils.isStringVazia(null));
	}

	@Test
	public void testeStringVaziaSemConteudo() {
		assertTrue(Utils.isStringVazia(""));
	}

	@Test
	public void testeStringVaziaEspaço() {
		assertTrue(Utils.isStringVazia(" "));
	}

	@Test
	public void testeStringVaziaCaracteresNovaLinhaTabulacao() {
		assertTrue(Utils.isStringVazia("\n\r\t"));
	}

	@Test
	public void testeStringVaziaTextoValido() {
		assertFalse(Utils.isStringVazia(" a "));
	}

	@Test
	public void testeEmptyCollectionValorNull() {
		assertTrue(Utils.isEmptyCollection(null));
	}

	@Test
	public void testeEmptyCollectionListaVazia() {
		assertTrue(Utils.isEmptyCollection(Collections.emptyList()));
	}

	@Test
	public void testeEmptyCollectionListaNaoVazia() {
		assertFalse(Utils.isEmptyCollection(Collections.singletonList("")));
	}

	@Test
	public void testeRemoverQuebrasLinhaValorNull() {
		assertNull(Utils.removerQuebrasLinha(null));
	}

	@Test
	public void testeRemoverQuebrasLinhaValorComQuebras() {
		assertEquals("texto com quebra de linha", Utils.removerQuebrasLinha("texto com \nquebra \rde linha"));
	}
}
