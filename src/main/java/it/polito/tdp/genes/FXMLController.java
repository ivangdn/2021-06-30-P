package it.polito.tdp.genes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.genes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model ;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnStatistiche;

    @FXML
    private Button btnRicerca;

    @FXML
    private ComboBox<String> boxLocalizzazione;

    @FXML
    private TextArea txtResult;

    @FXML
    void doRicerca(ActionEvent event) {
    	txtResult.clear();
    	String loc = boxLocalizzazione.getValue();
    	if(loc==null) {
    		txtResult.setText("Selezionare una localizzazione");
    		return;
    	}
    	
    	List<String> cammino = model.ricercaCammino(loc);
    	if(cammino.isEmpty()) {
    		txtResult.appendText("Nessun cammino trovato");
    	} else {
    		txtResult.appendText("Cammino di lunghezza maggiore a partire da "+loc+"\n");
    		for(String s : cammino) {
    			txtResult.appendText(s+"\n");
    		}
    	}
    }

    @FXML
    void doStatistiche(ActionEvent event) {
    	txtResult.clear();
    	String loc = boxLocalizzazione.getValue();
    	if(loc==null) {
    		txtResult.setText("Selezionare una localizzazione");
    		return;
    	}
    	
    	List<String> vicini = model.getLocalizzazioniConnesse(loc);
    	if(vicini.isEmpty()) {
    		txtResult.appendText("Nessuna localizzazione connessa a "+loc);
    	} else {
    		txtResult.appendText("Adiacenti a: "+loc+"\n");
    		for(String s : vicini) {
    			txtResult.appendText(String.format("%s %d\n", s, model.getPeso(loc, s)));
    		}
    	}
    }

    @FXML
    void initialize() {
        assert btnStatistiche != null : "fx:id=\"btnStatistiche\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnRicerca != null : "fx:id=\"btnRicerca\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxLocalizzazione != null : "fx:id=\"boxLocalizzazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		txtResult.setText(model.creaGrafo());
		boxLocalizzazione.getItems().clear();
		boxLocalizzazione.getItems().addAll(model.getVertici());
	}
}
