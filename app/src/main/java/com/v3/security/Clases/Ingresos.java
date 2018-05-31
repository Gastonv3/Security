package com.v3.security.Clases;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Ingresos {
    private Integer idIngresos;
    private Guardia2 guardia;
    private String nombreIngreso;
    private String apellidoIngreso;
    private String dni;
    private String motivo;
    private Bitmap imagenIngresos;
    private String fechaHoraIngreso;
    private String fechaHoraSalida;
    private String dato;

    public Ingresos() {
    }

    public Ingresos(Integer idIngresos, Guardia2 guardia, String nombreIngreso, String apellidoIngreso, String dni, String motivo, String fechaHoraIngreso) {
        this.idIngresos = idIngresos;
        this.guardia = guardia;
        this.nombreIngreso = nombreIngreso;
        this.apellidoIngreso = apellidoIngreso;
        this.dni = dni;
        this.motivo = motivo;
        this.fechaHoraIngreso = fechaHoraIngreso;
    }

    public Integer getIdIngresos() {
        return idIngresos;
    }

    public void setIdIngresos(Integer idIngresos) {
        this.idIngresos = idIngresos;
    }

    public Guardia2 getGuardia() {
        return guardia;
    }

    public void setGuardia(Guardia2 guardia) {
        this.guardia = guardia;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Bitmap getImagenIngresos() {
        return imagenIngresos;
    }

    public void setImagenIngresos(Bitmap imagenIngresos) {
        this.imagenIngresos = imagenIngresos;
    }

    public String getNombreIngreso() {
        return nombreIngreso;
    }

    public void setNombreIngreso(String nombreIngreso) {
        this.nombreIngreso = nombreIngreso;
    }

    public String getApellidoIngreso() {
        return apellidoIngreso;
    }

    public void setApellidoIngreso(String apellidoIngreso) {
        this.apellidoIngreso = apellidoIngreso;
    }

    public String getFechaHoraIngreso() {
        return fechaHoraIngreso;
    }

    public void setFechaHoraIngreso(String fechaHoraIngreso) {
        this.fechaHoraIngreso = fechaHoraIngreso;
    }

    public String getFechaHoraSalida() {
        return fechaHoraSalida;
    }

    public void setFechaHoraSalida(String fechaHoraSalida) {
        this.fechaHoraSalida = fechaHoraSalida;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
        try {
            byte[] byteCode = Base64.decode(dato, Base64.DEFAULT);
            this.imagenIngresos = BitmapFactory.decodeByteArray(byteCode, 0, byteCode.length);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
