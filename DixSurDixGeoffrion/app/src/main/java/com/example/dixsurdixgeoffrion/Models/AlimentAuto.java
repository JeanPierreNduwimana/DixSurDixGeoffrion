package com.example.dixsurdixgeoffrion.Models;

import java.util.Date;

public class AlimentAuto {

    public String alimentKey;
    public String nom;
    public String description;
    public int quantite;
    public Boolean validerAchat;
    public String imageUri;
    public boolean used;

    public AlimentAuto(String Nom, String Description, int Quantite, Boolean ValiderAchat, String imageUri, boolean used){

        this.nom = Nom;
        this.description = Description;
        this.quantite = Quantite;
        this.validerAchat = ValiderAchat;
        this.imageUri = imageUri;
        this.used = used;

    }

    public AlimentAuto(){}
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
    public boolean isUsed() {
        return used;
    }
}
