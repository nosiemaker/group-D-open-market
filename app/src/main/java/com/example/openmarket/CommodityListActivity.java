package com.example.openmarket;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CommodityListActivity extends AppCompatActivity {

    RecyclerView recyclerCommodities;
    CommodityAdapter adapter;
    List<Commodity> commodityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_list);

        recyclerCommodities = findViewById(R.id.recyclerCommodities);
        recyclerCommodities.setLayoutManager(new LinearLayoutManager(this));

        // Dummy data (for testing)
        commodityList = new ArrayList<>();
        commodityList.add(new Commodity("Maize", "2025-09-30", "K150", "+5%", R.drawable.ic_launcher_foreground));
        commodityList.add(new Commodity("Tomatoes", "2025-09-29", "K80", "-3%", R.drawable.ic_launcher_foreground));
        commodityList.add(new Commodity("Rice", "2025-09-28", "K200", "+2%", R.drawable.ic_launcher_foreground));

        adapter = new CommodityAdapter(this, commodityList);
        recyclerCommodities.setAdapter(adapter);
    }
}
