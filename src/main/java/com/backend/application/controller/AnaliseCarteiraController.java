package com.backend.application.controller;

import com.backend.application.dto.AnaliseCarteiraResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AnaliseCarteiraController {

    private final AnaliseCarteiraService analiseCarteiraService;
    
    @PostMapping("/analisar")
    public ResponseEntity<AnaliseCarteiraResponseDTO> analisarCarteira(@RequestBody AnaliseCarteiraRequestDTO request) {
        return ResponseEntity.ok().body(analiseCarteiraService.analiseCarteira(request));
    }
}
