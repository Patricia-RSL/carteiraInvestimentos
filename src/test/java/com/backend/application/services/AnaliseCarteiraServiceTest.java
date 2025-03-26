package com.backend.application.services;

import com.backend.application.dto.AnaliseCarteiraRequestDTO;
import com.backend.application.dto.AnaliseCarteiraResponseDTO;
import com.backend.application.dto.ItemDetalhesAnaliseCarteiraDTO;
import com.backend.application.dto.ResumoAnaliseCarteiraDTO;
import com.backend.application.entities.UserTrade;
import com.backend.application.enums.TipoOperacao;
import com.backend.application.factory.UserTradeMockFactory;
import com.backend.application.interfaces.ItemDetalhesAnaliseCarteiraProjection;
import com.backend.application.repository.UserTradeRepository;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AnaliseCarteiraServiceTest {

    @Mock
    private UserTradeRepository userTradeRepository;

    @Mock
    private CarteiraCalculatorService analiseCalculatorService;

    @Spy
    @InjectMocks
    private AnaliseCarteiraService analiseCarteiraService;


    private AnaliseCarteiraRequestDTO requestDTOComFiltro;
    private List<String> instruments;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private List<UserTrade> mockUserTrades;

    @BeforeEach
    void setUp() {
        mockUserTrades = UserTradeMockFactory.createMockUserTrades();

        instruments = mockUserTrades.stream()
                .map(UserTrade::getInstrument)
                .distinct()
                .toList();

        dataInicio = LocalDateTime.of(2019, 1, 1, 0, 0);
        dataFim = LocalDateTime.of(2019, 12, 31, 0, 0);

        requestDTOComFiltro = new AnaliseCarteiraRequestDTO();
        requestDTOComFiltro.setInstrumentList(instruments);
        requestDTOComFiltro.setDataInicio(dataInicio);
        requestDTOComFiltro.setDataFim(dataFim);
    }

    @ParameterizedTest
    @MethodSource("provideTestCasesForFindAllByTipoOperacaoAndInstrumentAndData")
    void findAllByTipoOperacaoAndInstrumentAndData_ok(TipoOperacao tipo, List<UserTrade> expectedTrades) {
        when(userTradeRepository.findAllByTipoOperacaoAndInstrumentInAndDataGreaterThanEqualAndDataLessThanEqual(
                tipo, instruments, dataInicio, dataFim)).thenReturn(expectedTrades);

        List<UserTrade> result = analiseCarteiraService.findAllByTipoOperacaoAndInstrumentAndData(
                tipo, instruments, dataInicio, dataFim);

        assertEquals(expectedTrades, result);
        verify(userTradeRepository).findAllByTipoOperacaoAndInstrumentInAndDataGreaterThanEqualAndDataLessThanEqual(
                tipo, instruments, dataInicio, dataFim);

    }

    private static Stream<Arguments> provideTestCasesForFindAllByTipoOperacaoAndInstrumentAndData() {
        LocalDateTime dataInicio = LocalDateTime.of(2019, 1, 1, 0, 0);
        LocalDateTime dataFim = LocalDateTime.of(2019, 12, 31, 0, 0);

        return Stream.of(
                Arguments.of(TipoOperacao.v, UserTradeMockFactory.createMockUserTrades().stream()
                        .filter(trade -> trade.getTipoOperacao() == TipoOperacao.v && trade.getData().isAfter(dataInicio) && trade.getData().isBefore(dataFim))
                        .toList()),
                Arguments.of(TipoOperacao.c, UserTradeMockFactory.createMockUserTrades().stream()
                        .filter(trade -> trade.getTipoOperacao() == TipoOperacao.c && trade.getData().isAfter(dataInicio) && trade.getData().isBefore(dataFim))
                        .toList())
        );
    }

    @Test
    void obterItensDetalhesAnalise_ok() {

        List<ItemDetalhesAnaliseCarteiraProjection> projections = Arrays.asList(
                createMockProjection("ITUB4F", 2, new BigDecimal("72.00")),
                createMockProjection("PETR4F", 6, new BigDecimal("142.26"))
        );

        instruments = new ArrayList<>();
        instruments.add(0,"ITUB4F");
        instruments.add(1,"PETR4F");
        requestDTOComFiltro.setInstrumentList(instruments);

        setupMockProjections(projections, requestDTOComFiltro);

        requestDTOComFiltro.setInstrumentList(instruments);
        List<ItemDetalhesAnaliseCarteiraDTO> result = analiseCarteiraService.obterItensDetalhesAnalise(requestDTOComFiltro);

        // Assert
        assertEquals(2, result.size());
        assertEquals("ITUB4F", result.get(0).getInstrument());
        assertEquals(2, result.get(0).getQtdAcoes());
        assertEquals(new BigDecimal("72.00"), result.get(0).getValorInvestido());
        assertNull(result.get(0).getValorMercado());
        assertNull(result.get(0).getRendimentosPorcentagem());

        assertEquals("PETR4F", result.get(1).getInstrument());
        assertEquals(6, result.get(1).getQtdAcoes());
        assertEquals(new BigDecimal("142.26"), result.get(1).getValorInvestido());
    }

    @Test
    void analisarCarteira_ok() throws BadRequestException {
        AnaliseCarteiraRequestDTO analiseCarteiraRequestDTO = new AnaliseCarteiraRequestDTO().init();
        AnaliseCarteiraResponseDTO expectedResponse = new AnaliseCarteiraResponseDTO();

        List<ItemDetalhesAnaliseCarteiraProjection> projections = criarProjectionParaAnaliseCarteira();
        List<ItemDetalhesAnaliseCarteiraDTO> detalhesCompletos = criarDetalhesCompletosParaAnaliseCarteira();

        setupMockProjections(projections, analiseCarteiraRequestDTO);

        when(analiseCalculatorService.calcularValorMercado(any(ItemDetalhesAnaliseCarteiraDTO.class),
                any(AnaliseCarteiraRequestDTO.class)))
                .thenAnswer(invocation -> {
                    ItemDetalhesAnaliseCarteiraDTO item = invocation.getArgument(0);

                    return detalhesCompletos.stream()
                            .filter(itemCompleto -> itemCompleto.getInstrument().equals(item.getInstrument()))
                            .findFirst()
                            .map(ItemDetalhesAnaliseCarteiraDTO::getValorMercado)
                            .orElse(BigDecimal.ZERO);
                });

        when(analiseCalculatorService.calcularRendimentoPorcentual(any(ItemDetalhesAnaliseCarteiraDTO.class)))
                .thenAnswer(invocation -> {
                    ItemDetalhesAnaliseCarteiraDTO item = invocation.getArgument(0);

                    return detalhesCompletos.stream()
                            .filter(itemCompleto -> itemCompleto.getInstrument().equals(item.getInstrument()))
                            .findFirst()
                            .map(ItemDetalhesAnaliseCarteiraDTO::getRendimentosPorcentagem)
                            .orElse(BigDecimal.ZERO);
                });



        ResumoAnaliseCarteiraDTO resumoAnaliseCarteiraDTO = new ResumoAnaliseCarteiraDTO();
        resumoAnaliseCarteiraDTO.setTotalAcoes(69);
        resumoAnaliseCarteiraDTO.setValorMercado(new BigDecimal("2249.85"));
        resumoAnaliseCarteiraDTO.setValorInvestido(new BigDecimal("1980.07"));
        resumoAnaliseCarteiraDTO.setRendimentosPorcentagem(new BigDecimal("13.62"));

        when(analiseCalculatorService.calcularResumo(any(List.class)))
                .thenReturn(resumoAnaliseCarteiraDTO);


        expectedResponse.setDetalhesAnaliseCarteiraDTO(detalhesCompletos);
        expectedResponse.setResumoAnaliseCarteiraDTO(resumoAnaliseCarteiraDTO);

        AnaliseCarteiraResponseDTO result = analiseCarteiraService.analisarCarteira();

        assertIterableEquals(expectedResponse.getDetalhesAnaliseCarteiraDTO(), result.getDetalhesAnaliseCarteiraDTO());
        assertEquals(expectedResponse.getResumoAnaliseCarteiraDTO(), result.getResumoAnaliseCarteiraDTO());
    }

    private ItemDetalhesAnaliseCarteiraProjection createMockProjection(String instrument, Integer qtdAcoes, BigDecimal valorInvestido) {
        return new ItemDetalhesAnaliseCarteiraProjection() {
            @Override
            public String getInstrument() {
                return instrument;
            }

            @Override
            public Integer getTotalAcoes() {
                return qtdAcoes;
            }

            @Override
            public BigDecimal getSaldoInvestido() {
                return valorInvestido;
            }
        };
    }


    @Test
    void analisarCarteiraComFiltro_ok() throws BadRequestException {
        AnaliseCarteiraResponseDTO expectedResponse = new AnaliseCarteiraResponseDTO();

        List<ItemDetalhesAnaliseCarteiraProjection> projections = criarProjectionParaAnaliseCarteiraComFiltro();
        List<ItemDetalhesAnaliseCarteiraDTO> detalhesCompletos = criarDetalhesCompletosParaAnaliseCarteiraComFiltro();

        setupMockProjections(projections, requestDTOComFiltro);

        when(analiseCalculatorService.calcularValorMercado(any(ItemDetalhesAnaliseCarteiraDTO.class), any(AnaliseCarteiraRequestDTO.class)))
                .thenAnswer(invocation -> {
                    ItemDetalhesAnaliseCarteiraDTO item = invocation.getArgument(0);

                    return detalhesCompletos.stream()
                            .filter(itemCompleto -> itemCompleto.getInstrument().equals(item.getInstrument()))
                            .findFirst()
                            .map(ItemDetalhesAnaliseCarteiraDTO::getValorMercado)
                            .orElse(BigDecimal.ZERO);
                });

        when(analiseCalculatorService.calcularRendimentoPorcentual(any(ItemDetalhesAnaliseCarteiraDTO.class)))
                .thenAnswer(invocation -> {
                    ItemDetalhesAnaliseCarteiraDTO item = invocation.getArgument(0);

                    return detalhesCompletos.stream()
                            .filter(itemCompleto -> itemCompleto.getInstrument().equals(item.getInstrument()))
                            .findFirst()
                            .map(ItemDetalhesAnaliseCarteiraDTO::getRendimentosPorcentagem)
                            .orElse(BigDecimal.ZERO);
                });

        ResumoAnaliseCarteiraDTO resumoAnaliseCarteiraDTO = new ResumoAnaliseCarteiraDTO();
        resumoAnaliseCarteiraDTO.setTotalAcoes(71);
        resumoAnaliseCarteiraDTO.setValorMercado(new BigDecimal("2325.05"));
        resumoAnaliseCarteiraDTO.setValorInvestido(new BigDecimal("2052.07"));
        resumoAnaliseCarteiraDTO.setRendimentosPorcentagem(new BigDecimal("13.30"));

        when(analiseCalculatorService.calcularResumo(any(List.class)))
                .thenReturn(resumoAnaliseCarteiraDTO);

        expectedResponse.setDetalhesAnaliseCarteiraDTO(detalhesCompletos);
        expectedResponse.setResumoAnaliseCarteiraDTO(resumoAnaliseCarteiraDTO);

        AnaliseCarteiraResponseDTO result = analiseCarteiraService.analisarCarteiraComFiltro(requestDTOComFiltro);

        assertIterableEquals(expectedResponse.getDetalhesAnaliseCarteiraDTO(), result.getDetalhesAnaliseCarteiraDTO());
        assertEquals(expectedResponse.getResumoAnaliseCarteiraDTO(), result.getResumoAnaliseCarteiraDTO());

    }



    @Test
    void calcularRendimentoItemDetalhe_ok() throws BadRequestException {

        UserTrade firstTrade = mockUserTrades.get(0);
        ItemDetalhesAnaliseCarteiraDTO item = createItemDetalheFromTrade(firstTrade);
        BigDecimal valorMercado = new BigDecimal("200.00");
        BigDecimal rendimentoPorcentual = new BigDecimal("8.40");

        when(analiseCalculatorService.calcularValorMercado(item, requestDTOComFiltro))
                .thenReturn(valorMercado);
        when(analiseCalculatorService.calcularRendimentoPorcentual(item))
                .thenReturn(rendimentoPorcentual);

        ItemDetalhesAnaliseCarteiraDTO result = analiseCarteiraService.calcularRendimentoItemDetalhe(item, requestDTOComFiltro);

        assertSame(item, result);
        assertEquals(valorMercado, result.getValorMercado());
        assertEquals(rendimentoPorcentual, result.getRendimentosPorcentagem());

        verify(analiseCalculatorService).calcularValorMercado(item, requestDTOComFiltro);
        verify(analiseCalculatorService).calcularRendimentoPorcentual(item);
    }

    //Métodos auxiliares
    private ItemDetalhesAnaliseCarteiraDTO createItemDetalhe(String instrument, Integer qtdAcoes, BigDecimal valorInvestido) {
        ItemDetalhesAnaliseCarteiraDTO item = new ItemDetalhesAnaliseCarteiraDTO();
        item.setInstrument(instrument);
        item.setQtdAcoes(qtdAcoes);
        item.setValorInvestido(valorInvestido);
        return item;
    }

    private ItemDetalhesAnaliseCarteiraDTO createItemDetalheFromTrade(UserTrade trade) {
        return createItemDetalhe(
                trade.getInstrument(),
                trade.getQuantidade(),
                trade.getValorTotal()
        );
    }

    private void setupMockProjections(List<ItemDetalhesAnaliseCarteiraProjection> projections, AnaliseCarteiraRequestDTO analiseCarteiraRequestDTO) {
        when(userTradeRepository.getQuantidadeAndSaldoPorInstrument(
                analiseCarteiraRequestDTO.getInstrumentList(),
                analiseCarteiraRequestDTO.getDataInicio(),
                analiseCarteiraRequestDTO.getDataFim())).thenReturn(projections);
    }

    private List<ItemDetalhesAnaliseCarteiraProjection> criarProjectionParaAnaliseCarteira() {
        List<ItemDetalhesAnaliseCarteiraProjection> mockItemDetalheList = new ArrayList<>();

        mockItemDetalheList.add(createMockProjection("ITUB4F", 0, new BigDecimal("-3.20")));
        mockItemDetalheList.add(createMockProjection("PETR4F", 6, new BigDecimal("142.26")));
        mockItemDetalheList.add(createMockProjection("VALE3F", 4, new BigDecimal("204.56")));
        mockItemDetalheList.add(createMockProjection("BBDC4", 10, new BigDecimal("317.40")));
        mockItemDetalheList.add(createMockProjection("MGLU3", 15, new BigDecimal("291.75")));
        mockItemDetalheList.add(createMockProjection("WEGE3", 12, new BigDecimal("244.20")));
        mockItemDetalheList.add(createMockProjection("ABEV3", 20, new BigDecimal("357.60")));
        mockItemDetalheList.add(createMockProjection("BBAS3", 10, new BigDecimal("422.30")));



        return mockItemDetalheList;
    }

    private List<ItemDetalhesAnaliseCarteiraDTO> criarDetalhesCompletosParaAnaliseCarteira() {
        List<ItemDetalhesAnaliseCarteiraDTO> detalhesCompletos = new ArrayList<>();

        // ITUB4F
        ItemDetalhesAnaliseCarteiraDTO mockItub4f = new ItemDetalhesAnaliseCarteiraDTO();
        mockItub4f.setInstrument("ITUB4F");
        mockItub4f.setQtdAcoes(0);
        mockItub4f.setValorInvestido(new BigDecimal("-3.20"));
        mockItub4f.setValorMercado(new BigDecimal("0"));
        mockItub4f.setRendimentosPorcentagem(new BigDecimal("0"));
        detalhesCompletos.add(mockItub4f);

        // PETR4F
        ItemDetalhesAnaliseCarteiraDTO mockPetr4f = new ItemDetalhesAnaliseCarteiraDTO();
        mockPetr4f.setInstrument("PETR4F");
        mockPetr4f.setQtdAcoes(6);
        mockPetr4f.setValorInvestido(new BigDecimal("142.26"));
        mockPetr4f.setValorMercado(new BigDecimal("172.50"));
        mockPetr4f.setRendimentosPorcentagem(new BigDecimal("21.26"));
        detalhesCompletos.add(mockPetr4f);

        // VALE3F
        ItemDetalhesAnaliseCarteiraDTO mockVale3f = new ItemDetalhesAnaliseCarteiraDTO();
        mockVale3f.setInstrument("VALE3F");
        mockVale3f.setQtdAcoes(4);
        mockVale3f.setValorInvestido(new BigDecimal("204.56"));
        mockVale3f.setValorMercado(new BigDecimal("240.80"));
        mockVale3f.setRendimentosPorcentagem(new BigDecimal("17.72"));
        detalhesCompletos.add(mockVale3f);

        // BBDC4
        ItemDetalhesAnaliseCarteiraDTO mockBbdc4 = new ItemDetalhesAnaliseCarteiraDTO();
        mockBbdc4.setInstrument("BBDC4");
        mockBbdc4.setQtdAcoes(10);
        mockBbdc4.setValorInvestido(new BigDecimal("317.40"));
        mockBbdc4.setValorMercado(new BigDecimal("354.00"));
        mockBbdc4.setRendimentosPorcentagem(new BigDecimal("11.53"));
        detalhesCompletos.add(mockBbdc4);

        // MGLU3
        ItemDetalhesAnaliseCarteiraDTO mockMglu3 = new ItemDetalhesAnaliseCarteiraDTO();
        mockMglu3.setInstrument("MGLU3");
        mockMglu3.setQtdAcoes(15);
        mockMglu3.setValorInvestido(new BigDecimal("291.75"));
        mockMglu3.setValorMercado(new BigDecimal("252.75"));
        mockMglu3.setRendimentosPorcentagem(new BigDecimal("-13.37"));
        detalhesCompletos.add(mockMglu3);

        // WEGE3
        ItemDetalhesAnaliseCarteiraDTO mockWege3 = new ItemDetalhesAnaliseCarteiraDTO();
        mockWege3.setInstrument("WEGE3");
        mockWege3.setQtdAcoes(12);
        mockWege3.setValorInvestido(new BigDecimal("244.20"));
        mockWege3.setValorMercado(new BigDecimal("382.80"));
        mockWege3.setRendimentosPorcentagem(new BigDecimal("56.76"));
        detalhesCompletos.add(mockWege3);

        // ABEV3
        ItemDetalhesAnaliseCarteiraDTO mockAbev3 = new ItemDetalhesAnaliseCarteiraDTO();
        mockAbev3.setInstrument("ABEV3");
        mockAbev3.setQtdAcoes(20);
        mockAbev3.setValorInvestido(new BigDecimal("357.60"));
        mockAbev3.setValorMercado(new BigDecimal("389.00"));
        mockAbev3.setRendimentosPorcentagem(new BigDecimal("8.78"));
        detalhesCompletos.add(mockAbev3);

        // BBAS3
        ItemDetalhesAnaliseCarteiraDTO mockBbas3 = new ItemDetalhesAnaliseCarteiraDTO();
        mockBbas3.setInstrument("BBAS3");
        mockBbas3.setQtdAcoes(10);
        mockBbas3.setValorInvestido(new BigDecimal("422.30"));
        mockBbas3.setValorMercado(new BigDecimal("458.00"));
        mockBbas3.setRendimentosPorcentagem(new BigDecimal("8.45"));
        detalhesCompletos.add(mockBbas3);

        return detalhesCompletos;
    }

    private List<ItemDetalhesAnaliseCarteiraDTO> criarDetalhesCompletosParaAnaliseCarteiraComFiltro() {
        List<ItemDetalhesAnaliseCarteiraDTO> detalhesCompletos = new ArrayList<>();

        // ITUB4F
        ItemDetalhesAnaliseCarteiraDTO mockItub4f = new ItemDetalhesAnaliseCarteiraDTO();
        mockItub4f.setInstrument("ITUB4F");
        mockItub4f.setQtdAcoes(2);
        mockItub4f.setValorInvestido(new BigDecimal("72.00"));
        mockItub4f.setValorMercado(new BigDecimal("75.20"));
        mockItub4f.setRendimentosPorcentagem(new BigDecimal("4.44"));
        detalhesCompletos.add(mockItub4f);

        // PETR4F
        ItemDetalhesAnaliseCarteiraDTO mockPetr4f = new ItemDetalhesAnaliseCarteiraDTO();
        mockPetr4f.setInstrument("PETR4F");
        mockPetr4f.setQtdAcoes(6);
        mockPetr4f.setValorInvestido(new BigDecimal("142.26"));
        mockPetr4f.setValorMercado(new BigDecimal("172.50"));
        mockPetr4f.setRendimentosPorcentagem(new BigDecimal("21.26"));
        detalhesCompletos.add(mockPetr4f);

        // VALE3F
        ItemDetalhesAnaliseCarteiraDTO mockVale3f = new ItemDetalhesAnaliseCarteiraDTO();
        mockVale3f.setInstrument("VALE3F");
        mockVale3f.setQtdAcoes(4);
        mockVale3f.setValorInvestido(new BigDecimal("204.56"));
        mockVale3f.setValorMercado(new BigDecimal("240.80"));
        mockVale3f.setRendimentosPorcentagem(new BigDecimal("17.72"));
        detalhesCompletos.add(mockVale3f);

        // BBDC4
        ItemDetalhesAnaliseCarteiraDTO mockBbdc4 = new ItemDetalhesAnaliseCarteiraDTO();
        mockBbdc4.setInstrument("BBDC4");
        mockBbdc4.setQtdAcoes(10);
        mockBbdc4.setValorInvestido(new BigDecimal("317.40"));
        mockBbdc4.setValorMercado(new BigDecimal("354.00"));
        mockBbdc4.setRendimentosPorcentagem(new BigDecimal("11.53"));
        detalhesCompletos.add(mockBbdc4);

        // MGLU3
        ItemDetalhesAnaliseCarteiraDTO mockMglu3 = new ItemDetalhesAnaliseCarteiraDTO();
        mockMglu3.setInstrument("MGLU3");
        mockMglu3.setQtdAcoes(15);
        mockMglu3.setValorInvestido(new BigDecimal("291.75"));
        mockMglu3.setValorMercado(new BigDecimal("252.75"));
        mockMglu3.setRendimentosPorcentagem(new BigDecimal("-13.37"));
        detalhesCompletos.add(mockMglu3);

        // WEGE3
        ItemDetalhesAnaliseCarteiraDTO mockWege3 = new ItemDetalhesAnaliseCarteiraDTO();
        mockWege3.setInstrument("WEGE3");
        mockWege3.setQtdAcoes(12);
        mockWege3.setValorInvestido(new BigDecimal("244.20"));
        mockWege3.setValorMercado(new BigDecimal("382.80"));
        mockWege3.setRendimentosPorcentagem(new BigDecimal("56.76"));
        detalhesCompletos.add(mockWege3);

        // ABEV3
        ItemDetalhesAnaliseCarteiraDTO mockAbev3 = new ItemDetalhesAnaliseCarteiraDTO();
        mockAbev3.setInstrument("ABEV3");
        mockAbev3.setQtdAcoes(20);
        mockAbev3.setValorInvestido(new BigDecimal("357.60"));
        mockAbev3.setValorMercado(new BigDecimal("389.00"));
        mockAbev3.setRendimentosPorcentagem(new BigDecimal("8.78"));
        detalhesCompletos.add(mockAbev3);

        // BBAS3
        ItemDetalhesAnaliseCarteiraDTO mockBbas3 = new ItemDetalhesAnaliseCarteiraDTO();
        mockBbas3.setInstrument("BBAS3");
        mockBbas3.setQtdAcoes(10);
        mockBbas3.setValorInvestido(new BigDecimal("422.30"));
        mockBbas3.setValorMercado(new BigDecimal("458.00"));
        mockBbas3.setRendimentosPorcentagem(new BigDecimal("8.45"));
        detalhesCompletos.add(mockBbas3);

        return detalhesCompletos;
    }

    private List<ItemDetalhesAnaliseCarteiraProjection> criarProjectionParaAnaliseCarteiraComFiltro() {
        List<ItemDetalhesAnaliseCarteiraProjection> mockItemDetalheList = new ArrayList<>();

        mockItemDetalheList.add(createMockProjection("ITUB4F", 2, new BigDecimal("72.00")));
        mockItemDetalheList.add(createMockProjection("PETR4F", 6, new BigDecimal("142.26")));
        mockItemDetalheList.add(createMockProjection("VALE3F", 4, new BigDecimal("204.56")));
        mockItemDetalheList.add(createMockProjection("BBDC4", 10, new BigDecimal("317.40")));
        mockItemDetalheList.add(createMockProjection("MGLU3", 15, new BigDecimal("291.75")));
        mockItemDetalheList.add(createMockProjection("WEGE3", 12, new BigDecimal("244.20")));
        mockItemDetalheList.add(createMockProjection("ABEV3", 20, new BigDecimal("357.60")));
        mockItemDetalheList.add(createMockProjection("BBAS3", 10, new BigDecimal("422.30")));



        return mockItemDetalheList;
    }

}