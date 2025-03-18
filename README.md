**Backup do banco PostgreSQL**

Para copiar o conteúdo de bolsa.bkp para o seu PostgreSQL execute os sequintes comandos

```sh
docker cp Application/bolsa.bkp application_db_1:/tmp/banco.bkp

docker exec -it application_db_1 bash

psql -U postgres -d bolsa -f /tmp/banco.bkp

```
