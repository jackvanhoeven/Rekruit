package com.example.rekruit.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekruit.R;
import com.example.rekruit.applicant.ApplicantJobDescriptionPage;
import com.example.rekruit.model.FilterJob;
import com.example.rekruit.model.Job;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements Filterable {

    public ArrayList<Job> filterList;
    private Context context;
    public ArrayList<Job> list;
    private FilterJob filter;


    public MyAdapter(Context context, ArrayList<Job> list) {
        this.context = context;
        this.list = list;
        this.filterList = new ArrayList<>(list);

    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.job_card_view,parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {

        Job job  = list.get(position);
        final String jobID = job.getJobID();

        holder.jobTitle.setText(job.getJobTitle());
        holder.jobType.setText(job.getJobType());
        holder.salary.setText("RM "+job.getSalary());
        holder.employerName.setText(job.getEmployerName());
        holder.city.setText(job.getCity());


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

        return list.size();
    }


    public Filter getFilter() {
        if (filter == null) {
            filter = new FilterJob(this,filterList);
        }
        return  filter;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitle, jobDesc, jobType, salary,employerName,city,jobCategory;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            jobTitle = itemView.findViewById(R.id.jobTitleTV);
            jobType = itemView.findViewById(R.id.jobTypeTV);
            salary = itemView.findViewById(R.id.salaryTV);
            employerName = itemView.findViewById(R.id.companyNameTV);
            city = itemView.findViewById(R.id.cityTV);
            jobCategory = itemView.findViewById(R.id.tvJobCategory);

        }
    }



}
