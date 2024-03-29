package com.example.rekruit.applicant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rekruit.R;
import com.example.rekruit.model.Job;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class ApplicantJobDescriptionPage extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private FirebaseFirestore db;
    private TextView jobTitleTVjobDesc, companyNameTVjobDesc,fullJobDescTV,fullAddrTV,jobReqTV,aboutUSTV,ourBenefitTV;
    private String jobID,employerID, applicationID,applicantID,applicationStatus,jobTittle,saveStatus,savedJobID,applicantName,employerLoc,salary;
    private double latlng;
    GoogleMap mGoogleMap;
    String employerName;
    ArrayList<Job> list;
    Button applyJobBtn, saveJobBtn;
    Map<String, Object> jobApplication = new HashMap<>();
    Map<String, Object> saveJobHashMap = new HashMap<>();

    private int GPS_REQUEST_CODE = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_job_description_page);

        jobTitleTVjobDesc = findViewById(R.id.jobTitleTVjobDesc);
        companyNameTVjobDesc = findViewById(R.id.companyNameTVjobDesc);
        fullJobDescTV = findViewById(R.id.fullJobDescTV);
        fullAddrTV = findViewById(R.id.fullAddTV);
        jobReqTV = findViewById(R.id.jobReqTV);

        aboutUSTV = findViewById(R.id.aboutUsTVAJDP);
        ourBenefitTV = findViewById(R.id.ourBenefitTVAJDP);


        db = FirebaseFirestore.getInstance();
        applyJobBtn = findViewById(R.id.applyJobBtn);
        saveJobBtn = findViewById(R.id.saveJobBtn);



        applicantID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.e("testGetCurrentUserID",  applicantID);


        Intent intent = getIntent();
        jobID = intent.getStringExtra("jobID");
        Log.e("getJobID",jobID);
        //create random id
        Random rand = new Random();
        int randomID = rand.nextInt(99999999)+1;
        applicationID = "application" + randomID;

        savedJobID = "savedJob" + randomID;

        getApplicantInfo();


        checkInitialApplicationStatus();
        checkInitialSaveStatus();

//        displayJobDesc();
        displayJobInfoTest();
        getEmployerInfo();

        initMap();

        applyJobBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                checkApplicationStatus();
            }
        });

        saveJobBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkSaveJobStatus();
            }
        });




    }

    private void geoLocate() {

        String locationName = employerLoc;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addressList =geocoder.getFromLocationName(locationName,5);

            if(addressList.size()>0){
                Address address = addressList.get(0);

                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(address.getLatitude(),address.getLongitude())));
                gotoLocation(address.getLatitude(),address.getLongitude());


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void gotoLocation(double latitude, double longitude) {

        LatLng LatLng = new LatLng(latitude, longitude);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng, 18);
        mGoogleMap.moveCamera(cameraUpdate);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    private void initMap() {

                SupportMapFragment supportMapFragment =  (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
                supportMapFragment.getMapAsync(this);
    }



    private void getEmployerInfo() {

        db.collection("users")
                .whereEqualTo("employerID",employerID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());

                                employerLoc = document.getData().get("employerLoc").toString();
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }

                    }
                });
    }

    private void getApplicantInfo() {

        db.collection("users")
                .whereEqualTo("applicantID",applicantID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());


                                applicantName = document.getData().get("applicantName").toString();



                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }

                    }
                });
    }

    private void checkInitialSaveStatus() {

        db.collection("savedJob")
                .whereEqualTo("jobID", jobID)
                .whereEqualTo("applicantID",applicantID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                saveJobBtn.setText("Saved");
                                saveJobBtn.setBackgroundColor(Color.GRAY);
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void checkSaveJobStatus() {
        db.collection("savedJob")
                .whereEqualTo("jobID", jobID)
                .whereEqualTo("applicantID",applicantID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().isEmpty()){
                                saveJob();
                            }
                            else{
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("TAG", document.getId() + " => " + document.getData());
                                    saveJobBtn.setText("Saved");
                                    saveJobBtn.setBackgroundColor(Color.GRAY);
                                }

                            }

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });


    }

    private void saveJob() {

        saveStatus = "saved";



        saveJobHashMap.put("applicationID", applicationID);
        saveJobHashMap.put("employerID", employerID);
        saveJobHashMap.put("applicantID", applicantID);
        saveJobHashMap.put("saveStatus", saveStatus);
        saveJobHashMap.put("savedJobID",savedJobID);
        saveJobHashMap.put("employerName",employerName);
        saveJobHashMap.put("jobID",jobID);
        saveJobHashMap.put("jobTitle",jobTittle);
        saveJobHashMap.put("employerLoc",employerLoc);
        saveJobHashMap.put("salary",salary);


        db.collection("savedJob").document(savedJobID).set(saveJobHashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {


                        changeSaveButtonColor();

                        Toast.makeText(ApplicantJobDescriptionPage.this, "Saved", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void changeSaveButtonColor() {

        db.collection("savedJob").whereEqualTo("savedJobID", savedJobID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());

                                if(!saveStatus.equals("")) {
                                    saveJobBtn.setText("Saved");
                                    saveJobBtn.setBackgroundColor(Color.GRAY);
                                }
                            }
                        }
                        else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }

                    }
                });
    }

    private void checkInitialApplicationStatus() {

        //this function used to set the button color and text when we open the job desc page initially
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
                            if(task.getResult().isEmpty()){                             //check if job application already exist
                                jobApplication();                                       //if not, then only we generate new one
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
        jobApplication.put("employerName",employerName);
        jobApplication.put("jobTitle",jobTittle);
        jobApplication.put("applicantName",applicantName);
        jobApplication.put("employerLoc",employerLoc);


        db.collection("application").document(applicationID).set(jobApplication)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {


                changeButtonColor();

                Toast.makeText(ApplicantJobDescriptionPage.this, "Saved", Toast.LENGTH_SHORT).show();

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
                                employerName = document.getData().get("employerName").toString();
                                jobTittle = document.getData().get("jobTitle").toString();
                                employerLoc = document.getData().get("employerLoc").toString();
                                salary = document.getData().get("salary").toString();
                                fullAddrTV.setText(document.getData().get("employerLoc").toString());
                                jobReqTV.setText(document.getData().get("jobReq").toString());

                                displayCompanyProfile();
                                geoLocate();


                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }

                    }
                });
    }

    private void displayCompanyProfile() {

        db.collection("users")
                .whereEqualTo("employerID",employerID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());


                                aboutUSTV.setText(document.getData().get("aboutUS").toString());
                                ourBenefitTV.setText(document.getData().get("ourBenefit").toString());



                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }

                    }
                });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap = googleMap;


    }


}