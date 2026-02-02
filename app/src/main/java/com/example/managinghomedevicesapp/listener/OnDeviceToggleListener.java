package com.example.managinghomedevicesapp.listener;

import com.example.managinghomedevicesapp.CardItem;

public interface OnDeviceToggleListener {
    void onDeviceToggled(CardItem item);
    void onTurnOnForTime(CardItem item, int minutes);
}
