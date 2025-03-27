# Investment Portfolio Tracker

Este projeto tem como objetivo ajudar investidores a monitorar a rentabilidade de suas carteiras de investimentos, focando especialmente nas operações de compra. Este projeto não considera a rentabilidade quando o investidor esta vendido em uma ação, ou seja, quando ele vendeu ações que ainda não possui para depois comprá-los.  O sistema cruza dados sobre o histórico de compra e venda da carteira do investidor com os valores de fechamento das ações para calcular o rendimento acumulado. <!-- e apresentar gráficos de desempenho.o -->

## Estrutura do Repositório

Este repositório contém dois diretórios principais:

- **back-end**: Implementação da API em JAVA que processa os dados do investimento, realizando o cálculo dos rendimentos e fornecendo os dados necessários para o front-end.
- **front-end**: Interface do usuário desenvolvida em Angular, com foco na visualização dos dados de rendimentos <!-- e gráficos-->.

Ambos os componentes podem ser executados de forma independente, mas aqui fazem parte do mesmo repositório para não poliuir o meu git.

## Funcionalidades

O projeto possui as seguintes funcionalidades esperadas:

1. **Visualização das ações no dia**:
   - Permite a visualização de todas as ações e seus respectivos rendimentos acumulados em R$ e % em um dia específico.
   - **Status**: Partcialmente implementada 🛠️ (Falta o campo para escolher o dia no Front-end)

2. **Rendimento total da carteira**:
   - Calcula o rendimento acumulado em R$ e % da carteira de investimentos para um período determinado (data inicial e data final).
   - **Status**: Partcialmente implementada 🛠️ (Falta o campo para escolher os dias no Front-end)

3. **Rendimento de uma ação individual**:
   - É possível filtrar, de forma a exibir o rendimento acumulado em R$ e % de uma ação em um dia específico, independente de ter ocorrido uma operação nesse dia.
   - **Status**: Parcialmente implementada 🛠️ (Falta o campo para escolher a ação no front-end )

4. **Determinando os ganhos e perdas**:
   - Apresenta o detalhamento do rendimento acumulado, diferenciando ações com ganhos e ações com prejuízo no período.
   - **Status**: Implementada ✅

5. **Gráficos de Rendimento**:
   - Exibe gráficos que facilitam a visualização do rendimento acumulado, tanto por ação quanto pelo total da carteira.
   - **Status**: Será implementada futuramente ⏳

## Como Rodar o Projeto

Tanto o back-end quanto o front-end tem seus Reame.md individuais, com explicações sobre como rodar o projeto.
