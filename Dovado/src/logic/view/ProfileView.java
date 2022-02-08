package logic.view;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
import javafx.stage.Popup;
import javafx.stage.Stage;
import logic.model.DAOPreferences;
import logic.model.Log;
import logic.model.Preferences;
import logic.model.User;

public class ProfileView implements Initializable{

	@FXML
	private HBox emailHbox;
	@FXML
	private HBox usrHbox;
	@FXML
	private HBox root;
	@FXML
	private Button preferencesSet;
	@FXML
	private Text setUsr;
	@FXML
	private Text currency;
	@FXML
	private Text setEmail;
	@FXML
	private HBox prefHBox;
	@FXML
	private VBox prefVBox;
	@FXML
	private HBox currencyHBox;
	
	
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
		currency.getStyleClass().add("textEventInfo");
		
		if(Navbar.getUser() instanceof User) {
			currency.setText(((User)Navbar.getUser()).getBalance()+" �");
		}
		setEmail.setText(Navbar.getUser().getEmail());
		setUsr.setText(Navbar.getUser().getUsername());
		
		daoPr = DAOPreferences.getInstance();

		preferencesSet.getStyleClass().add("src-btn");

		((VBox)root.getChildren().get(0)).setAlignment(Pos.CENTER);
		if(Navbar.getUser() instanceof User) {
			Preferences preferences = ((User)Navbar.getUser()).getPreferences();
			
			/*Mi faccio restituire un array di tutti i nomi delle preferenze.
			 * In seguito un array di tutte le preferenze che l'utente ha settato 
			 * (false o true che siano)*/
			boolean[] allPrefsSet = preferences.getSetPreferences();
			int pset=0;
			VBox prefVBox;
			for(int i=0;i<prefHBox.getChildren().size();i++) {

				prefVBox = (VBox)prefHBox.getChildren().get(i);
				for(int j=0;j<prefVBox.getChildren().size();j++) {
				//Se trovo una preferenza che è stata settata dall'utente mi fermo,
				//e segno il checkBox in modo tale da indicare che .
					if(allPrefsSet[pset]==true) {
						((CheckBox)(prefVBox.getChildren().get(j))).setSelected(true);
					}
					pset++;
				}
			}
			((VBox)root.getChildren().get(1)).setAlignment(Pos.CENTER);
		} else {
			root.getChildren().remove(prefVBox);
			((VBox)root.getChildren().get(0)).setAlignment(Pos.CENTER);
			((VBox)root.getChildren().get(0)).getChildren().remove(currencyHBox);
		}
	}
	
	public void updateUserPreferences() {
		boolean[] initPref = {false, false, false, false, false, false, false, false, false, false, false, false, false, false};
		Preferences newUserPref = new Preferences(initPref);
		
		//GRAZIE AL METODO SOTTOSTANTE MI PRENDO UN ARRAY DI BOOLEANI DA CUI POI 
		//POSSO MODIFICARE UNO AD UNO LE PREFERENZE.
		boolean[] userPrefSet = newUserPref.getSetPreferences();
		int pset=0;
		VBox prefVBox;
		for(int j=0;j<prefHBox.getChildren().size();j++) {
			prefVBox = (VBox) prefHBox.getChildren().get(j);
			int vboxPrefContained = prefVBox.getChildren().size();
			for(int i=0;i<vboxPrefContained;i++) {
				//SE IL CHECKBOX DELLA PREFERENZA E' SETTATO ALLORA QUESTA VERR� AGGIUNTA ALLA
				//LISTA DI PREFERENZE.
				userPrefSet[pset] = ((CheckBox)(prefVBox.getChildren().get(i))).isSelected();
				pset++;
			}
		}
		newUserPref.setInterestedCategories(userPrefSet);
		
		((User)Navbar.getUser()).setPreferences(newUserPref);
		try {
			daoPr.updateUserPreferences(Navbar.getUser().getUserID(), newUserPref.getSetPreferences());
		} catch (ClassNotFoundException | SQLException e) {
			Log.getInstance().getLogger().info("Error in DB update of user preferences.");
			e.printStackTrace();
		}
		final Popup popup = popupGen(500,50,"New preferences correctly set.");
		popup.centerOnScreen(); 
	    
	    popup.show(curr);
	    popup.setAutoHide(true);
		
	}
	
	public Popup popupGen(double width, double height, String error) {
		Popup popup = new Popup(); 
		popup.centerOnScreen();
		
		Text errorTxt = new Text(error);
		errorTxt.getStyleClass().add("textEventName");
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
