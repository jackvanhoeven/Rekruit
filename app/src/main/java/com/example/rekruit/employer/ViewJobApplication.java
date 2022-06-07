package com.example.rekruit.employer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.rekruit.R;
import com.example.rekruit.model.Application;
import com.example.rekruit.model.Job;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import adapter.EmployerViewJobPostedAdapter;
import adapter.ViewApplicationRVAdapter;

public class ViewJobApplication extends AppCompatActivity implements ViewApplicationRVAdapter.ItemClickListener {

    private RecyclerView viewApplicationRV;
    private ArrayList<Application> viewApplicationList;
    private ViewApplicationRVAdapter viewApplicationRVAdapter;
    private FirebaseFirestore db;
    ProgressBar loadingPB;

    private String jobID,employerID,applicantID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_job_application);


        loadingPB = findViewById(R.id.idProgressBar);
        viewApplicationRV = findViewById(R.id.viewApplicationRV);

        viewApplicationList = new ArrayList<>();
        viewApplicationRV.setHasFixedSize(true);
        viewApplicationRV.setLayoutManager(new LinearLayoutManager(this));

        viewApplicationRVAdapter = new ViewApplicationRVAdapter(viewApplicationList,this);
        viewApplicationRVAdapter.setClickListener(this);

        viewApplicationRV.setAdapter(viewApplicationRVAdapter);

        // initializing our variable for firebase
        // firestore and getting its instance.
        db = FirebaseFirestore.getInstance();


        Intent intent = getIntent();
        jobID = intent.getStringExtra("jobID");

        // below line is use to get the data from Firebase Firestore.
        // previously we were saving data on a reference of Courses
        // now we will be getting the data from the same reference.
        db.collection("application")
                .whereEqualTo("employerID", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereEqualTo("jobID",jobID)
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
                                Application c = d.toObject(Application.class);

                                // and we will pass this object class
                                // inside our arraylist which we have
                                // created for recycler view.
                                viewApplicationList.add(c);
                            }
                            // after adding the data to recycler view.
                            // we are calling recycler view notifuDataSetChanged
                            // method to notify that data has been changed in recycler view.
                            viewApplicationRVAdapter.notifyDataSetChanged();
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
        String applicationID = viewApplicationRVAdapter.getItem(position).getApplicationID();
        Intent intent = new Intent(this, ViewApplicantDetail.class);
        intent.putExtra("applicationID",applicationID);
        startActivity(intent);
    }
}