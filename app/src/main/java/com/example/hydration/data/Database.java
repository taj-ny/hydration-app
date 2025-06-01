package com.example.hydration.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Database {
    private List<HydrationEntry> entries = new ArrayList<>();
    private int goal = 2000;

    private transient Path path;
    private static Database instance = new Database();

    public static void load(Path path) throws IOException {
        instance = new Database();
        if (Files.exists(path)) {
            String raw = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
            Gson gson = new Gson();
            instance = gson.fromJson(JsonParser.parseString(raw), Database.class);
        }
        instance.path = path;
    }

    public static Database instance() {
        return instance;
    }

    public void save() throws IOException {
        Gson gson = new Gson();
        Files.write(path, gson.toJson(this).getBytes(StandardCharsets.UTF_8));
    }

    public List<HydrationEntry> getEntries() {
        return entries;
    }
    public List<HydrationEntry> getEntriesForDay(Calendar calendar) {
        List<HydrationEntry> result = new ArrayList<>();
        long dayInMilliseconds = 1000 * 60 * 60 * 24;
        for (HydrationEntry entry : entries) {
            if (calendar.getTimeInMillis() / dayInMilliseconds != entry.getTimestamp() / dayInMilliseconds) {
                continue;
            }
            result.add(entry);
        }
        return result;
    }

    public int getGoal() {
        return goal;
    }
}
