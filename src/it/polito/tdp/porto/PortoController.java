package it.polito.tdp.porto;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Model;
import it.polito.tdp.porto.model.Paper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class PortoController
{
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Author> boxPrimo;

    @FXML
    private ComboBox<Author> boxSecondo;

    @FXML
    private TextArea txtResult;

    @FXML
    void handleCoautori(ActionEvent event) 
    {
    	if (boxPrimo.getSelectionModel().getSelectedItem() != null)
    	{
    		Author autore = boxPrimo.getSelectionModel().getSelectedItem();
    		List<Author> coautori = model.loadCoAuthors(autore);
    		txtResult.clear();
			txtResult.appendText("Coautori per autore selezionato:\n");
    		for (Author a : coautori)
    		{
    			txtResult.appendText(a.toString() + "\n");
    		}
    		
    		//load cmb2
    		List<Author> authors2 = model.loadAuthors2(autore);
    		boxSecondo.getItems().addAll(authors2);
    	}
    	else
    	{
    		txtResult.clear();
    		txtResult.appendText("Devi prima selezionare un autore.");
    	}
    }

    @FXML
    void handleSequenza(ActionEvent event) 
    {
    	Author primo = boxPrimo.getSelectionModel().getSelectedItem();
    	Author secondo = boxSecondo.getSelectionModel().getSelectedItem();
    	if (primo != null && secondo != null)
    	{
    		List<Paper> sequenza = model.trovaSequenza(primo, secondo);
    		if (sequenza.size() > 0)
    			txtResult.appendText(sequenza.toString());
    		else
    			txtResult.appendText("Nessuna sequenza trovata.");
    	}
    	else
    	{
    		txtResult.clear();
    		txtResult.appendText("Devi prima selezionare due autori.\n");
    	}
    }

    @FXML
    void initialize() {
        assert boxPrimo != null : "fx:id=\"boxPrimo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert boxSecondo != null : "fx:id=\"boxSecondo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Porto.fxml'.";

    }

	public void setModel(Model model) 
	{
		this.model = model;
		
		//load cmb
		List<Author> authors = model.loadAuthors();
		boxPrimo.getItems().addAll(authors);
	}
}
