package com.backend.application.entities;

import com.backend.application.enums.OperationType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_trade")
@Data
public class UserTrade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @Column(name = "data", nullable = false)
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_operacao", nullable = false)
    private OperationType operationType;

    @Column(name = "mercado", nullable = false, length = 50)
    private String market;

    @Column(name = "prazo", length = 50)
    private String maturity;

    @Column(name = "instrument", nullable = false, length = 100)
    private String instrument;

    @Column(name = "especificacao", length = 100)
    private String specification;

    @Column(name = "quantidade", nullable = false)
    private Integer amount;

    @Column(name = "preco", nullable = false, precision = 10, scale = 2)
    private BigDecimal value;

    @Column(name = "valor_total", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalValue;

}
