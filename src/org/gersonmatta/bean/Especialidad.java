package org.gersonmatta.bean;

public class Especialidad {
    
    /* FORMA DE MYSQL
    
        codigoEspecialidad int not null auto_increment,
        descripcion varchar(100) not null,
    
    */
    
    private int codigoEspecialidad; //AUTOINCREMENT
    private String descripcion;

    public Especialidad() {
    }

    public Especialidad(int codigoEspecialidad, String descripcion) {
        this.codigoEspecialidad = codigoEspecialidad;
        this.descripcion = descripcion;
    }

    public int getCodigoEspecialidad() {
        return codigoEspecialidad;
    }

    public void setCodigoEspecialidad(int codigoEspecialidad) {
        this.codigoEspecialidad = codigoEspecialidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    } 

    @Override
    public String toString() {
        return getCodigoEspecialidad()+" | "+getDescripcion();
    }
    
    
    
}
