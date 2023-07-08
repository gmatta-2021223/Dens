package org.gersonmatta.bean;

public class Medicamento {
    
    /* FORMA DE MYSQL
    
        codigoMedicamento int not null auto_increment,
        nombreMedicamento varchar(100) not null,
    
    */
    
    private int codigoMedicamento;
    private String nombreMedicamento;

    public Medicamento() {
    }

    public Medicamento(int codigoMedicamento, String nombreMedicamento) {
        this.codigoMedicamento = codigoMedicamento;
        this.nombreMedicamento = nombreMedicamento;
    }

    public int getCodigoMedicamento() {
        return codigoMedicamento;
    }

    public void setCodigoMedicamento(int codigoMedicamento) {
        this.codigoMedicamento = codigoMedicamento;
    }

    public String getNombreMedicamento() {
        return nombreMedicamento;
    }

    public void setNombreMedicamento(String nombreMedicamento) {
        this.nombreMedicamento = nombreMedicamento;
    }

    @Override
    public String toString() {
        return getCodigoMedicamento()+" | "+getNombreMedicamento();
    }
    
    
    
}
