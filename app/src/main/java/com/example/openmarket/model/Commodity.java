package com.example.openmarket.model;

import androidx.annotation.NonNull;

import com.example.openmarket.utility.Unit;
public class Commodity {
    private final String name;
    private final Unit unit;

    public Commodity (String name, Unit unit) {
        this.name = name;
        this.unit = unit;
    }

    public String getName() { return name; }

    public Unit getUnit() { return unit; }

    @NonNull
    @Override
    public String toString() { return name; }
}
