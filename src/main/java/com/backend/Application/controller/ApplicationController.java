package com.backend.Application.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApplicationController {

    @GetMapping("/dados")
    public ResponseEntity<String> getDados() {
        return ResponseEntity.ok("Dados do servidor Java");
    }
}
