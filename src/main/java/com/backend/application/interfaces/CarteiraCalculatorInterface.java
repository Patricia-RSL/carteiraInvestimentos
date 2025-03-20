package com.backend.application.interfaces;

import java.math.BigDecimal;

import com.backend.application.dto.AnaliseCarteiraRequestDTO;
import com.backend.application.dto.ItemDetalhesAnaliseCarteiraDTO;

public interface CarteiraCalculatorInterface {
    BigDecimal calcularSaldo(ItemDetalhesAnaliseCarteiraDTO item, AnaliseCarteiraRequestDTO request);
    BigDecimal calcularRendimentoPorcentual(ItemDetalhesAnaliseCarteiraDTO item, AnaliseCarteiraRequestDTO request);
}