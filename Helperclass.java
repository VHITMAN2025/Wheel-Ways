package com.example.navigation;

public class Helperclass {
    String name;
    String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRadio() {
        return radio;
    }

    public void setRadio_button1(String radio) {
        this.radio = radio;
    }


    String password;
    String contact;
    String email;
    String radio;
;

    public Helperclass(String name, String id, String password, String contact, String email, String radio) {
        this.name = name;
        this.id = id;
        this.password = password;
        this.contact = contact;
        this.email = email;
        this.radio = radio;

    }
    public Helperclass() {
    }
}
