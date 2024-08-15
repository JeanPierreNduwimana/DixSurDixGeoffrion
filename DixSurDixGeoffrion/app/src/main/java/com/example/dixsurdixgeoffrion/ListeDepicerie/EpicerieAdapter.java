package com.example.dixsurdixgeoffrion.ListeDepicerie;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dixsurdixgeoffrion.Models.Aliment;
import com.example.dixsurdixgeoffrion.Profile.ProfilePublicActivity;
import com.example.dixsurdixgeoffrion.R;
import com.example.dixsurdixgeoffrion.Services.DialogService;
import com.example.dixsurdixgeoffrion.Services.ServiceEpicerie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EpicerieAdapter extends RecyclerView.Adapter<EpicerieAdapter.MyViewHolder> {

    public List<Aliment> listAliment;
    public Context context;
    private MyViewHolder itemprincipal;
    public ServiceEpicerie _serviceEpicerie;
    public DialogService dialogService;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgvwImageAliment;
        public TextView tvNomAliment;
        public TextView tvQteAliment;
        public TextView tvMessageAchete;
        public TextView tvProgressPourcentage;
        public ImageButton imgbtnValiderAliment;
        public LinearLayout  itemAliment;
        public LinearLayout itemPrincipal;
        public ProgressBar progressBar;
        public LinearLayout progressbarContainer;
        public LinearLayout quantityContainer;
        public TextView tvTitreQteAliment;

        //region scrollview
        public LinearLayout itemScrollJeanPierre;

        //endregion


        public MyViewHolder(LinearLayout v) {
            super(v);

            imgvwImageAliment = v.findViewById(R.id.imgvw_image_aliment);
            tvNomAliment = v.findViewById(R.id.txt_nom_aliment);
            tvQteAliment = v.findViewById(R.id.txt_qte_aliment);
            tvMessageAchete = v.findViewById(R.id.txt_message_achete);
            imgbtnValiderAliment = v.findViewById(R.id.imgbtn_valider_aliment);
            itemAliment =  v.findViewById(R.id.item_aliment);
            itemPrincipal = v.findViewById(R.id.item_principal);
            progressBar = v.findViewById(R.id.progressbar);
            tvProgressPourcentage = v.findViewById(R.id.progress_pourcentage);
            progressbarContainer = v.findViewById(R.id.progressbar_container);
            quantityContainer = v.findViewById(R.id.quantite_container);
            tvTitreQteAliment = v.findViewById(R.id.tv_titre_qte_aliment);

            //region scrollview
            itemScrollJeanPierre = v.findViewById(R.id.item_scrollview_jeanpierre);
            //endregion

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

            /*
            viewHolder.itemScrollJeanPierre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity( new Intent(context, ProfilePublicActivity.class));
                }
            }); */

            if ((listAliment.size() -1) > 0){
                viewHolder.progressbarContainer.setVisibility(View.VISIBLE);
                int progress = getprogression();
                viewHolder.progressBar.setProgress(progress);

                if (progress < 100){
                    viewHolder.tvProgressPourcentage.setText( String.valueOf(progress) + "%");
                    viewHolder.tvProgressPourcentage.setBackground(null);
                    viewHolder.tvProgressPourcentage.setTextColor(context.getResources().getColor(R.color.black,null));
                    viewHolder.progressBar.setProgressTintList(context.getResources().getColorStateList(R.color.hard_blue,null));
                }else{
                    viewHolder.tvProgressPourcentage.setText("100%");
                    viewHolder.tvProgressPourcentage.setTextColor(context.getResources().getColor(R.color.hard_green,null));
                    viewHolder.progressBar.setProgressTintList(context.getResources().getColorStateList(R.color.hard_green,null));
                }
            }else{ viewHolder.progressbarContainer.setVisibility(View.GONE);}

            itemprincipal = viewHolder;
        }
        else {
            viewHolder.itemView.setClickable(true);
            viewHolder.itemPrincipal.setVisibility(View.GONE);
            viewHolder.itemAliment.setVisibility(View.VISIBLE);
            if (alimentcourant.nom.length() > 16){
                String split = alimentcourant.nom.substring(0,15) + "...";
                viewHolder.tvNomAliment.setText(split);
            }else{
                viewHolder.tvNomAliment.setText(alimentcourant.nom);
            }

            if (alimentcourant.quantite > 0){
                viewHolder.tvQteAliment.setText(""+alimentcourant.quantite);
            }else{
                viewHolder.tvQteAliment.setText("-");
            }

            if (alimentcourant.validerAchat){
                viewHolder.imgbtnValiderAliment.setVisibility(View.GONE);
                viewHolder.tvMessageAchete.setVisibility(View.VISIBLE);
                viewHolder.itemView.setBackground(context.getDrawable(R.drawable.shape_stroke_item_background));

                int currentNightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                if (currentNightMode == Configuration.UI_MODE_NIGHT_YES){
                    viewHolder.tvNomAliment.setTextColor(context.getColor(R.color.white));
                    viewHolder.tvQteAliment.setTextColor(context.getColor(R.color.white));
                    viewHolder.tvTitreQteAliment.setTextColor(context.getColor(R.color.white));
                }
            }else{
                viewHolder.tvNomAliment.setTextColor(context.getColor(R.color.black));
                viewHolder.tvQteAliment.setTextColor(context.getColor(R.color.black));
                viewHolder.tvTitreQteAliment.setTextColor(context.getColor(R.color.black));
                viewHolder.imgbtnValiderAliment.setVisibility(View.VISIBLE);
                viewHolder.tvMessageAchete.setVisibility(View.GONE);
                viewHolder.itemView.setBackground(context.getDrawable(R.drawable.shape_item_background_rcyclvw_aliment));
            }
            Picasso.get().load(alimentcourant.imageUri).into(viewHolder.imgvwImageAliment);
            viewHolder.imgbtnValiderAliment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialogService.InitDialogOuiOuNon("Valider l'achat de cette aliment? \n \n"  + alimentcourant.nom, alimentcourant.imageUri);
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
            if (aliment != null) {
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
