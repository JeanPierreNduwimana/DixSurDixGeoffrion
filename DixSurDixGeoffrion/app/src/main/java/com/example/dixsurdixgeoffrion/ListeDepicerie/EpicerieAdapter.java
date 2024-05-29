package com.example.dixsurdixgeoffrion.ListeDepicerie;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dixsurdixgeoffrion.Models.Aliment;
import com.example.dixsurdixgeoffrion.R;
import com.example.dixsurdixgeoffrion.Services.DialogService;
import com.example.dixsurdixgeoffrion.Services.ServiceEpicerie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EpicerieAdapter extends RecyclerView.Adapter<EpicerieAdapter.MyViewHolder> {

    public List<Aliment> listAliment;
    public List<Integer> listimages = new ArrayList<>(); // pour le diaporama
    private int index = 0; //index utilisés pour les images de diaporama
    public Context context;
    private MyViewHolder itemprincipal;
    public ServiceEpicerie _serviceEpicerie;
    public DialogService dialogService;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgvwImageAliment;
        public TextView tvNomAliment;
        public TextView tvQteAliment;
        public TextView tvMessageAchete;
        public ImageButton imgbtnValiderAliment;
        public LinearLayout  itemAliment;
        public LinearLayout itemPrincipal;
        public ImageView imgvwDiaporama;
        public ProgressBar progressBar;


        public MyViewHolder(LinearLayout v) {
            super(v);

            imgvwImageAliment = v.findViewById(R.id.imgvw_image_aliment);
            tvNomAliment = v.findViewById(R.id.txt_nom_aliment);
            tvQteAliment = v.findViewById(R.id.txt_qte_aliment);
            tvMessageAchete = v.findViewById(R.id.txt_message_achete);
            imgbtnValiderAliment = v.findViewById(R.id.imgbtn_valider_aliment);
            itemAliment =  v.findViewById(R.id.item_aliment);
            itemPrincipal = v.findViewById(R.id.item_principal);
            imgvwDiaporama = v.findViewById(R.id.imgvw_diaporama);
            progressBar = v.findViewById(R.id.progressbar);
        }
    }
    public EpicerieAdapter() {
        listAliment = new ArrayList<>();
    }
    public EpicerieAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){

        LinearLayout v = (LinearLayout) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_aliment, viewGroup, false);

        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, final int position) {

        Aliment alimentcourant = listAliment.get(position);
        if (viewHolder.getBindingAdapterPosition() == 0)
        {
            viewHolder.itemView.setClickable(false);
            viewHolder.itemAliment.setVisibility(View.GONE);
            viewHolder.itemPrincipal.setVisibility(View.VISIBLE);
            viewHolder.progressBar.setProgress(getprogression());
            itemprincipal = viewHolder;

            Handler handler = new Handler();
            int delay = 5000; //five sec
            handler.postDelayed(new Runnable(){
                public void run(){
                    //do process here
                    handler.postDelayed(this, delay); // recall

                    if (index < listimages.size())
                    {
                        viewHolder.imgvwDiaporama.setImageDrawable(context.getDrawable(listimages.get(index)));
                        index++;
                    }else{
                        index = 0;
                        viewHolder.imgvwDiaporama.setImageDrawable(context.getDrawable(listimages.get(index)));
                    }

                }
            }, delay);
        }
        else {
            viewHolder.itemView.setClickable(true);
            viewHolder.itemPrincipal.setVisibility(View.GONE);
            viewHolder.itemAliment.setVisibility(View.VISIBLE);
            viewHolder.tvNomAliment.setText(alimentcourant.nom);
            viewHolder.tvQteAliment.setText(""+alimentcourant.quantite);
            if (alimentcourant.validerAchat){
                viewHolder.imgbtnValiderAliment.setVisibility(View.GONE);
                viewHolder.tvMessageAchete.setVisibility(View.VISIBLE);
                viewHolder.itemView.setBackground(context.getDrawable(R.drawable.shape_stroke_item_background));
            }else{
                viewHolder.imgbtnValiderAliment.setVisibility(View.VISIBLE);
                viewHolder.tvMessageAchete.setVisibility(View.GONE);
                viewHolder.itemView.setBackground(context.getDrawable(R.drawable.shape_item_background_rcyclvw_aliment));
            }
            Picasso.get().load(alimentcourant.imageUri).into(viewHolder.imgvwImageAliment);
            viewHolder.imgbtnValiderAliment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialogService.InitDialogOuiOuNon("Valider l'achat de cette aliment? \n \n"  + alimentcourant.nom);
                    dialogService.btn_Rep_Oui_dialog_OuiNon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            viewHolder.imgbtnValiderAliment.setVisibility(View.GONE);
                            viewHolder.tvMessageAchete.setVisibility(View.VISIBLE);
                            viewHolder.itemView.setBackground(context.getDrawable(R.drawable.shape_stroke_item_background));
                            viewHolder.itemView.setElevation(0);
                            alimentcourant.validerAchat = true;
                            _serviceEpicerie.UpdateAliment(alimentcourant);
                            updateProgression();
                            dialogService.dialogOuiOuNon.dismiss();
                        }
                    });
                    dialogService.dialogOuiOuNon.show();
                }
            });
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogService.showDialogDetailsAliment(
                            alimentcourant,
                            viewHolder.imgbtnValiderAliment,
                            viewHolder.tvMessageAchete,
                            viewHolder.itemView,
                            EpicerieAdapter.this);

                }
            });
            viewHolder.imgvwImageAliment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogService.showDialogFullImage(alimentcourant,null);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listAliment.size();
    }

    private int getprogression(){
        int nbdalimentValide = 0;

        for (Aliment aliment : listAliment) {
            if (aliment != null)
            {
                if (aliment.validerAchat){nbdalimentValide++;}
            }
        }
        if(nbdalimentValide == 0){
            return 0;}

        int pourcentage = nbdalimentValide * 100 / (listAliment.size() -1); //Moins un à cause de l'item principal
        return pourcentage;
    }

    public void updateProgression()
    {
        itemprincipal.progressBar.setProgress(getprogression());
    }
}
