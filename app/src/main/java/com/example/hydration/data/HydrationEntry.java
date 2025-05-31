package com.example.hydration.data;

public class HydrationEntry {
    private final long timestamp;
    private final String name;
    private final int amount;

    public HydrationEntry(long timestamp, String name, int amount) {
        this.timestamp = timestamp;
        this.name = name;
        this.amount = amount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }
}
