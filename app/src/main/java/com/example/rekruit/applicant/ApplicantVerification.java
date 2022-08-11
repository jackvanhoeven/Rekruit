package com.example.rekruit.applicant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rekruit.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ApplicantVerification extends AppCompatActivity {
    ImageView idIV,selfieIV;
    ProgressDialog progressDialog;
    private FirebaseFirestore db;

    //permission constants
    private static final int LOCATION_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;

    //image pick
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;

    private String[] cameraPermissions;
    private String[] storagePermissions;
    private String[] locationPermissions;

    private Uri ic_uri,selfie_uri,image_uri;
    String icUrl,selfieUrl;
    StorageReference icReference,selfieReference;
    int number;
    Button submitBtn;
    Bundle bundle;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_verification);


        //INIT permision array
//        locationPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        firebaseAuth = FirebaseAuth.getInstance();

        idIV = findViewById(R.id.verifyICIV);
        selfieIV = findViewById(R.id.selfieICIV);
        submitBtn = findViewById(R.id.applicantVrfBtn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkSubmissionStatus();
            }
        });

        idIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number = 1;
                showImagePickDialog();
            }
        });

        selfieIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number = 2;
                if(checkCameraPermission()){
                    //camera permission allowed
                    pickFromCamera1();
                }
                else {
                    //not allowed
                    requestCameraPermission();
                }

            }
        });

    }

    private void pickFromCamera1() {

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_Image_Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Image_Description");

        selfie_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, selfie_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    private void checkSubmissionStatus() {
        uploadImage();
    }


    private void showImagePickDialog() {

        //option to show dialog
        String[] options = {"Camera", "Gallery"};
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick image")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        //handle clicks
                        if(which==0) {
                            //camera clicks
                            if(checkCameraPermission()){
                                //camera permission allowed
                                pickFromCamera();
                            }
                            else {
                                //not allowed
                                requestCameraPermission();
                            }
                        }
                        else{
                            //gallery clicked
                            if(checkStoragePermission()){
                                //storage permission allowed
                                pickFromGallery();
                            }
                            else{
                                //not allowed
                                requestStoragePermission();
                            }
                        }
                    }
                })
                .show();




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {




//            if (number == 1) {
//                if (resultCode == RESULT_OK) {
//
//                    if (requestCode == IMAGE_PICK_GALLERY_CODE) {
//                        //IMAGE picked from gallery
//
//                        //save picked image uri
//                        ic_uri = data.getData();
//
//                    }
//                }
//
//            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
//                //IMAGE PICK FROM CAMERA
//                ic_uri = data.getData();
//            }
//
//            if (number == 2) {
//                if (requestCode == IMAGE_PICK_CAMERA_CODE) {
//
//                    selfie_uri = data.getData();
//                }
//            }

        if (resultCode == RESULT_OK) {
            if (number == 1) {
                if(requestCode == IMAGE_PICK_GALLERY_CODE){
                    //IMAGE picked from gallery

                    //save picked image uri
                    ic_uri = data.getData();
                    idIV.setImageURI(ic_uri);

//                //set image
//                userIV.setImageURI(image_uri);
                }
                else if(requestCode == IMAGE_PICK_CAMERA_CODE){
                    //IMAGE PICK FROM CAMERA
                    idIV.setImageURI(ic_uri);

                }

            }

            if (number == 2) {
                if (requestCode == IMAGE_PICK_CAMERA_CODE) {

//                    selfie_uri = data.getData();
                    selfieIV.setImageURI(selfie_uri);
                }
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
        
    }

    private void uploadImage() {

        if(ic_uri == null || selfie_uri == null )
        {
            Toast.makeText(this, "Please upload necessary document", Toast.LENGTH_SHORT).show();
        }
        else {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading file");
            progressDialog.show();
            icReference = FirebaseStorage.getInstance().getReference();
            selfieReference = FirebaseStorage.getInstance().getReference();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
            Date now = new Date();
            String fileName = formatter.format(now);
            icReference = FirebaseStorage.getInstance().getReference("images/" + " icPic " + fileName);
            selfieReference = FirebaseStorage.getInstance().getReference("images/" + " selfiePic " + fileName);
            icReference.putFile(ic_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            Toast.makeText(ApplicantVerification.this, "Successfully Uploaded IC", Toast.LENGTH_SHORT).show();
                            icReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    Log.e("URL ", "onSuccess: " + uri);
                                    icUrl = uri.toString();

                                    selfieReference.putFile(selfie_uri)
                                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    Toast.makeText(ApplicantVerification.this, "Successfully Uploaded Selfie", Toast.LENGTH_SHORT).show();
                                                    selfieReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            Log.e("URL", "on success: " + uri);
                                                            selfieUrl = uri.toString();

                                                        }
                                                    });
                                                }
                                            });

                                }
                            });
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            Toast.makeText(ApplicantVerification.this, "Failed to upload", Toast.LENGTH_SHORT).show();
                        }
                    });

        }

    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this
                ,Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);

        return result;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermissions
                , CAMERA_REQUEST_CODE);
    }

    private void pickFromCamera() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_Image_Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Image_Description");


        ic_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, ic_uri);


        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this
                ,Manifest.permission.CAMERA) ==
                (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(this
                ,Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);


        return result && result1;
    }
}