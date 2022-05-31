package com.example.rekruit.applicant;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekruit.R;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Job> list;

    public MyAdapter(Context context, ArrayList<Job> list) {
        this.context = context;
        this.list = list;
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
        holder.salary.setText(job.getSalary());
        holder.employerName.setText(job.getEmployerName());
        holder.employerLoc.setText(job.getEmployerLoc());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context,ApplicantJobDescriptionPage.class);
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

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitle, jobDesc, jobType, salary,employerName,employerLoc;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            jobTitle = itemView.findViewById(R.id.jobTitleTV);
            jobType = itemView.findViewById(R.id.jobTypeTV);
            salary = itemView.findViewById(R.id.salaryTV);
            employerName = itemView.findViewById(R.id.companyNameTV);
            employerLoc = itemView.findViewById(R.id.locationTV);

        }
    }


}
