package com.backend.Application.services;

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

import com.backend.Application.entities.UserTrade;
import com.backend.Application.enums.TipoOperacao;
import com.backend.Application.repository.UserTradeRepository;

@ExtendWith(MockitoExtension.class)
class UserTradeServiceTest {

    @Mock
    private UserTradeRepository userTradeRepository;

    @InjectMocks
    private UserTradeService userTradeService;

    private UserTrade trade1;
    private UserTrade trade2;

    @BeforeEach
    void setUp() {
        trade1 = new UserTrade();
        trade1.setId(1L);
        trade1.setInstrument("AAPL");
        trade1.setTipoOperacao(TipoOperacao.c);

        trade2 = new UserTrade();
        trade2.setId(2L);
        trade2.setInstrument("GOOGL");
        trade2.setTipoOperacao(TipoOperacao.v);
    }

    @Test
    void testGetAll() {
        when(userTradeRepository.findAll()).thenReturn(Arrays.asList(trade1, trade2));
        List<UserTrade> result = userTradeService.getAll();
        assertEquals(2, result.size());
        verify(userTradeRepository, times(1)).findAll();
    }

    @Test
    void testGetById() {
        when(userTradeRepository.findById(1L)).thenReturn(Optional.of(trade1));
        Optional<UserTrade> result = userTradeService.getById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(userTradeRepository, times(1)).findById(1L);
    }

    @Test
    void testGetById_NotFound() {
        when(userTradeRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<UserTrade> result = userTradeService.getById(99L);

        assertFalse(result.isPresent());
        verify(userTradeRepository, times(1)).findById(99L);
    }


    @Test
    void testFindAllByInstrument() {
        when(userTradeRepository.findAllByInstrument("AAPL")).thenReturn(Arrays.asList(trade1));
        List<UserTrade> result = userTradeService.findAllByInstrument("AAPL");
        assertEquals(1, result.size());
        assertEquals("AAPL", result.get(0).getInstrument());
        verify(userTradeRepository, times(1)).findAllByInstrument("AAPL");
    }

    @Test
    void testFindAllByTipoOperacao() {
        when(userTradeRepository.findAllByTipoOperacao(TipoOperacao.c)).thenReturn(Arrays.asList(trade1));
        List<UserTrade> result = userTradeService.findAllByTipoOperacao(TipoOperacao.c);
        assertEquals(1, result.size());
        assertEquals(TipoOperacao.c, result.get(0).getTipoOperacao());
        verify(userTradeRepository, times(1)).findAllByTipoOperacao(TipoOperacao.c);
    }
}