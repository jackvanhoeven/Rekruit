package com.example.rekruit.model;

public class Interview {


    private String interviewID,applicantID,applicationID,applicationStatus,employerID,jobID,employerName,jobTitle,applicantName,date;

    public Interview() {
    }



    public Interview(String applicantID, String applicationID, String applicationStatus, String employerID, String jobID, String employerName, String jobTitle, String applicantName, String date,String interviewID) {
        this.applicantID = applicantID;
        this.applicationID = applicationID;
        this.applicationStatus = applicationStatus;
        this.employerID = employerID;
        this.jobID = jobID;
        this.employerName = employerName;
        this.jobTitle = jobTitle;
        this.applicantName = applicantName;
        this.date = date;
        this.interviewID = interviewID;
    }

    public String getInterviewID() {
        return interviewID;
    }

    public void setInterviewID(String interviewID) {
        this.interviewID = interviewID;
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

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
