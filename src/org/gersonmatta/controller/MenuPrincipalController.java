package org.gersonmatta.controller;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.gersonmatta.system.Principal;

public class MenuPrincipalController implements Initializable{
    
    private Principal escenarioPrincipal;

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaProgramador(){ 
        
        escenarioPrincipal.ventanaProgramador();
        
    }
    
    public void ventanaPaciente(){
        
        escenarioPrincipal.ventanaPacientes();
        
    }
    
    public void ventanaEspecialidad(){
        
        escenarioPrincipal.ventanaEspecialidades();
        
    }
    
    public void ventanaMedicamentos(){
        
        escenarioPrincipal.ventanaMedicamentos();
    
    }
    
    public void ventanaDoctores(){
        
        escenarioPrincipal.ventanaDoctores();
    
    }
    
    public void ventanaRecetas(){
    
        escenarioPrincipal.ventanaRecetas();
    
    }
    
    public void ventanaDetallesRecetas(){
    
        escenarioPrincipal.ventanaDetallesRecetas();
    
    }
    
    public void ventanaLogin(){
    
        escenarioPrincipal.ventanaLogin();
    
    }
    
    public void ventanaUsuario(){
    
        escenarioPrincipal.ventanaUsuario();
    
    }
    
    public void ventanaCitas(){
    
        escenarioPrincipal.ventanaCitas();
    
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        

    }
    
}
