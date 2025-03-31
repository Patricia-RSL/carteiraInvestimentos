export interface PortfolioAnalysisResponseDTO {
    portfolioAnalysisSummaryDTO: PortfolioAnalysisSummaryDTO;
    portfolioAnalysisDetail: PortfolioAnalysisDetailItemDTO[];
  }

  export interface PortfolioAnalysisDetailItemDTO {
    instrument: string;
    instrumentAmount: number;
    investedValue: number;
    marketValue: number;
    gain: number;
    percentageYield: number;
  }

  export interface PortfolioAnalysisSummaryDTO {
    instrumentAmount: number;
    investedValue: number;
    marketValue: number;
    percentageYield: number;
    totalGainLoss: number;
  }

  export interface PortfolioAnalysisRequestDTO{
    beginDate: String;
    endDate: String;
    instrumentList: String[];
  }

  export interface UserTrade {
    id: number;
    date: string;
    operationType: OperationType;
    market: string;
    maturity?: string;
    instrument: string;
    specification?: string; 
    amount: number;
    value: number; 
    totalValue: number;
  }
  
  export enum OperationType {
    c = 'COMPRA',
    v = 'VENDA'
  }
