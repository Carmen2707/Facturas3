package com.example.facturas3;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FacturasAdapter extends RecyclerView.Adapter<FacturasAdapter.ViewHolder> {
    //Lista de facturas
    private List<FacturasVO> facturas;

    public FacturasAdapter(List<FacturasVO> facturas) {
        this.facturas = facturas;
    }

    //Llenamos la lista de facturas
    @NonNull
    @Override
    public FacturasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_factura, parent, false));
    }

    //Asignamos los datos
    @Override
    public void onBindViewHolder(@NonNull FacturasAdapter.ViewHolder holder, int position) {
        holder.textEstado.setText(facturas.get(position).getDescEstado());
        holder.textImporte.setText(String.valueOf(facturas.get(position).getImporteOrdenacion()) + "€");
        holder.textFecha.setText(facturas.get(position).getFecha());
        //mostrar pendiente de pago en rojo y pagada en verde
        if (facturas.get(position).getDescEstado().equals("Pendiente de pago")) {
            holder.textEstado.setTextColor(Color.RED);
        } else {
            holder.textEstado.setTextColor(0xFF07B940);
        }
    }

    @Override
    public int getItemCount() {
        return facturas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textEstado;
        private TextView textImporte;
        private TextView textFecha;
        Dialog dialogo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textEstado = itemView.findViewById(R.id.textEstado);
            textImporte = itemView.findViewById(R.id.textImporte);
            textFecha = itemView.findViewById(R.id.textFecha);

            //PopUp
            dialogo = new Dialog(itemView.getContext());

            itemView.setOnClickListener(view -> {
                dialogo.setContentView(R.layout.layout_popup);
                dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView mensajePopup = dialogo.findViewById(R.id.mensaje);
                mensajePopup.setText("Esta funcionalidad aún no está disponible.");
                dialogo.show();

                Button cerrarButton = dialogo.findViewById(R.id.boton);
                cerrarButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogo.dismiss(); // Cierra el popup
                    }
                });

            });

        }

    }


}
