package com.example.navigation;
public class DataClass
{
    String id;

    public DataClass() {
    }

    public String getId() {
        return id;
    }

    public String getColour() {
        return colour;
    }

    public String getLocation() {
        return location;
    }

    public String getStatus() {
        return status;
    }

    public String getCondition() {
        return condition;
    }

    String colour;
    String location;

    public DataClass(String id, String colour, String location, String status, String condition) {
        this.id = id;
        this.colour = colour;
        this.location = location;
        this.status = status;
        this.condition = condition;
    }

    String status;
    String condition;
}
