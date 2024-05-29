package com.example.dixsurdixgeoffrion.Services;

import android.app.Dialog;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dixsurdixgeoffrion.ListeDepicerie.AjoutAutoAdapter;
import com.example.dixsurdixgeoffrion.ListeDepicerie.MainListeDepicerie;
import com.example.dixsurdixgeoffrion.Models.Aliment;
import com.example.dixsurdixgeoffrion.Models.AlimentAuto;
import com.example.dixsurdixgeoffrion.R;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DialogService{
    public MainListeDepicerie context;
    ServiceEpicerie _serviceEpicerie;
    List<AlimentAuto> listAlimentsAuto; //Recoit tout les aliments automatiques à ajouter dans la bd.
    public Uri imageUri;

    public DialogService(MainListeDepicerie current_activity, ServiceEpicerie serviceEpicerie){
        context = current_activity;
        _serviceEpicerie = serviceEpicerie;
        listAlimentsAuto = new ArrayList<>();
    }

    //Ajoute ou enleve l'aliment à ajouter dans la bd
    public void AjoutAlimentAuto(AlimentAuto aliment){

        if (listAlimentsAuto.contains(aliment)){
            listAlimentsAuto.remove(aliment);
        }else {
            listAlimentsAuto.add(aliment);
        }
    }

    public void showDialogAjoutAutoAliment() {

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.ajout_auto_aliment);

        RecyclerView recyclerView = dialog.findViewById(R.id.recycle_ajout_auto_aliment);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(dialog.getContext());
        recyclerView.setLayoutManager(layoutManager);
        AjoutAutoAdapter ajoutAutoAdapter = new AjoutAutoAdapter();
        recyclerView.setAdapter(ajoutAutoAdapter);
        ajoutAutoAdapter.context = dialog.getContext();
        ajoutAutoAdapter.dialogService = this;

        _serviceEpicerie.GetListAutoAliment(ajoutAutoAdapter);

        dialog.findViewById(R.id.extfab_validate_auto_aliment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (listAlimentsAuto != null && listAlimentsAuto.size() > 0){

                    for (AlimentAuto alimentauto :
                            listAlimentsAuto) {
                        _serviceEpicerie.alimentListAuto.add(
                                new Aliment(
                                        alimentauto.nom,
                                        alimentauto.description,
                                        alimentauto.quantite,
                                        alimentauto.validerAchat,
                                        alimentauto.imageUri,
                                        Date.from(Instant.now())
                                ));
                    }
                    _serviceEpicerie.AjouterAutoAliment();
                }

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

    public void showDialogAjoutAliment() {

        Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.ajout_aliment);

        TextView btn_cancel = dialog.findViewById(R.id.btn_ajtAutoAliment_cancel);
        TextView btn_validate = dialog.findViewById(R.id.btn_ajtAliment_validate);
        TextView tv_quantite = dialog.findViewById(R.id.txt_ajtAliment_Quantite);
        EditText edt_nomAliment = dialog.findViewById(R.id.input_nom_aliment);
        EditText edt_descripAliment = dialog.findViewById(R.id.input_descrip_aliment);
        TextView btn_diminuer = dialog.findViewById(R.id.btn_ajtAliment_diminuer);
        TextView btn_augmenter = dialog.findViewById(R.id.btn_ajtAliment_augmenter);
        ImageView imgvwImageAliment = dialog.findViewById(R.id.image_aliment_ajout);

        //ImageView clickable pour upload l'image pour un nouvel aliment
        imgvwImageAliment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.UploadImageAliment(imgvwImageAliment);
            }
        });

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

        //Le bouton qui envoi le formulaire d'ajout d'un aliment
        btn_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nomAliment = edt_nomAliment.getText().toString();
                String descripAliment = edt_descripAliment.getText().toString();
                int quantite = Integer.valueOf(tv_quantite.getText().toString());
                Aliment nouvelAliment = new Aliment(nomAliment,descripAliment,quantite,false,imageUri.toString(), Date.from(Instant.now()));

                //Execution de la méthode du service
                _serviceEpicerie.AjouterAliment(nouvelAliment,imageUri,false);
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    public void showDialogFullImage(){
        Dialog dialogAfficheImageAlimet = new Dialog(context);
        dialogAfficheImageAlimet.setContentView(R.layout.dialog_show_image_full);
        ImageView imagealiment = dialogAfficheImageAlimet.findViewById(R.id.imgvw_image_alimentfull);
        //  imagealiment.setImageDrawable(context.getDrawable(alimentcourant.Photo));
        dialogAfficheImageAlimet.show();
        dialogAfficheImageAlimet.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogAfficheImageAlimet.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogAfficheImageAlimet.getWindow().setGravity(Gravity.CENTER);
    }
}
