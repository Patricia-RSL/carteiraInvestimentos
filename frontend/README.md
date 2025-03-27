# Investment Portfolio Tracker

Este projeto tem como objetivo ajudar investidores a monitorar a rentabilidade de suas carteiras de investimentos, focando especialmente nas operações de compra. Este projeto não considera a rentabilidade quando o investidor está vendido em um ativo, ou seja, quando ele vendeu ativos que ainda não possui para depois comprá-los. O sistema cruza dados sobre o histórico de compra e venda da carteira do investidor com os valores de fechamento das ações para calcular o rendimento acumulado.

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 16.2.16.

## Funcionalidades

O projeto possui as seguintes funcionalidades esperadas:

1. **Visualização das ações no dia**:
   - Permite a obter de todas as ações e seus respectivos rendimentos acumulados em R$ e % em um dia específico.
   - **Status**: Parcialmente implementada (Falta o filtro por data)

2. **Visualização de resumo da carteira**:
   - Mostra o rendimento acumulado em R$ e % da carteira de investimentos para um período determinado (data inicial e data final).
   - **Status**: Parcialmente implementada (Falta o filtro por data)

3. **Visualização do rendimento de uma ação individual**:
   - É possível filtrar, de forma a exibir o rendimento acumulado em R$ e % de uma ação em um dia específico, independente de ter ocorrido uma operação nesse dia.
   - **Status**: Parcialmente implementada (Falta o filtro por ação)
  
4. **Visualização do histórico de transações**:
   - É possível ver todas as operações de compra e venda presentes no banco.
   - **Status**: Será implementada futuramente ⏳

5. **Adicionar e remover transações do histórico**:
   - É possível adicionar e remover operações de compra e venda presentes no banco.
   - **Status**: Será implementada futuramente ⏳

6. **Gráficos de Rendimento**:
   - Exibe gráficos que facilitam a visualização do rendimento acumulado, tanto por ação quanto pelo total da carteira.
   - **Status**: Será implementada futuramente ⏳

## Pré-requisitos
- npm
- yarn


## Preparando o Ambiente

Antes de rodar a aplicação, é necessário instalar as dependências do projeto. Para isso, execute o seguinte comando:

```sh
yarn install
```

## Servidor de Desenvolvimento

Para rodar o servidor de desenvolvimento, execute o seguinte comando com Yarn:

```sh
yarn start
```

## Code scaffolding

Execute `ng generate component component-name` para gerar um novo componente. Você também pode usar `ng generate directive|pipe|service|class|guard|interface|enum|module`.

## Build

Execute `yarn build` para construir o projeto. Os arquivos gerados serão armazenados no diretório dist/.
