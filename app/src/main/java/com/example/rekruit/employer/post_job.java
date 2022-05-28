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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class post_job extends AppCompatActivity {

    Button btnPostJob;

    EditText etJobTitle,  etJobDesc, etSalary;
    TextView jobTypeTV;

    Map<String, Object> job = new HashMap<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job);



        btnPostJob = findViewById(R.id.btnPostJob);
        etJobTitle = findViewById(R.id.etJobTitle);
        jobTypeTV = findViewById(R.id.tvJobType);
        etJobDesc = findViewById(R.id.etJobDesc);
        etSalary = findViewById(R.id.etSalary);


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
    }

    private void postJob() {

        String jobTitle = etJobTitle.getText().toString();
        String jobType = jobTypeTV.getText().toString();
        String jobDesc = etJobDesc.getText().toString();
        String salary = etSalary.getText().toString();


        final String timestamp = ""+System.currentTimeMillis();

        job.put("jobTitle", jobTitle);
        job.put("jobType", jobType);
        job.put("jobDesc", jobDesc);
        job.put("salary", salary);
        job.put("employerID", FirebaseAuth.getInstance().getCurrentUser().getUid());
        job.put("jobID",timestamp);

        db.collection("Jobs")
                .add(job)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "DocumentSnapshot written with ID: " + documentReference.getId());

                        Toast.makeText(post_job.this, "Job Posted Successfully", Toast.LENGTH_SHORT).show();
//                        clearData();
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


