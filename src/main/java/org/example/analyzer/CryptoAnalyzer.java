package org.example.analyzer;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CryptoAnalyzer {

    private List<Crypto> cryptocurrencies;

    public CryptoAnalyzer(List<Crypto> cryptocurrencies) {
        this.cryptocurrencies = cryptocurrencies;
    }

    // 1. Calculate the total market cap of all cryptocurrencies
    public BigDecimal totalMarketCap() {
        return cryptocurrencies.stream()
                .map(Crypto::getMarketCap)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // 2. Find the top N cryptocurrencies by price
    public List<Crypto> topNCurrenciesByPrice(int n) {
        return cryptocurrencies.stream()
                .sorted(Comparator.comparing(Crypto::getPrice).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    // 3. Calculate the total trading volume
    public BigDecimal totalTradingVolume() {
        return cryptocurrencies.stream()
                .map(Crypto::getVolume)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Add more analysis methods as needed (top gainers, top losers, etc.)
}
