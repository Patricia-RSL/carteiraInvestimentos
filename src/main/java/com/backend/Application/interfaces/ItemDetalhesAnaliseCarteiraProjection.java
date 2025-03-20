package com.backend.Application.interfaces;

import java.math.BigDecimal;

public interface ItemDetalhesAnaliseCarteiraProjection {
    String getInstrument();
    Integer getTotalAcoes();
    BigDecimal getSaldoInvestido();
}