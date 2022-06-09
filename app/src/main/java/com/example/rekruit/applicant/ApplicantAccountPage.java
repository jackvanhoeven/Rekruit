package com.example.rekruit.applicant;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;


import com.example.rekruit.R;
import com.example.rekruit.authentication.login_applicant;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ApplicantAccountPage extends AppCompatActivity {

    ImageView btnLogout,userIV;
    ImageView pdfIV;
    Map<String, Object> user = new HashMap<>();
    ProgressDialog progressDialog;
    private FirebaseFirestore db;
    String url;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_account_page);


        //Iniatialize and Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.accountNav);

        btnLogout = findViewById(R.id.logoutBtn);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signOut();
                Intent intent = new Intent(getApplicationContext(), login_applicant.class);
                startActivity(intent);
            }
        });

        pdfIV = findViewById(R.id.resumeIV);

        pdfIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToApplicantResumePage();
            }
        });




        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.homeNav:
                        startActivity(new Intent(getApplicationContext()
                                ,ApplicantHomePage.class));
                        overridePendingTransition(0,0);

                        return true;
                    case R.id.jobNav:
                        startActivity(new Intent(getApplicationContext()
                                ,job_list.class));
                        overridePendingTransition(0,0);

                        return true;

                    case R.id.accountNav:

                        return true;
                }
                return false;
            }
        });


    }

    private void goToApplicantResumePage() {

        Intent intent = new Intent(ApplicantAccountPage.this, ApplicantResumePage.class);
        startActivity(intent);
    }

    private void signOut() {
        // [START auth_sign_out]
        FirebaseAuth.getInstance().signOut();
        // [END auth_sign_out]
    }
}