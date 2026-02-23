package com.example.managinghomedevicesapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.managinghomedevicesapp.api.ApiService;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class DeviceInfo extends AppCompatActivity {
    private ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_info);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.bgroutingmap.com/8/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        apiService.getAllTimers("iO92iJdwuJwe8Y",
                "showAllTimers"
        ).enqueue(new retrofit2.Callback<String>(){

            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful() && response.body() != null) {
                    //Proccess data from server for devices
                    String raw = response.body().trim();
                    String[] rows = raw.split(";");
                    //Iterate through everey element in rows array
                    for (String row : rows) {
                        row = row.trim();
                        if (row.isEmpty()) continue;

                        String[] parts = row.split(",");

                        if (parts.length < 7) continue;

                        int id = Integer.parseInt(parts[0].trim());
                        String name = parts[1].trim();
                        String ip = parts[2].trim();
                        String macAddress = parts[3].trim();
                        String turnOnOff = parts[4].trim();
                        String status = parts[5].trim();
                        String place = parts[6].trim();


                        boolean enabledOnOff = turnOnOff.equalsIgnoreCase("ON");
                        boolean statusBoolean = status.equalsIgnoreCase("ONLINE");

//                        devices.add(new CardItem(id, name, ip, macAddress, enabledOnOff, statusBoolean, place));

                    }
 //                   Log.d("AllDevices","Devices AllSize="+devices.size());
//                    adapter.notifyDataSetChanged();

//                    SelectedButton(btnHome);

                } else {
//                    Toast.makeText(MainAct.this,
//                            "Server error: " + response.code(),
//                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
       //         Toast.makeText(MainAct.this, "Network error="+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        TextView textView = findViewById(R.id.textdeviceDetails);
        ImageView backIcon = findViewById(R.id.backIcon);

        int deviceId =getIntent().getIntExtra("device_id",-1);
        String deviceName = getIntent().getStringExtra("device_name");
        String deviceIp = getIntent().getStringExtra("device_ip");
        boolean isOnline = getIntent().getBooleanExtra("device_status", false);

        textView.setText(deviceName);

        //backIcon.setOnClickListener(v-> finish());
        backIcon.setOnClickListener(v -> {
            v.startAnimation(
                    android.view.animation.AnimationUtils.loadAnimation(this, R.anim.scale_click)
            );
            finish();
        });


    }
}
