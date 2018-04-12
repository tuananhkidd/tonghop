package com.kidd.baitapandroid.models;

public class CallLogContact {
    private String name;
    private String number;
    private int duration;
    private int type;
    private long date;

    public CallLogContact(String name, String number, int duration, int type, long date) {
        this.name = name;
        this.number = number;
        this.duration = duration;
        this.type = type;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
