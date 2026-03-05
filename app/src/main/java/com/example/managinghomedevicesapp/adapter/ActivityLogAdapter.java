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



        holder.tvDate.setText(getRelativeTiming(item.getMinutePassed()));



//        // divider - hide on last item
//        holder.divider.setVisibility(
//                position == items.size() - 1 ? View.GONE : View.VISIBLE);
    }

    public String getRelativeTiming(long minutePassed){
        if(minutePassed == -1){
            return "---";
        }
        Log.d("MINUTEPASSEDADAPTER",""+minutePassed);
//        long pastMiliseconds = minutePassed * 60 * 1000;
//        long now = System.currentTimeMillis();
//        long diff = now - pastMiliseconds;

//        long seconds = diff / 1000;
        long minutes = minutePassed;
        long hours = minutes / 60;
        long days = hours / 24;


        if(days == 0){
            if(hours == 0){
                if(minutes == 0){
                    return "Just now";
                }
                return "Today, " + minutes+"m ago";
            }
            return "Today, " + hours+"h ago";
        }else{
            long remainingHours = hours%24;
            if(remainingHours == 0){
                return days + "d ago";
            }
            return days + "d, " + remainingHours + "h ago";
        }

//        String[] wholeDate = item.getDate().split(" ");
//        String date = wholeDate[0];
//        String time = wholeDate[1];
//
//        String[] partDate = date.split("-");
//        String year = partDate[0];
//        String month = partDate[1];
//        String day = partDate[2];
//
//        Calendar today = Calendar.getInstance();
//
//        String todayYear  = today.get(Calendar.YEAR)+"";   // 2026
//        String todayMonth = (today.get(Calendar.MONTH) + 1) < 10
//                ? 0 + (today.get(Calendar.MONTH) + 1) + ""
//                : (today.get(Calendar.MONTH) + 1) + ""; // +1 because months start from 0
//        String todayDay   = today.get(Calendar.DAY_OF_MONTH) < 10
//                ? 0 + (today.get(Calendar.DAY_OF_MONTH) +"")
//                : (today.get(Calendar.DAY_OF_MONTH)+""); // 1-31

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

