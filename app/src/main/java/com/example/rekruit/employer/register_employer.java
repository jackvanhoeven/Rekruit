package com.example.rekruit.employer;

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
import com.example.rekruit.applicant.registerApplicantActivity;
import com.example.rekruit.authentication.login_applicant;
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

public class register_employer extends AppCompatActivity {


    EditText etCompanyName, etAddress, etCompanyEmail, etPassword,etConfirmPassword,etPhoneNumber;
    String emailPattern = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]";
    Map<String, Object> user = new HashMap<>();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    Button btnSignUp;
    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private TextView registerEmployerTV,skipToLoginTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_employer);

        //declare EditText
        etCompanyName = findViewById(R.id.etCompanyName);
        etCompanyEmail = findViewById(R.id.etCompanyEmail);
        etAddress = findViewById(R.id.etAddress);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);


        btnSignUp = findViewById(R.id.btnSignUp); //declare Button
        skipToLoginTV = findViewById(R.id.skipToLogin);

        progressDialog = new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RegisterEmployer();

            }
        });

        skipToLoginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToLoginPage();
            }
        });
    }

    private void AddEmployerInfo() {

        String companyName = etCompanyName.getText().toString();
        String address = etAddress.getText().toString();
        String email = etCompanyEmail.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        String phoneNumber = etPhoneNumber.getText().toString();

        user.put("employerName",companyName);
        user.put("Address",address);
        user.put("Email",email);
        user.put("Password", password);
        user.put("confirmPassword",confirmPassword);
        user.put("PhoneNumber", phoneNumber);
        user.put("employerID", FirebaseAuth.getInstance().getCurrentUser().getUid());

        user.put("userType", "Employer");



        firebaseFirestore.collection("users").document(this.mAuth.getCurrentUser().getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Successfully Register", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), login_applicant.class);
                        startActivity(intent);
                        //Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Unsuccessfully Register", Toast.LENGTH_SHORT).show();
                        //Log.w(TAG, "Error writing document", e);
                    }
                });

    }

    private void ToLoginPage() {
        Intent intent = new Intent(register_employer.this,login_applicant.class);
        startActivity(intent);
    }

    private void RegisterEmployer() {

        String companyName = etCompanyName.getText().toString();
        String address = etAddress.getText().toString();
        String email = etCompanyEmail.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        String phoneNumber = etPhoneNumber.getText().toString();


        if(!etCompanyName.getText().toString().equals("") && !etAddress.getText().toString().equals("") &&!etCompanyEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("") && !etConfirmPassword.getText().toString().equals("")
            && !etPhoneNumber.getText().toString().equals("")) {

            mAuth.createUserWithEmailAndPassword(email,
                    password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        //Log.d(TAG, "createUserWithEmail:success");



                        Toast.makeText(getApplicationContext(), "Authentication Success.",
                                Toast.LENGTH_SHORT).show();

                        AddEmployerInfo();

                    } else {
                        // If sign in fails, display a message to the user.
                        //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(getApplicationContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        //updateUI(null);
                    }
                }

            });
        }
        else {
            Toast.makeText(register_employer.this, "Sign Up Failed", Toast.LENGTH_LONG).show();
        }
    }
}