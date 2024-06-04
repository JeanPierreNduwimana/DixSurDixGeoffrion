package com.example.dixsurdixgeoffrion.Services;
import android.net.Uri;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dixsurdixgeoffrion.ListeDepicerie.AjoutAutoAdapter;
import com.example.dixsurdixgeoffrion.ListeDepicerie.MainListeDepicerie;
import com.example.dixsurdixgeoffrion.Models.Aliment;
import com.example.dixsurdixgeoffrion.Models.AlimentAuto;
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

    public List<AlimentAuto> alimentAutoList = new ArrayList<>();
    private DatabaseReference _rootDataref;
    private StorageReference rootStorage;
    private MainListeDepicerie context;

    public DialogService dialogService;


    public ServiceEpicerie(MainListeDepicerie current_context){
        _rootDataref = FirebaseDatabase.getInstance().getReference().child("1010GeoffrionApp");
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
        });
    }
    public void GetListAutoAliment(AjoutAutoAdapter ajoutAutoAdapter, RecyclerView recyclerView, ProgressBar progressBar) {
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
                    ajoutAutoAdapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }else{
                    recyclerView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
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
                        Toast.makeText(context,"Image Storage added successfully!", Toast.LENGTH_LONG).show();

                        //Association de la clé et de l'image à l'aliment
                        aliment.alimentKey = key;
                        aliment.imageUri = uri.toString();

                        //Création de l'aliment
                        _rootDataref.child("AlimentsEpicerie").child(key).setValue(aliment).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //Chargement de la liste d'aliment pour avoir le nouvel aliment
                                GetListAliment();
                                Toast.makeText(context,"Data added successfully!", Toast.LENGTH_SHORT).show();

                            }
                        })
                            //Si echec d'ajout de l'aliment
                            .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context,"Failed adding data =(", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                })
                    //Si echec de la reception de l'image qu'on tenté d'ajouter
                    .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,"download url failed", Toast.LENGTH_LONG).show();
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
        String key = _rootDataref.push().getKey();
        aliment.alimentKey = key;
        aliment.imageUri = "https://firebasestorage.googleapis.com/v0/b/projet-test-f9f8c.appspot.com/o/AlimentImages%2FAlimentsAutomatiques%2FDefaultAliment0001.jpg?alt=media&token=edddf31a-d178-4f4c-85a7-e3b116644dab";
        _rootDataref.child("AlimentsEpicerie").child(key).setValue(aliment);
    }
    public void AjouterAutoAliment(){
        for (Aliment aliment : alimentListAuto){
            String key = _rootDataref.push().getKey();
            aliment.alimentKey = key;
            _rootDataref.child("AlimentsEpicerie").child(key).setValue(aliment);
        }
        alimentListAuto.clear();
        GetListAliment();
    }
    public void UpdateAliment(Aliment aliment){
        _rootDataref.child("AlimentsEpicerie/"+ aliment.alimentKey).setValue(aliment);
    }
    public void UpdateUsedAutoAliment(AlimentAuto alimentAuto){
        _rootDataref.child("Aliments Automatiques").child(alimentAuto.alimentKey).setValue(alimentAuto);
    }
    public void SupprimerAliment(Aliment aliment){
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
                                GetListAliment();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }else {
                    rootStorage.child("AlimentsManuels").child(aliment.alimentKey+".jpg").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            //do something
                            GetListAliment();
                            Toast.makeText(context,"Image effacée",Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        });
    }
    public void SupprimerToutLesAliments(List<Aliment> aliments){
        _rootDataref.child("Aliments Automatiques").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

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

                GetListAliment();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        
    }


}
