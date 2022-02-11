package logic.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import logic.model.DAOPlace;
import logic.model.Log;
import logic.model.Place;
import logic.model.SpotPlaceBean;

public class SpotPlaceView extends SuperView implements Initializable{
	
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

	private static final String TITLE = "Dovado - Spot a place";
	private static final String FILEFXML = "spotPlace.fxml";
	
	private TextField placeNameText;
	private ChoiceBox<String> regionText;
	private TextField cityText;
	private TextField addressText;
	private TextField civicoText;
	private DAOPlace daoPl;
	private ArrayList<Place> placesFound;
	
	private WebEngine we;
	private double[] latLng = {0,0};
	
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
	
	public static void render(Stage current) {
		SuperView.render(current, TITLE, FILEFXML, true, true);
	}
	
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
		
		WebView wv = new WebView();
		we = wv.getEngine();
		we.load("http://localhost:8080/Dovado/MapView.html");
		we.setJavaScriptEnabled(true);
		
		region.getItems().addAll(REGIONSKEY);
		
		spotPlaceBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				if(!spotPlace()) {
					popupGen("Place not spotted:\nInsert all informations"); 
					
				} else {
					popupGen("Place spotted successfully"); 
				   
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


		we.setOnAlert(event -> Log.getInstance().getLogger().info(event.getData()) );
			
		we.executeScript("retrieveLatLng('"+spotCivico+"',\""+spotAddress+"\",\""+spotCity+"\",\""+spotRegion+"\")");
		
		//Non funzionando per ora si aggiungono coordinate presettate
		latLng[0] = 41.93231;
		latLng[1] = 12.5167;
		String latLngStr="41.93231;12.5167";
		
		
		Log.getInstance().getLogger().info("Lat long string: "+latLngStr);
		if (latLngStr.equals("undefined") || latLngStr.equals(";") || latLngStr.isEmpty())
			return false;
		try {
			String[] splitLatLng = latLngStr.split(";");
			Log.getInstance().getLogger().info(splitLatLng[0]+" "+splitLatLng[1]);
			if(daoPl.spotPlace(spotAddress, placeName, spotCity, spotRegion, spotCivico, null,latLng)<0) {
				popupGen("Error: place not spotted!"); 
			    
				
				return false;
			}
		} catch (Exception e) {
			popupGen("Error: place not spotted!"); 
		    
			Log.getInstance().getLogger().info("Due to an error in the database the place wasn't spotted.");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void setLatLng(String alert) {
		String[] latLn = alert.split(";");
		latLng[0] = Double.parseDouble(latLn[0]);
		latLng[1] = Double.parseDouble(latLn[1]);
		Log.getInstance().getLogger().info("latlong vale "+latLng[0]+" "+latLng[1]);
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

	
	
}
