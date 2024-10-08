package com.example.dixsurdixgeoffrion.ListeDepicerie;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dixsurdixgeoffrion.Models.Aliment;
import com.example.dixsurdixgeoffrion.Models.AlimentAuto;
import com.example.dixsurdixgeoffrion.R;
import com.example.dixsurdixgeoffrion.Services.DialogService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AjoutAutoAdapter extends RecyclerView.Adapter<AjoutAutoAdapter.MyViewHolder> {

    public List<AlimentAuto> listAliment;
    public Context context;
    public DialogService dialogService;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgvw_ImageAliment;
        public TextView tv_NomAliment;
        public TextView tv_qtAliment;
        public ImageView imgbtn_selection_aliment;
        public TextView btn_augmenterQtAliment;
        public TextView btn_diminuerQtAliment;


        public MyViewHolder(LinearLayout v) {
            super(v);

            imgvw_ImageAliment = v.findViewById(R.id.imgvw_image_ajout_auto_aliment);
            tv_NomAliment = v.findViewById(R.id.txt_nom_ajout_auto_aliment);
            tv_qtAliment = v.findViewById(R.id.txt_ajtAutoAliment_Quantite);
            imgbtn_selection_aliment = v.findViewById(R.id.imgbtn_selectionner_ajout_auto_aliment);
            btn_augmenterQtAliment = v.findViewById(R.id.btn_ajtAutoAliment_augmenter);
            btn_diminuerQtAliment = v.findViewById(R.id.btn_ajtAutoAliment_diminuer);
        }
    }

    public AjoutAutoAdapter(){ listAliment = new ArrayList<>();}

    public AjoutAutoAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){

        LinearLayout v = (LinearLayout) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_ajout_auto_aliment, viewGroup, false);

        AjoutAutoAdapter.MyViewHolder viewHolder = new AjoutAutoAdapter.MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AjoutAutoAdapter.MyViewHolder viewHolder, final int position) {

        AlimentAuto alimentcourant = listAliment.get(position);
        viewHolder.tv_NomAliment.setText(alimentcourant.nom);
        Picasso.get().load(alimentcourant.imageUri).into(viewHolder.imgvw_ImageAliment);

        viewHolder.btn_augmenterQtAliment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantite = Integer.valueOf(viewHolder.tv_qtAliment.getText().toString());

                if (quantite < 20){
                    quantite = quantite + 1;
                    viewHolder.tv_qtAliment.setText(""+quantite);

                }
            }
        });

        viewHolder.btn_diminuerQtAliment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantite = Integer.valueOf(viewHolder.tv_qtAliment.getText().toString());

                if (quantite > 0){
                    quantite = quantite - 1;
                    viewHolder.tv_qtAliment.setText(""+quantite);

                }
            }
        });

        //Button de selection des aliments automatiques à ajouter à la liste d'épicerie
        viewHolder.imgbtn_selection_aliment.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                if(viewHolder.imgbtn_selection_aliment.getBackgroundTintList() == context.getResources().getColorStateList(R.color.white,null))
                {
                    viewHolder.imgbtn_selection_aliment.setBackgroundTintList(context.getResources().getColorStateList(R.color.blue,null));
                    viewHolder.imgbtn_selection_aliment.setImageDrawable(context.getDrawable(R.drawable.add_24));
                    alimentcourant.quantite = Integer.parseInt(viewHolder.tv_qtAliment.getText().toString());
                    viewHolder.btn_diminuerQtAliment.setVisibility(View.VISIBLE);
                    viewHolder.btn_augmenterQtAliment.setVisibility(View.VISIBLE);
                    viewHolder.btn_diminuerQtAliment.setClickable(true);
                    viewHolder.btn_augmenterQtAliment.setClickable(true);
                    dialogService.AjoutAlimentAuto(alimentcourant);
                }else{
                    viewHolder.imgbtn_selection_aliment.setBackgroundTintList(context.getResources().getColorStateList(R.color.white,null));
                    viewHolder.imgbtn_selection_aliment.setImageDrawable(context.getDrawable(R.drawable.check_24_blue));
                    alimentcourant.quantite = Integer.parseInt(viewHolder.tv_qtAliment.getText().toString());
                    viewHolder.btn_diminuerQtAliment.setVisibility(View.INVISIBLE);
                    viewHolder.btn_augmenterQtAliment.setVisibility(View.INVISIBLE);
                    viewHolder.btn_diminuerQtAliment.setClickable(false);
                    viewHolder.btn_augmenterQtAliment.setClickable(false);
                    dialogService.AjoutAlimentAuto(alimentcourant);
                }
            }
        });

        //Appui sur l'image pour l'afficher en plein écran
        viewHolder.imgvw_ImageAliment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogService.showDialogFullImage(null,alimentcourant);
            }
        });


    }

    @Override
    public int getItemCount() {
        return listAliment.size();
    }
}
