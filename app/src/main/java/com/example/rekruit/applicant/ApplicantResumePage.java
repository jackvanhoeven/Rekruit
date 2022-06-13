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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ApplicantResumePage extends AppCompatActivity {

    Button uploadResumeBtn;

    TextView tvUri,tvPath;
    ActivityResultLauncher<Intent> resultLauncher;
    Uri resumeUri;
    String pdfUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    


    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_resume_page);
        tvUri = findViewById(R.id.tv_uri);
        uploadResumeBtn = findViewById(R.id.uploadResumeBtn);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();

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

        StorageReference reference = storageReference.child("uploadPDF"+ System.currentTimeMillis()+".pdf");

        DocumentReference nameRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

       reference.putFile(resumeUri)
               .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                   @Override
                   public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                       Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                       while (!uriTask.isComplete());
                       Uri uri = uriTask.getResult();

                       databaseReference.child(databaseReference.push().getKey()).setValue(pdfUri);
                       Toast.makeText(ApplicantResumePage.this, "File Upload", Toast.LENGTH_SHORT).show();
                   }
               }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

               double progress = (100.0* snapshot.getBytesTransferred())/snapshot.getTotalByteCount();

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