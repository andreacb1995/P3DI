package com.example.andreacarballidop3di.UI;

import com.example.andreacarballidop3di.core.Tarea;

public interface AccionesTarea {


    void eliminarTarea(Tarea tarea);

    void tareaSeleccionada(Tarea tarea);

    void añadirTareaFinalizada(Tarea tarea);

    void eliminarTareaFinalizada(Tarea tarea);

    void añadirTareaFavorita(Tarea tarea);

    void eliminarTareaFavorita(Tarea tarea);

    void actualizarListaTareas(Tarea tarea);
}
