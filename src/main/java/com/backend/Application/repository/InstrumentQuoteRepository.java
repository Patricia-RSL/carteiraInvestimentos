package com.backend.Application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.backend.Application.entities.InstrumentQuote;

@Repository
public interface InstrumentQuoteRepository extends JpaRepository<InstrumentQuote, Long>, JpaSpecificationExecutor<InstrumentQuote>{
    
}
