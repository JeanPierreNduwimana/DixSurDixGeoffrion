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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.dixsurdixgeoffrion.Models.Aliment;
import com.example.dixsurdixgeoffrion.R;
import com.example.dixsurdixgeoffrion.Services.DialogService;
import com.example.dixsurdixgeoffrion.Services.ServiceEpicerie;
import com.example.dixsurdixgeoffrion.databinding.MainListeDepicerieBinding;

import java.util.ArrayList;
import java.util.Comparator;
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

        binding.swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                _serviceEpicerie.GetListAliment();
            }
        });
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
        binding.btnEffacerListe.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String dialogMessage =
                        "Êtes vous sûr d'effacer toute la liste? \n" +
                        "Cette liste sera effacée pour tout le monde.";
                dialogService.InitDialogOuiOuNon(dialogMessage);
                dialogService.btn_Rep_Oui_dialog_OuiNon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        _serviceEpicerie.SupprimerToutLesAliments(epicerieAdapter.listAliment);
                        dialogService.dialogOuiOuNon.dismiss();
                    }
                });
                dialogService.dialogOuiOuNon.show();

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
        if (listaliment.size() == 0){
            binding.btnEffacerListe.setVisibility(View.INVISIBLE);
            binding.btnEffacerListe.setClickable(false);
        }else{
            binding.btnEffacerListe.setVisibility(View.VISIBLE);
            binding.btnEffacerListe.setClickable(true);
        }

        epicerieAdapter.listAliment.clear();
        epicerieAdapter.listAliment.add(null);
        listaliment.sort(Comparator.comparing(Aliment::getDateAjout)); //liste d'aliments en ordre par date
        epicerieAdapter.listAliment.addAll(listaliment); //Ajout au recycleview
        epicerieAdapter.notifyDataSetChanged();
        binding.swiperefresh.setRefreshing(false);

    }
    private void initRecycler() {
        RecyclerView recyclerView = findViewById(R.id.recycle_liste_epicerie);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        epicerieAdapter = new EpicerieAdapter();
        recyclerView.setAdapter(epicerieAdapter);

        //Init des propriétés public de l'adapter
        epicerieAdapter.context = this;
        epicerieAdapter.listimages = this.listimages;
        epicerieAdapter.dialogService = dialogService;
        epicerieAdapter._serviceEpicerie = _serviceEpicerie;

        //Appel du service
        _serviceEpicerie.GetListAliment();
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
