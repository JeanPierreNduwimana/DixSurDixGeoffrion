package com.example.dixsurdixgeoffrion.ListeDepicerie;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dixsurdixgeoffrion.Models.Aliment;
import com.example.dixsurdixgeoffrion.R;
import com.example.dixsurdixgeoffrion.databinding.MainListeDepicerieBinding;

public class MainListeDepicerie extends AppCompatActivity {

    MainListeDepicerieBinding binding;
    EpicerieAdapter epicerieAdapter;
    Boolean isExpanded = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MainListeDepicerieBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setTitle("Liste d'épicerie");

        setContentView(view);


        binding.extfabAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAjoutAutoAliment();
                shrinkFab();
               // Toast.makeText(MainListeDepicerie.this,"btn extented auto clicked", Toast.LENGTH_SHORT).show();

            }
        });
        binding.extfabManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAjoutAliment();
                shrinkFab();

            }
        });
        binding.fabAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialogAjoutAutoAliment();
                shrinkFab();
                //Toast.makeText(MainListeDepicerie.this,"btn auto clicked", Toast.LENGTH_SHORT).show();

            }
        });
        binding.fabManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAjoutAliment();
                shrinkFab();
                //Toast.makeText(MainListeDepicerie.this,"btn manual clicked", Toast.LENGTH_SHORT).show();

            }
        });
        binding.fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isExpanded){
                    shrinkFab();
                } else{
                    expandFab();
                }
            }
        });

        initRecycler();
        remplirRecycler();

    }

    private void remplirRecycler() {

        for(int i = 0; i < 40; i++){

            epicerieAdapter.listAliment.add(new Aliment("Aliment" + i,"Ceci est la Description de l'alimaent duméro: " + i,"Key" + i,i,false));
        }
    }
    private void initRecycler() {

        RecyclerView recyclerView = findViewById(R.id.recycle_liste_epicerie);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        epicerieAdapter = new EpicerieAdapter();

        recyclerView.setAdapter(epicerieAdapter);
        epicerieAdapter.context = this;
    }

    private void showDialogAjoutAutoAliment() {

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.ajout_auto_aliment);

        RecyclerView recyclerView = dialog.findViewById(R.id.recycle_ajout_auto_aliment);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(dialog.getContext());
        recyclerView.setLayoutManager(layoutManager);
        AjoutAutoAdapter ajoutAutoAdapter = new AjoutAutoAdapter();
        recyclerView.setAdapter(ajoutAutoAdapter);
        ajoutAutoAdapter.context = dialog.getContext();

        for(int i = 0; i < 10; i++){

            ajoutAutoAdapter.listAliment.add(new Aliment("Aliment " + i,"Ceci est l'aliment numero " + i, "key",0,false));
        }

        dialog.findViewById(R.id.extfab_ajouter_auto_aliment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btn_ajtAutoAliment_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
    private void showDialogAjoutAliment() {

        Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.ajout_aliment);

        TextView btn_cancel = dialog.findViewById(R.id.btn_ajtAutoAliment_cancel);
        TextView btn_validate = dialog.findViewById(R.id.btn_ajtAliment_validate);
        TextView tv_quantite = dialog.findViewById(R.id.txt_ajtAliment_Quantite);
        TextView btn_diminuer = dialog.findViewById(R.id.btn_ajtAliment_diminuer);
        TextView btn_augmenter = dialog.findViewById(R.id.btn_ajtAliment_augmenter);

        btn_augmenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantite = Integer.valueOf(tv_quantite.getText().toString());

                if (quantite < 20){
                    quantite = quantite + 1;
                    tv_quantite.setText(""+quantite);

                }
            }
        });

        btn_diminuer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int quantite = Integer.valueOf(tv_quantite.getText().toString());

                if (quantite > 0){
                    quantite = quantite - 1;
                    tv_quantite.setText(""+quantite);
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            dialog.dismiss();
            }
        });

        btn_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }
    public void shrinkFab() {


        binding.fabAdd.startAnimation(rotateAntiClockWiseFabAnim());
        //binding.fabAdd.setBackgroundTintList(this.getResources().getColorStateList(R.color.purple_200, null));
        binding.lytFabAuto.startAnimation(toBottomFabAnim());
        binding.lytFabManual.startAnimation(toBottomFabAnim());
        binding.fabManual.setEnabled(false);
        binding.fabAuto.setEnabled(false);
        binding.extfabManual.setEnabled(false);
        binding.extfabAuto.setEnabled(false);

        isExpanded = !isExpanded;
    }
    public void expandFab() {

        binding.fabManual.setEnabled(true);
        binding.fabAuto.setEnabled(true);
        binding.extfabManual.setEnabled(true);
        binding.extfabAuto.setEnabled(true);

        binding.fabAdd.startAnimation(rotateClockWiseFabAnim());
        //binding.fabAdd.setBackgroundTintList(this.getResources().getColorStateList(R.color.purple_200, null));
        binding.lytFabAuto.startAnimation(fromBottomFabAnim());
        binding.lytFabManual.startAnimation(fromBottomFabAnim());
        isExpanded = !isExpanded;
    }
    private Animation fromBottomFabAnim (){
        return AnimationUtils.loadAnimation(this, R.anim.from_bottom);
    }
    private Animation toBottomFabAnim (){
        return AnimationUtils.loadAnimation(this, R.anim.to_bottom);
    }
    private Animation rotateClockWiseFabAnim (){
        return AnimationUtils.loadAnimation(this, R.anim.rotate_clockwise);
    }
    private Animation rotateAntiClockWiseFabAnim (){
        return AnimationUtils.loadAnimation(this, R.anim.rotate_anticlockwise);
    }
}
