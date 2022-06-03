package com.example.rekruit.applicant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rekruit.R;
import com.example.rekruit.employer.post_job;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.collection.LLRBNode;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ApplicantJobDescriptionPage extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView jobTitleTVjobDesc, companyNameTVjobDesc,fullJobDescTV;
    private String jobID,employerID, applicationID,applicantID,applicationStatus,jobTittle;

    String employerName;
    ArrayList<Job> list;
    Button applyJobBtn;
    Map<String, Object> jobApplication = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_job_description_page);

        jobTitleTVjobDesc = findViewById(R.id.jobTitleTVjobDesc);
        companyNameTVjobDesc = findViewById(R.id.companyNameTVjobDesc);
        fullJobDescTV = findViewById(R.id.fullJobDescTV);

        db = FirebaseFirestore.getInstance();
        applyJobBtn = findViewById(R.id.applyJobBtn);


        applicantID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.e("testGetCurrentUserID",  applicantID);


        Intent intent = getIntent();
        jobID = intent.getStringExtra("jobID");
        Log.e("getJobID",jobID);
        //create random id
        Random rand = new Random();
        int randomID = rand.nextInt(99999999)+1;
        applicationID = "application" + randomID;


        checkInitialApplicationStatus();
        
//        displayJobDesc();
        displayJobInfoTest();
        applyJobBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                checkApplicationStatus();
            }
        });







    }

    private void checkInitialApplicationStatus() {

        db.collection("application")
                .whereEqualTo("jobID", jobID)
                .whereEqualTo("applicantID",applicantID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                applyJobBtn.setText("Applied");
                                applyJobBtn.setBackgroundColor(Color.GRAY);
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void checkApplicationStatus() {

        db.collection("application")
                .whereEqualTo("jobID", jobID)
                .whereEqualTo("applicantID",applicantID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().isEmpty()){
                                jobApplication();
                            }
                            else{
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("TAG", document.getId() + " => " + document.getData());
                                    applyJobBtn.setText("Applied");
                                    applyJobBtn.setBackgroundColor(Color.GRAY);
                                }

                            }

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void getJobInfo() {

        db.collection("Jobs").whereEqualTo("jobID", jobID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());


                                employerID = document.getData().get("employerID").toString();
                                employerName = document.getData().get("employerName").toString();
                                jobTittle = document.getData().get("jobTitle").toString();



                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }

                    }
                });
    }

    private void jobApplication() {


        applicationStatus = "applied";



        jobApplication.put("applicationID", applicationID);
        jobApplication.put("employerID", employerID);
        jobApplication.put("applicantID", applicantID);
        jobApplication.put("applicationStatus", applicationStatus);
        jobApplication.put("jobID", jobID);


//        db.collection("application")
//                .add(jobApplication)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d("TAG", "DocumentSnapshot written with ID: " + documentReference.getId());
//
//                        changeButtonColor();
//
//                        Toast.makeText(ApplicantJobDescriptionPage.this, "Applied", Toast.LENGTH_SHORT).show();
//
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w("TAG", "Error adding document", e);
//                    }
//                });

        db.collection("application").document(applicationID).set(jobApplication)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {


                changeButtonColor();

                Toast.makeText(ApplicantJobDescriptionPage.this, "Applied", Toast.LENGTH_SHORT).show();

            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void changeButtonColor() {

        db.collection("application").whereEqualTo("applicationID", applicationID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());

                                if(!applicationStatus.equals("")) {
                                    applyJobBtn.setText("Applied");
                                    applyJobBtn.setBackgroundColor(Color.GRAY);
                                }
                            }
                        } 
                                else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }

                    }
                });
    }

    private void displayJobDesc() {

        DocumentReference documentReference = db.collection("Jobs").document(jobID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();

                    if(documentSnapshot.exists()){
                        Log.e("Tag","DocumentSnapshot data: "+documentSnapshot.getData());
                        jobTitleTVjobDesc.setText(documentSnapshot.getData().get("jobTitle").toString());
//                        companyNameTVjobDesc.setText(documentSnapshot.getData().get(""));
                    }
                    else {
                        Log.d("TAG", "No such document");
                    }
                }else{
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }

    public void displayJobInfoTest(){
        db.collection("Jobs").whereEqualTo("jobID",jobID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());

                                jobTitleTVjobDesc.setText(document.getData().get("jobTitle").toString());
                                companyNameTVjobDesc.setText(document.getData().get("employerName").toString());
                                fullJobDescTV.setText(document.getData().get("jobDesc").toString());
                                employerID = document.getData().get("employerID").toString();//get employerID for job application



//
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }

                    }
                });
    }
}