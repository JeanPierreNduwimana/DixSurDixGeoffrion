package com.example.dixsurdixgeoffrion.Profile;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dixsurdixgeoffrion.databinding.ProfilePublicActivityBinding;

public class ProfilePublicActivity extends AppCompatActivity {

    ProfilePublicActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ProfilePublicActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }
}
