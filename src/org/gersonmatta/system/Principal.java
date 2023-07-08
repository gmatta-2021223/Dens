/*
    Nombre: Gerson Aaròn Matta Aguilar
    Codigo Tecnico: IN5AV
    Carné: 2021223
    Fecha de Creación: 05/04/2022
    Fechas de Modificaciones:   05/04/2022, 11/04/2022, 12/04/2022, 18/04/2022, 
                                19/04/2022, 20/04/2022, 25/04/2022, 26/04/2022,
                                27/04/2022, 30/04/2022, 09/05/2022, 10/05/2022,
                                23/05/2022, 24/05/2022, 29/05/2022, 30/05/2022,
                                31/05/2022, 01/06/2022, 06/07/2022, 07/07/2022,
                                08/07/2022
*/

package org.gersonmatta.system;

import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.gersonmatta.controller.CitaController;
import org.gersonmatta.controller.DetalleRecetaController;
import org.gersonmatta.controller.DoctorController;
import org.gersonmatta.controller.EspecialidadController;
import org.gersonmatta.controller.LoginController;
import org.gersonmatta.controller.MedicamentoController;
import org.gersonmatta.controller.MenuPrincipalController;
import org.gersonmatta.controller.PacienteController;
import org.gersonmatta.controller.ProgramadorController;
import org.gersonmatta.controller.RecetaController;
import org.gersonmatta.controller.UsuarioController;

/**
 *
 * @author Aarón Matta
 * 
 */

public class Principal extends Application {
    
    private Stage escenarioPrincipal;
    private Scene escena;
    private final String PAQUETE_VISTA="/org/gersonmatta/view/";
    
    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Dens");
        escenarioPrincipal.getIcons().add( new Image("/org/gersonmatta/image/IconoVentana.png"));
        
        /*Parent root = FXMLLoader.load( getClass().getResource("/org/gersonmatta/view/MenuPrincipalView.fxml") );
        Scene escena = new Scene(root);
        escenarioPrincipal.setScene(escena);*/

        ventanaLogin();
        escenarioPrincipal.show();
        
    }

    public void menuPrincipal(){
    
        try{
        
            MenuPrincipalController ventanaMenu = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml",1200,600);
            ventanaMenu.setEscenarioPrincipal(this);
            
        }catch(Exception e){
        
            e.printStackTrace();
        
        }
        
    }
    
    public void ventanaProgramador(){
    
        try{
        
            ProgramadorController vistaProgramador = (ProgramadorController)cambiarEscena("ProgramadorView.fxml",1200,600);
            vistaProgramador.setEscenarioPrincipal(this);
        
        }catch(Exception e){
        
            e.printStackTrace();
        
        }
    
    }
    
    public void ventanaPacientes(){
    
        try{
        
            PacienteController vistaPaciente = (PacienteController)cambiarEscena("PacienteView.fxml",1200,600);
            vistaPaciente.setEscenarioPrincipal(this);
            
        }catch(Exception e){
        
            e.printStackTrace();
        
        }
        
    }
    
    public void ventanaEspecialidades(){
    
        try{
        
            EspecialidadController vistaEspecialidad = (EspecialidadController) cambiarEscena("EspecialidadView.fxml",1200,600);
            vistaEspecialidad.setEscenarioPrincipal(this);
            
        }catch(Exception e){
        
            e.printStackTrace();
        
        }
        
    }
    
    public void ventanaMedicamentos(){
    
        try{
        
            MedicamentoController vistaMedicamento = (MedicamentoController)cambiarEscena("MedicamentoView.fxml",1200,600);
            vistaMedicamento.setEscenarioPrincipal(this);
        
        }catch(Exception e){
        
            e.printStackTrace();
        
        }
    
    }
    
    public void ventanaDoctores(){
    
        try{
        
            DoctorController vistaDoctor = (DoctorController) cambiarEscena("DoctorView.fxml",1200,600);
            vistaDoctor.setEscenarioPrincipal(this);
        
        }catch(Exception e){
        
            e.printStackTrace();
        
        }
    
    }
    
    public void ventanaRecetas(){
    
        try{
        
            RecetaController vistaReceta = (RecetaController) cambiarEscena("RecetaView.fxml",1200,600);
            vistaReceta.setEscenarioPrincipal(this);
        
        }catch(Exception e){
            
            e.printStackTrace();
            
        }
    
    }
    
    public void ventanaDetallesRecetas(){
    
        try{
        
            DetalleRecetaController vistaDetalleReceta = (DetalleRecetaController)cambiarEscena("DetalleRecetaView.fxml",1200,600);
            vistaDetalleReceta.setEscenarioPrincipal(this);
            
        }catch(Exception e){
            
            e.printStackTrace();
            
        }
    
    
    }
    
    public void ventanaLogin(){
    
        try{
            
            LoginController login = (LoginController)cambiarEscena("LoginView.fxml",1200,600);
            login.setEscenarioPrincipal(this);
        
        }catch(Exception e){
        
            e.printStackTrace();
        
        }
    
    }
    
    public void ventanaUsuario(){
    
        try{
            
            UsuarioController usuario = (UsuarioController)cambiarEscena("UsuarioView.fxml",1200,600);
            usuario.setEscenarioPrincipal(this);
        
        }catch(Exception e){
        
            e.printStackTrace();
        
        }
    
    }
    
    public void ventanaCitas(){
    
        try{
        
            CitaController vistaCita = (CitaController)cambiarEscena("CitaView.fxml",1200,600);
            vistaCita.setEscenarioPrincipal(this);
        
        }catch(Exception e){
        
            e.printStackTrace();
        
        }
    
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception{
        
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory( new JavaFXBuilderFactory() );
        cargadorFXML.setLocation( Principal.class.getResource(PAQUETE_VISTA+fxml) );
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo),ancho,alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
        
        return resultado;
    
    }
    
}
