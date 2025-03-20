package com.backend.Application.services;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.Application.dto.AnaliseCarteiraRequestDTO;
import com.backend.Application.dto.AnaliseCarteiraResponseDTO;
import com.backend.Application.dto.ItemDetalhesAnaliseCarteiraDTO;
import com.backend.Application.entities.InstrumentQuote;
import com.backend.Application.entities.UserTrade;
import com.backend.Application.enums.TipoOperacao;
import com.backend.Application.interfaces.ItemDetalhesAnaliseCarteiraProjection;
import com.backend.Application.repository.UserTradeRepository;

@Service
public class AnaliseCarteiraService {

    @Autowired
    private InstrumentQuoteService instrumentQuoteService;

    @Autowired
    private UserTradeRepository userTradeRepository;

    @Autowired
    private CarteiraCalculatorService analiseCalculatorService;

    public List<UserTrade> findAllByTipoOperacaoAndInstrumentAndData(
                TipoOperacao tipo, List<String> instrument,  LocalDate dataInicio, LocalDate dataFim ){ 
        return userTradeRepository.findAllByTipoOperacaoAndInstrumentInAndDataGreaterThanEqualAndDataLessThanEqual(tipo, instrument, dataInicio.atStartOfDay(), dataFim.atTime(23, 59, 59));
    }

    public List<ItemDetalhesAnaliseCarteiraDTO> obterItensDetalhesAnalise(LocalDateTime dataInicio, LocalDateTime dataFim) {
        List<ItemDetalhesAnaliseCarteiraProjection> projections = userTradeRepository.calcularTotalQuantidadeAndSaldoPorInstrument(dataInicio, dataFim);

        return projections.stream().map(p -> {
            ItemDetalhesAnaliseCarteiraDTO dto = new ItemDetalhesAnaliseCarteiraDTO();
            dto.setInstrument(p.getInstrument());
            dto.setTotalAcoes(p.getTotalAcoes());
            dto.setSaldoInvestido(p.getSaldoInvestido());
            dto.setSaldoAtual(BigDecimal.ZERO);  // Valores padrão
            dto.setRendimentosPorcentagem(BigDecimal.ZERO);
            return dto;
        }).toList();
    }

    public AnaliseCarteiraResponseDTO analiseCarteira(AnaliseCarteiraRequestDTO analiseCarteiraRequestDTO){ 
        AnaliseCarteiraResponseDTO responseDTO = new AnaliseCarteiraResponseDTO();

        List<ItemDetalhesAnaliseCarteiraDTO> detalhesAnaliseCarteiraDTO = 
            this.obterItensDetalhesAnalise(analiseCarteiraRequestDTO.getDataInicio().atStartOfDay(), analiseCarteiraRequestDTO.getDataFim().atTime(23,59,59));
        detalhesAnaliseCarteiraDTO = detalhesAnaliseCarteiraDTO.stream()
                                                                .map((item)->completarItemDetalheRendimento(item, analiseCarteiraRequestDTO))
                                                                .toList();
        responseDTO.setDetalhesAnaliseCarteiraDTO(detalhesAnaliseCarteiraDTO);

        responseDTO.setResumoAnaliseCarteiraDTO(null); //TODO


        return responseDTO;
    }

    public ItemDetalhesAnaliseCarteiraDTO completarItemDetalheRendimento(ItemDetalhesAnaliseCarteiraDTO item, AnaliseCarteiraRequestDTO request) {
        BigDecimal saldoAtual = analiseCalculatorService.calcularSaldo(item, request);
        item.setSaldoAtual(saldoAtual);

        BigDecimal rendimentoPorcentual = analiseCalculatorService.calcularRendimentoPorcentual(item, request);
        item.setRendimentosPorcentagem(rendimentoPorcentual);
        return item;
    }

    /*public AnaliseCarteiraResponseDTO calculaRendimentoCarteira(AnaliseCarteiraRequestDTO analiseCarteiraRequestDTO){
        AnaliseCarteiraResponseDTO analiseCarteiraResponseDTO = new AnaliseCarteiraResponseDTO();
        BigDecimal saldoAtual = BigDecimal.valueOf(0);
        for(String instrument: analiseCarteiraRequestDTO.getInstrumentList()){        
                
                saldoAtual = saldoAtual.add(this.calculaRendimentoDoInstrument(instrument, analiseCarteiraRequestDTO));
            
            
        }

        BigDecimal totalInvestido = this.calculaValorInvestidoCarteira(analiseCarteiraRequestDTO);
        BigDecimal rendimentoEmReais = saldoAtual.subtract(totalInvestido);
        BigDecimal rendimentoPorcentual = utils.calcularPorcentagem(rendimentoEmReais, totalInvestido);

        
        analiseCarteiraResponseDTO.setSaldoAtual(saldoAtual);
        analiseCarteiraResponseDTO.setRendimentos(rendimentoPorcentual);
        return analiseCarteiraResponseDTO;
    }               
    
    */      
    
    
}
