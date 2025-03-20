package com.backend.application.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.backend.application.dto.AnaliseCarteiraRequestDTO;
import com.backend.application.dto.ItemDetalhesAnaliseCarteiraDTO;
import com.backend.application.entities.InstrumentQuote;
import com.backend.application.interfaces.CarteiraCalculatorInterface;

@Service
public class CarteiraCalculatorService implements CarteiraCalculatorInterface {

    private InstrumentQuoteService instrumentQuoteService;

    public CarteiraCalculatorService(InstrumentQuoteService instrumentQuoteService){

        this.instrumentQuoteService = instrumentQuoteService;
    }

    @Override
    public BigDecimal calcularSaldo(ItemDetalhesAnaliseCarteiraDTO item, AnaliseCarteiraRequestDTO request) {
        Optional<InstrumentQuote> bySymbolAndDate = instrumentQuoteService.findBySymbolAndDate(
            item.getInstrument(), request.getDataFim().atStartOfDay());

        if(bySymbolAndDate.isPresent()){ 
            return bySymbolAndDate.get().getPrice().multiply(BigDecimal.valueOf(item.getTotalAcoes()));
        }
        
        return null;
    }

    @Override
    public BigDecimal calcularRendimentoPorcentual(ItemDetalhesAnaliseCarteiraDTO item, AnaliseCarteiraRequestDTO request){
        if(item.getValorMercado()==null) return null;

        BigDecimal rendimentoEmReais = item.getValorMercado().subtract(item.getValorInvestido());
        return rendimentoEmReais.multiply(BigDecimal.valueOf(100)).divide(item.getValorInvestido(), 2, RoundingMode.HALF_UP);
    }
}
