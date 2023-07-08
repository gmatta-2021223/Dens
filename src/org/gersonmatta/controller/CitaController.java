package org.gersonmatta.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTimePicker;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
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
import org.gersonmatta.bean.Cita;
import org.gersonmatta.bean.Doctor;
import org.gersonmatta.bean.Paciente;
import org.gersonmatta.db.Conexion;
import org.gersonmatta.report.GenerarReporte;
import org.gersonmatta.system.Principal;

public class CitaController implements Initializable{
    
    private Principal escenarioPrincipal;
    
    private enum operaciones{GUARDAR,ACTUALIZAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;  
    
    private final String PAQUETE_IMAGE = "/org/gersonmatta/image/";
    
    private ObservableList<Cita> listaCita;
    private ObservableList<Paciente> listaPaciente;
    private ObservableList<Doctor> listaDoctor;
    
    private DatePicker fCita;
   
    
    @FXML private TextField txtCodigoCitas;
    @FXML private JFXTimePicker jfxHoraCita;
    @FXML private TextField txtTratamiento;
    @FXML private TextField txtDescripCondActual;
    
    @FXML private ComboBox cmbPaciente;
    @FXML private ComboBox cmbDoctor;
    
    @FXML private GridPane grpFechas;
    
    @FXML private TableView tblCitas;
    
    @FXML private TableColumn colCodigoCita;
    @FXML private TableColumn colFechaCita;
    @FXML private TableColumn colHoraCita;
    @FXML private TableColumn colTratamiento;
    @FXML private TableColumn colDescripCondActual;
    @FXML private TableColumn colPaciente;
    @FXML private TableColumn colDoctor;
    
    @FXML private JFXButton btnNuevo;
    @FXML private JFXButton btnEliminar;
    @FXML private JFXButton btnEditar;
    @FXML private JFXButton btnReporte;
    
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        cargarDatos();
        
        jfxHoraCita.set24HourView(true);
        
        fCita = new DatePicker(Locale.ENGLISH);
        fCita.setDateFormat( new SimpleDateFormat("yyyy-MM-dd") );
        fCita.getCalendarView().todayButtonTextProperty().set("Today");
        fCita.getCalendarView().setShowWeeks(false);
        grpFechas.add(fCita, 4, 0);
        fCita.getStylesheets().add("/org/gersonmatta/resource/DatePicker.css");
        fCita.setDisable(true);
        
        
        
        cmbPaciente.setItems( getPaciente() );
        cmbDoctor.setItems( getDoctor() );
        
    }
    
    public void cargarDatos(){
    
        tblCitas.setItems( getCita() );
        colCodigoCita.setCellValueFactory( new PropertyValueFactory<Cita,Integer>("codigoCita") );
        colFechaCita.setCellValueFactory( new PropertyValueFactory<Cita, Date>("fechaCita") );
        colHoraCita.setCellValueFactory( new PropertyValueFactory<Cita, Time>("horaCita") );
        colTratamiento.setCellValueFactory( new PropertyValueFactory<Cita, String>("tratamiento") );
        colDescripCondActual.setCellValueFactory( new PropertyValueFactory<Cita, String>("descripCondActual") );
        colPaciente.setCellValueFactory( new PropertyValueFactory<Cita, Integer>("codigoPaciente") );
        colDoctor.setCellValueFactory( new PropertyValueFactory<Cita, Integer>("numeroColegiado") );
    
    }
    
    public ObservableList<Cita> getCita(){
    
        ArrayList<Cita> lista = new ArrayList<>();
        
        try{
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCitas}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while( resultado.next() ){
            
                lista.add( new Cita(
                
                    resultado.getInt("codigoCita"),
                    resultado.getDate("fechaCita"),
                    resultado.getTime("horaCita"),
                    resultado.getString("tratamiento"),
                    resultado.getString("descripCondActual"),
                    resultado.getInt("codigoPaciente"),
                    resultado.getInt("numeroColegiado")
                
                ) );

            }
            
        
        }catch(Exception e){
            
            e.printStackTrace();
        
        }
        
        return listaCita = FXCollections.observableArrayList(lista);
    
    }
    
    public ObservableList<Paciente> getPaciente(){
    
        ArrayList<Paciente> lista = new ArrayList<Paciente>();
        
        try{
        
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPacientes}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
            
                lista.add( new Paciente(
                
                    resultado.getInt("codigoPaciente"),
                    resultado.getString("nombresPaciente"),
                    resultado.getString("apellidosPaciente"),
                    resultado.getString("sexo"),
                    resultado.getDate("fechaNacimiento"),
                    resultado.getString("direccionPaciente"),
                    resultado.getString("telefonoPersonal"),
                    resultado.getDate("fechaPrimeraVisita")
                
                ) );
                        
            
            }
        
        }catch(Exception e){
        
            e.printStackTrace();
        
        }
        
        return listaPaciente = FXCollections.observableArrayList(lista);
    
    }
    
    public ObservableList<Doctor> getDoctor(){
    
        ArrayList<Doctor> lista = new ArrayList<Doctor>();
        
        try{
        
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarDoctores}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
            
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
    
    public void seleccionarElemento(){
        
        if ( tblCitas.getSelectionModel().getSelectedItem() != null )  {
            
            txtCodigoCitas.setText( Integer.toString(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getCodigoCita()) );
            fCita.selectedDateProperty().set( ((Cita)tblCitas.getSelectionModel().getSelectedItem()).getFechaCita() );
            /*txtHoraCita.setText( String.valueOf(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getHoraCita()) );*/
            jfxHoraCita.getEditor().setText( ((Cita)tblCitas.getSelectionModel().getSelectedItem()).getHoraCita().toString() );
            txtTratamiento.setText( ((Cita)tblCitas.getSelectionModel().getSelectedItem()).getTratamiento() );
            txtDescripCondActual.setText( ((Cita)tblCitas.getSelectionModel().getSelectedItem()).getDescripCondActual() );
            cmbPaciente.getSelectionModel().select( buscarPaciente( ((Cita)tblCitas.getSelectionModel().getSelectedItem()).getCodigoPaciente() ) );
            cmbDoctor.getSelectionModel().select( buscarDoctor( ((Cita)tblCitas.getSelectionModel().getSelectedItem()).getNumeroColegiado() ) );
            
        }else{
        
            JOptionPane.showMessageDialog(null, "Ha seleccionado un registro sin datos", "Registro vacío", JOptionPane.WARNING_MESSAGE);
        
        }
    
    }
    
    public Paciente buscarPaciente(int codigoPaciente){
    
        Paciente resultado = null;
        
        try{
        
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarPaciente(?)}");
            procedimiento.setInt(1, codigoPaciente);
            
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next() ){
            
                resultado = new Paciente(
                
                        registro.getInt("codigoPaciente"),
                        registro.getString("nombresPaciente"),
                        registro.getString("apellidosPaciente"),
                        registro.getString("sexo"),
                        registro.getDate("fechaNacimiento"),
                        registro.getString("direccionPaciente"),
                        registro.getString("telefonoPersonal"),
                        registro.getDate("fechaPrimeraVisita")
                
                );
            
            }
        
        }catch(Exception e){
        
            e.printStackTrace();
        
        }
        
        return resultado;
    
    }
   
    public Doctor buscarDoctor(int numeroColegiado){
    
        Doctor resultado = null;
        
        try{
        
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarDoctor(?)}");
            procedimiento.setInt(1, numeroColegiado);
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
    
        switch( tipoDeOperacion ){
        
            case NINGUNO:
                
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                
                imgNuevo.setImage( new Image(PAQUETE_IMAGE+"IconoGuardar.png") );
                imgEliminar.setImage( new Image(PAQUETE_IMAGE+"IconoCancelar.png") );
                
                tblCitas.setDisable(true);
                
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
                
                imgNuevo.setImage( new Image(PAQUETE_IMAGE+"Agregar.png") );
                imgEliminar.setImage( new Image(PAQUETE_IMAGE+"Eliminar.png") );
                
                tblCitas.setDisable(false);
                
                limpiarControles();
                desactivarControles();
                
                cargarDatos();
                
                tipoDeOperacion = operaciones.NINGUNO;
                
                break;
        
        }
    
    }
    
    public void guardar(){
        
        Cita registro = new Cita();
        // jfxHoraCita.getValue()
        //Time.valueOf()
        registro.setFechaCita( fCita.getSelectedDate() );
        registro.setHoraCita( Time.valueOf(jfxHoraCita.getValue()) );
        registro.setTratamiento( txtTratamiento.getText() );
        registro.setDescripCondActual( txtDescripCondActual.getText() );
        registro.setCodigoPaciente( ((Paciente)cmbPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente() );
        registro.setNumeroColegiado( ((Doctor)cmbDoctor.getSelectionModel().getSelectedItem()).getNumeroColegiado() );
        
        /*System.out.println( registro.getFechaCita() );
        System.out.println( registro.getHoraCita() );
        System.out.println( registro.getTratamiento() );
        System.out.println( registro.getDescripCondActual() );
        System.out.println( registro.getCodigoPaciente() );
        System.out.println( registro.getNumeroColegiado() );*/
    
        try{
        
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarCita(?,?,?,?,?,?)}");
            
            procedimiento.setDate(1, new java.sql.Date( registro.getFechaCita().getTime() )  );
            procedimiento.setTime(2, registro.getHoraCita()   );
            procedimiento.setString(3, registro.getTratamiento());
            procedimiento.setString(4, registro.getDescripCondActual());
            procedimiento.setInt(5, registro.getCodigoPaciente());
            procedimiento.setInt(6, registro.getNumeroColegiado());
            
            procedimiento.execute();
            
            listaCita.add(registro);
            
        
        }catch(Exception e){
        
            e.printStackTrace();
        
        }
        
    }
    
    public void eliminar(){
    
        switch( tipoDeOperacion ){

                case NINGUNO:
                    
                    if ( tblCitas.getSelectionModel().getSelectedItem() != null ) {
                        
                        int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea eliminar el registro seleccionado?", "Eliminar Registro", JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE);
                        
                        if ( respuesta == JOptionPane.YES_OPTION ) {
                            
                            try{
                            
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarCita(?)}");
                            
                            procedimiento.setInt(1, ((Cita)tblCitas.getSelectionModel().getSelectedItem()).getCodigoCita() );
                            
                            procedimiento.execute();
                            
                            listaCita.remove( tblCitas.getSelectionModel().getSelectedItem() );
                            
                            limpiarControles();
                                
                            }catch(Exception e){
                            
                                e.printStackTrace();
                                
                            }
                            
                        }else{
                        
                            limpiarControles();
                        
                        }
                        
                    }else{
                    
                        JOptionPane.showMessageDialog(null, "No ha seleccionado ningun registro", "Registro Vacío", JOptionPane.WARNING_MESSAGE);
                    
                    }

                    break;
                    
                case GUARDAR:
                    
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");

                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    
                    imgNuevo.setImage( new Image(PAQUETE_IMAGE+"Agregar.png") );
                    imgEliminar.setImage( new Image(PAQUETE_IMAGE+"Eliminar.png") );
                    
                    tblCitas.setDisable(false);

                    limpiarControles();
                    desactivarControles();

                    tipoDeOperacion = operaciones.NINGUNO;
                    
                    break;

            }
    
    }
    
    public void editar(){
    
        switch( tipoDeOperacion ){
        
            case NINGUNO:
                
                if (tblCitas.getSelectionModel().getSelectedItem() != null) {
                    
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");

                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    
                    imgEditar.setImage( new Image(PAQUETE_IMAGE+"IconoActualizar.png") );
                    imgReporte.setImage( new Image(PAQUETE_IMAGE+"IconoCancelar.png") );

                    activarControles();
                    
                    cmbDoctor.setDisable(true);
                    cmbPaciente.setDisable(true);

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
                
                imgEditar.setImage( new Image(PAQUETE_IMAGE+"Modificar.png") );
                imgReporte.setImage( new Image(PAQUETE_IMAGE+"Reporte.png") );
                
                limpiarControles();
                desactivarControles();
                
                cargarDatos();
                
                tipoDeOperacion = operaciones.NINGUNO;
                
                break;
        }
    
    }
    
    public void actualizar(){
    
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarCita(?,?,?,?,?)}");
            
            Cita registro = ((Cita)tblCitas.getSelectionModel().getSelectedItem());
            
            Time hora = registro.getHoraCita();
            
            registro.setFechaCita( fCita.getSelectedDate() );
            registro.setTratamiento( txtTratamiento.getText() );
            registro.setDescripCondActual( txtDescripCondActual.getText() );
            
            if ( jfxHoraCita.editorProperty().isNull().get() ) {
                
                procedimiento.setInt(1, registro.getCodigoCita() );
                procedimiento.setDate(2, new java.sql.Date(registro.getFechaCita().getTime())  );
                procedimiento.setTime(3, hora );
                procedimiento.setString(4, registro.getTratamiento());
                procedimiento.setString(5, registro.getDescripCondActual());
                
            }else{
                
                registro.setHoraCita( Time.valueOf(jfxHoraCita.getValue()) );
            
                procedimiento.setInt(1, registro.getCodigoCita() );
                procedimiento.setDate(2, new java.sql.Date(registro.getFechaCita().getTime())  );
                procedimiento.setTime(3, registro.getHoraCita() );
                procedimiento.setString(4, registro.getTratamiento());
                procedimiento.setString(5, registro.getDescripCondActual());
            
            }
            
            procedimiento.execute();
        
        }catch(Exception e){
        
            e.printStackTrace();
        
        }
    
    }
    
    public void reporte(){
    
        switch( tipoDeOperacion ){
        
            case NINGUNO:
                
                imprimirReporte();
                break;
                
            case ACTUALIZAR:
                
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                
                imgEditar.setImage( new Image(PAQUETE_IMAGE+"Modificar.png") );
                imgReporte.setImage( new Image(PAQUETE_IMAGE+"Reporte.png") );
                
                limpiarControles();
                desactivarControles();
                
                tipoDeOperacion = operaciones.NINGUNO;
                
                break;
        
        }
    
    }

    public void imprimirReporte(){
    
        Map parametros = new HashMap();
        parametros.put("codigoCita", null);
        parametros.put("FONDO", GenerarReporte.class.getResource("/org/gersonmatta/image/FondoReporte.png"));
        parametros.put("LOGO", GenerarReporte.class.getResource("/org/gersonmatta/image/IconoPestaña.png"));
        GenerarReporte.mostrarReporte("ReporteCitas.jasper", "Reporte de Citas", parametros);
        
    }
    
    public void activarControles(){
    
        fCita.setDisable(false);
        jfxHoraCita.setDisable(false);
        
        txtTratamiento.setEditable(true);
        txtDescripCondActual.setEditable(true);
        
        cmbPaciente.setDisable(false);
        cmbDoctor.setDisable(false);
    
    }
    
    public void desactivarControles(){
    
        fCita.setDisable(true);
        jfxHoraCita.setDisable(true);
        
        txtTratamiento.setEditable(false);
        txtDescripCondActual.setEditable(false);
        
        cmbPaciente.setDisable(true);
        cmbDoctor.setDisable(true);
    
    }
    
    public void limpiarControles(){
    
        fCita.selectedDateProperty().set(null);
        jfxHoraCita.getEditor().clear();
        
        txtCodigoCitas.clear();
        txtTratamiento.clear();
        txtDescripCondActual.clear();
        
        tblCitas.getSelectionModel().clearSelection();
        
        cmbPaciente.setValue(null);
        cmbDoctor.setValue(null);
    
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
