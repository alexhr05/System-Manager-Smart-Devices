package com.example.managinghomedevicesapp.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managinghomedevicesapp.ActivityLogItem;
import com.example.managinghomedevicesapp.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityLogAdapter extends RecyclerView.Adapter<ActivityLogAdapter.ViewHolder> {

    private List<ActivityLogItem> items;

    //Translation from server response to more readable one
    private static final Map<String, String> textMap = new HashMap<>();
    static{
        textMap.put("startup","Device boot");
        textMap.put("short","Turn on\n for short interval");
        textMap.put("long","Turn on\n for long interval");
        textMap.put("timer_on","Turn on\n daily timer");
        textMap.put("timer_off","Expired Time");
        textMap.put("timer_off_web","Manual\nturn off");
    }
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
            holder.statusDot.setBackgroundResource(R.drawable.dot_on);
        } else {
            holder.tvStatus.setText("OFF");
            holder.statusDot.setBackgroundResource(R.drawable.dot_off);
        }

//        // REASON icon and text
//        holder.ivReasonIcon.setImageResource(item.getReasonIcon());
        String displayName = textMap.get(item.getReason());
        holder.tvReason.setText(displayName != null ? displayName : item.getReason() );

        // DATE
        holder.tvDate.setText(item.getDate());

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
        View statusDot;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStatus     = itemView.findViewById(R.id.textVStatus);
            statusDot    = itemView.findViewById(R.id.statusDot);
            tvReason     = itemView.findViewById(R.id.textVReason);
            tvDate       = itemView.findViewById(R.id.textVDate);
//            ivReasonIcon = itemView.findViewById(R.id.ivReasonIcon);
            divider      = itemView.findViewById(R.id.divider);
        }
    }
}

