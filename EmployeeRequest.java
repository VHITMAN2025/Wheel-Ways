package com.example.navigation;

public class EmployeeRequest {


    String emp_id;
    String cycle_color;
    String cycle_location;

    public EmployeeRequest() {
    }

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

    public String getCycle_location() {
        return cycle_location;
    }

    public void setCycle_location(String cycle_location) {
        this.cycle_location = cycle_location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrent_id() {
        return current_id;
    }

    public void setCurrent_id(String current_id) {
        this.current_id = current_id;
    }

    public EmployeeRequest(String emp_id, String cycle_color, String cycle_location, String status, String current_id) {
        this.emp_id = emp_id;
        this.cycle_color = cycle_color;
        this.cycle_location = cycle_location;
        this.status = status;
        this.current_id = current_id;
    }

    String status;
    String current_id;
}