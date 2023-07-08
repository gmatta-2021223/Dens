package org.gersonmatta.bean;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Date;

public class Cita {
    
    /* FORMA DE MYSQL
    
    codigoCita 		int not null auto_increment,
    fechaCita 		date not null,
    horaCita            time not null,
    tratamiento         varchar(150),
    descripCondActual   varchar(255) not null,
    codigoPaciente 	int not null,
    numeroColegiado     int not null,
    
    */
    
    private int codigoCita;
    private Date fechaCita;
    private Time horaCita;
    private String tratamiento;
    private String descripCondActual;
    private int codigoPaciente;
    private int numeroColegiado;

    public Cita() {
    }

    public Cita(int codigoCita, Date fechaCita, Time horaCita, String tratamiento, String descripCondActual, int codigoPaciente, int numeroColegiado) {
        this.codigoCita = codigoCita;
        this.fechaCita = fechaCita;
        this.horaCita = horaCita;
        this.tratamiento = tratamiento;
        this.descripCondActual = descripCondActual;
        this.codigoPaciente = codigoPaciente;
        this.numeroColegiado = numeroColegiado;
    }

    public int getCodigoCita() {
        return codigoCita;
    }

    public void setCodigoCita(int codigoCita) {
        this.codigoCita = codigoCita;
    }

    public Date getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(Date fechaCita) {
        this.fechaCita = fechaCita;
    }

    public Time getHoraCita() {
        return horaCita;
    }

    public void setHoraCita(Time horaCita) {
        this.horaCita = horaCita;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getDescripCondActual() {
        return descripCondActual;
    }

    public void setDescripCondActual(String descripCondActual) {
        this.descripCondActual = descripCondActual;
    }

    public int getCodigoPaciente() {
        return codigoPaciente;
    }

    public void setCodigoPaciente(int codigoPaciente) {
        this.codigoPaciente = codigoPaciente;
    }

    public int getNumeroColegiado() {
        return numeroColegiado;
    }

    public void setNumeroColegiado(int numeroColegiado) {
        this.numeroColegiado = numeroColegiado;
    }

    @Override
    public String toString() {
        return "Cita{" + "codigoCita=" + codigoCita + ", fechaCita=" + fechaCita + ", horaCita=" + horaCita + ", tratamiento=" + tratamiento + ", descripCondActual=" + descripCondActual + ", codigoPaciente=" + codigoPaciente + ", numeroColegiado=" + numeroColegiado + '}';
    }

    
    
}
