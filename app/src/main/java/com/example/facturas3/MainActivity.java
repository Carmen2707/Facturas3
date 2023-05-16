package com.example.facturas3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.facturas3.io.ApiAdapter;
import com.example.facturas3.io.response.FacturasResponse;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private FacturasAdapter adapter;
    private RecyclerView rv1;
    private ArrayList<FacturasVO> facturas;
    public static int maxImporte;
    TextView filtroVacio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv1 = findViewById(R.id.rv1);
        filtroVacio = findViewById(R.id.filtroVacio);

        //boton para ir a la pagina de filtros
        MenuHost menu = this;
        menu.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_filtros, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.botonFiltro) {
                    Intent intent = new Intent(MainActivity.this, FiltrosActivity.class);
                    intent.putExtra("facturas", facturas);
                    intent.putExtra("maxImporte", maxImporte);
                    startActivity(intent);
                    return true;
                } else {
                    return false;
                }
            }

        });


        Call<FacturasResponse> call = ApiAdapter.getApiService().getFacturas();
        call.enqueue(new Callback<FacturasResponse>() {
            @Override
            public void onResponse(Call<FacturasResponse> call, Response<FacturasResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("facturas cargadas", response.body().getFacturas().toString());

                    facturas = (ArrayList<FacturasVO>) response.body().getFacturas();
                    maxImporte = calcularMaximoImporte(facturas);

                    String datosFiltro = getIntent().getStringExtra(Constantes.FILTRO_DATOS);
                    if (datosFiltro != null) {
                        facturas = llenarDatos(datosFiltro);
                    }
                    adapter = new FacturasAdapter(facturas);
                    rv1.setAdapter(adapter);

                }
            }


            @Override
            public void onFailure(Call<FacturasResponse> call, Throwable t) {
                Log.d("onFailure", "Fallo callback de enqueue");//metodo vacio
            }
        });
    }

    private int calcularMaximoImporte(ArrayList<FacturasVO> listaFactura) {
        int maxImporte = 0;

        for (FacturasVO factura : facturas) {
            double maxFactura = factura.getImporteOrdenacion();
            if (maxImporte < maxFactura) {
                maxImporte = (int) Math.ceil(maxFactura);
            }
        }
        return maxImporte;
    }

    private ArrayList<FacturasVO> llenarDatos(String datosFiltro) {
        FiltroVO filtros = new Gson().fromJson(datosFiltro, FiltroVO.class);
        ArrayList<FacturasVO> filtroLista;


        filtroLista = filtroSeekBar(filtros.getImporteSeleccionado());
        if (!Objects.equals(filtros.getFechaInicio(), getBaseContext().getResources().getString(R.string.activity_filtros_botonFecha)) ||
                !Objects.equals(filtros.getFechaFin(), getBaseContext().getResources().getString(R.string.activity_filtros_botonFecha))) {
            filtroLista = filtroFecha(filtros.getFechaInicio(), filtros.getFechaFin(), filtroLista);
        }


        boolean estaCheckeado = false;
        for (Map.Entry<String, Boolean> entry : filtros.getMapaCheckBox().entrySet()) {
            if (Boolean.TRUE.equals(entry.getValue())) {
                estaCheckeado = true;
                break;
            }
        }
        if (estaCheckeado) {
            filtroLista = filtroCheckBox(filtros.getMapaCheckBox(), filtroLista);
        }
        if (filtroLista.isEmpty()) {

            filtroVacio.setVisibility(View.VISIBLE);
        }
        return filtroLista;
    }

    //filtrar estado
    private ArrayList<FacturasVO> filtroCheckBox(HashMap<String, Boolean> mapaCheckBox, ArrayList<FacturasVO> filtroLista) {
        ArrayList<FacturasVO> listaAux = new ArrayList<>();
        for (FacturasVO factura : filtroLista) {
            String descEstado = factura.getDescEstado();
            if (mapaCheckBox.containsKey(descEstado) && Boolean.TRUE.equals(mapaCheckBox.get(descEstado))) {
                listaAux.add(factura);
            }
        }
        return listaAux;
    }

    // filtrar importe
    private ArrayList<FacturasVO> filtroSeekBar(int maxImporte) {
        ArrayList<FacturasVO> listaAux = new ArrayList<>();
        for (FacturasVO facturaSeekBar : facturas) {
            if (Double.parseDouble(String.valueOf(facturaSeekBar.getImporteOrdenacion())) < maxImporte) {
                listaAux.add(facturaSeekBar);
            }
        }
        return listaAux;
    }


    //filtrar fechas
    private ArrayList<FacturasVO> filtroFecha(String fechaInicio, String fechaFin, ArrayList<FacturasVO> filtroLista) {

        ArrayList<FacturasVO> listaAux = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyyy");
        Date fechaDesde = new Date();
        Date fechaHasta = new Date();

        try {
            fechaDesde = sdf.parse(fechaInicio);
            fechaHasta = sdf.parse(fechaFin);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (FacturasVO facturaFecha : filtroLista) {
            Date fechaFactura;
            try {
                fechaFactura = sdf.parse(facturaFecha.getFecha());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            if (fechaFactura.after(fechaDesde) && fechaFactura.before(fechaHasta)) {
                listaAux.add(facturaFecha);
            }
        }

        return listaAux;
    }
}