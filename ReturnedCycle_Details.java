package com.example.navigation;

public class ReturnedCycle_Details {
    String emp_id;

    public ReturnedCycle_Details() {
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getCycle_id() {
        return cycle_id;
    }

    public void setCycle_id(String cycle_id) {
        this.cycle_id = cycle_id;
    }

    String cycle_id;

    public ReturnedCycle_Details(String emp_id, String cycle_id) {
        this.emp_id = emp_id;
        this.cycle_id = cycle_id;
    }
}
