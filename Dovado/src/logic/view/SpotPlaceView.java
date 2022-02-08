package logic.view;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import logic.model.DAOPlace;
import logic.model.Log;
import logic.model.NormalActivity;
import logic.model.Partner;
import logic.model.Place;
import logic.model.SpotPlaceBean;
import logic.model.SuperActivity;

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

	private static TextField placeNameText;
	private static ChoiceBox<String> regionText;
	private static TextField cityText;
	private static TextField addressText;
	private static TextField civicoText;
	private static VBox rt;
	private static Stage curr;
	private static DAOPlace daoPl;
	private static Place placeSelected;
	private static ArrayList<Place> placesFound;
	private static StackPane lastPlaceBoxSelected;
	private static long wPopup = 500;
	private static long hPopup = 50;
	private static WebEngine we;
	
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
		placeSelected = null;
		placesFound = new ArrayList<>();

		placeNameText = placeNameTF;
		rt=root;
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
					final Popup popup = popupGen(wPopup,hPopup,"Place not spotted:\nInsert all informations"); 
					popup.centerOnScreen(); 
				    
				    popup.show(curr);
				    popup.setAutoHide(true);
					
				} else {
					final Popup popup = popupGen(wPopup,hPopup,"Place spotted successfully"); 
					popup.centerOnScreen(); 
				    
				    popup.show(curr);
				    popup.setAutoHide(true);
				}
			}

			
		});
		
	}
	
	private boolean spotPlace() {
		
		String placeName;
		String region;
		String address;
		String civico;
		String city;
		
		if(!prevCheck()) {
			return false;
		}
		
		placeName = placeNameText.getText();
		region = regionText.getSelectionModel().getSelectedItem().toString();
		address = addressText.getText();
		civico = civicoText.getText();
		city = cityText.getText();
	
		System.out.println(placeName+'\n'+region+'\n'+address+'\n'+city+'\n'+civico);
		
		Place newPlc = null;
		
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
				if(placesFound.get(i).getAddress().equals(address)
				&& placesFound.get(i).getAddress().equals(city)
				&& placesFound.get(i).getAddress().equals(civico)
				&& placesFound.get(i).getAddress().equals(region));
				
					return false;
			}
            
		}
		
		SpotPlaceBean spBean = new SpotPlaceBean();
		spBean.setAddress(address);
		spBean.setCity(city);
		spBean.setPlaceName(placeName);
		spBean.setRegion(region);
		spBean.setStreetNumber(civico);

        
		we.executeScript("retrieveLatLng('"+civico+"',\""+address+"\",\""+city+"\",\""+region+"\",'null')");
		double[] coord = {0,0};
		coord[0] = (double) we.executeScript("getDesktopLatitude()");
		coord[1] = (double) we.executeScript("getDesktopLongitude()");
		
		try {
			if(daoPl.spotPlace(address, placeName, city, region, civico, city,coord)<0) {
				final Popup popup = popupGen(wPopup,hPopup,"Error: place not spotted!"); 
				popup.centerOnScreen(); 
			    
			    popup.show(curr);
			    popup.setAutoHide(true);
				
				return false;
			}
		} catch (Exception e) {
			final Popup popup = popupGen(wPopup,hPopup,"Error: place not spotted!"); 
			popup.centerOnScreen(); 
		    
		    popup.show(curr);
		    popup.setAutoHide(true);
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
		
		if(placeNameText.getText().isEmpty())
			return false;
		
		return true;
	}
	
//	private static void updatePlaces() {
//		pList.getItems().clear();
//		
//		for(int i=0;i<placesFound.size();i++) {
//			System.out.println(placesFound.get(i).getOwner());
//				if(placesFound.get(i).getOwner()==null) {
//					ImageView plImage = new ImageView();
//					Text plName = new Text(placesFound.get(i).getName()+"\n");
//					Log.getInstance().getLogger().info("\n\n"+placesFound.get(i).getName()+"\n\n");
//					Text plInfo = new Text(placesFound.get(i).getCity()+
//						"\n"+placesFound.get(i).getRegion()+
//						"\n"+placesFound.get(i).getAddress()+
//						"-"+placesFound.get(i).getCivico());
//						
//					plImage.setImage(new Image("https://source.unsplash.com/user/erondu/350x100"));
//					plImage.getStyleClass().add("place-image");
//						
//					plInfo.setId("placeInfo");
//					plInfo.getStyleClass().add("placeInfo");
//			/*		eventInfo.setTextAlignment(TextAlignment.LEFT);
//					eventInfo.setFont(Font.font("Monserrat-Black", FontWeight.EXTRA_LIGHT, 12));
//					eventInfo.setFill(Paint.valueOf("#ffffff"));
//					eventInfo.setStrokeWidth(0.3);
//					eventInfo.setStroke(Paint.valueOf("#000000"));
//				*/		
//					plName.setId("placeName");
//					plName.getStyleClass().add("placeName");
//			/*		eventName.setFont(Font.font("Monserrat-Black", FontWeight.BLACK, 20));
//					eventName.setFill(Paint.valueOf("#ffffff"));
//					eventName.setStrokeWidth(0.3);
//					eventName.setStroke(Paint.valueOf("#000000"));
//				*/	
//					VBox eventText = new VBox(plName,plInfo);
//					eventText.setAlignment(Pos.CENTER);
//					eventText.getStyleClass().add("eventTextVbox");
//					//Preparo un box in cui contenere il nome dell'attivit� e altre sue
//					//informazioni; uso uno StackPane per poter mettere scritte su immagini.
//					StackPane eventBox = new StackPane();
//					eventBox.getStyleClass().add("eventBox");
//						
//						
//					Text placeId = new Text();
//						
//					Long pID = placesFound.get(i).getId();
//					Log.getInstance().getLogger().info("ID POSTO: "+pID);
//					placeId.setId(pID.toString());
//						
//					//Aggiungo allo stack pane l'id dell'evento, quello del posto, l'immagine
//					//dell'evento ed infine il testo dell'evento.
//					eventBox.getChildren().add(placeId);
//					eventBox.getChildren().add(plImage);
//					eventBox.getChildren().add(eventText);
//						
//					//Stabilisco l'allineamento ed in seguito lo aggiungo alla lista di eventi.
//					eventBox.setAlignment(Pos.CENTER_LEFT);
//						
//					eventBox.setMinWidth(rt.getWidth()/2);
//					eventBox.setMaxWidth(rt.getWidth()/2);
//					pList.getItems().add(eventBox);
//				}
//			}
//			
//	}
	
	public static void render(Stage current) {
		try {
			VBox root = new VBox();
			BorderPane navbar = Navbar.getNavbar();
			Navbar.authenticatedSetup();
			
			VBox spotPlace = new VBox();
			
			curr=current;
			
			Scene scene = new Scene(root,Navbar.getWidth(),Navbar.getHeight());
			scene.getStylesheets().add(Main.class.getResource("Dovado.css").toExternalForm());
			current.setTitle("Dovado - Spot a place");
			current.setScene(scene);
			spotPlace = FXMLLoader.load(Main.class.getResource("spotPlace.fxml"));
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
