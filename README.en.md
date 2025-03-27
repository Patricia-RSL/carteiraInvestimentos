# Investment Portfolio Tracker

This project aims to help investors track the profitability of their investment portfolios, focusing especially on purchase operations. This project does not consider profitability when the investor is short on a stock, meaning when they have sold stocks they do not yet own and then repurchased them. The system cross-references data on the investor's purchase and sale history with stock closing prices to calculate the accumulated return. <!-- and display performance graphs. -->

## Repository Structure

This repository contains two main directories:

- **back-end**: The implementation of the API in JAVA that processes investment data, calculates returns, and provides the necessary data for the front-end.
- **front-end**: The user interface developed in Angular, focusing on visualizing return data <!-- and graphs-->.

Both components can be run independently, but they are part of the same repository to avoid cluttering my Git.

## Features

The project has the following expected features:

1. **View of stocks on a specific day**:
   - Allows viewing all stocks and their respective accumulated returns in R$ and % on a specific day.
   - **Status**: Partially implemented 🛠️ (Missing the field to select the day in the Front-end)

2. **Total portfolio return**:
   - Calculates the accumulated return in R$ and % of the investment portfolio for a given period (start date and end date).
   - **Status**: Partially implemented 🛠️ (Missing the field to select the days in the Front-end)

3. **Return of an individual stock**:
   - Allows filtering to display the accumulated return in R$ and % of a stock on a specific day, regardless of whether a transaction occurred that day.
   - **Status**: Partially implemented 🛠️ (Missing the field to select the stock in the Front-end)

4. **Determining gains and losses**:
   - Presents the detailed accumulated return, distinguishing stocks with gains and stocks with losses over the period.
   - **Status**: Implemented ✅

5. **Return Graphs**:
   - Displays graphs that facilitate the visualization of accumulated returns, both by stock and by the total portfolio.
   - **Status**: Will be implemented in the future ⏳

## How to Run the Project

Both the back-end and front-end have their individual `README.md` files, with instructions on how to run the project.
