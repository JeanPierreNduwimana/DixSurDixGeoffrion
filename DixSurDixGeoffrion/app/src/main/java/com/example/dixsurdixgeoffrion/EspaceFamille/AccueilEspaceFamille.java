package com.example.dixsurdixgeoffrion.EspaceFamille;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dixsurdixgeoffrion.ListeDepicerie.MainListeDepicerie;
import com.example.dixsurdixgeoffrion.Profile.MainProfileActivity;
import com.example.dixsurdixgeoffrion.databinding.AcceuilEspaceFamilleBinding;

public class AccueilEspaceFamille extends AppCompatActivity {

    AcceuilEspaceFamilleBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AcceuilEspaceFamilleBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setTitle("Espace Famille");
        setContentView(view);

        binding.imgvMainIconIcecream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccueilEspaceFamille.this, MainListeDepicerie.class));
            }
        });


        binding.imgvMainIconProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccueilEspaceFamille.this,MainProfileActivity.class));
            }
        });
    }
}
