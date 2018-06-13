package com.projekti.projekti;

import java.math.BigDecimal;

/**
 * Created by Admin on 5/27/2018.
 */

public class Doctor {
    //public String firstName,lastName,username,address,phone,speciality,hospital,type;
    private String firstName;
    private String lastName;
    private String username;
    private String address;
    private String speciality;
    private String hospital;
    private String type;
    private String phone;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Doctor(String firstName, String lastName, String username, String address, String speciality, String hospital, String type, String phone, String url,String id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.address = address;
        this.speciality = speciality;
        this.hospital = hospital;
        this.type = type;
        this.phone = phone;
        this.url=url;
        this.id=id;
    }

    public Doctor() {
    }
}
