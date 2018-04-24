package com.v3.security.Clases;

public class PersonalAutorizado {
    private Integer idPersonalAutorizado;
    private String nombre;
    private String apellido;
    private String codigo;

    public PersonalAutorizado() {
    }

    public Integer getIdPersonalAutorizado() {
        return idPersonalAutorizado;
    }

    public void setIdPersonalAutorizado(Integer idPersonalAutorizado) {
        this.idPersonalAutorizado = idPersonalAutorizado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
