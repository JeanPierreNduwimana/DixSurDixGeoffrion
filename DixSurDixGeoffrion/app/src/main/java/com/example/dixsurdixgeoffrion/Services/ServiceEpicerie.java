package com.example.dixsurdixgeoffrion.Services;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

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

    private List<Aliment> alimentList = new ArrayList<>(); //Cette liste sert à envoyer une liste d'aliments dans la liste d'epicerie principal
    public List<Aliment> alimentListAuto = new ArrayList<>(); //Cette liste sert à ajouter des aliments automatiques selectionnés
    private DatabaseReference _rootDataref;
    private StorageReference rootStorage;
    private MainListeDepicerie context;


    public ServiceEpicerie(MainListeDepicerie current_context){
        _rootDataref = FirebaseDatabase.getInstance().getReference().child("1010GeoffrionApp");
        rootStorage = FirebaseStorage.getInstance().getReference().child("AlimentImages");
        context = current_context;
    }

    //Ceci est l'étape 1 du chargement de la liste. La requête charge les aliments ajouté manuelement.
    public void GetListAlimentManuel(){

        _rootDataref.child("AlimentsEpicerie").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Iterable<DataSnapshot> list = snapshot.getChildren();

                    for (DataSnapshot s : list){
                        alimentList.add(s.getValue(Aliment.class));
                    }
                }

                _rootDataref.child("AlimentsAutoEpicerie").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()){
                            //Si la liste d'aliments automatiques est rempli :
                            GetListAlimentAuto();
                        }else{
                            context.remplirRecycler(alimentList);
                            alimentList.clear();// Une fois la liste envoyé dans le recycleview, on la vide pour les prochaine requetes..
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context,"Problème inattendue",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context,"Problème inattendue",Toast.LENGTH_LONG).show();
            }
        });
    }

    //Ceci est l'étape 2 du chargement de la liste. La requête charge les aliments ajouté automatiquement, puis lance le recycleview.
    private void GetListAlimentAuto() {
        _rootDataref.child("AlimentsAutoEpicerie").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Iterable<DataSnapshot> list = snapshot.getChildren();

                    for (DataSnapshot s : list){
                        alimentList.add(s.getValue(Aliment.class));
                    }
                }
                context.remplirRecycler(alimentList);
                alimentList.clear();// Une fois la liste envoyé dans le recycleview, on la vide pour les prochaine requetes..
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context,"Problème inattendue",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void GetListAutoAliment(AjoutAutoAdapter ajoutAutoAdapter) {
        _rootDataref.child("Aliments Automatiques").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Iterable<DataSnapshot> list = snapshot.getChildren();
                    for (DataSnapshot s : list){
                        ajoutAutoAdapter.listAliment.add(s.getValue(AlimentAuto.class));
                    }

                    ajoutAutoAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context,"Problème inattendue",Toast.LENGTH_LONG).show();
            }
        });
    }

    //Le booléen fait savoir au méthode, si c'est un aliment auto qui va se faire ajouter.
    public void AjouterAliment(Aliment aliment, Uri imageUri, boolean AjoutAlimentAuto){

        rootStorage = FirebaseStorage.getInstance().getReference().child("AlimentImages");

        //Création de la clé qui sera la même pour l'image et l'aliment
        String key = _rootDataref.push().getKey();

        //Ajout de l'image d'abord, enfin de l'associer à l'aliment après
        rootStorage.child(key+".jpg").putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                rootStorage.child(key+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
                                GetListAlimentManuel();
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
        }); //addonprogresslistener possible.
    }

    //Le parametre de position permet de signaler à cette méthode à quel position on est rendu dans la liste des aliments auto à ajouter
    public void AjouterAutoAliment(){
        _rootDataref.child("AlimentsAutoEpicerie").setValue(alimentListAuto).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                GetListAlimentManuel();
                Toast.makeText(context,"Reussii",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,"Problème inattendue",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void SupprimerAliment(String key){
        _rootDataref.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                rootStorage.child(key+".jpg").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //do something
                    }
                });
            }
        });
    }


}
