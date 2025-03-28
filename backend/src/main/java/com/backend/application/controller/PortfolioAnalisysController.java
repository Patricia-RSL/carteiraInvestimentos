package com.backend.application.controller;

import com.backend.application.dto.PortfolioAnalysisResponseDTO;
import com.backend.application.dto.PortfolioAnalysisRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.backend.application.services.PortfolioAnalisysService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/analise-carteira")
@Tag(name = "PortfolioAnalisysController", description = "Endpoint para obter analises da carteira de ativos")
public class PortfolioAnalisysController {

    private final PortfolioAnalisysService portfolioAnalisysService;

    @Operation(summary = "Analisar carteira de ativos", description = "Retorna análises da carteira de ativos")
    @ApiResponse( responseCode = "200", description = "Resultado das análises da carteira obtido com sucesso",
            content = { @Content(mediaType = "application/json",
            schema = @Schema(implementation = PortfolioAnalysisResponseDTO.class)) })
    @GetMapping("/analisar")
    public ResponseEntity<PortfolioAnalysisResponseDTO> analyzePortfolio() throws BadRequestException {
        return ResponseEntity.ok().body(portfolioAnalisysService.analyzePortfolio());
    }

    @Operation(summary = "Analisar carteira de ativos com filtro", description = "Retorna análises da carteira de ativos de acordo com filtros fornecidos")
    @ApiResponse( responseCode = "200", description = "Resultado das análises da carteira obtido com sucesso",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PortfolioAnalysisResponseDTO.class)) })
    @PostMapping("/analisar")
    public ResponseEntity<PortfolioAnalysisResponseDTO> analyzePortfolioWithFilter(@Parameter(description = "filtros") @RequestBody PortfolioAnalysisRequestDTO request) throws BadRequestException {
        return ResponseEntity.ok().body(portfolioAnalisysService.analyzePortfolio(request.init()));
    }

  @Operation(summary = "Obter ações", description = "Retorna uma lista de todas as ações presentes na carteira")
  @ApiResponse(responseCode = "200", description = "Lista de ações obtida com sucesso",
    content = {@Content(mediaType = "application/json",
      schema = @Schema(implementation = String.class))})
  @GetMapping("/instruments")
  public ResponseEntity<List<String>> getInstrumentList(){
    return ResponseEntity.ok().body(portfolioAnalisysService.getInstruments());
  }
}
