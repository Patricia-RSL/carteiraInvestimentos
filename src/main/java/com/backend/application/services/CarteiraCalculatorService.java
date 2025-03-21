package com.backend.application.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import com.backend.application.dto.ResumoAnaliseCarteiraDTO;
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
    public BigDecimal calcularValorMercado(ItemDetalhesAnaliseCarteiraDTO item, AnaliseCarteiraRequestDTO request) {
        Optional<InstrumentQuote> bySymbolAndDate = instrumentQuoteService.findBySymbolAndDate(
            item.getInstrument(), request.getDataFim());

        if(bySymbolAndDate.isPresent()){ 
            return bySymbolAndDate.get().getPrice().multiply(BigDecimal.valueOf(item.getQtdAcoes()));
        }
        
        return null; //TODO achar no brapi
    }

    @Override
    public BigDecimal calcularRendimentoPorcentual(ItemDetalhesAnaliseCarteiraDTO item){
        if(item.getValorMercado()==null) return null;

        BigDecimal rendimentoEmReais = item.getValorMercado().subtract(item.getValorInvestido());
        return rendimentoEmReais.multiply(BigDecimal.valueOf(100)).divide(item.getValorInvestido(), 2, RoundingMode.HALF_UP);
    }

    @Override
    public ResumoAnaliseCarteiraDTO calcularResumo(List<ItemDetalhesAnaliseCarteiraDTO> itens, AnaliseCarteiraRequestDTO request) {
        ResumoAnaliseCarteiraDTO response = new ResumoAnaliseCarteiraDTO();
        BigDecimal valorInvestido = BigDecimal.valueOf(0);
        BigDecimal valorMercado = BigDecimal.valueOf(0);
        Integer totalAcoes = 0;

        for(ItemDetalhesAnaliseCarteiraDTO instrument: itens){

            valorInvestido = valorInvestido.add(instrument.getValorInvestido());
            valorMercado = valorMercado.add(instrument.getValorMercado());
            totalAcoes += instrument.getQtdAcoes();


        }

        BigDecimal rendimentoEmReais = valorMercado.subtract(valorInvestido);
        BigDecimal rendimentoPorcentual = rendimentoEmReais.multiply(BigDecimal.valueOf(100)).divide(valorInvestido, 2, RoundingMode.HALF_UP);

        response.setTotalAcoes(totalAcoes);
        response.setValorMercado(valorMercado);
        response.setValorInvestido(valorInvestido);
        response.setRendimentosPorcentagem(rendimentoPorcentual);
        return response;
    }
}
