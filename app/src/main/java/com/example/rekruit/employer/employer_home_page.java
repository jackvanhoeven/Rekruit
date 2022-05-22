package com.example.rekruit.employer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.rekruit.R;
import com.example.rekruit.applicant.registerApplicantActivity;
import com.example.rekruit.authentication.login_applicant;

public class employer_home_page extends AppCompatActivity {


    Button btnPostJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_home_page);


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