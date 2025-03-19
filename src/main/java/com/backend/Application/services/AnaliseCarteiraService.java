package com.backend.Application.services;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.Application.entities.UserTrade;
import com.backend.Application.enums.TipoOperacao;
import com.backend.Application.repository.UserTradeRepository;

@Service
public class AnaliseCarteiraService {

    @Autowired
    private UserTradeService userTradeService;

    @Autowired
    private UserTradeRepository userTradeRepository;

    public List<UserTrade> findAllByTipoOperacaoAndInstrumentAndData(
                TipoOperacao tipo, List<String> instrument,  LocalDate dataInicio, LocalDate dataFim ){ 
        return userTradeRepository.findAllByTipoOperacaoAndInstrumentInAndDataGreaterThanEqualAndDataLessThanEqual(tipo, instrument, dataInicio.atStartOfDay(), dataFim.atTime(23, 59, 59));
    }

    public BigDecimal calculaResultadoCarteira(List<String> instrument,  LocalDate dataInicio, LocalDate dataFim){
        return calculaTotalComprado(instrument, dataInicio, dataFim).subtract(calculaTotalVendido(instrument, dataInicio, dataFim));
    }

    public BigDecimal calculaTotalComprado(List<String> instrument,  LocalDate dataInicio, LocalDate dataFim){
        BigDecimal soma = userTradeRepository.getSumFromAllByTipoOperacaoAndInstrumentInAndDataGreaterThanEqualAndDataLessThanEqual(TipoOperacao.c, instrument, dataInicio.atStartOfDay(), dataFim.atTime(23, 59, 59));
        if(soma!=null) return soma;
        return BigDecimal.valueOf(0);
    }

    public BigDecimal calculaTotalVendido(List<String> instrument,  LocalDate dataInicio, LocalDate dataFim){
        BigDecimal soma = userTradeRepository.getSumFromAllByTipoOperacaoAndInstrumentInAndDataGreaterThanEqualAndDataLessThanEqual(TipoOperacao.v, instrument, dataInicio.atStartOfDay(), dataFim.atTime(23, 59, 59));
        if(soma!=null) return soma;
        return BigDecimal.valueOf(0);
    }

    public BigDecimal calculaRendimentoInvidual(String instrumentName, AnaliseCarteiraRequestDTO analiseCarteiraRequestDTO) {
        Optional<InstrumentQuote> bySymbolAndDate = this.instrumentQuoteService.findBySymbolAndDate(instrumentName, analiseCarteiraRequestDTO.getDataFim().atTime(23, 59, 59));
        Integer totaisNaCarteira = this.getTotalInstrumentCompradosFilterBY( instrumentName, analiseCarteiraRequestDTO) - this. getTotalInstrumentVendidosFilterBY(instrumentName, analiseCarteiraRequestDTO);
        
        if(bySymbolAndDate.isPresent()){
            InstrumentQuote instrumentQuote = bySymbolAndDate.get();
            return instrumentQuote.getPrice().multiply(BigDecimal.valueOf(totaisNaCarteira));
        }
        throw new UnsupportedOperationException("Unimplemented method 'getTotalInstrumentCompradosFilter'");;
       
    }

    public Integer getTotalInstrumentCompradosFilterBY(String instrumentName, AnaliseCarteiraRequestDTO analiseCarteiraRequestDTO){
        return this.userTradeRepository.getTotalInstrumentFilterBy(TipoOperacao.c, instrumentName, analiseCarteiraRequestDTO.getDataInicio().atStartOfDay(), analiseCarteiraRequestDTO.getDataFim().atTime(23, 59, 59));

    }

    public Integer getTotalInstrumentVendidosFilterBY(String instrumentName, AnaliseCarteiraRequestDTO analiseCarteiraRequestDTO){
        return this.userTradeRepository.getTotalInstrumentFilterBy(TipoOperacao.v, instrumentName, analiseCarteiraRequestDTO.getDataInicio().atStartOfDay(), analiseCarteiraRequestDTO.getDataFim().atTime(23, 59, 59));

    }
    
    
}
