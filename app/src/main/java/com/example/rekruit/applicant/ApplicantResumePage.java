package com.example.rekruit.applicant;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.text.Html;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ApplicantResumePage extends AppCompatActivity {

    Button uploadResumeBtn;
    ProgressDialog progressDialog;

    TextView tvUri,tvPath,userNameTV;
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
        databaseReference = FirebaseDatabase.getInstance().getReference("resumeUri");

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

//                            //Get PDF Path
//                            String sPath = sUri.getPath();
//                            //Set Path  on text view
//                            tvPath.setText(Html.fromHtml(
//                                    "<big><b>PDF Path</b></big><br>"+sPath
//                            ));
                            uploadResume();
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
        
       tvUri.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               download();
           }
       });

    }

    private void download() {

        storageReference=FirebaseStorage.getInstance().getReference();


        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("ProfileActivity", "DocumentSnapshot data: " + document.getData());



                        pdfUri = document.getData().get("resumeUri").toString();
//                        new EditProfileActivity.FetchImage(url).start();

                        downloadFile(ApplicantResumePage.this,"Applicant Resume",".pdf", Environment.DIRECTORY_DOWNLOADS,pdfUri);





                    } else {
                        Log.d("ProfileActivity", "No such document");
                    }
                } else {
                    Log.d("ProfileActivity", "get failed with ", task.getException());
                }
            }
        });


    }

    public void downloadFile(Context context,String fileName,String fileExtension, String destinationDirectory, String url){

        DownloadManager downloadManager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory,fileName+fileExtension);

        downloadManager.enqueue(request);

    }

    private void displayResume() {

        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("ProfileActivity", "DocumentSnapshot data: " + document.getData());






                        tvUri.setText(document.getData().get("resumeUri").toString());

                    } else {
                        Log.d("ProfileActivity", "No such document");
                    }
                } else {
                    Log.d("ProfileActivity", "get failed with ", task.getException());
                }
            }
        });
    }

    private void selectPDF() {

        //Initialize intent
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //Set Type
        intent.setType("application/pdf");
        //Launch intent
        resultLauncher.launch(intent);


    }

    private void uploadResume() {

        progressDialog =new ProgressDialog(this);
        progressDialog.setTitle("Uploading file");
        progressDialog.show();
        storageReference = FirebaseStorage.getInstance().getReference();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
        Date now = new Date();
        String fileName = formatter.format(now);
        storageReference = FirebaseStorage.getInstance().getReference("resume/"+ " resume " +fileName+".pdf");
        storageReference.putFile(resumeUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        Toast.makeText(ApplicantResumePage.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                Log.e("URL ", "onSuccess: " + uri);
                                pdfUri = uri.toString();
                                updateResumeUrl();
                            }
                        });
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

                Toast.makeText(ApplicantResumePage.this, "Failed to upload", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateResumeUrl() {
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