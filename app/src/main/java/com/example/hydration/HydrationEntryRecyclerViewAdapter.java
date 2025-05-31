package com.example.hydration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hydration.data.HydrationEntry;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HydrationEntryRecyclerViewAdapter extends RecyclerView.Adapter<HydrationEntryRecyclerViewAdapter.ViewHolder> {

    private List<HydrationEntry> data;
    private LayoutInflater inflater;

    HydrationEntryRecyclerViewAdapter(Context context, List<HydrationEntry> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.hydrationentry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HydrationEntry entry = data.get(position);

        holder.time.setText(new SimpleDateFormat("HH:mm").format(new Date(new Timestamp(entry.getTimestamp()).getTime())));
        holder.name.setText(entry.getName());
        holder.amount.setText(String.format("%d ml", entry.getAmount()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView time;
        TextView name;
        TextView amount;

        ViewHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            name = itemView.findViewById(R.id.name);
            amount = itemView.findViewById(R.id.amount);
        }
    }
}