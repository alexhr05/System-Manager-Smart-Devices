package com.example.managinghomedevicesapp;

import static java.lang.Integer.parseInt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managinghomedevicesapp.adapter.ActivityLogAdapter;
import com.example.managinghomedevicesapp.adapter.CardAdapter;
import com.example.managinghomedevicesapp.api.ApiService;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
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
    private TextView textViewStatus;
    private TextView dateText;
    private TextView textViewNetworkName;
    private TextView textViewSignalStrength;
    private TextView textViewTimeManualActivation;
    private TextView textViewIP;
    private TextView textViewMAC;
    private MaterialButton timeButtonShortInt;

    private MaterialButton timeButtonLongInt;

    private MaterialButton timeButtonTurnOff;

    private RecyclerView recyclerView;
    private String deviceName = "";
    private String deviceIp = "";
    private boolean isEnabled;
    private boolean isOnline;

    private String deviceMacAddress = "";
    private int deviceLastActivation;
    private String deviceWifiNetwork = "";
    private String deviceSignalStrength = "";
    //Short interval turn on device
    private String shortInt = "";
    //Long interval turn on device
    private String longInt = "";

    private int deviceId;
    private List<ActivityLogItem> logItems;

    private ActivityLogAdapter activityLogadapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_info);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.bgroutingmap.com/8/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        logItems = new ArrayList<>();

        activityLogadapter = new ActivityLogAdapter(logItems);
        recyclerView.setAdapter(activityLogadapter);


        int counts = 5;
        apiService = retrofit.create(ApiService.class);

        textDevicePlace = findViewById(R.id.textDevicePlace);

        textViewStartTime = findViewById(R.id.textViewStartTime);
        textViewStartTime2 = findViewById(R.id.textViewStartTime2);
        textViewTimeEnd1 = findViewById(R.id.textViewTimeEnd1);
        textViewTimeEnd2 = findViewById(R.id.textViewTimeEnd2);
        textViewDuration1 = findViewById(R.id.textViewDuration1);
        textViewDuration2 = findViewById(R.id.textViewDuration2);

        textViewStatus = findViewById(R.id.textViewStatus);
        dateText = findViewById(R.id.dateText);
       // textViewTimeManualActivation = findViewById(R.id.textViewTimeManualActivation);
        textViewNetworkName = findViewById(R.id.textViewNetworkName);
        textViewSignalStrength = findViewById(R.id.textViewSignalStrength);
        textViewIP = findViewById(R.id.textViewIP);
        textViewMAC = findViewById(R.id.textViewMAC);

        timeButtonShortInt = findViewById(R.id.timeButtonShortInt);
        timeButtonLongInt = findViewById(R.id.timeButtonLongInt);
        timeButtonTurnOff = findViewById(R.id.timeButtonTurnOff);


        TextView textView = findViewById(R.id.textdeviceDetails);
        ImageView backIcon = findViewById(R.id.backIcon);

        deviceId = getIntent().getIntExtra("device_id", -1);
        deviceName = getIntent().getStringExtra("device_name");
        deviceIp = getIntent().getStringExtra("device_ip");
        isEnabled = getIntent().getBooleanExtra("device_is_enabled", false);
        isOnline = getIntent().getBooleanExtra("device_status", false);
        deviceMacAddress = getIntent().getStringExtra("device_mac_address");
        deviceLastActivation = getIntent().getIntExtra("device_last_activation", -1);
        deviceWifiNetwork = getIntent().getStringExtra("device_wifi_network");
        deviceSignalStrength = getIntent().getStringExtra("device_signal_strength");

        if (isOnline) {
            if (isEnabled) {
                textViewStatus.setText("Status: ON");
            } else {
                textViewStatus.setText("Status: OFF");
            }
        } else {
            textViewStatus.setText("Status: OFFLINE");
        }

   //     textViewTimeManualActivation.setText(minutesToTime(deviceLastActivation));
        textViewNetworkName.setText(deviceWifiNetwork);
        textViewSignalStrength.setText(deviceSignalStrength + " dBm");
        textViewIP.setText(deviceIp);

        StringBuilder temp = new StringBuilder(deviceMacAddress);

        temp.insert(2,  ':');  // "C8:C9A32F17EC"
        temp.insert(5,  ':');  // "C8:C9:A32F17EC"
        temp.insert(8,  ':');  // "C8:C9:A3:2F17EC"
        temp.insert(11, ':');  // "C8:C9:A3:2F:17EC"
        temp.insert(14, ':');  // "C8:C9:A3:2F:17:EC"
        deviceMacAddress = temp.toString();
        textViewMAC.setText(deviceMacAddress);
      //  fetchTimerConfig("daytimer", deviceMacAddress);
        fetchTimerConfig("config", deviceMacAddress);
        Log.d("IDTIMER","deviceId="+deviceId);
        getTimerLog(deviceId, counts);

        textView.setText(deviceName);

        //backIcon.setOnClickListener(v-> finish());
        backIcon.setOnClickListener(v -> {
            v.startAnimation(
                    android.view.animation.AnimationUtils.loadAnimation(this, R.anim.scale_click)
            );
            Intent intent = new Intent(DeviceInfo.this, MainAct.class);
            intent.putExtra("device_id", deviceId);
            intent.putExtra("device_name", deviceName);
            intent.putExtra("device_ip", deviceIp);
            intent.putExtra("device_is_enabled", isEnabled);
            intent.putExtra("device_status", isOnline);
            intent.putExtra("device_mac_address", deviceMacAddress);

            startActivity(intent);

            finish();
        });

        timeButtonShortInt.setOnClickListener(v -> {
            turnOnDeviceForTime(parseInt(shortInt));
        });

        timeButtonLongInt.setOnClickListener(v -> {
            turnOnDeviceForTime(parseInt(longInt));
        });
        timeButtonTurnOff.setOnClickListener(v -> {
            turnOffDevice();
        });
    }

    private void getTimerLog(int timer_id, int counts){
        String action = "activity_log";
        apiService.getTimerStateCount(
                "iO92iJdwuJwe8Y",
                action,
                timer_id,
                counts
        ).enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful() && response.body() != null) {
                    String res = response.body().trim();

                    String raw = response.body().trim();
                    String[] rows = raw.split(";");

                    //Iterate through everey element in rows array
                    String date = "";
                    String status = "";
                    String reason = "";
                    String minutesPassed = "";

                    for (String row : rows) {
                        row = row.trim();
                        if (row.isEmpty()) continue;

                        String[] parts = row.split(",");

                        for(String part: parts){
                            part = part.trim();
                            if (part.isEmpty())
                                continue;

                            if (part.startsWith("date=")) {
                                date = part.replace("date=","");
                            }else if(part.startsWith("status=")){
                                status = part.replace("status=","");
                            }else if(part.startsWith("reason=")){
                                reason = part.replace("reason=","");
                            }else if(part.startsWith("minutes_passed=")){
                                minutesPassed = part.replace("minutes_passed=","");
                            }
                        }
                        boolean statusBoolean = status.equalsIgnoreCase("ON");
                        logItems.add(new ActivityLogItem(date,statusBoolean,reason,minutesPassed));

                    }

                    activityLogadapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(DeviceInfo.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String convertDatetTo(){
        return "";
    }

    //Get Info about certain Timer
    private void fetchTimerConfig(String needParams, String deviceMacAddress) {
        apiService.getTimerConfig(
                "Fekm8Y3j6M43",
                needParams,
                "68C63AF70EF3"
        ).enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful() && response.body() != null) {
                    String res = response.body().trim();
                    Log.d("TIMER RESPONSE", "Response: " + res);
  //                  Toast.makeText(DeviceInfo.this, "" + res, Toast.LENGTH_SHORT).show();
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
        if (startIdx == -1) {
            Toast.makeText(DeviceInfo.this, "Invalid response format", Toast.LENGTH_SHORT).show();
            return;
        }
        String cleaned = response.substring(startIdx);
        switch (needParams) {
            case "daytimer":
                break;
            case "config":
                Log.d("CONFIG",""+cleaned);

                String[] parts2 = cleaned.split("@");
                String place = "";

                String uptime = "";
                String lastSeen = "";
                String onTime1 = "";
                String duration1 = "";
                String onTime2 = "";
                String duration2 = "";

                for (String part : parts2) {
                    part = part.trim();
                    if (part.isEmpty())
                        continue;
                    if (part.startsWith("place1=")) {
                        place = part.replace("place1=", "");
                    } else if (part.startsWith("shortInterval1=")) {
                        shortInt = part.replace("shortInterval1=", "");
                    } else if (part.startsWith("longInterval1=")) {
                        longInt = part.replace("longInterval1=", "");
                    } else if (part.startsWith("uptime=")) {
                        uptime = part.replace("uptime=", "");
                    } else if (part.startsWith("last_time_seen=")) {
                        lastSeen = part.replace("last_time_seen=", "");
                    }else if (part.startsWith("Timer1onTime1=")) {
                        onTime1 = part.replace("Timer1onTime1=", "");
                    } else if (part.startsWith("Timer1duration1=") && duration1.isEmpty()) {
                        duration1 = part.replace("Timer1duration1=", "");
                    } else if (part.startsWith("Timer1onTime2=")) {
                        onTime2 = part.replace("Timer1onTime2=", "");
                    } else if (part.startsWith("Timer1duration2=") && !duration2.isEmpty()) {
                        duration2 = part.replace("Timer1duration2=", "");
                    }
                }
                if (place.equalsIgnoreCase("vilata")) {
                    textDevicePlace.setText("Place: Vilata");
                } else {
                    textDevicePlace.setText("Place: Home");

                }
                timeButtonShortInt.setText(shortInt + " mins");
                timeButtonLongInt.setText(longInt + " mins");

                dateText.setText(uptime);

                Log.d("Time1+dur",""+(onTime1+duration1));
                Log.d("Time1",""+onTime1);
                Log.d("dur",""+duration1);
                setTimerViews(onTime1, duration1, textViewStartTime,  textViewTimeEnd1, textViewDuration1);
                setTimerViews(onTime2, duration2, textViewStartTime2, textViewTimeEnd2, textViewDuration2);
                //  Toast.makeText(DeviceInfo.this, "Config " + response, Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(DeviceInfo.this, "Unknown type " + response, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void setTimerViews(String onTime, String duration,
                               TextView tvStart, TextView tvEnd, TextView tvDuration) {
        if (onTime.isEmpty() || duration.isEmpty()) {
            tvStart.setText("---");
            tvEnd.setText("---");
            tvDuration.setText("---");
        } else {
            int start    = Integer.parseInt(onTime);
            int dur      = Integer.parseInt(duration);
            tvStart.setText(minutesToTime(start));
            tvEnd.setText(minutesToTime(start + dur));
            tvDuration.setText(minutesToTime(dur));
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
        String res = String.format("%02d:%02d", hours, minutes);

        return res;
    }

    private void turnOnDeviceForTime(int minutes) {
        //Make request to server for changing turn on/off current state for device
        // and return the new state of device
        String mins = String.valueOf(minutes);

        apiService.setDeviceState(
                "iO92iJdwuJwe8Y",
                mins,
                deviceId
        ).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.body() == null) return;
                String result = response.body().trim();
                boolean status = result.equalsIgnoreCase("offline");
               // Toast.makeText(DeviceInfo.this, "Device is ON for " + minutes + " mins", Toast.LENGTH_SHORT).show();
                if (status) {
                    textViewStatus.setText("Status: OFFLINE");
                    Toast.makeText(DeviceInfo.this, "This devices is OFFLINE", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isOn = result.equalsIgnoreCase("ON");
                    textViewStatus.setText("Status: " + (isOn ? "ON" : "OFF"));
                    Toast.makeText(DeviceInfo.this, "Device is turn on for " + mins+ " minutes", Toast.LENGTH_SHORT).show();

//                handler.postDelayed(() -> {
//                    turnOffDevice(item);
//                }, minutes * 60 * 1000L);

                }
                //adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("API", "Network error", t);
            }
        });
    }

    private void turnOffDevice() {

        apiService.setDeviceState(
                "iO92iJdwuJwe8Y",
                "off",
                deviceId
        ).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {


                String result = response.body().trim();
                boolean status = result.equalsIgnoreCase("offline");
                if (status) {
                    textViewStatus.setText("Status: OFFLINE");
                    Toast.makeText(DeviceInfo.this, "Device is OFFLINE", Toast.LENGTH_SHORT).show();
                } else {
                    textViewStatus.setText("Status: OFF");
                    Toast.makeText(DeviceInfo.this, "Device is OFF", Toast.LENGTH_SHORT).show();

                }
//                item.setIsEnabled(false);
//                adapter.notifyDataSetChanged();

//                Toast.makeText(
//                        MainAct.this,
//                        "Device turned OFF automatically",
//                        Toast.LENGTH_SHORT
//                ).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("API", "Failed to turn OFF", t);
            }
        });
    }

}
