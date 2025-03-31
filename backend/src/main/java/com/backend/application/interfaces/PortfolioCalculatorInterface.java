package com.backend.application.interfaces;

import java.math.BigDecimal;
import java.util.List;

import com.backend.application.dto.PortfolioAnalysisRequestDTO;
import com.backend.application.dto.PortfolioAnalysisDetailItemDTO;
import com.backend.application.dto.PortfolioAnalysisSummaryDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.coyote.BadRequestException;

public interface PortfolioCalculatorInterface {
    BigDecimal calculateMarketPrice(PortfolioAnalysisDetailItemDTO item, PortfolioAnalysisRequestDTO request) throws BadRequestException;
    BigDecimal calculatePercentageYield(PortfolioAnalysisDetailItemDTO item) throws BadRequestException;
    PortfolioAnalysisSummaryDTO calculateSummary(List<PortfolioAnalysisDetailItemDTO> itens) throws BadRequestException;
}
