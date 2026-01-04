package com.example.managinghomedevicesapp;

public class CardItem {
    private final String title;
    private final String description;
    private boolean isEnabled = false;

    public CardItem(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean getIEnabled() {
        return isEnabled;
    }

}
