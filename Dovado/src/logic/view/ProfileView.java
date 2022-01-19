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
			BorderPane navbar = Navbar.getNavbar();
			Navbar.authenticatedSetup();
			
			HBox profileView = new HBox();
			
			curr=current;
			
			Scene scene = new Scene(root,Navbar.getWidth(),Navbar.getHeight());
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

		setEmail.setText(Navbar.getUser().getEmail());
		setUsr.setText(Navbar.getUser().getUsername());
		
		daoPr = DAOPreferences.getInstance();

		emailModifyBtn.getStyleClass().add("src-btn");
		usrModifyBtn.getStyleClass().add("src-btn");
		prefSearchBtn.getStyleClass().add("src-btn");
		
		ArrayList<String> preferences =(ArrayList<String>) Navbar.getUser().getPreferences();
	
		for(int i=0;i<preferences.size();i++) {
			Text prefListElement = new Text(preferences.get(i));
			
			prefListElement.getStyleClass().add("textEventName");
			
			prefList.getItems().add(prefListElement);
		}
		
	}

	public void addNewPref() {
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
		
		if(daoPr.preferenceIsInJSON(pref)) {
			HBox prefBox = new HBox();
			Text prefName = new Text (pref.toUpperCase());
			prefBox.getChildren().add(prefName);
			
			prefName.getStyleClass().add("textEventName");
			
			ArrayList<String> newPref =(ArrayList<String>) Navbar.getUser().getPreferences();
			newPref.add(pref.toUpperCase());
			
			Navbar.getUser().setPreferences((List<String>)newPref);
			DAOSuperUser.getInstance().updateUserPreferences(Navbar.getUser());
			prefList.getItems().add(prefBox);
			
			return;
		}
		
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
