package com.v3.security.Clases;

public class PersonalAutorizado {
    private Integer idPersonalAutorizado;
    private String nombrePersonalAutorizado;
    private String apellidoPersonalAutorizado;
    private String codigo;
    private String dni;
    private String cargo;
    public PersonalAutorizado() {
    }

    public PersonalAutorizado(Integer idPersonalAutorizado, String nombrePersonalAutorizado, String apellidoPersonalAutorizado, String codigo, String dni, String cargo) {
        this.idPersonalAutorizado = idPersonalAutorizado;
        this.nombrePersonalAutorizado = nombrePersonalAutorizado;
        this.apellidoPersonalAutorizado = apellidoPersonalAutorizado;
        this.codigo = codigo;
        this.dni = dni;
        this.cargo = cargo;
    }

    public String getNombrePersonalAutorizado() {
        return nombrePersonalAutorizado;
    }

    public void setNombrePersonalAutorizado(String nombrePersonalAutorizado) {
        this.nombrePersonalAutorizado = nombrePersonalAutorizado;
    }

    public String getApellidoPersonalAutorizado() {
        return apellidoPersonalAutorizado;
    }

    public void setApellidoPersonalAutorizado(String apellidoPersonalAutorizado) {
        this.apellidoPersonalAutorizado = apellidoPersonalAutorizado;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Integer getIdPersonalAutorizado() {
        return idPersonalAutorizado;
    }

    public void setIdPersonalAutorizado(Integer idPersonalAutorizado) {
        this.idPersonalAutorizado = idPersonalAutorizado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
