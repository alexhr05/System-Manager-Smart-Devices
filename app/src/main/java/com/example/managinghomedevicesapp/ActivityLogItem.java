package com.example.managinghomedevicesapp;

public class ActivityLogItem {
    private String date;
    private boolean status = false;
    private String reason;
    private String minutePassed;

    public ActivityLogItem(String date, boolean status, String reason, String minutePassed) {
        this.date = date;
        this.status = status;
        this.reason = reason;
        this.minutePassed = minutePassed;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    public void setMinutePassed(String minutePassed) {
        this.minutePassed = minutePassed;
    }
    public String getDate() {
        return date;
    }
    public boolean getStatus() {
        return status;
    }
    public String getReason() {
        return reason;
    }
    public String getMinutePassed() {
        return minutePassed;
    }


}
