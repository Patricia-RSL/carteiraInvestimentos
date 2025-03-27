# Investment Portfolio Tracker

This project aims to help investors track the profitability of their investment portfolios, focusing especially on purchase operations. This project does not consider profitability when the investor is short on an asset, meaning when they have sold assets they do not yet own and then repurchased them. The system cross-references data on the investor's purchase and sale history with stock closing prices to calculate the accumulated return.

The API documentation can be accessed at http://localhost:8080/swagger-ui/index.html#/

## Prerequisites

- Java 11 or higher
- PostgreSQL
  
## Features

The project has the following expected features:

1. **View of stocks on a specific day**:
   - Allows retrieving all stocks and their respective accumulated returns in R$ and % on a specific day.
   - **Status**: Implemented ✅

2. **Total portfolio return**:
   - Calculates the accumulated return in R$ and % of the investment portfolio for a given period (start date and end date).
   - **Status**: Implemented ✅

3. **Return of an individual stock**:
   - Allows filtering to display the accumulated return in R$ and % of a stock on a specific day, regardless of whether a transaction occurred that day.
   - **Status**: Implemented ✅

4. **Return Graphs**:
   - Displays graphs that facilitate the visualization of accumulated returns, both by stock and by the total portfolio.
   - **Status**: Will be implemented in the future ⏳

**PostgreSQL Database Backup**

If you want to have some initial data in the database, you can copy the content of `bolsa.bkp` to your PostgreSQL by running the following commands:

```sh
psql -U postgres -d bolsa -f <your path>/banco.bkp
```

**How to run the project with Docker**

This project already has a Dockerfile configured to run the application in a container.

```sh
docker build -t investment-portfolio-tracker .

docker run -p 8080:8080 investment-portfolio-tracker
```

In this case, before importing the PostgreSQL database backup, you should copy the bolsa.bkp file to your container.

```sh
docker cp <your_path>/bolsa.bkp application_db_1:/tmp/banco.bkp

docker exec -it application_db_1 bash
```
You will also need to update your application.properties to point to the database in the container:

```sh
spring.datasource.url=jdbc:postgresql://db:5432/bolsa
```
