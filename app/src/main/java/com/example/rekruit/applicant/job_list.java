package com.example.rekruit.applicant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.rekruit.R;
import com.example.rekruit.model.Job;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import adapter.MyAdapter;

public class job_list extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Job> list;

    FirebaseFirestore db;

    MyAdapter adapter;

    ProgressDialog progressDialog;

    TabLayout tabLayout;
    ViewPager viewPager;
    TabLayoutAdapter tabLayoutAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);

        //Iniatialize and Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.jobNav);

        tabLayout  = findViewById(R.id.tab_Layout);
        viewPager = findViewById(R.id.view_pager);

        //Initialize adapter
        tabLayoutAdapter = new TabLayoutAdapter(getSupportFragmentManager());
        //add fragment
        tabLayoutAdapter.AddFragment(new ApplicantApplied(),"Applied");
        tabLayoutAdapter.AddFragment(new ApplicantSaved(),"Saved");
//        adapter.AddFragment(new three(),"three");

        //set Adapter
        viewPager.setAdapter(tabLayoutAdapter);
        //connect tablayout with view pager
        tabLayout.setupWithViewPager(viewPager);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.homeNav:
                        startActivity(new Intent(getApplicationContext()
                                ,ApplicantHomePage.class));
                        overridePendingTransition(0,0);

                        return true;
                    case R.id.jobNav:

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


    private class TabLayoutAdapter extends FragmentPagerAdapter {

        //Initialize array list
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        ArrayList<String> stringArrayList = new ArrayList<>();

        //Create constructor
        public void AddFragment(Fragment fragment, String s){
            //Add fragment
            fragmentArrayList.add(fragment);
            //Add string
            stringArrayList.add(s);
        }

        public TabLayoutAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            //Return fragment position
            return fragmentArrayList.get(position);
        }

        @Override
        public int getCount() {
            //Return fragment list size
            return fragmentArrayList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            //Return tab title
            return stringArrayList.get(position);
        }
    }

}