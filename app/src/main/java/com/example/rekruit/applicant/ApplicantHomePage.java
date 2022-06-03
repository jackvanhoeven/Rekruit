package com.example.rekruit.applicant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.rekruit.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ApplicantHomePage extends AppCompatActivity {



    RecyclerView recyclerView;
    ArrayList<Job> list;

    FirebaseFirestore db;

    private String jobID;
    MyAdapter adapter;

    ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_home_page);



        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data.....");
        progressDialog.show();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        list = new ArrayList<Job>();//job list
        adapter = new MyAdapter(this,list);
        recyclerView.setAdapter(adapter);


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