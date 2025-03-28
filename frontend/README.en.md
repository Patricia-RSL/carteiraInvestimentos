# Investment Portfolio Tracker

This project aims to help investors track the profitability of their investment portfolios, focusing especially on purchase operations. This project does not consider profitability when the investor is short on an asset, meaning when they have sold assets they do not yet own and then repurchased them. The system cross-references data on the investor's purchase and sale history with stock closing prices to calculate the accumulated return.

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 16.2.16.

## Features

The project has the following expected features:

1. **Daily stock visualization**:
   - Allows obtaining all stocks and their respective accumulated returns in R$ and % for a given period (start date and end date).
   - **Status**: Implemented ✅

2. **Portfolio summary visualization**:
   - Displays the accumulated return in R$ and % of the investment portfolio for a given period (start date and end date).
   - **Status**: Implemented ✅

3. **Individual stock return visualization**:
   - It is possible to filter to display the accumulated return in R$ and % of a specific list of stocks for a specific period.
   - **Status**: Implemented ✅
  
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

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

## Code scaffolding

Run `yarn build` to build the project. The generated files will be stored in the dist/ directory.
