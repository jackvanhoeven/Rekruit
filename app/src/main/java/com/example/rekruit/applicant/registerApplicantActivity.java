package com.example.rekruit.applicant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rekruit.R;
import com.example.rekruit.authentication.login_applicant;
import com.example.rekruit.employer.register_employer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class registerApplicantActivity extends AppCompatActivity {


    EditText etEmail, etPassword,etConfirmPassword, etFullName,etPhoneNumber;
    String emailPattern = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]";
    Map<String, Object> user = new HashMap<>();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    Button btnSignUp;
    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private TextView registerEmployerTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_applicant);

        //declare EditText
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfrimPassword);
        etFullName = findViewById(R.id.etFullName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        registerEmployerTV = findViewById(R.id.registerEmployerTV);

        btnSignUp = findViewById(R.id.btnSignUp); //declare Button

        progressDialog = new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RegisterApplicant();
            }
        });

        registerEmployerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toRegisterEmployer();
            }
        });

    }

    private void toRegisterEmployer() {
        Intent intent = new Intent(registerApplicantActivity.this, register_employer.class);
        startActivity(intent);
    }

    private void RegisterApplicant() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        String fullName = etFullName.getText().toString();
        String phoneNumber = etPhoneNumber.getText().toString();

        // Create a new user with a first and last name

        user.put("Email",email);
        user.put("Password", password);
        user.put("confirmPassword",confirmPassword);
        user.put("FullName", fullName);
        user.put("PhoneNumber", phoneNumber);

        user.put("userType", "Applicant");


        if(!etEmail.getText().toString().equals("") && !etFullName.getText().toString().equals("") && !etPhoneNumber.getText().toString().equals("")
                && !etPassword.getText().toString().equals("") && !etConfirmPassword.getText().toString().equals("")) {

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    task.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(@NonNull AuthResult authResult) {
                            // Register User add to Firebase
                            firebaseFirestore.collection("users")
                                    .document(authResult.getUser().getUid())
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(@NonNull Void unused) {
                                            Toast.makeText(registerApplicantActivity.this, "Sign Up Succesfully", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(registerApplicantActivity.this, login_applicant.class);
                                            startActivity(intent);
                                        }
                                    });


                        }
                    });
                    task.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(registerApplicantActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
        else {
            Toast.makeText(registerApplicantActivity.this, "Sign Up Failed", Toast.LENGTH_LONG).show();
        }
    }




}