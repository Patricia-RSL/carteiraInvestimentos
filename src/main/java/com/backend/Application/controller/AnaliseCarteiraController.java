package com.backend.Application.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.Application.dto.AnaliseCarteiraRequestDTO;
import com.backend.Application.dto.AnaliseCarteiraResponseDTO;
import com.backend.Application.entities.UserTrade;
import com.backend.Application.enums.TipoOperacao;
import com.backend.Application.services.AnaliseCarteiraService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/analise-carteira")
public class AnaliseCarteiraController {

    @Autowired
    private final AnaliseCarteiraService analiseCarteiraService;
    
    @GetMapping("/analisar")
    public ResponseEntity<Object> analisarCarteira(@RequestBody AnaliseCarteiraRequestDTO request) {
        return ResponseEntity.ok().body(analiseCarteiraService.analiseCarteira(request));
    }
}
