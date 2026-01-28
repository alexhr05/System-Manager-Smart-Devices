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


    public CardItem(int id, String title, String ip, boolean isEnabled, boolean status) {
        this.id = id;
        this.title = title;
        this.ip = ip;
        this.isEnabled = isEnabled;
        this.status = status;
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

}