package logic.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.model.DAOPreferences;
import logic.model.DAOSuperUser;
import logic.model.Preferences;
import logic.model.User;

public class ProfileView implements Initializable{

	@FXML
	private HBox emailHbox;
	@FXML
	private HBox usrHbox;
	@FXML
	private Button emailModifyBtn;
	@FXML
	private Button usrModifyBtn;
	@FXML
	private Button prefSearchBtn;
	@FXML
	private Text setUsr;
	@FXML
	private Text setEmail;
	@FXML
	private TextField prefSearch;
	@FXML
	private ListView prefList;	
	
	private static DAOPreferences daoPr;
	private static Stage curr;
	
	public static void render(Stage current) {
		try {
			VBox root = new VBox();
			BorderPane navbar = NavbarExplorer.getNavbar();
			NavbarExplorer.authenticatedSetup();
			
			HBox profileView = new HBox();
			
			curr=current;
			
			Scene scene = new Scene(root,NavbarExplorer.getWidth(),NavbarExplorer.getHeight());
			scene.getStylesheets().add(Main.class.getResource("Dovado.css").toExternalForm());
			current.setTitle("Dovado - events");
			current.setScene(scene);
			profileView = FXMLLoader.load(Main.class.getResource("Profile.fxml"));
			VBox.setVgrow(profileView, Priority.SOMETIMES);
			
			root.getChildren().addAll(navbar,profileView);
			
			current.show();	
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setEmail.getStyleClass().add("textEventInfo");
		setUsr.getStyleClass().add("textEventInfo");

		setEmail.setText(NavbarExplorer.getUser().getEmail());
		setUsr.setText(NavbarExplorer.getUser().getUsername());
		
		daoPr = DAOPreferences.getInstance();

		emailModifyBtn.getStyleClass().add("src-btn");
		usrModifyBtn.getStyleClass().add("src-btn");
		prefSearchBtn.getStyleClass().add("src-btn");
		
		Preferences preferences = ((User)NavbarExplorer.getUser()).getPreferences();
		
		/*Mi faccio restituire un array di tutti i nomi delle preferenze.
		 * In seguito un array di tutte le preferenze che l'utente ha settato 
		 * (false o true che siano)*/
		String[] allPrefsName = preferences.getPreferencesName();
		boolean[] allPrefsSet = preferences.getSetPreferences();
		
		
		for(int i=0;i<allPrefsSet.length;i++) {
			//Se trovo una preferenza che è stata settata dall'utente mi fermo,
			//Costruisco un element di Testo e lo aggiungo alla View.
			if(allPrefsSet[i]==true) {
				
				Text prefListElement = new Text(allPrefsName[i]);
				
				prefListElement.getStyleClass().add("textEventName");
				
				prefList.getItems().add(prefListElement);
			}
		}
		
	}

	public void addNewPref() {
		
		//IN QUESTO CASO LA VIEW VA MODIFICATA IN MODO DA INDICARE 
		//CON DEI BOTTONI DA SETTARE ON o OFF L'AGGIUNTA O RIMOZIONE DI PREFERENZE.
		if(prefSearch.getText().isEmpty()) {
			final Stage dialog = new Stage();
	        dialog.initModality(Modality.NONE);
	        dialog.initOwner(curr);
	        VBox dialogVbox = new VBox(20);
	        dialogVbox.getChildren().add(new Text("Insert a preference!"));
	        Scene dialogScene = new Scene(dialogVbox, 300, 200);
	        dialog.setScene(dialogScene);
	        dialog.show();
			return;
		}
		
		String pref = prefSearch.getText();
		
//		if(daoPr.(pref)) {
//			HBox prefBox = new HBox();
//			Text prefName = new Text (pref.toUpperCase());
//			prefBox.getChildren().add(prefName);
//			
//			prefName.getStyleClass().add("textEventName");
//			
//			ArrayList<String> newPref =(ArrayList<String>) Navbar.getUser().getPreferences();
//			newPref.add(pref.toUpperCase());
//			
//			Navbar.getUser().setPreferences((List<String>)newPref);
//			DAOSuperUser.getInstance().updateUserPreferences(Navbar.getUser());
//			prefList.getItems().add(prefBox);
//			
//			return;
//		}
//		
		final Stage dialog = new Stage();
        dialog.initModality(Modality.NONE);
        dialog.initOwner(curr);
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Text("Preference not found!"));
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
	}
	
	public void modifyEmail() {
		
		TextField newEmail = new TextField();
		
	}

	public void modifyUsrname() {
		
	}
}
