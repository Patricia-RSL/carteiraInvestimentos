# Investment Portfolio Tracker

This project aims to help investors monitor the profitability of their investment portfolios by updating the database information through an external API.
The system cross-references data on the investor's purchase and sale history with stock closing prices to calculate the accumulated return. <!-- and display performance graphs. -->

## Repository Structure

This repository contains two main directories:

- **back-end**: The implementation of the API in JAVA that processes investment data, calculates returns, and provides the necessary data for the front-end.
- **front-end**: The user interface developed in Angular, focusing on visualizing return data <!-- and graphs-->.

Both components can be run independently, but they are part of the same repository to avoid cluttering my Git.

## Features

The project has the following expected features:

1. **Daily stock visualization**:  
   - Allows the visualization of all stocks and their respective accumulated returns in R$ and % for a given period (start date and end date).  
   - **Status**: Implemented ✅  

2. **Total portfolio return**:  
   - Calculates the accumulated return in R$ and % of the investment portfolio for a given period (start date and end date).  
   - **Status**: Implemented ✅  

3. **Individual stock return**:  
   - It is possible to filter to display the accumulated return in R$ and % of a list of stocks for a specific date range, regardless of whether an operation occurred during that period.  
   - **Status**: Implemented ✅  
   
4. **Determining gains and losses**:
   - Presents the detailed accumulated return, distinguishing stocks with gains and stocks with losses over the period.
   - **Status**: Implemented ✅

5. **Paginated view of user trade history**:  
   - **Status**: Implemented ✅

6. **Return Graphs**:
   - Displays graphs that facilitate the visualization of accumulated returns, both by stock and by the total portfolio.
   - **Status**: Will be implemented in the future ⏳

## How to Run the Project

Both the back-end and front-end have their individual `README.md` files, with instructions on how to run the project.
