package com.backend.Application.interfaces;

import java.math.BigDecimal;

import com.backend.Application.dto.AnaliseCarteiraRequestDTO;
import com.backend.Application.dto.ItemDetalhesAnaliseCarteiraDTO;

public interface CarteiraCalculatorInterface {
    BigDecimal calcularSaldo(ItemDetalhesAnaliseCarteiraDTO item, AnaliseCarteiraRequestDTO request);
    BigDecimal calcularRendimentoPorcentual(ItemDetalhesAnaliseCarteiraDTO item, AnaliseCarteiraRequestDTO request);
}