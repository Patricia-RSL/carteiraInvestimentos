package com.backend.Application.dto;

import java.math.BigDecimal;

public class AnaliseCarteiraResponseDTO {
    
    private AnaliseCarteiraRequestDTO analiseCarteiraRequestDTO;
    private int totalAcoes;
    private BigDecimal saldoAtual;
    private BigDecimal rendimentos;

    public AnaliseCarteiraResponseDTO() {
    }

    public AnaliseCarteiraResponseDTO(int totalAcoes, BigDecimal saldoAtual, BigDecimal rendimentos) {
        this.totalAcoes = totalAcoes;
        this.saldoAtual = saldoAtual;
        this.rendimentos = rendimentos;
    }

    public int getTotalAcoes() {
        return totalAcoes;
    }

    public void setTotalAcoes(int totalAcoes) {
        this.totalAcoes = totalAcoes;
    }

    public BigDecimal getSaldoAtual() {
        return saldoAtual;
    }

    public void setSaldoAtual(BigDecimal saldoAtual) {
        this.saldoAtual = saldoAtual;
    }

    public BigDecimal getRendimentos() {
        return rendimentos;
    }

    public void setRendimentos(BigDecimal rendimentos) {
        this.rendimentos = rendimentos;
    }

    @Override
    public String toString() {
        return "AnaliseCarteiraDTO{" +
                "totalAcoes=" + totalAcoes +
                ", saldoAtual=" + saldoAtual +
                ", rendimentos=" + rendimentos +
                '}';
    }
}
