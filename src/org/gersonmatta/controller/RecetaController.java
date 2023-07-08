package org.gersonmatta.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
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
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.gersonmatta.bean.Doctor;
import org.gersonmatta.bean.Receta;
import org.gersonmatta.db.Conexion;
import org.gersonmatta.report.GenerarReporte;
import org.gersonmatta.system.Principal;

public class RecetaController implements Initializable{
    
    private Principal escenarioPrincipal;
    
    private enum operaciones{NUEVO, ELIMINAR, EDITAR, GUARDAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    
    private final String PAQUETE_IMAGE = "/org/gersonmatta/image/";
    
    private ObservableList<Receta> listaReceta;
    private ObservableList<Doctor> listaDoctor;
    
    private DatePicker fReceta;
    
    @FXML private TextField txtCodigoReceta;

    @FXML private ComboBox cmbNumeroColegiado;
    
    @FXML private GridPane grpFechas;
    
    @FXML private TableView tblRecetas;
    
    @FXML private TableColumn colCodigoReceta;
    @FXML private TableColumn colFechaReceta;
    @FXML private TableColumn colNumeroColegiado;
    
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
        
        fReceta = new DatePicker(Locale.ENGLISH);
        fReceta.setDateFormat( new SimpleDateFormat("yyyy-MM-dd") );
        fReceta.getCalendarView().todayButtonTextProperty().set("Today");
        fReceta.getCalendarView().setShowWeeks(false);
        grpFechas.add(fReceta, 4, 0);
        fReceta.getStylesheets().add("/org/gersonmatta/resource/DatePicker.css");
        
        cmbNumeroColegiado.setItems( getDoctor() );
        cmbNumeroColegiado.setDisable(true);
        fReceta.setDisable(true);
        
    }
    
    public void cargarDatos(){
        
        tblRecetas.setItems( getReceta() );
        colCodigoReceta.setCellValueFactory( new PropertyValueFactory<Receta,Integer>("codigoReceta") );
        colFechaReceta.setCellValueFactory( new PropertyValueFactory<Receta, Date>("fechaReceta"));
        colNumeroColegiado.setCellValueFactory(  new PropertyValueFactory<Receta, Integer>("numeroColegiado"));
    
    }
    
    public void seleccionarElemento(){
        
        if ( tblRecetas.getSelectionModel().getSelectedItem() != null ) {
            
            txtCodigoReceta.setText( Integer.toString(((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta()));
            fReceta.selectedDateProperty().set( ((Receta)(tblRecetas.getSelectionModel().getSelectedItem())).getFechaReceta() );
            cmbNumeroColegiado.getSelectionModel().select( buscarDoctor( ((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getNumeroColegiado() ) );
            
        }else{
        
            JOptionPane.showMessageDialog(null, "El registro seleccionado esta sin datos", "Registro vacío", JOptionPane.WARNING_MESSAGE);
        
        }
    
    }
    
    public ObservableList<Receta> getReceta(){
    
        ArrayList<Receta> lista = new ArrayList<Receta>();
        
        try{
        
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarRecetas()}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
            
                lista.add(new Receta(
                
                        resultado.getInt("codigoReceta"),
                        resultado.getDate("fechaReceta"),
                        resultado.getInt("numeroColegiado")
                
                
                ));
            
            }
        
        
        }catch(Exception e){
        
            e.printStackTrace();
        
        }
        
        return listaReceta = FXCollections.observableArrayList(lista);
    
    }
    
    public ObservableList<Doctor> getDoctor(){
    
        ArrayList<Doctor> lista = new ArrayList<Doctor>();
        
        try{
        
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarDoctores}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while( resultado.next() ){
            
                lista.add( new Doctor(
                
                        resultado.getInt("numeroColegiado"),
                        resultado.getString("nombresDoctor"),
                        resultado.getString("apellidosDoctor"),
                        resultado.getString("telefonoContacto"),
                        resultado.getInt("codigoEspecialidad")
                
                ) );
            
            }
        
        }catch(Exception e){
        
            e.printStackTrace();
        
        }
        
        return listaDoctor = FXCollections.observableArrayList(lista);
    
    }
    
    public Doctor buscarDoctor(int codigoDoctor){
    
        Doctor resultado = null;
        
        try{
        
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarDoctor(?)}");
            
            procedimiento.setInt(1, codigoDoctor);
            
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
            
                resultado = new Doctor(
                
                        registro.getInt("numeroColegiado"),
                        registro.getString("nombresDoctor"),
                        registro.getString("apellidosDoctor"),
                        registro.getString("telefonoContacto"),
                        registro.getInt("codigoEspecialidad")
                        
                
                );
            
            }
        
        }catch(Exception e){
        
            e.printStackTrace();
        
        }
        
        return resultado;
    
    }
    
    public void nuevo(){
    
        switch(tipoDeOperacion){
        
            case NINGUNO:
                
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                
                imgNuevo.setImage(new Image( PAQUETE_IMAGE+"IconoGuardar.png" ));
                imgEliminar.setImage(new Image( PAQUETE_IMAGE+"IconoCancelar.png" ));
                
                tblRecetas.setDisable(true);
                
                limpiarControles();
                activarControles();
                
                tipoDeOperacion = operaciones.GUARDAR;
                
                break;
                
            case GUARDAR:
                    
                    guardar();

                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");

                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);

                    imgNuevo.setImage(new Image( PAQUETE_IMAGE+"Agregar.png" ));
                    imgEliminar.setImage(new Image( PAQUETE_IMAGE+"Eliminar.png" ));

                    tblRecetas.setDisable(false);

                    limpiarControles();
                    desactivarControles();

                    tipoDeOperacion = operaciones.NINGUNO;

                    cargarDatos();
                    
                
                break;
        
        }
    
    }

    public void guardar(){
    
        Receta registro = new Receta();
        
        registro.setFechaReceta( fReceta.getSelectedDate() );
        registro.setNumeroColegiado( ((Doctor)cmbNumeroColegiado.getSelectionModel().getSelectedItem()).getNumeroColegiado() );
        
        try{
        
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarReceta(?,?)}");
            
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaReceta().getTime() )  );
            procedimiento.setInt(2, registro.getNumeroColegiado());
            
            procedimiento.execute();
            
            listaReceta.add(registro);
        
        }catch(Exception e){
        
            e.printStackTrace();
        
        }
        
        
    }
    
    public void eliminar(){
        
        switch(tipoDeOperacion){
        
            case GUARDAR:
                
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                
                imgNuevo.setImage(new Image( PAQUETE_IMAGE+"Agregar.png" ));
                imgEliminar.setImage(new Image( PAQUETE_IMAGE+"Eliminar.png" ));
                
                tblRecetas.setDisable(false);
                
                limpiarControles();
                desactivarControles();
                
                tipoDeOperacion = operaciones.NINGUNO;
                
                break;
        
                
            default:
                
                if ( tblRecetas.getSelectionModel().getSelectedItem() != null ) {
                    
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar este registro?", "Eliminar Registro", JOptionPane.YES_NO_OPTION);
                    
                    if (respuesta == JOptionPane.YES_OPTION) {
                        
                        try{
                        
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarReceta(?)}");
                            
                            procedimiento.setInt(1, ((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta() );
                            
                            procedimiento.execute();
                            
                            listaReceta.remove( tblRecetas.getSelectionModel().getSelectedItem() );
                            
                            limpiarControles();
                        
                        }catch(Exception e){
                        
                            e.printStackTrace();
                        
                        }
                        
                    }else{
                    
                        limpiarControles();
                    
                    }
                    
                }else{
                
                    JOptionPane.showMessageDialog(null, "No ha seleccionado ningún registro","Registro vacío",JOptionPane.WARNING_MESSAGE);
                
                }
                
                break;
        }
        
    }
    
    public void editar(){
    
        switch(tipoDeOperacion){
        
            case NINGUNO:
                
                if ( tblRecetas.getSelectionModel().getSelectedItem() != null ) {
                    
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");

                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);

                    imgEditar.setImage(new Image( PAQUETE_IMAGE+"IconoActualizar.png" ));
                    imgReporte.setImage(new Image( PAQUETE_IMAGE+"IconoCancelar.png" ));

                    activarControles();
                    cmbNumeroColegiado.setDisable(true);

                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    
                }else{
                
                    JOptionPane.showMessageDialog(null, "No hay un registro seleccionado", "Registro Vacío", JOptionPane.WARNING_MESSAGE);
                
                }
                
                break;
                
            case ACTUALIZAR:
                
                actualizar();
                
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                
                imgEditar.setImage(new Image( PAQUETE_IMAGE+"Modificar.png" ));
                imgReporte.setImage(new Image( PAQUETE_IMAGE+"Reporte.png" ));
                
                limpiarControles();
                desactivarControles();
                
                cargarDatos();
                
                tipoDeOperacion = operaciones.NINGUNO;
                
                break;
        
        }
        
    }
    
    public void actualizar(){
        
        try{

            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarReceta(?, ?)}");

            Receta registro = (Receta)tblRecetas.getSelectionModel().getSelectedItem();

            registro.setFechaReceta( fReceta.getSelectedDate() );

            procedimiento.setDate(1, new java.sql.Date(registro.getFechaReceta().getTime() ) );
            procedimiento.setInt(2, registro.getCodigoReceta() );

            procedimiento.execute();


        }catch(Exception e){

           e.printStackTrace();

        }
    
    }
    
    public void reporte(){
        
        switch(tipoDeOperacion){
            
            case NINGUNO:
                
                if ( tblRecetas.getSelectionModel().getSelectedItem() != null ) {
                    
                    imprimirReporte();
                    
                }else{
                
                    JOptionPane.showMessageDialog(null, "Seleccione un registro", "Registro vacío", JOptionPane.WARNING_MESSAGE);
                
                }
                
                break;
        
            case ACTUALIZAR:
                
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                
                imgEditar.setImage(new Image( PAQUETE_IMAGE+"Modificar.png" ));
                imgReporte.setImage(new Image( PAQUETE_IMAGE+"Reporte.png" ));
                
                limpiarControles();
                desactivarControles();
                
                tipoDeOperacion = operaciones.NINGUNO;
                
                break;
                
        }
    
    }
    
    public void imprimirReporte(){
        
        //String codPaciente = JOptionPane.showInputDialog(null, "Por favor ingresa el codigo del Paciente", "Codigo Paciente", JOptionPane.QUESTION_MESSAGE);
        

            
            Map parametros = new HashMap();

            parametros.put("codigoCita", null);
            parametros.put("FONDO", GenerarReporte.class.getResource("/org/gersonmatta/image/FondoReporte.png"));
            parametros.put("LOGO", GenerarReporte.class.getResource("/org/gersonmatta/image/IconoPestaña.png"));
            parametros.put("FIRMA", GenerarReporte.class.getResource("/org/gersonmatta/image/Firma.png"));

            int codReceta = ((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta();
            int numColegiado = ((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getNumeroColegiado();

            parametros.put("codReceta", codReceta);
            //parametros.put("codPaciente", Integer.parseInt(codPaciente) );
            parametros.put("numColegiado", numColegiado);

            GenerarReporte.mostrarReporte("ReporteReceta.jasper", "Reporte de Recetas", parametros);
            
            

    
    }
    
    public void activarControles(){
        
        fReceta.setDisable(false);
        cmbNumeroColegiado.setDisable(false);
    
    }
    
    public void desactivarControles(){
        
        fReceta.setDisable(true);
        cmbNumeroColegiado.setDisable(true);
        
    }
    
    public void limpiarControles(){
    
        txtCodigoReceta.clear();
        fReceta.selectedDateProperty().set(null);
        tblRecetas.getSelectionModel().clearSelection();
        cmbNumeroColegiado.setValue(null);
    
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
