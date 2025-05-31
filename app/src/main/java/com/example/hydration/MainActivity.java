package com.example.hydration;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
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