package com.example.rekruit.applicant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.rekruit.R;
import com.example.rekruit.authentication.login_applicant;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class ApplicantAccountPage extends AppCompatActivity {

    ImageView btnLogout;
    ImageView pdfIV;

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