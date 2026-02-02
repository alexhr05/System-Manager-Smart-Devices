package com.example.managinghomedevicesapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DeviceInfo extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_info);

        TextView textView = findViewById(R.id.textdeviceDetails);
        ImageView backIcon = findViewById(R.id.backIcon);

        int deviceId =getIntent().getIntExtra("device_id",-1);
        String deviceName = getIntent().getStringExtra("device_name");
        String deviceIp = getIntent().getStringExtra("device_ip");
        boolean isOnline = getIntent().getBooleanExtra("device_status", false);

        textView.setText(
                "ID: " + deviceId + "\n" +
                        "Name: " + deviceName + "\n" +
                        "IP: " + deviceIp + "\n" +
                        "Status: " + (isOnline ? "ONLINE" : "OFFLINE")
        );

        //backIcon.setOnClickListener(v-> finish());
        backIcon.setOnClickListener(v -> {
            v.startAnimation(
                    android.view.animation.AnimationUtils.loadAnimation(this, R.anim.scale_click)
            );
            finish();
        });


    }
}
