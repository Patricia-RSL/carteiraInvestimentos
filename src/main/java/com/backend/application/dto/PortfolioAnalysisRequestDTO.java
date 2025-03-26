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
public class PortfolioAnalysisRequestDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beginDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
    private List<String> instrumentList;

    @PostConstruct
    public PortfolioAnalysisRequestDTO init() {
        if (this.instrumentList == null) {
            this.instrumentList = new ArrayList<>();
        }
        if (this.beginDate == null) {
            this.beginDate = LocalDateTime.of(1900, 1, 1, 0, 0);  // valor padrão para dataInicio
        }
        if (this.endDate == null) {
            this.endDate = LocalDateTime.of(2020,4,30, 0,0);
        }
        return this;
    }

    @Override
    public String toString() {
        return "PortfolioAnalysisRequestDTO{" +
                "beginDate=" + beginDate +
                ", endDate=" + endDate +
                ", instrumentList=" + instrumentList +
                '}';
    }
}

