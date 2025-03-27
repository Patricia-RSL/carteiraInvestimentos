# Investment Portfolio Tracker

This project aims to help investors track the profitability of their investment portfolios, focusing especially on purchase operations. This project does not consider profitability when the investor is short on an asset, meaning when they have sold assets they do not yet own and then repurchased them. The system cross-references data on the investor's purchase and sale history with stock closing prices to calculate the accumulated return.

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 16.2.16.

## Features

The project has the following expected features:

1. **View of stocks on a specific day**:
   - Allows retrieving all stocks and their respective accumulated returns in R$ and % on a specific day.
   - **Status**: Partially implemented (Missing date filter)

2. **Portfolio summary view**:
   - Displays the accumulated return in R$ and % of the investment portfolio for a given period (start date and end date).
   - **Status**: Partially implemented (Missing date filter)

3. **View of an individual stock's return**:
   - Allows filtering to display the accumulated return in R$ and % of a stock on a specific day, regardless of whether a transaction occurred that day.
   - **Status**: Partially implemented (Missing action filter)
  
4. **Transaction history view**:
   - Allows viewing all the purchase and sale operations present in the database.
   - **Status**: Will be implemented in the future ⏳

5. **Add and remove transactions from history**:
   - Allows adding and removing purchase and sale operations present in the database.
   - **Status**: Will be implemented in the future ⏳

6. **Return Graphs**:
   - Displays graphs that facilitate the visualization of accumulated returns, both by stock and by the total portfolio.
   - **Status**: Will be implemented in the future ⏳

## Prerequisites
- npm
- yarn

## Setting up the Environment

Before running the application, you need to install the project dependencies. To do this, run the following command:

```sh
yarn install
```

## Development Server

To run the development server, execute the following command with Yarn:

```sh
yarn start
```

## Code scaffolding

Run `yarn generate component component-name` to generate a new component. You can also use `yarn generate directive|pipe|service|class|guard|interface|enum|module`.

## Code scaffolding

Run `yarn build` to build the project. The generated files will be stored in the dist/ directory.
