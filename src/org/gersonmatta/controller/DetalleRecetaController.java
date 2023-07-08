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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.gersonmatta.bean.DetalleReceta;
import org.gersonmatta.bean.Medicamento;
import org.gersonmatta.bean.Receta;
import org.gersonmatta.db.Conexion;
import org.gersonmatta.report.GenerarReporte;
import org.gersonmatta.system.Principal;

public class DetalleRecetaController implements Initializable{
    
    private Principal escenarioPrincipal;
    
    private enum operaciones{ GUARDAR, ACTUALIZAR, NINGUNO};
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    
    private final String PAQUETE_IMAGE = "/org/gersonmatta/image/";
    
    private ObservableList<DetalleReceta> listaDetalleReceta;
    private ObservableList<Receta> listaReceta;
    private ObservableList<Medicamento> listaMedicamento;
    
    @FXML private TextField txtCodigoDetalleReceta;
    @FXML private TextField txtDosis;
    
    @FXML private ComboBox cmbReceta;
    @FXML private ComboBox cmbMedicamento;
    
    @FXML private TableView tblDetallesRecetas;
    
    @FXML private TableColumn colCodigoDetalleReceta;
    @FXML private TableColumn colDosis;
    @FXML private TableColumn colReceta;
    @FXML private TableColumn colMedicamento;
    
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
        
        cmbReceta.setItems( getReceta() );
        cmbMedicamento.setItems( getMedicamento() );
        
        cmbReceta.setDisable(true);
        cmbMedicamento.setDisable(true);
        
    }
    
    public void cargarDatos(){
    
        tblDetallesRecetas.setItems( getDetalleReceta() );
        colCodigoDetalleReceta.setCellValueFactory( new PropertyValueFactory("codigoDetalleReceta") );
        colDosis.setCellValueFactory( new PropertyValueFactory("dosis") );
        colReceta.setCellValueFactory( new PropertyValueFactory("codigoReceta") );
        colMedicamento.setCellValueFactory( new PropertyValueFactory("codigoMedicamento") );
    
    }
    
    public ObservableList<DetalleReceta> getDetalleReceta(){
    
        ArrayList<DetalleReceta> lista = new ArrayList<DetalleReceta>();
        
        try{
        
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarDetalleRecetas()}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while( resultado.next() ){
            
                lista.add( new DetalleReceta( 
                
                        resultado.getInt("codigoDetalleReceta"),
                        resultado.getString("dosis"),
                        resultado.getInt("codigoReceta"),
                        resultado.getInt("codigoMedicamento")
                
                ));
                
            }
        
        }catch(Exception e){
            
            e.printStackTrace();
            
        }
        
        return listaDetalleReceta = FXCollections.observableArrayList(lista);
    
    }
    
    public void seleccionarElemento(){
        
        if ( tblDetallesRecetas.getSelectionModel().getSelectedItem() != null ) {
            
            txtCodigoDetalleReceta.setText( Integer.toString( ((DetalleReceta)tblDetallesRecetas.getSelectionModel().getSelectedItem()).getCodigoDetalleReceta() ) );
            txtDosis.setText(  ((DetalleReceta)tblDetallesRecetas.getSelectionModel().getSelectedItem()).getDosis()  );
            cmbReceta.getSelectionModel().select( buscarReceta( ((DetalleReceta)tblDetallesRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta() )  );
            cmbMedicamento.getSelectionModel().select( buscarMedicamento( ((DetalleReceta)tblDetallesRecetas.getSelectionModel().getSelectedItem()).getCodigoMedicamento() ) );
            
        }else{
        
            JOptionPane.showMessageDialog(null, "El registro seleccionado no tiene datos", "Registro vacío", JOptionPane.WARNING_MESSAGE);
        
        }
    
        
    
    }
    
    public ObservableList<Receta> getReceta(){
    
        ArrayList<Receta> lista = new ArrayList<Receta>();
        
        try{
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarRecetas()}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
            
                lista.add( new Receta(
                
                        resultado.getInt("codigoReceta"),
                        resultado.getDate("fechaReceta"),
                        resultado.getInt("numeroColegiado")
                
                ) );
            
            }
            
        }catch(Exception e){
            
            e.printStackTrace();
            
        }
        
        return listaReceta = FXCollections.observableArrayList(lista);
    
    }
    
    public Receta buscarReceta(int codigoReceta){
    
        Receta resultado = null;
        
        try{
        
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarReceta(?)}");
            procedimiento.setInt(1, codigoReceta);
            
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
            
                resultado = new Receta(
                
                        registro.getInt("codigoReceta"),
                        registro.getDate("fechaReceta"),
                        registro.getInt("numeroColegiado")
                        
                
                );
            
            }
        
        }catch(Exception e){
        
            e.printStackTrace();
        
        }
        
        return resultado;
    
    }
    
    public ObservableList<Medicamento> getMedicamento(){
    
        ArrayList<Medicamento> lista = new ArrayList<Medicamento>();
        
        try{
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarMedicamentos()}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while( resultado.next() ){
            
                lista.add( new Medicamento(
                
                        resultado.getInt("codigoMedicamento"),
                        resultado.getString("nombreMedicamento")
                
                ) );
            
            }
            
        }catch(Exception e){
        
            e.printStackTrace();
        
        }
    
        return listaMedicamento = FXCollections.observableArrayList(lista);
    
    }
    
    public Medicamento buscarMedicamento(int codigoMedicamento){
    
        Medicamento resultado = null;
        
        try{
        
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarMedicamento(?)}");
            procedimiento.setInt(1, codigoMedicamento);
            
            ResultSet registro = procedimiento.executeQuery();
            
            while( registro.next() ){
                
                resultado = new Medicamento(
                
                        registro.getInt("codigoMedicamento"),
                        registro.getString("nombreMedicamento")
                
                );
            
            
            }
        
        }catch(Exception e){
        
            e.printStackTrace();
        
        }
        
        return resultado;
    
    }
    
    public void nuevo(){
    
        switch(tipoDeOperaciones){
        
            case NINGUNO:
                
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                
                imgNuevo.setImage( new Image(PAQUETE_IMAGE+"IconoGuardar.png") );
                imgEliminar.setImage( new Image(PAQUETE_IMAGE+"IconoCancelar.png") );
                
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                
                tblDetallesRecetas.setDisable(true);
                
                limpiarControles();
                activarControles();
                
                tipoDeOperaciones = operaciones.GUARDAR;
                
                break;
            
            case GUARDAR:
                
                guardar();
                
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                
                imgNuevo.setImage( new Image(PAQUETE_IMAGE+"Agregar.png") );
                imgEliminar.setImage( new Image(PAQUETE_IMAGE+"Eliminar.png") );
                
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                
                tblDetallesRecetas.setDisable(false);
                
                limpiarControles();
                desactivarControles();
                
                cargarDatos();
                
                tipoDeOperaciones = operaciones.NINGUNO;
                
                
                break;
        }
    
    }
    public void guardar(){
        
        DetalleReceta registro = new DetalleReceta();
        
        registro.setDosis( txtDosis.getText() );
        registro.setCodigoReceta( ((Receta)cmbReceta.getSelectionModel().getSelectedItem()).getCodigoReceta() );
        registro.setCodigoMedicamento( ((Medicamento)cmbMedicamento.getSelectionModel().getSelectedItem()).getCodigoMedicamento() );
    
        try{

            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarDetalleReceta(?, ?, ?)}");

            procedimiento.setString(1, registro.getDosis() );
            procedimiento.setInt(2, registro.getCodigoReceta() );
            procedimiento.setInt(3, registro.getCodigoMedicamento() );
            
            procedimiento.execute();
            
            listaDetalleReceta.add(registro);

        }catch(Exception e){

            e.printStackTrace();

        }
    
    }
    
    public void eliminar(){
    
        switch(tipoDeOperaciones){
        
            case GUARDAR:
                
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                
                imgNuevo.setImage( new Image(PAQUETE_IMAGE+"Agregar.png") );
                imgEliminar.setImage( new Image(PAQUETE_IMAGE+"Eliminar.png") );
                
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                
                tblDetallesRecetas.setDisable(false);
                
                limpiarControles();
                desactivarControles();
                
                tipoDeOperaciones = operaciones.NINGUNO;
                
                break;  
                
            default:
                
                if ( tblDetallesRecetas.getSelectionModel().getSelectedItem() != null ) {
                    
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea eliminar este registro?", "Eliminar Registro", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    
                    if ( respuesta == JOptionPane.YES_OPTION) {
                        
                        try{
                        
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarDetalleReceta(?)}");
                            procedimiento.setInt(1, ((DetalleReceta)tblDetallesRecetas.getSelectionModel().getSelectedItem()).getCodigoDetalleReceta() );
                            procedimiento.execute();
                            
                            listaDetalleReceta.remove( tblDetallesRecetas.getSelectionModel().getSelectedItem() );
                            
                            limpiarControles();
                        
                        }catch(Exception e){
                        
                            e.printStackTrace();
                        
                        }
                        
                    }else{
                    
                        limpiarControles();
                    
                    }
                    
                }else{
                
                    JOptionPane.showMessageDialog(null, "Seleccione un registro", "Registro vacío", JOptionPane.WARNING_MESSAGE);
                    limpiarControles();
                
                }
                
                break;
        }
    
    
    
    }
    public void editar(){
    
        switch(tipoDeOperaciones){
        
            case NINGUNO:
                
                if ( tblDetallesRecetas.getSelectionModel().getSelectedItem() != null ) {
                    
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");

                    imgEditar.setImage( new Image(PAQUETE_IMAGE+"IconoActualizar.png"));
                    imgReporte.setImage( new Image( PAQUETE_IMAGE+"IconoCancelar.png" ) );

                    activarControles();
                    cmbReceta.setDisable(true);
                    cmbMedicamento.setDisable(true);

                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);

                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                    
                }else{
                
                    JOptionPane.showMessageDialog(null, "Ningun registro seleccionado", "Registro sin seleccionar", JOptionPane.WARNING_MESSAGE );
                    limpiarControles();
                    
                }
                
                break;
                
            case ACTUALIZAR:
                
                actualizar();
                
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                
                imgEditar.setImage( new Image(PAQUETE_IMAGE+"Modificar.png"));
                imgReporte.setImage( new Image( PAQUETE_IMAGE+"Reporte.png" ) );
                
                desactivarControles();
                limpiarControles();
                
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                
                cargarDatos();
                
                tipoDeOperaciones = operaciones.NINGUNO;
                
                break;
        
        }
    
    }
    public void actualizar(){
    
        try{
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarDetalleReceta(?, ?)}");
            
            DetalleReceta registro = (DetalleReceta)tblDetallesRecetas.getSelectionModel().getSelectedItem();
            
            registro.setDosis( txtDosis.getText() );
            
            procedimiento.setString(1, registro.getDosis() );
            procedimiento.setInt(2, registro.getCodigoDetalleReceta() );
            
            procedimiento.execute();
            
        
        }catch(Exception e){
        
            e.printStackTrace();
        
        }
    
    }
    
    public void reporte(){
    
        switch(tipoDeOperaciones){
            
            case NINGUNO:
                
                if ( tblDetallesRecetas.getSelectionModel().getSelectedItem() != null ) {
                    
                    imprimirReporte();
                    
                }else{
                
                    JOptionPane.showMessageDialog(null, "Seleccione un registro", "Registro vacío", JOptionPane.WARNING_MESSAGE);
                
                }
                
                break;
            
            case ACTUALIZAR:
                
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                
                imgEditar.setImage( new Image(PAQUETE_IMAGE+"Modificar.png"));
                imgReporte.setImage( new Image( PAQUETE_IMAGE+"Reporte.png" ) );
                
                desactivarControles();
                limpiarControles();
                
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                
                tipoDeOperaciones = operaciones.NINGUNO;
                
                break;
        
        }
    
    }
    
    public void imprimirReporte(){
    
        Map parametros = new HashMap();
        parametros.put("codigoDetalleReceta", null);
        /*parametros.put("FONDO", GenerarReporte.class.getResource("/org/gersonmatta/image/FondoReporte.png"));
        parametros.put("LOGO", GenerarReporte.class.getResource("/org/gersonmatta/image/IconoPestaña.png"));*/
        //parametros.put("FIRMA", GenerarReporte.class.getResource("/org/gersonmatta/image/Firma.png"));
        
        int codReceta = ((DetalleReceta)tblDetallesRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta();
        
        parametros.put("codReceta", codReceta);
        
        GenerarReporte.mostrarReporte("ReporteDetallesRecetas.jasper", "Reporte Detalles de Recetas", parametros);
    
    }
    
    public void activarControles(){
    
        txtDosis.setEditable(true);
        cmbReceta.setDisable(false);
        cmbMedicamento.setDisable(false);
    
    }
    public void desactivarControles(){
    
        txtDosis.setEditable(false);
        cmbReceta.setDisable(true);
        cmbMedicamento.setDisable(true);
    
    }
    public void limpiarControles(){
    
        txtCodigoDetalleReceta.clear();
        txtDosis.clear();
        cmbReceta.setValue(null);
        cmbMedicamento.setValue(null);
        tblDetallesRecetas.getSelectionModel().clearSelection();
    
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
