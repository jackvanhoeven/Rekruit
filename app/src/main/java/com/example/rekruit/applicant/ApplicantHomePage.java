package com.example.rekruit.applicant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rekruit.R;
import com.example.rekruit.model.Constant;
import com.example.rekruit.model.Job;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import com.example.rekruit.adapter.FilterJobAdapter;
import com.example.rekruit.adapter.MyAdapter;

public class ApplicantHomePage extends AppCompatActivity {



    RecyclerView recyclerView, filterRecyclerView;

    public ArrayList<Job> list,filterJobList;

    FirebaseFirestore db;
    private ImageView filterJobBtn;

    private String jobID;
    private EditText searchJobET;
    private TextView filterJobTV;
    MyAdapter adapter;
    FilterJobAdapter filterJobAdapter;

    ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_home_page);

//        searchJobET = findViewById(R.id.searchJobEt);
        filterJobBtn = findViewById(R.id.filterBtn);
        filterJobTV = findViewById(R.id.filterJobTV);


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data.....");
        progressDialog.show();
        progressDialog.dismiss();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        filterRecyclerView = findViewById(R.id.filterRecyclerView);
        filterRecyclerView.setHasFixedSize(true);
        filterRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        list = new ArrayList<Job>();//job list
        filterJobList = new ArrayList<Job>();//job list
        adapter = new MyAdapter(this,list);
        filterJobAdapter = new FilterJobAdapter(this,filterJobList);
        recyclerView.setAdapter(adapter);
        filterRecyclerView.setAdapter(filterJobAdapter);



//        jobID = getIntent().getStringExtra("jobID");

        EventChangeListener();//to list available job using recyclerview



        //Iniatialize and Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.homeNav);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.homeNav:

                        return true;
                    case R.id.jobNav:
                        startActivity(new Intent(getApplicationContext()
                                ,job_list.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.accountNav:
                        startActivity(new Intent(getApplicationContext()
                                ,ApplicantAccountPage.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });









        filterJobBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                filterJobAdapter.clear();

                jobCategoryDialog();





            }
        });


    }

    private void jobCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Job Specialization")
                .setItems(Constant.jobCategory, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String selected = Constant.jobCategory[which];



                        //set pick category
                        filterJobTV.setText(selected);
                        Log.e("Show something",selected);



                        if(selected.equals("All")){


                            recyclerView.setVisibility(View.VISIBLE);

                        }
                        else{
                            recyclerView.setVisibility(View.GONE);

                            db.collection("Jobs")
                                    .whereEqualTo("jobCategory",selected)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                            if(error != null){

                                                if(progressDialog.isShowing())
                                                    progressDialog.dismiss();

                                                Log.e("Firestore Error",error.getMessage());
                                                return;
                                            }

                                            for (DocumentChange dc : value.getDocumentChanges()){
                                                if (dc.getType()==DocumentChange.Type.ADDED){


                                                    filterJobList.add(dc.getDocument().toObject(Job.class));



                                                }

                                                filterJobAdapter.notifyDataSetChanged();{

                                                    if(progressDialog.isShowing())
                                                        progressDialog.dismiss();
                                                }
                                            }
                                        }
                                    });

                        }

                    }
                })
                .show();
    }

    private void clearData() {
    }


//    public void onItemClick(View view, int position) {
//        uid = recyclerView.getItem(position).getUid();
//        Log.e("ManageUser,  ", "Uid: " + uid);
//        Toast.makeText(getApplicationContext()," Uid: " + uid, Toast.LENGTH_SHORT).show();
//
//
//
//    }

    private void EventChangeListener() {
//        DocumentReference docRef = db.collection("Jobs").document(jobID);

        db.collection("Jobs")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){

                            if(progressDialog.isShowing())
                                progressDialog.dismiss();

                            Log.e("Firestore Error",error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()){
                            if (dc.getType()==DocumentChange.Type.ADDED){

                                list.add(dc.getDocument().toObject(Job.class));
                            }

                            adapter.notifyDataSetChanged();{

                                if(progressDialog.isShowing())
                                    progressDialog.dismiss();
                            }
                        }
                    }
                });
    }
}