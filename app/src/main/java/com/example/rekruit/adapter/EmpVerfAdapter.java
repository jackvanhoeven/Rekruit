package com.example.rekruit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekruit.R;
import com.example.rekruit.admin.AdminManageEmployer;
import com.example.rekruit.model.EmployerVerificationRequest;
import com.example.rekruit.model.VerificationRequest;

import java.util.ArrayList;

public class EmpVerfAdapter extends RecyclerView.Adapter<EmpVerfAdapter.ViewHolder> {
    private ArrayList<EmployerVerificationRequest> employerVerificationRequestList;
    private Context context;
    private ItemClickListener itemClickListener;

    public EmpVerfAdapter(ArrayList<EmployerVerificationRequest> employerVerificationRequestList, Context context) {
        this.employerVerificationRequestList = employerVerificationRequestList;
        this.context = context;
    }

    @NonNull
    @Override
    public EmpVerfAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EmpVerfAdapter.ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.view_employer_verification,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull EmpVerfAdapter.ViewHolder holder, int position) {

        EmployerVerificationRequest emVerificationRequest = employerVerificationRequestList.get(position);

        holder.empVerReqIDTV.setText(emVerificationRequest.getEmployerID());

    }

    @Override
    public int getItemCount() {
        return employerVerificationRequestList.size();
    }




    public interface ItemClickListener {
        void onItemClick (View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView empVerReqIDTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            empVerReqIDTV = itemView.findViewById(R.id.empVerReqIDTV);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(itemClickListener !=null) itemClickListener.onItemClick(view,getAdapterPosition());

        }
    }

    public void setClickListener(EmpVerfAdapter.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    public EmployerVerificationRequest getItem(int id){
        return employerVerificationRequestList.get(id);
    }
}
