package com.samir.cfos.models;

public class RegisterModel {
    private String name;
    private String gender;
    private String address;
    private String contact;
    private  String email;
    private  String device_token;
    private String role;


//    empty constructor required
    public RegisterModel(){

    }

    public RegisterModel(String name, String gender, String address, String contact, String email, String device_token, String role) {
        this.name = name;
        this.gender = gender;
        this.address = address;
        this.contact = contact;
        this.email = email;
        this.device_token = device_token;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
