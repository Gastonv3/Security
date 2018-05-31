package com.v3.security.Clases;

public class IngresosAutorizados {
    private Integer idIngresosAutorizados;
    private PersonalAutorizado personalAutorizado;
    private Guardia guardia;
    private String fechaHora;
    private String fechaHoraSalida;

    public String getFechaHoraSalida() {
        return fechaHoraSalida;
    }

    public void setFechaHoraSalida(String fechaHoraSalida) {
        this.fechaHoraSalida = fechaHoraSalida;
    }

    public IngresosAutorizados() {
    }

    public IngresosAutorizados(Integer idIngresosAutorizados, PersonalAutorizado personalAutorizado, Guardia guardia, String fechaHora) {
        this.idIngresosAutorizados = idIngresosAutorizados;
        this.personalAutorizado = personalAutorizado;
        this.guardia = guardia;
        this.fechaHora = fechaHora;
    }

    public Integer getIdIngresosAutorizados() {
        return idIngresosAutorizados;
    }

    public void setIdIngresosAutorizados(Integer idIngresosAutorizados) {
        this.idIngresosAutorizados = idIngresosAutorizados;
    }

    public PersonalAutorizado getPersonalAutorizado() {
        return personalAutorizado;
    }

    public void setPersonalAutorizado(PersonalAutorizado personalAutorizado) {
        this.personalAutorizado = personalAutorizado;
    }

    public Guardia getGuardia() {
        return guardia;
    }

    public void setGuardia(Guardia guardia) {
        this.guardia = guardia;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }
}
