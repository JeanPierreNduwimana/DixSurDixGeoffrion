package com.example.dixsurdixgeoffrion.Services;

import android.app.Dialog;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dixsurdixgeoffrion.ListeDepicerie.AjoutAutoAdapter;
import com.example.dixsurdixgeoffrion.ListeDepicerie.EpicerieAdapter;
import com.example.dixsurdixgeoffrion.ListeDepicerie.MainListeDepicerie;
import com.example.dixsurdixgeoffrion.Models.Aliment;
import com.example.dixsurdixgeoffrion.Models.AlimentAuto;
import com.example.dixsurdixgeoffrion.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DialogService{
    public MainListeDepicerie context;
    public Dialog dialogOuiOuNon; //Est utilisé pour initialiser le dialog à reponse Oui ou Non
    public Dialog dialogLoadingWaiting; //Est utilisé pour l'attente durant les requetes
    public Button btn_Rep_Oui_dialog_OuiNon;//Est utilisé par d'autres classe pour les actions à faire quand la réponse est Oui
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
        recyclerView.setVisibility(View.GONE);
        ProgressBar progressBar = dialog.findViewById(R.id.progressAutoAliment);
        progressBar.setVisibility(View.VISIBLE);

        _serviceEpicerie.GetListAutoAliment(ajoutAutoAdapter,recyclerView, progressBar);

        dialog.findViewById(R.id.extfab_validate_auto_aliment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (listAlimentsAuto != null && listAlimentsAuto.size() > 0){

                    for (AlimentAuto alimentauto : listAlimentsAuto) {

                        alimentauto.used = true;
                        _serviceEpicerie.UpdateUsedAutoAliment(alimentauto);

                        _serviceEpicerie.alimentListAuto.add(
                                new Aliment(
                                        alimentauto.nom,
                                        alimentauto.description,
                                        alimentauto.quantite,
                                        alimentauto.validerAchat,
                                        alimentauto.imageUri,
                                        Date.from(Instant.now()),
                                        true
                                ));
                    }
                    listAlimentsAuto.clear();
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
    public void showDialogAjoutAliment(Aliment aliment) {

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

        if (aliment != null){
            btn_validate.setText("Modifier");
            btn_validate.setTextColor(context.getColor(R.color.yellow));
            edt_nomAliment.setText(aliment.nom);
            edt_descripAliment.setText(aliment.description);
            tv_quantite.setText(String.valueOf(aliment.quantite));
            Picasso.get().load(aliment.imageUri).into(imgvwImageAliment);
        }

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

                if (nomAliment.equals("")){
                    edt_nomAliment.setError("Ce champs ne peut pas être vide");
                    return;
                }

                if (aliment != null){
                    aliment.nom = nomAliment;
                    aliment.description = descripAliment;
                    aliment.quantite = quantite;
                    aliment.dateAjout = Date.from(Instant.now());
                    //Si l'image a été modifier ou pas
                    if (imageUri != null){
                        _serviceEpicerie.UpdateAliment(aliment,imageUri);
                    }else{
                        _serviceEpicerie.UpdateAliment(aliment);
                        _serviceEpicerie.GetListAliment();
                    }
                }else{
                    if (imageUri == null){
                        Aliment nouvelAliment = new Aliment(nomAliment,descripAliment,quantite,false,null, Date.from(Instant.now()),false);
                        //Execution de la méthode du service
                        _serviceEpicerie.AjouterAlimentSansImage(nouvelAliment);

                    }else{
                        Aliment nouvelAliment = new Aliment(nomAliment,descripAliment,quantite,false,imageUri.toString(), Date.from(Instant.now()),false);
                        //Execution de la méthode du service
                        _serviceEpicerie.AjouterAliment(nouvelAliment,imageUri);
                    }
                }

                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }
    public void showDialogFullImage(Aliment aliment, AlimentAuto alimentAuto){
        Dialog dialogAfficheImageAlimet = new Dialog(context);
        dialogAfficheImageAlimet.setContentView(R.layout.dialog_show_image_full);
        ImageView imagealiment = dialogAfficheImageAlimet.findViewById(R.id.imgvw_image_alimentfull);
        if (aliment != null) {
            Picasso.get().load(aliment.imageUri).into(imagealiment);
        }else{
            Picasso.get().load(alimentAuto.imageUri).into(imagealiment);
        }

        dialogAfficheImageAlimet.show();
        dialogAfficheImageAlimet.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogAfficheImageAlimet.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogAfficheImageAlimet.getWindow().setGravity(Gravity.CENTER);
    }
    public void InitDialogOuiOuNon(String Message, String ImageUri){
        dialogOuiOuNon = new Dialog(context);
        dialogOuiOuNon.setContentView(R.layout.dialog_yes_not);
        TextView messageDialogOuiNon = dialogOuiOuNon.findViewById(R.id.txt_dialog_question_oui_non);
        ImageView imageDialogOuiNon = dialogOuiOuNon.findViewById(R.id.imgv_dialog_question_oui_non);
        btn_Rep_Oui_dialog_OuiNon = dialogOuiOuNon.findViewById(R.id.btn_dialog_rep_oui);
        messageDialogOuiNon.setText(Message);
        if (ImageUri != null){
            Picasso.get().load(ImageUri).into(imageDialogOuiNon);
        }

        dialogOuiOuNon.findViewById(R.id.btn_dialog_rep_non).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogOuiOuNon.dismiss();
            }
        });
        dialogOuiOuNon.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogOuiOuNon.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogOuiOuNon.getWindow().setGravity(Gravity.CENTER);

    }

    /*INFO: les propriètés imgbutton, txtview, view ou adapter servent à changer l'état de l'item
        dans la liste principale du recycleview par apport à l'état de validitié de l'aliment. */
    public void showDialogDetailsAliment(Aliment aliment, ImageButton imgbtnValiderAliment, TextView tvMessageAchete, View itemView, EpicerieAdapter epicerieAdapter){

        Dialog dialog_dtlsAliment = new Dialog(context);
        dialog_dtlsAliment.setContentView(R.layout.dialog_details_aliment);
        TextView tv_dialog_details_description= dialog_dtlsAliment.findViewById(R.id.txt_dtlsALiment_description);
        Button btn_dialog_details_validate = dialog_dtlsAliment.findViewById(R.id.btn_dtlsAliment_validate);
        Button btn_dialog_details_annulerAchat = dialog_dtlsAliment.findViewById(R.id.btn_dtlsAliment_annuler_achat);
        TextView btn_dialog_details_delete = dialog_dtlsAliment.findViewById(R.id.btn_dtlsAlimenent_delete);
        TextView btn_dialog_details_cancel = dialog_dtlsAliment.findViewById(R.id.dialog_details_cancel);
        TextView tv_dateAjoutAliment = dialog_dtlsAliment.findViewById(R.id.date_AjoutAliment);
        TextView tv_nomAliment = dialog_dtlsAliment.findViewById(R.id.txt_details_nom_aliment);
        ImageView imgvwDetailsAliment = dialog_dtlsAliment.findViewById(R.id.imgvw_dialog_imageDetailsAliment);
        ImageButton imgbtn_dialog_details_edit = dialog_dtlsAliment.findViewById(R.id.imgbtn_dialog_details_edit);

        tv_dialog_details_description.setText(aliment.description);
        tv_nomAliment.setText(aliment.nom);
        String pattern = "EEEE dd MMMM yyyy HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, new Locale("fr", "FR"));
        tv_dateAjoutAliment.setText(dateFormat.format(aliment.dateAjout).toString());
        Picasso.get().load(aliment.imageUri).into(imgvwDetailsAliment);

        btn_dialog_details_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_dtlsAliment.dismiss();
            }
        });

        btn_dialog_details_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InitDialogOuiOuNon("Voulez vous supprimer cette aliment? \n" + aliment.nom, aliment.imageUri);
                btn_Rep_Oui_dialog_OuiNon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        _serviceEpicerie.SupprimerAliment(aliment);
                        dialogOuiOuNon.dismiss();
                        dialog_dtlsAliment.dismiss();
                    }
                });
                dialogOuiOuNon.show();
            }
        });

        imgbtn_dialog_details_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAjoutAliment(aliment);
                dialog_dtlsAliment.dismiss();
            }
        });


        if (aliment.alimentauto){
            imgbtn_dialog_details_edit.setVisibility(View.GONE);
            imgbtn_dialog_details_edit.setClickable(false);
        }

        if (aliment.validerAchat){
            //on change les boutons dans le dialog détails
            btn_dialog_details_validate.setVisibility(View.GONE);
            btn_dialog_details_annulerAchat.setVisibility(View.VISIBLE);

            btn_dialog_details_annulerAchat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InitDialogOuiOuNon("Annuler cette aliment? \n \n"  + aliment.nom, aliment.imageUri);
                    btn_Rep_Oui_dialog_OuiNon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //si on appui sur oui dans le dialog_dtlsAliment, l'achat est annulé,
                            //on change les les bouton du dialog_dtlsAliment
                            //btn_dialog_details_validate.setVisibility(View.GONE);
                            //btn_dialog_details_annulerAchat.setVisibility(View.VISIBLE);
                            //on enleve le message sur son item et réaffiche le bouton
                            imgbtnValiderAliment.setVisibility(View.VISIBLE);
                            tvMessageAchete.setVisibility(View.GONE);
                            //on met à jour les couleurs de l'item
                            itemView.setBackground(context.getDrawable(R.drawable.shape_item_background_rcyclvw_aliment));
                            itemView.setElevation(5);
                            //on change l'etat achat
                            aliment.validerAchat = false;
                            _serviceEpicerie.UpdateAliment(aliment);
                            dialogOuiOuNon.dismiss();
                            dialog_dtlsAliment.dismiss();
                            _serviceEpicerie.GetListAliment();
                        }
                    });

                    dialogOuiOuNon.show();

                }
            });
        }
        else{
            btn_dialog_details_validate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InitDialogOuiOuNon("Valider l'achat de cette aliment? \n \n"  + aliment.nom, aliment.imageUri);
                    btn_Rep_Oui_dialog_OuiNon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //on enleve le bouton de l'item et on affiche le message
                            imgbtnValiderAliment.setVisibility(View.GONE);
                            tvMessageAchete.setVisibility(View.VISIBLE);
                            itemView.setBackground(context.getDrawable(R.drawable.shape_stroke_item_background));
                            itemView.setElevation(0);
                            aliment.validerAchat = true;
                            _serviceEpicerie.UpdateAliment(aliment);
                            epicerieAdapter.updateProgression();
                            dialogOuiOuNon.dismiss();
                            dialog_dtlsAliment.dismiss();
                            _serviceEpicerie.GetListAliment();
                        }
                    });

                    dialogOuiOuNon.show();

                }
            });
        }

        dialog_dtlsAliment.show();
        dialog_dtlsAliment.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog_dtlsAliment.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog_dtlsAliment.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog_dtlsAliment.getWindow().setGravity(Gravity.BOTTOM);


    }
    public void showDialogLoadingWaiting(){
        dialogLoadingWaiting = new Dialog(context);
        dialogLoadingWaiting.setContentView(R.layout.dialog_loading_waiting);
        dialogLoadingWaiting.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogLoadingWaiting.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogLoadingWaiting.getWindow().setGravity(Gravity.CENTER);
        dialogLoadingWaiting.setCanceledOnTouchOutside(false);
        dialogLoadingWaiting.show();
    }

    public void dismissDialogLoadingWaiting(){
        if (dialogLoadingWaiting != null){
            dialogLoadingWaiting.dismiss();
        }
    }
}
