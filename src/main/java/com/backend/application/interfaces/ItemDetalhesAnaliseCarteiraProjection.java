package com.backend.application.interfaces;

import java.math.BigDecimal;

public interface ItemDetalhesAnaliseCarteiraProjection {
    String getInstrument();
    Integer getTotalAcoes();
    BigDecimal getSaldoInvestido();
}