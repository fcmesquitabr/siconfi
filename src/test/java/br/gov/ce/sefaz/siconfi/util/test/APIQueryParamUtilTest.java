package br.gov.ce.sefaz.siconfi.util.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import br.gov.ce.sefaz.siconfi.util.APIQueryParamUtil;

public class APIQueryParamUtilTest {

	private APIQueryParamUtil apiQueryParamUtil = new APIQueryParamUtil();
	
	@Test
	public void testeConstrutor() {
		assertNotNull(apiQueryParamUtil);
	}
	
	@Test
	public void testeAddParamAnExercicioNull() {
		apiQueryParamUtil.addParamAnExercicio(null);
		assertNull(apiQueryParamUtil.getMapQueryParam().get(APIQueryParamUtil.API_QUERY_PARAM_AN_EXERCICIO));
	}

	@Test
	public void testeAddParamAnExercicioNotNull() {
		apiQueryParamUtil.addParamAnExercicio(2020);
		assertEquals(2020, apiQueryParamUtil.getMapQueryParam().get(APIQueryParamUtil.API_QUERY_PARAM_AN_EXERCICIO));
	}

	@Test
	public void testeAddParamAnReferenciaNull() {
		apiQueryParamUtil.addParamAnReferencia(null);
		assertNull(apiQueryParamUtil.getMapQueryParam().get(APIQueryParamUtil.API_QUERY_PARAM_AN_REFERENCIA));
	}

	@Test
	public void testeAddParamAnReferenciaNotNull() {		
		apiQueryParamUtil.addParamAnReferencia(2020);
		assertEquals(2020, apiQueryParamUtil.getMapQueryParam().get(APIQueryParamUtil.API_QUERY_PARAM_AN_REFERENCIA));
	}

	@Test
	public void testeAddParamAnexoNull() {
		apiQueryParamUtil.addParamAnexo(null);
		assertNull(apiQueryParamUtil.getMapQueryParam().get(APIQueryParamUtil.API_QUERY_PARAM_NO_ANEXO));
	}

	@Test
	public void testeAddParamAnexoStringVazia() {
		apiQueryParamUtil.addParamAnexo(" \r\n\t ");
		assertNull(apiQueryParamUtil.getMapQueryParam().get(APIQueryParamUtil.API_QUERY_PARAM_NO_ANEXO));
	}

	@Test
	public void testeAddParamAnexoValorValido() {
		apiQueryParamUtil.addParamAnexo("anexo");
		assertEquals("anexo", apiQueryParamUtil.getMapQueryParam().get(APIQueryParamUtil.API_QUERY_PARAM_NO_ANEXO));
	}

	@Test
	public void testeAddParamClasseContaNull() {
		apiQueryParamUtil.addParamClasseConta(null);
		assertNull(apiQueryParamUtil.getMapQueryParam().get(APIQueryParamUtil.API_QUERY_PARAM_CLASSE_CONTA));
	}

	@Test
	public void testeAddParamClasseContaNotNull() {
		apiQueryParamUtil.addParamClasseConta(1);
		assertEquals(1, apiQueryParamUtil.getMapQueryParam().get(APIQueryParamUtil.API_QUERY_PARAM_CLASSE_CONTA));
	}

	@Test
	public void testeAddParamIdEnte() {
		apiQueryParamUtil.addParamIdEnte(null);
		assertNull(apiQueryParamUtil.getMapQueryParam().get(APIQueryParamUtil.API_QUERY_PARAM_ID_ENTE));		

		apiQueryParamUtil.addParamIdEnte(" ");
		assertNull(apiQueryParamUtil.getMapQueryParam().get(APIQueryParamUtil.API_QUERY_PARAM_ID_ENTE));		

		apiQueryParamUtil.addParamIdEnte("23");
		assertEquals("23", apiQueryParamUtil.getMapQueryParam().get(APIQueryParamUtil.API_QUERY_PARAM_ID_ENTE));
	}

	@Test
	public void testeAddParamIndicadorPeriodiciadade() {
		apiQueryParamUtil.addParamIndicadorPeriodiciadade(null);
		assertNull(apiQueryParamUtil.getMapQueryParam().get(APIQueryParamUtil.API_QUERY_PARAM_IN_PERIODICIDADE));		

		apiQueryParamUtil.addParamIndicadorPeriodiciadade(" ");
		assertNull(apiQueryParamUtil.getMapQueryParam().get(APIQueryParamUtil.API_QUERY_PARAM_IN_PERIODICIDADE));		

		apiQueryParamUtil.addParamIndicadorPeriodiciadade("B");
		assertEquals("B", apiQueryParamUtil.getMapQueryParam().get(APIQueryParamUtil.API_QUERY_PARAM_IN_PERIODICIDADE));
	}

	@Test
	public void testeAddParamMesReferenciaNull() {
		apiQueryParamUtil.addParamMesReferencia(null);
		assertNull(apiQueryParamUtil.getMapQueryParam().get(APIQueryParamUtil.API_QUERY_PARAM_ME_REFERENCIA));		
	}

	@Test
	public void testeAddParamMesReferenciaNotNull() {
		apiQueryParamUtil.addParamMesReferencia(1);
		assertEquals(1, apiQueryParamUtil.getMapQueryParam().get(APIQueryParamUtil.API_QUERY_PARAM_ME_REFERENCIA));
	}

	@Test
	public void testeAddParamPeriodoNull() {
		apiQueryParamUtil.addParamPeriodo(null);
		assertNull(apiQueryParamUtil.getMapQueryParam().get(APIQueryParamUtil.API_QUERY_PARAM_NR_PERIODO));		
	}

	@Test
	public void testeAddParamPeriodoNotNull() {
		apiQueryParamUtil.addParamPeriodo(1);
		assertEquals(1, apiQueryParamUtil.getMapQueryParam().get(APIQueryParamUtil.API_QUERY_PARAM_NR_PERIODO));
	}

	@Test
	public void testeAddParamPoder() {
		apiQueryParamUtil.addParamPoder(null);
		assertNull(apiQueryParamUtil.getMapQueryParam().get(APIQueryParamUtil.API_QUERY_PARAM_CO_PODER));		

		apiQueryParamUtil.addParamPoder(" ");
		assertNull(apiQueryParamUtil.getMapQueryParam().get(APIQueryParamUtil.API_QUERY_PARAM_CO_PODER));		

		apiQueryParamUtil.addParamPoder("E");
		assertEquals("E", apiQueryParamUtil.getMapQueryParam().get(APIQueryParamUtil.API_QUERY_PARAM_CO_PODER));
	}

	@Test
	public void testeAddParamTipoDemonstrativo() {
		apiQueryParamUtil.addParamTipoDemonstrativo(null);
		assertNull(apiQueryParamUtil.getMapQueryParam().get(APIQueryParamUtil.API_QUERY_PARAM_CO_TIPO_DEMONSTRATIVO));		

		apiQueryParamUtil.addParamTipoDemonstrativo(" ");
		assertNull(apiQueryParamUtil.getMapQueryParam().get(APIQueryParamUtil.API_QUERY_PARAM_CO_TIPO_DEMONSTRATIVO));		

		apiQueryParamUtil.addParamTipoDemonstrativo("RREO");
		assertEquals("RREO", apiQueryParamUtil.getMapQueryParam().get(APIQueryParamUtil.API_QUERY_PARAM_CO_TIPO_DEMONSTRATIVO));
	}

	@Test
	public void testeAddParamTipoMatriz() {
		apiQueryParamUtil.addParamTipoMatriz(null);
		assertNull(apiQueryParamUtil.getMapQueryParam().get(APIQueryParamUtil.API_QUERY_PARAM_CO_TIPO_MATRIZ));		

		apiQueryParamUtil.addParamTipoMatriz(" ");
		assertNull(apiQueryParamUtil.getMapQueryParam().get(APIQueryParamUtil.API_QUERY_PARAM_CO_TIPO_MATRIZ));		

		apiQueryParamUtil.addParamTipoMatriz("MSCC");
		assertEquals("MSCC", apiQueryParamUtil.getMapQueryParam().get(APIQueryParamUtil.API_QUERY_PARAM_CO_TIPO_MATRIZ));
	}

	@Test
	public void testeAddParamTipoValorMatriz() {
		apiQueryParamUtil.addParamTipoValorMatriz(null);
		assertNull(apiQueryParamUtil.getMapQueryParam().get(APIQueryParamUtil.API_QUERY_PARAM_ID_TV));		

		apiQueryParamUtil.addParamTipoValorMatriz(" ");
		assertNull(apiQueryParamUtil.getMapQueryParam().get(APIQueryParamUtil.API_QUERY_PARAM_ID_TV));		

		apiQueryParamUtil.addParamTipoValorMatriz("period_change");
		assertEquals("period_change", apiQueryParamUtil.getMapQueryParam().get(APIQueryParamUtil.API_QUERY_PARAM_ID_TV));
	}
}
