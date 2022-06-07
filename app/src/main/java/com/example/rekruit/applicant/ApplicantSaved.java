package com.example.rekruit.applicant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rekruit.R;
import com.example.rekruit.model.Application;
import com.example.rekruit.model.Job;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import adapter.ApplicationRVAdapter;
import adapter.FilterJobAdapter;
import adapter.MyAdapter;
import adapter.SavedJobRVAdapter;


public class ApplicantSaved extends Fragment implements  SavedJobRVAdapter.ItemClickListener{



    private RecyclerView savedRV;
    private ArrayList<Job> savedJobList;
    private SavedJobRVAdapter savedJobRVAdapter;
    private FirebaseFirestore db;
    ProgressBar loadingPB;


    private String jobID,saveStatus;
    private EditText searchJobET;
    private TextView filterJobTV;



    ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_applicant_saved, container, false);



        // initializing our variables.
        savedRV = view.findViewById(R.id.rvSaved);
        loadingPB = view.findViewById(R.id.idProgressBar);

        // initializing our variable for firebase
        // firestore and getting its instance.
        db = FirebaseFirestore.getInstance();

        // creating our new array list
        savedJobList = new ArrayList<>();
        savedRV.setHasFixedSize(true);
        savedRV.setLayoutManager(new LinearLayoutManager(getContext()));

        // adding our array list to our recycler view adapter class.
        savedJobRVAdapter = new SavedJobRVAdapter(savedJobList, getContext());

        // enable setclicklistener to recyclerview
        savedJobRVAdapter.setClickListener(this);

        // setting adapter to our recycler view.
        savedRV.setAdapter(savedJobRVAdapter);


        // below line is use to get the data from Firebase Firestore.
        // previously we were saving data on a reference of Courses
        // now we will be getting the data from the same reference.
        db.collection("savedJob")
                .whereEqualTo("applicantID", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // after getting the data we are calling on success method
                        // and inside this method we are checking if the received
                        // query snapshot is empty or not.
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // if the snapshot is not empty we are
                            // hiding our progress bar and adding
                            // our data in a list.

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                // after getting this list we are passing
                                // that list to our object class.
                               Job c = d.toObject(Job.class);

                                // and we will pass this object class
                                // inside our arraylist which we have
                                // created for recycler view.
                                savedJobList.add(c);
                            }
                            // after adding the data to recycler view.
                            // we are calling recycler view notifuDataSetChanged
                            // method to notify that data has been changed in recycler view.
                            savedJobRVAdapter.notifyDataSetChanged();
                        } else {
                            // if the snapshot is empty we are displaying a toast message.
                            loadingPB.setVisibility(View.GONE);

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // if we do not get any data or any error we are displaying
                // a toast message that we do not get any data
                Toast.makeText(view.getContext(), "Fail to get the data.", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }


    @Override
    public void onItemClick(View view, int position) {
        String jobID = savedJobRVAdapter.getItem(position).getJobID();
        Intent intent = new Intent(getContext(),ApplicantJobDescriptionPage.class);
        intent.putExtra("jobID",jobID);
        startActivity(intent);





    }
}