package com.backend.application.services;

import com.backend.application.dto.BrapiResponseDTO;
import com.backend.application.entities.InstrumentQuote;
import com.backend.application.repository.InstrumentQuoteRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientException;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@Service
public class InstrumentQuoteService {

  private final InstrumentQuoteRepository instrumentQuoteRepository;
  private final BrapiApiService brapiApiService;

  public InstrumentQuoteService(InstrumentQuoteRepository instrumentQuoteRepository, BrapiApiService brapiApiService) {
    this.instrumentQuoteRepository = instrumentQuoteRepository;
    this.brapiApiService = brapiApiService;
  }

  public List<InstrumentQuote> getAll() {
    return instrumentQuoteRepository.findAll();
  }

  public Optional<InstrumentQuote> findBySymbolAndDate(String symbol, LocalDate date) {
    return instrumentQuoteRepository.findBySymbolAndDate(symbol, date);
  }

  //TODO criar teste
  public List<InstrumentQuote> createByBrapiRequest(String symbol) {
    List<InstrumentQuote> response = new ArrayList<>();
    if (symbol == null || symbol.isEmpty()) {
      return response;
    }
    try {
      BrapiResponseDTO instrumentsQuoteResponse = this.brapiApiService.getInstrumentQuote(symbol);

      List<BrapiResponseDTO.BrapiHistoricalData> newQuotes = filterNewHistoricalData(symbol, instrumentsQuoteResponse);

      for (BrapiResponseDTO.BrapiHistoricalData quote : newQuotes) {

        response.add(createInstrumentQuoteByBrapiHistoryItem(symbol, quote));

      }
    } catch (WebClientException e) {
      log.error("e: ", e);
      return response;
    }
    return response;
  }

  private LocalDate convertEpochToLocalDate(Long epoch) {
    Instant instant = Instant.ofEpochSecond(epoch);
    return LocalDate.ofInstant(instant, ZoneId.systemDefault());
  }

  private InstrumentQuote createInstrumentQuoteByBrapiHistoryItem(String symbol,
                                                       BrapiResponseDTO.BrapiHistoricalData quote) {
    InstrumentQuote instrumentQuote = new InstrumentQuote();
    instrumentQuote.setSymbol(symbol);
    instrumentQuote.setClosePrice(BigDecimal.valueOf(quote.getClose()));
    instrumentQuote.setDate(convertEpochToLocalDate(quote.getDate()));


    return this.instrumentQuoteRepository.save(instrumentQuote);
  }

  public List<BrapiResponseDTO.BrapiHistoricalData> filterNewHistoricalData(String symbol,
                                                                            BrapiResponseDTO instrumentsQuoteResponse) {
    List<BrapiResponseDTO.BrapiHistoricalData> historyData = instrumentsQuoteResponse
      .getResults()
      .get(0)
      .getHistoricalDataPrice();

    Set<LocalDate> historyDataDates = historyData.stream()
      .map(data -> convertEpochToLocalDate(data.getDate()))
      .collect(Collectors.toSet());

    Set<LocalDate> existingDates = instrumentQuoteRepository
      .findBySymbolAndDateIn(symbol, new ArrayList<>(historyDataDates))
      .stream()
      .map(InstrumentQuote::getDate)
      .collect(Collectors.toSet());

    return historyData.stream()
      .filter(item -> !existingDates.contains(convertEpochToLocalDate(item.getDate())))
      .toList();
  }
}
