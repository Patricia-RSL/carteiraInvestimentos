package com.backend.application.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.application.entities.UserTrade;
import com.backend.application.repository.UserTradeRepository;
import com.backend.application.enums.TipoOperacao;

@Service
public class UserTradeService {

    @Autowired
    private UserTradeRepository userTradeRepository;

    public List<UserTrade> getAll() {
       return userTradeRepository.findAll();
    }

    public Optional<UserTrade> getById(Long id) {
        return userTradeRepository.findById(id);
    }

    public List<UserTrade> findAllByInstrument(String instrument){
        return userTradeRepository.findAllByInstrument(instrument);
    }

    public List<UserTrade> findAllByTipoOperacao(TipoOperacao tipo){
        return userTradeRepository.findAllByTipoOperacao(tipo);
    }
}
