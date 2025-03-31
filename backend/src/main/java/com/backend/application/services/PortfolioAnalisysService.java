package com.backend.application.services;

import com.backend.application.converter.PortfolioAnalysisDetailItemDTOConverter;
import com.backend.application.dto.*;
import com.backend.application.entities.UserTrade;
import com.backend.application.enums.OperationType;
import com.backend.application.interfaces.PortfolioAnalysisDetailItemProjection;
import com.backend.application.repository.UserTradeRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class PortfolioAnalisysService {

    private final UserTradeRepository userTradeRepository;
    private final AnalysisCalculatorService analysisCalculatorService;

    public PortfolioAnalisysService(UserTradeRepository userTradeRepository,
                                    AnalysisCalculatorService analysisCalculatorService){
        this.userTradeRepository = userTradeRepository;
        this.analysisCalculatorService = analysisCalculatorService;
    }

    public List<UserTrade> findAllByTipoOperacaoAndInstrumentAndData(
			OperationType type, List<String> instrument, LocalDate beginDate, LocalDate endDate ){
        return userTradeRepository.findAllByOperationTypeAndInstrumentInAndDataGreaterThanEqualAndDataLessThanEqual(type, instrument, beginDate, endDate);
    }

    public List<PortfolioAnalysisDetailItemDTO> findPortfolioAnalysisDetailItems(PortfolioAnalysisRequestDTO portfolioAnalysisRequestDTO) {
        List<PortfolioAnalysisDetailItemProjection> projections = userTradeRepository.getAmountAndValueByInstrument(portfolioAnalysisRequestDTO.getInstrumentList(),
                portfolioAnalysisRequestDTO.getBeginDate(),
                portfolioAnalysisRequestDTO.getEndDate());

        return PortfolioAnalysisDetailItemDTOConverter.toDTOList(projections);
    }

    public PortfolioAnalysisResponseDTO analyzePortfolio() throws BadRequestException {
        PortfolioAnalysisRequestDTO request  = new PortfolioAnalysisRequestDTO().init();
        return analyzePortfolio(request);
    }

    public PortfolioAnalysisResponseDTO analyzePortfolio(PortfolioAnalysisRequestDTO portfolioAnalysisRequestDTO) throws BadRequestException {
        PortfolioAnalysisResponseDTO responseDTO = new PortfolioAnalysisResponseDTO();

        List<PortfolioAnalysisDetailItemDTO> portfolioAnalysisDetailItems =
            this.findPortfolioAnalysisDetailItems(portfolioAnalysisRequestDTO);
        portfolioAnalysisDetailItems = setYield(portfolioAnalysisRequestDTO, portfolioAnalysisDetailItems);
        responseDTO.setPortfolioAnalysisDetail(portfolioAnalysisDetailItems);

        responseDTO.setPortfolioAnalysisSummaryDTO(analysisCalculatorService.calculateSummary(portfolioAnalysisDetailItems));
        return responseDTO;
    }

    public List<PortfolioAnalysisDetailItemDTO> setYield(PortfolioAnalysisRequestDTO portfolioAnalysisRequestDTO, List<PortfolioAnalysisDetailItemDTO> portfolioAnalysisDetailItem) {
      return portfolioAnalysisDetailItem.stream()
        .map(item -> setYield(item, portfolioAnalysisRequestDTO))
        .toList();
    }

    public PortfolioAnalysisDetailItemDTO setYield(PortfolioAnalysisDetailItemDTO item, PortfolioAnalysisRequestDTO request) {
      BigDecimal marketValue = null;
      try {
        marketValue = analysisCalculatorService.calculateMarketPrice(item, request);
      } catch (BadRequestException e) {
        throw new IllegalArgumentException(e.getMessage());
      }
      item.setMarketValue(marketValue);

        BigDecimal percentageYield = analysisCalculatorService.calculatePercentageYield(item);
        item.setPercentageYield(percentageYield);
        return item;
    }

    public List<String> getInstrumentsFromPortfolio() {
      return userTradeRepository.findDistinctInstrument();
    }
}
