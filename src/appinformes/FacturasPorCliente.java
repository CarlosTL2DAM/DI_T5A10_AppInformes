/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appinformes;

import static appinformes.AppInformes.conexion;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 * FXML Controller class
 *
 * @author Carlos Torres Liñán
 */
public class FacturasPorCliente implements Initializable {

    @FXML
    private TextField tfId;
    @FXML
    private Button btnInforme;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnInforme.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                generaInformeFacturasPorCliente(tfId);
            }
        });
    }

    public void generaInformeFacturasPorCliente(TextField tintro) {
        //En caso de que se haya introducido ningún ID
        if (!tintro.getText().isEmpty()) {
            try {
                //Cargamos el informe
                JasperReport jr = (JasperReport) JRLoader.loadObject(AppInformes.class.getResource("FacturasPorCliente.jasper"));
                //Map de parámetros
                Map parametros = new HashMap();
                int nproducto = Integer.valueOf(tintro.getText());
                parametros.put("IDAdress", nproducto);

                //Mostramos el informe
                JasperPrint jp = (JasperPrint) JasperFillManager.fillReport(jr, parametros, conexion);
                JasperViewer.viewReport(jp);
            } catch (JRException ex) {
                System.out.println("Error al recuperar el jasper");
                JOptionPane.showMessageDialog(null, ex);
            }
        } 
        //En caso de que no se haya introducido ningún ID en el textField
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Se debe rellenar el Campo con la ID del cliente.");
            alert.setHeaderText("Campo Faltante.");
            alert.showAndWait();
        }

    }

}
