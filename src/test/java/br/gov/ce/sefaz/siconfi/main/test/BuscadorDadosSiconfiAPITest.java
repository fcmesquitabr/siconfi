package br.gov.ce.sefaz.siconfi.main.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import br.gov.ce.sefaz.siconfi.enums.Esfera;
import br.gov.ce.sefaz.siconfi.enums.Relatorio;
import br.gov.ce.sefaz.siconfi.main.BuscadorDadosSiconfiAPI;
import br.gov.ce.sefaz.siconfi.opcoes.OpcoesCargaDadosEnte;
import br.gov.ce.sefaz.siconfi.service.AnexoRelatorioService;
import br.gov.ce.sefaz.siconfi.service.DCAService;
import br.gov.ce.sefaz.siconfi.service.EnteService;
import br.gov.ce.sefaz.siconfi.service.ExtratoEntregaService;
import br.gov.ce.sefaz.siconfi.service.MSCControleService;
import br.gov.ce.sefaz.siconfi.service.MSCOrcamentariaService;
import br.gov.ce.sefaz.siconfi.service.MSCPatrimonialService;
import br.gov.ce.sefaz.siconfi.service.RGFService;
import br.gov.ce.sefaz.siconfi.service.RREOService;
import br.gov.ce.sefaz.siconfi.service.SiconfiService;
import br.gov.ce.sefaz.siconfi.util.LeitorParametrosPrograma;

@RunWith(MockitoJUnitRunner.class)
public class BuscadorDadosSiconfiAPITest {

	@Mock
	public EnteService enteService;

	@Mock
	public AnexoRelatorioService anexoRelatorioService;

	@Mock
	public ExtratoEntregaService extratoEntregaService;

	@Mock
	public DCAService dcaService;

	@Mock
	public RGFService rgfService;

	@Mock
	public RREOService rreoService;

	@Mock
	public MSCPatrimonialService mscPatrimonialService;

	@Mock
	public MSCOrcamentariaService mscOrcamentariaService;

	@Mock
	public MSCControleService mscControleService;

	@InjectMocks
	public BuscadorDadosSiconfiAPI buscadorDados;
	
	@Captor
	public ArgumentCaptor<OpcoesCargaDadosEnte> captorOpcao; 
	
	@Test
	public void testeConstrutor() {
		BuscadorDadosSiconfiAPI buscador = new BuscadorDadosSiconfiAPI();
		assertNotNull(buscador);
	}

	@Test
	public void testeCarregarDadosRelatorioNaoSelecionado() {
		System.clearProperty(LeitorParametrosPrograma.OPCAO_RELATORIO);		
		assertThrows("Relatorio Não Selecionado", IllegalArgumentException.class, () -> buscadorDados.buscarDados());
	}

	@Test
	public void testeCarregarDadosEntes() {
		System.setProperty(LeitorParametrosPrograma.OPCAO_RELATORIO, Relatorio.ente.name());
		System.setProperty(LeitorParametrosPrograma.OPCAO_ESFERA, Esfera.ESTADO.getCodigo());

		doNothing().when(enteService).carregarDados(any());
		buscadorDados.buscarDados();
		verify(enteService).carregarDados(captorOpcao.capture());
		OpcoesCargaDadosEnte opcao = captorOpcao.getValue();
		assertEquals(Esfera.ESTADO, opcao.getEsfera());
	}
	
	
	@Test
	public void testeCarregarDadosAnexoRelatorio() {
		verificarSeMetodoCarregarDadosFoiChamado(Relatorio.anexo_relatorio, anexoRelatorioService);
	}

	@Test
	public void testeCarregarDadosExtratoEntrega() {
		verificarSeMetodoCarregarDadosFoiChamado(Relatorio.extrato_entrega, extratoEntregaService);
	}

	@Test
	public void testeCarregarDadosDCA() {
		verificarSeMetodoCarregarDadosFoiChamado(Relatorio.dca, dcaService);

	}

	@Test
	public void testeCarregarDadosRGF() {
		verificarSeMetodoCarregarDadosFoiChamado(Relatorio.rgf, rgfService);

	}
	
	@Test
	public void testeCarregarDadosRREO() {
		verificarSeMetodoCarregarDadosFoiChamado(Relatorio.rreo, rreoService);
	}

	@Test
	public void testeCarregarDadosMSCPatrimonial() {
		verificarSeMetodoCarregarDadosFoiChamado(Relatorio.msc_patrimonial, mscPatrimonialService);
	}

	@Test
	public void testeCarregarDadosMSCOrcamentaria() {
		verificarSeMetodoCarregarDadosFoiChamado(Relatorio.msc_orcamentaria, mscOrcamentariaService);
	}

	@Test
	public void testeCarregarDadosMSCControle() {
		verificarSeMetodoCarregarDadosFoiChamado(Relatorio.msc_controle, mscControleService);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void verificarSeMetodoCarregarDadosFoiChamado(Relatorio relatorio, SiconfiService siconfiService) {
		System.setProperty(LeitorParametrosPrograma.OPCAO_RELATORIO, relatorio.name());
		doNothing().when(siconfiService).carregarDados(any());
		buscadorDados.buscarDados();
		verify(siconfiService).carregarDados(any());		
	}
}
