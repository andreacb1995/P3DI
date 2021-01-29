package com.example.andreacarballidop3di.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.andreacarballidop3di.core.Tarea;

import java.util.List;

@Dao
public interface TareaDao {

    @Query("SELECT * FROM tarea")
    List<Tarea> getTareas();

    //@Query("SELECT * FROM tarea WHERE esFav = 1")
    //List<Tarea> getTareasImportantes();

    //@Query("SELECT * FROM tarea WHERE finalizado = 1")
    //List<Tarea> getTareasFinalizadas();

    @Insert
    void add(Tarea tarea);

    @Delete
    void delete(Tarea tarea);

    @Update
    void update(Tarea tarea);
}
