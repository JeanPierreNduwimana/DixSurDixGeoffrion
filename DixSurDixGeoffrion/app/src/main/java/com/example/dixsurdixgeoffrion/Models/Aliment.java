package com.example.dixsurdixgeoffrion.Models;

import java.time.LocalDateTime;
import java.util.Date;

public class Aliment {

    public String alimentKey;
    public String nom;
    public String description;
    public int quantite;
    public Boolean validerAchat;
    public String imageUri;
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
