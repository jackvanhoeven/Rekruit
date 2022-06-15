package com.example.rekruit.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekruit.R;
import com.example.rekruit.applicant.ApplicantJobDescriptionPage;
import com.example.rekruit.model.Job;

import java.util.ArrayList;

public class FilterJobAdapter extends RecyclerView.Adapter<FilterJobAdapter.MyViewHolder>{

private Context context;
        ArrayList<Job> filterJobList;



public FilterJobAdapter(Context context, ArrayList<Job> filterJobList) {
        this.context = context;
        this.filterJobList = filterJobList;

        }

    @NonNull
    @Override
    public FilterJobAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.filter_job_card_view,parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterJobAdapter.MyViewHolder holder, int position) {

        Job job  = filterJobList.get(position);
        final String jobID = job.getJobID();

        holder.jobTitle.setText(job.getJobTitle());
        holder.jobType.setText(job.getJobType());
        holder.salary.setText(job.getSalary());
        holder.employerName.setText(job.getEmployerName());
        holder.employerLoc.setText(job.getEmployerLoc());



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ApplicantJobDescriptionPage.class);
                intent.putExtra("jobID",jobID);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return filterJobList.size();
    }

    public void clear() {
        int size = filterJobList.size();
        filterJobList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitle, jobDesc, jobType, salary,employerName,employerLoc,jobCategory;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            jobTitle = itemView.findViewById(R.id.jobTitleTV);
            jobType = itemView.findViewById(R.id.jobTypeTV);
            salary = itemView.findViewById(R.id.salaryTV);
            employerName = itemView.findViewById(R.id.companyNameTV);
            employerLoc = itemView.findViewById(R.id.locationTV);
            jobCategory = itemView.findViewById(R.id.tvJobCategory);

        }
    }
}
