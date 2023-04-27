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
import java.util.ArrayList;
import java.util.Comparator;
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
            try {
                String valor = facturas.toString();
                JSONArray arreglo = new JSONArray(valor);
                for (int f = 0; f < arreglo.length(); f++) {
                    JSONObject objeto = new JSONObject(arreglo.get(f).toString());
                    String descEstado = objeto.getString("descEstado");
                    String importeOrdenacion = (objeto.getString("importeOrdenacion"));
                    String fecha = objeto.getString("fecha");
                    FacturasVO factura = new FacturasVO(descEstado, importeOrdenacion, fecha);
                    facturas.add(factura);

                }
                maxImporte = Double.valueOf(facturas.stream().max(Comparator.comparing(FacturasVO::getImporteOrdenacion)).get().getImporteOrdenacion());

                Bundle extras = getIntent().getExtras();

                if (extras != null) {
                    ArrayList<FacturasVO> listFiltro = new ArrayList<>();

                    double importeFiltro = getIntent().getDoubleExtra("importe", maxImporte);

                    for (FacturasVO factura : facturas) {
                        if (Double.parseDouble(factura.getImporteOrdenacion()) < importeFiltro) {
                            listFiltro.add(factura);
                        }
                    }

                    adapter = new FacturasAdapter(facturas);
                    rv1.setAdapter(adapter);


                }
            } catch (JSONException e) {
                e.printStackTrace();


            }
        }
    }

    @Override
    public void onFailure(Call<FacturasResponse> call, Throwable t) {

    }

}