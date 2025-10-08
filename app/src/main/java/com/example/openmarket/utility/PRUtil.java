package com.example.openmarket.utility;

import java.time.LocalDate;
import com.example.openmarket.model.Commodity;
import com.example.openmarket.model.PriceRecord;
import com.example.openmarket.fdata.FakeRepo;
import java.util.*;
import java.util.stream.Collectors;

public class PRUtil {
    private static final int MAX = 0;
    private static final int MIN = 1;
    private static PriceRecord getCurrentPrice(Commodity commodity) {
        List<PriceRecord> priceRecords = FakeRepo.getPricesForCommodity(commodity);
        PriceRecord currentPrice = null;
        LocalDate latestDate = priceRecords.get(0).getLastUpdated();

        for (PriceRecord pr : priceRecords) {
            if (latestDate.isAfter(pr.getLastUpdated())) {
                latestDate = pr.getLastUpdated();
                currentPrice = pr;
            }
        }

        return currentPrice;
    }

    private static List<Double> getExtremes(Commodity commodity) {
        double max, min;
        List<PriceRecord> priceRecords = FakeRepo.getPricesForCommodity(commodity);

        max = priceRecords.get(0).getPrice();
        min = priceRecords.get(0).getPrice();

        for (PriceRecord pr : priceRecords) {
            if (pr.getPrice() > max) {
                max = pr.getPrice();
            } else if (pr.getPrice() < min) {
                min = pr.getPrice();
            }
        }
        
        return Arrays.asList(max, min);
    }
    
    private static List<PriceRecord> getPricesSortedByDate(Commodity commodity) {
        List<PriceRecord> priceRecords = FakeRepo.getPricesForCommodity(commodity);

        return priceRecords.stream()
                .sorted((a, b) -> a.getLastUpdated().compareTo(b.getLastUpdated()))
                .collect(Collectors.toList());
    }

    private static double getRecentPriceChange(Commodity commodity) {
        List<PriceRecord> priceRecords = getPricesSortedByDate(commodity);

        double current = priceRecords.get(0).getPrice();
        double previous = priceRecords.get(1).getPrice();

        return ((current - previous) / previous) * 100;
    }
}
