package com.example.dixsurdixgeoffrion.Models;

public class Aliment {

    public String Nom;
    public String Description;
    public String UserOwnerKey;
    public int Quantite;
    public Boolean ValiderAchat;
    public int Photo;

    public Aliment(String Nom, String Description, String UserOwnerKey, int Quantite,Boolean ValiderAchat, int photo){

        this.Nom = Nom;
        this.Description = Description;
        this.UserOwnerKey = UserOwnerKey;
        this.Quantite = Quantite;
        this.ValiderAchat = ValiderAchat;
        this.Photo = photo;

    }
}
