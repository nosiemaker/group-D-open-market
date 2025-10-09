package com.example.openmarket.controller;


import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.openmarket.R;
import com.example.openmarket.model.CommodityPrice;
import com.example.openmarket.utility.DataUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TrendsActivity extends AppCompatActivity {
    private AutoCompleteTextView commodityAutoComplete;
    private LineChart priceTrendChart;
    private TextView tvMinPrice, tvMaxPrice, tvAvgPrice, commodityName;
    private MaterialToolbar toolbar;
    private View emptyView;
    private CircularProgressIndicator loadingIndicator;
    private MaterialCardView summaryCard, chartCard;
    private final DecimalFormat priceFormat = new DecimalFormat("K#,##0.00");
    private final SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private final SimpleDateFormat outputDateFormat = new SimpleDateFormat("MMM dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trends);

        initViews();
        setUpToolbar();
        setupChart();
        setupSpinner();
    }

    private void initViews() {
        toolbar = findViewById(R.id.trendsToolbar);
        commodityAutoComplete = findViewById(R.id.commodityAutoComplete);
        priceTrendChart = findViewById(R.id.priceTrendChart);
        tvAvgPrice = findViewById(R.id.tvAvgPrice);
        tvMaxPrice = findViewById(R.id.tvMaxPrice);
        tvMinPrice = findViewById(R.id.tvMinPrice);
        commodityName = findViewById(R.id.commodityName);
        emptyView = findViewById(R.id.emptyState);
        loadingIndicator = findViewById(R.id.loadingIndicator);
        summaryCard = findViewById(R.id.summaryCard);
        chartCard = findViewById(R.id.chartCard);
    }

    private void setUpToolbar(){
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setupSpinner() {
        List<String> commodities = DataUtils.getAllCommodities();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                commodities
        );
        //learn about this
        commodityAutoComplete.setAdapter(adapter);
        commodityAutoComplete.setThreshold(1);

        commodityAutoComplete.setOnItemClickListener(((parent, view, position, id) -> {
            String selectedCommodity = adapter.getItem(position);
            if(selectedCommodity != null){
                loadCommodityData(selectedCommodity);
            }
        }));
    }

    private void setupChart() {
        priceTrendChart.setDrawGridBackground(false);
        priceTrendChart.setDrawBorders(false);
        priceTrendChart.setTouchEnabled(true);
        priceTrendChart.setDragEnabled(true);
        priceTrendChart.setScaleEnabled(true);
        priceTrendChart.setPinchZoom(true);
        priceTrendChart.setExtraOffsets(10,10,10,10);

        Description description = new Description();
        description.setText("");
        priceTrendChart.setDescription(description);

        //Legend learn about this
        Legend legend = priceTrendChart.getLegend();
        legend.setEnabled(true);
        legend.setTextSize(12f);
        legend.setFormSize(12f);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);

        //X-Axis
        XAxis xAxis = priceTrendChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.parseColor("#666666"));
        xAxis.setLabelRotationAngle(-45f);

        //Left Y-Axis
        YAxis leftAxis = priceTrendChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(Color.parseColor("#E0E0E0"));
        leftAxis.setTextSize(10f);
        leftAxis.setTextColor(Color.parseColor("#666666"));
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value){
                return "K" + (int) value;
            }
        });
        // Right Y-Axis disabled
        priceTrendChart.getAxisRight().setEnabled(false);
    }

    private void loadCommodityData(String commodity) {
        showLoading();

        //Todo: create the commodityPrice model and DataUtile class
        //A short delay
        priceTrendChart.postDelayed(() -> {
            commodityName.setText(commodity);
            List<CommodityPrice> prices = DataUtils.getPricesForCommodity(commodity);

            if(prices.isEmpty()) {
                showEmptySate();
            } else {
                updateChart(commodity, prices);
                updateSummary(prices);
                showContent();

            }
        }, 300);
    }

    private void updateChart(String commodity, List<CommodityPrice> prices) {

        List<Entry> entries = new ArrayList<>();
        List<String> dataLabels = new ArrayList<>();

        for (int i = 0; i < prices.size(); i++) {
            CommodityPrice price = prices.get(i);
            entries.add(new Entry(i, price.getPrice()));
            dataLabels.add(formatDate(price.getDate()));
        }

        LineDataSet dataSet = new LineDataSet(entries, commodity);

        //Dataset Styling
        dataSet.setColor(Color.parseColor("#4CAF50"));
        dataSet.setCircleColor(Color.parseColor("#4CAF50"));
        dataSet.setCircleHoleColor(Color.WHITE);
        dataSet.setLineWidth(3f);
        dataSet.setCircleRadius(5f);
        dataSet.setCircleHoleRadius(3f);
        dataSet.setDrawValues(true);
        dataSet.setValueTextSize(9f);
        dataSet.setValueTextColor(Color.parseColor("#333333"));
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setCubicIntensity(0.2f);

        //Fill gradient
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.parseColor("#4CAF50"));
        dataSet.setFillAlpha(30);

        //Value formatter
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value){
                return "K" + (int) value;
            }
        });

        //Highlight settings
        dataSet.setHighLightColor(Color.parseColor("#FF6F00"));
        dataSet.setDrawHighlightIndicators(true);
        dataSet.setHighlightLineWidth(2f);

        LineData lineData = new LineData(dataSet);
        priceTrendChart.setData(lineData);

        //Set X-axis labels
        XAxis xAxis = priceTrendChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if(index >= 0 && index < dataLabels.size()) {
                    return dataLabels.get(index);
                }
                return "";
            }
        });

        priceTrendChart.animateXY(800, 800);
        priceTrendChart.invalidate();
    }

    private void updateSummary(List<CommodityPrice> prices) {
        if(prices.isEmpty()) {
            tvMinPrice.setText("K0.00");
            tvMaxPrice.setText("K0.00");
            tvAvgPrice.setText("K0.00");
            return;
        }

        float min = Float.MAX_VALUE;
        float max = Float.MIN_VALUE;
        float total = 0;

        for (CommodityPrice cp : prices) {
            float price = cp.getPrice();
            min = Math.min(min, price);
            max = Math.max(max, price);
            total += price;
        }

        float avg = total / prices.size();

        tvMinPrice.setText(priceFormat.format(min));
        tvMaxPrice.setText(priceFormat.format(max));
        tvAvgPrice.setText(priceFormat.format(avg));
    }

    private String formatDate(String dateStr) {
        try {
            Date date = inputDateFormat.parse(dateStr);

            if (date != null) {
                return outputDateFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    private void showLoading() {
        loadingIndicator.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        summaryCard.setVisibility(View.GONE);
        chartCard.setVisibility(View.GONE);
    }

    private void showContent() {
        loadingIndicator.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        summaryCard.setVisibility(View.VISIBLE);
        chartCard.setVisibility(View.VISIBLE);
    }

    private void showEmptySate() {
        loadingIndicator.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        summaryCard.setVisibility(View.GONE);
        chartCard.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(priceTrendChart != null) {
            priceTrendChart.clear();
        }
    }
}
