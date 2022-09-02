package com.example.rekruit.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rekruit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class AdminViewEmployerDetails extends AppCompatActivity {

    TextView employerName,regNum,email,phoneNum,address;
    String employerID;
    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    Button approveBtn,rejectBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_employer_details);

        employerName = findViewById(R.id.tvEmpNameAVED);
        regNum = findViewById(R.id.tvRegNumAVED);
        email = findViewById(R.id.tvEmailAVED);
        phoneNum = findViewById(R.id.tvPhoneNumAVED);
        address = findViewById(R.id.tvAddressAVED);

        approveBtn = findViewById(R.id.approveEmpBtn);
        rejectBtn = findViewById(R.id.rejectEmpBtn);

        Intent intent = getIntent();
        employerID = intent.getStringExtra("employerID");

        displayDetails();

        approveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                verifyEmployer();
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
        DocumentReference docRef = db.collection("users").document(employerID);

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

    private void verifyEmployer() {

        DocumentReference docRef = db.collection("users").document(employerID);

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
                .whereEqualTo("employerID", employerID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());



                                email.setText(document.getData().get("email").toString());
                                regNum.setText(document.getData().get("regNum").toString());
                                employerName.setText(document.getData().get("employerName").toString());
                                phoneNum.setText(document.getData().get("phoneNum").toString());
                                address.setText(document.getData().get("employerLoc").toString());
//
//


                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}