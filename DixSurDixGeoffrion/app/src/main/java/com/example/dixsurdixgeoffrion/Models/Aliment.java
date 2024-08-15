package com.example.dixsurdixgeoffrion.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class Aliment {

    @PrimaryKey(autoGenerate = true)
    public Long alimentId;
    public String alimentKey;
    public String nom;
    public String description;
    public int quantite;
    public Boolean validerAchat;
    public String imageUri;
    @Ignore
    public Date dateAjout;
    public Boolean alimentauto;

    public Aliment(String Nom, String Description, int Quantite,Boolean ValiderAchat, String imageUri, Date dateAjout, Boolean alimentauto){

        this.nom = Nom;
        this.description = Description;
        this.quantite = Quantite;
        this.validerAchat = ValiderAchat;
        this.imageUri = imageUri;
        this.dateAjout = dateAjout;
        this.alimentauto = alimentauto;

    }

    public Aliment(){}
    public String getAlimentKey() {
        return alimentKey;
    }
    public String getNom() {
        return nom;
    }
    public String getDescription() {
        return description;
    }
    public int getQuantite() {
        return quantite;
    }
    public Boolean getValiderAchat() {
        return validerAchat;
    }
    public String getImageUri() {
        return imageUri;
    }
    public Date getDateAjout() {
        return dateAjout;
    }
    public Boolean getAlimentauto() {return alimentauto;}
}
