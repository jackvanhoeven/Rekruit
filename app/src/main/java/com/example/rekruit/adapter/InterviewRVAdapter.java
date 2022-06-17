package com.example.rekruit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekruit.R;
import com.example.rekruit.model.Interview;

import java.util.ArrayList;

public class InterviewRVAdapter  extends RecyclerView.Adapter<InterviewRVAdapter.ViewHolder>{

    private ArrayList<Interview> interviewArrayList;
    private Context context;
    private ItemClickListener mClickListener;


    // creating constructor for our com.example.rekruit.adapter class
    public InterviewRVAdapter(ArrayList<Interview> interviewArrayList, Context context) {
        this.interviewArrayList = interviewArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public InterviewRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InterviewRVAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.view_interview_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InterviewRVAdapter.ViewHolder holder, int position) {

        // setting data to our text views from our modal class.
        Interview application = interviewArrayList.get(position);

        holder.tvJobName.setText(application.getJobTitle());
        holder.tvEmployerName.setText(application.getEmployerName());
        holder.tvApplicationStatus.setText(application.getApplicationStatus());
        holder.tvApplicationID.setText(application.getApplicationID());


    }

    @Override
    public int getItemCount() {
        // returning the size of our array list.
        return interviewArrayList.size();
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // creating variables for our text views.

        private final TextView tvJobName;
        private final TextView tvEmployerName;
        private final TextView tvApplicationStatus;
        private final TextView tvApplicationID;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.

            tvEmployerName = itemView.findViewById(R.id.appCompanyTV);
            tvJobName = itemView.findViewById(R.id.appJobTitleTV);
            tvApplicationStatus = itemView.findViewById(R.id.applicantStatus);
            tvApplicationID = itemView.findViewById(R.id.applicationIDTV);

            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }


    // allows clicks events to be caught
    public void setClickListener(InterviewRVAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // convenience method for getting data at click position
    public Interview getItem(int id) {
        return interviewArrayList.get(id);
    }
}
