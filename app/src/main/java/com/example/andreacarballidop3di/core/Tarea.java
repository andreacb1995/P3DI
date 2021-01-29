package com.example.andreacarballidop3di.core;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

@Entity(tableName = "Tarea")
public class Tarea implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;
    public String textoTarea;
    public Date fecha;
    public boolean fav;
    public boolean fin;
    private boolean tareaSeleccionada;

    public Tarea(Date fecha, String textoTarea) {
        this.textoTarea = textoTarea;
        this.fecha = fecha;

        tareaSeleccionada = false;
    }



    public void modificarTarea(Date fecha, String textotarea) {
        this.fecha = fecha;
        this.textoTarea = textotarea;

    }
    public String getTextotarea() {
        return textoTarea;
    }

    public void setTextoTarea(String textoTarea) {
        this.textoTarea = textoTarea;
    }



    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }



    public String getTextoTarea() {
        return textoTarea;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getFormatoFecha() {

        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy");
        return formatoFecha.format(fecha);

    }

    public boolean isFav() {
        return fav;
    }

    public void setFav(boolean fav) {
        this.fav = fav;
    }

    public boolean isFin() {
        return fin;
    }

    public void setFin(boolean fin) {
        this.fin = fin;
    }

    public void modificar(String textoTarea, Date fecha) {

        this.textoTarea = textoTarea;
        this.fecha = fecha;
    }

    public boolean isTareaSeleccionada() {
        return tareaSeleccionada;
    }

    public void setTareaSeleccionada(boolean tareaSeleccionada) {
        this.tareaSeleccionada = tareaSeleccionada;
    }
}
