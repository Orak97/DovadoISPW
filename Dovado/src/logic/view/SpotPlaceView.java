package logic.view;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

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
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.model.DAOPlace;
import logic.model.Log;
import logic.model.NormalActivity;
import logic.model.Partner;
import logic.model.Place;
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
	private ListView placesList;
	@FXML
	private Button spotPlaceBtn;
	@FXML
	private Button searchBtn;
	@FXML
	private TextField searchBar;
	
	@FXML
	private ChoiceBox region;

	private static TextField placeNameText;
	private static ChoiceBox regionText;
	private static TextField cityText;
	private static TextField addressText;
	private static TextField civicoText;
	private static ListView pList;
	private static VBox rt;
	private static Stage curr;
	private static DAOPlace daoPl;
	private static Place placeSelected;
	private static ArrayList<Place> placesFound;
	private static StackPane lastPlaceBoxSelected;
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
		searchBtn.getStyleClass().add("src-btn");
		
		daoPl = DAOPlace.getInstance();
		placeSelected = null;
		placesFound = new ArrayList<>();

		placeNameText = placeNameTF;
		rt=root;
		pList = placesList;
		regionText = region;
		addressText = address;
		civicoText = civico;
		cityText = city;
		
		region.getItems().addAll(REGIONSKEY);
		
		spotPlaceBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				if(!spotPlace()) {
					final Stage dialog = new Stage();
	                dialog.initModality(Modality.NONE);
	                dialog.initOwner(curr);
	                VBox dialogVbox = new VBox(20);
	                dialogVbox.getChildren().add(new Text("Activity not created:\nInsert all informations"));
	                Scene dialogScene = new Scene(dialogVbox, 300, 200);
	                dialog.setScene(dialogScene);
	                dialog.show();
				} else {
					final Stage dialog = new Stage();
	                dialog.initModality(Modality.NONE);
	                dialog.initOwner(curr);
	                VBox dialogVbox = new VBox(20);
	                dialogVbox.getChildren().add(new Text("Activity created successfully"));
	                Scene dialogScene = new Scene(dialogVbox, 300, 200);
	                dialog.setScene(dialogScene);
	                dialog.show();
				}
			}

			
		});
		searchBtn.setText("Search");
		searchBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				
				String[] placeAttr;
				placeAttr = searchBar.getText().split(",");
				
				if(placeAttr.length==1) {
					if( (placesFound =  (ArrayList<Place>) daoPl.findPlacesByCity(placeAttr[0]))==null){
						final Stage dialog = new Stage();
		                dialog.initModality(Modality.NONE);
		                dialog.initOwner(curr);
		                VBox dialogVbox = new VBox(20);
		                dialogVbox.getChildren().add(new Text("No place found in "+placeAttr[0]));
		                Scene dialogScene = new Scene(dialogVbox, 300, 200);
		                dialog.setScene(dialogScene);
		                dialog.show();
					}
					else if(placeAttr.length==3) {
						placesFound.add(daoPl.findPlace(placeAttr[1], placeAttr[0], placeAttr[2], null));
						if(placesFound.contains(null)){
							final Stage dialog = new Stage();
			                dialog.initModality(Modality.NONE);
			                dialog.initOwner(curr);
			                VBox dialogVbox = new VBox(20);
			                dialogVbox.getChildren().add(new Text("No place found in: "+placeAttr[0]+'\n'+"in region: "+placeAttr[2]+'\n'+"named: "+ placeAttr[1]));
			                Scene dialogScene = new Scene(dialogVbox, 300, 200);
			                dialog.setScene(dialogScene);
			                dialog.show();
						}
					}
					else {
						updatePlaces();
					}
				}
				/*if(placeAttr.length<3) {
					final Stage dialog = new Stage();
	                dialog.initModality(Modality.NONE);
	                dialog.initOwner(curr);
	                VBox dialogVbox = new VBox(20);
	                dialogVbox.getChildren().add(new Text("Place's attributest not written correctly"));
	                Scene dialogScene = new Scene(dialogVbox, 300, 200);
	                dialog.setScene(dialogScene);
	                dialog.show();
					return;
				}*/
				
			}

			
		});
	}

public void selectedPlace() {
		
		int lastPlaceSelected=-1;
		
		StackPane placeBox = null;
		try {
			placeBox = (StackPane) pList.getSelectionModel().getSelectedItem();
		} catch(ClassCastException ce) {
			Log.getInstance().getLogger().info(ce.getMessage());
			return;
		}

		if(lastPlaceBoxSelected == placeBox) return;
		
		if(lastPlaceBoxSelected!=null) placeDeselected(lastPlaceBoxSelected);
		
		int itemNumber = pList.getSelectionModel().getSelectedIndex();
		
		lastPlaceSelected = itemNumber;
		Log.getInstance().getLogger().info(String.valueOf(lastPlaceSelected));
		
		//La prossima volta che selezioner� un altro evento oltre questo si resetta il suo eventBox.
		lastPlaceBoxSelected = placeBox;
		Long pID = Long.parseLong(((Text)placeBox.getChildren().get(0)).getId());
		placeSelected = daoPl.findPlaceById(pID);
	
		ImageView placeImage = (ImageView) placeBox.getChildren().get(1);

		placeImage.setScaleX(1.25);
		placeImage.setScaleY(1.25);
		
		Log.getInstance().getLogger().info("Place id found: "+pID+" "+placeSelected.getName());
			
	}
	
	public void placeDeselected(StackPane lastPlaceBoxSelected2) {
		
		ImageView eventImage = (ImageView) lastPlaceBoxSelected2.getChildren().get(1);
		
		eventImage.setScaleX(1);
		eventImage.setScaleY(1);
	}
	
	private static boolean spotPlace() {
		
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
		if(daoPl.findPlaceInJSON(placeName, city, region)!=null) {
			final Stage dialog = new Stage();
            dialog.initModality(Modality.NONE);
            dialog.initOwner(curr);
            VBox dialogVbox = new VBox(20);
            dialogVbox.getChildren().add(new Text("Place already spotted!"));
            Scene dialogScene = new Scene(dialogVbox, 300, 200);
            dialog.setScene(dialogScene);
            dialog.show();
            
			return false;
		}
		
		Partner owner = null;
		if(Navbar.getUser() instanceof Partner)
			owner = (Partner)Navbar.getUser();
		
		//Se lo "spottatore" del posto è un partner
		//Si crea il nuovo posto già con un owner.		
		if(daoPl.addPlaceToJSON(address, placeName, city, region, civico, owner)==-1) {
		
			final Stage dialog = new Stage();
            dialog.initModality(Modality.NONE);
            dialog.initOwner(curr);
            VBox dialogVbox = new VBox(20);
            dialogVbox.getChildren().add(new Text("Error: place not spotted!"));
            Scene dialogScene = new Scene(dialogVbox, 300, 200);
            dialog.setScene(dialogScene);
            dialog.show();
			return false;
		}
		return true;
	}


// ----------------------- Metodo di supporto per createActivity() ------------------------------

	private static boolean prevCheck() {
		
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
	
	private static void updatePlaces() {
		pList.getItems().clear();
		
		for(int i=0;i<placesFound.size();i++) {
			//TODO guardare qui con Andre
			for(int j=0;j<placesFound.size();j++) {
					 	
					ImageView plImage = new ImageView();
					Text plName = new Text(placesFound.get(i).getName()+"\n");
					Log.getInstance().getLogger().info("\n\n"+placesFound.get(i).getName()+"\n\n");
					Text plInfo = new Text(placesFound.get(i).getCity()+
							"\n"+placesFound.get(i).getRegion()+
							"\n"+placesFound.get(i).getAddress()+
							"-"+placesFound.get(i).getCivico());
					
					plImage.setImage(new Image("https://source.unsplash.com/user/erondu/400x100"));
					plImage.getStyleClass().add("place-image");
					
					plInfo.setId("placeInfo");
					plInfo.getStyleClass().add("placeInfo");
			/*		eventInfo.setTextAlignment(TextAlignment.LEFT);
					eventInfo.setFont(Font.font("Monserrat-Black", FontWeight.EXTRA_LIGHT, 12));
					eventInfo.setFill(Paint.valueOf("#ffffff"));
					eventInfo.setStrokeWidth(0.3);
					eventInfo.setStroke(Paint.valueOf("#000000"));
			*/		
					plName.setId("placeName");
					plName.getStyleClass().add("placeName");
			/*		eventName.setFont(Font.font("Monserrat-Black", FontWeight.BLACK, 20));
					eventName.setFill(Paint.valueOf("#ffffff"));
					eventName.setStrokeWidth(0.3);
					eventName.setStroke(Paint.valueOf("#000000"));
				*/	
					VBox eventText = new VBox(plName,plInfo);
					eventText.setAlignment(Pos.CENTER);
					eventText.getStyleClass().add("eventTextVbox");
					//Preparo un box in cui contenere il nome dell'attivit� e altre sue
					//informazioni; uso uno StackPane per poter mettere scritte su immagini.
					StackPane eventBox = new StackPane();
					eventBox.getStyleClass().add("eventBox");
					
					
					Text placeId = new Text();
					
					Long pID = placesFound.get(i).getId();
					Log.getInstance().getLogger().info("ID POSTO: "+pID);
					placeId.setId(pID.toString());
					
					//Aggiungo allo stack pane l'id dell'evento, quello del posto, l'immagine
					//dell'evento ed infine il testo dell'evento.
					eventBox.getChildren().add(placeId);
					eventBox.getChildren().add(plImage);
					eventBox.getChildren().add(eventText);
					
					//Stabilisco l'allineamento ed in seguito lo aggiungo alla lista di eventi.
					eventBox.setAlignment(Pos.CENTER_LEFT);
					
					eventBox.setMinWidth(rt.getWidth()/2);
					eventBox.setMaxWidth(rt.getWidth()/2);
					pList.getItems().add(eventBox);
					}
				}
	}
	
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

}
