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

public class SavedJobRVAdapter extends RecyclerView.Adapter<SavedJobRVAdapter.ViewHolder> {

    private ArrayList<Job> savedJobList;
    private Context context;
    private ItemClickListener mClickListener;


    // creating constructor for our com.example.rekruit.adapter class
    public SavedJobRVAdapter(ArrayList<Job> savedJobList, Context context) {
        this.savedJobList = savedJobList;
        this.context = context;
    }

    @NonNull
    @Override
    public SavedJobRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new SavedJobRVAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.saved_job_card_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SavedJobRVAdapter.ViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        Job job = savedJobList.get(position);

        holder.tvJobTitle.setText(job.getJobTitle());
        holder.tvEmployerName.setText(job.getEmployerName());
        holder.tvJobType.setText(job.getJobType());
        holder.tvSalary.setText(job.getSalary());
        holder.tvEmployerLoc.setText(job.getEmployerLoc());



    }

    @Override
    public int getItemCount() {
        // returning the size of our array list.
        return savedJobList.size();
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // creating variables for our text views.


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


    // allows clicks events to be caught
    public void setClickListener(SavedJobRVAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // convenience method for getting data at click position
    public Job getItem(int id) {
        return savedJobList.get(id);
    }
}
