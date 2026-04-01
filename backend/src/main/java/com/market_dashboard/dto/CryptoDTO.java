package com.market_dashboard.dto;

import com.market_dashboard.model.CryptoSummary;
import lombok.Builder;
import lombok.Data;

/**
 * This is a frontend-facing DTO. Plain camelCase fields — no @JsonProperty.
 * Jackson serializes this exactly as React expects: currentPrice, marketCap, etc.
 */
@Data
@Builder
public class CryptoDTO {

    private String id;
    private String symbol;
    private String name;
    private String image;

    private Double currentPrice;
    private Long marketCap;
    private Integer marketCapRank;
    private Double priceChangePercentage24h;
    private Double priceChange24h;
    private Long totalVolume;
    private Double high24h;
    private Double low24h;
    private Double circulatingSupply;
    private Double allTimeHigh;
    private Double athChangePercentage;

    /** Maps a CoinGecko-deserialized CryptoSummary → this DTO */
    public static CryptoDTO from(CryptoSummary s) {
        return CryptoDTO.builder()
                .id(s.getId())
                .symbol(s.getSymbol())
                .name(s.getName())
                .image(s.getImage())
                .currentPrice(s.getCurrentPrice())
                .marketCap(s.getMarketCap())
                .marketCapRank(s.getMarketCapRank())
                .priceChangePercentage24h(s.getPriceChangePercentage24h())
                .priceChange24h(s.getPriceChange24h())
                .totalVolume(s.getTotalVolume())
                .high24h(s.getHigh24h())
                .low24h(s.getLow24h())
                .circulatingSupply(s.getCirculatingSupply())
                .allTimeHigh(s.getAllTimeHigh())
                .athChangePercentage(s.getAthChangePercentage())
                .build();
    }
}
