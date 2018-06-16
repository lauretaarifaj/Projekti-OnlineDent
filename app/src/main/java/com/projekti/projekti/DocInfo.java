package com.projekti.projekti;

/**
 * Created by laureta on 5/13/2018.
 */

public class DocInfo {
    public String firstName,lastName,address,phone,speciality,hospital,type,url,id,email;

    public DocInfo(String firstName, String lastName, String address, String phone,String speciality, String hospital,String type,String url,String id,String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.speciality = speciality;
        this.hospital = hospital;
        this.type = type;
        this.url=url;
        this.id=id;
        this.email =email;
    }

}
