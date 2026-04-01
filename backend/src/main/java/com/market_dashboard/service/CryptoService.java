package com.market_dashboard.service;

import com.market_dashboard.dto.CryptoDTO;
import com.market_dashboard.model.ApiResponse;
import com.market_dashboard.model.CryptoSummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CryptoService {

    private final RestTemplate restTemplate;

    @Value("${coingecko.base.url}")
    private String baseUrl;

    private static final String SOURCE = "CoinGecko";

    @Cacheable("cryptoTop")
    public ApiResponse<List<CryptoDTO>> getTopCoins(int limit) {
        log.debug("Fetching top {} coins from CoinGecko", limit);

        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/coins/markets")
                .queryParam("vs_currency", "usd")
                .queryParam("order", "market_cap_desc")
                .queryParam("per_page", limit)
                .queryParam("page", 1)
                .queryParam("sparkline", false)
                .queryParam("price_change_percentage", "24h")
                .toUriString();

        ResponseEntity<List<CryptoSummary>> response = restTemplate.exchange(
                url, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {}
        );

        List<CryptoDTO> coins = response.getBody()
                .stream()
                .map(CryptoDTO::from)
                .toList();

        log.debug("Fetched {} coins from CoinGecko", coins.size());
        return ApiResponse.of(coins, SOURCE);
    }

    @Cacheable(value = "cryptoDetail", key = "#coinId")
    public ApiResponse<CryptoDTO> getCoinById(String coinId) {
        log.debug("Fetching coin detail for: {}", coinId);

        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/coins/markets")
                .queryParam("vs_currency", "usd")
                .queryParam("ids", coinId)
                .queryParam("sparkline", false)
                .toUriString();

        ResponseEntity<List<CryptoSummary>> response = restTemplate.exchange(
                url, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {}
        );

        List<CryptoSummary> raw = response.getBody();
        if (raw == null || raw.isEmpty()) {
            throw new RuntimeException("Coin not found: " + coinId);
        }

        return ApiResponse.of(CryptoDTO.from(raw.get(0)), SOURCE);
    }
}