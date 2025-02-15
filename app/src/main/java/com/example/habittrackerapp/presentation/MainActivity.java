package com.example.habittrackerapp.presentation;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.habittrackerapp.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
