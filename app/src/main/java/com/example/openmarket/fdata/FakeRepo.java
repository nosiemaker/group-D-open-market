package com.example.openmarket.fdata;

import com.example.openmarket.model.*;
import com.example.openmarket.utility.Unit;
import java.util.*;
import java.time.LocalDate;
import java.util.stream.Collectors;

public class FakeRepo {
    private static final List<Commodity> commodities = new ArrayList<>();
    private static final List<PriceRecord> priceRecords = new ArrayList<>();

    static {
        commodities.add(new Commodity("Maize", Unit.KILOGRAMS));
        commodities.add(new Commodity("Copper", Unit.TONNES));
        commodities.add(new Commodity("Mealie Meal", Unit.BAGS));


    }

    public static void addPrice(PriceRecord priceRecord) { priceRecords.add(priceRecord); }

    public static List<Commodity> getCommodities() { return commodities; }

    public static List<PriceRecord> getPrices() { return priceRecords; }

    public static List<PriceRecord> getPricesForCommodity(Commodity commodity) {
        return priceRecords.stream()
                .filter(pr -> pr.getCommodity().getName().equals(commodity.getName()))
                .collect(Collectors.toList());
    }
}
