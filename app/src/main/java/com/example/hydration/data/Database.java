package com.example.hydration.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private List<HydrationEntry> entries = new ArrayList<>();
    private int goal = 2000;

    private transient Path path;

    public static Database load(Path path) throws IOException {
        Database database = new Database();
        if (Files.exists(path)) {
            String raw = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
            Gson gson = new Gson();
            database = gson.fromJson(JsonParser.parseString(raw), Database.class);
        }
        database.path = path;
        return database;
    }

    public void save() throws IOException {
        Gson gson = new Gson();
        Files.write(path, gson.toJson(this).getBytes(StandardCharsets.UTF_8));
    }

    public List<HydrationEntry> getEntries() {
        return entries;
    }

    public int getGoal() {
        return goal;
    }
}
