package com.market_dashboard.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Internal model — maps CoinGecko's snake_case JSON fields into Java.
 * NOT returned directly to the frontend. CryptoService maps this into CryptoDTO.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CryptoSummary {

    private String id;
    private String symbol;
    private String name;
    private String image;

    @JsonProperty("current_price")
    private Double currentPrice;

    @JsonProperty("market_cap")
    private Long marketCap;

    @JsonProperty("market_cap_rank")
    private Integer marketCapRank;

    @JsonProperty("price_change_percentage_24h")
    private Double priceChangePercentage24h;

    @JsonProperty("price_change_24h")
    private Double priceChange24h;

    @JsonProperty("total_volume")
    private Long totalVolume;

    @JsonProperty("high_24h")
    private Double high24h;

    @JsonProperty("low_24h")
    private Double low24h;

    @JsonProperty("circulating_supply")
    private Double circulatingSupply;

    @JsonProperty("ath")
    private Double allTimeHigh;

    @JsonProperty("ath_change_percentage")
    private Double athChangePercentage;
}