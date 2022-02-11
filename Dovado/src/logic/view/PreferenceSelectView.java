package logic.view;

import java.net.URL;
import java.util.ResourceBundle;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.model.Preferences;
import logic.model.User;

public class PreferenceSelectView extends SuperView implements Initializable{

	@FXML
	private Button continueBtn;

	@FXML
	private HBox prefHBox;
	
	private Preferences preferences;
	private boolean[] preferencesChosen;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		boolean[] initPref = {false, false, false, false, false, false, false, false, false, false, false, false, false, false};
		preferences = new Preferences(initPref);		
		preferencesChosen = preferences.getSetPreferences();
		continueBtn.getStyleClass().add("src-btn");
		
		
	}

	public void goHomeView() {

		int pset=0;
		VBox prefVBox;
		for(int j=0;j<prefHBox.getChildren().size();j++) {
			prefVBox = (VBox) prefHBox.getChildren().get(j);
			int vboxPrefContained = prefVBox.getChildren().size();
			for(int i=0;i<vboxPrefContained;i++) {
				//SE IL CHECKBOX DELLA PREFERENZA E' SETTATO ALLORA QUESTA VERR� AGGIUNTA ALLA
				//LISTA DI PREFERENZE.
				preferencesChosen[pset] = ((CheckBox)(prefVBox.getChildren().get(i))).isSelected();
				pset++;
			}
		}
		
		preferences = new Preferences(preferencesChosen);
		
		((User)Navbar.getUser()).setPreferences(preferences);
		
    	HomeView.render(curr);
		
	}

	public static void render(Stage current) {
		Stage primaryStage = current;

		curr=current;
		
		try {
			VBox root = new VBox();
			BorderPane navbar = Navbar.getNavbar();
			Scene scene = new Scene(root,Navbar.getWidth(),Navbar.getHeight());
			scene.getStylesheets().add(Main.class.getResource("Dovado.css").toExternalForm());
			primaryStage.setTitle("Dovado - Select preferences");
			primaryStage.setScene(scene);
			VBox home = FXMLLoader.load(Main.class.getResource("PreferenceSelect.fxml"));
			
			root.getChildren().addAll(navbar,home);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
