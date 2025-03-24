package com.backend.application.converter;

import com.backend.application.dto.ItemDetalhesAnaliseCarteiraDTO;
import com.backend.application.interfaces.ItemDetalhesAnaliseCarteiraProjection;

import java.math.BigDecimal;
import java.util.List;

public class ItemDetalhesAnaliseCarteiraDTOConverter {

    public static ItemDetalhesAnaliseCarteiraDTO fromProjectionToDTO(ItemDetalhesAnaliseCarteiraProjection projection){
        ItemDetalhesAnaliseCarteiraDTO dto = new ItemDetalhesAnaliseCarteiraDTO();
        dto.setInstrument(projection.getInstrument());
        dto.setQtdAcoes(projection.getTotalAcoes());
        dto.setValorInvestido(projection.getSaldoInvestido());
        return dto;
    }

    public static List<ItemDetalhesAnaliseCarteiraDTO> toDTOList(List<ItemDetalhesAnaliseCarteiraProjection> projections){
        return projections.stream().map(ItemDetalhesAnaliseCarteiraDTOConverter::fromProjectionToDTO).toList();
    }

    public static ItemDetalhesAnaliseCarteiraProjection toProjection(ItemDetalhesAnaliseCarteiraDTO dto){
        return new ItemDetalhesAnaliseCarteiraProjection() {
            @Override
            public String getInstrument() {
                return dto.getInstrument();
            }

            @Override
            public Integer getTotalAcoes() {
                return dto.getQtdAcoes();
            }

            @Override
            public BigDecimal getSaldoInvestido() {
                return dto.getValorInvestido();
            }
        };
    }

    public static List<ItemDetalhesAnaliseCarteiraProjection> toProjectionList(List<ItemDetalhesAnaliseCarteiraDTO> dtos){
        return dtos.stream().map(ItemDetalhesAnaliseCarteiraDTOConverter::toProjection).toList();
    }
}
