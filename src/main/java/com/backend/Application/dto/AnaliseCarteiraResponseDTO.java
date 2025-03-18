package com.backend.Application.dto;

public class AnaliseCarteiraResponseDTO {
    
    private AnaliseCarteiraRequestDTO analiseCarteiraRequestDTO;
    private int totalAcoes;
    private double saldoAtual;
    private double rendimentos;

    public AnaliseCarteiraResponseDTO() {
    }

    public AnaliseCarteiraResponseDTO(int totalAcoes, double saldoAtual, double rendimentos) {
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

    public double getSaldoAtual() {
        return saldoAtual;
    }

    public void setSaldoAtual(double saldoAtual) {
        this.saldoAtual = saldoAtual;
    }

    public double getRendimentos() {
        return rendimentos;
    }

    public void setRendimentos(double rendimentos) {
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
