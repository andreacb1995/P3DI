package com.example.andreacarballidop3di.UI;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andreacarballidop3di.R;
import com.example.andreacarballidop3di.core.Tarea;
import com.example.andreacarballidop3di.database.TareaLab;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TercerFragment extends Fragment implements AccionesTarea{
    private RecyclerView recyclerView;
    List<Tarea> tareasTerminadas;
    List<Tarea> tareasDB;
    MyRecyclerViewAdapter adapter;
    List<Tarea> tareasSeleccionadas;
    List<Tarea> tareasFavoritas;
    LinearLayout myToolbar;
    public TercerFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment3, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvFragmento3);

        return rootView;
    }

    /*Método que se ejecutará cuando la Activity del Fragment esté completamente
    creada.*/
    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        myToolbar = getView().findViewById(R.id.toolbar3);
        myToolbar.setVisibility(View.GONE);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        tareasTerminadas  = new ArrayList<>();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyRecyclerViewAdapter(tareasTerminadas, this);
        recyclerView.setAdapter(adapter);

        tareasSeleccionadas = new ArrayList<>();
        tareasDB= TareaLab.get(getActivity()).getTareas();



    }


    public void añadirTarea(Tarea tareaModifico) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final View dialogLayout = getLayoutInflater().inflate(R.layout.dialog_tarea_nueva, null);
        builder.setView(dialogLayout);

        final TextView tvFecha = dialogLayout.findViewById(R.id.tvFecha);
        final EditText tvTarea = dialogLayout.findViewById(R.id.edTarea);

        final Calendar calendar = Calendar.getInstance();
        if (tareaModifico != null) {
            String tareaT = String.valueOf(tareaModifico.getTextotarea());
            String fecha = String.valueOf(tareaModifico.getFormatoFecha());
            tvFecha.setText(fecha);
            calendar.setTime(tareaModifico.getFecha());
            tvTarea.setText(tareaT);

        }

        tvFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                final DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        calendar.set(Calendar.YEAR, selectedYear);
                        calendar.set(Calendar.MONTH, selectedMonth);
                        calendar.set(Calendar.DAY_OF_MONTH, selectedDay);

                        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd MMMM 'de' yyyy", Locale.getDefault());
                        tvFecha.setText(formatoFecha.format(calendar.getTime()));
                    }
                }, year, month, day);

                datePicker.show();
            }
        });


        builder.setPositiveButton( "Añadir tarea", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String tTarea = tvTarea.getText().toString();

                if ( tvFecha.getText().toString().length()<=0 && tTarea.equals("")){
                    Toast.makeText(getActivity(), "No se permiten los campos vacíos", Toast.LENGTH_SHORT).show();
                    return;

                }
                if(tvFecha.getText().toString().length()<=0){
                    Toast.makeText(getActivity(), "Debe escoger una fecha", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(tTarea.equals("")){

                    Toast.makeText(getActivity(),  "Campo de la tarea vacío", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (tareaModifico == null) {
                    Tarea tarea = new Tarea(calendar.getTime(),tTarea);
                    TareaLab.get(getActivity()).addTarea(tarea);
//                    tareas.add(tarea);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "Tarea añadida", Toast.LENGTH_SHORT).show();

                } else {
                    tareaModifico.modificarTarea(calendar.getTime(),tTarea);
                    TareaLab.get(getActivity()).updateTarea(tareaModifico);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "Tarea modificada", Toast.LENGTH_SHORT).show();

                }




            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.create().show();
    }

    public void eliminarTarea(Tarea tarea) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Borrar Elemento");
        builder.setMessage("Está seguro de que desea eliminar este elemento?\n\n" + tarea.getTextoTarea());
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tareasTerminadas.remove(tarea);
                tareasDB.remove(tarea);
                TareaLab.get(getActivity()).deleteTarea(tarea);
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("No", null);
        builder.create().show();
    }

    public void tareaSeleccionada(Tarea tarea) {

        if (tarea.isTareaSeleccionada()) {
            tareasSeleccionadas.add(tarea);
        } else {
            tareasSeleccionadas.remove(tarea);
        }
        myToolbar.setVisibility(tareasSeleccionadas.isEmpty() ? View.GONE : View.VISIBLE);

        ImageView volver = getView().findViewById(R.id.toolbarVolver3);
        ImageView editarTarea =getView().findViewById(R.id.toolbarEditar3);
        ImageView eliminarTarea =getView().findViewById(R.id.toolbarEliminar3);
        editarTarea.setVisibility(tareasSeleccionadas.size() == 1 ? View.VISIBLE : View.GONE);

        volver.setOnClickListener(v -> {

            toolbarVolver();

        });


        editarTarea.setOnClickListener(v -> {

            añadirTarea(tarea);

        });

        eliminarTarea.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Borrar Elemento");
            builder.setMessage("Está seguro de que desea eliminar los elemento seleccionados?\n\n" + tarea.getTextoTarea());
            builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    for (Tarea t : tareasSeleccionadas) {
                        tareasSeleccionadas.remove(t);
                        tareasTerminadas.remove(t);
                        tareasDB.remove(t);
                        TareaLab.get(getActivity()).deleteTarea(tarea);
                    }
                    Toast.makeText(getActivity(), "Tareas eliminadas correctamente", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();

                }
            });
            builder.setNegativeButton("No", null);
            builder.create().show();

        });

    }
    @Override
    public void añadirTareaFinalizada(Tarea tarea) {
        tarea.setFin(true);
        tareasTerminadas.add(tarea);
        TareaLab.get(getActivity()).updateTarea(tarea);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void eliminarTareaFinalizada(Tarea tarea) {
        tarea.setFin(false);
        tareasTerminadas.remove(tarea);
        TareaLab.get(getActivity()).updateTarea(tarea);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void añadirTareaFavorita(Tarea tarea) {
        tarea.setFav(true);
//        tareasImportantes.add(tarea);
        TareaLab.get(getActivity()).updateTarea(tarea);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void eliminarTareaFavorita(Tarea tarea) {
        tarea.setFav(false);
//        tareasImportantes.remove(tarea);
        TareaLab.get(getActivity()).updateTarea(tarea);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void actualizarListaTareas(Tarea tarea) {

    }

    public void toolbarVolver() {

        for (Tarea tarea : tareasSeleccionadas)
            tarea.setTareaSeleccionada(false);

        tareasSeleccionadas.clear();
        myToolbar.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
    }



    @Override
    public void onResume() {
        super.onResume();
        for (Tarea tarea : tareasDB) {
            if(tarea.isFin()){
                tareasTerminadas.add(tarea);
            }
        }
        adapter.notifyDataSetChanged();

    }
}
