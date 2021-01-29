package com.example.andreacarballidop3di.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andreacarballidop3di.R;
import com.example.andreacarballidop3di.core.Tarea;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private static List<Tarea> items;

    private AccionesTarea accionesTarea;

   public MyRecyclerViewAdapter(List<Tarea> items, AccionesTarea accionesTarea) {
        this.items=items;
        this.accionesTarea=accionesTarea;
        this.context = context;

    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private AccionesTarea accionesTarea;
        // Campos respectivos de un item
        private CardView cv;
        private TextView fecha;
        private TextView textotarea;
        private ImageView imagencheckbox;
        private ImageView imagencheckboxMarcado;
        private ImageView imagenfavorito;
        private ImageView imagenfavoritoRelleno;

        private ImageView imagenborrar;
        private static List<Tarea> tareasFavoritas;
        private static List<Tarea> tareasFinalizadas;
        private ConstraintLayout constraintLayout;

        public ViewHolder(View v, AccionesTarea accionesTarea) {
            super(v);

            cv= (CardView) v.findViewById(R.id.cv);
            fecha = (TextView) v.findViewById(R.id.tv_card_Fecha);
            textotarea = (TextView) v.findViewById(R.id.tv_card_Tarea);
            imagencheckbox= (ImageButton)v.findViewById(R.id.imagencheckbox);
            imagencheckboxMarcado= (ImageButton)v.findViewById(R.id.imagencheckboxMarcado);
            imagenfavorito= (ImageButton)v.findViewById(R.id.imagenfavorito);
            imagenfavoritoRelleno= (ImageButton)v.findViewById(R.id.imagenfavoritoRelleno);
            imagenborrar= (ImageButton)v.findViewById(R.id.imagenborrar);
            constraintLayout = v.findViewById(R.id.constraintTarea);
            this.accionesTarea = accionesTarea;

            tareasFavoritas= new ArrayList<>();
            tareasFinalizadas= new ArrayList<>();


        }

        public void mostrarTarea(final Tarea tarea) {

            fecha.setText(tarea.getFormatoFecha());
            textotarea.setText(tarea.getTextotarea());

            imagenfavorito.setVisibility(tarea.isFav() ? View.INVISIBLE : View.VISIBLE);
            imagenfavoritoRelleno.setVisibility(tarea.isFav() ? View.VISIBLE : View.INVISIBLE);

            imagencheckbox.setVisibility(tarea.isFin() ? View.INVISIBLE : View.VISIBLE);
            imagencheckboxMarcado.setVisibility(tarea.isFin() ? View.VISIBLE : View.INVISIBLE);

            fondoTarea(tarea);

            imagencheckbox.setOnClickListener(v -> {
                imagencheckboxMarcado.setVisibility(View.VISIBLE);
                imagencheckbox.setVisibility(View.INVISIBLE);
                accionesTarea.añadirTareaFinalizada(tarea);
                tareasFinalizadas.add(tarea);


            });
            imagencheckboxMarcado.setOnClickListener(v -> {
                imagencheckbox.setVisibility(View.VISIBLE);
                imagencheckboxMarcado.setVisibility(View.INVISIBLE);
                accionesTarea.eliminarTareaFinalizada(tarea);
                tareasFinalizadas.remove(tarea);

            });

            imagenfavorito.setOnClickListener(v -> {
                imagenfavorito.setVisibility(View.INVISIBLE);
                imagenfavoritoRelleno.setVisibility(View.VISIBLE);
                accionesTarea.añadirTareaFavorita(tarea);
                tareasFavoritas.add(tarea);

            });

            imagenfavoritoRelleno.setOnClickListener(v -> {
                imagenfavoritoRelleno.setVisibility(View.INVISIBLE);
                imagenfavorito.setVisibility(View.VISIBLE);
                accionesTarea.eliminarTareaFavorita(tarea);
                tareasFavoritas.remove(tarea);

            });

            imagenborrar.setOnClickListener(v -> {
                    accionesTarea.eliminarTarea(tarea);
            });



            cv.setOnClickListener(v -> {

                tarea.setTareaSeleccionada(!tarea.isTareaSeleccionada());
                fondoTarea(tarea);
                accionesTarea.tareaSeleccionada(tarea);
            });

        }
        private void fondoTarea(Tarea tarea) {
            final String colorSeleccionado = "#8Affa4a2";
            final String colorNOSeleccionado = "#ffa4a2";
            int color = Color.parseColor(tarea.isTareaSeleccionada() ? colorSeleccionado : colorNOSeleccionado);
            constraintLayout.setBackgroundTintList(ColorStateList.valueOf(color));
        }

    }
    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_tarea, viewGroup, false);
        ViewHolder pvh = new ViewHolder(v,accionesTarea);
        return pvh;
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.mostrarTarea(items.get(i));
    }

//    public MyRecyclerViewAdapter(MainActivity mainActivity, ArrayList<Tarea> items) {
//        this.items = items;
//    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



}
