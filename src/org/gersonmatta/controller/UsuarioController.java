package org.gersonmatta.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.gersonmatta.bean.Usuario;
import org.gersonmatta.db.Conexion;
import org.gersonmatta.system.Principal;

public class UsuarioController implements Initializable{
    
    private Principal escenarioPrincipal;
    private enum operaciones{NINGUNO,GUARDAR}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private boolean respuesta;
    
    //@FXML private TextField txtCodigoUsuario;
    @FXML private TextField txtNombreUsuario;
    @FXML private TextField txtApellidoUsuario;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtPasswordConfirmar;
    
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    
    //@FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        activarControles();
    }
    
    public void nuevo(){
    
        switch(tipoDeOperacion){
        
            case NINGUNO:
                
                if ( !(txtUsuario.getText().isEmpty() || txtPassword.getText().isEmpty() || txtNombreUsuario.getText().isEmpty() || txtApellidoUsuario.getText().isEmpty() ) ) {
                    
                    if (txtPassword.getText().equals(txtPasswordConfirmar.getText())  ) {
                        
                        usuarioRepetido();
                        
                        if (respuesta == true) {
                            
                            JOptionPane.showMessageDialog(null, "Este usuario ya se encuentra en uso","Usuario usado", JOptionPane.INFORMATION_MESSAGE);
                            
                        }else{
                            
                            guardar();
                            //imgNuevo.setImage( new Image("/org/gersonmatta/image/Agregar.png"));
                        
                        }
                         
                        
                    }else{
                    
                        JOptionPane.showMessageDialog(null, "Las contraseñas no son iguales", "Contraseñas incorrectas", JOptionPane.ERROR_MESSAGE);
                        txtPassword.clear();
                        txtPasswordConfirmar.clear();
                    
                    }
                    
                }else{
                
                    JOptionPane.showMessageDialog(null, "Ha dejado datos sin llenar", "Datos sin rellenar", JOptionPane.ERROR_MESSAGE);
                
                }
                
                /*limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                imgNuevo.setImage( new Image("/org/gersonmatta/image/IconoGuardar.png"));
                tipoDeOperacion = operaciones.GUARDAR;*/
                
                break;
                
            case GUARDAR:
               
                break;
                
        }
    
    }
    
    public boolean usuarioRepetido(){
    
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
            
            if ( usuario.getUsuarioLogin().equals( txtUsuario.getText() ) ) {
                
                respuesta = true;
                break;
                
            }else{
            
                respuesta = false;
            
            }
        
        }
        
        return respuesta;
    
    }
 
    public void guardar(){
    
        Usuario registro = new Usuario();
        
        registro.setNombreUsuario( txtNombreUsuario.getText() );
        registro.setApellidoUsuario( txtApellidoUsuario.getText() );
        registro.setUsuarioLogin( txtUsuario.getText() );
        registro.setContrasena( txtPassword.getText() );
        
        try{
        
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarUsuario(?,?,?,?)}");
            procedimiento.setString(1, registro.getNombreUsuario());
            procedimiento.setString(2, registro.getApellidoUsuario());
            procedimiento.setString(3, registro.getUsuarioLogin());
            procedimiento.setString(4, registro.getContrasena());
            procedimiento.execute();
            
            JOptionPane.showMessageDialog(null, "Datos ingresados correctamente", "Usuario agregado", JOptionPane.INFORMATION_MESSAGE);
            limpiarControles();
            ventanaLogin();
            tipoDeOperacion = operaciones.NINGUNO;
            
        }catch(Exception e){
        
            e.printStackTrace();
        
        }
        
    }
    
    public void eliminar(){
        
        if ( (txtUsuario.getText().isEmpty() && txtPassword.getText().isEmpty() && txtPasswordConfirmar.getText().isEmpty() && txtNombreUsuario.getText().isEmpty() && txtApellidoUsuario.getText().isEmpty() ) ) {
        
            JOptionPane.showMessageDialog(null, "No hay datos agregados para limpiar", "Sin datos colocados", JOptionPane.ERROR_MESSAGE);
        
        }else{
            
            limpiarControles();
            
        }
    
        
    
    }
    
    public void ventanaLogin(){
    
        escenarioPrincipal.ventanaLogin();
        
    }
    
    public void activarControles(){
    
        txtNombreUsuario.setEditable(true);
        txtApellidoUsuario.setEditable(true);
        txtUsuario.setEditable(true);
        txtPassword.setEditable(true);
        txtPasswordConfirmar.setEditable(true);
        btnEliminar.setDisable(false);
    
    }
    
    public void desactivarControles(){
    
        //txtCodigoUsuario.setEditable(false);
        txtNombreUsuario.setEditable(false);
        txtApellidoUsuario.setEditable(false);
        txtUsuario.setEditable(false);
        txtPassword.setEditable(false);
        txtPasswordConfirmar.setEditable(false);
        btnEliminar.setDisable(true);
    
    }
    
    public void limpiarControles(){
    
        //txtCodigoUsuario.clear();
        txtNombreUsuario.clear();  
        txtApellidoUsuario.clear();
        txtUsuario.clear();
        txtPassword.clear();
        txtPasswordConfirmar.clear();
    
    }
    
}
