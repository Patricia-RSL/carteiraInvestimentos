package com.backend.application.converter;

import com.backend.application.dto.PortfolioAnalysisDetailItemDTO;
import com.backend.application.interfaces.PortfolioAnalysisDetailItemProjection;

import java.math.BigDecimal;
import java.util.List;

public class PortfolioAnalysisDetailItemDTOConverter {

    public static PortfolioAnalysisDetailItemDTO fromProjectionToDTO(PortfolioAnalysisDetailItemProjection projection){
        PortfolioAnalysisDetailItemDTO dto = new PortfolioAnalysisDetailItemDTO();
        dto.setInstrument(projection.getInstrument());
        dto.setInstrumentAmount(projection.getInstrumentAmount());
        dto.setInvestedValue(projection.getInvestedValue());
        return dto;
    }

    public static List<PortfolioAnalysisDetailItemDTO> toDTOList(List<PortfolioAnalysisDetailItemProjection> projections){
        return projections.stream().map(PortfolioAnalysisDetailItemDTOConverter::fromProjectionToDTO).toList();
    }

    public static PortfolioAnalysisDetailItemProjection toProjection(PortfolioAnalysisDetailItemDTO dto){
        return new PortfolioAnalysisDetailItemProjection() {
            @Override
            public String getInstrument() {
                return dto.getInstrument();
            }

            @Override
            public Integer getInstrumentAmount() {
                return dto.getInstrumentAmount();
            }

            @Override
            public BigDecimal getInvestedValue() {
                return dto.getInvestedValue();
            }
        };
    }

    public static List<PortfolioAnalysisDetailItemProjection> toProjectionList(List<PortfolioAnalysisDetailItemDTO> dtos){
        return dtos.stream().map(PortfolioAnalysisDetailItemDTOConverter::toProjection).toList();
    }
}
