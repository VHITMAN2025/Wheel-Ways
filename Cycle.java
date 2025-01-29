package com.example.navigation;

public class Cycle {
    private String cycleNumber;
    private String cycleColor;
    private String location;

    public Cycle() {
        // Required empty public constructor for Firestore serialization
    }

    public Cycle(String cycleNumber, String cycleColor, String location) {
        this.cycleNumber = cycleNumber;
        this.cycleColor = cycleColor;
        this.location = location;
    }

    public String getCycleNumber() {
        return cycleNumber;
    }

    public void setCycleNumber(String cycleNumber) {
        this.cycleNumber = cycleNumber;
    }

    public String getCycleColor() {
        return cycleColor;
    }

    public void setCycleColor(String cycleColor) {
        this.cycleColor = cycleColor;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

