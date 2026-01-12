package com.example.managinghomedevicesapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private final List<CardItem> items;
    private final OnDeviceToggleListener listener;
    public CardAdapter(List<CardItem> items, OnDeviceToggleListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CardItem item = items.get(position);
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());

        //Remove the last active onCheckedChangeListener
        holder.switchMaterial.setOnCheckedChangeListener(null);
        holder.switchMaterial.setChecked(item.getIsEnabled());
        holder.switchMaterial.setText(item.getIsEnabled() ? "ON" : "OFF");

        // Listen for switch changes
        holder.switchMaterial.setOnCheckedChangeListener((buttonView, isChecked) -> {

            holder.switchMaterial.setText(item.getIsEnabled() ? "ON" : "OFF");
            // Immediately revert UI
            holder.switchMaterial.setChecked(item.getIsEnabled());

            // Notify activity
            listener.onDeviceToggled(item);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView description;

        SwitchMaterial switchMaterial;


        CardViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.cardTitle);
            description = itemView.findViewById(R.id.cardDescription);
            switchMaterial = itemView.findViewById(R.id.switchNotifications);


        }
    }
}
