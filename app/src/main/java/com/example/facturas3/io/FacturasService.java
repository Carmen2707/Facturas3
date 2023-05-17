package com.example.facturas3.io;

import com.example.facturas3.io.response.FacturasResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FacturasService {
    @GET("facturas")
    Call<FacturasResponse> getFacturas();

}
