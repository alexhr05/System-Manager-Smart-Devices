package com.example.managinghomedevicesapp;

public class ActivityLogItem {
    private String date;
    private boolean status = false;
    private String reason;
    private long minutePassed;

    public ActivityLogItem(String date, boolean status, String reason, long minutePassed) {
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
    public void setMinutePassed(long minutePassed) {
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
    public long getMinutePassed() {
        return minutePassed;
    }


}
