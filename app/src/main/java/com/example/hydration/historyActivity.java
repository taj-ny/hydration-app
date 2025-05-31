package com.example.hydration;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class historyActivity extends AppCompatActivity {

    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        barChart = findViewById(R.id.barChart);
        setupBarChart();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_history);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intent = new Intent(historyActivity.this, MainActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.nav_history:
                        return true;
                }
                return false;
            }
        });
    }

    private void setupBarChart() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        float[] hydrationValues = {500, 1200, 800, 1500, 600, 2000, 1000};
        ArrayList<String> labels = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -6);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM", Locale.getDefault());

        for (int i = 0; i < 7; i++) {
            entries.add(new BarEntry(i, hydrationValues[i]));
            labels.add(sdf.format(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        BarDataSet dataSet = new BarDataSet(entries, "Hydration (ml)");
        dataSet.setColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary));
        dataSet.setDrawValues(false);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f);

        barChart.setData(barData);
        barChart.setFitBars(true);
        barChart.getDescription().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);

        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(2200f);
        yAxis.setGranularity(500f);

        LimitLine limitLine = new LimitLine(2000f, "Goal: 2000 ml");
        limitLine.setLineWidth(2f);
        limitLine.setLineColor(getResources().getColor(android.R.color.holo_red_dark));
        limitLine.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        limitLine.setTextSize(12f);
        limitLine.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        yAxis.addLimitLine(limitLine);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        barChart.invalidate();
    }
}
