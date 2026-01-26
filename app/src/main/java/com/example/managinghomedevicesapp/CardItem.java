package com.example.managinghomedevicesapp;

public class CardItem {
    private int id;
    private String title;
    private String ip;
    private boolean isEnabled = false;
    //private int status = 1;
    // 0 - offline
    // 1 - online
    // 2 - disabled

    public CardItem(int id, String title, String ip, boolean isEnabled) {
        this.id = id;
        this.title = title;
        this.ip = ip;
        this.isEnabled = isEnabled;
//        this.status = status;
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

//    public void setStatus(int status) {
//        this.status = status;
//    }
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
    //public int getStatus() {
//        return status;
//    }

}