package com.example.facturas3;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class FacturasVO {
    //Variables
    private String descEstado;
    private Double importeOrdenacion;
    private String fecha;

    //Constructor
    public FacturasVO(String descEstado, Double importeOrdenacion, String fecha) {
        this.descEstado = descEstado;
        this.importeOrdenacion = importeOrdenacion;
        this.fecha = fecha;
    }

    //Getters
    public String getDescEstado() {
        return descEstado;
    }

    public Double getImporteOrdenacion() {
        return importeOrdenacion;
    }

    public String getFecha() {
        return fecha;
    }

}
