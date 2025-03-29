package com.backend.application.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.backend.application.entities.InstrumentQuote;
import com.backend.application.repository.InstrumentQuoteRepository;

@ExtendWith(MockitoExtension.class)
class InstrumentQuoteServiceTest {

    @Mock
    private InstrumentQuoteRepository instrumentQuoteRepository;

    @InjectMocks
    private InstrumentQuoteService instrumentQuoteService;

    private InstrumentQuote quote1;
    private InstrumentQuote quote2;

    @BeforeEach
    void setUp() {

        quote1 = new InstrumentQuote();
        quote1.setSymbol("BOVA11");
				quote1.setDate(LocalDate.now());

        quote2 = new InstrumentQuote();
				quote2.setSymbol("ITUB4F");
				quote2.setDate(LocalDate.now());
    }

    @Test
    void testGetAll() {
        when(instrumentQuoteRepository.findAll()).thenReturn(Arrays.asList(quote1, quote2));
        List<InstrumentQuote> result = instrumentQuoteService.getAll();
        assertEquals(2, result.size());
        verify(instrumentQuoteRepository, times(1)).findAll();
    }

    @Test
    void testGetBySymbolAndDate_Ok() {
				LocalDate hoje =  LocalDate.now();
        when(instrumentQuoteRepository.findBySymbolAndDate("BOVA11",hoje)).thenReturn(Optional.of(quote1));
        Optional<InstrumentQuote> result = instrumentQuoteService.findBySymbolAndDate("BOVA11", hoje);
        assertTrue(result.isPresent());
        assertEquals("BOVA11", result.get().getSymbol());
				assertEquals(hoje, result.get().getDate());
        verify(instrumentQuoteRepository, times(1)).findBySymbolAndDate("BOVA11", hoje);
    }

    @Test
    void testGetBySymbolAndDate_NotFound() {
			LocalDate hoje =  LocalDate.now();

			when(instrumentQuoteRepository.findBySymbolAndDate("MGLU3F",hoje)).thenReturn(Optional.empty());
			Optional<InstrumentQuote> result = instrumentQuoteService.findBySymbolAndDate("MGLU3F", hoje);
			assertFalse(result.isPresent());
			verify(instrumentQuoteRepository, times(1)).findBySymbolAndDate("MGLU3F", hoje);
    }
}
