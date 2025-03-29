package com.backend.application.entities;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "instrument_quote")
@IdClass(InstrumentQuoteId.class)
@Data
public class InstrumentQuote {

		@Id
		@Column(name = "simbol", nullable = false, length = 50)
    private String symbol;

		@Id
		@Column(name = "date", nullable = false)
		private LocalDate date;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal closePrice;


    public InstrumentQuote() {}

    public InstrumentQuote(String symbol, BigDecimal closePrice, LocalDate date) {
        this.symbol = symbol;
        this.closePrice = closePrice;
        this.date = date;
    }

    @Override
    public String toString() {
        return "InstrumentQuote{" +
                ", symbol='" + symbol + '\'' +
                ", price=" + closePrice +
                ", date=" + date +
                '}';
    }
}
