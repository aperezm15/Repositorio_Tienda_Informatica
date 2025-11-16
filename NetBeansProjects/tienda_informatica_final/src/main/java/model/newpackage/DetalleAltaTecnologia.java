package model.newpackage;

import java.time.LocalDate;
import java.util.Date;

public class DetalleAltaTecnologia {
    private int idDetalleAltaTecnologia; //PK
    private String paisOrigen;
    private LocalDate fechaFabricacion;
    private int empresaFabricanteId; //FK

    private EmpresaFabricante empresaFabricante;
    
    public DetalleAltaTecnologia() {
    }

    public DetalleAltaTecnologia(int idDetalleAltaTecnologia, String paisOrigen, LocalDate fechaFabricacion, int empresaFabricanteId) {
        this.idDetalleAltaTecnologia = idDetalleAltaTecnologia;
        this.paisOrigen = paisOrigen;
        this.fechaFabricacion = fechaFabricacion;
        this.empresaFabricanteId = empresaFabricanteId;
    }

    //Getters y Setters
    public int getIdDetalleAltaTecnologia() {
        return idDetalleAltaTecnologia;
    }

    public String getPaisOrigen() {
        return paisOrigen;
    }

    public LocalDate getFechaFabricacion() {
        return fechaFabricacion;
    }

    public int getEmpresaFabricanteId() {
        return empresaFabricanteId;
    }

    public void setIdDetalleAltaTecnologia(int idDetalleAltaTecnologia) {
        this.idDetalleAltaTecnologia = idDetalleAltaTecnologia;
    }

    public void setPaisOrigen(String paisOrigen) {
        this.paisOrigen = paisOrigen;
    }

    public void setFechaFabricacion(LocalDate fechaFabricacion) {
        this.fechaFabricacion = fechaFabricacion;
    }

    public void setEmpresaFabricanteId(int empresaFabricanteId) {
        this.empresaFabricanteId = empresaFabricanteId;
    }

}