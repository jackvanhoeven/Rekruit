package com.example.rekruit.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.rekruit.R;
import com.example.rekruit.adapter.EmpVerfAdapter;
import com.example.rekruit.adapter.EmployerVerificationAdapter;
import com.example.rekruit.adapter.VerificationRequestAdapter;
import com.example.rekruit.model.EmployerVerificationRequest;
import com.example.rekruit.model.VerificationRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminManageEmployer extends AppCompatActivity implements EmpVerfAdapter.ItemClickListener{

    private RecyclerView empVerfRV;  //Employer verification RV
    private ArrayList<EmployerVerificationRequest> employerVerificationRequestList;
    private EmpVerfAdapter employerVerificationAdapterAdapter;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private String verificationID,verificationStatus,applicantID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_employer);

        db = FirebaseFirestore.getInstance();

        empVerfRV = findViewById(R.id.empVerReqRV);

       employerVerificationRequestList = new ArrayList<>();
        empVerfRV.setHasFixedSize(true);
        empVerfRV.setLayoutManager(new LinearLayoutManager(this));


        employerVerificationAdapterAdapter = new EmpVerfAdapter(employerVerificationRequestList,this);
        employerVerificationAdapterAdapter.setClickListener(this);
        empVerfRV.setAdapter(employerVerificationAdapterAdapter);

        // below line is use to get the data from Firebase Firestore.
        // previously we were saving data on a reference of Courses
        // now we will be getting the data from the same reference.
        db.collection("users")
                .whereEqualTo("userType","Employer")
                .whereEqualTo("verify","pending")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // after getting the data we are calling on success method
                        // and inside this method we are checking if the received
                        // query snapshot is empty or not.
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // if the snapshot is not empty we are
                            // hiding our progress bar and adding
                            // our data in a list.
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                // after getting this list we are passing
                                // that list to our object class.
                                EmployerVerificationRequest c = d.toObject(EmployerVerificationRequest.class);

                                // and we will pass this object class
                                // inside our arraylist which we have
                                // created for recycler view.
                                employerVerificationRequestList.add(c);
                            }
                            // after adding the data to recycler view.
                            // we are calling recycler view notifyDataSetChanged
                            // method to notify that data has been changed in recycler view.
                            employerVerificationAdapterAdapter.notifyDataSetChanged();
                        } else {
                            // if the snapshot is empty we are displaying a toast message.
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // if we do not get any data or any error we are displaying
                        // a toast message that we do not get any data
                        Toast.makeText(getApplicationContext(), "Fail to get the data.", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    @Override
    public void onItemClick(View view, int position) {
        String employerID = employerVerificationAdapterAdapter.getItem(position).getEmployerID();
        Intent intent = new Intent(this, AdminViewEmployerDetails.class);
        intent.putExtra("employerID",employerID);
        startActivity(intent);
    }
}