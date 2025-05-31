package com.example.hydration;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

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

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            db = Database.load(databasePath);
            entries = db.getEntries();
        } catch (IOException e) {
        }

        entries.clear();
        entries.add(new HydrationEntry(1748664000000L, "Woda", 250));
        entries.add(new HydrationEntry(1748671980000L, "Woda", 257));
        entries.add(new HydrationEntry(1748674620000L, "Woda", 252));
        for (int i = 0; i < 100; i++) {
            entries.add(new HydrationEntry(1748728740000L, "Woda", 249));
        }

        RecyclerView recyclerView = findViewById(R.id.entries);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new HydrationEntryRecyclerViewAdapter(this, db.getEntries()));
        recyclerView.setNestedScrollingEnabled(false);
    }
}