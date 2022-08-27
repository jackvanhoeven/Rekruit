package com.example.rekruit.model;

public class VerificationRequest {

    private String verificationID,applicantID, verificationStatus;

    public VerificationRequest(){

    }

    public VerificationRequest(String verificationID, String applicantID, String verificationStatus) {
        this.verificationID = verificationID;
        this.applicantID = applicantID;
        this.verificationStatus = verificationStatus;
    }

    public String getVerificationID() {
        return verificationID;
    }

    public void setVerificationID(String verificationID) {
        this.verificationID = verificationID;
    }

    public String getApplicantID() {
        return applicantID;
    }

    public void setApplicantID(String applicantID) {
        this.applicantID = applicantID;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }
}
