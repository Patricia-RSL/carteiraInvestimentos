package com.backend.application.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.backend.application.entities.UserTrade;
import com.backend.application.enums.OperationType;
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

    @Operation(summary = "Obter todas as transações com paginação", description = "Retorna uma lista paginada de todas as transações de usuários")
    @ApiResponse(responseCode = "200", description = "Lista de transações obtida com sucesso",
      content = {@Content(mediaType = "application/json",
        schema = @Schema(implementation = UserTrade.class))})
    @GetMapping("/paginated")
    public ResponseEntity<Page<UserTrade>> getAllPaginated(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate begindate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
      @RequestParam(required = false) List<String> instruments) {
      return ResponseEntity.ok().body(userTradeService.getAllPaginatedAndFiltered(page, size, begindate, endDate, instruments));
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
    @GetMapping("/tipo-operacao/{operationType}")
    public ResponseEntity<List<UserTrade>> getByTipoOpeacao(@Parameter(description = "Tipo de operação") @PathVariable OperationType operationType) {
        return ResponseEntity.ok().body(userTradeService.findAllByOperationType(operationType));
    }

    @Operation(summary = "Criar uma nova transação", description = "Cria uma nova transação de usuário")
    @ApiResponse(responseCode = "201", description = "Transação criada com sucesso",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserTrade.class))})
    @PostMapping("/")
    public ResponseEntity<UserTrade> create(@RequestBody UserTrade userTrade) {
        UserTrade createdTrade = userTradeService.save(userTrade);
        return ResponseEntity.status(201).body(createdTrade);
    }

    @Operation(summary = "Atualizar uma transação", description = "Atualiza uma transação de usuário existente")
    @ApiResponse(responseCode = "200", description = "Transação atualizada com sucesso",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserTrade.class))})
    @PutMapping("/{id}")
    public ResponseEntity<UserTrade> update(@PathVariable Long id, @RequestBody UserTrade userTrade) {
        Optional<UserTrade> optTrade = userTradeService.getById(id);

        if (optTrade.isPresent()) {
            userTrade.setId(id);
            UserTrade updatedTrade = userTradeService.save(userTrade);
            return ResponseEntity.ok().body(updatedTrade);
        }

        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Deletar uma transação", description = "Deleta uma transação de usuário existente")
    @ApiResponse(responseCode = "204", description = "Transação deletada com sucesso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<UserTrade> optTrade = userTradeService.getById(id);

        if (optTrade.isPresent()) {
            userTradeService.delete(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
