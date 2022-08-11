package com.example.rekruit.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rekruit.R;
import com.example.rekruit.applicant.ApplicantHomePage;
import com.example.rekruit.applicant.ApplicantVerification;
import com.example.rekruit.applicant.job_list;
import com.example.rekruit.applicant.registerApplicantActivity;
import com.example.rekruit.employer.employer_home_page;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class login_applicant extends AppCompatActivity {
    private static final String TAG = "login_applicant" ;

    private FirebaseAuth mAuth;

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView createNewAccountTV, forgotPasswordTV;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser mUser;
    private ProgressDialog progressDialog;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_applicant);

        //view
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        createNewAccountTV = findViewById(R.id.createNewAccountTV);
        forgotPasswordTV = findViewById(R.id.forgotPasswordTV);

        // Initialize Firebase Auth
            mAuth = FirebaseAuth.getInstance();
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle("Please wait");
//        progressDialog.setCanceledOnTouchOutside(false);

        createNewAccountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toRegisterActivity();
            }
        });
//
//        forgotTV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
//            }
//        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginApplicant();
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            checkUserType();
        }
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//          toJobList();
//        }
//    }
    private void loginApplicant() {


        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();


        if(!email.equals("") && !password.equals("")) {

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                checkUserType();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(login_applicant.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else {

            Toast.makeText(login_applicant.this, "Authentication failed.",Toast.LENGTH_SHORT).show();

        }



    }

    private void checkUserType() {

        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        if(document.getData().get("userType").toString().equals("Applicant")){
                            if(document.getData().get("verify").toString().equals("true")){
                                toApplicantHomePage();//if applicant is verified, proceed
                            }else if(document.getData().get("verify").toString().equals("false")){
                                toApplicantVerifyPage();//applicant need to be verified to use the app
                            }
                        }else if(document.getData().get("userType").toString().equals("Employer")){
                            if(document.getData().get("verify").toString().equals("true")){
                                toEmployerHomePage();
                            }else{
                                toEmployerVerifyPage();
                            }



                        }
                    } else {
                       Log.d(TAG, "No such User");

                    }
                } else {
                    Log.d(TAG, "User not exist ", task.getException());
                }
            }
        });



    }

    private void toEmployerVerifyPage() {
    }

    private void toApplicantVerifyPage() {
        Intent intent = new Intent(login_applicant.this, ApplicantVerification.class);
        startActivity(intent);
    }

    private void toApplicantHomePage() {
        Intent intent = new Intent(login_applicant.this, ApplicantHomePage.class);
        startActivity(intent);
    }

    private void toEmployerHomePage() {
        Intent intent = new Intent(login_applicant.this, employer_home_page.class);
        startActivity(intent);
    }


    public void toJobList(){
        Intent intent = new Intent(login_applicant.this, job_list.class);
        startActivity(intent);
    }

    public void toRegisterActivity(){
        Intent intent = new Intent(login_applicant.this, registerApplicantActivity.class);
        startActivity(intent);
    }


}