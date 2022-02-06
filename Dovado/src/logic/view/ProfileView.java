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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Popup;
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
		
		Preferences preferences = ((User)Navbar.getUser()).getPreferences();
		
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

	public void modifyEmail() {
		
		TextField newEmail = new TextField();
		
	}

	public void modifyUsrname() {
		
	}
	
	public Popup popupGen(double width, double height, String error) {
		Popup popup = new Popup(); 
		popup.centerOnScreen();
		
		Text errorTxt = new Text(error);
		errorTxt.getStyleClass().add("textEventInfo");
		errorTxt.setTextAlignment(TextAlignment.CENTER);
		errorTxt.setWrappingWidth(480);
	    
	    //Circle c = new Circle(0, 0, diameter, Color.valueOf("212121"));
	    Rectangle r = new Rectangle(width, height, Color.valueOf("212121"));
	    StackPane popupContent = new StackPane(r,errorTxt); 
	    
	    r.setStrokeType(StrokeType.OUTSIDE);
	    r.setStrokeWidth(0.3);
	    r.setStroke(Paint.valueOf("ffffff"));
	    
	    popup.getContent().add(popupContent);
	    return popup;
	}
}
