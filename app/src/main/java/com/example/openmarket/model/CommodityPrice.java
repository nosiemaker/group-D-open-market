package com.example.openmarket.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class CommodityPrice implements Serializable, Comparable<CommodityPrice> {
    private String date;
    private float price;
    private String commodity;
    private String location;
    private String unit;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public CommodityPrice() {
        this.unit = "per kg";
        this.location = "Market";
    }

    public CommodityPrice(String date, float price) {
        this.date = date;
        this.price = price;
        this.unit = "per kg";
        this.location = "Market";
    }

    public CommodityPrice(String date, float price, String commodity, String location, String unit) {
        this.date = date;
        this.price = price;
        this.unit = unit;
        this.location = location;
        this.commodity = commodity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }

    public String getLocation() {
        return  location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit){
        this.unit = unit;
    }

    public Date getDateAsObject() {
        try {
            return DATE_FORMAT.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getFormattedPrice() {
        return String.format(Locale.getDefault(), "K%.2f", price);
    }

    public String getFormattedDate() {
        try {
            Date dateObj = DATE_FORMAT.parse(date);

            if(dateObj != null) {
                SimpleDateFormat displayFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
                return displayFormat.format(dateObj);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    @Override
    public int compareTo(CommodityPrice others) {
        if(others == null) return 1;

        Date thisDate = this.getDateAsObject();
        Date otherDate = others.getDateAsObject();

        if(thisDate == null) return -1;
        if(otherDate == null) return  1;
        return thisDate.compareTo(otherDate);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        CommodityPrice that = (CommodityPrice) o;
        return Float.compare(that.price, price) == 0 &&
                Objects.equals(date, that.date) &&
                Objects.equals(commodity, that.commodity)&&
                Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, price, commodity, location);
    }

    @NonNull
    @Override
    public String toString() {
        return "CommodityPrice{" +
                "date='" + date + '\'' +
                ", price=" + price +
                ", commodity=" + commodity +'\'' +
                ", location='" + location + '\'' +
                ", unit='" + unit + '\'' +
                '}';
    }

    public boolean isValid() {
        if (date == null || date.trim().isEmpty()) return false;
        if(price < 0) return false;

        try {
            DATE_FORMAT.parse(date);
            return true;

        } catch (ParseException e) {}
        return false;
    }

    public float calculatePercentageChange(CommodityPrice previousPrice) {
        if(previousPrice == null || previousPrice.getPrice() == 0) {
            return 0f;
        }
        return ((this.price - previousPrice.getPrice()) / previousPrice.getPrice()) * 100f;
    }
}

