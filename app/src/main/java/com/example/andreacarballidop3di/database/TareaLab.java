package com.example.andreacarballidop3di.database;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.room.Room;

import com.example.andreacarballidop3di.core.Tarea;

import java.util.List;

public class TareaLab {

    @SuppressLint("StaticFieldLeak")
    private static TareaLab tareaLab;

    private TareaDao tareaDao;

    private TareaLab(Context context){
        Context appContext = context.getApplicationContext();
        TareaDataBase.AppDatabase database = Room.databaseBuilder(appContext, TareaDataBase.AppDatabase.class, "tarea").allowMainThreadQueries().build(); //.addMigrations ( MIGRATION_1_2 )
        tareaDao = database.getTareaDao();
    }

    public static TareaLab get(Context context) {
        if (tareaLab == null) {
            tareaLab = new TareaLab(context);
        }
        return tareaLab;
    }

    public List<Tarea> getTareas() {
        return tareaDao.getTareas();
    }

    /*public List<Tarea> getTareasFavoritas() {
        return tareaDao.getTareasFavoritas();
    }

    public List<Tarea> getTareasFinalizadas() {
        return tareaDao.getTareasFinalizadas();
    }*/

    public void addTarea(Tarea tarea)
    {
        tareaDao.add(tarea);
    }

    public void updateTarea(Tarea tarea)
    {
        tareaDao.update(tarea);
    }

    public void deleteTarea(Tarea tarea) {

        tareaDao.delete(tarea);
    }
}
