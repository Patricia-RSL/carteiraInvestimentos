package com.backend.application.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnaliseCarteiraRequestDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataInicio;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataFim;
    private List<String> instrumentList;

    @PostConstruct
    public AnaliseCarteiraRequestDTO init() {
        if (this.instrumentList == null) {
            this.instrumentList = new ArrayList<>();  // valor padrão, lista vazia
        }
        if (this.dataInicio == null) {
            this.dataInicio = LocalDateTime.of(1900, 1, 1, 0, 0);  // valor padrão para dataInicio
        }
        if (this.dataFim == null) {
            this.dataFim = LocalDateTime.of(2020,4,30, 0,0);
        }
        return this;
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

