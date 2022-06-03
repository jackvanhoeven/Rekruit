package com.example.rekruit.employer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.rekruit.R;
import com.example.rekruit.applicant.ApplicantHomePage;
import com.example.rekruit.applicant.job_list;
import com.example.rekruit.authentication.login_applicant;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class EmployerAccountPage extends AppCompatActivity {

    ImageView btnLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_account_page);

        btnLogout = findViewById(R.id.employerLogoutBtn);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signOut();
                Intent intent = new Intent(getApplicationContext(), login_applicant.class);
                startActivity(intent);
            }
        });

        //Iniatialize and Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.employer_Bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.employerAccountNav);

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
                        startActivity(new Intent(getApplicationContext()
                                , EmployerJobPage.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.employerAccountNav:

                        return true;
                }
                return false;
            }
        });
    }

    private void signOut() {
        // [START auth_sign_out]
        FirebaseAuth.getInstance().signOut();
        // [END auth_sign_out]
    }
}