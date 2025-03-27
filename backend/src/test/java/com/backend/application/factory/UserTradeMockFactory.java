package com.backend.application.factory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.backend.application.entities.UserTrade;
import com.backend.application.enums.OperationType;

/**
 * Classe utilitária para criar objetos UserTrade mockados para testes
 * com formato compatível com os dados reais do banco
 */
public class UserTradeMockFactory {

    /**
     * Cria uma lista com 10 objetos UserTrade mocados baseados em formato real do banco
     * @return Lista de UserTrades
     */
    public static List<UserTrade> createMockUserTrades() {
        return Arrays.asList(
                // 1. ITUB4F - Compra (baseado no exemplo real)
                createUserTrade(
                        122084L,
                        LocalDateTime.of(2019, 1, 17, 0, 0, 0),
                        OperationType.c,
                        "Merc. Fracionário",
                        "",
                        "ITUB4F",
                        "ITAUUNIBANCO PN      N1",
                        5,
                        new BigDecimal("36.90"),
                        new BigDecimal("184.50")
                ),

                // 2. PETR4F - Compra
                createUserTrade(
                        122085L,
                        LocalDateTime.of(2019, 2, 10, 0, 0, 0),
                        OperationType.c,
                        "Merc. Fracionário",
                        "",
                        "PETR4F",
                        "PETROBRAS PN",
                        8,
                        new BigDecimal("24.32"),
                        new BigDecimal("194.56")
                ),

                // 3. VALE3F - Compra
                createUserTrade(
                        122086L,
                        LocalDateTime.of(2019, 3, 5, 0, 0, 0),
                        OperationType.c,
                        "Merc. Fracionário",
                        "",
                        "VALE3F",
                        "VALE ON      NM",
                        4,
                        new BigDecimal("51.14"),
                        new BigDecimal("204.56")
                ),

                // 4. BBDC4 - Compra
                createUserTrade(
                        122087L,
                        LocalDateTime.of(2019, 4, 12, 0, 0, 0),
                        OperationType.c,
                        "BOVESPA",
                        "",
                        "BBDC4",
                        "BRADESCO PN      N1",
                        10,
                        new BigDecimal("31.74"),
                        new BigDecimal("317.40")
                ),

                // 5. MGLU3 - Compra
                createUserTrade(
                        122088L,
                        LocalDateTime.of(2019, 5, 20, 0, 0, 0),
                        OperationType.c,
                        "BOVESPA",
                        "",
                        "MGLU3",
                        "MAGAZ LUIZA ON      NM",
                        15,
                        new BigDecimal("19.45"),
                        new BigDecimal("291.75")
                ),

                // 6. ITUB4F - Venda
                createUserTrade(
                        122089L,
                        LocalDateTime.of(2019, 6, 15, 0, 0, 0),
                        OperationType.v,
                        "Merc. Fracionário",
                        "",
                        "ITUB4F",
                        "ITAUUNIBANCO PN      N1",
                        3,
                        new BigDecimal("37.50"),
                        new BigDecimal("112.50")
                ),

                // 7. WEGE3 - Compra
                createUserTrade(
                        122090L,
                        LocalDateTime.of(2019, 7, 8, 0, 0, 0),
                        OperationType.c,
                        "BOVESPA",
                        "",
                        "WEGE3",
                        "WEG ON      NM",
                        12,
                        new BigDecimal("20.35"),
                        new BigDecimal("244.20")
                ),

                // 8. PETR4F - Venda
                createUserTrade(
                        122091L,
                        LocalDateTime.of(2019, 8, 14, 0, 0, 0),
                        OperationType.v,
                        "Merc. Fracionário",
                        "",
                        "PETR4F",
                        "PETROBRAS PN",
                        2,
                        new BigDecimal("26.15"),
                        new BigDecimal("52.30")
                ),

                // 9. ABEV3 - Compra
                createUserTrade(
                        122092L,
                        LocalDateTime.of(2019, 9, 10, 0, 0, 0),
                        OperationType.c,
                        "BOVESPA",
                        "",
                        "ABEV3",
                        "AMBEV S/A ON",
                        20,
                        new BigDecimal("17.88"),
                        new BigDecimal("357.60")
                ),

                // 10. BBAS3 - Compra
                createUserTrade(
                        122093L,
                        LocalDateTime.of(2019, 10, 2, 0, 0, 0),
                        OperationType.c,
                        "BOVESPA",
                        "",
                        "BBAS3",
                        "BRASIL ON      NM",
                        10,
                        new BigDecimal("42.23"),
                        new BigDecimal("422.30")
                ),
                // 11. ITUB4F - Venda
                createUserTrade(
                        122089L,
                        LocalDateTime.of(2020, 1, 15, 0, 0, 0),
                        OperationType.v,
                        "Merc. Fracionário",
                        "",
                        "ITUB4F",
                        "ITAUUNIBANCO PN      N1",
                        2,
                        new BigDecimal("37.60"),
                        new BigDecimal("75.20")
                )

        );
    }

    /**
     * Método auxiliar para criar um objeto UserTrade
     */
    private static UserTrade createUserTrade(
            Long id,
            LocalDateTime data,
            OperationType operationType,
            String mercado,
            String prazo,
            String instrument,
            String especificacao,
            Integer quantidade,
            BigDecimal preco,
            BigDecimal valorTotal) {

        UserTrade trade = new UserTrade();
        trade.setId(id);
        trade.setData(data);
        // No banco aparentemente o tipo de operação é armazenado como "c" para compra
        trade.setOperationType(operationType);
        trade.setMercado(mercado);
        trade.setPrazo(prazo);
        trade.setInstrument(instrument);
        trade.setEspecificacao(especificacao);
        trade.setQuantidade(quantidade);
        trade.setPreco(preco);
        trade.setValorTotal(valorTotal);

        return trade;
    }
}