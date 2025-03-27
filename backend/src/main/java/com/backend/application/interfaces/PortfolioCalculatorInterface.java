package com.backend.application.interfaces;

import java.math.BigDecimal;
import java.util.List;

import com.backend.application.dto.PortfolioAnalysisRequestDTO;
import com.backend.application.dto.PortfolioAnalysisDetailItemDTO;
import com.backend.application.dto.PortfolioAnalysisSummaryDTO;
import org.apache.coyote.BadRequestException;

public interface PortfolioCalculatorInterface {
    BigDecimal calculateMarketPrice(PortfolioAnalysisDetailItemDTO item, PortfolioAnalysisRequestDTO request);
    BigDecimal calculatePercentageYield(PortfolioAnalysisDetailItemDTO item) throws BadRequestException;
    PortfolioAnalysisSummaryDTO calculateSummary(List<PortfolioAnalysisDetailItemDTO> itens) throws BadRequestException;
}