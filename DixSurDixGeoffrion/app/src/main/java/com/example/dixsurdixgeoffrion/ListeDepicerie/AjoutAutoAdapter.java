package com.example.dixsurdixgeoffrion.ListeDepicerie;

import android.app.Dialog;
import android.content.Context;
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

import androidx.recyclerview.widget.RecyclerView;

import com.example.dixsurdixgeoffrion.Models.Aliment;
import com.example.dixsurdixgeoffrion.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AjoutAutoAdapter extends RecyclerView.Adapter<AjoutAutoAdapter.MyViewHolder> {

    public List<Aliment> listAliment;
    public Context context;
    public List<Aliment> listAlimentSelected;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgvw_ImageAliment;
        public TextView tv_NomAliment;
        public TextView tv_qtAliment;
        public CheckBox chbx_selection_aliment;
        public TextView btn_augmenterQtAliment;
        public TextView btn_diminuerQtAliment;


        public MyViewHolder(LinearLayout v) {
            super(v);

            imgvw_ImageAliment = v.findViewById(R.id.imgvw_image_ajout_auto_aliment);
            tv_NomAliment = v.findViewById(R.id.txt_nom_ajout_auto_aliment);
            tv_qtAliment = v.findViewById(R.id.txt_ajtAutoAliment_Quantite);
            chbx_selection_aliment = v.findViewById(R.id.chbx_selectionner_ajout_auto_aliment);
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

        viewHolder.chbx_selection_aliment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                   // Toast.makeText(context,alimentcourant.Nom + " est coché", Toast.LENGTH_LONG).show();
                    Log.i("CHECKED",alimentcourant.Nom + " est coché");
                }
                else {
                   // Toast.makeText(context,alimentcourant.Nom + " est décoché", Toast.LENGTH_LONG).show();
                    Log.i("CHECKED",alimentcourant.Nom + " est décoché");
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return listAliment.size();
    }
}
