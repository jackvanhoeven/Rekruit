package com.example.rekruit.employer;

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

public class EditOurBenefit extends AppCompatActivity {

    FirebaseFirestore db;
    Map<String, Object> user = new HashMap<>();
    TextView etBenefit;
    Button btnSave;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_our_benefit);

        etBenefit = findViewById(R.id.ourBenefitET);
        btnSave = findViewById(R.id.editOurBenefitBtn);


        db = FirebaseFirestore.getInstance();

        displayInfo();

//        backBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                toEmployerEditProfile();
//            }
//        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateInfo();
            }
        });
    }


    private void updateInfo() {

        user.put("updateInfo", etBenefit.getText().toString());

        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        docRef.update("ourBenefit", user.get("updateInfo"))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(@NonNull Void unused) {
                        Toast.makeText(getApplicationContext(), "Successfull Update About US", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed To Update Your Name", Toast.LENGTH_SHORT).show();
                    }
                });

        Intent intent = new Intent(EditOurBenefit.this, EmployerEditProfile.class);
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
                        Log.d("nak tngok about us", "DocumentSnapshot data: " + doc.getData());
                        etBenefit.setText(doc.getData().get("ourBenefit").toString());

                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Failed to get your name", Toast.LENGTH_SHORT).show();
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