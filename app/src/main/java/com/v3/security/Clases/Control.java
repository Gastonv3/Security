package com.v3.security.Clases;

import java.util.Date;

/**
 * Created by Skylake on 26/3/2018.
 */

public class Control {
    private int idControles;
    private int idGuardia;
    private int idLugares;
    private String latitud;
    private String longitud;

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    private String fechaHora;

    public Control() {
    }

    public Control(int idControles, int idGuardia, int idLugares, String latitud, String longitud, int estado) {
        this.idControles = idControles;
        this.idGuardia = idGuardia;
        this.idLugares = idLugares;
        this.latitud = latitud;
        this.longitud = longitud;

    }

    public int getIdControles() {
        return idControles;
    }

    public void setIdControles(int idControles) {
        this.idControles = idControles;
    }

    public int getIdGuardia() {
        return idGuardia;
    }

    public void setIdGuardia(int idGuardia) {
        this.idGuardia = idGuardia;
    }

    public int getIdLugares() {
        return idLugares;
    }

    public void setIdLugares(int idLugares) {
        this.idLugares = idLugares;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
}
