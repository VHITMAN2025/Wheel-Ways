package com.example.navigation;

public class RequestHelperClass {
    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getCycle_color() {
        return cycle_color;
    }

    public void setCycle_color(String cycle_color) {
        this.cycle_color = cycle_color;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    String emp_id;

    public RequestHelperClass(String emp_id, String cycle_color, String location) {
        this.emp_id = emp_id;
        this.cycle_color = cycle_color;
        this.location = location;
    }

    String cycle_color;
    String location;

    public RequestHelperClass() {
    }
}
