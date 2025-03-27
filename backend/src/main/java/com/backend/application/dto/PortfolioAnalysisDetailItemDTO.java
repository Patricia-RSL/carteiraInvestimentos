package com.backend.application.dto;

import java.math.BigDecimal;
import java.util.Objects;

import lombok.Data;

@Data
public class PortfolioAnalysisDetailItemDTO {

    private String instrument;
    private int instrumentAmount;
    private BigDecimal investedValue;
    private BigDecimal marketValue;
    private BigDecimal percentageYield;

    public PortfolioAnalysisDetailItemDTO init(String instrument, int instrumentAmount, BigDecimal investedValue, BigDecimal marketValue, BigDecimal percentageYield) {
        this.instrument = instrument;
        this.investedValue = investedValue;
        this.marketValue = marketValue;
        this.instrumentAmount = instrumentAmount;
        this.percentageYield = percentageYield;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PortfolioAnalysisDetailItemDTO argument = (PortfolioAnalysisDetailItemDTO) o;
        return argument.getInstrument().equals(this.getInstrument()) &&
                argument.getInstrumentAmount() == this.getInstrumentAmount() &&
                argument.getInvestedValue().compareTo(this.getInvestedValue()) == 0 &&
                (argument.getMarketValue() == null ? this.getMarketValue() == null : argument.getMarketValue().compareTo(this.getMarketValue()) == 0) &&
                (argument.getPercentageYield() == null ? this.getPercentageYield() == null : argument.getPercentageYield().compareTo(this.getPercentageYield()) == 0);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instrument, instrumentAmount, investedValue, marketValue, percentageYield);
    }
}
