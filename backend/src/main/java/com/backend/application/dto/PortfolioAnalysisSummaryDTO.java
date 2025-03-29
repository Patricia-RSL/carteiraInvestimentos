package com.backend.application.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PortfolioAnalysisSummaryDTO {

    private int instrumentAmount;
    private BigDecimal investedValue;
    private BigDecimal totalGainLoss;
    private BigDecimal marketValue;
    private BigDecimal percentageYield;
}
