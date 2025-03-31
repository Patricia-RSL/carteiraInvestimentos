# Investment Portfolio Tracker


Este projeto tem como objetivo ajudar investidores a monitorar a rentabilidade de suas carteiras de investimentos, atualizando as informações do banco através de uma API externa. O sistema cruza dados sobre o histórico de compra e venda da carteira do investidor com os valores de fechamento das ações para calcular o rendimento acumulado.

A documentação da API pode ser acessada em http://localhost:8080/swagger-ui/index.html#/

## Pré-requisitos

- Java 17
- PostgreSQL
- Chave para Brapi API (https://brapi.dev/dashboard) (Caso queira acessar dados de cotação atualizados, uma ve que os dados de bkp só vão ate 2020-04-30)
  
## Funcionalidades

O projeto possui as seguintes funcionalidades esperadas:

1. **Visualização das ações no dia**:
   - Permite a obter de todas as ações e seus respectivos rendimentos acumulados em R$ e % em um período específico.
   - **Status**: Implementada ✅

2. **Rendimento total da carteira**:
   - Calcula o rendimento acumulado em R$ e % da carteira de investimentos para um período determinado (data inicial e data final).
   - **Status**: Implementada ✅

3. **Rendimento de uma ação individual**:
   - É possível filtrar, de forma a exibir o rendimento acumulado em R$ e % de uma ação em um dia específico, independente de ter ocorrido uma operação nesse dia.
   - **Status**: Implementada ✅

4. **Atualização da cotação da ação através de consulta à API Externa**:
   - Ao analizar rendimentos até uma data fim específica, se a cotação da ação não estiver no banco, busca-se essa informação na API do Brapi. Ações que não tiverem a informação na Brapi API são desconsideradas do resumo da carteira.
   - **Status**: Implementada ✅ (Atualmente esta atualizando os ultimos três meses para evitar requisições, melhorias irão ser feitas ao verificar qual a ultima data de cotação salva no banco para determinada ação).

5. **Endpoint para obtenção paginada do histórico de transações**:  
   - Implementa paginação no retorno das transações, permitindo consultas mais eficientes e escaláveis.  
   - **Status**: Implementada ✅

6. **Gráficos de Rendimento**:
   - Exibe gráficos que facilitam a visualização do rendimento acumulado, tanto por ação quanto pelo total da carteira.
   - **Status**: Será implementada futuramente ⏳

**Backup do banco PostgreSQL**

Se desejar ter alguns dados iniciais no banco, é possível copiar o conteúdo de bolsa.bkp para o seu PostgreSQL executando os sequintes comandos

```sh

psql -U postgres -d bolsa -f <seu caminho>/banco.bkp

```

## Como rodar o projeto com Docker

Este projeto já possui um `Dockerfile` configurado para rodar a aplicação em um container.

```sh
docker-compose down
docker-compose build backend
docker-compose up -d
```

Neste caso, antes de importar o backup do banco PostgreSQL você deve importar o arquivo `bolsa.bkp` para o seu container.

```sh
docker cp bolsa.bkp backend_db_1:/tmp/banco.bkp

docker exec -it backend_db_1 bash

psql -U postgres -d bolsa -f tmp/banco.bkp
```
ou

```sh
docker cp bolsa.bkp backend-db-1:/tmp/banco.bkp

docker exec -it backend-db-1 bash

psql -U postgres -d bolsa -f tmp/banco.bkp
```

E o `.env` para apontar para o banco que está no container

```sh

DB_HOST=db
DB_PORT=5431
DB_NAME=bolsa
DB_USER=postgres
DB_PASSWORD=password

```
