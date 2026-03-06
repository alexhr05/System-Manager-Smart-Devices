package com.example.managinghomedevicesapp.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managinghomedevicesapp.ActivityLogItem;
import com.example.managinghomedevicesapp.CardItem;
import com.example.managinghomedevicesapp.R;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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



        holder.tvDate.setText(getRelativeTime(item.getDate()));



//        // divider - hide on last item
//        holder.divider.setVisibility(
//                position == items.size() - 1 ? View.GONE : View.VISIBLE);
    }

    private String getRelativeTime(String dateTimeString) {
        try {
            // match your server format exactly
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date pastDate     = sdf.parse(dateTimeString);
            long now          = System.currentTimeMillis();
            long diffMillis   = now - pastDate.getTime();

            long totalSeconds = diffMillis / 1000;
            long totalMinutes = totalSeconds / 60;
            long totalHours   = totalMinutes / 60;
            long totalDays    = totalHours / 24;

            long remMinutes   = totalMinutes % 60;
            long remHours     = totalHours % 24;

            if (totalDays == 0 && totalHours == 0) {
                if (totalMinutes == 0) return "Just now";
                return "Today, " + totalMinutes + "m ago";
            } else if (totalDays == 0) {
                return remMinutes == 0
                        ? "Today, " + totalHours + "h ago"
                        : "Today, " + totalHours + "h " + remMinutes + "m ago";
            } else if (totalDays == 1) {
                return remHours == 0
                        ? "Yesterday"
                        : "Yesterday, " + remHours + "h ago";
            } else {
                return remHours == 0
                        ? totalDays + "d ago"
                        : totalDays + "d " + remHours + "h ago";
            }

        } catch (Exception e) {
            Log.e("RelativeTime", "Error parsing: " + e.getMessage());
            return "Unknown";
        }
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

