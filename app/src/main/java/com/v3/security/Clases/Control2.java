package com.v3.security.Clases;


import java.io.Serializable;

public class Control2 implements Serializable {
    private int idControles;
    private Guardia2 guardia;
    private Lugar lugar;
    private String latitud;
    private String longitud;
    private String fechaHora;

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Control2() {
    }

    public Guardia2 getGuardia() {
        return guardia;
    }

    public void setGuardia(Guardia2 guardia) {
        this.guardia = guardia;
    }

    public int getIdControles() {
        return idControles;
    }

    public void setIdControles(int idControles) {
        this.idControles = idControles;
    }

    public Lugar getLugar() {
        return lugar;
    }

    public void setLugar(Lugar lugar) {
        this.lugar = lugar;
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
