/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appinformes;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Carlos Torres Liñán
 */
public class AppInformes extends Application {

    //Inicializamos los elementos de FXML
    @FXML
    private Menu menuClientes;
    @FXML
    private Menu menuReservas;
    @FXML
    private MenuItem menuitemHabitaciones;
    @FXML
    private MenuItem menuitemSalon;
    @FXML
    private Menu menuAyuda;
    @FXML
    private ImageView imageViewPrincipal;

    public static Connection conexion = null;

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Cargamos la interfaz de AppHotel
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Resources/fxml/AppInformes.fxml"));
        Parent root = fxmlLoader.load();

        // La añadimos a la escena
        Scene scene = new Scene(root);

        // Establecemos la conexión con la BD
        conectaBD();

        // Añadimos la escena al stage y la hacemos visible
        primaryStage.setTitle("AppInformes");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    // METODO PARA ACCEDER AL LISTADO DE FACTURAS
    @FXML
    private void onClickListadoFacturas(ActionEvent event) throws IOException, Exception {
        //Generamos el informe
        generaInformeFacturas();
    }

    // METODO PARA ACCEDER A LAS VENTAS TOTALES
    @FXML
    private void onClickVentasTotales(ActionEvent event) throws IOException, Exception {
        // Cargamos la clase de habitaciones
        generaInformeVentasTotales();
    }

    // MÉTODO PARA ACCEDER A LAS FACTURAS POR CLIENTE
    @FXML
    private void onClickFacturasPorCliente(ActionEvent event) throws IOException {
        // Cargamos la clase de los clientes                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Resources/fxml/FacturasPorClienteFXML.fxml"));
        Parent facturasPorCliente = fxmlLoader.load();

        // Creamos la escena y lo añadimos
        Scene sceneFacturasPorCliente = new Scene(facturasPorCliente);

        // Creamos un nuevo stage para una nueva pestaña, en la que se mostraran los clientes
        Stage stageClientes = new Stage();
        stageClientes.setResizable(false);
        // Hacemos la ventana de tipo modal respecto a la aplicación
        stageClientes.initModality(Modality.APPLICATION_MODAL);

        // Añadimos la escena al stage y la hacemos visible
        stageClientes.setTitle("Facturas por cliente");
        stageClientes.setScene(sceneFacturasPorCliente);
        stageClientes.setResizable(false);
        stageClientes.show();
    }

    // MÉTODO PARA ACCEDER AL SUBINFORME DE LISTADO FACTURAS
    @FXML
    private void onClickSubinformeListadoFacturas(ActionEvent event) throws IOException {
        generaInformeFacturasSubconsultas();
    }

    // MÉTODO PARA ACCEDER A LA PESTAÑA DE AYUDA
    @FXML
    private void onClickAyuda() throws IOException {
        // Cargamos la clase de ayuda
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Resources/fxml/Ayuda.fxml"));
        Parent ayuda = fxmlLoader.load();

        // Creamos una escena y la añadimos a la primaryStage
        Scene sceneAyuda = new Scene(ayuda);

        // Creamos un stage para una nueva pestaña, en la que estará la ayuda para el usuario
        Stage stageAyuda = new Stage();
        stageAyuda.setResizable(false);
        // Hacemos la ventana de tipo modal respecto a la aplicación
        stageAyuda.initModality(Modality.APPLICATION_MODAL);

        // Añadimos la escena a la stage y la hacemos visible.
        stageAyuda.setTitle("Sobre nosotros: ");

        stageAyuda.setScene(sceneAyuda);
        stageAyuda.setResizable(false);
        stageAyuda.show();
    }

    public void stop() throws Exception {
        try {
            DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/Test;shutdown=true");
        } catch (Exception ex) {
            System.out.println("No se pudo cerrar la conexion a la BD");
        }
    }

    /*MÉTODO PARA CONECTARSE A LA BASE DE DATOS*/
    public void conectaBD() {
        //Establecemos conexión con la BD
        String baseDatos = "jdbc:hsqldb:hsql://localhost/Test";
        String usuario = "sa";
        String clave = "";
        try {
            Class.forName("org.hsqldb.jdbcDriver").newInstance();
            conexion = DriverManager.getConnection(baseDatos, usuario, clave);
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Fallo al cargar JDBC");
            System.exit(1);
        } catch (SQLException sqle) {
            System.err.println("No se pudo conectar a BD");
            System.exit(1);
        } catch (java.lang.InstantiationException sqlex) {
            System.err.println("Imposible Conectar");
            System.exit(1);
        } catch (Exception ex) {
            System.err.println("Imposible Conectar");
            System.exit(1);
        }
    }

    /*MÉTODO PARA GENERAR EL INFORME DE FACTURAS*/
    public void generaInformeFacturas() {
        try {
            //Cargamos el informe
            JasperReport jr = (JasperReport) JRLoader.loadObject(AppInformes.class.getResource("Facturas.jasper"));

            //Mostramos el informe
            JasperPrint jp = (JasperPrint) JasperFillManager.fillReport(jr, null, conexion);
            JasperViewer.viewReport(jp);
        } catch (JRException ex) {
            System.out.println("Error al recuperar el jasper");
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    /*MÉTODO PARA GENERAR EL INFORME DE VENTAS TOTALES*/
    public void generaInformeVentasTotales() {
        try {
            //Cargamos el informe
            JasperReport jr = (JasperReport) JRLoader.loadObject(AppInformes.class.getResource("VentasTotales.jasper"));

            //Mostramos el informe
            JasperPrint jp = (JasperPrint) JasperFillManager.fillReport(jr, null, conexion);
            JasperViewer.viewReport(jp);
        } catch (JRException ex) {
            System.out.println("Error al recuperar el jasper");
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    /*MÉTODO PARA GENERAR INFORMES DE FACTURAS MEDIANTE SUBCONSULTAS*/
    public void generaInformeFacturasSubconsultas() { 
        try {
            //Cargamos el informe y los subinformes
            JasperReport jr = (JasperReport) JRLoader.loadObject(AppInformes.class.getResource("FacturasSubinformes.jasper"));
            JasperReport jsr1 = (JasperReport) JRLoader.loadObject(AppInformes.class.getResource("FacturasSubInforme1.jasper"));
            JasperReport jsr2 = (JasperReport) JRLoader.loadObject(AppInformes.class.getResource("FacturasSubInforme2.jasper"));

            //Map de parámetros
            Map parametros = new HashMap();
            parametros.put("FacturasSubInforme1", jsr1);
            parametros.put("FacturasSubInforme2", jsr2);
                        
            //Mostramos el Informe
            JasperPrint jp = (JasperPrint) JasperFillManager.fillReport(jr, parametros, conexion);
            JasperViewer.viewReport(jp, false);
        } catch (JRException ex) {
            System.out.println("Error al recuperar el jasper");
            JOptionPane.showMessageDialog(null, ex);
        }
    }
}

