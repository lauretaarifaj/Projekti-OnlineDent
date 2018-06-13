package com.projekti.projekti;

/**
 * Created by Admin on 6/8/2018.
 */

public class Appointment {
    public String patientId;
    public String doctorId;
    public String time;
    public String date;
    //public String NrPersonal;
   // public String docfirstname;
    //public String doclastname;
    public String hospital;
    public String nrPersonal;
    public String docfirstname;
    public String doclastname;


    public Appointment() {
    }

    public Appointment(String patientId, String doctorId, String time, String date, String hospital, String nrPersonal,String docfirstname,String doclastname) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.time = time;
        this.date = date;
        this.hospital = hospital;
        this.nrPersonal = nrPersonal;
        this.docfirstname=docfirstname;
        this.doclastname=doclastname;

    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getNrPersonal() {
        return nrPersonal;
    }

    public void setNrPersonal(String nrPersonal) {
        this.nrPersonal = nrPersonal;
    }

    public String getDocfirstname() {
        return docfirstname;
    }

    public void setDocfirstname(String docfirstname) {
        this.docfirstname = docfirstname;
    }

    public String getDoclastname() {
        return doclastname;
    }

    public void setDoclastname(String doclastname) {
        this.doclastname = doclastname;
    }
}
