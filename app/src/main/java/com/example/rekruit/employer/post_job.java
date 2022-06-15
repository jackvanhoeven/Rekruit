package com.example.rekruit.employer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rekruit.R;
import com.example.rekruit.model.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class post_job extends AppCompatActivity {

    Button btnPostJob;

    EditText etJobTitle,  etJobDesc, etSalary,etJobReq;
    TextView jobTypeTV,jobCategoryTV;

    String employerName,employerLoc,state;
    String currentUserID;
    private double latlng;
    Map<String, Object> job = new HashMap<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job);


        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.e("testGetCurrentUserID", currentUserID);

        getEmployerInfo();

        btnPostJob = findViewById(R.id.btnPostJob);
        etJobTitle = findViewById(R.id.etJobTitle);
        jobTypeTV = findViewById(R.id.tvJobType);
        etJobDesc = findViewById(R.id.etJobDesc);
        etJobReq = findViewById(R.id.etJobReq);
        etSalary = findViewById(R.id.etSalary);
        jobCategoryTV = findViewById(R.id.tvJobCategory);




        btnPostJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                postJob();
            }
        });

        jobTypeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jobTypeDialog();

            }
        });

        jobCategoryTV.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jobCategoryDialog();
            }
        }));
    }

    private void jobCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Job Specialization")
                .setItems(Constant.jobCategory, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String category = Constant.jobCategory[which];


                        //set pick category
                        jobCategoryTV.setText(category);
                    }
                })
                .show();
    }

    private void getEmployerInfo() {


            db.collection("users").whereEqualTo("employerID", currentUserID)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("TAG", document.getId() + " => " + document.getData());


                                    employerName = document.getData().get("employerName").toString();
                                    employerLoc = document.getData().get("employerLoc").toString();
                                    state = document.getData().get("state").toString();




                                }
                            } else {
                                Log.d("TAG", "Error getting documents: ", task.getException());
                            }

                        }
                    });

    }

    private void postJob() {

        String jobTitle = etJobTitle.getText().toString();
        String jobType = jobTypeTV.getText().toString();
        String jobReq = etJobReq.getText().toString();
        String jobCategory = jobCategoryTV.getText().toString();
        String jobDesc = etJobDesc.getText().toString();
        String salary = etSalary.getText().toString();


        final String timestamp = ""+System.currentTimeMillis();//to generate job ID


        job.put("jobTitle", jobTitle);
        job.put("jobType", jobType);
        job.put("jobCategory", jobCategory);
        job.put("jobReq", jobReq);
        job.put("jobDesc", jobDesc);
        job.put("salary", salary);
        job.put("employerName", employerName);
        job.put("employerLoc",employerLoc);
        job.put("employerID", FirebaseAuth.getInstance().getCurrentUser().getUid());
        job.put("jobID",timestamp);

        db.collection("Jobs")
                .add(job)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "DocumentSnapshot written with ID: " + documentReference.getId());

                        Toast.makeText(post_job.this, "Job Posted Successfully", Toast.LENGTH_SHORT).show();
                          clearData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });






    }

    private void clearData() {

        //clear
        etJobTitle.setText("");
        jobTypeTV.setText("");
        etJobDesc.setText("");
        etSalary.setText("");

    }

    private void jobTypeDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Job Type")
                .setItems(Constant.jobType, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String category = Constant.jobType[which];


                        //set pick category
                        jobTypeTV.setText(category);
                    }
                })
                .show();
    }
}


