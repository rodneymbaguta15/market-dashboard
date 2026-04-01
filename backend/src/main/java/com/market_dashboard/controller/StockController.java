package com.market_dashboard.controller;

import com.market_dashboard.model.ApiResponse;
import com.market_dashboard.model.StockQuote;
import com.market_dashboard.model.StockSearchResult;
import com.market_dashboard.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    /**
     * GET /api/stocks/quote/AAPL
     * Returns a real-time global quote for the given stock symbol.
     */
    @GetMapping("/quote/{symbol}")
    public ResponseEntity<ApiResponse<StockQuote>> getQuote(
            @PathVariable String symbol) {

        return ResponseEntity.ok(stockService.getQuote(symbol.toUpperCase()));
    }

    /**
     * example: GET /api/stocks/search?q=apple
     * Searches for stock symbols matching the query keyword.
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<StockSearchResult>>> search(
            @RequestParam("q") String query) {

        if (query == null || query.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(stockService.searchSymbols(query));
    }
}
