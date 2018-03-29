package com.v3.security.Clases;

/**
 * Created by Skylake on 25/3/2018.
 */

public class Guardia {
    private int idpersona;
    private String codigo_guardia;
    private String login;
    private String password;
    private String estado;

    public Guardia() {
    }

    public Guardia(int idpersona, String codigo_guardia, String login, String password, String estado) {
        this.idpersona = idpersona;
        this.codigo_guardia = codigo_guardia;
        this.login = login;
        this.password = password;
        this.estado = estado;
    }

    public int getIdpersona() {
        return idpersona;
    }

    public void setIdpersona(int idpersona) {
        this.idpersona = idpersona;
    }

    public String getCodigo_guardia() {
        return codigo_guardia;
    }

    public void setCodigo_guardia(String codigo_guardia) {
        this.codigo_guardia = codigo_guardia;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}


