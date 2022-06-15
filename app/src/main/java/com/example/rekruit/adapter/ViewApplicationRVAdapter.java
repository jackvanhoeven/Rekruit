package com.example.rekruit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekruit.R;
import com.example.rekruit.model.Application;

import java.util.ArrayList;

public class ViewApplicationRVAdapter extends RecyclerView.Adapter<ViewApplicationRVAdapter.ViewHolder> {

    private ArrayList<Application> viewApplicationList;
    private Context context;
    private ViewApplicationRVAdapter.ItemClickListener mClickListener;

    public ViewApplicationRVAdapter(ArrayList<Application> viewApplicationList, Context context){
        this.viewApplicationList = viewApplicationList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewApplicationRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewApplicationRVAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.view_application_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewApplicationRVAdapter.ViewHolder holder, int position) {

        // setting data to our text views from our modal class.
        Application application = viewApplicationList.get(position);

        holder.tvJobTitle.setText(application.getJobTitle());
        holder.tvApplicantName.setText(application.getApplicantName());
        holder.tvStatus.setText(application.getApplicationStatus());
        holder.tvJobID.setText(application.getJobID());



    }

    @Override
    public int getItemCount() {
        return viewApplicationList.size();
    }

    public interface ItemClickListener{
        void onItemClick (View view, int position);
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private final TextView tvJobTitle;
        private final TextView tvApplicantName;
        private final TextView tvStatus;
        private final TextView tvJobID;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.


            tvJobTitle = itemView.findViewById(R.id.VJAJobTitle);
            tvJobID = itemView.findViewById(R.id.VJAJobID);
            tvStatus = itemView.findViewById(R.id.VJAApplicationStatus);
            tvApplicantName = itemView.findViewById(R.id.VJAApplicantName);



            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    public void setClickListener(ViewApplicationRVAdapter.ItemClickListener itemClickListener){
        this.mClickListener = itemClickListener;
    }


    public Application getItem(int id){
        return viewApplicationList.get(id);
    }
}
