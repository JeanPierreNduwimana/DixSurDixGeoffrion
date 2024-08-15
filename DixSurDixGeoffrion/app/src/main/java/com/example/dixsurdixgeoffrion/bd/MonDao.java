package com.example.dixsurdixgeoffrion.bd;

import androidx.room.Dao;
import com.example.dixsurdixgeoffrion.Models.Aliment;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MonDao {


    @Query("SELECT * FROM Aliment")
    List<Aliment> touslesaliments();

    @Query("SELECT * FROM Aliment WHERE alimentKey = :alimentKey")
    Aliment DetailsAliment(String alimentKey);
}
