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
import javax.swing.JOptionPane;
import org.gersonmatta.bean.Medicamento;
import org.gersonmatta.db.Conexion;
import org.gersonmatta.report.GenerarReporte;
import org.gersonmatta.system.Principal;

public class MedicamentoController implements Initializable{
    
    private Principal escenarioPrincipal;
    
    private enum operaciones{NUEVO,ELIMINAR,EDITAR,REPORTE,GUARDAR,CANCELAR,ACTUALIZAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    
    private ObservableList<Medicamento> listaMedicamento;
    
    private final String PAQUETE_IMAGE = "/org/gersonmatta/image/";

    @FXML private TextField txtCodigoMedicamento;
    @FXML private TextField txtNombreMedicamento;
    
    @FXML private GridPane grpFechas;
    
    @FXML private TableView tblMedicamentos;
    
    @FXML private TableColumn colCodigoMedicamento;
    @FXML private TableColumn colNombreMedicamento;
    
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
    
        tblMedicamentos.setItems( getMedicamento() );
        colCodigoMedicamento.setCellValueFactory( new PropertyValueFactory<Medicamento, Integer>("codigoMedicamento") );
        colNombreMedicamento.setCellValueFactory( new PropertyValueFactory<Medicamento, String>("nombreMedicamento") );
    
    }
    
    public void seleccionarElemento(){
        
        if ( tblMedicamentos.getSelectionModel().getSelectedItem() != null ) {
            
            txtCodigoMedicamento.setText( String.valueOf( ((Medicamento)tblMedicamentos.getSelectionModel().getSelectedItem()).getCodigoMedicamento() ) );
            txtNombreMedicamento.setText( String.valueOf( ((Medicamento)tblMedicamentos.getSelectionModel().getSelectedItem()).getNombreMedicamento() ) );
            
        }else{
        
            JOptionPane.showMessageDialog(null, "El registro que ha seleccionado\nse encuentra vacío", "Registro sin datos", JOptionPane.WARNING_MESSAGE);
            
        }
        
        
    
    
    }
    
    public ObservableList<Medicamento> getMedicamento(){
    
        ArrayList<Medicamento> lista = new ArrayList<Medicamento>();
        
        try{
        
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarMedicamentos()}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while( resultado.next() ){
            
                lista.add( new Medicamento( resultado.getInt    ("codigoMedicamento"),
                                            resultado.getString ("nombreMedicamento")
                        
                ) );
            
            
            }
            
        
        }catch(Exception e){
        
            e.printStackTrace();
        
        }
        
        return listaMedicamento = FXCollections.observableList(lista);
    
    }
    
    public void nuevo(){
    
        switch(tipoDeOperacion){
        
            case NINGUNO:
                
                activarControles();
                limpiarControles();
                
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                
                imgNuevo.setImage( new Image(PAQUETE_IMAGE+"IconoGuardar.png") );
                imgEliminar.setImage( new Image(PAQUETE_IMAGE+"IconoCancelar.png") );
                
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                
                tblMedicamentos.setDisable(true);
                
                tipoDeOperacion = operaciones.GUARDAR;
                
                break;
                
            case GUARDAR:
                
                guardar();
                desactivarControles();
                limpiarControles();
                
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                
                imgNuevo.setImage( new Image(PAQUETE_IMAGE+"Agregar.png") );
                imgEliminar.setImage( new Image(PAQUETE_IMAGE+"Eliminar.png") );
                
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                
                tblMedicamentos.setDisable(false);
                
                cargarDatos();
                
                tipoDeOperacion = operaciones.NINGUNO;
                
                break;
        
        }
    
    }
    
    public void guardar(){
    
        Medicamento registro = new Medicamento();
        
        registro.setNombreMedicamento( txtNombreMedicamento.getText() );
        
        try{
        
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarMedicamento(?)}");
            
            procedimiento.setString(1, registro.getNombreMedicamento() );
            
            procedimiento.execute();
            
            listaMedicamento.add(registro);
        
        }catch(Exception e){
        
            e.printStackTrace();
        
        }
    
    }
    
    public void eliminar(){
    
        switch(tipoDeOperacion){
        
            case GUARDAR:
                
                desactivarControles();
                limpiarControles();
                
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                
                imgNuevo.setImage( new Image(PAQUETE_IMAGE+"Agregar.png") );
                imgEliminar.setImage( new Image(PAQUETE_IMAGE+"Eliminar.png") );
                
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                
                tblMedicamentos.setDisable(false);
                
                tipoDeOperacion = operaciones.NINGUNO;
                
                break;
                
            default:
                
                if ( tblMedicamentos.getSelectionModel().getSelectedItem() != null ) {
                    
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea eliminar este registro?\n[Esta acción es irreversible]", "Eliminar Registro", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE );
                    
                    if ( respuesta == JOptionPane.YES_OPTION) {
                        
                        try{
                        
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarMedicamento(?)}");
                            
                            procedimiento.setInt(1, ((Medicamento)tblMedicamentos.getSelectionModel().getSelectedItem()).getCodigoMedicamento() );
                            procedimiento.execute();
                            
                            listaMedicamento.remove( tblMedicamentos.getSelectionModel().getSelectedItem() );
                            
                            limpiarControles();
                        
                        }catch(Exception e){
                        
                            e.printStackTrace();
                        
                        }
                        
                    }else{
                    
                        limpiarControles();
                    
                    }
                    
                }else{
                
                    JOptionPane.showMessageDialog(null, "Registro seleccionado sin datos", "Registro vacío", JOptionPane.WARNING_MESSAGE);
                
                }
            
                break;
        }
    
    }
    
    public void editar(){
    
        switch(tipoDeOperacion){
        
            case NINGUNO:
                
                if ( tblMedicamentos.getSelectionModel().getSelectedItem() != null) {
                    
                    activarControles();
                
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");

                    imgEditar.setImage( new Image(PAQUETE_IMAGE+"IconoActualizar.png") );
                    imgReporte.setImage( new Image( PAQUETE_IMAGE+"IconoCancelar.png" ) );

                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);

                    tipoDeOperacion = operaciones.ACTUALIZAR;                   
                    
                }else{
                
                    JOptionPane.showMessageDialog(null, "No ha seleccionado ningun registro", "Registro vacío", JOptionPane.WARNING_MESSAGE);
                
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
        
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarMedicamento(?,?)}");
            Medicamento registro = (Medicamento) tblMedicamentos.getSelectionModel().getSelectedItem();
            
            registro.setNombreMedicamento(txtNombreMedicamento.getText() );
            
            procedimiento.setInt(1, registro.getCodigoMedicamento());
            procedimiento.setString(2, registro.getNombreMedicamento());
        
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
                
                desactivarControles();
                limpiarControles();
                
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                
                imgEditar.setImage( new Image(PAQUETE_IMAGE+"Modificar.png") );
                imgReporte.setImage( new Image(PAQUETE_IMAGE+"Reporte.png") );
                
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                
                tipoDeOperacion = operaciones.NINGUNO;
                
                break;
        
        
        }
    
    
    }
    
    public void imprimirReporte(){
    
        Map parametros = new HashMap();
        parametros.put("codigoMedicamento", null);
        parametros.put("FONDO", GenerarReporte.class.getResource("/org/gersonmatta/image/FondoReporte.png"));
        parametros.put("LOGO", GenerarReporte.class.getResource("/org/gersonmatta/image/IconoPestaña.png"));
        GenerarReporte.mostrarReporte("ReporteMedicamentos.jasper", "Reporte de Medicamentos", parametros);
    
    }
    
    public void desactivarControles(){
    
        txtNombreMedicamento.setEditable(false);
    
    }
    
    public void activarControles(){
    
        txtNombreMedicamento.setEditable(true);
    
    }
    
    public void limpiarControles(){
    
        txtCodigoMedicamento.clear();
        txtNombreMedicamento.clear();
        tblMedicamentos.getSelectionModel().clearSelection();
    
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
