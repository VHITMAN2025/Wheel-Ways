package com.example.navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CyclesAdapter extends RecyclerView.Adapter<CyclesAdapter.ViewHolder> {
    private final List<Cycle> cyclesList;

    public CyclesAdapter(List<Cycle> cyclesList) {
        this.cyclesList = cyclesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cycle, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cycle cycle = cyclesList.get(position);
        // Bind cycle data to views in the item layout
        holder.cycleNumberTextView.setText(cycle.getCycleNumber());
        holder.cycleColorTextView.setText(cycle.getCycleColor());
        holder.locationTextView.setText(cycle.getLocation());
    }

    @Override
    public int getItemCount() {
        return cyclesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cycleNumberTextView, cycleColorTextView, locationTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cycleNumberTextView = itemView.findViewById(R.id.cycleNumberTextView);
            cycleColorTextView = itemView.findViewById(R.id.cycleColorTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
        }
    }
}
