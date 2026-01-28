package com.example.managinghomedevicesapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

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
    private List<CardItem> devices;
    private TextView textView;

    private final Handler handler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        devices = new ArrayList<>();


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

                        if (parts.length < 5) continue;

                        int id = Integer.parseInt(parts[0].trim());
                        String name = parts[1].trim();
                        String ip = parts[2].trim();
                        String turnOnOff = parts[3].trim();
                        String status = parts[4].trim();

                        boolean enabledOnOff = turnOnOff.equalsIgnoreCase("ON");
                        boolean statusBoolean = status.equalsIgnoreCase("ONLINE");

                        devices.add(new CardItem(id, name, ip, enabledOnOff, statusBoolean));

                    }
                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(MainAct.this,
                            "Server error: " + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MainAct.this, "Network error="+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new CardAdapter(devices, new OnDeviceToggleListener() {
            @Override
            public void onDeviceToggled(CardItem item) {
                toggleDevice(item);
            }

            @Override
            public void onTurnOnForTime(CardItem item, int minutes) {
                turnOnDeviceForTime(item,minutes);
            }
        });


        recyclerView.setAdapter(adapter);

        MaterialButton buttonOne = findViewById(R.id.btnOptionA);
        MaterialButton buttonTwo = findViewById(R.id.btnOptionB);
        buttonOne.setText("Home");
        buttonTwo.setText("Vilata");

        buttonOne.setOnClickListener(v -> {
            Toast.makeText(
                    MainAct.this,
                    "Button clicked!",
                    Toast.LENGTH_SHORT
            ).show();
        });


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
                if(status){
                    item.setIsEnabled(false);
                    item.setStatus(false);

                    Toast.makeText(MainAct.this, "This devices is OFFLINE", Toast.LENGTH_SHORT).show();
//                    adapter.notifyDataSetChanged();
                }else{
                    boolean newState = result.equalsIgnoreCase("ON");

                    //Set new state for switch
                    item.setIsEnabled(newState);

                    //Nofify adapter for changes in switch
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("API", "Network error", t);
            }
        });
    }

    private void turnOnDeviceForTime(CardItem item, int minutes){
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
                if(status){
                    item.setIsEnabled(false);
                    item.setStatus(false);

                    Toast.makeText(MainAct.this, "This devices is OFFLINE;status="+item.getStatus(), Toast.LENGTH_SHORT).show();
                }else{
                    boolean isOn = result.equalsIgnoreCase("ON");
                    item.setIsEnabled(isOn);
                    adapter.notifyDataSetChanged();

                    Toast.makeText(MainAct.this, "Device is turn on for " + mins+ " minutes", Toast.LENGTH_SHORT).show();

//                handler.postDelayed(() -> {
//                    turnOffDevice(item);
//                }, minutes * 60 * 1000L);

                }


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
