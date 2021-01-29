package com.example.andreacarballidop3di.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.andreacarballidop3di.core.DateConverter;
import com.example.andreacarballidop3di.core.Tarea;

public class TareaDataBase {
    @Database(entities = {Tarea.class}, version = 1, exportSchema = false)
    @TypeConverters(DateConverter.class)
    public static abstract class AppDatabase extends RoomDatabase {
        public abstract TareaDao getTareaDao();
    }
}
