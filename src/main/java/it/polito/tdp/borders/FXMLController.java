
package it.polito.tdp.borders;

import java.net.URL;
import java.util.ResourceBundle;

import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtAnno;

    @FXML
    private ComboBox<Country> comboStati;

    @FXML
    private Button btnTrovaVicini;

    @FXML
    private TextArea txtResult;

    @FXML
    void doCalcolaConfini(ActionEvent event) {
    	txtResult.clear();
    	
    	int x ;
    	try {
    		x = Integer.parseInt(txtAnno.getText());
    	} catch (Throwable t){
    		txtResult.appendText("Errore nell'input!");
    		return;
    	}
    	
    	this.model.creaGrafo(x);
    	//txtResult.appendText("Grafo creato!\n");
    	//txtResult.appendText("Il grafo contiene " + model.vertexNumber() + " vertici e " + model.edgeNumber() + " archi\n");
    	
    	//txtResult.appendText("Vertici: " + model.getGrafo().vertexSet() + "\n");
    	//txtResult.appendText("Archi: " + model.getGrafo().edgeSet() + "\n");
    	
    	for(Country c : model.getGrafo().vertexSet()) {
    		txtResult.appendText(c.toString() + ": " + model.getGrafo().degreeOf(c) + "\n");
    	}
    	
    	ConnectivityInspector<Country, DefaultEdge> ispettore = new ConnectivityInspector<Country, DefaultEdge>(model.getGrafo());
    	txtResult.appendText("Componenti connesse presenti: " + ispettore.connectedSets().size() + "\n");
    	
    	comboStati.getItems().addAll(model.getGrafo().vertexSet());
    }


    @FXML
    void doTrovaVicini(ActionEvent event) {
    	
    	txtResult.appendText("I vicini dello stato selezionato sono:\n");
    	for(Country c : model.trovaVicini(comboStati.getValue())) {
    		txtResult.appendText(c.toString() + "\n");
    	}
    }

    @FXML
    void initialize() {
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert comboStati != null : "fx:id=\"comboStati\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnTrovaVicini != null : "fx:id=\"btnTrovaVicini\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}

