package com.backend.application.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.backend.application.entities.InstrumentQuote;
import java.time.LocalDateTime;

@Repository
public interface InstrumentQuoteRepository extends JpaRepository<InstrumentQuote, Long>, JpaSpecificationExecutor<InstrumentQuote>{

    Optional<InstrumentQuote> findBySymbolAndDate(String symbol, LocalDateTime date);
    
}
