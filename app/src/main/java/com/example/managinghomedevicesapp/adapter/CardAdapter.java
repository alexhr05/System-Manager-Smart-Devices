package com.example.managinghomedevicesapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managinghomedevicesapp.CardItem;
import com.example.managinghomedevicesapp.R;
import com.example.managinghomedevicesapp.listener.OnDeviceToggleListener;
import com.example.managinghomedevicesapp.listener.OnItemClickListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private final List<CardItem> items;
    private final OnDeviceToggleListener listener;
    private final OnItemClickListener itemClickListener;

    public CardAdapter(List<CardItem> items, OnDeviceToggleListener listener, OnItemClickListener itemClickListener) {
        this.items = items;
        this.listener = listener;
        this.itemClickListener = itemClickListener;
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
        if(item.getStatus() == true){
            if(item.getIsEnabled() == true){
                holder.description.setText("Last Change:\nON from:\n"+minutesToTime(item.getLastActivation()));
            }else if(item.getIsEnabled() == false){
                holder.description.setText("Last Change:\nOFF from:\n"+minutesToTime(item.getLastActivation()));
            }

        }else{
            holder.description.setText("OFFLINE");
        }


        //Remove the last active onCheckedChangeListener
        holder.switchMaterial.setOnCheckedChangeListener(null);
        holder.switchMaterial.setChecked(item.getIsEnabled());
        holder.switchMaterial.setText(item.getIsEnabled() ? "On" : "Off");

        changeBackgroundColor(holder,item);

        // Listen for switch changes
        holder.switchMaterial.setOnCheckedChangeListener((buttonView, isChecked) -> {
            holder.switchMaterial.setChecked(item.getIsEnabled());
            item.setStatus(item.getStatus());
            holder.switchMaterial.setText(item.getIsEnabled() ? "On" : "Off");

            // Notify activity
            listener.onDeviceToggled(item);
        });

        holder.btnShortInterval.setOnClickListener(v -> {
            int minutes = Integer.parseInt(v.getTag().toString());
            listener.onTurnOnForTime(item,minutes);

        });

        holder.btnLongInterval.setOnClickListener(v -> {
            int minutes = Integer.parseInt(v.getTag().toString());
            listener.onTurnOnForTime(item,minutes);
        });

        holder.itemView.setOnClickListener(v ->
                itemClickListener.onItemClick(item)
        );

    }


    private void changeBackgroundColor(CardViewHolder holder, CardItem item){
        if(item.getStatus()){
            holder.materialCardView.setCardBackgroundColor(holder.itemView.getContext().getColor(R.color.card_online));

        }else{
            holder.materialCardView.setCardBackgroundColor(holder.itemView.getContext().getColor(R.color.card_offline));
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView description;

        SwitchMaterial switchMaterial;
        MaterialButton btnShortInterval;
        MaterialButton btnLongInterval;
        MaterialCardView materialCardView;
        CardViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.cardTitle);
            description = itemView.findViewById(R.id.cardLastActive);
            switchMaterial = itemView.findViewById(R.id.switchNotifications);
            btnShortInterval = itemView.findViewById(R.id.btnShortInterval);
            btnLongInterval = itemView.findViewById(R.id.btnLongInterval);
            materialCardView = itemView.findViewById(R.id.CardView);
        }
    }

    private String minutesToTime(int totalMinutes) {
        if(totalMinutes == -1){
            return "---";
        }
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;

        // % - start of placeholder
        // 0 - if number is short, add 0 infront
        // 2 - minimum 2 digits wide
        // d - value is integer
        String res = "";
        if(totalMinutes < 60){
            res = totalMinutes + " mins ago";
        }else{

            if(hours > 24){
                res = String.format("%02dd %02dh ago", hours/24,hours%24);
            }else{
                res = String.format("%02dh ago", hours);
            }
        }

        return res;
    }


}
