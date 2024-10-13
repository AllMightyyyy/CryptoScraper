package org.example.analyzer;

import java.math.BigDecimal;

public class Crypto {
    private String currencyName;
    private BigDecimal price;
    private BigDecimal change;
    private BigDecimal changePercent;
    private BigDecimal marketCap;
    private BigDecimal volume;
    private BigDecimal supply;
    private String timestamp;

    public Crypto(String currencyName, BigDecimal price, BigDecimal change, BigDecimal changePercent, BigDecimal marketCap, BigDecimal volume, BigDecimal supply, String timestamp) {
        this.currencyName = currencyName;
        this.price = price;
        this.change = change;
        this.changePercent = changePercent;
        this.marketCap = marketCap;
        this.volume = volume;
        this.supply = supply;
        this.timestamp = timestamp;
    }

    // Getters for each field
    public String getCurrencyName() { return currencyName; }
    public BigDecimal getPrice() { return price; }
    public BigDecimal getChange() { return change; }
    public BigDecimal getChangePercent() { return changePercent; }
    public BigDecimal getMarketCap() { return marketCap; }
    public BigDecimal getVolume() { return volume; }
    public BigDecimal getSupply() { return supply; }
    public String getTimestamp() { return timestamp; }
}
