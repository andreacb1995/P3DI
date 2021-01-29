package com.example.andreacarballidop3di.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andreacarballidop3di.R;
import com.example.andreacarballidop3di.core.Tarea;
import com.example.andreacarballidop3di.database.TareaLab;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,AccionesTarea{
    private boolean viewIsAtHome;

    List<Tarea> listaTareas;
    List<Tarea> tareas;
    ArrayList<Tarea> tareasfinalizadas;
    private MyRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    TareaLab tareaLab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setSupportActionBar(myToolbar);
//        myToolbar.setVisibility(View.GONE);
        //loading the default fragment
        loadFragment(new PrimerFragment());

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        tareaLab = TareaLab.get(this);
        listaTareas = tareaLab.getTareas();
        FloatingActionButton btAdd = findViewById(R.id.btAdd);

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                añadirTarea(null);
//                PrimerFragment.PrimerFragment.añadirTarea(null);
            }
        });


    }

    public void añadirTarea(Tarea tareaModifico) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

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

                final DatePickerDialog datePicker = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {

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
                    Toast.makeText(MainActivity.this, "No se permiten los campos vacíos", Toast.LENGTH_SHORT).show();
                    return;

                }
                if(tvFecha.getText().toString().length()<=0){
                    Toast.makeText(MainActivity.this, "Debe escoger una fecha", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(tTarea.equals("")){

                    Toast.makeText(MainActivity.this,  "Campo de la tarea vacío", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (tareaModifico == null) {
                    Tarea tarea = new Tarea(calendar.getTime(),tTarea);
                    PrimerFragment.PrimerFragment.actualizarListaTareas(tarea);
                    Toast.makeText(MainActivity.this, "Tarea añadida", Toast.LENGTH_SHORT).show();

                } else {
                    tareaModifico.modificarTarea(calendar.getTime(),tTarea);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Tarea modificada", Toast.LENGTH_SHORT).show();

                }




            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.create().show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.tareasRealizar:
                fragment = new PrimerFragment();
                break;

            case R.id.tareasImportantes:
                fragment = new SegundoFragment();
                break;

            case R.id.tareasFinalizadas:
                fragment = new TercerFragment();
                break;

        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }


    @Override
    public void eliminarTarea(Tarea tarea) {

    }

    @Override
    public void tareaSeleccionada(Tarea tarea) {

    }

    @Override
    public void añadirTareaFinalizada(Tarea tarea) {

    }

    @Override
    public void eliminarTareaFinalizada(Tarea tarea) {

    }

    @Override
    public void añadirTareaFavorita(Tarea tarea) {

    }

    @Override
    public void eliminarTareaFavorita(Tarea tarea) {

    }

    @Override
    public void actualizarListaTareas(Tarea tarea) {

    }


}