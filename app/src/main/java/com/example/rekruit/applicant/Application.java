package com.example.rekruit.applicant;

public class Application {

    private String applicantID,applicationID,applicationStatus,employerID,jobID;

    public Application() {
    }

    public Application(String applicantID, String applicationID, String applicationStatus, String employerID, String jobID) {
        this.applicantID = applicantID;
        this.applicationID = applicationID;
        this.applicationStatus = applicationStatus;
        this.employerID = employerID;
        this.jobID = jobID;
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
