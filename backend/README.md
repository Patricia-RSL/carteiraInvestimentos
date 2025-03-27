**Carteira de Investimentos**

Esta aplicação continua em desenvolvimento. A aplicação tem o simples objetivo de realizar analises em um portfólio de ativos, a partir do histórico de transaçõe (compra e venda de ativos) de um usuário e as cotações dos ativos presentes nela.

A documentação da API pode ser acessada em http://localhost:8080/swagger-ui/index.html#/

**Backup do banco PostgreSQL**

Para copiar o conteúdo de bolsa.bkp para o seu PostgreSQL execute os sequintes comandos

```sh
docker cp Application/bolsa.bkp application_db_1:/tmp/banco.bkp

docker exec -it application_db_1 bash

psql -U postgres -d bolsa -f /tmp/banco.bkp

```
