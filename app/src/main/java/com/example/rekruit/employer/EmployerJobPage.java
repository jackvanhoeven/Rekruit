package com.example.rekruit.employer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.rekruit.R;
import com.example.rekruit.applicant.ApplicantHomePage;
import com.example.rekruit.applicant.job_list;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EmployerJobPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_job_page);

        //Iniatialize and Assign Variable
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
    }
}