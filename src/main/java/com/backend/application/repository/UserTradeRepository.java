package com.backend.application.repository;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.application.entities.UserTrade;
import com.backend.application.enums.TipoOperacao;
import com.backend.application.interfaces.ItemDetalhesAnaliseCarteiraProjection;

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
    BigDecimal getSumValorTotalFilterBy(
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
    Integer getTotalQuantidadeFilterBy(
        @Param("tipo") TipoOperacao tipo, 
        @Param("instrument") String instrument, 
        @Param("dataInicio") LocalDateTime dataInicio, 
        @Param("dataFim") LocalDateTime dataFim
    );

    @Query(value = """
        SELECT 
            COALESCE(c.instrument, v.instrument) AS instrument,
            COALESCE(c.total_quantidade, 0) - COALESCE(v.total_quantidade, 0) AS total_acoes,
            COALESCE(c.valor_total_somado, 0) - COALESCE(v.valor_total_somado, 0) AS saldo_investido
        FROM 
            (SELECT instrument, SUM(quantidade) AS total_quantidade, SUM(valor_total) AS valor_total_somado
            FROM user_trade 
            WHERE tipo_operacao = 'c'
            AND data BETWEEN :dataInicio AND :dataFim
            GROUP BY instrument) c
        FULL OUTER JOIN 
            (SELECT instrument, SUM(quantidade) AS total_quantidade, SUM(valor_total) AS valor_total_somado
            FROM user_trade 
            WHERE tipo_operacao = 'v'
            AND data BETWEEN :dataInicio AND :dataFim
            GROUP BY instrument) v
        ON c.instrument = v.instrument
    """, nativeQuery = true)
    List<ItemDetalhesAnaliseCarteiraProjection> calcularTotalQuantidadeAndSaldoPorInstrument(@Param("dataInicio") LocalDateTime dataInicio, @Param("dataFim") LocalDateTime dataFim);

}
