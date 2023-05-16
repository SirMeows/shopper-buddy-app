package com.he.engelund;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.he.engelund.databinding.ActivityMainBinding;

public class Main extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);

         binding = ActivityMainBinding.inflate(getLayoutInflater());
         setContentView(binding.getRoot());

    }

}