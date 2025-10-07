package com.example.openmarket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommodityAdapter extends RecyclerView.Adapter<CommodityAdapter.CommodityViewHolder> {

    private Context context;
    private List<Commodity> commodityList;

    public CommodityAdapter(Context context, List<Commodity> commodityList) {
        this.context = context;
        this.commodityList = commodityList;
    }

    @NonNull
    @Override
    public CommodityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_commodity, parent, false);
        return new CommodityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommodityViewHolder holder, int position) {
        Commodity commodity = commodityList.get(position);
        holder.textCommodityName.setText(commodity.getName());
        holder.textLastUpdated.setText("Updated: " + commodity.getLastUpdated());
        holder.textPrice.setText(commodity.getPrice());
        holder.textChange.setText(commodity.getChangePercent());

        // Change color based on positive/negative
        if (commodity.getChangePercent().startsWith("-")) {
            holder.textChange.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        } else {
            holder.textChange.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        }

        holder.imageCommodity.setImageResource(commodity.getImageRes());
    }

    @Override
    public int getItemCount() {
        return commodityList.size();
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
}

