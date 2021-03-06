package com.example.rekruit.employer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.rekruit.R;
import com.example.rekruit.applicant.ApplicantAccountPage;
import com.example.rekruit.applicant.job_list;
import com.example.rekruit.applicant.registerApplicantActivity;
import com.example.rekruit.authentication.login_applicant;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class employer_home_page extends AppCompatActivity {


    Button btnPostJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_home_page);

        //Iniatialize and Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.employer_Bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.employerHomeNav);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.employerHomeNav:

                        return true;
                    case R.id.employerJobNav:
                        startActivity(new Intent(getApplicationContext()
                                , EmployerJobPage.class));
                        overridePendingTransition(0,0);
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


        btnPostJob = findViewById(R.id.btnPostJob); //declare Button

        btnPostJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                toPostJobPage();
            }
        });
    }

    private void toPostJobPage() {

        Intent intent = new Intent(employer_home_page.this, post_job.class);
        startActivity(intent);
    }
}