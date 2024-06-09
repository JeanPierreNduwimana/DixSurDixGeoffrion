package com.example.dixsurdixgeoffrion.Models;

import java.util.Date;

public class Profile {

    public String profileKey;
    public String profileName;
    public String imgUri;
    public String anniversary;
    public int rating;


    public Profile(String profileName, String anniversary){
        this.profileName = profileName;
        this.anniversary = anniversary;
    }

    public Profile(){}

    public String getProfileName() {
        return profileName;
    }
    public String getAnniversary() {
        return anniversary;
    }

    public String getImgUri() {
        return imgUri;
    }

    public int getRating() {
        return rating;
    }
}
