package com.backend.application.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.backend.application.dto.BrapiResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.backend.application.entities.InstrumentQuote;
import com.backend.application.repository.InstrumentQuoteRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClientRequestException;

@ExtendWith(MockitoExtension.class)
class InstrumentQuoteServiceTest {

    @Mock
    private InstrumentQuoteRepository instrumentQuoteRepository;

    @Mock
    private BrapiApiService brapiApiService;

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

  @Test
  void filterNewHistoricalData_ReturnsNewData() {
    BrapiResponseDTO.BrapiHistoricalData data1 = new BrapiResponseDTO.BrapiHistoricalData();
    data1.setDate(Instant.now().getEpochSecond());
    data1.setClose(100.0);

    BrapiResponseDTO.BrapiHistoricalData data2 = new BrapiResponseDTO.BrapiHistoricalData();
    data2.setDate(Instant.now().minusSeconds(86400).getEpochSecond());
    data2.setClose(200.0);

    BrapiResponseDTO.BrapiResposeResult result = new BrapiResponseDTO.BrapiResposeResult();
    result.setHistoricalDataPrice(Arrays.asList(data1, data2));

    BrapiResponseDTO response = new BrapiResponseDTO();
    response.setResults(List.of(result));

    InstrumentQuote existingQuote = new InstrumentQuote();
    existingQuote.setSymbol("BOVA11");
    existingQuote.setDate(LocalDate.now());

    when(instrumentQuoteRepository.findBySymbolAndDateIn(eq("BOVA11"), anyList())).thenReturn(List.of(existingQuote));

    List<BrapiResponseDTO.BrapiHistoricalData> newData = instrumentQuoteService.filterNewHistoricalData("BOVA11", response);

    assertEquals(1, newData.size());
    assertEquals(data2.getDate(), newData.get(0).getDate());
  }

  @Test
  void filterNewHistoricalData_ReturnsEmptyListWhenNoNewData() {
    BrapiResponseDTO.BrapiHistoricalData data1 = new BrapiResponseDTO.BrapiHistoricalData();
    data1.setDate(Instant.now().getEpochSecond());
    data1.setClose(100.0);

    BrapiResponseDTO.BrapiResposeResult result = new BrapiResponseDTO.BrapiResposeResult();
    result.setHistoricalDataPrice(List.of(data1));

    BrapiResponseDTO response = new BrapiResponseDTO();
    response.setResults(List.of(result));

    InstrumentQuote existingQuote = new InstrumentQuote();
    existingQuote.setSymbol("BOVA11");
    existingQuote.setDate(LocalDate.now());

    when(instrumentQuoteRepository.findBySymbolAndDateIn(eq("BOVA11"), anyList())).thenReturn(List.of(existingQuote));

    List<BrapiResponseDTO.BrapiHistoricalData> newData = instrumentQuoteService.filterNewHistoricalData("BOVA11", response);

    assertTrue(newData.isEmpty());
  }

  @Test
  void filterNewHistoricalData_ReturnsAllDataWhenNoExistingData() {
    BrapiResponseDTO.BrapiHistoricalData data1 = new BrapiResponseDTO.BrapiHistoricalData();
    data1.setDate(Instant.now().getEpochSecond());
    data1.setClose(100.0);

    BrapiResponseDTO.BrapiHistoricalData data2 = new BrapiResponseDTO.BrapiHistoricalData();
    data2.setDate(Instant.now().minusSeconds(86400).getEpochSecond());
    data2.setClose(200.0);

    BrapiResponseDTO.BrapiResposeResult result = new BrapiResponseDTO.BrapiResposeResult();
    result.setHistoricalDataPrice(Arrays.asList(data1, data2));

    BrapiResponseDTO response = new BrapiResponseDTO();
    response.setResults(List.of(result));

    when(instrumentQuoteRepository.findBySymbolAndDateIn(eq("BOVA11"), anyList())).thenReturn(Collections.emptyList());

    List<BrapiResponseDTO.BrapiHistoricalData> newData = instrumentQuoteService.filterNewHistoricalData("BOVA11", response);

    assertEquals(2, newData.size());
    assertEquals(data1.getDate(), newData.get(0).getDate());
    assertEquals(data2.getDate(), newData.get(1).getDate());
  }

  @Test
  void createByBrapiRequest_ReturnsEmptyListWhenSymbolIsNull() {
    List<InstrumentQuote> result = instrumentQuoteService.createByBrapiRequest(null);
    assertTrue(result.isEmpty());
  }

  @Test
  void createByBrapiRequest_ReturnsEmptyListWhenSymbolIsEmpty() {
    List<InstrumentQuote> result = instrumentQuoteService.createByBrapiRequest("");
    assertTrue(result.isEmpty());
  }

  @Test
  void createByBrapiRequest_ReturnsQuotesWhenNewDataExists() {
    BrapiResponseDTO.BrapiHistoricalData data1 = new BrapiResponseDTO.BrapiHistoricalData();
    data1.setDate(Instant.now().getEpochSecond());
    data1.setClose(100.0);

    BrapiResponseDTO.BrapiResposeResult result = new BrapiResponseDTO.BrapiResposeResult();
    result.setHistoricalDataPrice(List.of(data1));

    BrapiResponseDTO response = new BrapiResponseDTO();
    response.setResults(List.of(result));

    when(brapiApiService.getInstrumentQuote("BOVA11")).thenReturn(response);
    when(instrumentQuoteRepository.findBySymbolAndDateIn(eq("BOVA11"), anyList())).thenReturn(Collections.emptyList());
    when(instrumentQuoteRepository.save(any(InstrumentQuote.class))).thenReturn(new InstrumentQuote("BOVA11", BigDecimal.valueOf(100.0), LocalDate.now()));

    List<InstrumentQuote> resultQuotes = instrumentQuoteService.createByBrapiRequest("BOVA11");

    assertEquals(1, resultQuotes.size());
    assertEquals("BOVA11", resultQuotes.get(0).getSymbol());
    assertEquals(LocalDate.now(), resultQuotes.get(0).getDate());
    assertEquals(BigDecimal.valueOf(100.0), resultQuotes.get(0).getClosePrice());
  }

  @Test
  void createByBrapiRequest_ReturnsEmptyListWhenNoNewData() {
    BrapiResponseDTO.BrapiHistoricalData data1 = new BrapiResponseDTO.BrapiHistoricalData();
    data1.setDate(Instant.now().getEpochSecond());
    data1.setClose(100.0);

    BrapiResponseDTO.BrapiResposeResult result = new BrapiResponseDTO.BrapiResposeResult();
    result.setHistoricalDataPrice(List.of(data1));

    BrapiResponseDTO response = new BrapiResponseDTO();
    response.setResults(List.of(result));

    InstrumentQuote existingQuote = new InstrumentQuote();
    existingQuote.setSymbol("BOVA11");
    existingQuote.setDate(LocalDate.now());

    when(brapiApiService.getInstrumentQuote("BOVA11")).thenReturn(response);
    when(instrumentQuoteRepository.findBySymbolAndDateIn(eq("BOVA11"), anyList())).thenReturn(List.of(existingQuote));

    List<InstrumentQuote> resultQuotes = instrumentQuoteService.createByBrapiRequest("BOVA11");

    assertTrue(resultQuotes.isEmpty());
  }

  @Test
  void createByBrapiRequest_ReturnsEmptyListWhenExceptionOccurs() {
    when(brapiApiService.getInstrumentQuote("BOVA11")).thenThrow(new WebClientRequestException(new Throwable("Error"),
      HttpMethod.GET, URI.create("http://example.com"), new HttpHeaders()));

    List<InstrumentQuote> resultQuotes = instrumentQuoteService.createByBrapiRequest("BOVA11");

    assertTrue(resultQuotes.isEmpty());
  }
}
