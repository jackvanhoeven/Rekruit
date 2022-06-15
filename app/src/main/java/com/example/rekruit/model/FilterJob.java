package com.example.rekruit.model;

import android.widget.Filter;

import com.example.rekruit.adapter.MyAdapter;

import java.util.ArrayList;

public class FilterJob  extends Filter {
    private String employerName, jobDesc, jobTitle, jobType, salary, jobID, employerLoc,jobCategory,employerID,jobReq;

    private MyAdapter adapter;
    private ArrayList<Job> filterJobList,filterList;

    public FilterJob(MyAdapter adapter, ArrayList<Job> filterJobList,ArrayList<Job> filterList) {
        this.employerName = employerName;
        this.jobDesc = jobDesc;
        this.jobTitle = jobTitle;
        this.jobType = jobType;
        this.salary = salary;
        this.jobID = jobID;
        this.employerLoc = employerLoc;
        this.jobCategory = jobCategory;
        this.employerID = employerID;
        this.jobReq = jobReq;
        this.adapter = adapter;
        this.filterJobList = filterJobList;

    }

    public FilterJob(MyAdapter adapter, ArrayList<Job> filterList) {
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public String getEmployerLoc() {
        return employerLoc;
    }

    public void setEmployerLoc(String employerLoc) {
        this.employerLoc = employerLoc;
    }

    public String getJobCategory() {
        return jobCategory;
    }

    public void setJobCategory(String jobCategory) {
        this.jobCategory = jobCategory;
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

    public MyAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(MyAdapter adapter) {
        this.adapter = adapter;
    }

    public ArrayList<Job> getFilterJobList() {
        return filterJobList;
    }
    public ArrayList<Job> getFilterList() {
        return filterList;
    }

    public void setFilterJobList(ArrayList<Job> filterJobList) {
        this.filterJobList = filterJobList;
    }
    public void setFilterList(ArrayList<Job> filterList) {
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults results = new FilterResults();
        ArrayList<Job> filterList = new ArrayList<>();
        //validate
        if(charSequence != null && charSequence.length()>0){
            //search filled not empty, searching something, perform search
            //change to uppercase
            charSequence = charSequence.toString().toLowerCase();
            //STORE our filtered list
            ArrayList<Job>filteredModels = new ArrayList<>();
            for (int i=0; i<filterList.size();i++){
                //check, search by title and category
                if(filterList.get(i).getJobTitle().toLowerCase().contains(charSequence.toString().toLowerCase())||
                        filterList.get(i).getJobCategory().toLowerCase().contains(charSequence.toString().toLowerCase())){

                    //add filtered data to list
                    filteredModels.add(filterList.get(i));
                }
            }
            results.count = filteredModels.size();
            results.values = filteredModels;
        }
        else{
            //search filled not empty,not searching,return something, return original/all/compelete
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults results) {

     filterList.addAll((ArrayList) results.values);
        //refresh com.example.rekruit.adapter
        adapter.notifyDataSetChanged();

    }


}
