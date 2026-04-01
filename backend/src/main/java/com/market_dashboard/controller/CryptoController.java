package com.market_dashboard.controller;

import com.market_dashboard.dto.CryptoDTO;
import com.market_dashboard.model.ApiResponse;
import com.market_dashboard.service.CryptoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/crypto")
@RequiredArgsConstructor
public class CryptoController {

    private final CryptoService cryptoService;

    @GetMapping("/top")
    public ResponseEntity<ApiResponse<List<CryptoDTO>>> getTopCoins(
            @RequestParam(defaultValue = "10") int limit) {

        if (limit < 1 || limit > 50) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(cryptoService.getTopCoins(limit));
    }

    @GetMapping("/{coinId}")
    public ResponseEntity<ApiResponse<CryptoDTO>> getCoinById(
            @PathVariable String coinId) {

        return ResponseEntity.ok(cryptoService.getCoinById(coinId.toLowerCase()));
    }
}