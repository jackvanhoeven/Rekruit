package com.example.rekruit.employer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rekruit.R;
import com.example.rekruit.applicant.registerApplicantActivity;
import com.example.rekruit.authentication.login_applicant;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class register_employer extends AppCompatActivity {


    EditText etCompanyName, etCompanyRgNum, etAddress,etAddress2,etCity,etPostcode,etState, etCompanyEmail, etPassword,etConfirmPassword,etPhoneNumber;
    String emailPattern = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]";
    String aboutUS;
    String ourBenefit;
    String ourMission;
    Map<String, Object> user = new HashMap<>();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    ImageView logoIV;

    Button btnSignUp;
    ProgressDialog progressDialog;

    GoogleMap mGoogleMap;

    private double latitude, longitude;
    private Geocoder geocoder;
    GeoPoint geoPoint;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private TextView registerEmployerTV,skipToLoginTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_employer);

        //declare EditText
        etCompanyName = findViewById(R.id.etCompanyName);
        etCompanyRgNum = findViewById(R.id.etCompanyRgNum);
        etCompanyEmail = findViewById(R.id.etCompanyEmail);
        etAddress = findViewById(R.id.etAddress);
        etAddress2 = findViewById(R.id.etAddress2);
        etCity = findViewById(R.id.etCity);
        etPostcode = findViewById(R.id.etPostcode);
        etState = findViewById(R.id.etState);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);



        btnSignUp = findViewById(R.id.btnSignUp); //declare Button
//        skipToLoginTV = findViewById(R.id.skipToLogin);
        logoIV = findViewById(R.id.logoIV);

        progressDialog = new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();



        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RegisterEmployer();

            }
        });

        logoIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToLoginPage();
            }
        });
    }



    private void AddEmployerInfo() {

        String companyName = etCompanyName.getText().toString();
        String companyRgNum = etCompanyRgNum.getText().toString();
        String state = etState.getText().toString();
        String city = etCity.getText().toString();
        String address1 = etAddress.getText().toString();
        String address2 = etAddress2.getText().toString();
        String postcode = etPostcode.getText().toString();
        String email = etCompanyEmail.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        String phoneNumber = etPhoneNumber.getText().toString();
        String fullAddress = etAddress.getText().toString()+", "+etAddress2.getText().toString()+", "+etCity.getText().toString()
                +", "+etPostcode.getText().toString() +", "+etState.getText().toString();

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addressList = geocoder.getFromLocationName(fullAddress,5);

            if(addressList.size()>0){
                Address address = addressList.get(0);

                geoPoint = new GeoPoint(address.getLatitude(),address.getLongitude());

                Toast.makeText(this, address.getLocality(), Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }




        user.put("verify","pending");
        user.put("employerName",companyName);
        user.put("regNum",companyRgNum);
        user.put("employerLoc",fullAddress);
        user.put("aboutUS","-");
        user.put("ourBenefit","-");
        user.put("ourMission","-");
        user.put("Picture URL", "-");
        user.put("state",state);
        user.put("city",city);
        user.put("latlng", geoPoint);
        user.put("email",email);
        user.put("password", password);
        user.put("confirmPassword",confirmPassword);
        user.put("phoneNum", phoneNumber);
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
        String companyRgNum = etCompanyRgNum.getText().toString();
        String state = etState.getText().toString();
        String city = etCity.getText().toString();
        String address1 = etAddress.getText().toString();
        String address2 = etAddress2.getText().toString();
        String postcode = etPostcode.getText().toString();
        String email = etCompanyEmail.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        String phoneNumber = etPhoneNumber.getText().toString();
        String fullAddress = etAddress.getText().toString()+", "+etAddress2.getText().toString()+", "+etCity.getText().toString()
                +", "+etPostcode.getText().toString() +", "+etState.getText().toString();


        //// <---- Validation ---- ////

        // <---- EditText Validation

        // If All Empty
        if (companyName.isEmpty() &&

                companyRgNum.isEmpty()&&
                address1.isEmpty()&&
                address2.isEmpty()&&
                postcode.isEmpty()&&
                state.isEmpty()&&
                city.isEmpty()&&
                phoneNumber.isEmpty() &&
                email.isEmpty() &&
                password.isEmpty() &&
                confirmPassword.isEmpty() ){

            user.put("verify","-");


            etCompanyName.setError("Require to fill");
            etCompanyRgNum.setError("Require to fill");
            etState.setError("Require to fill");
            etCity.setError("Require to fill");
            etPhoneNumber.setError("Require to fill");
            etCompanyEmail.setError("Require to fill");
            etPassword.setError("Require to fill");
            etConfirmPassword.setError("Require to fill");
            etAddress.setError("Require to fill");
            etAddress2.setError("Require to fill");
            etPostcode.setError("Require to fill");

            return;

        };

        //// <---- Validation ---- ////

        // <---- EditText Validation

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
            etCompanyEmail.setError("Require to fill");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etCompanyEmail.setError("Invalid email format");
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

    private void geoLocate(View view) {

        String companyAddress = etAddress.getText().toString();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addressList = geocoder.getFromLocationName(companyAddress,1);

            if(addressList.size()>0){
                Address address = addressList.get(0);



                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(address.getLatitude(),address.getLongitude())));

                Toast.makeText(this, address.getLocality(), Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}