package com.example.openmarket.model;

import java.time.LocalDate;
public class PriceRecord {
    private final Commodity commodity;
    private final double price;
    private final LocalDate lastUpdated;

    public PriceRecord(Commodity commodity, double price, LocalDate lastUpdated) {
        this.commodity = commodity;
        this.price = price;
        this.lastUpdated = lastUpdated;
    }

    public Commodity getCommodity() { return commodity; }

    public double getPrice() { return price; }

    public LocalDate getLastUpdated() { return lastUpdated; }
}
