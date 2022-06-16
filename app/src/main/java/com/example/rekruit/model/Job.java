package com.example.rekruit.model;

public class Job {

    private String employerName, city, state,jobDesc, jobTitle, jobType, salary, jobID, employerLoc,jobCategory,employerID,jobReq;

    public Job() {
    }


    public Job(String jobID, String employerName, String jobDesc, String jobTitle,String jobType,String salary,String employerLoc, String EmployerID, String jobReq,String state,String city) {
        this.jobID = jobID;
        this.employerName = employerName;
        this.jobDesc = jobDesc;
        this.jobTitle = jobTitle;
        this.jobType = jobType;
        this.salary = salary;
        this.employerLoc = employerLoc;
        this.jobCategory = jobCategory;
        this.jobReq = jobReq;
        this.employerID = employerID;
        this.state = state;
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEmployerID() {
        return employerID;
    }

    public void setEmployerID(String employerID) {
        this.employerID = employerID;
    }

    public String getJobReq() {
        return jobReq;
    }

    public void setJobReq(String jobReq) {
        this.jobReq = jobReq;
    }

    public String getJobCategory() {
        return jobCategory;
    }

    public void setJobCategory(String jobCategory) {
        this.jobCategory = jobCategory;
    }

    public String getEmployerLoc() {
        return employerLoc;
    }

    public void setEmployerLoc(String employerLoc) {
        this.employerLoc = employerLoc;
    }

    public String getJobID() {
        return jobID;
    }
    public String getEmployerName() {
        return employerName;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getJobType() {
        return jobType;
    }

    public String getSalary() {
        return salary;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }
}
