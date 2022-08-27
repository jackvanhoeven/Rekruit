package com.example.rekruit.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rekruit.R;
import com.example.rekruit.applicant.ApplicantAccountPage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class AdminViewVerificationDetails extends AppCompatActivity {

    TextView userName,email,phoneNum;
    ImageView icPic,selfiePic;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db= FirebaseFirestore.getInstance();;
    String icUrl,selfieUrl,applicantID;
    Button approveBtn,rejectBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_verification_details);

        userName = findViewById(R.id.usernameAVVDTV);
        email = findViewById(R.id.userEmailAVVDTV);
        phoneNum = findViewById(R.id.phoneNumAVVDTV);

        icPic =findViewById(R.id.icIV);
        selfiePic = findViewById(R.id.selfieICIV);

        approveBtn = findViewById(R.id.approveUserBtn);
        rejectBtn = findViewById(R.id.rejectUserBtn);

        Intent intent = getIntent();
        applicantID = intent.getStringExtra("applicantID");

        displayDetails();


        approveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                verifyUser();
            }
        });
        
        rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rejectRequest();
            }
        });
    }

    private void rejectRequest() {

        DocumentReference docRef = db.collection("users").document(applicantID);

        docRef
                .update("verify", "reject")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Intent intent = new Intent(getApplicationContext(), AdminHomePage.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                        Toast.makeText(getApplicationContext(), "The account have been successfully verified", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("EditName", "Error updating document", e);
                    }
                });


    }

    private void verifyUser() {

        DocumentReference docRef = db.collection("users").document(applicantID);

        docRef
                .update("verify", "verify")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Intent intent = new Intent(getApplicationContext(), AdminHomePage.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                        Toast.makeText(getApplicationContext(), "The account have been successfully verified", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("EditName", "Error updating document", e);
                    }
                });
    }

    private void displayDetails() {
        db.collection("users")
                .whereEqualTo("applicantID", applicantID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());

                                icUrl = document.getData().get("IC URL").toString();
                                selfieUrl = document.getData().get("Selfie URL").toString();
                                Log.d("Check url", document.getData().get("Selfie URL").toString());
                                email.setText(document.getData().get("email").toString());
                                userName.setText(document.getData().get("applicantName").toString());
                                phoneNum.setText(document.getData().get("phoneNum").toString());
//
//                                icPic.setImageURI((Uri) document.getData().get(icUrl));
//                                selfiePic.setImageURI((Uri) document.getData().get(selfieUrl));
                                Picasso.with(AdminViewVerificationDetails.this).load(icUrl).into(icPic);
                                Picasso.with(AdminViewVerificationDetails.this).load(selfieUrl).into(selfiePic);


                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
}