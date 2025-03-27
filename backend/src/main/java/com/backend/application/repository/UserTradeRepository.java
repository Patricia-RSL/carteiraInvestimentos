package com.backend.application.repository;

import com.backend.application.enums.OperationType;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.application.entities.UserTrade;
import com.backend.application.interfaces.PortfolioAnalysisDetailItemProjection;

@Repository
public interface UserTradeRepository extends JpaRepository<UserTrade, Long>, JpaSpecificationExecutor<UserTrade>{

    public List<UserTrade> findAllByInstrument(String instrument);

    public List<UserTrade> findAllByOperationType(OperationType tipo);

    public List<UserTrade> findAllByOperationTypeAndInstrumentAndData(OperationType tipo, String instrument, LocalDateTime dataInicio);

    @Query("SELECT ut FROM UserTrade ut WHERE ut.operationType = :tipo " +
       "AND ut.instrument IN :instrument " +
       "AND ut.data >= :dataInicio " +
       "AND ut.data <= :dataFim")
    List<UserTrade> findAllByOperationTypeAndInstrumentInAndDataGreaterThanEqualAndDataLessThanEqual(
        @Param("tipo") OperationType tipo,
        @Param("instrument") List<String> instrument, 
        @Param("dataInicio") LocalDateTime dataInicio, 
        @Param("dataFim") LocalDateTime dataFim
    );

    @Query("SELECT SUM(ut.valorTotal) as soma FROM UserTrade ut WHERE ut.operationType = :tipo " +
       "AND ut.instrument IN :instrument " +
       "AND ut.data >= :dataInicio " +
       "AND ut.data <= :dataFim")
    BigDecimal getSumTotalValueFilterBy(
        @Param("tipo") OperationType tipo,
        @Param("instrument") List<String> instrument, 
        @Param("dataInicio") LocalDateTime dataInicio, 
        @Param("dataFim") LocalDateTime dataFim
    );

    @Query("SELECT SUM(ut.quantidade)   " +
            "FROM UserTrade ut " +
            "WHERE ut.operationType = :tipo " +
            "AND ut.instrument IN :instrument " +
            "AND ut.data >= :dataInicio " +
            "AND ut.data <= :dataFim " +
            "GROUP BY ut.instrument")
    Integer getTotalAmountFilterBy(
        @Param("tipo") OperationType tipo,
        @Param("instrument") String instrument, 
        @Param("dataInicio") LocalDateTime dataInicio, 
        @Param("dataFim") LocalDateTime dataFim
    );

    @Query(value = """
        SELECT 
            COALESCE(c.instrument, v.instrument) AS instrument,
            COALESCE(c.total_quantidade, 0) - COALESCE(v.total_quantidade, 0) AS instrument_amount,
            COALESCE(c.valor_total_somado, 0) - COALESCE(v.valor_total_somado, 0) AS invested_value
        FROM 
            (SELECT instrument, SUM(quantidade) AS total_quantidade, SUM(valor_total) AS valor_total_somado
            FROM user_trade 
            WHERE tipo_operacao = 'c'
            AND (:instrumentList IS NULL OR instrument IN (:instrumentList))
            AND data BETWEEN :beginDate AND :endDate
            GROUP BY instrument) c
        FULL OUTER JOIN 
            (SELECT instrument, SUM(quantidade) AS total_quantidade, SUM(valor_total) AS valor_total_somado
            FROM user_trade 
            WHERE tipo_operacao = 'v'
            AND (:instrumentList IS NULL OR instrument IN (:instrumentList))
            AND data BETWEEN :beginDate AND :endDate
            GROUP BY instrument) v
        ON c.instrument = v.instrument
    """, nativeQuery = true)
    List<PortfolioAnalysisDetailItemProjection> getAmountAndValueByInstrument(@Param("instrumentList") List<String> instrumentList, @Param("beginDate") LocalDateTime beginDate, @Param("endDate") LocalDateTime endDate);

}
