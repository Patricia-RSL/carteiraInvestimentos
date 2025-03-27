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
  }