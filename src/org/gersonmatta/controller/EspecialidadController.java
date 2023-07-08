package org.gersonmatta.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Line;
import javax.swing.JOptionPane;
import org.gersonmatta.bean.Especialidad;
import org.gersonmatta.db.Conexion;
import org.gersonmatta.report.GenerarReporte;
import org.gersonmatta.system.Principal;

public class EspecialidadController implements Initializable{
    
    private Principal escenarioPrincipal;
    
    private enum operaciones{NUEVO,ELIMINAR,EDITAR,GUARDAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Especialidad> listaEspecialidad;
    
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtCodigoEspecialidad;
    
    @FXML private GridPane grpFechas;
    
    @FXML private TableView tblEspecialidades;
    
    @FXML private TableColumn colCodigoEspecialidad;
    @FXML private TableColumn colDescripcion;
    
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
    
        tblEspecialidades.setItems( getEspecialidad() );
        colCodigoEspecialidad.setCellValueFactory( new PropertyValueFactory<Especialidad, Integer>("codigoEspecialidad")  );
        colDescripcion.setCellValueFactory( new PropertyValueFactory<Especialidad, String>("descripcion") );
        
    }
    
    public void seleccionarElemento(){
        
        if ( tblEspecialidades.getSelectionModel().getSelectedItem() == null ) {
            
            JOptionPane.showMessageDialog(null, "El registro que ha seleccionado está vacío", "Registro sin datos", JOptionPane.WARNING_MESSAGE);
            
        }else{
        
            txtCodigoEspecialidad.setText( String.valueOf( ( (Especialidad) tblEspecialidades.getSelectionModel().getSelectedItem() ).getCodigoEspecialidad() ) );
            txtDescripcion.setText(((Especialidad)tblEspecialidades.getSelectionModel().getSelectedItem()).getDescripcion());
            
        }
    
        
    
    }
    
    public ObservableList<Especialidad> getEspecialidad(){
    
        ArrayList<Especialidad> lista = new ArrayList<Especialidad>();
        
        try{
        
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEspecialidades}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while( resultado.next() ){
            
                lista.add( new Especialidad(    resultado.getInt("codigoEspecialidad"),
                                                resultado.getString("descripcion")
                
                
                ) );
            
            }
        
        }catch(Exception e){
        
            e.printStackTrace();
        
        }
        
        return listaEspecialidad = FXCollections.observableList(lista);
        
    }
    
    public void nuevo(){
    
        switch(tipoDeOperacion){
        
            case NINGUNO:
                
                limpiarControles();
                activarControles();
                
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                imgNuevo.setImage(new Image("/org/gersonmatta/image/IconoGuardar.png"));
                imgEliminar.setImage(new Image("/org/gersonmatta/image/IconoCancelar.png"));
                
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);

                tblEspecialidades.setDisable(true);
                
                tipoDeOperacion = operaciones.GUARDAR;
                
                break;
                
            case GUARDAR:
                
                guardar();
                limpiarControles();
                desactivarControles();
                
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                imgNuevo.setImage(new Image("/org/gersonmatta/image/Agregar.png"));
                imgEliminar.setImage(new Image("/org/gersonmatta/image/Eliminar.png"));
                
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                
                tblEspecialidades.setDisable(false);

                cargarDatos();
                
                tipoDeOperacion = operaciones.NINGUNO;
                
                break;
        
        }
    
    }
    
    public void guardar(){
    
        Especialidad registro = new Especialidad();
        
        registro.setDescripcion( txtDescripcion.getText() );
        
        try{
        
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEspecialidad(?)}");
            
            
            procedimiento.setString(1, registro.getDescripcion() );
            
            procedimiento.execute();
            listaEspecialidad.add(registro);
            
        }catch(Exception e){
        
            e.printStackTrace();
        
        }
    
    }
    
    public void eliminar(){
    
        switch(tipoDeOperacion){
        
            case GUARDAR:
                
                limpiarControles();
                desactivarControles();
                
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                
                imgNuevo.setImage(new Image("/org/gersonmatta/image/Agregar.png"));
                imgEliminar.setImage(new Image("/org/gersonmatta/image/Eliminar.png"));
                
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                
                tblEspecialidades.setDisable(false);
                
                tipoDeOperacion = operaciones.NINGUNO;
                
                break;
            
            default:
                
                if ( tblEspecialidades.getSelectionModel().getSelectedItem() != null ) {
                    
                    int respuesta = JOptionPane.showConfirmDialog(null, "Está seguro que desea eliminar el registro", "Eliminar Especialidad", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            
                    if (respuesta == JOptionPane.YES_OPTION) {

                        try{

                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarEspecialidad(?)}");
                            
                            procedimiento.setInt(1,  ((Especialidad)tblEspecialidades.getSelectionModel().getSelectedItem()).getCodigoEspecialidad() );
                            procedimiento.execute();
                            
                            listaEspecialidad.remove( tblEspecialidades.getSelectionModel().getSelectedItem() );
                            
                            limpiarControles();


                        }catch(Exception e){

                            e.printStackTrace();

                        }

                    }else{

                        limpiarControles();

                    }
                    
                    
                    
                }else{
                
                    JOptionPane.showMessageDialog(null, "Ha seleccionado algo vacío", "Registro vacío", JOptionPane.WARNING_MESSAGE);
                
                }
                
                
        
                break;
        }
    
    }
    
    public void editar(){
    
        switch(tipoDeOperacion){
        
            case NINGUNO:
                
                if ( tblEspecialidades.getSelectionModel().getSelectedItem() != null ) {
                    
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);

                    imgEditar.setImage( new Image("/org/gersonmatta/image/IconoActualizar.png") );
                    imgReporte.setImage( new Image("/org/gersonmatta/image/IconoCancelar.png") );

                    activarControles();
                    

                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    
                }else{
                
                    JOptionPane.showMessageDialog(null, "No ha seleccionado ningun registro", "Registro vacio", JOptionPane.WARNING_MESSAGE);
                
                }
                
                break;
                
            case ACTUALIZAR:
                
                actualizar();
                
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                
                imgEditar.setImage( new Image("/org/gersonmatta/image/Modificar.png") );
                imgReporte.setImage( new Image("/org/gersonmatta/image/Reporte.png") );
                
                desactivarControles();
                limpiarControles();
                
                cargarDatos();
                
                tipoDeOperacion = operaciones.NINGUNO;
                
                break;
        
        }
    
    }
    
    
    
    public void actualizar(){
    
        try{
        
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarEspecialidad(?,?)}");
            Especialidad registro = (Especialidad) tblEspecialidades.getSelectionModel().getSelectedItem();
            
            registro.setDescripcion( txtDescripcion.getText() );
            
            procedimiento.setInt(1, registro.getCodigoEspecialidad() );
            procedimiento.setString(2, registro.getDescripcion() );
        
            procedimiento.execute();
            
        }catch(Exception e){
        
            e.printStackTrace();
        
        }
    
    }
    
    public void reporte(){
    
        switch(tipoDeOperacion){
            
            case NINGUNO:
                
                imprimirReporte();
                break;
        
            case ACTUALIZAR:
                
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                
                imgEditar.setImage( new Image("/org/gersonmatta/image/Modificar.png") );
                imgReporte.setImage( new Image("/org/gersonmatta/image/Reporte.png") );
                
                desactivarControles();
                limpiarControles();
                
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        
        }
    
    }
    
    public void imprimirReporte(){
    
        Map parametros = new HashMap();
        parametros.put("codigoEspecialidad", null);
        parametros.put("FONDO", GenerarReporte.class.getResource("/org/gersonmatta/image/FondoReporte.png"));
        parametros.put("LOGO", GenerarReporte.class.getResource("/org/gersonmatta/image/IconoPestaña.png"));
        GenerarReporte.mostrarReporte("ReporteEspecialidades.jasper", "Reporte de Especialidades", parametros);
    
    }
    
    public void desactivarControles(){
    
        txtDescripcion.setEditable(false);
    
    }
    
    public void activarControles(){
    
        txtDescripcion.setEditable(true);
        
    }
    
    public void limpiarControles(){
    
        txtCodigoEspecialidad.clear();
        txtDescripcion.clear();
        tblEspecialidades.getSelectionModel().clearSelection();
    
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
    
        escenarioPrincipal.menuPrincipal();
    
    }
    
    
}
