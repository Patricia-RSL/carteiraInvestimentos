package com.backend.Application.repository;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.Application.entities.UserTrade;
import com.backend.Application.enums.TipoOperacao;

@Repository
public interface UserTradeRepository extends JpaRepository<UserTrade, Long>, JpaSpecificationExecutor<UserTrade>{

    public List<UserTrade> findAllByInstrument(String instrument);

    public List<UserTrade> findAllByTipoOperacao(TipoOperacao tipo);

    public List<UserTrade> findAllByTipoOperacaoAndInstrumentAndData(TipoOperacao tipo, String instrument, LocalDateTime dataInicio);

    @Query("SELECT ut FROM UserTrade ut WHERE ut.tipoOperacao = :tipo " +
       "AND ut.instrument IN :instrument " +
       "AND ut.data >= :dataInicio " +
       "AND ut.data <= :dataFim")
    List<UserTrade> findAllByTipoOperacaoAndInstrumentInAndDataGreaterThanEqualAndDataLessThanEqual(
        @Param("tipo") TipoOperacao tipo, 
        @Param("instrument") List<String> instrument, 
        @Param("dataInicio") LocalDateTime dataInicio, 
        @Param("dataFim") LocalDateTime dataFim
    );

    @Query("SELECT SUM(ut.valorTotal) as soma FROM UserTrade ut WHERE ut.tipoOperacao = :tipo " +
       "AND ut.instrument IN :instrument " +
       "AND ut.data >= :dataInicio " +
       "AND ut.data <= :dataFim")
    BigDecimal getSumInstrumentIFilterBy(
        @Param("tipo") TipoOperacao tipo, 
        @Param("instrument") List<String> instrument, 
        @Param("dataInicio") LocalDateTime dataInicio, 
        @Param("dataFim") LocalDateTime dataFim
    );

    @Query("SELECT SUM(ut.quantidade)   " +
            "FROM UserTrade ut " +
            "WHERE ut.tipoOperacao = :tipo " +
            "AND ut.instrument IN :instrument " +
            "AND ut.data >= :dataInicio " +
            "AND ut.data <= :dataFim " +
            "GROUP BY ut.instrument")
    Integer getTotalInstrumentFilterBy(
        @Param("tipo") TipoOperacao tipo, 
        @Param("instrument") String instrument, 
        @Param("dataInicio") LocalDateTime dataInicio, 
        @Param("dataFim") LocalDateTime dataFim
    );
}
