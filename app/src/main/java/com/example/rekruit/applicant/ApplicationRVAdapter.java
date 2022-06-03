package com.example.rekruit.applicant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekruit.R;

import java.util.ArrayList;

public class ApplicationRVAdapter extends RecyclerView.Adapter<ApplicationRVAdapter.ViewHolder> {

    private ArrayList<Application> applicationArrayList;
    private Context context;
    private ItemClickListener mClickListener;


    // creating constructor for our adapter class
    public ApplicationRVAdapter(ArrayList<Application> applicationArrayList, Context context) {
        this.applicationArrayList = applicationArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ApplicationRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ApplicationRVAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.application_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationRVAdapter.ViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        Application application = applicationArrayList.get(position);

        holder.tvJobName.setText(application.getJobID());
        holder.tvEmployerName.setText(application.getEmployerID());


    }

    @Override
    public int getItemCount() {
        // returning the size of our array list.
        return applicationArrayList.size();
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // creating variables for our text views.

        private final TextView tvJobName;
        private final TextView tvEmployerName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.

            tvEmployerName = itemView.findViewById(R.id.appCompanyTV);
            tvJobName = itemView.findViewById(R.id.appJobTitleTV);
            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }


    // allows clicks events to be caught
    void setClickListener(ApplicationRVAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // convenience method for getting data at click position
    public Application getItem(int id) {
        return applicationArrayList.get(id);
    }

}
