package logic.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebEngine;
import javafx.stage.Popup;
import javafx.stage.Stage;
import logic.model.DAOPlace;
import logic.model.Log;
import logic.model.Place;
import logic.model.SpotPlaceBean;

public class SpotPlaceView implements Initializable{
	
	@FXML
	private TextField placeNameTF;
	
	@FXML
	private VBox root;
	
	@FXML
	private TextField city;
	
	@FXML
	private TextField civico;
	
	@FXML
	private TextField address;

	@FXML
	private Button spotPlaceBtn;
	
	@FXML
	private ChoiceBox<String> region;

	private TextField placeNameText;
	private ChoiceBox<String> regionText;
	private TextField cityText;
	private TextField addressText;
	private TextField civicoText;
	private static Stage curr;
	private DAOPlace daoPl;
	private ArrayList<Place> placesFound;
	private long wPopup = 500;
	private long hPopup = 50;
	private WebEngine we;
	
	private static final  String[] REGIONSKEY = {
			"Abruzzo"
			,"Basilicata"
			,"Calabria"
			,"Campania"
			,"Emilia-Romagna"
			,"Friuli Venezia Giulia"
			,"Lazio"
			,"Liguria"
			,"Lombardia"
			,"Marche"
			,"Molise"
			,"Piemonte"
			,"Puglia"
			,"Sardegna"
			,"Sicilia"
			,"Toscana"
			,"Trentino-Alto Adige"
			,"Umbria"
			,"Valle d'Aosta"
			,"Veneto"
	};
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		spotPlaceBtn.getStyleClass().add("src-btn");
		
		daoPl = DAOPlace.getInstance();
		placesFound = new ArrayList<>();

		placeNameText = placeNameTF;
		regionText = region;
		addressText = address;
		civicoText = civico;
		cityText = city;
		
		we = new WebEngine();
		we.load("http://localhost:8080/Dovado/MapView.html");
		we.setJavaScriptEnabled(true);
		
		region.getItems().addAll(REGIONSKEY);
		
		spotPlaceBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				if(!spotPlace()) {
					popupGen(wPopup,hPopup,"Place not spotted:\nInsert all informations"); 
					
				} else {
					popupGen(wPopup,hPopup,"Place spotted successfully"); 
				   
				}
			}

			
		});
		
	}
	
	private boolean spotPlace() {
		
		String placeName;
		String spotRegion;
		String spotAddress;
		String spotCivico;
		String spotCity;
		
		if(!prevCheck()) {
			return false;
		}
		
		placeName = placeNameText.getText();
		spotRegion = regionText.getSelectionModel().getSelectedItem().toString();
		spotAddress = addressText.getText();
		spotCivico = civicoText.getText();
		spotCity = cityText.getText();
	
		Log.getInstance().getLogger().info(placeName+'\n'+spotRegion+'\n'+spotAddress+'\n'+spotCity+'\n'+spotCivico);
				
		//Forse va cambiato e aggiunto anche il civico per
		//distinguere con precisione posti nuovi da alcuni
		//probabilmente già presenti.
		try {
			placesFound = (ArrayList<Place>) daoPl.searchPlaces(placeName);
		} catch (Exception e) {
			Log.getInstance().getLogger().info("Due to database error info about places was not fetched");
			e.printStackTrace();
			return false;
		}
		
		
		if(placesFound!=null) {
			
			for(int i=0;i<placesFound.size();i++) {
			//Se un posto si trova nella stessa via
			//e indirizzo del nostro nuovo posto sar� improbabile
			//che questo sia un nuovo posto.
				if(placesFound.get(i).getAddress().equals(spotAddress)
				&& placesFound.get(i).getAddress().equals(spotCity)
				&& placesFound.get(i).getAddress().equals(spotCivico)
				&& placesFound.get(i).getAddress().equals(spotRegion))
				
					return false;
			}
            
		}
		
		SpotPlaceBean spBean = new SpotPlaceBean();
		spBean.setAddress(spotAddress);
		spBean.setCity(spotCity);
		spBean.setPlaceName(placeName);
		spBean.setRegion(spotRegion);
		spBean.setStreetNumber(spotCivico);

        
		we.executeScript("retrieveLatLng('"+spotCivico+"',\""+spotAddress+"\",\""+spotCity+"\",\""+spotRegion+"\",'null')");
		double[] coord = {0,0};
		coord[0] = (double) we.executeScript("getDesktopLatitude()");
		coord[1] = (double) we.executeScript("getDesktopLongitude()");
		
		try {
			if(daoPl.spotPlace(spotAddress, placeName, spotCity, spotRegion, spotCivico, spotCity,coord)<0) {
				popupGen(wPopup,hPopup,"Error: place not spotted!"); 
			    
				
				return false;
			}
		} catch (Exception e) {
			popupGen(wPopup,hPopup,"Error: place not spotted!"); 
		    
			Log.getInstance().getLogger().info("Due to an error in the database the place wasn't spotted.");
			e.printStackTrace();
			return false;
		}
		return true;
	}


// ----------------------- Metodo di supporto per createActivity() ------------------------------

	private boolean prevCheck() {
		
		if(cityText.getText().isEmpty())
			return false;
	
		if(addressText.getText().isEmpty())
			return false;
		
		if(civicoText.getText().isEmpty())
			return false;
		
		if(regionText.getSelectionModel().getSelectedItem().toString().isEmpty())
			return false;
		
		return !placeNameText.getText().isEmpty();
	}

	
	public static void render(Stage current) {
		try {
			VBox root = new VBox();
			BorderPane navbar = Navbar.getNavbar();
			Navbar.authenticatedSetup();
			
			curr=current;
			
			Scene scene = new Scene(root,Navbar.getWidth(),Navbar.getHeight());
			scene.getStylesheets().add(Main.class.getResource("Dovado.css").toExternalForm());
			current.setTitle("Dovado - Spot a place");
			current.setScene(scene);
			VBox spotPlace = FXMLLoader.load(Main.class.getResource("spotPlace.fxml"));
			VBox.setVgrow(spotPlace, Priority.SOMETIMES);
			
			root.getChildren().addAll(navbar,spotPlace);
			
			current.show();	
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public Popup popupGen(double width, double height, String error) {
		Popup popup = new Popup(); 
		popup.centerOnScreen();
		
		Text errorTxt = new Text(error);
    	errorTxt.setWrappingWidth(width - 10);

		errorTxt.getStyleClass().add("textEventName");
		errorTxt.setTextAlignment(TextAlignment.CENTER);
		errorTxt.setWrappingWidth(480);
	    
	    Rectangle r = new Rectangle(width, height, Color.valueOf("212121"));
	    StackPane popupContent = new StackPane(r,errorTxt); 
	    
	    r.setStrokeType(StrokeType.OUTSIDE);
	    r.setStrokeWidth(0.3);
	    r.setStroke(Paint.valueOf("ffffff"));
	    
	    popup.getContent().add(popupContent);
	    popup.centerOnScreen(); 
	    
	    popup.show(curr);
	    popup.setAutoHide(true);
	    return popup;
	}
}
