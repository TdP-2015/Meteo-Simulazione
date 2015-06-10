package it.polito.tdp.meteo;

import it.polito.tdp.meteo.bean.Model;
import it.polito.tdp.meteo.bean.Optimizer;

import java.net.URL;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;


public class MeteoController {
	
	private Model model ;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<Month> boxMese;

    @FXML
    private Button btnCalcola;

    @FXML
    private Button btnUmidita;

    @FXML
    private TextArea txtResult;
    

    @FXML
    void doUmidita(ActionEvent event) {
    	
    	Month mese = boxMese.getValue() ;
    	
    	if(mese==null) {
    		txtResult.appendText("Errore: specificare il mese\n"); 
    		return ;
    	}
    	
    	Map<String, Double> umidita = model.getUmiditaMedia(mese) ;
    	
    	txtResult.appendText(String.format("Mese: %s\n",mese.getDisplayName(TextStyle.FULL, Locale.ITALIAN))) ;
    	for(String citta : umidita.keySet()) {
    		txtResult.appendText(String.format("%s: %.2f\n", citta, umidita.get(citta))); 
    	}

    }

    @FXML
    void doCalcola(ActionEvent event) {
    	
		Month mese = boxMese.getValue() ;
    	
    	if(mese==null) {
    		txtResult.appendText("Errore: specificare il mese\n"); 
    		return ;
    	}
    	
    	Optimizer opt = new Optimizer(model) ;
    	List<String> path = opt.findOptimalPath(mese) ;
    	
    	for(String c : path) {
    		txtResult.appendText(String.format("%s\n", c));
    	}

    }


    @FXML
    void initialize() {
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Meteo.fxml'.";
        assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'Meteo.fxml'.";
        assert btnUmidita != null : "fx:id=\"btnUmidita\" was not injected: check your FXML file 'Meteo.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Meteo.fxml'.";

        boxMese.getItems().clear() ;
        boxMese.getItems().addAll(Month.values()) ;

    }
    
    public void setModel(Model m) {
    	this.model = m ;
    }

}
