package com.market_dashboard.service;

import com.market_dashboard.model.ApiResponse;
import com.market_dashboard.model.StockQuote;
import com.market_dashboard.model.StockSearchResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {

    private final RestTemplate restTemplate;

    @Value("${alphavantage.base.url}")
    private String baseUrl;

    @Value("${alphavantage.api.key}")
    private String apiKey;

    private static final String SOURCE = "Alpha Vantage";

    /**
     * Fetches a real-time global quote for a stock symbol (e.g. AAPL, TSLA).
     * Cached for 60 seconds.
     */
    @Cacheable(value = "stockQuote", key = "#symbol.toUpperCase()")
    public ApiResponse<StockQuote> getQuote(String symbol) {
        log.debug("Fetching stock quote for: {}", symbol);

        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("function", "GLOBAL_QUOTE")
                .queryParam("symbol", symbol.toUpperCase())
                .queryParam("apikey", apiKey)
                .toUriString();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response == null || !response.containsKey("Global Quote")) {
            throw new RuntimeException("No quote data returned for symbol: " + symbol);
        }

        @SuppressWarnings("unchecked")
        Map<String, String> quote = (Map<String, String>) response.get("Global Quote");

        if (quote == null || quote.isEmpty()) {
            throw new RuntimeException("Symbol not found or API limit reached: " + symbol);
        }

        StockQuote stockQuote = StockQuote.builder()
                .symbol(quote.get("01. symbol"))
                .price(parseDouble(quote.get("05. price")))
                .change(parseDouble(quote.get("09. change")))
                .changePercent(parseChangePercent(quote.get("10. change percent")))
                .open(parseDouble(quote.get("02. open")))
                .high(parseDouble(quote.get("03. high")))
                .low(parseDouble(quote.get("04. low")))
                .previousClose(parseDouble(quote.get("08. previous close")))
                .volume(parseLong(quote.get("06. volume")))
                .latestTradingDay(quote.get("07. latest trading day"))
                .build();

        return ApiResponse.of(stockQuote, SOURCE);
    }

    /**
     * Searches for stock symbols matching a keyword.
     * Cached for 5 minutes since search results don't change frequently.
     */
    @Cacheable(value = "stockSearch", key = "#query.toLowerCase()")
    public ApiResponse<List<StockSearchResult>> searchSymbols(String query) {
        log.debug("Searching stocks for: {}", query);

        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("function", "SYMBOL_SEARCH")
                .queryParam("keywords", query)
                .queryParam("apikey", apiKey)
                .toUriString();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        List<StockSearchResult> results = new ArrayList<>();

        if (response != null && response.containsKey("bestMatches")) {
            @SuppressWarnings("unchecked")
            List<Map<String, String>> matches = (List<Map<String, String>>) response.get("bestMatches");

            for (Map<String, String> match : matches) {
                results.add(StockSearchResult.builder()
                        .symbol(match.get("1. symbol"))
                        .name(match.get("2. name"))
                        .type(match.get("3. type"))
                        .region(match.get("4. region"))
                        .currency(match.get("8. currency"))
                        .build());
            }
        }

        return ApiResponse.of(results, SOURCE);
    }

    // --- Helpers ---

    private Double parseDouble(String value) {
        if (value == null || value.isBlank()) return null;
        try { return Double.parseDouble(value.trim()); }
        catch (NumberFormatException e) { return null; }
    }

    private Long parseLong(String value) {
        if (value == null || value.isBlank()) return null;
        try { return Long.parseLong(value.trim()); }
        catch (NumberFormatException e) { return null; }
    }

    private Double parseChangePercent(String value) {
        if (value == null || value.isBlank()) return null;
        try { return Double.parseDouble(value.replace("%", "").trim()); }
        catch (NumberFormatException e) { return null; }
    }
}
