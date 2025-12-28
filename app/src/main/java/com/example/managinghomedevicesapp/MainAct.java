package com.example.managinghomedevicesapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        List<CardItem> data = new ArrayList<>();
        data.add(new CardItem("Lights", "Living room lights"));
        data.add(new CardItem("Thermostat", "Set temperature"));
        data.add(new CardItem("Security", "Camera system"));
        data.add(new CardItem("Garage", "Door status"));

        CardAdapter adapter = new CardAdapter(data);
        recyclerView.setAdapter(adapter);


    }


}
