package com.example.rekruit.model;

public class Application {

    private String applicantID,applicationID,applicationStatus,employerID,jobID,employerName,jobTitle,applicantName;

    public Application() {
    }

    public Application(String applicantID, String applicationID, String applicationStatus, String employerID, String jobID,String employerName, String jobTitle,String applicantName) {
        this.applicantID = applicantID;
        this.applicationID = applicationID;
        this.applicationStatus = applicationStatus;
        this.employerID = employerID;
        this.jobID = jobID;
        this.employerName = employerName;
        this.jobTitle = jobTitle;
        this.applicantName = applicantName;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getApplicantID() {
        return applicantID;
    }

    public void setApplicantID(String applicantID) {
        this.applicantID = applicantID;
    }

    public String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public String getEmployerID() {
        return employerID;
    }

    public void setEmployerID(String employerID) {
        this.employerID = employerID;
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }
}
