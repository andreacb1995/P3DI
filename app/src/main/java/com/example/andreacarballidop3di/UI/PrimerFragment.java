package com.example.andreacarballidop3di.UI;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andreacarballidop3di.R;
import com.example.andreacarballidop3di.core.Tarea;
import com.example.andreacarballidop3di.database.TareaLab;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PrimerFragment  extends Fragment implements AccionesTarea{
    public static PrimerFragment PrimerFragment;
    private RecyclerView recyclerView;
    List<Tarea> tareas;
    List<Tarea> listaTareas;
    MyRecyclerViewAdapter adapter;
    private TareaLab tareaLab;
    List<Tarea> tareasSeleccionadas;
    List<Tarea> tareasFavoritas;
    List<Tarea> tareasFinalizadas;
    ArrayList<Tarea> tareasFinalizadasOnResume;

    LinearLayout myToolbar;


    public PrimerFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment1, container, false);
            recyclerView = (RecyclerView) rootView.findViewById(R.id.rvFragmento1);
            PrimerFragment = this;
            tareasSeleccionadas = new ArrayList<>();
            tareasFavoritas= new ArrayList<>();
            tareasFinalizadas=new ArrayList<>();
            listaTareas  = new ArrayList<>();
            tareas  = new ArrayList<>();
            tareaLab = TareaLab.get(getContext());
            listaTareas = tareaLab.getTareas();

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyRecyclerViewAdapter(listaTareas,this);
        recyclerView.setAdapter(adapter);
            return rootView;

    }

    /*Método que se ejecutará cuando la Activity del Fragment esté completamente
    creada.*/
    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        myToolbar = getView().findViewById(R.id.toolbar1);
        myToolbar.setVisibility(View.GONE);
        tareasSeleccionadas = new ArrayList<>();




    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



//        for(int i = 0; i < listaTareas.size(); i++){
//            tareas.add(listaTareas.get(i));
//        }



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
                    tareaLab.addTarea(tarea);
                    listaTareas.add(tarea);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "Tarea añadida", Toast.LENGTH_SHORT).show();

                } else {
                    tareaModifico.modificarTarea(calendar.getTime(),tTarea);
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

                tareas.remove(tarea);
                tareaLab.deleteTarea(tarea);
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

        ImageView volver = getView().findViewById(R.id.toolbarVolver1);
        ImageView editarTarea =getView().findViewById(R.id.toolbarEditar1);
        ImageView eliminarTarea =getView().findViewById(R.id.toolbarEliminar1);
        ImageView tareaFinalizada =getView().findViewById(R.id.toolbarTareaFinalizada1);
        editarTarea.setVisibility(tareasSeleccionadas.size() == 1 ? View.VISIBLE : View.GONE);

        volver.setOnClickListener(v -> {

            toolbarVolver();

        });

        editarTarea.setOnClickListener(v -> {

            añadirTarea(tarea);

        });

        eliminarTarea.setOnClickListener(v -> {
            eliminarTareas();

        });

        tareaFinalizada.setOnClickListener(v -> {
            for (Tarea t : tareasSeleccionadas) {
                añadirTareaFinalizada(t);
                Toast.makeText(getActivity(), "Tareas añadidas a la lista de tareas finalizadas", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void añadirTareaFinalizada(Tarea tarea) {
        tarea.setFin(true);
        tareasFinalizadas.add(tarea);
        tareaLab.updateTarea(tarea);
//        listaTareas.remove(tarea);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void eliminarTareaFinalizada(Tarea tarea) {
        tarea.setFin(false);
        tareasFinalizadas.remove(tarea);
        tareaLab.updateTarea(tarea);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void añadirTareaFavorita(Tarea tarea) {
        tarea.setFav(true);
        tareasFavoritas.add(tarea);
        tareaLab.updateTarea(tarea);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void eliminarTareaFavorita(Tarea tarea) {
        tarea.setFav(false);
        tareasFavoritas.remove(tarea);
        tareaLab.updateTarea(tarea);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void actualizarListaTareas(Tarea tarea) {
        tareaLab.addTarea(tarea);
        listaTareas.add(tarea);
        adapter.notifyDataSetChanged();
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
//        for (Tarea t : listaTareas){
//            if(t.isFin()){
//                tareas.remove(t);
//            }
//        }

        tareasFinalizadasOnResume = new ArrayList<>();


        for (Tarea t : listaTareas) {
            if (t.getFecha().compareTo(new Date()) < 0) {
                tareasFinalizadasOnResume.add(t);
            }
        }
        if (!tareasFinalizadasOnResume.isEmpty()) {
            tareasCaducadas(tareasFinalizadasOnResume);
        }
    }


    private void tareasCaducadas(final List<Tarea> tareasfinalizadas) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Tareas Finalizadas");
        ArrayList<String> listatareasfinalizadas = new  ArrayList<String>();
        String[] stringtareas = new String[tareasfinalizadas.size()];
        String tareaseliminar = null;
        final boolean[] tareaseleccion = new boolean[tareasfinalizadas.size()];

        for (int i = 0; i < tareasfinalizadas.size(); i++) {
            stringtareas[i] = "Tarea: " + tareasfinalizadas.get(i).getTextotarea() + "\nFecha: "
                    + tareasfinalizadas.get(i).getFormatoFecha();
            tareaseliminar =  stringtareas[i];
        }
        builder.setMultiChoiceItems(stringtareas, tareaseleccion, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i, boolean isChecked) {
                tareaseleccion[i] = isChecked;
            }
        });
        final String listaTareasElimino = tareaseliminar;
        builder.setNegativeButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("¿Eliminar el elemento?");
                builder.setMessage(listaTareasElimino);
                AlertDialog.Builder buildereliminar = new AlertDialog.Builder(getContext());
                buildereliminar.setMessage("¿Eliminar los elementos?");
                buildereliminar.setNegativeButton("Cancelar", null);
                buildereliminar.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for (int i = tareasfinalizadas.size() - 1; i >= 0; i--) {
                            if (tareaseleccion[i]) {
                                listaTareas.remove(tareasfinalizadas.get(i));
                                TareaLab.get(getContext()).deleteTarea(tareasfinalizadas.get(i));
                            }
                        }
                        Toast.makeText(getActivity(), "Tareas eliminadas correctamente", Toast.LENGTH_SHORT).show();
                       adapter.notifyDataSetChanged();
                    }
                });

                buildereliminar.create().show();
            }
        });
        builder.setPositiveButton("Añadir a tareas finalizadas", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for (int i = tareasfinalizadas.size() - 1; i >= 0; i--) {
                    if (tareaseleccion[i]) {
                        añadirTareaFinalizada(tareasfinalizadas.get(i));
                    }
                }
                Toast.makeText(getActivity(), "Tareas añadidas a la lista de tareas finalizadas", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }
        });

        builder.setNeutralButton("Cancelar", null);
        builder.create().show();
    }

    private void eliminarTareas() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Eliminar tareas");
        ArrayList<String> listatareasfinalizadas = new  ArrayList<String>();
        String[] stringtareas = new String[tareasSeleccionadas.size()];
        String tareaseliminar = null;
        final boolean[] tareaseleccion = new boolean[tareasSeleccionadas.size()];

        for (int i = 0; i < tareasSeleccionadas.size(); i++) {
            stringtareas[i] = "Tarea: " + tareasSeleccionadas.get(i).getTextotarea() + "\nFecha: "
                    + tareasSeleccionadas.get(i).getFormatoFecha();
            tareaseliminar =  stringtareas[i];
        }
        builder.setMultiChoiceItems(stringtareas, tareaseleccion, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i, boolean isChecked) {
                tareaseleccion[i] = isChecked;
            }
        });
        final String listaTareasElimino = tareaseliminar;
        builder.setNegativeButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("¿Eliminar el elemento?");
                builder.setMessage(listaTareasElimino);
                AlertDialog.Builder buildereliminar = new AlertDialog.Builder(getContext());
                buildereliminar.setMessage("¿Eliminar los elementos?");
                buildereliminar.setNegativeButton("Cancelar", null);
                buildereliminar.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for (int i = tareasSeleccionadas.size() - 1; i >= 0; i--) {
                            if (tareaseleccion[i]) {
                                listaTareas.remove(tareasSeleccionadas.get(i));
                                TareaLab.get(getContext()).deleteTarea(tareasSeleccionadas.get(i));

                            }
                        }
                        Toast.makeText(getActivity(), "Tareas eliminadas correctamente", Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                    }
                });

                buildereliminar.create().show();
            }
        });


        builder.setPositiveButton("Cancelar", null);
        builder.create().show();
    }

}
