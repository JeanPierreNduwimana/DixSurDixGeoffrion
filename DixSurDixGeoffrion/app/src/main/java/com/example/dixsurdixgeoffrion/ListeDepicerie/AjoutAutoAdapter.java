package com.example.dixsurdixgeoffrion.ListeDepicerie;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dixsurdixgeoffrion.Models.Aliment;
import com.example.dixsurdixgeoffrion.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AjoutAutoAdapter extends RecyclerView.Adapter<AjoutAutoAdapter.MyViewHolder> {

    public List<Aliment> listAliment;
    public Context context;
    public List<Aliment> listAlimentSelected;

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

    public AjoutAutoAdapter(){ listAliment = new ArrayList<>();
    }

    public AjoutAutoAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){

        LinearLayout v = (LinearLayout) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_ajout_auto_aliment, viewGroup, false);

        AjoutAutoAdapter.MyViewHolder viewHolder = new AjoutAutoAdapter.MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AjoutAutoAdapter.MyViewHolder viewHolder, final int position) {

        Aliment alimentcourant = listAliment.get(position);
        viewHolder.tv_NomAliment.setText(alimentcourant.Nom);

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

        viewHolder.imgbtn_selection_aliment.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                if(viewHolder.imgbtn_selection_aliment.getBackgroundTintList() == context.getResources().getColorStateList(R.color.white,null))
                {
                    viewHolder.imgbtn_selection_aliment.setBackgroundTintList(context.getResources().getColorStateList(R.color.blue,null));
                    viewHolder.imgbtn_selection_aliment.setImageDrawable(context.getDrawable(R.drawable.add_24));
                }else{
                    viewHolder.imgbtn_selection_aliment.setBackgroundTintList(context.getResources().getColorStateList(R.color.white,null));
                    viewHolder.imgbtn_selection_aliment.setImageDrawable(context.getDrawable(R.drawable.check_24_blue));

                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return listAliment.size();
    }
}
