package com.backend.application.services;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.backend.application.dto.BrapiResponseDTO;
import org.springframework.stereotype.Service;

import com.backend.application.entities.InstrumentQuote;
import com.backend.application.repository.InstrumentQuoteRepository;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.reactive.function.client.WebClientException;

@Log4j2
@Service
public class InstrumentQuoteService{

  private final InstrumentQuoteRepository instrumentQuoteRepository;
  private final BrapiApiService brapiApiService;

  public InstrumentQuoteService(InstrumentQuoteRepository instrumentQuoteRepository, BrapiApiService brapiApiService){
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
	public List<InstrumentQuote> createByExternalRequest(String symbol) {
		List<InstrumentQuote> response = new ArrayList<>();

		try{
			BrapiResponseDTO instrumentsQuoteResponse = this.brapiApiService.getInstrumentQuote(symbol);

			List<BrapiResponseDTO.BrapiHistoricalData> historicoAcao = instrumentsQuoteResponse.getResults().get(0).getHistoricalDataPrice();

			List<LocalDate> dates = historicoAcao.stream().map(data->{
				Instant instant = Instant.ofEpochSecond(data.getDate());
				return LocalDate.ofInstant(instant, ZoneId.systemDefault());
			}).toList();

			List<InstrumentQuote> existingQuotes = instrumentQuoteRepository.findBySymbolAndDateIn(symbol, dates);

			List<LocalDate> existingDates = existingQuotes.stream()
				.map(InstrumentQuote::getDate).toList();

			List<BrapiResponseDTO.BrapiHistoricalData> newQuotes = historicoAcao.stream()
				.filter(item -> {
					Instant instant = Instant.ofEpochSecond(item.getDate());
					return !existingDates.contains(LocalDate.ofInstant(instant, ZoneId.systemDefault()));
				})
				.toList();

			for(BrapiResponseDTO.BrapiHistoricalData quote : newQuotes){
				InstrumentQuote instrumentQuote = new InstrumentQuote();
				instrumentQuote.setSymbol(symbol);
				instrumentQuote.setClosePrice(BigDecimal.valueOf(quote.getClose()));

				Instant instant = Instant.ofEpochSecond(quote.getDate());
				LocalDate dateTime = LocalDate.ofInstant(instant, ZoneId.systemDefault());
				instrumentQuote.setDate(dateTime);
				instrumentQuote = this.instrumentQuoteRepository.save(instrumentQuote);
				response.add(instrumentQuote);

			}
		}catch (WebClientException e){
			log.error("e: ", e);
			return response;
		}

			return response;
		}
}
