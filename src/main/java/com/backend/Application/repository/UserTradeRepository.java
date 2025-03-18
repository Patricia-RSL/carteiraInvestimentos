package com.backend.Application.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.backend.Application.entities.UserTrade;
import com.backend.Application.enums.TipoOperacao;

@Repository
public interface UserTradeRepository extends JpaRepository<UserTrade, Long>, JpaSpecificationExecutor<UserTrade>{

    public List<UserTrade> findAllByInstrument(String instrument);

    public List<UserTrade> findAllByTipoOperacao(TipoOperacao tipo);
    
}
