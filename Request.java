package com.example.navigation;

public class Request {

    private String id;
    private String colour;
    private String location;



    public Request() {
    }

    public Request(String id, String colour, String location) {
        this.id = id;
        this.colour = colour;
        this.location = location;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public void setLocation(String location) {
        this.location = location;
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
}


