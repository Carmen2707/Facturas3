package com.example.facturas3.io.response;

import com.example.facturas3.FacturasVO;

import java.util.List;

public class FacturasResponse {
    //Variables
    private String numFacturas;
    private List<FacturasVO> facturas;

    //Constructor
    public FacturasResponse(String numFacturas, List<FacturasVO> facturas) {
        this.numFacturas = numFacturas;
        this.facturas = facturas;
    }

    //getters y setters
    public String getNumFacturas() {
        return numFacturas;
    }

    public void setNumFacturas(String numFacturas) {
        this.numFacturas = numFacturas;
    }

    public List<FacturasVO> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<FacturasVO> facturas) {
        this.facturas = facturas;
    }
}
