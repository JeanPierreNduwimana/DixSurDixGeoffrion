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

import com.example.dixsurdixgeoffrion.R;
import com.example.dixsurdixgeoffrion.Services.DialogService;
import com.example.dixsurdixgeoffrion.Services.ServiceEpicerie;
import com.example.dixsurdixgeoffrion.databinding.MainListeDepicerieBinding;

import java.util.ArrayList;
import java.util.List;

public class MainListeDepicerie extends AppCompatActivity {

    MainListeDepicerieBinding binding;
    EpicerieAdapter epicerieAdapter;
    Boolean isExpanded = false;
    List<Integer> listimages = new ArrayList<>();
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
        setListimages();
        _serviceEpicerie = new ServiceEpicerie(MainListeDepicerie.this);
        dialogService = new DialogService(MainListeDepicerie.this,_serviceEpicerie);


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

        initRecycler();
        remplirRecycler();

    }

    public void UploadImageAliment(ImageView imageView){
        mGetContent.launch("image/*");
        this.imageView = imageView;
    }

    private void setListimages() {
        listimages.add(R.drawable.banane);
        listimages.add(R.drawable.patates);
        listimages.add(R.drawable.pommes);
        listimages.add(R.drawable.raisins);
    }

    private void remplirRecycler() {
        epicerieAdapter.listAliment.add(null);


        /*for(int i = 1; i < 40; i++){
            int randomNum = ThreadLocalRandom.current().nextInt(0, 3 + 1);
            epicerieAdapter.listAliment.add(new Aliment("Aliment" + i,"Ceci est la Description de l'alimaent duméro: " + i,"Key" + i,i,false,listimages.get(randomNum)));
        }*/
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
