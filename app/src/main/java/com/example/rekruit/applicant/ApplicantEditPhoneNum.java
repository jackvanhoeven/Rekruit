package com.example.rekruit.applicant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rekruit.R;
import com.example.rekruit.employer.EditMission;
import com.example.rekruit.employer.EmployerEditProfile;
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

public class ApplicantEditPhoneNum extends AppCompatActivity {
    FirebaseFirestore db;
    Map<String, Object> user = new HashMap<>();
    TextView etPhoneNum;
    Button btnSave;
    ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_edit_phone_num);

        etPhoneNum = findViewById(R.id.applicantPhoneNumEt);
        btnSave = findViewById(R.id.editPhoneNumBtn);

        db = FirebaseFirestore.getInstance();

        displayInfo();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateInfo();
            }
        });

    }

    private void updateInfo() {

        user.put("updateInfo", etPhoneNum.getText().toString());

        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        docRef.update("phoneNum", user.get("updateInfo"))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(@NonNull Void unused) {
                        Toast.makeText(getApplicationContext(), "Successfully Update Phone Number", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed To Update Your Phone Number", Toast.LENGTH_SHORT).show();
                    }
                });

        Intent intent = new Intent(ApplicantEditPhoneNum.this, ApplicantAccountPage.class);
        startActivity(intent);
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
                        etPhoneNum.setText(doc.getData().get("phoneNum").toString());

                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Failed to get your phone Num", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Log.d("Failed", "get failed with ", task.getException());
                }
            }
        });
    }
}