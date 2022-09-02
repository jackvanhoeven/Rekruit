package com.example.rekruit.employer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rekruit.R;
import com.example.rekruit.applicant.ApplicantAccountPage;
import com.example.rekruit.applicant.ApplicantEditPhoneNum;
import com.example.rekruit.applicant.ApplicantVerification;
import com.example.rekruit.authentication.login_applicant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EmployerVerification extends AppCompatActivity {

    EditText compName,regNum;
    FirebaseFirestore db;
    Map<String, Object> user = new HashMap<>();
    TextView pendingTV,rejectTV,logOut;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_verification);

        db = FirebaseFirestore.getInstance();
        compName = findViewById(R.id.etCompNameVer);
        regNum = findViewById(R.id.etRegNumVer);
        btnSave = findViewById(R.id.btnEmpVer);

        pendingTV = findViewById(R.id.empPendingTV);
        rejectTV = findViewById(R.id.empRejectTV);
        logOut= findViewById(R.id.logOutEmpVerTV);


        displayInfo();
        checkSubmission();

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
                Intent intent = new Intent(getApplicationContext(), login_applicant.class);
                startActivity(intent);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateInfo();
                updateRegNum();
                updateStatus(); //update submission status
            }
        });

    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    private void updateStatus() {

        user.put("updateInfo","pending");

        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        docRef.update("verify", user.get("updateInfo"))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(@NonNull Void unused) {
                        Toast.makeText(getApplicationContext(), "Successfully Submitted", Toast.LENGTH_SHORT).show();
                        btnSave.setClickable(false);
                        rejectTV.setVisibility(View.INVISIBLE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed To Update Your Reg Number", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkSubmission() {
        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.e("ProfileActivity", "DocumentSnapshot data: " + document.getData());

                        String status = document.getData().get("verify").toString();

                        if(status.equals("pending")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(EmployerVerification.this);

                            builder.setMessage("Please wait, your verification will be processed within 24 hours");
                            builder.setTitle("Your account verification are still pending.");
                            builder.setCancelable(false);
                            btnSave.setClickable(false);
                            compName.setClickable(false);
                            regNum.setClickable(false);
                            rejectTV.setVisibility(View.INVISIBLE);
                            btnSave.setVisibility(View.INVISIBLE);


                            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.cancel();
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                        else if(status.equals("reject")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(EmployerVerification.this);

                            builder.setMessage("Your Verification is rejected,");
                            builder.setTitle("Your account have not been verified.");
                            builder.setCancelable(false);

                            pendingTV.setVisibility(View.INVISIBLE);

                            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.cancel();
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                        else{

                        }


                    } else {
                        Log.d("ProfileActivity", "No such document");
                    }
                } else {
                    Log.d("ProfileActivity", "get failed with ", task.getException());
                }
            }
        });
    }


    private void displayInfo() {

        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful())
                {
                    DocumentSnapshot doc = task.getResult();

                    if (doc.exists())
                    {
                        Log.d("phone Num", "DocumentSnapshot data: " + doc.getData());
                        compName.setText(doc.getData().get("employerName").toString());
                        regNum.setText(doc.getData().get("regNum").toString());

                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Failed to get your Details", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Log.d("Failed", "get failed with ", task.getException());
                }
            }
        });
    }

    private void updateRegNum() {

        user.put("updateInfo", regNum.getText().toString());

        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        docRef.update("regNum", user.get("updateInfo"))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(@NonNull Void unused) {
                        Toast.makeText(getApplicationContext(), "Successfully Update Reg Number", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed To Update Your Reg Number", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateInfo() {

        user.put("updateInfo", compName.getText().toString());


        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        docRef.update("employerName", user.get("updateInfo"))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(@NonNull Void unused) {
                        Toast.makeText(getApplicationContext(), "Successfully Update employer Name", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed To Update Your employer Name", Toast.LENGTH_SHORT).show();
                    }
                });


    }
}