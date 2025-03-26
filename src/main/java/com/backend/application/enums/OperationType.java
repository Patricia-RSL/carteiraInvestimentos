package com.backend.application.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OperationType {
    c("Buy"),
    v("Sale");

    private final String label;

    public String getLabel() {
        return label;
    }

    public static OperationType fromCode(String code) {
        for (OperationType type : OperationType.values()) {
            if (type.name().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}
