package com.example.rekruit.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.rekruit.R;
import com.example.rekruit.authentication.login_applicant;
import com.google.firebase.auth.FirebaseAuth;

public class AdminHomePage extends AppCompatActivity {

    LinearLayout mngUserLL,mngEmpLL;
    ImageView lgtBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page);

       mngUserLL = findViewById(R.id.mngUserLL);
        mngEmpLL = findViewById(R.id.mngEmpLL);

       lgtBtn = findViewById(R.id.logoutBtnAdmin);

       lgtBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               signOut();
               Intent intent = new Intent(getApplicationContext(), login_applicant.class);
               startActivity(intent);
           }
       });
       mngUserLL.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               goToManageUserPage();
           }
       });
        mngEmpLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToManageEmployerPage();
            }
        });
    }

    private void goToManageEmployerPage() {

        Intent intent = new Intent(AdminHomePage.this,AdminManageEmployer.class);
        startActivity(intent);
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    private void goToManageUserPage() {
        Intent intent = new Intent(AdminHomePage.this,AdminManageUser.class);
        startActivity(intent);
    }
}