package com.example.facturas3;

import android.widget.DatePicker;

import java.util.HashMap;

public class FiltroVO {
    //Creamos estas variables para los botones de las fechas
    private String fechaInicio;
    private String fechaFin;
    //Esta variable para la seekbar
    private int maxImporte;
    //Este mapa para los valores de las checkbox
    private HashMap<String, Boolean> mapaCheckBox = new HashMap<>();

    public FiltroVO(String fechaInicio, String fechaFin, int maxImporte, HashMap<String, Boolean> mapaCheckBox) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.maxImporte = maxImporte;
        this.mapaCheckBox = mapaCheckBox;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getMaxImporte() {
        return maxImporte;
    }

    public void setMaxImporte(int maxImporte) {
        this.maxImporte = maxImporte;
    }

    public HashMap<String, Boolean> getMapaCheckBox() {
        return mapaCheckBox;
    }

    public void setMapaCheckBox(HashMap<String, Boolean> mapaCheckBox) {
        this.mapaCheckBox = mapaCheckBox;
    }
}
