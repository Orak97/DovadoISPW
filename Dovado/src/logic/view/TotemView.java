package logic.view;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class TotemView extends SuperView{
	
	@FXML
	private Button buttonPartner;
	
	@FXML
	private Button buttonExplorer;
	
	@FXML
	private GridPane totemGrid;
	
	private static final String TITLE = "Dovado - Totem";
	private static final String FILEFXML = "Totem.fxml";

	public static void render(Stage current) {
		SuperView.render(current, TITLE, FILEFXML, true, false);
	}
	
	@FXML
	public void goToRegExp(ActionEvent event) {
    	Stage current = (Stage)((Node)event.getSource()).getScene().getWindow();
    	RegisterView.render(current);
	}
	
	@FXML
	public void goToRegPartner(ActionEvent event) {
    	Stage current = (Stage)((Node)event.getSource()).getScene().getWindow();
    	RegPartnerView.render(current);
	}
	
	
}
