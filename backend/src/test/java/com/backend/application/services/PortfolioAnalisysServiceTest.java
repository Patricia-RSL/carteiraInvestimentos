package com.backend.application.services;

import com.backend.application.dto.PortfolioAnalysisDetailItemDTO;
import com.backend.application.dto.PortfolioAnalysisRequestDTO;
import com.backend.application.entities.UserTrade;
import com.backend.application.factory.UserTradeMockFactory;
import com.backend.application.interfaces.PortfolioAnalysisDetailItemProjection;
import com.backend.application.repository.UserTradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PortfolioAnalisysServiceTest {

  @Mock
  private UserTradeRepository userTradeRepository;

  @Mock
  private AnalysisCalculatorService analiseCalculatorService;

  @Mock
  private AuthService authService;

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

    // Mock AuthService to return a user ID
    when(authService.getCurrentUser()).thenReturn(1L);
  }

  @Test
  void findPortfolioAnalysisDetailItems_ok() {
    List<PortfolioAnalysisDetailItemProjection> projections = Arrays.asList(
      createMockProjection("ITUB4F", 2, new BigDecimal("72.00")),
      createMockProjection("PETR4F", 6, new BigDecimal("142.26"))
    );

    instruments = new ArrayList<>();
    instruments.add("ITUB4F");
    instruments.add("PETR4F");
    requestDTOComFiltro.setInstrumentList(instruments);

    when(userTradeRepository.getAmountAndValueByInstrument(
      requestDTOComFiltro.getInstrumentList(),
      requestDTOComFiltro.getBeginDate(),
      requestDTOComFiltro.getEndDate(),
      1L)).thenReturn(projections);

    List<PortfolioAnalysisDetailItemDTO> result = portfolioAnalisysService.findPortfolioAnalysisDetailItems(requestDTOComFiltro);

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
}
