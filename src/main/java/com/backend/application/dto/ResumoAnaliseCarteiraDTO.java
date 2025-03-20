package com.backend.application.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ResumoAnaliseCarteiraDTO {

    private int totalAcoes;
    private BigDecimal valorInvestido;
    private BigDecimal valorMercado;
    private BigDecimal rendimentosPorcentagem;
}
