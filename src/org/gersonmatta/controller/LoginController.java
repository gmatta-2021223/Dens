package org.gersonmatta.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import org.gersonmatta.bean.Login;
import org.gersonmatta.bean.Usuario;
import org.gersonmatta.db.Conexion;
import org.gersonmatta.system.Principal;

public class LoginController implements Initializable{


    private Principal escenarioPrincipal;
    private boolean respuesta;
    
    private ObservableList<Usuario> listaUsuario;
    
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    public void buscarUsuario(){
    
        ArrayList<Usuario> lista = new ArrayList<Usuario>();
        
        try{
        
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarUsuarios}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while( resultado.next() ){
            
                lista.add( new Usuario(
                
                        resultado.getInt("codigoUsuario"),
                        resultado.getString("nombreUsuario"),
                        resultado.getString("apellidoUsuario"),
                        resultado.getString("usuarioLogin"),
                        resultado.getString("contrasena")
                
                ) );
            
            }
        
        }catch(Exception e){
        
            e.printStackTrace();
        
        }
        
        for( Usuario usuario: lista ){
            
            if ( usuario.getUsuarioLogin().equals( txtUsuario.getText()) && usuario.getContrasena().equals( txtPassword.getText() ) ) {
                
                limpiarControles();
                menuPrincipal();
                respuesta = true;
                break;
                
            }else{
            
                respuesta = false;
            
            }
        }
        
        if(respuesta == false){
            
            JOptionPane.showMessageDialog(null, "Usuario o contraseña estan incorrectos", "Datos inexistentes", JOptionPane.WARNING_MESSAGE);
            limpiarControles();
            
        }
        
    }
    
    public ObservableList<Usuario> getUsuario(){
    
        ArrayList<Usuario> lista = new ArrayList<Usuario>();
        
        try{
        
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarUsuarios}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
            
                lista.add( new Usuario(
                
                        resultado.getInt("codigoUsuario"),
                        resultado.getString("nombreUsuario"),
                        resultado.getString("apellidoUsuario"),
                        resultado.getString("usuarioLogin"),
                        resultado.getString("contrasena")
                
                ) );
            
            }
        
        }catch(Exception e){
            
            e.printStackTrace();
        
        }
        
        return FXCollections.observableArrayList(lista);
    
    }
    
    @FXML
    private void Login(){
    
        Login lo = new Login();
        int x = 0;
        boolean bandera = false;
        lo.setUsuarioMaster( txtUsuario.getText() );
        lo.setPasswordLogin( txtPassword.getText() );
        
        while( x<getUsuario().size() ){
        
            String user = getUsuario().get(x).getUsuarioLogin();
            String password = getUsuario().get(x).getContrasena();
            
            if ( user.equals( lo.getUsuarioMaster() ) && password.equals( lo.getPasswordLogin() ) ) {
                
                JOptionPane.showMessageDialog(null, "Sesión iniciada \n"+getUsuario().get(x).getNombreUsuario()+" "+getUsuario().get(x).getApellidoUsuario(), "Iniciando sesión", JOptionPane.INFORMATION_MESSAGE);
                x = getUsuario().size();
                bandera = true;
                
            }
            
            x++;
        
        }
        
        if (bandera == false) {
            
            JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrecta", "Datos incorrectos", JOptionPane.INFORMATION_MESSAGE);
            
        }
    
    }
    
    public void menuPrincipal(){
    
        escenarioPrincipal.menuPrincipal();
    
    }
    
    
    public void ventanaUsuario(){
    
        escenarioPrincipal.ventanaUsuario();

    }
    
    public void limpiarControles(){
    
        txtUsuario.clear();
        txtPassword.clear();
    
    }
    
    
    
}
