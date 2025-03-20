package com.backend.application.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnaliseCarteiraRequestDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataInicio;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
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

