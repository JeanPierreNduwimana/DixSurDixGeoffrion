package com.example.dixsurdixgeoffrion.Services;
import android.net.Uri;
import android.view.View;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dixsurdixgeoffrion.ListeDepicerie.AjoutAutoAdapter;
import com.example.dixsurdixgeoffrion.ListeDepicerie.MainListeDepicerie;
import com.example.dixsurdixgeoffrion.Models.Aliment;
import com.example.dixsurdixgeoffrion.Models.AlimentAuto;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class ServiceEpicerie {

    public List<Aliment> alimentList = new ArrayList<>(); //Cette liste sert à envoyer une liste d'aliments dans la liste d'epicerie principal
    public List<Aliment> alimentListAuto = new ArrayList<>(); //Cette liste sert à ajouter des aliments automatiques selectionnés
    private DatabaseReference _rootDataref;
    private StorageReference rootStorage;
    private MainListeDepicerie context;
    public DialogService dialogService;


    public ServiceEpicerie(MainListeDepicerie current_context){
        //_rootDataref = FirebaseDatabase.getInstance().getReference().child("1010GeoffrionApp"); //Public
        _rootDataref = FirebaseDatabase.getInstance().getReference().child("1010GeoffrionApp"); //Test
        rootStorage = FirebaseStorage.getInstance().getReference().child("AlimentImages");
        context = current_context;
    }
    public void GetListAliment(){

        dialogService.showDialogLoadingWaiting();

        _rootDataref.child("AlimentsEpicerie").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Iterable<DataSnapshot> list = snapshot.getChildren();

                    for (DataSnapshot s : list){
                        alimentList.add(s.getValue(Aliment.class));
                    }
                }
                //dialogService.dismissDialogLoadingWaiting();
                context.remplirRecycler(alimentList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,"Problème inattendue",Toast.LENGTH_LONG).show();
                dialogService.dismissDialogLoadingWaiting();
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Toast.makeText(context,"Problème inattendue",Toast.LENGTH_LONG).show();
                dialogService.dismissDialogLoadingWaiting();
            }
        });
    }
    public void GetListAutoAliment(AjoutAutoAdapter ajoutAutoAdapter, RecyclerView recyclerView, ProgressBar progressBar, TextView msg_ListeVide) {
        _rootDataref.child("Aliments Automatiques").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Iterable<DataSnapshot> list = snapshot.getChildren();
                    for (DataSnapshot s : list){
                        AlimentAuto alimentAuto = s.getValue(AlimentAuto.class);
                        if (!alimentAuto.isUsed()){
                            ajoutAutoAdapter.listAliment.add(alimentAuto);
                        }
                    }

                    if (ajoutAutoAdapter.listAliment.size() == 0){
                        recyclerView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        msg_ListeVide.setVisibility(View.VISIBLE);
                    }else{
                        ajoutAutoAdapter.notifyDataSetChanged();
                        msg_ListeVide.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }else{
                    msg_ListeVide.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    Toast.makeText(context,"Erreur d'affiche des aliments auto", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context,"Problème inattendue",Toast.LENGTH_LONG).show();
            }
        });
    }
    public void AjouterAliment(Aliment aliment, Uri imageUri){
        dialogService.showDialogLoadingWaiting();
        //Création de la clé qui sera la même pour l'image et l'aliment
        String key = _rootDataref.push().getKey();

        //Ajout de l'image d'abord, enfin de l'associer à l'aliment après
        rootStorage.child("AlimentsManuels").child(key+".jpg").putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                rootStorage.child("AlimentsManuels").child(key+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        //Création de l'aliment après le succes et la reception de l'image
                        //Association de la clé et de l'image à l'aliment
                        aliment.alimentKey = key;
                        aliment.imageUri = uri.toString();

                        //Création de l'aliment
                        _rootDataref.child("AlimentsEpicerie").child(key).setValue(aliment).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                dialogService.dialogLoadingWaiting.dismiss();
                                //Chargement de la liste d'aliment pour avoir le nouvel aliment
                                GetListAliment();

                            }
                        })
                            //Si echec d'ajout de l'aliment
                            .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context,"Erreur d'ajout de l'aliment", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                })
                    //Si echec de la reception de l'image qu'on tenté d'ajouter
                    .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,"Erreur avec l'image ajoutée", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,"Erreur d'ajout de photo",Toast.LENGTH_LONG).show();
            }
        }); //addonprogresslistener possible.
    }
    public void AjouterAlimentSansImage(Aliment aliment){
        dialogService.showDialogLoadingWaiting();
        String key = _rootDataref.push().getKey();
        aliment.alimentKey = key;
        aliment.imageUri = "https://firebasestorage.googleapis.com/v0/b/projet-test-f9f8c.appspot.com/o/AlimentImages%2FGeoffrionStockImages%2Fgeoffrion_main_icon.png?alt=media&token=7f3bfefc-18c2-4f16-9b5c-ba82109c361d";
        _rootDataref.child("AlimentsEpicerie").child(key).setValue(aliment);
        dialogService.dialogLoadingWaiting.dismiss();
        GetListAliment();
    }
    public void AjouterAutoAliment(){
        dialogService.showDialogLoadingWaiting();
        for (Aliment aliment : alimentListAuto){
            String key = _rootDataref.push().getKey();
            aliment.alimentKey = key;
            _rootDataref.child("AlimentsEpicerie").child(key).setValue(aliment);
        }
        alimentListAuto.clear();
        dialogService.dialogLoadingWaiting.dismiss();
        GetListAliment();
    }
    public void UpdateAliment(Aliment aliment){
        dialogService.showDialogLoadingWaiting();
        _rootDataref.child("AlimentsEpicerie/"+ aliment.alimentKey).setValue(aliment);
        dialogService.dialogLoadingWaiting.dismiss();
        GetListAliment();
    }
    public void UpdateAliment(Aliment aliment, Uri imageUri){
        dialogService.showDialogLoadingWaiting();
        final String Path =  "AlimentsManuels/" + aliment.alimentKey + ".jpg";
        rootStorage.child(Path).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                rootStorage.child(Path).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        rootStorage.child(Path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                aliment.imageUri = uri.toString();
                                UpdateAliment(aliment);
                                dialogService.dialogLoadingWaiting.dismiss();
                                GetListAliment();
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    //Mise à jour des aliments automatiques utilisés enfin de ne plus les afficher dans le dialog d'ajout auto aliment.
    public void UpdateUsedAutoAliment(AlimentAuto alimentAuto){
        dialogService.showDialogLoadingWaiting();
        _rootDataref.child("Aliments Automatiques").child(alimentAuto.alimentKey).setValue(alimentAuto);
        dialogService.dialogLoadingWaiting.dismiss();
    }
    public void SupprimerAliment(Aliment aliment){
        dialogService.showDialogLoadingWaiting();
        _rootDataref.child("AlimentsEpicerie/"+ aliment.alimentKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                if (aliment.alimentauto){
                    _rootDataref.child("Aliments Automatiques").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot snapshot) {
                            Iterable<DataSnapshot> list = snapshot.getChildren();
                            AlimentAuto alimentAuto = null;
                            for (DataSnapshot s : list){
                                if (s.getValue(AlimentAuto.class).nom.equals(aliment.nom)){
                                    alimentAuto = s.getValue(AlimentAuto.class);
                                    alimentAuto.used = false;
                                    alimentAuto.quantite = 0;
                                    alimentAuto.validerAchat = false;
                                    break;
                                }
                            }
                            if (alimentAuto != null){
                                _rootDataref.child("Aliments Automatiques/" + alimentAuto.alimentKey).setValue(alimentAuto);
                                dialogService.dialogLoadingWaiting.dismiss();
                                GetListAliment();
                            }

                        }
                    });
                }else {
                    rootStorage.child("AlimentsManuels").child(aliment.alimentKey+".jpg").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            //do something
                            dialogService.dialogLoadingWaiting.dismiss();
                            GetListAliment();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //S'il n'y a pas d'images
                            dialogService.dialogLoadingWaiting.dismiss();
                            GetListAliment();
                        }
                    });
                }

            }
        });
    }
    public void SupprimerToutLesAliments(List<Aliment> aliments){

        dialogService.showDialogLoadingWaiting();
        _rootDataref.child("Aliments Automatiques").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot snapshot) {
                //Comme la liste aliments provient de l'adapter, on supprime le premier element qui est null.
                if (aliments.size() > 0){
                    aliments.remove(0);
                }
                Iterable<DataSnapshot> list = snapshot.getChildren();
                List<AlimentAuto> alimentAutos = new ArrayList<>();
                for (DataSnapshot s : list){
                    alimentAutos.add(s.getValue(AlimentAuto.class));
                }
                for(Aliment aliment : aliments){
                    _rootDataref.child("AlimentsEpicerie").child(aliment.alimentKey).removeValue();
                    if (aliment.alimentauto){
                        for (AlimentAuto alimentAuto : alimentAutos){
                            if (alimentAuto.nom.equals(aliment.nom)){
                                alimentAuto.used = false;
                                alimentAuto.quantite = 0;
                                alimentAuto.validerAchat = false;
                                _rootDataref.child("Aliments Automatiques/" + alimentAuto.alimentKey).setValue(alimentAuto);
                            }
                        }
                    }else{
                        rootStorage.child("AlimentsManuels").child(aliment.alimentKey + ".jpg").delete();
                    }
                }
                dialogService.dialogLoadingWaiting.dismiss();
                GetListAliment();
            }
        });
        
    }
}
