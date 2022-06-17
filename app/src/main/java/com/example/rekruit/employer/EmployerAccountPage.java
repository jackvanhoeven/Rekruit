package com.example.rekruit.employer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rekruit.R;
import com.example.rekruit.applicant.ApplicantHomePage;
import com.example.rekruit.applicant.job_list;
import com.example.rekruit.authentication.login_applicant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class EmployerAccountPage extends AppCompatActivity {

    ImageView btnLogout;
    LinearLayout companyProfileLL;
    TextView fullAddTV;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();;
    String url;
    private TextView companyNameTV, companyEmailTV;
    private ImageView companyIV;
    StorageReference storageReference;
    private Uri image_uri;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_account_page);


        firebaseAuth = FirebaseAuth.getInstance();

        companyIV = findViewById(R.id.companyIV);
        companyProfileLL = findViewById(R.id.companyProfileLLL);

        companyNameTV = findViewById(R.id.employerNameTVA);
        companyEmailTV = findViewById(R.id.companyEmailTVA);

        btnLogout = findViewById(R.id.employerLogoutBtn);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signOut();
                Intent intent = new Intent(getApplicationContext(), login_applicant.class);
                startActivity(intent);
            }
        });

        companyProfileLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toCompanyProfilePage();
            }
        });


        displayCompanyProfile();

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


    private void displayCompanyProfile() {

        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                        companyNameTV.setText(document.getData().get("employerName").toString());
                        companyEmailTV.setText(document.getData().get("email").toString());
//                        employerLoc = document.getData().get("employerLoc").toString();




                        url = document.getData().get("Picture URL").toString();
//                        new EditProfileActivity.FetchImage(url).start();
                        Picasso.with(EmployerAccountPage.this).load(url).into(companyIV);







                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });


    }

    private void toCompanyProfilePage() {

        Intent intent = new Intent(EmployerAccountPage.this, EmployerEditProfile.class);
        startActivity(intent);
    }

    private void signOut() {
        // [START auth_sign_out]
        FirebaseAuth.getInstance().signOut();
        // [END auth_sign_out]
    }
}