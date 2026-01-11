package com.example.managinghomedevicesapp;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("status")
    Call<String> getAllTimers(
            @Query("nkey") String nkey,
            @Query("action") String action
    );
}
