package com.example.managinghomedevicesapp.api;


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

    //Get info when Timer need to be turned on/off for the day
    @GET("timerGetConfigNew.php")
    Call<String> getTimerConfig(

            @Query("nkey") String nkey,
            @Query("needParams") String needParams,
            @Query("mac_address") String mac_address
    );





}
