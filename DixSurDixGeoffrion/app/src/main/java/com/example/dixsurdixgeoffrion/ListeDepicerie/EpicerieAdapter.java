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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EpicerieAdapter extends RecyclerView.Adapter<EpicerieAdapter.MyViewHolder> {

    public List<Aliment> listAliment;
    public List<Integer> listimages = new ArrayList<>();
    public Context context;
    private int index = 0;
    private MyViewHolder itemprincipal;

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
            Picasso.get().load(alimentcourant.imageUri).into(viewHolder.imgvwImageAliment);

            //Creation du dialog oui non pour pas avoir à le repeter
            Dialog dialogOuiNon = new Dialog(context);
            dialogOuiNon.setContentView(R.layout.dialog_yes_not);
            TextView messageDialogOuiNon = dialogOuiNon.findViewById(R.id.txt_dialog_question_oui_non);
            Button btn_Rep_Oui_dialog_OuiNon = dialogOuiNon.findViewById(R.id.btn_dialog_rep_oui);
            //Vu que le bouton "non" ne fait que fermer le dialogOuiNon, pas la peine de le répeter plein de fois
            dialogOuiNon.findViewById(R.id.btn_dialog_rep_non).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogOuiNon.dismiss();
                }
            });

            dialogOuiNon.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialogOuiNon.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialogOuiNon.getWindow().setGravity(Gravity.CENTER);

            viewHolder.imgbtnValiderAliment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    messageDialogOuiNon.setText("Valider l'achat de cette aliment? \n \n"  + alimentcourant.nom);
                    btn_Rep_Oui_dialog_OuiNon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            viewHolder.imgbtnValiderAliment.setVisibility(View.GONE);
                            viewHolder.tvMessageAchete.setVisibility(View.VISIBLE);
                            alimentcourant.validerAchat = true;
                            viewHolder.itemView.setBackground(context.getDrawable(R.drawable.shape_stroke_item_background));
                            viewHolder.itemView.setElevation(0);
                            updateProgression();
                            dialogOuiNon.dismiss();
                        }
                    });
                    dialogOuiNon.show();
                }
            });
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog dialog_dtlsAliment = new Dialog(context);
                    dialog_dtlsAliment.setContentView(R.layout.details_aliment);

                    TextView tv_dialog_details_description= dialog_dtlsAliment.findViewById(R.id.txt_dtlsALiment_description);
                    Button btn_dialog_details_validate = dialog_dtlsAliment.findViewById(R.id.btn_dtlsAliment_validate);
                    Button btn_dialog_details_annulerAchat = dialog_dtlsAliment.findViewById(R.id.btn_dtlsAliment_annuler_achat);
                    ImageView imgvw_dialog_details_ImageDetailsAliment = dialog_dtlsAliment.findViewById(R.id.imgvw_dialog_imageDetailsAliment);
                    tv_dialog_details_description.setText(alimentcourant.description);
                    //imgvw_dialog_details_ImageDetailsAliment.setImageDrawable(context.getDrawable(alimentcourant.Photo));

                    if (alimentcourant.validerAchat){
                        //on change les boutons dans le dialog détails
                        btn_dialog_details_validate.setVisibility(View.GONE);
                        btn_dialog_details_annulerAchat.setVisibility(View.VISIBLE);

                        btn_dialog_details_annulerAchat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                messageDialogOuiNon.setText("Annuler cette aliment? \n \n"  + alimentcourant.nom);
                                btn_Rep_Oui_dialog_OuiNon.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //si on appui sur oui dans le dialog_dtlsAliment, l'achat est annulé,
                                        //on change les les bouton du dialog_dtlsAliment
                                        btn_dialog_details_validate.setVisibility(View.GONE);
                                        btn_dialog_details_annulerAchat.setVisibility(View.VISIBLE);
                                        //on enleve le message sur son item et réaffiche le bouton
                                        viewHolder.imgbtnValiderAliment.setVisibility(View.VISIBLE);
                                        viewHolder.tvMessageAchete.setVisibility(View.GONE);
                                        //on met à jour les couleurs de l'item
                                        viewHolder.itemView.setBackground(context.getDrawable(R.drawable.shape_item_background_rcyclvw_aliment));
                                        viewHolder.itemView.setElevation(5);
                                        //on change l'etat achat
                                        alimentcourant.validerAchat = false;
                                        dialogOuiNon.dismiss();
                                        dialog_dtlsAliment.dismiss();
                                    }
                                });

                                dialogOuiNon.show();

                            }
                        });
                    }
                    else{
                        btn_dialog_details_validate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //si on valide l'achat à partir du dialog_dtlsAliment
                                //on affiche le message dans le diagOUiNon
                                messageDialogOuiNon.setText("Valider l'achat de cette aliment? \n \n"  + alimentcourant.nom);
                                //on change les boutons du dialog_dtlsAliment
                                btn_Rep_Oui_dialog_OuiNon.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        btn_dialog_details_validate.setVisibility(View.GONE);
                                        btn_dialog_details_annulerAchat.setVisibility(View.VISIBLE);
                                        //on enleve le bouton de l'item et on affiche le message
                                        viewHolder.imgbtnValiderAliment.setVisibility(View.GONE);
                                        viewHolder.tvMessageAchete.setVisibility(View.VISIBLE);
                                        viewHolder.itemView.setBackground(context.getDrawable(R.drawable.shape_stroke_item_background));
                                        viewHolder.itemView.setElevation(0);
                                        alimentcourant.validerAchat = true;
                                        updateProgression();
                                        dialogOuiNon.dismiss();
                                        dialog_dtlsAliment.dismiss();
                                    }
                                });

                                dialogOuiNon.show();

                            }
                        });
                    }


                    dialog_dtlsAliment.show();
                    dialog_dtlsAliment.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog_dtlsAliment.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog_dtlsAliment.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    dialog_dtlsAliment.getWindow().setGravity(Gravity.BOTTOM);
                }
            });

            viewHolder.imgvwImageAliment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Dialog dialogAfficheImageAlimet = new Dialog(context);
                    dialogAfficheImageAlimet.setContentView(R.layout.dialog_show_image_full);
                    ImageView imagealiment = dialogAfficheImageAlimet.findViewById(R.id.imgvw_image_alimentfull);
                    //imagealiment.setImageDrawable(context.getDrawable(alimentcourant.Photo));
                    dialogAfficheImageAlimet.show();
                    dialogAfficheImageAlimet.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialogAfficheImageAlimet.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialogAfficheImageAlimet.getWindow().setGravity(Gravity.CENTER);
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

        int pourcentage = nbdalimentValide * 100 / listAliment.size();
        return pourcentage;
    }

    private void updateProgression()
    {
        itemprincipal.progressBar.setProgress(getprogression());
    }
}
