package com.backend.application.services;

import com.backend.application.dto.PortfolioAnalysisRequestDTO;
import com.backend.application.dto.PortfolioAnalysisDetailItemDTO;
import com.backend.application.dto.PortfolioAnalysisSummaryDTO;
import com.backend.application.entities.InstrumentQuote;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnalysisCalculatorServiceTest {

    @Mock
    private InstrumentQuoteService instrumentQuoteService;

    @InjectMocks
    private AnalysisCalculatorService analisysCalculatorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void calculateMarketPrice_validInstrumentQuote_returnsCorrectValue() throws BadRequestException {
        PortfolioAnalysisDetailItemDTO item = new PortfolioAnalysisDetailItemDTO();
        item.setInstrument("AAPL");
        item.setInstrumentAmount(10);
        PortfolioAnalysisRequestDTO request = new PortfolioAnalysisRequestDTO();
        request.setEndDate(LocalDate.of(2023, 10, 10));

        InstrumentQuote quote = new InstrumentQuote();
        quote.setClosePrice(BigDecimal.valueOf(150));
        when(instrumentQuoteService.findBySymbolAndDate("AAPL", request.getEndDate())).thenReturn(Optional.of(quote));

        BigDecimal result = analisysCalculatorService.calculateMarketPrice(item, request);

        assertEquals(BigDecimal.valueOf(1500), result);
    }

    @Test
    void calculatePercentageYield_validInputs_returnsCorrectPercentage() {
        PortfolioAnalysisDetailItemDTO item = new PortfolioAnalysisDetailItemDTO();
        item.setMarketValue(BigDecimal.valueOf(1500));
        item.setInvestedValue(BigDecimal.valueOf(1000));
        item.setInstrumentAmount(10);

        BigDecimal result = analisysCalculatorService.calculatePercentageYield(item);

        assertEquals(BigDecimal.valueOf(50.00).setScale(2, RoundingMode.HALF_UP), result);
    }

    @Test
    void calculatePercentageYield_nullMarketValue_ReturnNull() {
        PortfolioAnalysisDetailItemDTO item = new PortfolioAnalysisDetailItemDTO();
        item.setMarketValue(null);
        item.setInvestedValue(BigDecimal.valueOf(1000));
        item.setInstrumentAmount(10);

				BigDecimal result = analisysCalculatorService.calculatePercentageYield(item);

				assertNull(result);
    }

    @Test
    void calculatePercentageYield_zeroQtdAcoes_returnsNull(){
        PortfolioAnalysisDetailItemDTO item = new PortfolioAnalysisDetailItemDTO();
        item.setMarketValue(BigDecimal.valueOf(1500));
        item.setInvestedValue(BigDecimal.valueOf(-500));
        item.setInstrumentAmount(0);

        BigDecimal result = analisysCalculatorService.calculatePercentageYield(item);

        assertNull(result);
    }

    @Test
    void calculateSummary_ok() throws BadRequestException {
        PortfolioAnalysisDetailItemDTO item1 = new PortfolioAnalysisDetailItemDTO();
        item1.setMarketValue(BigDecimal.valueOf(1500));
        item1.setInvestedValue(BigDecimal.valueOf(1000));
        item1.setInstrumentAmount(10);

        PortfolioAnalysisDetailItemDTO item2 = new PortfolioAnalysisDetailItemDTO();
        item2.setMarketValue(BigDecimal.valueOf(3000));
        item2.setInvestedValue(BigDecimal.valueOf(2000));
        item2.setInstrumentAmount(20);

        PortfolioAnalysisSummaryDTO result = analisysCalculatorService.calculateSummary(List.of(item1, item2));

        assertEquals(30, result.getInstrumentAmount());
        assertEquals(BigDecimal.valueOf(4500), result.getMarketValue());
        assertEquals(BigDecimal.valueOf(3000), result.getInvestedValue());
        assertEquals(BigDecimal.valueOf(50.00).setScale(2, RoundingMode.HALF_UP), result.getPercentageYield());
    }

    @Test
    void calculateSummary_emptylist() throws BadRequestException {
        PortfolioAnalysisSummaryDTO result = analisysCalculatorService.calculateSummary(Collections.emptyList());

        assertEquals(0, result.getInstrumentAmount());
        assertEquals(BigDecimal.valueOf(0), result.getMarketValue());
        assertEquals(BigDecimal.valueOf(0), result.getInvestedValue());
        assertNull(result.getPercentageYield());
    }
}
