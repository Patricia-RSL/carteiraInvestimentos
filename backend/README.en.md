# Investment Portfolio Tracker

This project aims to help investors monitor the profitability of their investment portfolios by updating the database information through an external API. The system cross-references data on the investor's purchase and sale history with stock closing prices to calculate the accumulated return.

The API documentation can be accessed at http://localhost:8080/swagger-ui/index.html#/

## Prerequisites

- Java 17
- PostgreSQL
- Key for Brapi API (https://brapi.dev/dashboard) (If you want to access updated quote data, as the backup data only goes until 2020-04-30).
  
## Features

The project has the following expected features:

1. **View of stocks on a specific day**:
  - Allows retrieving all stocks and their respective accumulated returns in R\$ and % for a given period (start date and end date).
  - **Status**: Implemented ✅

2. **Total portfolio return**:
  - Calculates the accumulated return in R\$ and % of the investment portfolio for a given period (start date and end date).
  - **Status**: Implemented ✅

3. **Return of an individual stock**:
  - Allows filtering to display the accumulated return in R\$ and % of a stock on a specific day, regardless of whether a transaction occurred that period.
  - **Status**: Implemented ✅

4. **Stock price update through external API query**:
  - When analyzing returns up to a specific end date, if the stock price is not available in the database, the information is retrieved from the Brapi API. Stocks without available data in the Brapi API are excluded from the portfolio summary.
  - **Status**: Implemented ✅ (Currently updating the last three months to reduce requests; improvements will be made to check the last saved stock price date in the database for a given stock).

5. **Endpoint for paginated retrieval of transaction history**:
  - Implements pagination in the transaction history response, enabling more efficient and scalable queries.
  - **Status**: Implemented ✅

6. **UserTrade CRUD**:
  - Create, edit, and delete buy and sell transactions for stocks.
  - **Status**: To be implemented in the future ⏳

7. **Return Graphs**:
  - Displays graphs that facilitate the visualization of accumulated returns, both by stock and by the total portfolio.
  - **Status**: Will be implemented in the future ⏳

## PostgreSQL Database Backup
If you want to have some initial data in the database, you can copy the contents of bolsa.bkp to your PostgreSQL by executing the following commands:

```sh
psql -U postgres -c "CREATE DATABASE bolsa;"
psql -U postgres -d bolsa -f <your path>/bolsa.bkp
```
## Configuration
Before starting the project, configure the `application.properties` file with the necessary information for creating the admin user, password encryption, and Brapi API access key.

```properties
brapi.api.key= <your_brapi_api_key>
admin.password= your_admin_password
jwt.secret= your_32_characters_long_secret_key_here
```

Also, it is necessary to configure the `.env` file with the database information.

```sh

DB_HOST=localhost
DB_PORT=5432
DB_NAME=bolsa
DB_USER=postgres
DB_PASSWORD=password
```
## How to run the project with Docker
This project already has a `Dockerfile` configured to run the application in a container.

```sh
docker-compose down
docker-compose build backend
docker-compose up -d
```
In this case, before importing the PostgreSQL database backup, you should import the `bolsa.bkp` file into your container.

```sh  
docker cp bolsa.bkp backend_db_1:/tmp/banco.bkp
docker exec -it backend_db_1 bash
psql -U postgres -d bolsa -f tmp/banco.bkp
```
or
```sh
docker cp bolsa.bkp backend-db-1:/tmp/banco.bkp
docker exec -it backend-db-1 bash
psql -U postgres -d bolsa -f tmp/banco.bkp
```
You will also need to modify your `.env` file to point to the database in the container.
```sh
DB_HOST=db
DB_PORT=5431
DB_NAME=bolsa
DB_USER=postgres
DB_PASSWORD=password
```
