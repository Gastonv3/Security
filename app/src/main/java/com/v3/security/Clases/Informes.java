package com.v3.security.Clases;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.Serializable;

public class Informes implements Serializable  {
    private  Integer idInformes;
    private Control control;
    private  String tituloInforme;
    private  String informe;
    private Bitmap imagenInforme;
    private String dato;

    public Informes() {
    }

    public Informes(Integer idInformes, Control control, String tituloInforme, String informe) {
        this.idInformes = idInformes;
        this.control = control;
        this.tituloInforme = tituloInforme;
        this.informe = informe;
    }

    public Integer getIdInformes() {
        return idInformes;
    }

    public void setIdInformes(Integer idInformes) {
        this.idInformes = idInformes;
    }

    public Control getControl() {
        return control;
    }

    public void setControl(Control control) {
        this.control = control;
    }

    public String getTituloInforme() {
        return tituloInforme;
    }

    public void setTituloInforme(String tituloInforme) {
        this.tituloInforme = tituloInforme;
    }

    public String getInforme() {
        return informe;
    }

    public void setInforme(String informe) {
        this.informe = informe;
    }

    public Bitmap getImagenInforme() {
        return imagenInforme;
    }

    public void setImagenInforme(Bitmap imagenInforme) {
        this.imagenInforme = imagenInforme;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
        this.dato = dato;
        try {
            byte[] byteCode = Base64.decode(dato, Base64.DEFAULT);
            this.imagenInforme = BitmapFactory.decodeByteArray(byteCode, 0, byteCode.length);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
