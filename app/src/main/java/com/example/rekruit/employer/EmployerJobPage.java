package com.example.rekruit.employer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.rekruit.R;
import com.example.rekruit.model.Job;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import com.example.rekruit.adapter.EmployerViewJobPostedAdapter;

public class EmployerJobPage extends AppCompatActivity implements EmployerViewJobPostedAdapter.ItemClickListener{

    private RecyclerView jobPostedRV;
    private ArrayList<Job> jobPostedList;
    private EmployerViewJobPostedAdapter jobPostedRVAdapter;
    private FirebaseFirestore db;
    ProgressBar loadingPB;

    private String jobID,employerID,applicantID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_job_page);


        loadingPB = findViewById(R.id.idProgressBar);
        jobPostedRV = findViewById(R.id.jobPostedRV);

        jobPostedList = new ArrayList<>();
        jobPostedRV.setHasFixedSize(true);
        jobPostedRV.setLayoutManager(new LinearLayoutManager(this));

        jobPostedRVAdapter = new EmployerViewJobPostedAdapter(jobPostedList,this);
        jobPostedRVAdapter.setClickListener(this);

        jobPostedRV.setAdapter(jobPostedRVAdapter);

        // initializing our variable for firebase
        // firestore and getting its instance.
        db = FirebaseFirestore.getInstance();


        //Initialize and Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.employer_Bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.employerJobNav);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.employerHomeNav:
                        startActivity(new Intent(getApplicationContext()
                                , employer_home_page.class));
                        overridePendingTransition(0,0);

                        return true;
                    case R.id.employerJobNav:


                        return true;
                    case R.id.employerAccountNav:
                        startActivity(new Intent(getApplicationContext()
                                , EmployerAccountPage.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });


        // below line is use to get the data from Firebase Firestore.
        // previously we were saving data on a reference of Courses
        // now we will be getting the data from the same reference.
        db.collection("Jobs")
                .whereEqualTo("employerID", FirebaseAuth.getInstance().getCurrentUser().getUid())
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
                            loadingPB.setVisibility(View.GONE);
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                // after getting this list we are passing
                                // that list to our object class.
                                Job c = d.toObject(Job.class);

                                // and we will pass this object class
                                // inside our arraylist which we have
                                // created for recycler view.
                                jobPostedList.add(c);
                            }
                            // after adding the data to recycler view.
                            // we are calling recycler view notifuDataSetChanged
                            // method to notify that data has been changed in recycler view.
                            jobPostedRVAdapter.notifyDataSetChanged();
                        } else {
                            // if the snapshot is empty we are displaying a toast message.
                            loadingPB.setVisibility(View.GONE);

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
        String jobID = jobPostedRVAdapter.getItem(position).getJobID();
        Intent intent = new Intent(this, ViewJobApplication.class);
        intent.putExtra("jobID",jobID);
        startActivity(intent);
    }
}