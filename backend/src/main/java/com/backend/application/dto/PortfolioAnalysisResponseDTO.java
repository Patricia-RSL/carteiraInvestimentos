package com.backend.application.dto;

import java.util.List;

import lombok.Data;


@Data
public class PortfolioAnalysisResponseDTO {

    private PortfolioAnalysisSummaryDTO portfolioAnalysisSummaryDTO;
    private List<PortfolioAnalysisDetailItemDTO> portfolioAnalysisDetail;
}
