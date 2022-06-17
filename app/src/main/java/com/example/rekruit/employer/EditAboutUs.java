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

public class EditAboutUs extends AppCompatActivity {

    FirebaseFirestore db;
    Map<String, Object> user = new HashMap<>();
    TextView etAboutUs;
    Button btnSave;
    ImageView backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_about_us);

        etAboutUs = findViewById(R.id.etAboutUs);
        btnSave = findViewById(R.id.editAboutUsBtn);
        backBtn = findViewById(R.id.eauBackBtn);

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

        user.put("updateInfo", etAboutUs.getText().toString());

        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        docRef.update("aboutUS", user.get("updateInfo"))
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

        Intent intent = new Intent(EditAboutUs.this, EmployerEditProfile.class);
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
                        etAboutUs.setText(doc.getData().get("aboutUS").toString());

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