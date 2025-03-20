package com.backend.application.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.backend.application.enums.TipoOperacao;

@Entity
@Table(name = "user_trade")
@Data 
public class UserTrade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data", nullable = false)
    private LocalDateTime data;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_operacao", nullable = false)
    private TipoOperacao tipoOperacao;

    @Column(name = "mercado", nullable = false, length = 50)
    private String mercado;

    @Column(name = "prazo", length = 50)
    private String prazo;

    @Column(name = "instrument", nullable = false, length = 100)
    private String instrument;

    @Column(name = "especificacao", length = 100)
    private String especificacao;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @Column(name = "preco", nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(name = "valor_total", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorTotal;

}
