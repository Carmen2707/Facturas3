package com.example.facturas3;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class FacturasVO implements Parcelable {
    private String descEstado;
    private Double importeOrdenacion;
    private String fecha;

    public FacturasVO(String descEstado, Double importeOrdenacion, String fecha) {
        this.descEstado = descEstado;
        this.importeOrdenacion = importeOrdenacion;
        this.fecha = fecha;
    }
    public String getDescEstado() {
        return descEstado;
    }

    public Double getImporteOrdenacion() {
        return importeOrdenacion;
    }

    public String getFecha() {
        return fecha;
    }

    protected FacturasVO(Parcel in) {
        this.descEstado = in.readString();
        this.importeOrdenacion = in.readDouble();
        this.fecha = in.readString();
    }

    public static final Creator<FacturasVO> CREATOR = new Creator<FacturasVO>() {
        @Override
        public FacturasVO createFromParcel(Parcel in) {
            return new FacturasVO(in);
        }

        @Override
        public FacturasVO[] newArray(int size) {
            return new FacturasVO[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(this.descEstado);
        parcel.writeDouble(this.importeOrdenacion);
        parcel.writeString(this.fecha);
    }
}
