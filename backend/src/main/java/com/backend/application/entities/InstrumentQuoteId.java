package com.backend.application.entities;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
public class InstrumentQuoteId implements Serializable {
	private String symbol;
	private LocalDate date;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		InstrumentQuoteId that = (InstrumentQuoteId) o;
		return Objects.equals(symbol, that.symbol) && Objects.equals(date, that.date);
	}

	@Override
	public int hashCode() {
		return Objects.hash(symbol, date);
	}
}
