package com.backend.application.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ResumoAnaliseCarteiraDTO {
    
    private int totalAcoes;
    private BigDecimal saldoAtual;
    private BigDecimal rendimentos;

    @Override
    public String toString() {
        return "AnaliseCarteiraDTO{" +
                "totalAcoes=" + totalAcoes +
                ", saldoAtual=" + saldoAtual +
                ", rendimentos=" + rendimentos +
                '}';
    }
}
