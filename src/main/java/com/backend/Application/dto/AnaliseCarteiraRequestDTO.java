package com.backend.Application.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;
@Data
public class AnaliseCarteiraRequestDTO {
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private List<String> instrumentList;

    public AnaliseCarteiraRequestDTO() {
    }

    public AnaliseCarteiraRequestDTO(LocalDate dataInicio, LocalDate dataFim, List<String> instrumentList) {
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.instrumentList = instrumentList;
    }

    @Override
    public String toString() {
        return "AnaliseCarteiraRequestDTO{" +
                "dataInicio=" + dataInicio +
                ", dataFim=" + dataFim +
                ", instrumentList=" + instrumentList +
                '}';
    }
}

