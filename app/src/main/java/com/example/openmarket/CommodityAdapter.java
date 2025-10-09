package com.example.openmarket;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.openmarket.model.Commodity;
import com.example.openmarket.model.PriceRecord;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class CommodityAdapter extends RecyclerView.Adapter<CommodityAdapter.CommodityViewHolder> {

    private Context context;
    private List<CommodityDisplayData> displayDataList;

    public CommodityAdapter(Context context, List<CommodityDisplayData> displayDataList) {
        this.context = context;
        this.displayDataList = displayDataList;
    }

    @NonNull
    @Override
    public CommodityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_commodity, parent, false);
        return new CommodityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommodityViewHolder holder, int position) {
        CommodityDisplayData data = displayDataList.get(position);

        holder.textCommodityName.setText(data.getName());
        holder.textLastUpdated.setText("Updated: " + data.getLastUpdated());
        holder.textPrice.setText(data.getFormattedPrice());
        holder.textChange.setText(data.getChangePercent());

        // Change color based on positive/negative
        if (data.getChangePercent().startsWith("-")) {
            holder.textChange.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        } else {
            holder.textChange.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        }

        holder.imageCommodity.setImageResource(data.getImageRes());

        // Set click listener for the entire card
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, com.example.openmarket.controller.AddPriceActivity.class);
            intent.putExtra("COMMODITY_NAME", data.getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return displayDataList.size();
    }

    public static class CommodityViewHolder extends RecyclerView.ViewHolder {
        TextView textCommodityName, textLastUpdated, textPrice, textChange;
        ImageView imageCommodity;

        public CommodityViewHolder(@NonNull View itemView) {
            super(itemView);
            textCommodityName = itemView.findViewById(R.id.textCommodityName);
            textLastUpdated = itemView.findViewById(R.id.textLastUpdated);
            textPrice = itemView.findViewById(R.id.textPrice);
            textChange = itemView.findViewById(R.id.textChange);
            imageCommodity = itemView.findViewById(R.id.imageCommodity);
        }
    }

    // Helper class to hold display data
    public static class CommodityDisplayData {
        private String name;
        private String lastUpdated;
        private String formattedPrice;
        private String changePercent;
        private int imageRes;

        public CommodityDisplayData(String name, String lastUpdated, String formattedPrice,
                                    String changePercent, int imageRes) {
            this.name = name;
            this.lastUpdated = lastUpdated;
            this.formattedPrice = formattedPrice;
            this.changePercent = changePercent;
            this.imageRes = imageRes;
        }

        public String getName() { return name; }
        public String getLastUpdated() { return lastUpdated; }
        public String getFormattedPrice() { return formattedPrice; }
        public String getChangePercent() { return changePercent; }
        public int getImageRes() { return imageRes; }
    }
}