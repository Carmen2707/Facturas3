package com.example.facturas3;

import android.widget.DatePicker;

import java.util.HashMap;

public class FiltroVO {
    //variables
    private String fechaInicio;
    private String fechaFin;
    private int importeSeleccionado;
    private int maxImporte;
    private HashMap<String, Boolean> mapaCheckBox;

    //constructor
    public FiltroVO(String fechaInicio, String fechaFin, int maxImporte, int importeSeleccionado, HashMap<String, Boolean> mapaCheckBox) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.maxImporte = maxImporte;
        this.importeSeleccionado = importeSeleccionado;
        this.mapaCheckBox = mapaCheckBox;
    }

    //getters
    public String getFechaInicio() {
        return fechaInicio;
    }


    public String getFechaFin() {
        return fechaFin;
    }


    public int getMaxImporte() {
        return maxImporte;
    }


    public int getImporteSeleccionado() {
        return importeSeleccionado;
    }

    public HashMap<String, Boolean> getMapaCheckBox() {
        return mapaCheckBox;
    }

}
