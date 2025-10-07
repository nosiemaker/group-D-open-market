package com.example.openmarket;

public class Commodity {
    private String name;
    private String lastUpdated;
    private String price;
    private String changePercent;
    private int imageRes;

    public Commodity(String name, String lastUpdated, String price, String changePercent, int imageRes) {
        this.name = name;
        this.lastUpdated = lastUpdated;
        this.price = price;
        this.changePercent = changePercent;
        this.imageRes = imageRes;
    }

    public String getName() { return name; }
    public String getLastUpdated() { return lastUpdated; }
    public String getPrice() { return price; }
    public String getChangePercent() { return changePercent; }
    public int getImageRes() { return imageRes; }
}

