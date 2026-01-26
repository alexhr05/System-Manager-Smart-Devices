package com.example.managinghomedevicesapp;

public interface OnDeviceToggleListener {
    void onDeviceToggled(CardItem item);
    void onTurnOnForTime(CardItem item, int minutes);
}
