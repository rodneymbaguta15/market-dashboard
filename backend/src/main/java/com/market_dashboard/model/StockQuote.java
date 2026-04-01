package com.market_dashboard.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StockQuote {
    private String symbol;
    private String name;
    private Double price;
    private Double change;
    private Double changePercent;
    private Double open;
    private Double high;
    private Double low;
    private Double previousClose;
    private Long volume;
    private String latestTradingDay;
}
