package com.example.openmarket.utility;

public enum Unit {
    DEFAULT("Unit"),
    KILOGRAMS("kg"),
    BAGS("bags"),
    TONNES("t"),
    LITRES("L");
    private final String abbreviation;

    Unit(String abbreviation) { this.abbreviation = abbreviation; }

    public String getAbbreviation() { return abbreviation; }
}
