package com.backend.application.dto;

import java.math.BigDecimal;
import java.util.Objects;

import lombok.Data;

@Data
public class ItemDetalhesAnaliseCarteiraDTO {

    private String instrument;
    private int qtdAcoes;
    private BigDecimal valorInvestido;
    private BigDecimal valorMercado;
    private BigDecimal rendimentosPorcentagem;

    public ItemDetalhesAnaliseCarteiraDTO init(String instrument, int totalAcoes, BigDecimal valorInvestido, BigDecimal valorMercado, BigDecimal rendimentosPorcentagem) {
        this.instrument = instrument;
        this.valorInvestido = valorInvestido;
        this.valorMercado = valorMercado;
        this.qtdAcoes = totalAcoes;
        this.rendimentosPorcentagem = rendimentosPorcentagem;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemDetalhesAnaliseCarteiraDTO argument = (ItemDetalhesAnaliseCarteiraDTO) o;
        return argument.getInstrument().equals(this.getInstrument()) &&
                argument.getQtdAcoes() == this.getQtdAcoes() &&
                argument.getValorInvestido().compareTo(this.getValorInvestido()) == 0 &&
                (argument.getValorMercado() == null ? this.getValorMercado() == null : argument.getValorMercado().compareTo(this.getValorMercado()) == 0) &&
                (argument.getRendimentosPorcentagem() == null ? this.getRendimentosPorcentagem() == null : argument.getRendimentosPorcentagem().compareTo(this.getRendimentosPorcentagem()) == 0);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instrument, qtdAcoes, valorInvestido, valorMercado, rendimentosPorcentagem);
    }
}
