package com.v3.security.Clases;

/**
 * Created by Skylake on 26/3/2018.
 */

public class Control {
    private  int idControles;
    private  int idGuardia;
    private  int idLugares;
    private String coordenadas;
    private int Estado;

    public Control() {
    }

    public Control(int idControles, int idGuardia, int idLugares, String coordenadas, int estado) {
        this.idControles = idControles;
        this.idGuardia = idGuardia;
        this.idLugares = idLugares;
        this.coordenadas = coordenadas;
        Estado = estado;
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

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

    public int getEstado() {
        return Estado;
    }

    public void setEstado(int estado) {
        Estado = estado;
    }
}
