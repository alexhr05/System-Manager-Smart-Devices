package com.example.managinghomedevicesapp.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managinghomedevicesapp.ActivityLogItem;
import com.example.managinghomedevicesapp.CardItem;
import com.example.managinghomedevicesapp.R;
import com.example.managinghomedevicesapp.listener.OnDeviceToggleListener;
import com.example.managinghomedevicesapp.listener.OnItemClickListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.List;

public class ActivityLogAdapter extends RecyclerView.Adapter<ActivityLogAdapter.ViewHolder> {

    private List<ActivityLogItem> items;

    public ActivityLogAdapter(List<ActivityLogItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_activity_log, parent, false);
        return new ViewHolder(view);
    }
    // Change activity log styles and info table
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ActivityLogItem item = items.get(position);

        // STATUS badge
        if (item.getStatus()) {
            holder.tvStatus.setText("ON");
            holder.tvStatus.setBackgroundTintList(
                    ColorStateList.valueOf(Color.parseColor("#2E7D32"))); // green
        } else {
            holder.tvStatus.setText("OFF");
            holder.tvStatus.setBackgroundTintList(
                    ColorStateList.valueOf(Color.parseColor("#424242"))); // gray
        }

//        // REASON icon and text
//        holder.ivReasonIcon.setImageResource(item.getReasonIcon());
        holder.tvReason.setText(item.getReason());

        // DATE
        holder.tvDate.setText(item.getDate());

        // PASSED
        holder.tvPassed.setText(item.getMinutePassed());

//        // divider - hide on last item
//        holder.divider.setVisibility(
//                position == items.size() - 1 ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // Defining visual elements in item_acitvity_log
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStatus, tvReason, tvDate, tvPassed;
  //      ImageView ivReasonIcon;
        View divider;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStatus     = itemView.findViewById(R.id.textVStatus);
            tvReason     = itemView.findViewById(R.id.textVReason);
            tvDate       = itemView.findViewById(R.id.textVDate);
            tvPassed     = itemView.findViewById(R.id.textVPassed);
//            ivReasonIcon = itemView.findViewById(R.id.ivReasonIcon);
            divider      = itemView.findViewById(R.id.divider);
        }
    }
}

