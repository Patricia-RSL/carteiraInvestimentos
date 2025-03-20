package com.backend.Application.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.Application.dto.AnaliseCarteiraRequestDTO;
import com.backend.Application.dto.ItemDetalhesAnaliseCarteiraDTO;
import com.backend.Application.entities.InstrumentQuote;
import com.backend.Application.interfaces.CarteiraCalculatorInterface;

@Service
public class CarteiraCalculatorService implements CarteiraCalculatorInterface {
    
    @Autowired
    private InstrumentQuoteService instrumentQuoteService;

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
        if(item.getSaldoAtual()==null) return null;

        BigDecimal rendimentoEmReais = item.getSaldoAtual().subtract(item.getSaldoInvestido());
        BigDecimal rendimentoPorcentual = rendimentoEmReais.multiply(BigDecimal.valueOf(100)).divide(item.getSaldoInvestido(), 2, RoundingMode.HALF_UP);
        return rendimentoPorcentual;
    }
}
