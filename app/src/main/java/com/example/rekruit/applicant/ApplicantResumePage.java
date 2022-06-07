package com.example.rekruit.applicant;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rekruit.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ApplicantResumePage extends AppCompatActivity {

    Button uploadResumeBtn;

    TextView tvUri,tvPath;
    ActivityResultLauncher<Intent> resultLauncher;
    Uri resumeUri;
    String pdfUri;
    


    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_resume_page);
        tvUri = findViewById(R.id.tv_uri);
        uploadResumeBtn = findViewById(R.id.uploadResumeBtn);

        resultLauncher= registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        //Iniatilize result data
                        Intent data = result.getData();
                        //Check condition
                        if(data !=null ){
                            //When data is not equal to empty
                             //Get pdf uri
                            resumeUri = data.getData();
                            pdfUri = resumeUri.toString();
                            //Set uri on textview
                            tvUri.setText(Html.fromHtml(
                                    "<big><b>PDF Uri</b></big><br>"+resumeUri
                            ));
//                            //Get PDF Path
//                            String sPath = sUri.getPath();
//                            //Set Path  on text view
//                            tvPath.setText(Html.fromHtml(
//                                    "<big><b>PDF Path</b></big><br>"+sPath
//                            ));
                        }
                    }
                });

        uploadResumeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivityCompat.checkSelfPermission(ApplicantResumePage.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED){
                    //When permission is not granted
                    //Request permission
                    ActivityCompat.requestPermissions(ApplicantResumePage.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

                }else{
                    //When Pemission is granted
                    //Create method
                    selectPDF();
                }

            }
        });
        
//        displayResume();

    }

    private void displayResume() {
    }

    private void selectPDF() {

        //Initialize intent
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //Set Type
        intent.setType("application/pdf");
        //Launch intent
        resultLauncher.launch(intent);

        uploadResume();
    }

    private void uploadResume() {

        DocumentReference nameRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        nameRef
                .update("resumeUri", pdfUri)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("EditName", "DocumentSnapshot successfully updated!");

                       /* Intent intent = new Intent(getApplicationContext(),customerProfile.class);
                        startActivity(intent);*/

                        Toast.makeText(ApplicantResumePage.this, "Profile Picture updated successfully.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("EditName", "Error updating document", e);
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Check condition
        if(requestCode == 1&& grantResults.length>0 &&grantResults[0]==
                PackageManager.PERMISSION_GRANTED){
            //When permisision is granted
            //call method
            selectPDF();
        }else{
            //When Permission is denied
            //Display toast
            Toast.makeText(getApplicationContext(),
                    "Permission Denied. ",Toast.LENGTH_SHORT).show();
        }
    }
}