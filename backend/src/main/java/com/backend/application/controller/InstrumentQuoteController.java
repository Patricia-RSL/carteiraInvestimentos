package com.backend.application.controller;

import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.application.entities.InstrumentQuote;
import com.backend.application.services.InstrumentQuoteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/instrumentquote")
@Tag(name = "InstrumentQuoteController", description = "Endpoint para obter cotações de ativos")
public class InstrumentQuoteController {

    private final InstrumentQuoteService instrumentQuoteService;

    @Operation(summary = "Obter todas as cotações", description = "Retorna uma lista de todas as cotações disponíveis")
    @ApiResponse(responseCode = "200", description = "Lista de cotações obtida com sucesso",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = InstrumentQuote.class))})
    @GetMapping("/")
    public ResponseEntity<List<InstrumentQuote>> getAll() {
        return ResponseEntity.ok().body(instrumentQuoteService.getAll());
    }

    @Operation(summary = "Obter cotação por ID", description = "Retorna a cotação de um ativo específico pelo ID")
    @ApiResponse(responseCode = "200", description = "Cotação obtida com sucesso",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = InstrumentQuote.class))})
    @ApiResponse(responseCode = "404", description = "Cotação não encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<InstrumentQuote> getById(@Parameter(description = "ID da cotação") @PathVariable Long id) {
        Optional<InstrumentQuote> optIO = instrumentQuoteService.getById(id);

        if (optIO.isPresent()) {
            return ResponseEntity.ok().body(optIO.get());
        }

        return ResponseEntity.notFound().build();
    }
}