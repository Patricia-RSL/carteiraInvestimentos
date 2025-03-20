package com.backend.Application.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ItemDetalhesAnaliseCarteiraDTO {

    private String instrument;
    private int totalAcoes;
    private BigDecimal saldoInvestido;
    private BigDecimal saldoAtual;    
    private BigDecimal rendimentosPorcentagem;
}
