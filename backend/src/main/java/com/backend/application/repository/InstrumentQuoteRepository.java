package com.backend.application.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.backend.application.entities.InstrumentQuote;

@Repository
public interface InstrumentQuoteRepository extends JpaRepository<InstrumentQuote, Long>, JpaSpecificationExecutor<InstrumentQuote>{

    Optional<InstrumentQuote> findBySymbolAndDate(String symbol, LocalDate date);

	List<InstrumentQuote> findBySymbolAndDateIn(String instrument, List<LocalDate> dates);
}
