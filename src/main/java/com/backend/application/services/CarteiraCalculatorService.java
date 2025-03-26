package com.backend.application.services;

import com.backend.application.dto.AnaliseCarteiraRequestDTO;
import com.backend.application.dto.ItemDetalhesAnaliseCarteiraDTO;
import com.backend.application.dto.ResumoAnaliseCarteiraDTO;
import com.backend.application.entities.InstrumentQuote;
import com.backend.application.interfaces.CarteiraCalculatorInterface;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class CarteiraCalculatorService implements CarteiraCalculatorInterface {

    private final InstrumentQuoteService instrumentQuoteService;

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
    public BigDecimal calcularRendimentoPorcentual(ItemDetalhesAnaliseCarteiraDTO item) throws BadRequestException {
        if(item.getValorMercado()==null) throw new BadRequestException("Valor de mercado não pode ser nulo");
        if(item.getQtdAcoes()==0) return BigDecimal.valueOf(0);

        BigDecimal rendimentoEmReais = item.getValorMercado().subtract(item.getValorInvestido());
        return rendimentoEmReais.multiply(BigDecimal.valueOf(100)).divide(item.getValorInvestido(), 2, RoundingMode.HALF_UP);
    }

    @Override
    public ResumoAnaliseCarteiraDTO calcularResumo(List<ItemDetalhesAnaliseCarteiraDTO> itens) throws BadRequestException {
        ResumoAnaliseCarteiraDTO response = new ResumoAnaliseCarteiraDTO();
        BigDecimal valorInvestido = BigDecimal.valueOf(0);
        BigDecimal valorMercado = BigDecimal.valueOf(0);
        int totalAcoes = 0;

        for(ItemDetalhesAnaliseCarteiraDTO instrument: itens){
            if(instrument.getQtdAcoes()!=0){
                valorInvestido = valorInvestido.add(instrument.getValorInvestido());
                valorMercado = valorMercado.add(instrument.getValorMercado());
                totalAcoes += instrument.getQtdAcoes();
            }


        }

        BigDecimal rendimentoPorcentual = calcularRendimentoPorcentual(new ItemDetalhesAnaliseCarteiraDTO().init("", totalAcoes, valorInvestido, valorMercado,  null));

        response.setTotalAcoes(totalAcoes);
        response.setValorMercado(valorMercado);
        response.setValorInvestido(valorInvestido);
        response.setRendimentosPorcentagem(rendimentoPorcentual);
        return response;
    }
}
