package com.v3.security.Clases;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.util.Base64;

import java.io.Serializable;

/**
 * Created by Skylake on 25/3/2018.
 */

public class Lugar{
    private Integer idLugares;
    private String nombre_lugares;
    private String ubicacion;
    private String coordenadas;
    private Integer estado;
    private Bitmap imagen;
    private String dato;

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
        try {
            byte[] byteCode = Base64.decode(dato,Base64.DEFAULT);
            this.imagen = BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public Lugar() {

    }

    public Lugar(Integer idLugares, String nombre_lugares, String ubicacion, String coordenadas, Integer estado) {
        this.idLugares = idLugares;
        this.nombre_lugares = nombre_lugares;
        this.ubicacion = ubicacion;
        this.coordenadas = coordenadas;
        this.estado = estado;
    }

    public Integer getIdLugares() {
        return idLugares;
    }

    public void setIdLugares(Integer idLugares) {
        this.idLugares = idLugares;
    }

    public String getNombre_lugares() {
        return nombre_lugares;
    }

    public void setNombre_lugares(String nombre_lugares) {
        this.nombre_lugares = nombre_lugares;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }
}
