package com.example.rekruit.adapter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.rekruit.R;

public class EmployerVerificationAdapter extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_verification_adapter);
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}