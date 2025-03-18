package com.backend.Application.entities;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "instrument_quote")
@Data
public class InstrumentQuote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "simbol", nullable = false, length = 50)
    private String symbol;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    // Construtores
    public InstrumentQuote() {}

    public InstrumentQuote(String symbol, BigDecimal price, LocalDateTime date) {
        this.symbol = symbol;
        this.price = price;
        this.date = date;
    }

    @Override
    public String toString() {
        return "InstrumentQuote{" +
                "id=" + id +
                ", symbol='" + symbol + '\'' +
                ", price=" + price +
                ", date=" + date +
                '}';
    }
}
