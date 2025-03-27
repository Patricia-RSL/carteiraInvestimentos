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

    public AnalysisCalculatorService(InstrumentQuoteService instrumentQuoteService){

        this.instrumentQuoteService = instrumentQuoteService;
    }

    @Override
    public BigDecimal calculateMarketPrice(PortfolioAnalysisDetailItemDTO item, PortfolioAnalysisRequestDTO request) {
        Optional<InstrumentQuote> bySymbolAndDate = instrumentQuoteService.findBySymbolAndDate(
            item.getInstrument(), request.getEndDate());

        if(bySymbolAndDate.isPresent()){ 
            return bySymbolAndDate.get().getPrice().multiply(BigDecimal.valueOf(item.getInstrumentAmount()));
        }
        
        return null; //TODO achar no brapi
    }

    @Override
    public BigDecimal calculatePercentageYield(PortfolioAnalysisDetailItemDTO item) throws BadRequestException {
        if (item.getMarketValue() == null) throw new BadRequestException("Market value cannot be null");
        if (item.getInstrumentAmount() == 0) return BigDecimal.valueOf(0);

        BigDecimal yieldInReais = item.getMarketValue().subtract(item.getInvestedValue());
        return yieldInReais.multiply(BigDecimal.valueOf(100)).divide(item.getInvestedValue(), 2, RoundingMode.HALF_UP);
    }

    @Override
    public PortfolioAnalysisSummaryDTO calculateSummary(List<PortfolioAnalysisDetailItemDTO> itens) throws BadRequestException {
        PortfolioAnalysisSummaryDTO response = new PortfolioAnalysisSummaryDTO();
        BigDecimal valorInvestido = BigDecimal.valueOf(0);
        BigDecimal valorMercado = BigDecimal.valueOf(0);
        int totalAcoes = 0;

        for(PortfolioAnalysisDetailItemDTO instrument: itens){
            if(instrument.getInstrumentAmount()!=0){
                valorInvestido = valorInvestido.add(instrument.getInvestedValue());
                valorMercado = valorMercado.add(instrument.getMarketValue());
                totalAcoes += instrument.getInstrumentAmount();
            }


        }

        BigDecimal rendimentoPorcentual = this.calculatePercentageYield(new PortfolioAnalysisDetailItemDTO().init("", totalAcoes, valorInvestido, valorMercado,  null));

        response.setInstrumentAmount(totalAcoes);
        response.setMarketValue(valorMercado);
        response.setInvestedValue(valorInvestido);
        response.setPercentageYield(rendimentoPorcentual);
        return response;
    }
}
