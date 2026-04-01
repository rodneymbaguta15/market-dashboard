package com.market_dashboard.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Value("${cors.allowed.origins}")
    private String allowedOrigins;    //Injects configuration properties for CORS allowed origins and cache TTL values for cryptocurrencies and stocks from application.properties file

    @Value("${cache.crypto.ttl}")
    private int cryptoCacheTtl;     //Injects configuration properties for CORS allowed origins and cache TTL values for cryptocurrencies from application.properties file

    @Value("${cache.stocks.ttl}")
    private int stocksCacheTtl;   //Injects configuration properties for CORS allowed origins and cache TTL values for stocks from application.properties file

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();       //Provides a RestTemplate bean for making HTTP requests to external APIs, such as CoinGecko and Alpha Vantage
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET", "OPTIONS")
                .allowedHeaders("*");
    }

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager("cryptoTop", "cryptoDetail", "stockQuote", "stockSearch");
        manager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(60, TimeUnit.SECONDS)    //data expires 60 seconds after being written to cache
                .maximumSize(200));  //limit memory usage by keeping only the most recent 200 entries in cache
        return manager;
    }
}