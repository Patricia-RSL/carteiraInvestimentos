package com.backend.application.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.backend.application.enums.OperationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.backend.application.entities.UserTrade;
import com.backend.application.repository.UserTradeRepository;

@ExtendWith(MockitoExtension.class)
class UserTradeServiceTest {

  @Mock
  private UserTradeRepository userTradeRepository;

  @Mock
  private AuthService authService;

  @InjectMocks
  private UserTradeService userTradeService;

  private UserTrade trade1;
  private UserTrade trade2;

  @BeforeEach
  void setUp() {
    trade1 = new UserTrade();
    trade1.setId(1L);
    trade1.setInstrument("AAPL");
    trade1.setOperationType(OperationType.c);

    trade2 = new UserTrade();
    trade2.setId(2L);
    trade2.setInstrument("GOOGL");
    trade2.setOperationType(OperationType.v);

    // Mock AuthService to return a specific user ID
    lenient().when(authService.getCurrentUser()).thenReturn(1L);  }

  @Test
  void testGetAll() {
    when(userTradeRepository.findAllByUserId(1L)).thenReturn(Arrays.asList(trade1, trade2));
    List<UserTrade> result = userTradeService.getAll();
    assertEquals(2, result.size());
    verify(userTradeRepository, times(1)).findAllByUserId(1L);
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
    when(userTradeRepository.findAllByInstrumentAndUserId("AAPL", 1L)).thenReturn(Collections.singletonList(trade1));
    List<UserTrade> result = userTradeService.findAllByInstrument("AAPL");
    assertEquals(1, result.size());
    assertEquals("AAPL", result.get(0).getInstrument());
    verify(userTradeRepository, times(1)).findAllByInstrumentAndUserId("AAPL", 1L);
  }

  @Test
  void testFindAllByOperationType() {
    when(userTradeRepository.findAllByOperationTypeAndUserId(OperationType.c, 1L)).thenReturn(Collections.singletonList(trade1));
    List<UserTrade> result = userTradeService.findAllByOperationType(OperationType.c);
    assertEquals(1, result.size());
    assertEquals(OperationType.c, result.get(0).getOperationType());
    verify(userTradeRepository, times(1)).findAllByOperationTypeAndUserId(OperationType.c, 1L);
  }
}
