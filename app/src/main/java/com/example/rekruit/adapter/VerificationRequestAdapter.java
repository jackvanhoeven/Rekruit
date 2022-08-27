package com.example.rekruit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekruit.R;
import com.example.rekruit.admin.AdminManageUser;
import com.example.rekruit.model.Job;
import com.example.rekruit.model.VerificationRequest;

import java.util.ArrayList;

public class VerificationRequestAdapter extends RecyclerView.Adapter<VerificationRequestAdapter.ViewHolder> {

    private ArrayList<VerificationRequest> verificationRequestList;
    private Context context;
    private ItemClickListener itemClickListener;

    public VerificationRequestAdapter(ArrayList<VerificationRequest> verificationRequestList, Context context) {
        this.verificationRequestList = verificationRequestList;
        this.context = context;
    }

    @NonNull
    @Override
    public VerificationRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      return new VerificationRequestAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.view_user_verification,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VerificationRequestAdapter.ViewHolder holder, int position) {

        VerificationRequest verificationRequest = verificationRequestList.get(position);

       holder.verReqIDTV.setText(verificationRequest.getApplicantID());

    }

    @Override
    public int getItemCount() {
        return verificationRequestList.size();
    }

    public interface ItemClickListener {
        void onItemClick (View view, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView verReqIDTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            verReqIDTV = itemView.findViewById(R.id.verReqIDTV);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) itemClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public void setClickListener(VerificationRequestAdapter.ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public VerificationRequest getItem(int id){
        return verificationRequestList.get(id);
    }
}
