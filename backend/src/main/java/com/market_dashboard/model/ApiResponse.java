package com.market_dashboard.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse <T>{
    private T data;
    private String source;
    private Instant cachedAt;
    private boolean fromCache;

    public static <T> ApiResponse<T> of(T data, String source) {
        return ApiResponse.<T>builder()
                .data(data)
                .source(source)
                .cachedAt(Instant.now())
                .fromCache(false)
                .build();
    }
}