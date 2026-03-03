package com.example.managinghomedevicesapp;

import static java.lang.Integer.parseInt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managinghomedevicesapp.adapter.CardAdapter;
import com.example.managinghomedevicesapp.api.ApiService;
import com.example.managinghomedevicesapp.listener.OnDeviceToggleListener;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainAct extends AppCompatActivity {
    private ApiService apiService;
    private CardAdapter adapter;
    //    private List<CardItem> devices;
    private List<CardItem> visibleDevices;
    private TextView textView;

    private MaterialButton btnHome;
    private MaterialButton btnVilata;
    private MaterialButton selectedButton = null;
    private RecyclerView recyclerView;
    private String textHome = "Home";
    private String textVilata = "Vilata";
    //private CardAdapter.RecyclerViewClickListner recyclerListner;
    private final Handler handler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnHome = findViewById(R.id.btnHome);
        btnVilata = findViewById(R.id.btnVilata);

        AppData.devices = new ArrayList<>();
        visibleDevices = new ArrayList<>();


        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new CardAdapter(visibleDevices, new OnDeviceToggleListener() {
            @Override
            public void onDeviceToggled(CardItem item) {
                toggleDevice(item);
            }

            @Override
            public void onTurnOnForTime(CardItem item, int minutes) {
                turnOnDeviceForTime(item, minutes);
            }
        },
                item -> {
                    Intent intent = new Intent(MainAct.this, DeviceInfo.class);
                    intent.putExtra("device_id", item.getId());
                    intent.putExtra("device_name", item.getTitle());
                    intent.putExtra("device_ip", item.getIp());
                    intent.putExtra("device_is_enabled", item.getIsEnabled());
                    intent.putExtra("device_status", item.getStatus());
                    intent.putExtra("device_mac_address", item.getMacAddress());
                    intent.putExtra("device_last_activation", item.getLastActivation());
                    intent.putExtra("device_wifi_network", item.getWifiNetwork());
                    intent.putExtra("device_signal_strength", item.getSignalStrength());

                    startActivity(intent);

                });

        recyclerView.setAdapter(adapter);

        btnHome.setText(textHome);
        btnVilata.setText(textVilata);

        btnHome.setOnClickListener(v -> {
            SelectedButton(btnHome);
            Toast.makeText(
                    MainAct.this,
                    "Clicked Home button!",
                    Toast.LENGTH_SHORT
            ).show();
        });

        btnVilata.setOnClickListener(v -> {
            SelectedButton(btnVilata);
            //showPlace(textVilata);
            Toast.makeText(
                    MainAct.this,
                    "Clicked Vilata button!",
                    Toast.LENGTH_SHORT
            ).show();
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.bgroutingmap.com/8/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);


        apiService.getAllTimers("iO92iJdwuJwe8Y",
                "showAllTimers"
        ).enqueue(new retrofit2.Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful() && response.body() != null) {
                    //Proccess data from server for devices
                    String raw = response.body().trim();
                    String[] rows = raw.split(";");
                    //Iterate through everey element in rows array
                    Log.d("Test","");
                    int id = -1;
                    String name = "";
                    String ip = "";
                    String macAddress = "";
                    String turnOnOff = "";
                    String status = "";
                    String place = "";
                    String uptime = "";
                    String wifiNetwork = "";
                    String signalStrength = "";
                    int lastActivation = -1;
                    for (String row : rows) {
                        row = row.trim();
                        if (row.isEmpty()) continue;

                        String[] parts = row.split(",");
                        if (row.length() < 11) continue;

                        for(String part: parts){
                            part = part.trim();
                            if (part.isEmpty())
                                continue;

                            if (part.startsWith("timer_id=")) {
                                id = StringTextToInt(part.replace("timer_id=",""),"timer_id=");
                                //         id = parts[0].replace("place1=", "").isEmpty() ? -1: parseInt(parts[0].replace("place1=", ""));
                            }else if(part.startsWith("timer_description=")){
                                name = part.replace("timer_description=","");
                            }else if(part.startsWith("ip_address=")){
                                ip = part.replace("ip_address=","");
                            }else if(part.startsWith("mac_address=")){
                                macAddress = part.replace("mac_address=","");
                            }else if(part.startsWith("status=")){
                                turnOnOff = part.replace("status=","");
                            }else if(part.startsWith("online_status=")){
                                status = part.replace("online_status=","");
                            }else if(part.startsWith("uptime=")){
                                uptime = part.replace("uptime=","");
                            }else if(part.startsWith("place=")){
                                place = part.replace("place=","");
                            }else if(part.startsWith("last_time_change=")){
                                lastActivation = StringTextToInt(part.replace("last_time_change=",""),"last_time_change=");
                            }else if(part.startsWith("ssid_name=")){
                                wifiNetwork = part.replace("ssid_name=","");
                            }else if(part.startsWith("rssi=")){
                                signalStrength = part.replace("rssi=","");
                            }

                        }
                        Log.d("AllDevices", "lastActivation=" + lastActivation);

                        boolean enabledOnOff = turnOnOff.equalsIgnoreCase("ON");
                        boolean statusBoolean = status.equalsIgnoreCase("ONLINE");

                        AppData.devices.add(new CardItem(id, name, ip, macAddress, enabledOnOff, statusBoolean,
                                place, lastActivation, wifiNetwork, signalStrength));

                    }
                    Log.d("AllDevices", "Devices AllSize=" + AppData.devices.size());
                    adapter.notifyDataSetChanged();



                    SelectedButton(btnHome);


                } else {
                    Toast.makeText(MainAct.this,
                            "Server error: " + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MainAct.this, "Network error=" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




    }

    private int StringTextToInt(String str, String startWithStr){
        if(str.isEmpty()){
            return -1;
        }
        str = str.replace(startWithStr,"");
        return parseInt(str);
    }
    @Override
    protected void onResume() {
        super.onResume();
        // refresh the list every time you come back to this page
        int deviceId = getIntent().getIntExtra("device_id", -1);
        String deviceName = getIntent().getStringExtra("device_name");
        String deviceIp = getIntent().getStringExtra("device_ip");
        boolean isEnabled = getIntent().getBooleanExtra("device_is_enabled", false);
        boolean isOnline = getIntent().getBooleanExtra("device_status", false);
        String deviceMacAddress = getIntent().getStringExtra("device_mac_address");

        for (CardItem device : AppData.devices) {
            if (device.getId() == deviceId) {
                device.setTitle(deviceName);
                device.setIp(deviceIp);
                device.setIsEnabled(isEnabled);
                device.setStatus(isOnline);
                device.setMacAddress(deviceMacAddress);
            }
        }
        showPlace("Home");
        adapter.notifyDataSetChanged();
    }

    private void SelectedButton(MaterialButton button) {
        // Prevent reselecting the same button
        if (button == selectedButton) return;

        // Deselect previous
        if (selectedButton != null) {
            selectedButton.setChecked(false);
            selectedButton.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
            selectedButton.setTextColor(ContextCompat.getColor(this, R.color.toggle_text_unselected));
        }

        // Select New
        button.setChecked(true);
        button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.toggle_selected)));
        button.setTextColor(ContextCompat.getColor(this, R.color.toggle_text_selected));
        selectedButton = button;

        // React to which button is selected
        if (button.getId() == R.id.btnHome) {
            onHomeSelected();
        } else if (button.getId() == R.id.btnVilata) {
            onVilataSelected();
        }
    }

    private void onHomeSelected() {
        showPlace("Home");
    }

    private void onVilataSelected() {
        showPlace("Vilata");
    }

    private void showPlace(String place) {
        visibleDevices.clear();
        for (CardItem item : AppData.devices) {
            if (item.getPlace().equalsIgnoreCase(place)) {
                visibleDevices.add(item);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void toggleDevice(CardItem item) {
        //Get the opposite value of the current switch value
        String action = item.getIsEnabled() ? "off" : "short";

        //Make request to server for changing turn on/off current state for device
        // and return the new state of device
        apiService.setDeviceState(
                "iO92iJdwuJwe8Y",
                action,
                item.getId()
        ).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String result = response.body().trim();
                boolean status = result.equalsIgnoreCase("offline");
                Log.d("API", "Response: " + result + "; status=" + status);
                if (status) {
                    item.setIsEnabled(false);
                    item.setStatus(false);


                    Toast.makeText(MainAct.this, "This devices is OFFLINE", Toast.LENGTH_SHORT).show();
                } else {
                    boolean newState = result.equalsIgnoreCase("ON");
                    item.setStatus(true);
                    //Set new state for switch
                    item.setIsEnabled(newState);
                    item.setLastActivation(0);
                }
                //Nofify adapter for changes in switch
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("API", "Network error", t);
            }
        });
    }

    private void turnOnDeviceForTime(CardItem item, int minutes) {
        //Make request to server for changing turn on/off current state for device
        // and return the new state of device
        String mins = String.valueOf(minutes);

        apiService.setDeviceState(
                "iO92iJdwuJwe8Y",
                mins,
                item.getId()
        ).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.body() == null) return;
                String result = response.body().trim();
                boolean status = result.equalsIgnoreCase("offline");
                if (status) {
                    item.setIsEnabled(false);
                    item.setStatus(false);

                    Toast.makeText(MainAct.this, "This devices is OFFLINE", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isOn = result.equalsIgnoreCase("ON");
                    item.setIsEnabled(isOn);
                    item.setStatus(true);

                    Toast.makeText(MainAct.this, "Device is turn on for " + mins + " minutes", Toast.LENGTH_SHORT).show();

//                handler.postDelayed(() -> {
//                    turnOffDevice(item);
//                }, minutes * 60 * 1000L);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("API", "Network error", t);
            }
        });
    }

    private void turnOffDevice(CardItem item) {

        apiService.setDeviceState(
                "iO92iJdwuJwe8Y",
                "off",
                item.getId()
        ).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                item.setIsEnabled(false);
                adapter.notifyDataSetChanged();

                Toast.makeText(
                        MainAct.this,
                        "Device turned OFF automatically",
                        Toast.LENGTH_SHORT
                ).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("API", "Failed to turn OFF", t);
            }
        });
    }


}
