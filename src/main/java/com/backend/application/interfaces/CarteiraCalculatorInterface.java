package com.backend.application.interfaces;

import java.math.BigDecimal;
import java.util.List;

import com.backend.application.dto.AnaliseCarteiraRequestDTO;
import com.backend.application.dto.ItemDetalhesAnaliseCarteiraDTO;
import com.backend.application.dto.ResumoAnaliseCarteiraDTO;
import org.apache.coyote.BadRequestException;

public interface CarteiraCalculatorInterface {
    BigDecimal calcularValorMercado(ItemDetalhesAnaliseCarteiraDTO item, AnaliseCarteiraRequestDTO request);
    BigDecimal calcularRendimentoPorcentual(ItemDetalhesAnaliseCarteiraDTO item) throws BadRequestException;
    ResumoAnaliseCarteiraDTO calcularResumo(List<ItemDetalhesAnaliseCarteiraDTO> itens, AnaliseCarteiraRequestDTO request);
}