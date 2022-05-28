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

    private TextView registerEmployerTV;

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

        progressDialog = new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RegisterEmployer();
            }
        });
    }

    private void RegisterEmployer() {

        String companyName = etCompanyName.getText().toString();
        String address = etAddress.getText().toString();
        String email = etCompanyEmail.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        String phoneNumber = etPhoneNumber.getText().toString();

        // Create a new user with a first and last name


        user.put("Company Name",companyName);
        user.put("Address",address);
        user.put("Email",email);
        user.put("Password", password);
        user.put("confirmPassword",confirmPassword);
        user.put("PhoneNumber", phoneNumber);

        user.put("userType", "Employer");


        if(!etCompanyName.getText().toString().equals("") && !etAddress.getText().toString().equals("") &&!etCompanyEmail.getText().toString().equals("")
                && !etPassword.getText().toString().equals("") && !etConfirmPassword.getText().toString().equals("")
            && !etPhoneNumber.getText().toString().equals("")) {

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(etCompanyEmail.getText().toString(),
                    etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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
                                            Toast.makeText(register_employer.this, "Sign Up Successfully", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(register_employer.this, login_applicant.class);
                                            startActivity(intent);
                                        }
                                    });


                        }
                    });
                    task.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(register_employer.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
        else {
            Toast.makeText(register_employer.this, "Sign Up Failed", Toast.LENGTH_LONG).show();
        }
    }
}