package com.example.facturas3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.facturas3.io.ApiAdapter;
import com.example.facturas3.io.response.FacturasResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Callback<FacturasResponse> {

    private FacturasAdapter adapter;
    private RecyclerView rv1;
    public static Double maxImporte = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv1 = findViewById(R.id.rv1);

        Call<FacturasResponse> call = ApiAdapter.getApiService().getFacturas();
        call.enqueue(this);

        //boton para ir a la pagina de filtros
        MenuHost menu=this;
        menu.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_filtros, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.botonFiltro:
                        Intent intent=new Intent(MainActivity.this,FiltrosActivity.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });
    }


    @Override
    public void onResponse(Call<FacturasResponse> call, Response<FacturasResponse> response) {
        if (response.isSuccessful()) {
            List<FacturasVO> facturas = response.body().getFacturas();

            boolean checkBoxPagadas = getIntent().getBooleanExtra("Pagada", false);
            boolean checkBoxPagadas2 = getIntent().getBooleanExtra("Pendiente de pago", false);
            boolean checkBoxPagadas3 = getIntent().getBooleanExtra("Anulada", false);
            boolean checkBoxPagadas4 = getIntent().getBooleanExtra("Cuota Fija", false);
            boolean checkBoxPagadas5 = getIntent().getBooleanExtra("Plan de pago", false);
            //checkbox

            if (checkBoxPagadas || checkBoxPagadas2 || checkBoxPagadas3 || checkBoxPagadas4 || checkBoxPagadas5) {
                ArrayList<FacturasVO> listFiltro2 = new ArrayList<>();

                for (FacturasVO factura : facturas) {
                    if (factura.getDescEstado().equals("Pagada") && checkBoxPagadas) {
                        listFiltro2.add(factura);
                    }
                    if (factura.getDescEstado().equals("Pendiente de pago") && checkBoxPagadas2) {
                        listFiltro2.add(factura);
                    }
                    if (factura.getDescEstado().equals("Anulada") && checkBoxPagadas3) {
                        listFiltro2.add(factura);
                    }
                    if (factura.getDescEstado().equals("Cuota Fija") && checkBoxPagadas4) {
                        listFiltro2.add(factura);
                    }
                    if (factura.getDescEstado().equals("Plan de pago") && checkBoxPagadas5) {
                        listFiltro2.add(factura);
                    }
                }

                facturas = listFiltro2;
            }

            if (!getIntent().getStringExtra("fechaDesde").equals("dia/mes/año") && !getIntent().getStringExtra("fechaHasta").equals("dia/mes/año")) {
                ArrayList<FacturasVO> facturasFiltradas = new ArrayList<>();


                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date fechaDesde = null;
                Date fechaHasta = null;

                try {
                    fechaDesde = sdf.parse(getIntent().getStringExtra("fechaDesde"));
                    fechaHasta = sdf.parse(getIntent().getStringExtra("fechaHasta"));


                } catch (ParseException e) {
                    e.printStackTrace();
                }
                for (FacturasVO factura : facturas) {
                    Date fechaFactura = null;
                    try {
                        fechaFactura = sdf.parse(factura.getFecha());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    if (fechaFactura.after(fechaDesde) && fechaFactura.before(fechaHasta)) {
                        facturasFiltradas.add(factura);
                    }
                }

                facturas = facturasFiltradas;
            }

            adapter = new FacturasAdapter(facturas);
            rv1.setAdapter(adapter);

            maxImporte = Double.valueOf(facturas.stream().max(Comparator.comparing(FacturasVO::getImporteOrdenacion)).get().getImporteOrdenacion());

        }
    }

    @Override
    public void onFailure(Call<FacturasResponse> call, Throwable t) {

    }

}