package com.backend.application.services;

import com.backend.application.dto.PortfolioAnalysisRequestDTO;
import com.backend.application.dto.PortfolioAnalysisDetailItemDTO;
import com.backend.application.dto.PortfolioAnalysisSummaryDTO;
import com.backend.application.entities.InstrumentQuote;
import com.backend.application.interfaces.PortfolioCalculatorInterface;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class AnalysisCalculatorService implements PortfolioCalculatorInterface {

  private final InstrumentQuoteService instrumentQuoteService;

  public AnalysisCalculatorService(InstrumentQuoteService instrumentQuoteService) {

    this.instrumentQuoteService = instrumentQuoteService;
  }

  @Override
  public BigDecimal calculateMarketPrice(PortfolioAnalysisDetailItemDTO item, PortfolioAnalysisRequestDTO request) throws BadRequestException {
		if (item.getInstrumentAmount() == 0) return BigDecimal.valueOf(0);
		Optional<InstrumentQuote> bySymbolAndDate = instrumentQuoteService.findBySymbolAndDate(
      item.getInstrument(), request.getEndDate());

		if (bySymbolAndDate.isEmpty()) {
      List<InstrumentQuote> novosInstrumentsQuotes = this.instrumentQuoteService.createByBrapiRequest(item.getInstrument());
			if(novosInstrumentsQuotes.isEmpty()) return  null;

			bySymbolAndDate = novosInstrumentsQuotes.stream().filter(instrumentQuote -> instrumentQuote.getDate().equals(request.getEndDate())).findFirst();
		}

		return bySymbolAndDate.get().getClosePrice().multiply(BigDecimal.valueOf(item.getInstrumentAmount()));
  }

  @Override
  public BigDecimal calculatePercentageYield(PortfolioAnalysisDetailItemDTO item) {
    if (item.getMarketValue() == null || item.getInstrumentAmount() == 0) return null;

    if (item.getInstrumentAmount() > 0) {
      BigDecimal yieldInReais = item.getMarketValue().subtract(item.getInvestedValue());
      return yieldInReais.multiply(BigDecimal.valueOf(100)).divide(item.getInvestedValue(), 2, RoundingMode.HALF_UP);

    } else {
      BigDecimal yieldInReais = item.getInvestedValue().subtract(item.getMarketValue());
      return yieldInReais.multiply(BigDecimal.valueOf(100)).divide(item.getInvestedValue(), 2, RoundingMode.HALF_UP);

    }
  }

  @Override
  public PortfolioAnalysisSummaryDTO calculateSummary(List<PortfolioAnalysisDetailItemDTO> itens) throws BadRequestException {
    PortfolioAnalysisSummaryDTO response = new PortfolioAnalysisSummaryDTO();
    BigDecimal valorInvestido = BigDecimal.valueOf(0);
    BigDecimal totalGainLoss = BigDecimal.valueOf(0);
    BigDecimal valorMercado = BigDecimal.valueOf(0);
    int totalAcoes = 0;

    for (PortfolioAnalysisDetailItemDTO instrument : itens) {
      if (instrument.getInstrumentAmount() != 0 && instrument.getMarketValue()!=null) {
        valorInvestido = valorInvestido.add(instrument.getInvestedValue());
        valorMercado = valorMercado.add(instrument.getMarketValue());
        totalAcoes += instrument.getInstrumentAmount();
      } else if (instrument.getInstrumentAmount() == 0 ) {
        totalGainLoss = totalGainLoss.subtract(instrument.getInvestedValue());
      }


    }

    BigDecimal rendimentoPorcentual = this.calculatePercentageYield(new PortfolioAnalysisDetailItemDTO().init("", totalAcoes, valorInvestido, valorMercado, null));

    response.setInstrumentAmount(totalAcoes);
    response.setMarketValue(valorMercado);
    response.setInvestedValue(valorInvestido);
    response.setTotalGainLoss(totalGainLoss);
    response.setPercentageYield(rendimentoPorcentual);
    return response;
  }
}
