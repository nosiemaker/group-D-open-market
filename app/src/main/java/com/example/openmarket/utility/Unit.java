package com.example.openmarket.utility;

public enum Unit {
    KILOGRAMS("kg"),
    BAGS("bags"),
    TONNES("t");
    private final String abbreviation;

    Unit(String abbreviation) { this.abbreviation = abbreviation; }

    public String getAbbreviation() { return abbreviation; }
}
