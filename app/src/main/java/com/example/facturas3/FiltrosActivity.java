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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros);
        ArrayList<FacturasVO> listaFactura = pasarListaFacturas();
      
        //boton de cerrar la pagina de filtros
        MenuHost menu = this;
        menu.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_cerrar, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.botonCerrar:
                        Intent intent = new Intent(FiltrosActivity.this, MainActivity.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });


        //calendarios
        Button fechaDesde = (Button) findViewById(R.id.fechaDesde);
        Button fechaHasta = (Button) findViewById(R.id.fechaHasta);
        fechaDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(FiltrosActivity.this, (view, year1, monthofyear, dayofmonth) ->
                        fechaDesde.setText(dayofmonth + "/" + (monthofyear + 1) + "/" + year1), year, month, day);
                dpd.show();
            }

        });
        fechaHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(FiltrosActivity.this, (view, year1, monthofyear, dayofmonth) ->
                        fechaHasta.setText(dayofmonth + "/" + (monthofyear + 1) + "/" + year1), year, month, day);
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

        //botonAplicar
        Button botonAplicar=(Button) findViewById(R.id.botonAplicar);
        CheckBox checkPagadas = findViewById(R.id.checkPagadas);
        CheckBox checkAnuladas = findViewById(R.id.checkAnuladas);
        CheckBox checkCuota = findViewById(R.id.checkCuota);
        CheckBox checkPendientes = findViewById(R.id.checkPendientes);
        CheckBox checkPlan = findViewById(R.id.checkPlan);
        Button botonDesde= (Button) findViewById(R.id.fechaDesde);
        Button botonHasta=(Button) findViewById(R.id.fechaHasta);

        botonAplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                Intent intent=new Intent(FiltrosActivity.this,MainActivity.class);
                HashMap<String, Boolean> mapaCheckBox = new HashMap<>();
              //  intent.putExtra("importe", Double.parseDouble(central.getText().toString()));
                mapaCheckBox.put("Pagada", checkPagadas.isChecked());
                mapaCheckBox.put("Anulada", checkAnuladas.isChecked());
                mapaCheckBox.put("Cuota Fija", checkCuota.isChecked());
                mapaCheckBox.put("Pendiente de pago", checkPendientes.isChecked());
                mapaCheckBox.put("Plan de pago", checkPlan.isChecked());
                //intent.putExtra("fechaDesde", botonDesde.getText().toString());
               // intent.putExtra("fechaHasta", botonHasta.getText().toString());

                FiltroVO filtroEnviado = new FiltroVO(botonDesde.getText().toString(), botonHasta.getText().toString(),maxImporte, mapaCheckBox);

                intent.putExtra("filtro", gson.toJson(filtroEnviado));
                startActivity(intent);
            }
        });

        // TODO : poner las frchas en los strings
        //boton eliminar filtros
        Button botonEliminar = findViewById(R.id.botonEliminar);
        botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //fecha
                fechaDesde.setText("!!!!!!!!!!!");
                fechaHasta.setText("!!!!!!!!!");

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

    private ArrayList<FacturasVO> pasarListaFacturas() {
        ArrayList<FacturasVO> listaFacturas = getIntent().getParcelableArrayListExtra("facturas");
        Log.d("tamaño facturas", "" + listaFacturas.size());

        return listaFacturas;
    }


}