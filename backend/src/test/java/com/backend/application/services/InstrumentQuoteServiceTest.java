package com.backend.application.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        quote1.setId(1L);
        
        quote2 = new InstrumentQuote();
        quote2.setId(2L);
    }

    @Test
    void testGetAll() {
        when(instrumentQuoteRepository.findAll()).thenReturn(Arrays.asList(quote1, quote2));
        List<InstrumentQuote> result = instrumentQuoteService.getAll();
        assertEquals(2, result.size());
        verify(instrumentQuoteRepository, times(1)).findAll();
    }

    @Test
    void testGetById() {
        when(instrumentQuoteRepository.findById(1L)).thenReturn(Optional.of(quote1));
        Optional<InstrumentQuote> result = instrumentQuoteService.getById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(instrumentQuoteRepository, times(1)).findById(1L);
    }

    @Test
    void testGetById_NotFound() {
        when(instrumentQuoteRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<InstrumentQuote> result = instrumentQuoteService.getById(99L);
        assertFalse(result.isPresent());
        verify(instrumentQuoteRepository, times(1)).findById(99L);
    }
}
