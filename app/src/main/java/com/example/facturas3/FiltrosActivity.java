package com.example.facturas3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class FiltrosActivity extends AppCompatActivity {
    FiltroVO filtros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros);

        //boton para cerrar la pagina de filtros y volver a la principal
        MenuHost menu = this;
        menu.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_cerrar, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.botonCerrar) {
                    Intent intent = new Intent(FiltrosActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        //declarar variables
        Button botonAplicar = (Button) findViewById(R.id.botonAplicar);
        CheckBox checkPagadas = findViewById(R.id.checkPagadas);
        CheckBox checkAnuladas = findViewById(R.id.checkAnuladas);
        CheckBox checkCuota = findViewById(R.id.checkCuota);
        CheckBox checkPendientes = findViewById(R.id.checkPendientes);
        CheckBox checkPlan = findViewById(R.id.checkPlan);
        Button botonDesde = (Button) findViewById(R.id.fechaDesde);
        Button botonHasta = (Button) findViewById(R.id.fechaHasta);


        //calendarios
        botonDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(FiltrosActivity.this, (view, year1, monthofyear, dayofmonth) ->
                        botonDesde.setText(dayofmonth + "/" + (monthofyear + 1) + "/" + year1), year, month, day);
                dpd.show();
            }

        });
        botonHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(FiltrosActivity.this, (view, year1, monthofyear, dayofmonth) ->
                        botonHasta.setText(dayofmonth + "/" + (monthofyear + 1) + "/" + year1), year, month, day);
                dpd.show();
            }
        });


        //slider
        SeekBar seekBar = findViewById(R.id.seekBar);
        TextView maximo = findViewById(R.id.maximo);
        TextView central = findViewById(R.id.central);
        int maxImporte = getIntent().getIntExtra("maxImporte", 0);
        seekBar.setMax(maxImporte);
        seekBar.setProgress(maxImporte);
        maximo.setText(String.valueOf(maxImporte));
        central.setText(String.valueOf(maxImporte));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                central.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d("onStartTrackingTouch", "onStartTrackingTouch: ha fallado");//metodo vacio
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("onStopTrackingTouch", "onStopTrackingTouch: ha fallado");//metodo vacio
            }
        });

        //Hace que los filtros se queden fijos cuando salimos a la pagina principal
        filtros = new Gson().fromJson(getIntent().getStringExtra(Constantes.FILTRO_DATOS), FiltroVO.class);
        if (filtros != null) {
            botonDesde.setText(filtros.getFechaInicio());
            botonHasta.setText(filtros.getFechaFin());
            seekBar.setProgress(filtros.getImporteSeleccionado());
            checkPagadas.setChecked(filtros.getMapaCheckBox().get(Constantes.PAGADAS));
            checkAnuladas.setChecked(filtros.getMapaCheckBox().get(Constantes.ANULADAS));
            checkCuota.setChecked(filtros.getMapaCheckBox().get(Constantes.CUOTA_FIJA));
            checkPendientes.setChecked(filtros.getMapaCheckBox().get(Constantes.PENDIENTES_PAGO));
            checkPlan.setChecked(filtros.getMapaCheckBox().get(Constantes.PLAN_PAGO));
        }
        //boton aplicar
        botonAplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                Intent intent = new Intent(FiltrosActivity.this, MainActivity.class);
                HashMap<String, Boolean> mapaCheckBox = new HashMap<>();
                mapaCheckBox.put(Constantes.PAGADAS, checkPagadas.isChecked());
                mapaCheckBox.put(Constantes.ANULADAS, checkAnuladas.isChecked());
                mapaCheckBox.put(Constantes.CUOTA_FIJA, checkCuota.isChecked());
                mapaCheckBox.put(Constantes.PENDIENTES_PAGO, checkPendientes.isChecked());
                mapaCheckBox.put(Constantes.PLAN_PAGO, checkPlan.isChecked());

                FiltroVO filtroEnviado = new FiltroVO(botonDesde.getText().toString(), botonHasta.getText().toString(),
                        maxImporte, Integer.parseInt(central.getText().toString()), mapaCheckBox);

                intent.putExtra("filtro", gson.toJson(filtroEnviado));
                startActivity(intent);
            }
        });

        //boton eliminar filtros
        Button botonEliminar = findViewById(R.id.botonEliminar);
        botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //fecha
                botonDesde.setText(R.string.activity_filtros_botonFecha);
                botonHasta.setText(R.string.activity_filtros_botonFecha);

                //importe
                seekBar.setMax(maxImporte);
                seekBar.setProgress(maxImporte);
                central.setText(String.valueOf(maxImporte));

                //estado
                checkPagadas.setChecked(false);
                checkAnuladas.setChecked(false);
                checkCuota.setChecked(false);
                checkPendientes.setChecked(false);
                checkPlan.setChecked(false);
            }
        });
    }

}