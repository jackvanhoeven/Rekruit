package com.example.rekruit.model;


public class EmployerVerificationRequest {

    private String verificationID,employerID, verificationStatus;

    public EmployerVerificationRequest(){

    }

    public EmployerVerificationRequest(String verificationID, String employerID, String verificationStatus) {
        this.verificationID = verificationID;
        this.employerID = employerID;
        this.verificationStatus = verificationStatus;
    }

    public String getVerificationID() {
        return verificationID;
    }

    public void setVerificationID(String verificationID) {
        this.verificationID = verificationID;
    }

    public String getEmployerID() {
        return employerID;
    }

    public void setEmployerID(String employerID) {
        this.employerID = employerID;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }
}
