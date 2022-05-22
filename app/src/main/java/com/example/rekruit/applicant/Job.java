package com.example.rekruit.applicant;

public class Job {

    private String employerName, jobDesc, jobTitle, jobType, salary;

    public Job() {
    }

    public Job(String employerName, String jobDesc, String jobTitle, String jobType, String salary) {
        this.employerName = employerName;
        this.jobDesc = jobDesc;
        this.jobTitle = jobTitle;
        this.jobType = jobType;
        this.salary = salary;
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
