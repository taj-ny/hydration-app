package com.example.hydration;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hydration.data.Database;
import com.example.hydration.data.HydrationEntry;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        return true;

                    case R.id.nav_history:
                        Intent intent = new Intent(MainActivity.this, historyActivity.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });

        Database db = null;
        List<HydrationEntry> entries = null;
        try {
            Path databasePath = Paths.get(getFilesDir().getPath(), "database.json");
            Database.load(databasePath);
            db = Database.instance();
            entries = db.getEntries();
        } catch (IOException e) {
        }

        entries.clear();
        entries.add(new HydrationEntry(1748764200000L, "Woda", 500));
        entries.add(new HydrationEntry(1748764200000L, "Woda", 600));
        entries.add(new HydrationEntry(1748764200000L, "Sok jabłkowy", 700));
        entries.add(new HydrationEntry(1748584200000L, "Woda", 250));
        entries.add(new HydrationEntry(1748670600000L, "Woda", 500));
        entries.add(new HydrationEntry(1748670600000L, "Sok jabłkowy", 500));
//        entries.add(new HydrationEntry(1748674620000L, "Woda", 2490));

        List<HydrationEntry> todayEntries = db.getEntriesForDay(Calendar.getInstance());
        int hydrationAmountTotal = 0;
        for (HydrationEntry entry : todayEntries) {
            hydrationAmountTotal += entry.getAmount();
        }

        int hydrationPercentage = Math.round(hydrationAmountTotal / (float)db.getGoal() * 100);
        ((CircularProgressIndicator) findViewById(R.id.hydrationGoalIndicator)).setProgress(hydrationPercentage);
        ((TextView) findViewById(R.id.hydrationPercentageText)).setText(String.format("%d%%", hydrationPercentage));
        ((TextView) findViewById(R.id.hydrationCurrentText)).setText(String.format("%d ml", hydrationAmountTotal));
        ((TextView) findViewById(R.id.hydrationRemainingText)).setText(String.format("- %d ml", Math.max(0, db.getGoal() - hydrationAmountTotal)));

        RecyclerView recyclerView = findViewById(R.id.entries);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new HydrationEntryRecyclerViewAdapter(this, todayEntries));
        recyclerView.setNestedScrollingEnabled(false);
    }
}