package com.backend.application.interfaces;

import java.math.BigDecimal;

public interface PortfolioAnalysisDetailItemProjection {
    String getInstrument();
    Integer getInstrumentAmount();
    BigDecimal getInvestedValue();
}