package com.backend.Application.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.Application.entities.InstrumentQuote;
import com.backend.Application.repository.InstrumentQuoteRepository;

import lombok.extern.log4j.Log4j2;
@Log4j2
@Service
public class InstrumentQuoteService{

    @Autowired
    private InstrumentQuoteRepository instrumentQuoteRepository;


    public InstrumentQuoteService(){}


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
