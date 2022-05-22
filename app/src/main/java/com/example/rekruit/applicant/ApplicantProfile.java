package com.example.rekruit.applicant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.rekruit.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ApplicantProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_profile);

        //Iniatialize and Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.accountNav);

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
}