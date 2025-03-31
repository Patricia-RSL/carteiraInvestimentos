package com.backend.application.services;

import com.backend.application.dto.PortfolioAnalysisRequestDTO;
import com.backend.application.dto.PortfolioAnalysisResponseDTO;
import com.backend.application.dto.PortfolioAnalysisDetailItemDTO;
import com.backend.application.dto.PortfolioAnalysisSummaryDTO;
import com.backend.application.entities.UserTrade;
import com.backend.application.enums.OperationType;
import com.backend.application.factory.UserTradeMockFactory;
import com.backend.application.interfaces.PortfolioAnalysisDetailItemProjection;
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
import java.time.LocalDate;
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
class PortfolioAnalisysServiceTest {

    @Mock
    private UserTradeRepository userTradeRepository;

    @Mock
    private AnalysisCalculatorService analiseCalculatorService;

    @Spy
    @InjectMocks
    private PortfolioAnalisysService portfolioAnalisysService;


    private PortfolioAnalysisRequestDTO requestDTOComFiltro;
    private List<String> instruments;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private List<UserTrade> mockUserTrades;

    @BeforeEach
    void setUp() {
        mockUserTrades = UserTradeMockFactory.createMockUserTrades();

        instruments = mockUserTrades.stream()
                .map(UserTrade::getInstrument)
                .distinct()
                .toList();

        dataInicio = LocalDate.of(2019, 1, 1);
        dataFim = LocalDate.of(2019, 12, 31);

        requestDTOComFiltro = new PortfolioAnalysisRequestDTO();
        requestDTOComFiltro.setInstrumentList(instruments);
        requestDTOComFiltro.setBeginDate(dataInicio);
        requestDTOComFiltro.setEndDate(dataFim);
    }

    @ParameterizedTest
    @MethodSource("provideTestCasesForFindAllByTipoOperacaoAndInstrumentAndData")
    void findAllByTipoOperacaoAndInstrumentAndData_ok(OperationType tipo, List<UserTrade> expectedTrades) {
        when(userTradeRepository.findAllByOperationTypeAndInstrumentInAndDateGreaterThanEqualAndDateLessThanEqual(
                tipo, instruments, dataInicio, dataFim)).thenReturn(expectedTrades);

        List<UserTrade> result = portfolioAnalisysService.findAllByTipoOperacaoAndInstrumentAndData(
                tipo, instruments, dataInicio, dataFim);

        assertEquals(expectedTrades, result);
        verify(userTradeRepository).findAllByOperationTypeAndInstrumentInAndDateGreaterThanEqualAndDateLessThanEqual(
                tipo, instruments, dataInicio, dataFim);

    }

    private static Stream<Arguments> provideTestCasesForFindAllByTipoOperacaoAndInstrumentAndData() {
        LocalDateTime dataInicio = LocalDateTime.of(2019, 1, 1, 0, 0);
        LocalDateTime dataFim = LocalDateTime.of(2019, 12, 31, 0, 0);

        return Stream.of(
                Arguments.of(OperationType.v, UserTradeMockFactory.createMockUserTrades().stream()
                        .filter(trade -> trade.getOperationType() == OperationType.v && trade.getDate().isAfter(dataInicio) && trade.getDate().isBefore(dataFim))
                        .toList()),
                Arguments.of(OperationType.c, UserTradeMockFactory.createMockUserTrades().stream()
                        .filter(trade -> trade.getOperationType() == OperationType.c && trade.getDate().isAfter(dataInicio) && trade.getDate().isBefore(dataFim))
                        .toList())
        );
    }

    @Test
    void findPortfolioAnalysisDetailItems_ok() {

        List<PortfolioAnalysisDetailItemProjection> projections = Arrays.asList(
                createMockProjection("ITUB4F", 2, new BigDecimal("72.00")),
                createMockProjection("PETR4F", 6, new BigDecimal("142.26"))
        );

        instruments = new ArrayList<>();
        instruments.add(0,"ITUB4F");
        instruments.add(1,"PETR4F");
        requestDTOComFiltro.setInstrumentList(instruments);

        setupMockProjections(projections, requestDTOComFiltro);

        requestDTOComFiltro.setInstrumentList(instruments);
        List<PortfolioAnalysisDetailItemDTO> result = portfolioAnalisysService.findPortfolioAnalysisDetailItems(requestDTOComFiltro);

        // Assert
        assertEquals(2, result.size());
        assertEquals("ITUB4F", result.get(0).getInstrument());
        assertEquals(2, result.get(0).getInstrumentAmount());
        assertEquals(new BigDecimal("72.00"), result.get(0).getInvestedValue());
        assertNull(result.get(0).getMarketValue());
        assertNull(result.get(0).getPercentageYield());

        assertEquals("PETR4F", result.get(1).getInstrument());
        assertEquals(6, result.get(1).getInstrumentAmount());
        assertEquals(new BigDecimal("142.26"), result.get(1).getInvestedValue());
    }

    @Test
    void analyzePortfolio_ok() throws BadRequestException {
        PortfolioAnalysisRequestDTO portfolioAnalysisRequestDTO = new PortfolioAnalysisRequestDTO().init();
        PortfolioAnalysisResponseDTO expectedResponse = new PortfolioAnalysisResponseDTO();

        List<PortfolioAnalysisDetailItemProjection> projections = criarProjectionParaAnaliseCarteira();
        List<PortfolioAnalysisDetailItemDTO> detalhesCompletos = criarDetalhesCompletosParaAnaliseCarteira();

        setupMockProjections(projections, portfolioAnalysisRequestDTO);

        when(analiseCalculatorService.calculateMarketPrice(any(PortfolioAnalysisDetailItemDTO.class),
                any(PortfolioAnalysisRequestDTO.class)))
                .thenAnswer(invocation -> {
                    PortfolioAnalysisDetailItemDTO item = invocation.getArgument(0);

                    return detalhesCompletos.stream()
                            .filter(itemCompleto -> itemCompleto.getInstrument().equals(item.getInstrument()))
                            .findFirst()
                            .map(PortfolioAnalysisDetailItemDTO::getMarketValue)
                            .orElse(BigDecimal.ZERO);
                });

        when(analiseCalculatorService.calculatePercentageYield(any(PortfolioAnalysisDetailItemDTO.class)))
                .thenAnswer(invocation -> {
                    PortfolioAnalysisDetailItemDTO item = invocation.getArgument(0);

                    return detalhesCompletos.stream()
                            .filter(itemCompleto -> itemCompleto.getInstrument().equals(item.getInstrument()))
                            .findFirst()
                            .map(PortfolioAnalysisDetailItemDTO::getPercentageYield)
                            .orElse(BigDecimal.ZERO);
                });



        PortfolioAnalysisSummaryDTO portfolioAnalysisSummaryDTO = new PortfolioAnalysisSummaryDTO();
        portfolioAnalysisSummaryDTO.setInstrumentAmount(69);
        portfolioAnalysisSummaryDTO.setMarketValue(new BigDecimal("2249.85"));
        portfolioAnalysisSummaryDTO.setInvestedValue(new BigDecimal("1980.07"));
        portfolioAnalysisSummaryDTO.setPercentageYield(new BigDecimal("13.62"));

        when(analiseCalculatorService.calculateSummary(any(List.class)))
                .thenReturn(portfolioAnalysisSummaryDTO);


        expectedResponse.setPortfolioAnalysisDetail(detalhesCompletos);
        expectedResponse.setPortfolioAnalysisSummaryDTO(portfolioAnalysisSummaryDTO);

        PortfolioAnalysisResponseDTO result = portfolioAnalisysService.analyzePortfolio();

        assertIterableEquals(expectedResponse.getPortfolioAnalysisDetail(), result.getPortfolioAnalysisDetail());
        assertEquals(expectedResponse.getPortfolioAnalysisSummaryDTO(), result.getPortfolioAnalysisSummaryDTO());
    }

    private PortfolioAnalysisDetailItemProjection createMockProjection(String instrument, Integer qtdAcoes, BigDecimal valorInvestido) {
        return new PortfolioAnalysisDetailItemProjection() {
            @Override
            public String getInstrument() {
                return instrument;
            }

            @Override
            public Integer getInstrumentAmount() {
                return qtdAcoes;
            }

            @Override
            public BigDecimal getInvestedValue() {
                return valorInvestido;
            }
        };
    }


    @Test
    void analyzePortfolioComFiltro_ok() throws BadRequestException {
        PortfolioAnalysisResponseDTO expectedResponse = new PortfolioAnalysisResponseDTO();

        List<PortfolioAnalysisDetailItemProjection> projections = criarProjectionParaAnaliseCarteiraComFiltro();
        List<PortfolioAnalysisDetailItemDTO> detalhesCompletos = criarDetalhesCompletosParaAnaliseCarteiraComFiltro();

        setupMockProjections(projections, requestDTOComFiltro);

        when(analiseCalculatorService.calculateMarketPrice(any(PortfolioAnalysisDetailItemDTO.class), any(PortfolioAnalysisRequestDTO.class)))
                .thenAnswer(invocation -> {
                    PortfolioAnalysisDetailItemDTO item = invocation.getArgument(0);

                    return detalhesCompletos.stream()
                            .filter(itemCompleto -> itemCompleto.getInstrument().equals(item.getInstrument()))
                            .findFirst()
                            .map(PortfolioAnalysisDetailItemDTO::getMarketValue)
                            .orElse(BigDecimal.ZERO);
                });

        when(analiseCalculatorService.calculatePercentageYield(any(PortfolioAnalysisDetailItemDTO.class)))
                .thenAnswer(invocation -> {
                    PortfolioAnalysisDetailItemDTO item = invocation.getArgument(0);

                    return detalhesCompletos.stream()
                            .filter(itemCompleto -> itemCompleto.getInstrument().equals(item.getInstrument()))
                            .findFirst()
                            .map(PortfolioAnalysisDetailItemDTO::getPercentageYield)
                            .orElse(BigDecimal.ZERO);
                });

        PortfolioAnalysisSummaryDTO portfolioAnalysisSummaryDTO = new PortfolioAnalysisSummaryDTO();
        portfolioAnalysisSummaryDTO.setInstrumentAmount(71);
        portfolioAnalysisSummaryDTO.setMarketValue(new BigDecimal("2325.05"));
        portfolioAnalysisSummaryDTO.setInvestedValue(new BigDecimal("2052.07"));
        portfolioAnalysisSummaryDTO.setPercentageYield(new BigDecimal("13.30"));

        when(analiseCalculatorService.calculateSummary(any(List.class)))
                .thenReturn(portfolioAnalysisSummaryDTO);

        expectedResponse.setPortfolioAnalysisDetail(detalhesCompletos);
        expectedResponse.setPortfolioAnalysisSummaryDTO(portfolioAnalysisSummaryDTO);

        PortfolioAnalysisResponseDTO result = portfolioAnalisysService.analyzePortfolio(requestDTOComFiltro);

        assertIterableEquals(expectedResponse.getPortfolioAnalysisDetail(), result.getPortfolioAnalysisDetail());
        assertEquals(expectedResponse.getPortfolioAnalysisSummaryDTO(), result.getPortfolioAnalysisSummaryDTO());

    }



    @Test
    void setYield_ok() throws BadRequestException {

        UserTrade firstTrade = mockUserTrades.get(0);
        PortfolioAnalysisDetailItemDTO item = createItemDetalheFromTrade(firstTrade);
        BigDecimal valorMercado = new BigDecimal("200.00");
        BigDecimal rendimentoPorcentual = new BigDecimal("8.40");

        when(analiseCalculatorService.calculateMarketPrice(item, requestDTOComFiltro))
                .thenReturn(valorMercado);
        when(analiseCalculatorService.calculatePercentageYield(item))
                .thenReturn(rendimentoPorcentual);

        PortfolioAnalysisDetailItemDTO result = portfolioAnalisysService.setYield(item, requestDTOComFiltro);

        assertSame(item, result);
        assertEquals(valorMercado, result.getMarketValue());
        assertEquals(rendimentoPorcentual, result.getPercentageYield());

        verify(analiseCalculatorService).calculateMarketPrice(item, requestDTOComFiltro);
        verify(analiseCalculatorService).calculatePercentageYield(item);
    }

    //Métodos auxiliares
    private PortfolioAnalysisDetailItemDTO createItemDetalhe(String instrument, Integer qtdAcoes, BigDecimal valorInvestido) {
        PortfolioAnalysisDetailItemDTO item = new PortfolioAnalysisDetailItemDTO();
        item.setInstrument(instrument);
        item.setInstrumentAmount(qtdAcoes);
        item.setInvestedValue(valorInvestido);
        return item;
    }

    private PortfolioAnalysisDetailItemDTO createItemDetalheFromTrade(UserTrade trade) {
        return createItemDetalhe(
                trade.getInstrument(),
                trade.getAmount(),
                trade.getTotalValue()
        );
    }

    private void setupMockProjections(List<PortfolioAnalysisDetailItemProjection> projections, PortfolioAnalysisRequestDTO portfolioAnalysisRequestDTO) {
        when(userTradeRepository.getAmountAndValueByInstrument(
                portfolioAnalysisRequestDTO.getInstrumentList(),
                portfolioAnalysisRequestDTO.getBeginDate(),
                portfolioAnalysisRequestDTO.getEndDate())).thenReturn(projections);
    }

    private List<PortfolioAnalysisDetailItemProjection> criarProjectionParaAnaliseCarteira() {
        List<PortfolioAnalysisDetailItemProjection> mockItemDetalheList = new ArrayList<>();

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

    private List<PortfolioAnalysisDetailItemDTO> criarDetalhesCompletosParaAnaliseCarteira() {
        List<PortfolioAnalysisDetailItemDTO> detalhesCompletos = new ArrayList<>();

        // ITUB4F
        PortfolioAnalysisDetailItemDTO mockItub4f = new PortfolioAnalysisDetailItemDTO();
        mockItub4f.setInstrument("ITUB4F");
        mockItub4f.setInstrumentAmount(0);
        mockItub4f.setInvestedValue(new BigDecimal("-3.20"));
        mockItub4f.setMarketValue(new BigDecimal("0"));
        mockItub4f.setPercentageYield(new BigDecimal("0"));
        detalhesCompletos.add(mockItub4f);

        // PETR4F
        PortfolioAnalysisDetailItemDTO mockPetr4f = new PortfolioAnalysisDetailItemDTO();
        mockPetr4f.setInstrument("PETR4F");
        mockPetr4f.setInstrumentAmount(6);
        mockPetr4f.setInvestedValue(new BigDecimal("142.26"));
        mockPetr4f.setMarketValue(new BigDecimal("172.50"));
        mockPetr4f.setPercentageYield(new BigDecimal("21.26"));
        detalhesCompletos.add(mockPetr4f);

        // VALE3F
        PortfolioAnalysisDetailItemDTO mockVale3f = new PortfolioAnalysisDetailItemDTO();
        mockVale3f.setInstrument("VALE3F");
        mockVale3f.setInstrumentAmount(4);
        mockVale3f.setInvestedValue(new BigDecimal("204.56"));
        mockVale3f.setMarketValue(new BigDecimal("240.80"));
        mockVale3f.setPercentageYield(new BigDecimal("17.72"));
        detalhesCompletos.add(mockVale3f);

        // BBDC4
        PortfolioAnalysisDetailItemDTO mockBbdc4 = new PortfolioAnalysisDetailItemDTO();
        mockBbdc4.setInstrument("BBDC4");
        mockBbdc4.setInstrumentAmount(10);
        mockBbdc4.setInvestedValue(new BigDecimal("317.40"));
        mockBbdc4.setMarketValue(new BigDecimal("354.00"));
        mockBbdc4.setPercentageYield(new BigDecimal("11.53"));
        detalhesCompletos.add(mockBbdc4);

        // MGLU3
        PortfolioAnalysisDetailItemDTO mockMglu3 = new PortfolioAnalysisDetailItemDTO();
        mockMglu3.setInstrument("MGLU3");
        mockMglu3.setInstrumentAmount(15);
        mockMglu3.setInvestedValue(new BigDecimal("291.75"));
        mockMglu3.setMarketValue(new BigDecimal("252.75"));
        mockMglu3.setPercentageYield(new BigDecimal("-13.37"));
        detalhesCompletos.add(mockMglu3);

        // WEGE3
        PortfolioAnalysisDetailItemDTO mockWege3 = new PortfolioAnalysisDetailItemDTO();
        mockWege3.setInstrument("WEGE3");
        mockWege3.setInstrumentAmount(12);
        mockWege3.setInvestedValue(new BigDecimal("244.20"));
        mockWege3.setMarketValue(new BigDecimal("382.80"));
        mockWege3.setPercentageYield(new BigDecimal("56.76"));
        detalhesCompletos.add(mockWege3);

        // ABEV3
        PortfolioAnalysisDetailItemDTO mockAbev3 = new PortfolioAnalysisDetailItemDTO();
        mockAbev3.setInstrument("ABEV3");
        mockAbev3.setInstrumentAmount(20);
        mockAbev3.setInvestedValue(new BigDecimal("357.60"));
        mockAbev3.setMarketValue(new BigDecimal("389.00"));
        mockAbev3.setPercentageYield(new BigDecimal("8.78"));
        detalhesCompletos.add(mockAbev3);

        // BBAS3
        PortfolioAnalysisDetailItemDTO mockBbas3 = new PortfolioAnalysisDetailItemDTO();
        mockBbas3.setInstrument("BBAS3");
        mockBbas3.setInstrumentAmount(10);
        mockBbas3.setInvestedValue(new BigDecimal("422.30"));
        mockBbas3.setMarketValue(new BigDecimal("458.00"));
        mockBbas3.setPercentageYield(new BigDecimal("8.45"));
        detalhesCompletos.add(mockBbas3);

        return detalhesCompletos;
    }

    private List<PortfolioAnalysisDetailItemDTO> criarDetalhesCompletosParaAnaliseCarteiraComFiltro() {
        List<PortfolioAnalysisDetailItemDTO> detalhesCompletos = new ArrayList<>();

        // ITUB4F
        PortfolioAnalysisDetailItemDTO mockItub4f = new PortfolioAnalysisDetailItemDTO();
        mockItub4f.setInstrument("ITUB4F");
        mockItub4f.setInstrumentAmount(2);
        mockItub4f.setInvestedValue(new BigDecimal("72.00"));
        mockItub4f.setMarketValue(new BigDecimal("75.20"));
        mockItub4f.setPercentageYield(new BigDecimal("4.44"));
        detalhesCompletos.add(mockItub4f);

        // PETR4F
        PortfolioAnalysisDetailItemDTO mockPetr4f = new PortfolioAnalysisDetailItemDTO();
        mockPetr4f.setInstrument("PETR4F");
        mockPetr4f.setInstrumentAmount(6);
        mockPetr4f.setInvestedValue(new BigDecimal("142.26"));
        mockPetr4f.setMarketValue(new BigDecimal("172.50"));
        mockPetr4f.setPercentageYield(new BigDecimal("21.26"));
        detalhesCompletos.add(mockPetr4f);

        // VALE3F
        PortfolioAnalysisDetailItemDTO mockVale3f = new PortfolioAnalysisDetailItemDTO();
        mockVale3f.setInstrument("VALE3F");
        mockVale3f.setInstrumentAmount(4);
        mockVale3f.setInvestedValue(new BigDecimal("204.56"));
        mockVale3f.setMarketValue(new BigDecimal("240.80"));
        mockVale3f.setPercentageYield(new BigDecimal("17.72"));
        detalhesCompletos.add(mockVale3f);

        // BBDC4
        PortfolioAnalysisDetailItemDTO mockBbdc4 = new PortfolioAnalysisDetailItemDTO();
        mockBbdc4.setInstrument("BBDC4");
        mockBbdc4.setInstrumentAmount(10);
        mockBbdc4.setInvestedValue(new BigDecimal("317.40"));
        mockBbdc4.setMarketValue(new BigDecimal("354.00"));
        mockBbdc4.setPercentageYield(new BigDecimal("11.53"));
        detalhesCompletos.add(mockBbdc4);

        // MGLU3
        PortfolioAnalysisDetailItemDTO mockMglu3 = new PortfolioAnalysisDetailItemDTO();
        mockMglu3.setInstrument("MGLU3");
        mockMglu3.setInstrumentAmount(15);
        mockMglu3.setInvestedValue(new BigDecimal("291.75"));
        mockMglu3.setMarketValue(new BigDecimal("252.75"));
        mockMglu3.setPercentageYield(new BigDecimal("-13.37"));
        detalhesCompletos.add(mockMglu3);

        // WEGE3
        PortfolioAnalysisDetailItemDTO mockWege3 = new PortfolioAnalysisDetailItemDTO();
        mockWege3.setInstrument("WEGE3");
        mockWege3.setInstrumentAmount(12);
        mockWege3.setInvestedValue(new BigDecimal("244.20"));
        mockWege3.setMarketValue(new BigDecimal("382.80"));
        mockWege3.setPercentageYield(new BigDecimal("56.76"));
        detalhesCompletos.add(mockWege3);

        // ABEV3
        PortfolioAnalysisDetailItemDTO mockAbev3 = new PortfolioAnalysisDetailItemDTO();
        mockAbev3.setInstrument("ABEV3");
        mockAbev3.setInstrumentAmount(20);
        mockAbev3.setInvestedValue(new BigDecimal("357.60"));
        mockAbev3.setMarketValue(new BigDecimal("389.00"));
        mockAbev3.setPercentageYield(new BigDecimal("8.78"));
        detalhesCompletos.add(mockAbev3);

        // BBAS3
        PortfolioAnalysisDetailItemDTO mockBbas3 = new PortfolioAnalysisDetailItemDTO();
        mockBbas3.setInstrument("BBAS3");
        mockBbas3.setInstrumentAmount(10);
        mockBbas3.setInvestedValue(new BigDecimal("422.30"));
        mockBbas3.setMarketValue(new BigDecimal("458.00"));
        mockBbas3.setPercentageYield(new BigDecimal("8.45"));
        detalhesCompletos.add(mockBbas3);

        return detalhesCompletos;
    }

    private List<PortfolioAnalysisDetailItemProjection> criarProjectionParaAnaliseCarteiraComFiltro() {
        List<PortfolioAnalysisDetailItemProjection> mockItemDetalheList = new ArrayList<>();

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
