package com.backend.application.dto;

import java.util.List;

import lombok.Data;


@Data
public class AnaliseCarteiraResponseDTO {

    private ResumoAnaliseCarteiraDTO resumoAnaliseCarteiraDTO;
    private List<ItemDetalhesAnaliseCarteiraDTO> detalhesAnaliseCarteiraDTO;
}
