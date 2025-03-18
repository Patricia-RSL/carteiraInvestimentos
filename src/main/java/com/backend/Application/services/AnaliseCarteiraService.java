package com.backend.Application.services;
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

    public void calculaResultadoCarteira(List<String> instrument,  LocalDate dataInicio, LocalDate dataFim){
        return;
    }
    
    
}
