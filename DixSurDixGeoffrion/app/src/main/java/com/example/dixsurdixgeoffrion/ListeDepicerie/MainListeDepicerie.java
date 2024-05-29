package com.example.dixsurdixgeoffrion.ListeDepicerie;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dixsurdixgeoffrion.Models.Aliment;
import com.example.dixsurdixgeoffrion.R;
import com.example.dixsurdixgeoffrion.Services.DialogService;
import com.example.dixsurdixgeoffrion.Services.ServiceEpicerie;
import com.example.dixsurdixgeoffrion.databinding.MainListeDepicerieBinding;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainListeDepicerie extends AppCompatActivity {

    MainListeDepicerieBinding binding;
    EpicerieAdapter epicerieAdapter;
    Boolean isExpanded = false;
    List<Integer> listimages = new ArrayList<>(); //images pour le diaporama
    DialogService dialogService;
    ServiceEpicerie _serviceEpicerie;
    ImageView imageView;

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    if (uri != null){
                        imageView.setImageURI(uri);
                        dialogService.imageUri = uri;
                    }
                }
            });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MainListeDepicerieBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setTitle("Liste d'épicerie");
        setContentView(view);
        setListimages(); //images pour le diaporama
        _serviceEpicerie = new ServiceEpicerie(MainListeDepicerie.this);
        dialogService = new DialogService(MainListeDepicerie.this,_serviceEpicerie);

        //region BINDINGS

        binding.extfabAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogService.showDialogAjoutAutoAliment();
                shrinkFab();
            }
        });
        binding.extfabManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogService.showDialogAjoutAliment();
                shrinkFab();
            }
        });
        binding.fabAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogService.showDialogAjoutAutoAliment();
                shrinkFab();
            }
        });
        binding.fabManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogService.showDialogAjoutAliment();
                shrinkFab();

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

        //endregion

        initRecycler();
    }

    public void UploadImageAliment(ImageView imageView){
        mGetContent.launch("image/*");
        this.imageView = imageView;
    }

    public void remplirRecycler(List<Aliment> listaliment) {
        epicerieAdapter.listAliment.clear();
        epicerieAdapter.listAliment.add(null);

        //Mettre la liste d'aliment en ordre par date avant de l'ajouter au recycleview
        listaliment.sort(Comparator.comparing(Aliment::getDateAjout));

        //Ajout au recycleview
        epicerieAdapter.listAliment.addAll(listaliment);
        epicerieAdapter.notifyDataSetChanged();
    }
    private void initRecycler() {

        RecyclerView recyclerView = findViewById(R.id.recycle_liste_epicerie);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        epicerieAdapter = new EpicerieAdapter();
        recyclerView.setAdapter(epicerieAdapter);
        epicerieAdapter.context = this;
        epicerieAdapter.listimages = this.listimages;
        _serviceEpicerie.GetListAlimentManuel();
    }


    //region ANIMATIONS & FLOAT ACTION BUTTON

    //Ces images servent à mettre en marche le diaporama
    private void setListimages() {
        listimages.add(R.drawable.banane);
        listimages.add(R.drawable.patates);
        listimages.add(R.drawable.pommes);
        listimages.add(R.drawable.raisins);
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

        binding.fabManual.setClickable(false);
        binding.fabAuto.setClickable(false);
        binding.extfabManual.setClickable(false);
        binding.extfabAuto.setClickable(false);

        isExpanded = !isExpanded;
    }
    public void expandFab() {

        binding.fabManual.setEnabled(true);
        binding.fabAuto.setEnabled(true);
        binding.extfabManual.setEnabled(true);
        binding.extfabAuto.setEnabled(true);

        binding.fabManual.setClickable(true);
        binding.fabAuto.setClickable(true);
        binding.extfabManual.setClickable(true);
        binding.extfabAuto.setClickable(true);

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

    //endregion
}
