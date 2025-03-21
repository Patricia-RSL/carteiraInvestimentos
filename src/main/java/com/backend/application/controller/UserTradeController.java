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

import com.backend.application.entities.UserTrade;
import com.backend.application.enums.TipoOperacao;
import com.backend.application.services.UserTradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/usertrade")
@Tag(name = "UserTradeController", description = "Endpoint para obter transações de usuários")
public class UserTradeController {

    private final UserTradeService userTradeService;

    @Operation(summary = "Obter todas as transações", description = "Retorna uma lista de todas as transações de usuários")
    @ApiResponse(responseCode = "200", description = "Lista de transações obtida com sucesso",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserTrade.class))})
    @GetMapping("/")
    public ResponseEntity<List<UserTrade>> getAll() {
        return ResponseEntity.ok().body(userTradeService.getAll());
    }

    @Operation(summary = "Obter transação por ID", description = "Retorna uma transação específica pelo ID")
    @ApiResponse(responseCode = "200", description = "Transação obtida com sucesso",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserTrade.class))})
    @ApiResponse(responseCode = "404", description = "Transação não encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<UserTrade> getById(@Parameter(description = "ID da transação") @PathVariable Long id) {
        Optional<UserTrade> optTrade = userTradeService.getById(id);

        if(optTrade.isPresent()){
            return ResponseEntity.ok().body(optTrade.get());
        }

        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Obter transações por ativo", description = "Retorna todas as transações associadas a um ativo específico")
    @ApiResponse(responseCode = "200", description = "Lista de transações obtida com sucesso",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserTrade.class))})
    @GetMapping("/instrument/{instrument}")
    public ResponseEntity<List<UserTrade>> getByInstrument(@Parameter(description = "Nome do instrumento") @PathVariable String instrument) {
        return ResponseEntity.ok().body(userTradeService.findAllByInstrument(instrument));
    }

    @Operation(summary = "Obter transações por tipo de operação", description = "Retorna todas as transações associadas a um tipo de operação específico")
    @ApiResponse(responseCode = "200", description = "Lista de transações obtida com sucesso",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserTrade.class))})
    @GetMapping("/tipo-operacao/{tipoOperacao}")
    public ResponseEntity<List<UserTrade>> getByTipoOpeacao(@Parameter(description = "Tipo de operação") @PathVariable TipoOperacao tipoOperacao) {
        return ResponseEntity.ok().body(userTradeService.findAllByTipoOperacao(tipoOperacao));
    }
}