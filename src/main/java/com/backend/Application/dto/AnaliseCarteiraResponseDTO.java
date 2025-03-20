package com.backend.Application.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;


@Data
public class AnaliseCarteiraResponseDTO {
    
    private AnaliseCarteiraRequestDTO analiseCarteiraRequestDTO;
    private ResumoAnaliseCarteiraDTO resumoAnaliseCarteiraDTO;
    private List<ItemDetalhesAnaliseCarteiraDTO> detalhesAnaliseCarteiraDTO;
}
