package com.backend.application.services;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.backend.application.converter.ItemDetalhesAnaliseCarteiraDTOConverter;
import org.apache.coyote.BadRequestException;
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
                TipoOperacao tipo, List<String> instrument,  LocalDateTime dataInicio, LocalDateTime dataFim ){
        return userTradeRepository.findAllByTipoOperacaoAndInstrumentInAndDataGreaterThanEqualAndDataLessThanEqual(tipo, instrument, dataInicio, dataFim);
    }

    public List<ItemDetalhesAnaliseCarteiraDTO> obterItensDetalhesAnalise(AnaliseCarteiraRequestDTO analiseCarteiraRequestDTO) {
        List<ItemDetalhesAnaliseCarteiraProjection> projections = userTradeRepository.getQuantidadeAndSaldoPorInstrument(analiseCarteiraRequestDTO.getInstrumentList(),
                analiseCarteiraRequestDTO.getDataInicio(),
                analiseCarteiraRequestDTO.getDataFim());

        return ItemDetalhesAnaliseCarteiraDTOConverter.toDTOList(projections);
    }

    public AnaliseCarteiraResponseDTO analisarCarteira() throws BadRequestException {
        AnaliseCarteiraRequestDTO request  = new AnaliseCarteiraRequestDTO().init();
        return analisarCarteiraComFiltro(request);
    }

    public AnaliseCarteiraResponseDTO analisarCarteiraComFiltro(AnaliseCarteiraRequestDTO analiseCarteiraRequestDTO) throws BadRequestException {
        AnaliseCarteiraResponseDTO responseDTO = new AnaliseCarteiraResponseDTO();

        List<ItemDetalhesAnaliseCarteiraDTO> detalhesAnaliseCarteiraDTO = 
            this.obterItensDetalhesAnalise(analiseCarteiraRequestDTO);
        detalhesAnaliseCarteiraDTO = calcularRendimentoListaItemDetalhe(analiseCarteiraRequestDTO, detalhesAnaliseCarteiraDTO);
        responseDTO.setDetalhesAnaliseCarteiraDTO(detalhesAnaliseCarteiraDTO);

        responseDTO.setResumoAnaliseCarteiraDTO(analiseCalculatorService.calcularResumo(detalhesAnaliseCarteiraDTO));
        return responseDTO;
    }

    public List<ItemDetalhesAnaliseCarteiraDTO> calcularRendimentoListaItemDetalhe(AnaliseCarteiraRequestDTO analiseCarteiraRequestDTO, List<ItemDetalhesAnaliseCarteiraDTO> detalhesAnaliseCarteiraDTO) {
        return detalhesAnaliseCarteiraDTO.stream()
                .map(item -> {
                    try {
                        return calcularRendimentoItemDetalhe(item, analiseCarteiraRequestDTO);
                    } catch (BadRequestException e) {
                        e.printStackTrace();
                        return item;
                    }
                })
                .toList();
    }

    public ItemDetalhesAnaliseCarteiraDTO calcularRendimentoItemDetalhe(ItemDetalhesAnaliseCarteiraDTO item, AnaliseCarteiraRequestDTO request) throws BadRequestException {
        BigDecimal valorMercado = analiseCalculatorService.calcularValorMercado(item, request);
        item.setValorMercado(valorMercado);

        BigDecimal rendimentoPorcentual = analiseCalculatorService.calcularRendimentoPorcentual(item);
        item.setRendimentosPorcentagem(rendimentoPorcentual);
        return item;
    }

}
