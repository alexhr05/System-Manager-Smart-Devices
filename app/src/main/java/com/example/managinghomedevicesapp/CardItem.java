package com.example.managinghomedevicesapp;

public class CardItem {
    private int id;
    private String title;
    private String ip;

    //ON
    //OFF
    private boolean isEnabled = false;

    // 0 - offline
    // 1 - online
    private boolean status;

    private String place;

    public CardItem(int id, String title, String ip, boolean isEnabled, boolean status, String place) {
        this.id = id;
        this.title = title;
        this.ip = ip;
        this.isEnabled = isEnabled;
        this.status = status;
        this.place = place;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public void setIsEnabled(boolean value) {
        this.isEnabled = value;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    public void setPlace(String place) {
        this.place = place;
    }
    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }

    public String getIp() {
        return ip;
    }

    public boolean getIsEnabled() {
        return isEnabled;
    }
    public boolean getStatus() {
        return status;
    }
    public String getPlace() {
        return place;
    }

}