package com.example.openmarket.controller;

import android.app.DatePickerDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.*;

import com.example.openmarket.model.PriceRecord;

import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.openmarket.model.Commodity;
import com.example.openmarket.R;
import com.example.openmarket.fdata.FakeRepo;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AddPriceActivity extends AppCompatActivity {
    private Spinner spinnerCommodity;
    private TextView textUnit;
    private TextView editPriceError;
    private EditText editPrice;
    private TextView editDateError;
    private EditText editDate;
    private Button saveButton;
    private LocalDate date;
    private double price;
    private String selectedCommodityName;


    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        setContentView(R.layout.activity_price_entry);

        // Get the commodity name passed from the previous activity
        selectedCommodityName = getIntent().getStringExtra("COMMODITY_NAME");

        initViews();
        resetErrors();
        setupSpinnerCommodity();
        setUpSaveButton();
    }

    private void initViews() {
        spinnerCommodity = findViewById(R.id.spinner_commodity);
        textUnit = findViewById(R.id.text_unit);
        editDateError = findViewById(R.id.edit_date_error);
        editDate = findViewById(R.id.text_date);
        editPriceError = findViewById(R.id.edit_price_error);
        editPrice = findViewById(R.id.edit_price);
        saveButton = findViewById(R.id.button_save);

        date = LocalDate.now();
        editDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

        editDate.setOnClickListener(v -> showDatePicker());
    }

    private void setupSpinnerCommodity() {
        List<Commodity> commodities = FakeRepo.getCommodities();

        ArrayAdapter<Commodity> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                commodities
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCommodity.setAdapter(adapter);

        // Pre-select the commodity if one was passed
        if (selectedCommodityName != null) {
            for (int i = 0; i < commodities.size(); i++) {
                if (commodities.get(i).getName().equals(selectedCommodityName)) {
                    spinnerCommodity.setSelection(i);
                    break;
                }
            }
        }

        spinnerCommodity.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Commodity selected = (Commodity) parent.getItemAtPosition(position);
                        textUnit.setText(String.format("%s%s", getString(R.string.unit), selected.getUnit().getAbbreviation()));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        textUnit.setText(R.string.unit_e_g_per_kg);
                    }
                }
        );
    }

    private  void setUpSaveButton() {
        saveButton.setOnClickListener(e -> {
            resetErrors();

            if (!isValidPrice()) return;

            if (editDate.getText().toString().isEmpty()) {
                date = LocalDate.now();
                editDate.setText(date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            }

            Commodity commodity = (Commodity) spinnerCommodity.getSelectedItem();

            PriceRecord priceRecord = new PriceRecord(commodity, price, date);

            FakeRepo.addPrice(priceRecord);

            editPrice.setText("");
            editDate.setText(date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

            Toast.makeText(this, "Price successfully saved", Toast.LENGTH_SHORT).show();

            // Close activity and return to commodity list
            finish();
        });
    }

    private void showDatePicker() {
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        int month = currentDate.getMonthValue() - 1;
        int day = currentDate.getDayOfMonth();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    date = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay);
                    editDate.setText(date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void resetErrors() {
        editDateError.setVisibility(View.GONE);
        editDate.setBackgroundResource(R.drawable.edit_text_background);

        editPriceError.setVisibility((View.GONE));
        editPrice.setBackgroundResource(R.drawable.edit_text_background);
    }

    private boolean isValidPrice() {
        String input = editPrice.getText().toString().trim();

        if (input.isEmpty()) {
            editPriceError.setText(R.string.edit_price_error);
            editPriceError.setVisibility(View.VISIBLE);
            editPrice.setBackgroundResource(R.drawable.edit_text_error_backgroud);
            editPrice.requestFocus();
            return false;
        }

        try {
            price = Double.parseDouble(input);

            if (price <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            editPriceError.setText((price <= 0 ? "Price must be greater than 0.00" : "Invalid number entered"));
            editPriceError.setVisibility(View.VISIBLE);
            editPrice.setBackgroundResource(R.drawable.edit_text_error_backgroud);
            editPrice.requestFocus();
            return false;
        }
        return true;
    }
}