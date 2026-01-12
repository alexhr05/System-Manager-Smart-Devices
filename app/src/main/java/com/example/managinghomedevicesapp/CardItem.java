package com.example.managinghomedevicesapp;

public class CardItem {
    private final int id;
    private final String title;
    private final String description;
    private boolean isEnabled = false;

    public CardItem(int id, String title, String description, boolean isEnabled) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isEnabled =isEnabled;
    }

    public void setIsEnabled(boolean value) {
         this.isEnabled = value;
    }
    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean getIsEnabled() {
        return isEnabled;
    }

}
