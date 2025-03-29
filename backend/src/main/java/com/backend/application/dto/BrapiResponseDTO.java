package com.backend.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BrapiResponseDTO {

  private List<BrapiResposeResult> results;
  private String requestedAt;
  private String took;



  @Getter
  @Setter
  public static class BrapiResposeResult {
    private String currency;
    private String marketCap;
    private String shortName;
    private String longName;
    private Double regularMarketChange;
    private Double regularMarketChangePercent;
    private String regularMarketTime;
    private Double regularMarketPrice;
    private Double regularMarketDayHigh;
    private String regularMarketDayRange;
    private Double regularMarketDayLow;
    private Long regularMarketVolume;
    private Double regularMarketPreviousClose;
    private Double regularMarketOpen;
    private String fiftyTwoWeekRange;
    private Double fiftyTwoWeekLow;
    private Double fiftyTwoWeekHigh;
    private String symbol;
    private String usedInterval;
    private String usedRange;
    private List<BrapiHistoricalData> historicalDataPrice;
    private List<String> validRanges;
    private List<String> validIntervals;
    private Double priceEarnings;
    private Double earningsPerShare;
    private String logourl;
  }

  @Getter
  @Setter
  public static class BrapiHistoricalData {
    private Long date;
    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Long volume;
    private Double adjustedClose;
  }
}
