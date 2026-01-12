package com.example.managinghomedevicesapp;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("houmTaimerActionApk.php")
    Call<String> getAllTimers(
            @Query("nkey") String nkey,
            @Query("action") String action
    );

    @GET("houmTaimerActionApk.php")
    Call<String> setDeviceState(
            @Query("nkey") String nkey,
            @Query("action") String action,
            @Query("timer_id") int timer_id
    );
}
