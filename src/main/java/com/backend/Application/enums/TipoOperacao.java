package com.backend.Application.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoOperacao {
    c("Compra"),
    v("Venda");

    private final String label;

    public String getLabel() {
        return label;
    }

    // Alterado para comparar o 'codigo' (String) com o valor do 'label' (String)
    public static TipoOperacao fromCodigo(String codigo) {
        for (TipoOperacao tipo : TipoOperacao.values()) {
            if (tipo.name().equals(codigo)) { // Corrigido para comparar corretamente
                return tipo;
            }
        }
        throw new IllegalArgumentException("Código inválido: " + codigo);
    }
}
