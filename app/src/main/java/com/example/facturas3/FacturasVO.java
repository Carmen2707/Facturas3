package com.example.facturas3;

public class FacturasVO {
    private String descEstado;
    private String importeOrdenacion;
    private String fecha;

    public FacturasVO(String descEstado, String importeOrdenacion, String fecha) {
        this.descEstado = descEstado;
        this.importeOrdenacion = importeOrdenacion;
        this.fecha = fecha;
    }

    public String getDescEstado() {
        return descEstado;
    }

    public String getImporteOrdenacion() {
        return importeOrdenacion;
    }

    public String getFecha() {
        return fecha;
    }
}
