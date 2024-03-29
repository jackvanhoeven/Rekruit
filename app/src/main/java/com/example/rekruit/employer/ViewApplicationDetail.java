package com.example.rekruit.employer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rekruit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class ViewApplicationDetail extends AppCompatActivity {

    Button interviewBtn,rejectBtn;
    private String applicationID,applicantID,jobID,employerID,interviewID,applicationStatus, jobTittle, employerName,applicantName,employerLoc,salary;
    private TextView resumeTV;

    Uri resumeUri;
    String pdfUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;



    FirebaseFirestore db = FirebaseFirestore.getInstance();



    TextView applicantNameTV,emailTV,phoneNumberTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_applicant_detail);

        interviewBtn = findViewById(R.id.interviewBtn);
        rejectBtn = findViewById(R.id.rejectBtn);

        applicantNameTV = findViewById(R.id.nameTVVAD);
        emailTV = findViewById(R.id.emailTVVAD);
        phoneNumberTV = findViewById(R.id.phoneNumVAD);

        resumeTV = findViewById(R.id.applicantResumeTV);


        SpannableString content = new SpannableString("Download Resume");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        resumeTV.setText(content);


        Intent intent = getIntent();
        applicationID = intent.getStringExtra("applicationID");
        jobID = intent.getStringExtra("jobID");


        getApplicantInfo();
        getJobInfo();


        Random rand = new Random();
        int randomID = rand.nextInt(99999999)+1;
        interviewID = "interview" + randomID;




        checkInitialInterviewStatus();
        checkInitialRejectStatus();


        resumeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                download();
            }
        });
        interviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInterviewStatus();
            }
        });
        
        rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//              checkRejectedStatus();
                rejectApplication();
            }
        });




    }

    private void displayApplicantInfo() {

        db.collection("users")
                .whereEqualTo("applicantID",applicantID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("display profile", document.getId() + " => " + document.getData());

                                applicantNameTV.setText(document.getData().get("applicantName").toString());
                                emailTV.setText(document.getData().get("email").toString());
                                phoneNumberTV.setText(document.getData().get("phoneNum").toString());


                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }

                    }
                });


    }

    private void download() {

        storageReference= FirebaseStorage.getInstance().getReference();


        db.collection("users")
                .whereEqualTo("applicantID",applicantID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                Log.e("tengok id",applicantID);


                                pdfUri = document.getData().get("resumeUri").toString();

                                downloadFile(ViewApplicationDetail.this,"Applicant Resume",".pdf", Environment.DIRECTORY_DOWNLOADS,pdfUri);



                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }

                    }
                });
    }

    public void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url){

        DownloadManager downloadManager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory,fileName+fileExtension);

        downloadManager.enqueue(request);

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
                                employerLoc = document.getData().get("employerLoc").toString();



                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }

                    }
                });
    }

    private void rejectApplication() {

        Map<String, Object> updateApplication = new HashMap<>();



        updateApplication.put("applicationStatus", "rejected");
        DocumentReference docRef = db.collection("application").document(applicationID);
        docRef.update("applicationStatus", updateApplication.get("applicationStatus"))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Successfully Updated", Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "DocumentSnapshot Rejected");
                        rejectBtn.setText("Rejected");
                        rejectBtn.setBackgroundColor(Color.GRAY);
                        rejectBtn.setClickable(false);
                        interviewBtn.setClickable(false);
                        interviewBtn.setBackgroundColor(Color.GRAY);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("TAG", "Error updating document", e);
            }
        });

    }

    private void getApplicantInfo() {

        db.collection("application")
                .whereEqualTo("applicationID",applicationID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());


                                applicantID = document.getData().get("applicantID").toString();

                                displayApplicantInfo();



                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }

                    }
                });


    }

    private void checkRejectedStatus() {



        db.collection("application").whereEqualTo("applicationID",applicationID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());

                                applicationStatus = document.getData().get("applicationStatus").toString();//get current job status

                                if(applicationStatus == "rejected"){
                                    rejectBtn.setText("Rejected");
                                    rejectBtn.setBackgroundColor(Color.GRAY);
                                }


                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }

                    }
                });
    }

    private void checkInterviewStatus() {
        db.collection("interview")
                .whereEqualTo("jobID", jobID)
                .whereEqualTo("applicantID",applicantID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().isEmpty()){
                                interviewApplicant();
                            }
                            else{
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("TAG", document.getId() + " => " + document.getData());
                                    interviewBtn.setText("Applied");
                                    interviewBtn.setBackgroundColor(Color.GRAY);
                                    interviewBtn.setClickable(false);
                                }

                            }

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });


    }

    private void interviewApplicant() {

        Map<String, Object> interviewApplicant = new HashMap<>();
        applicationStatus = "interview";



        interviewApplicant.put("applicationID", applicationID);
        interviewApplicant.put("interviewID", interviewID);
        interviewApplicant.put("employerID", employerID);
        interviewApplicant.put("applicantID", applicantID);
        interviewApplicant.put("applicationStatus", applicationStatus);
        interviewApplicant.put("jobID", jobID);
        interviewApplicant.put("employerName",employerName);
        interviewApplicant.put("jobTitle",jobTittle);
        interviewApplicant.put("applicantName",applicantName);
        interviewApplicant.put("employerLoc",employerLoc);


        db.collection("interview").document(interviewID).set(interviewApplicant)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {


                        changeButtonColor();

                        Toast.makeText(ViewApplicationDetail.this, "Saved", Toast.LENGTH_SHORT).show();

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

                                if(!applicationStatus.equals("interview")) {

                                    interviewBtn.setBackgroundColor(Color.GRAY);
                                    interviewBtn.setClickable(false);
                                }
                            }
                        }
                        else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }

                    }
                });
    }

    private void checkInitialRejectStatus() {

        db.collection("application").whereEqualTo("applicationID",applicationID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());

                                applicationStatus = document.getData().get("applicationStatus").toString();//get current job status

                                if(applicationStatus.equals("rejected")){
                                    rejectBtn.setText("Rejected");
                                    rejectBtn.setBackgroundColor(Color.GRAY);
                                    rejectBtn.setClickable(false);
                                    interviewBtn.setClickable(false);
                                    interviewBtn.setBackgroundColor(Color.GRAY);
                                }


                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }

                    }
                });
    }

    private void checkInitialInterviewStatus() {

        db.collection("interview")
                .whereEqualTo("jobID", jobID)
                .whereEqualTo("applicationID",applicationID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                interviewBtn.setText("Interview");
                                interviewBtn.setBackgroundColor(Color.GRAY);
                                interviewBtn.setClickable(false);
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}