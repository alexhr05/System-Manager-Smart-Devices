package com.example.managinghomedevicesapp;

public class CardItem {
    private int id;
    private String title;
    private String ip;

    private String macAddress;
    //ON
    //OFF
    private boolean isEnabled = false;

    // 0 - offline
    // 1 - online
    private boolean status;
    private String place;
    private int lastActivation;
    private String wifiNetwork;
    private String signalStrength;

    public CardItem(int id, String title, String ip, String macAddress, boolean isEnabled, boolean status, String place,
        int lastActivation,
        String wifiNetwork,
        String signalStrength
    ) {
        this.id = id;
        this.title = title;
        this.ip = ip;
        this.macAddress = macAddress;
        this.isEnabled = isEnabled;
        this.status = status;
        this.place = place;
        this.lastActivation = lastActivation;
        this.wifiNetwork = wifiNetwork;
        this.signalStrength = signalStrength;
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

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public void setLastActivation(int lastActivation) {
        this.lastActivation = lastActivation;
    }
    public void setWifiNetwork(String wifiNetwork) {
        this.wifiNetwork = wifiNetwork;
    }
    public void setSignalStrength(String signalStrength) {
        this.signalStrength = signalStrength;
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

    public String getMacAddress() {
        return macAddress;
    }

    public int getLastActivation() {
        return lastActivation;
    }
    public String getWifiNetwork() {
        return wifiNetwork;
    }
    public String getSignalStrength() {
        return signalStrength;
    }

}