package com.example.managinghomedevicesapp;

import android.os.Bundle;
import android.util.Log;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



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
                    Log.d("API", "Response: " + response.body());
                    Toast.makeText(MainAct.this,
                            "SUCCESS:\n" + response.body(),
                            Toast.LENGTH_LONG).show();
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

        List<CardItem> data = new ArrayList<>();
//        data.add(new CardItem(1,"Lights", "Living room lights"));
//        data.add(new CardItem(2,"Thermostat", "Set temperature"));
//        data.add(new CardItem(3,"Security", "Camera system"));
        data.add(new CardItem(8, "Light", "Living Room", false));


        adapter = new CardAdapter(data, this::toggleDevice);


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

                boolean newState = result.equalsIgnoreCase("ON");

                //Set new state for switch
                item.setIsEnabled(newState);

                //Nofify adapter for changes in switch
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("API", "Network error", t);
            }
        });
    }
//    private void refreshDeviceState(CardItem item) {
//
//            apiService.setDeviceState(
//                    "iO92iJdwuJwe8Y",
//                    "status",
//                    item.getId()
//            ).enqueue(new Callback<String>() {
//
//                @Override
//                public void onResponse(Call<String> call, Response<String> response) {
//                    if (response.isSuccessful() && response.body() != null) {
//
//                        boolean state =
//                                response.body().trim().equalsIgnoreCase("ON");
//
//                        item.setIsEnabled(state);
//                        adapter.notifyDataSetChanged();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<String> call, Throwable t) {}
//            });
//    }

}
