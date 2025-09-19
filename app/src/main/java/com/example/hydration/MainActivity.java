package com.example.hydration;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

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

        if (entries.isEmpty()) {
            entries.add(new HydrationEntry(1758289582000L, "Woda", 250));
            entries.add(new HydrationEntry(1758289582000L, "Woda", 500));
            entries.add(new HydrationEntry(1758289582000L, "Woda", 500));
            entries.add(new HydrationEntry(1758289582000L, "Woda", 250));
            try {
                db.save();
            } catch (IOException e){}
        }

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

    public void dodaj(View view) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.addentry, null);

        TimePicker timePicker = dialogView.findViewById(R.id.time);
        EditText amount = dialogView.findViewById(R.id.amount);
        EditText type = dialogView.findViewById(R.id.type);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Dodaj")
            .setView(dialogView)
            .setPositiveButton("Dodaj", (dialog, which) -> {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                calendar.set(Calendar.MINUTE, timePicker.getMinute());
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                Database db = Database.instance();
                db.getEntries().add(new HydrationEntry(calendar.getTimeInMillis(), type.getText().toString(), Integer.parseInt(amount.getText().toString())));
                try {
                    db.save();
                } catch (IOException e) {
                }

                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            })
            .setNegativeButton("Anuluj", null)
            .show();

    }
}