package com.example.rekruit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekruit.R;
import com.example.rekruit.model.Job;

import java.util.ArrayList;

public class EmployerViewJobPostedAdapter extends RecyclerView.Adapter<EmployerViewJobPostedAdapter.ViewHolder>{

private ArrayList<Job> postedJobList;
private Context context;
private ItemClickListener mClickListener;


    public EmployerViewJobPostedAdapter(ArrayList<Job> postedJobList, Context context){
        this.postedJobList = postedJobList;
        this.context = context;
    }


    @NonNull
    @Override
    public EmployerViewJobPostedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EmployerViewJobPostedAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.saved_job_card_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EmployerViewJobPostedAdapter.ViewHolder holder, int position) {

        // setting data to our text views from our modal class.
        Job job = postedJobList.get(position);

        holder.tvJobTitle.setText(job.getJobTitle());
        holder.tvEmployerName.setText(job.getEmployerName());
        holder.tvJobType.setText(job.getJobType());
        holder.tvSalary.setText(job.getSalary());
        holder.tvEmployerLoc.setText(job.getEmployerLoc());


    }

    @Override
    public int getItemCount() {
        return postedJobList.size();
    }

    public interface ItemClickListener{
        void onItemClick (View view, int position);
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tvEmployerName;
        private final TextView tvEmployerLoc;
        private final TextView tvJobTitle;
        private final TextView tvJobType;
        private final TextView tvSalary;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.


            tvJobTitle = itemView.findViewById(R.id.cdJobTitleTV);
            tvJobType = itemView.findViewById(R.id.cdJobTypeTV);
            tvSalary = itemView.findViewById(R.id.cdSalaryTV);
            tvEmployerName = itemView.findViewById(R.id.cdCompanyNameTV);
            tvEmployerLoc = itemView.findViewById(R.id.cdLocationTV);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    public void setClickListener(EmployerViewJobPostedAdapter.ItemClickListener itemClickListener){
        this.mClickListener = itemClickListener;
    }


    public Job getItem(int id){
        return postedJobList.get(id);
    }
}

