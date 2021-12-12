package com.example.powerpuff_game;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Leader implements Comparable<Leader>, Serializable {
    private int id = 0;
    private String name = "";
    private String date = " 0";
    private int score = 0;
    private double lat;
    private double lng;
    private boolean withSensors;

    public Leader() {
    }

    public Leader(String name, int score, String date, boolean withSensors) {
        this.name += name;
        this.score = score;
        this.date = date;
        this.withSensors = withSensors;
    }

    public Leader(String name, int score, double lat, double lng,boolean withSensors) {
        this.name = name;
        this.score = score;
        this.lat = lat;
        this.lng = lng;
        this.withSensors = withSensors;
        setDate();
    }

    public int getId() {
        return id;
    }

    public Leader setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Leader setName(String name) {
        this.name = name;
        return this;
    }

    public String getDate() {
        return date;
    }

    public Leader setDate(String date) {
        this.date = date;
        return this;
    }

    public int getScore() {
        return score;
    }

    public Leader setScore(int score) {
        this.score = score;
        return this;
    }

    public double getLat() {
        return lat;
    }

    public Leader setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLng() {
        return lng;
    }

    public Leader setLng(double lng) {
        this.lng = lng;
        return this;
    }

    private void setDate() {
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        date = df.format(Calendar.getInstance().getTime());
    }

    public String isWithSensors() {
        if (withSensors)
            return "Yes";
        return "No";
    }
    public boolean getWithSensors() {
        return withSensors;
    }

    public Leader setWithSensors(boolean withSensors) {
        this.withSensors = withSensors;
        return this;
    }

    @Override
    public int compareTo(Leader leader) {
        if (this.score == leader.score)
            return 0;
        else if (this.score > leader.score)
            return -1;
        else
            return 1;
    }

    @Override
    public String toString() {
        return name +
                "   Score: " + score +
                "   " + date;
    }
}
