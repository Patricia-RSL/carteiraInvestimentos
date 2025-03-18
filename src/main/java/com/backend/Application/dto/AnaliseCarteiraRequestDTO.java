package com.backend.Application.dto;

import java.time.LocalDate;
import java.util.List;

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

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public List<String> getInstrumentList() {
        return instrumentList;
    }

    public void setInstrumentList(List<String> instrumentList) {
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

