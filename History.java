package com.example.navigation;


import java.util.Date;

public class History {
    private String cycleId;
    private String employeeId;
    private String actual_employee_id;
    private String actual_cycle_id;
    private Date issuanceDate;

    public History() {
    }

    public String getCycleId() {
        return cycleId;
    }

    public void setCycleId(String cycleId) {
        this.cycleId = cycleId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getActual_employee_id() {
        return actual_employee_id;
    }

    public void setActual_employee_id(String actual_employee_id) {
        this.actual_employee_id = actual_employee_id;
    }

    public String getActual_cycle_id() {
        return actual_cycle_id;
    }

    public void setActual_cycle_id(String actual_cycle_id) {
        this.actual_cycle_id = actual_cycle_id;
    }

    public Date getIssuanceDate() {
        return issuanceDate;
    }

    public void setIssuanceDate(Date issuanceDate) {
        this.issuanceDate = issuanceDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public History(String cycleId, String employeeId, String actual_employee_id, String actual_cycle_id, Date issuanceDate, Date returnDate) {
        this.cycleId = cycleId;
        this.employeeId = employeeId;
        this.actual_employee_id = actual_employee_id;
        this.actual_cycle_id = actual_cycle_id;
        this.issuanceDate = issuanceDate;
        this.returnDate = returnDate;
    }

    private Date returnDate;
}