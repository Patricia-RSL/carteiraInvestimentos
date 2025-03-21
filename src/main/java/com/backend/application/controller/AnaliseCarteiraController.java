package com.backend.application.controller;

import com.backend.application.dto.AnaliseCarteiraResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    @GetMapping("/analisar")
    public ResponseEntity<AnaliseCarteiraResponseDTO> analisarCarteira() {
        return ResponseEntity.ok().body(analiseCarteiraService.analiseCarteira());
    }

    @PostMapping("/analisar")
    public ResponseEntity<AnaliseCarteiraResponseDTO> analisarCarteiraComFiltro(@RequestBody AnaliseCarteiraRequestDTO request) {
        return ResponseEntity.ok().body(analiseCarteiraService.analiseCarteiraComFiltro(request.init()));
    }
}
