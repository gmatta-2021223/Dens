package org.gersonmatta.bean;

import java.util.Date;

public class Receta {
    
    /* FORMA MYSQL
    
    codigoReceta 	int not null auto_increment,
    fechaReceta 	date not null,
    numeroColegiado 	int not null,
    
    */
    
    private int codigoReceta;
    private Date fechaReceta;
    private int numeroColegiado;

    public Receta() {
    }

    public Receta(int codigoReceta, Date fechaReceta, int numeroColegiado) {
        this.codigoReceta = codigoReceta;
        this.fechaReceta = fechaReceta;
        this.numeroColegiado = numeroColegiado;
    }

    public int getCodigoReceta() {
        return codigoReceta;
    }

    public void setCodigoReceta(int codigoReceta) {
        this.codigoReceta = codigoReceta;
    }

    public Date getFechaReceta() {
        return fechaReceta;
    }

    public void setFechaReceta(Date fechaReceta) {
        this.fechaReceta = fechaReceta;
    }

    public int getNumeroColegiado() {
        return numeroColegiado;
    }

    public void setNumeroColegiado(int numeroColegiado) {
        this.numeroColegiado = numeroColegiado;
    }

    @Override
    public String toString() {
        return getCodigoReceta()+" | Fecha: "+getFechaReceta()+" | Num. Colegiado: "+getNumeroColegiado();
    }
    
    
    
}
