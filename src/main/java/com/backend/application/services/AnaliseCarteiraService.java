package com.backend.application.services;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.backend.application.dto.AnaliseCarteiraRequestDTO;
import com.backend.application.dto.AnaliseCarteiraResponseDTO;
import com.backend.application.dto.ItemDetalhesAnaliseCarteiraDTO;
import com.backend.application.entities.UserTrade;
import com.backend.application.enums.TipoOperacao;
import com.backend.application.interfaces.ItemDetalhesAnaliseCarteiraProjection;
import com.backend.application.repository.UserTradeRepository;

@Service
public class AnaliseCarteiraService {

    private final UserTradeRepository userTradeRepository;
    private final CarteiraCalculatorService analiseCalculatorService;

    public AnaliseCarteiraService(UserTradeRepository userTradeRepository,
                                  CarteiraCalculatorService analiseCalculatorService){
        this.userTradeRepository = userTradeRepository;
        this.analiseCalculatorService = analiseCalculatorService;
    }

    public List<UserTrade> findAllByTipoOperacaoAndInstrumentAndData(
                TipoOperacao tipo, List<String> instrument,  LocalDate dataInicio, LocalDate dataFim ){ 
        return userTradeRepository.findAllByTipoOperacaoAndInstrumentInAndDataGreaterThanEqualAndDataLessThanEqual(tipo, instrument, dataInicio.atStartOfDay(), dataFim.atTime(23, 59, 59));
    }

    public List<ItemDetalhesAnaliseCarteiraDTO> obterItensDetalhesAnalise(AnaliseCarteiraRequestDTO analiseCarteiraRequestDTO) {
        List<ItemDetalhesAnaliseCarteiraProjection> projections = userTradeRepository.getQuantidadeAndSaldoPorInstrument(analiseCarteiraRequestDTO.getInstrumentList(),
                analiseCarteiraRequestDTO.getDataInicio(),
                analiseCarteiraRequestDTO.getDataFim());

        return projections.stream().map(p -> {
            ItemDetalhesAnaliseCarteiraDTO dto = new ItemDetalhesAnaliseCarteiraDTO();
            dto.setInstrument(p.getInstrument());
            dto.setQtdAcoes(p.getTotalAcoes());
            dto.setValorInvestido(p.getSaldoInvestido());
            dto.setValorMercado(BigDecimal.ZERO);  // Valores padrão
            dto.setRendimentosPorcentagem(BigDecimal.ZERO);
            return dto;
        }).toList();
    }

    public AnaliseCarteiraResponseDTO analisarCarteira(){
        AnaliseCarteiraRequestDTO request  = new AnaliseCarteiraRequestDTO().init();
        return analisarCarteiraComFiltro(request);
    }

    public AnaliseCarteiraResponseDTO analisarCarteiraComFiltro(AnaliseCarteiraRequestDTO analiseCarteiraRequestDTO){
        AnaliseCarteiraResponseDTO responseDTO = new AnaliseCarteiraResponseDTO();

        List<ItemDetalhesAnaliseCarteiraDTO> detalhesAnaliseCarteiraDTO = 
            this.obterItensDetalhesAnalise(analiseCarteiraRequestDTO);
        detalhesAnaliseCarteiraDTO = detalhesAnaliseCarteiraDTO.stream()
                                                                .map(item->completarItemDetalheRendimento(item, analiseCarteiraRequestDTO))
                                                                .toList();
        responseDTO.setDetalhesAnaliseCarteiraDTO(detalhesAnaliseCarteiraDTO);

        responseDTO.setResumoAnaliseCarteiraDTO(analiseCalculatorService.calcularResumo(detalhesAnaliseCarteiraDTO, analiseCarteiraRequestDTO));
        return responseDTO;
    }

    public ItemDetalhesAnaliseCarteiraDTO calcularRendimentoItemDetalhe(ItemDetalhesAnaliseCarteiraDTO item, AnaliseCarteiraRequestDTO request) throws BadRequestException {
        BigDecimal valorMercado = analiseCalculatorService.calcularValorMercado(item, request);
        item.setValorMercado(valorMercado);

        BigDecimal rendimentoPorcentual = analiseCalculatorService.calcularRendimentoPorcentual(item);
        item.setRendimentosPorcentagem(rendimentoPorcentual);
        return item;
    }

}
