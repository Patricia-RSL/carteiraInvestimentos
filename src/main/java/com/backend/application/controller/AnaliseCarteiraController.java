package com.backend.application.controller;

import com.backend.application.dto.AnaliseCarteiraResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.backend.application.dto.AnaliseCarteiraRequestDTO;
import com.backend.application.services.AnaliseCarteiraService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/analise-carteira")
@Tag(name = "AnaliseCarteiraController", description = "Endpoint para obter analises da carteira de ativos")
public class AnaliseCarteiraController {

    private final AnaliseCarteiraService analiseCarteiraService;

    @Operation(summary = "Analisar carteira de ativos", description = "Retorna análises da carteira de ativos")
    @ApiResponse( responseCode = "200", description = "Resultado das análises da carteira obtido com sucesso",
            content = { @Content(mediaType = "application/json",
            schema = @Schema(implementation = AnaliseCarteiraResponseDTO.class)) })
    @GetMapping("/analisar")
    public ResponseEntity<AnaliseCarteiraResponseDTO> analisarCarteira() {
        return ResponseEntity.ok().body(analiseCarteiraService.analisarCarteira());
    }

    @Operation(summary = "Analisar carteira de ativos com filtro", description = "Retorna análises da carteira de ativos de acordo com filtros fornecidos")
    @ApiResponse( responseCode = "200", description = "Resultado das análises da carteira obtido com sucesso",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AnaliseCarteiraResponseDTO.class)) })
    @PostMapping("/analisar")
    public ResponseEntity<AnaliseCarteiraResponseDTO> analisarCarteiraComFiltro(@Parameter(description = "filtros") @RequestBody AnaliseCarteiraRequestDTO request) {
        return ResponseEntity.ok().body(analiseCarteiraService.analisarCarteiraComFiltro(request.init()));
    }
}
