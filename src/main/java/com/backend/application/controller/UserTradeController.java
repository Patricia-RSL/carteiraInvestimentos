package com.backend.application.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
public class UserTradeController {

    @Autowired
    private final UserTradeService userTradeService;


    @GetMapping("/")
    public ResponseEntity<List<UserTrade>> getAll() {
        return ResponseEntity.ok().body(userTradeService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserTrade> getById(@PathVariable Long id) {
        Optional<UserTrade> optTrade = userTradeService.getById(id);

        if(optTrade.isPresent()){
            return ResponseEntity.ok().body(optTrade.get());
        }

        return ResponseEntity.notFound().build();
        
    }

    @GetMapping("/instrument/{instrument}")
    public ResponseEntity<List<UserTrade>> getByInstrument(@PathVariable String instrument) {
        return ResponseEntity.ok().body(userTradeService.findAllByInstrument(instrument));
        
    }

    @GetMapping("/tipo-operacao/{tipoOperacao}")
    public ResponseEntity<List<UserTrade>> getByTipoOpeacao(@PathVariable TipoOperacao tipoOperacao) {
        return ResponseEntity.ok().body(userTradeService.findAllByTipoOperacao(tipoOperacao));
        
    }
    
}