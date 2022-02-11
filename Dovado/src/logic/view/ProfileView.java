package logic.view;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.model.DAOActivity;
import logic.model.DAOPreferences;
import logic.model.Log;
import logic.model.Preferences;
import logic.model.User;

public class ProfileView extends SuperView implements Initializable{

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
	

	private DAOPreferences daoPr;
	
	public static void render(Stage current) {
		try {
			VBox root = new VBox();
			BorderPane navbar = Navbar.getNavbar();
			Navbar.authenticatedSetup();
			
			curr=current;
			
			Scene scene = new Scene(root,Navbar.getWidth(),Navbar.getHeight());
			scene.getStylesheets().add(Main.class.getResource("Dovado.css").toExternalForm());
			current.setTitle("Dovado - events");
			current.setScene(scene);
			HBox profileView = FXMLLoader.load(Main.class.getResource("Profile.fxml"));
			VBox.setVgrow(profileView, Priority.SOMETIMES);
			
			root.getChildren().addAll(navbar,profileView);
			
			current.show();	
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String styleTextInfo = "textEventName";
		setEmail.getStyleClass().add(styleTextInfo);
		setUsr.getStyleClass().add(styleTextInfo);
		currency.getStyleClass().add(styleTextInfo);
		
		if(Navbar.getUser() instanceof User) {
			currency.setText(((User)Navbar.getUser()).getBalance()+" $");
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
			VBox prefInitVBox;
			for(int i=0;i<prefHBox.getChildren().size();i++) {

				prefInitVBox = (VBox)prefHBox.getChildren().get(i);
				for(int j=0;j<prefInitVBox.getChildren().size();j++) {
				//Se trovo una preferenza che è stata settata dall'utente mi fermo,
				//e segno il checkBox in modo tale da indicare che .
					if(allPrefsSet[pset]) {
						((CheckBox)(prefInitVBox.getChildren().get(j))).setSelected(true);
					}
					pset++;
				}
			}
			((VBox)root.getChildren().get(1)).setAlignment(Pos.CENTER);
		} else {
			
			prefVBox.getChildren().clear();
			Text partActivities = new Text("Activities under management:");
			Text numPAct = new Text();
			Text usersTouched = new Text("Users reached:");
			Text numusers = new Text("133");
			Text coupGenerated = new Text("CouponsGenerated:");
			Text numCoup = new Text("30");
			
			usersTouched.getStyleClass().add(styleTextInfo);
			partActivities.getStyleClass().add(styleTextInfo);
			coupGenerated.getStyleClass().add(styleTextInfo);
			numPAct.getStyleClass().add("textEventName");
			numCoup.getStyleClass().add("textEventName");
			numusers.getStyleClass().add("textEventName");
			
			try {
				numPAct.setText(String.valueOf(DAOActivity.getInstance().getPartnerActivities(Navbar.getUser().getUserID()).size()));
			} catch (ClassNotFoundException e) {		
				e.printStackTrace();
			} catch (SQLException e) {
				Log.getInstance().getLogger().info("Errore dal DB; non trovo il numero di attivit� del partner");
				e.printStackTrace();
				numPAct.setText("0");
			}
			
			VBox infoPartner = new VBox();
			infoPartner.setPadding(new Insets(20));
			infoPartner.getChildren().addAll(partActivities,numPAct,usersTouched,numusers,coupGenerated,numCoup);
			prefVBox.getChildren().addAll(infoPartner);
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
		VBox prefUpdateVBox;
		for(int j=0;j<prefHBox.getChildren().size();j++) {
			prefUpdateVBox = (VBox) prefHBox.getChildren().get(j);
			int vboxPrefContained = prefUpdateVBox.getChildren().size();
			for(int i=0;i<vboxPrefContained;i++) {
				//SE IL CHECKBOX DELLA PREFERENZA E' SETTATO ALLORA QUESTA VERR� AGGIUNTA ALLA
				//LISTA DI PREFERENZE.
				userPrefSet[pset] = ((CheckBox)(prefUpdateVBox.getChildren().get(i))).isSelected();
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
		popupGen("New preferences correctly set.");
		
	}
	
}
