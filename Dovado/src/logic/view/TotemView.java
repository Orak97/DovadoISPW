package logic.view;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TotemView {
	
	@FXML
	Button buttonPartner;
	
	@FXML
	Button buttonExplorer;
	
	@FXML
	GridPane totemGrid;
	

	
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
	
	public static void render(Stage current) {
		Stage primaryStage = current;
		try {
			VBox root = new VBox();
			BorderPane navbar = Navbar.getNavbar();
			Scene scene = new Scene(root,Navbar.getWidth(),Navbar.getHeight());
			scene.getStylesheets().add(Main.class.getResource("Dovado.css").toExternalForm());
			primaryStage.setTitle("Dovado - totem");
			primaryStage.setScene(scene);
			GridPane totem = FXMLLoader.load(Main.class.getResource("Totem.fxml"));
			
			
			root.getChildren().addAll(navbar,totem);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		} 
	}
}
