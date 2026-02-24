package com.example.managinghomedevicesapp;

import static java.lang.Integer.parseInt;

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
    private TextView textViewStartTime;

    private TextView textViewStartTime2;
    private TextView textViewTimeEnd1;
    private TextView textViewTimeEnd2;

    private TextView textViewDuration1;
    private TextView textViewDuration2;
    private TextView textDevicePlace;
    //Short interval turn on device
    private String shortInt = "";
    //Long interval turn on device
    private String longInt = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_info);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.bgroutingmap.com/8/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        textDevicePlace = findViewById(R.id.textDevicePlace);

        textViewStartTime = findViewById(R.id.textViewStartTime);
        textViewStartTime2 = findViewById(R.id.textViewStartTime2);
        textViewTimeEnd1 = findViewById(R.id.textViewTimeEnd1);
        textViewTimeEnd2 = findViewById(R.id.textViewTimeEnd2);
        textViewDuration1 = findViewById(R.id.textViewDuration1);
        textViewDuration2 = findViewById(R.id.textViewDuration2);



        TextView textView = findViewById(R.id.textdeviceDetails);
        ImageView backIcon = findViewById(R.id.backIcon);

        int deviceId =getIntent().getIntExtra("device_id",-1);
        String deviceName = getIntent().getStringExtra("device_name");
        String deviceIp = getIntent().getStringExtra("device_ip");
        boolean isOnline = getIntent().getBooleanExtra("device_status", false);
        String deviceMacAddress = getIntent().getStringExtra("device_mac_address");

        fetchTimerConfig("daytimer", deviceMacAddress);
        fetchTimerConfig("config", deviceMacAddress);

        textView.setText(deviceName);

        //backIcon.setOnClickListener(v-> finish());
        backIcon.setOnClickListener(v -> {
            v.startAnimation(
                    android.view.animation.AnimationUtils.loadAnimation(this, R.anim.scale_click)
            );
            finish();
        });
    }

    //
    private void fetchTimerConfig(String needParams, String deviceMacAddress){
        apiService.getTimerConfig(

                "Fekm8Y3j6M43",
                needParams,
                "C8C9A32F17EC"
        ).enqueue(new retrofit2.Callback<String>(){

            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful() && response.body() != null) {
                    String res = response.body().trim();
                    Log.d("TIMER RESPONSE", "Response: " + res);
                    Toast.makeText(DeviceInfo.this, ""+res, Toast.LENGTH_SHORT).show();
                    handleTimerResponse(needParams, res);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(DeviceInfo.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void handleTimerResponse(String needParams, String response) {
        int startIdx = response.indexOf("@");
        if(startIdx == -1){
            Toast.makeText(DeviceInfo.this, "Invalid response format", Toast.LENGTH_SHORT).show();
            return;
        }
        String cleaned = response.substring(startIdx);

        switch (needParams){
            case "daytimer":
                String[] parts = response.split("@");
                String onTime1 = "";
                String duration1 = "";
                String onTime2 = "";
                String duration2 = "";

                for (String part : parts) {
                    part = part.trim();
                    if (part.isEmpty()) continue;
                    if (part.startsWith("onTime1=")) {
                        onTime1 = part.replace("onTime1=", "");
                    } else if (part.startsWith("duration=") && duration1.isEmpty()) {
                        duration1 = part.replace("duration=", "");
                    } else if (part.startsWith("onTime2=")) {
                        onTime2 = part.replace("onTime2=", "");
                    } else if (part.startsWith("duration=") && !duration1.isEmpty()) {
                        duration2 = part.replace("duration=", "");
                    }
                }


                textViewStartTime.setText(minutesToTime(parseInt(onTime1)));
                textViewStartTime2.setText(minutesToTime(parseInt(onTime2)));
                textViewTimeEnd1.setText(minutesToTime(parseInt(onTime1) + parseInt(duration1)));
                textViewTimeEnd2.setText(minutesToTime(parseInt(onTime2) + parseInt(duration2)));
                textViewDuration1.setText(minutesToTime(parseInt(duration1)));
                textViewDuration2.setText(minutesToTime(parseInt(duration2)));

                Log.d("Timer", "onTime1: " + onTime1);    // 1305
                Log.d("Timer", "duration1: " + duration1); // 0
                Log.d("Timer", "onTime2: " + onTime2);    // 1315
                Log.d("Timer", "duration2: " + duration2); // 0



                Toast.makeText(DeviceInfo.this, "Day Timer "+response, Toast.LENGTH_SHORT).show();
                break;
            case "config":
                String[] parts2 = cleaned.split("@");
                String place = "";
                String shortInt = "";
                String longInt = "";
                String uptime = "";
                String lastSeen = "";
                for(String part: parts2){
                    part = part.trim();
                    if(part.isEmpty())
                        continue;
                    if(part.startsWith("place1=")){
                        place = part.replace("place1=", "");
                    }else if(part.startsWith("shortInterval1=")){
                        shortInt = part.replace("shortInt=", "");
                    }else if(part.startsWith("longInterval1=")){
                        longInt = part.replace("longInterval1=", "");
                    }else if(part.startsWith("uptime=")){
                        uptime = part.replace("uptime=", "");
                    }else if(part.startsWith("last_time_seen=")){
                        lastSeen = part.replace("last_time_seen=", "");
                    }
                }
                if(place.equalsIgnoreCase("vilata")){
                    textDevicePlace.setText("Vilata");
                }else{
                    textDevicePlace.setText("Home");
                }




                Toast.makeText(DeviceInfo.this, "Config "+response, Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(DeviceInfo.this, "Unknown type "+response, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private String minutesToTime(int totalMinutes){
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;

        // % - start of placeholder
        // 0 - if number is short, add 0 infront
        // 2 - minimum 2 digits wide
        // d - value is integer
        String res = String.format("%02d:%02d",hours, minutes);

        return res;
    }
}
