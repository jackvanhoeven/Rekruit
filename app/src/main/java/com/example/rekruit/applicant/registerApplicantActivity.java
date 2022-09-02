package com.example.rekruit.applicant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class   registerApplicantActivity extends AppCompatActivity {


    EditText etEmail, etPassword,etConfirmPassword, etFullName,etPhoneNumber;
    Boolean verify = false;
    String emailPattern = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]";
    String email;
    Map<String, Object> user = new HashMap<>();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    Button btnSignUp;
    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private TextView registerEmployerTV;
    ImageView toLoginPageArrow;
    TextView toLoginPageTV;



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


        toLoginPageArrow = findViewById(R.id.toLoginPageArrow);
        toLoginPageTV = findViewById(R.id.toLoginPageTV);


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

        toLoginPageArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToLoginPage();
            }
        });
        toLoginPageTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToLoginPage();
            }
        });

    }

    private void ToLoginPage() {
        Intent intent = new Intent(registerApplicantActivity.this,login_applicant.class);
        startActivity(intent);
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


        //// <---- Validation ---- ////

        // <---- EditText Validation

        // If All Empty
        if (fullName.isEmpty() &&

                phoneNumber.isEmpty() &&
                email.isEmpty() &&
                password.isEmpty() &&
                confirmPassword.isEmpty() ){

            etFullName.setError("Require to fill");
            etPhoneNumber.setError("Require to fill");
            etEmail.setError("Require to fill");
            etPassword.setError("Require to fill");
            etConfirmPassword.setError("Require to fill");

            return;

        };

        if (fullName.isEmpty()){
            etFullName.setError("Require to fill");
            return;
        }
        // validation phone
        if (phoneNumber.isEmpty()){
            etPhoneNumber.setError("Require to fill");
            return;
        }
        if (!phoneNumber.matches("^[0-9]+$")){
            etPhoneNumber.setError("Invalid character, input 0~9 only");
            return;
        }

        if (phoneNumber.length() < 10 || phoneNumber.length() > 11 ){
            etPhoneNumber.setError("Phone number should be at least 10 and at most 11 characters");
            return;
        }

        //validation email
        if (email.isEmpty()){
            etEmail.setError("Require to fill");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Invalid email format");
            return;
        }

        //validation password
        if (password.isEmpty()){
            etPassword.setError("Require to fill");
            return;
        }
        if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*+=?-]).{8,25}$")){
            etPassword.setError("Password should contain 0~9, a~z, symbol, more than 8");
            return;
        }


        //validation confirm password
        if (confirmPassword.isEmpty()){
            etConfirmPassword.setError("Require to fill");
            return;
        }
        if (!confirmPassword.equals(password)){
            etConfirmPassword.setError("Password not same");
            return;
        }



        // ----> EditText Validation


        //// ---- Validation ----> ////




        if(!email.equals("") && !fullName.equals("") && !phoneNumber.equals("")&& !password.equals("") && !confirmPassword.equals("")) {
            mAuth.createUserWithEmailAndPassword(email,
                    password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        //Log.d(TAG, "createUserWithEmail:success");
                        Toast.makeText(getApplicationContext(), "Authentication Success.",
                                Toast.LENGTH_SHORT).show();

                        AddApplicantInfo();

                    } else {
                        // If sign in fails, display a message to the user.
                        //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(getApplicationContext(), "Authentication failed or Email already in use",
                                Toast.LENGTH_SHORT).show();
                        //updateUI(null);
                    }
                }

            });
        }
        else {
            Toast.makeText(registerApplicantActivity.this, "Sign Up Failed", Toast.LENGTH_LONG).show();
        }
    }



    private void AddApplicantInfo() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        String fullName = etFullName.getText().toString();
        String phoneNumber = etPhoneNumber.getText().toString();

        // Create a new user with a first and last name

        user.put("verify","-");
        user.put("email",email);
        user.put("password", password);
        user.put("confirmPassword",confirmPassword);
        user.put("applicantName", fullName);
        user.put("phoneNum", phoneNumber);
        user.put("Picture URL", "-");
        user.put("applicantID", FirebaseAuth.getInstance().getCurrentUser().getUid());

        user.put("userType", "Applicant");


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


}