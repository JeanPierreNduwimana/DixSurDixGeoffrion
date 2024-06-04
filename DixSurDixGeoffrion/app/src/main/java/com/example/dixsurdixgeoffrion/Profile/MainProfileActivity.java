package com.example.dixsurdixgeoffrion.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dixsurdixgeoffrion.ListeDepicerie.MainListeDepicerie;
import com.example.dixsurdixgeoffrion.EspaceFamille.AccueilEspaceFamille;
import com.example.dixsurdixgeoffrion.databinding.MainProfileActivityBinding;

public class MainProfileActivity extends AppCompatActivity {

    MainProfileActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MainProfileActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setTitle("Profile");
        setContentView(view);

        binding.imgvMainIconHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainProfileActivity.this, AccueilEspaceFamille.class));
            }
        });

        binding.imgvMainIconIcecream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainProfileActivity.this, MainListeDepicerie.class));
            }
        });
    }
}
