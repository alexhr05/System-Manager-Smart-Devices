package com.example.managinghomedevicesapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Log.d("API", "Calling endpoint...");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.bgroutingmap.com/8/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();


        ApiService apiService = retrofit.create(ApiService.class);

        apiService.getAllTimers("iO92iJdwuJwe8Y",
                "showAllTimers"
        ).enqueue(new retrofit2.Callback<String>(){

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful() && response.body() != null){
                    String rawText =response.body();
                    Toast.makeText(MainAct.this, "Text="+rawText, Toast.LENGTH_SHORT).show();
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
        data.add(new CardItem("Lights", "Living room lights"));
        data.add(new CardItem("Thermostat", "Set temperature"));
        data.add(new CardItem("Security", "Camera system"));
        data.add(new CardItem("Garage", "Door status"));

        CardAdapter adapter = new CardAdapter(data);
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


}
