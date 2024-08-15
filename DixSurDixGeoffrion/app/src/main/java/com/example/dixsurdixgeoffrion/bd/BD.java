package com.example.dixsurdixgeoffrion.bd;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.dixsurdixgeoffrion.Models.Aliment;
import com.example.dixsurdixgeoffrion.Models.AlimentAuto;

@Database(entities = {Aliment.class, AlimentAuto.class}, version = 3)
public abstract class BD extends RoomDatabase {
    public abstract MonDao monDao();
}
