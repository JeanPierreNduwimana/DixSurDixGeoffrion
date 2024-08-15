package com.example.dixsurdixgeoffrion.bd;

import androidx.room.Dao;
import com.example.dixsurdixgeoffrion.Models.Aliment;

import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MonDao {


    @Insert
    Long AjoutAliment(Aliment a);

    @Query("SELECT * FROM Aliment")
    List<Aliment> touslesaliments();

    @Query("SELECT * FROM Aliment WHERE alimentKey = :alimentKey")
    Aliment DetailsAliment(String alimentKey);

    @Query("DELETE FROM Aliment")
    void SupprimerLesAliments();
}
