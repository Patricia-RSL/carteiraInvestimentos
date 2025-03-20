package com.backend.application.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.backend.application.entities.InstrumentQuote;
import com.backend.application.repository.InstrumentQuoteRepository;

import lombok.extern.log4j.Log4j2;
@Log4j2
@Service
public class InstrumentQuoteService{

    private final InstrumentQuoteRepository instrumentQuoteRepository;

    public InstrumentQuoteService(InstrumentQuoteRepository instrumentQuoteRepository){
        this.instrumentQuoteRepository = instrumentQuoteRepository;
    }

    public List<InstrumentQuote> getAll() {
        return instrumentQuoteRepository.findAll();
    }


    public Optional<InstrumentQuote> getById(Long id) {
        return instrumentQuoteRepository.findById(id);
    }

    public Optional<InstrumentQuote> findBySymbolAndDate(String symbol, LocalDateTime date) {
        return instrumentQuoteRepository.findBySymbolAndDate(symbol, date);
    }
    
}
