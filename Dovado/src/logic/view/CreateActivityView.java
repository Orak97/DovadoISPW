package logic.view;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import logic.controller.ActivityType;
import logic.controller.CreateActivityController;
import logic.controller.FindActivityController;
import logic.controller.SpotPlaceController;
import logic.controller.UpdateCertActController;
import logic.model.Activity;
import logic.model.Cadence;
import logic.model.CertifiedActivity;
import logic.model.CreateActivityBean;
import logic.model.DAOActivity;
import logic.model.DAOPlace;
import logic.model.Log;
import logic.model.Partner;
import logic.model.Place;
import logic.model.SpotPlaceBean;
import logic.model.SuperActivity;

public class CreateActivityView extends SuperView implements Initializable{

	@FXML
	private Button createActBtn;

	@FXML
	private ListView<Object> placesList;
	
	@FXML
	private TextField closingTime;

	@FXML
	private TextField openingTime;

	@FXML
	private TextField tagField;

	@FXML
	private VBox elementsVBox;
	
	@FXML
	private VBox root;

	@FXML
	private TextField actNameTF;

	@FXML
	private TextField searchBar;
	
	@FXML
	private Text cadenceDescription;

	@FXML
	private Button searchBtn;
	@FXML
	private Button searchActs;
	
	@FXML
	private Button spotPlace;
	
	@FXML
	private Button spotPlace1;

	@FXML
	private TextField activityDescriptionText;
	@FXML
	private ChoiceBox<String> tBox;
	@FXML
	private HBox prefHBox;
	@FXML
	private TextField addressTF;

	@FXML
	private TextField civicoTF;

	@FXML
	private TextField cityNameTF;

	@FXML
	private TextField placeNameTF;
	
	@FXML
	private VBox boxSpot;
	
	@FXML
	private Button spotActs;
	
	@FXML
	private TextField searchActivites;
	
	@FXML
	private ListView<Object> actsList;
	
	@FXML
	private Button reclaim;	
	
	@FXML
	private Button createNew;
	
	@FXML
	private ChoiceBox<String> regionBox;
	
	private static final  String[] CADENCEKEY = {"Weekly","Monthly","Annually"};
	private static final String TITLE = "Dovado - events";
	private static final String FILEFXML = "createActivity.fxml";
	
	private static ListView<Object> pList;
	private static ListView<Object> aList;
	private static ChoiceBox<String> typeBox;
	private static ChoiceBox<String> cadBox;
	private static TextField clTime;
	private static TextField opTime;
	private static DatePicker sDate;
	private static DatePicker eDate;
	private static DatePicker sDate2;
	private static DatePicker eDate2;
	private static TextField actNameField;
	private static VBox rt;
	private DAOPlace daoPl;
	private static Place placeSelected;
	private ArrayList<Place> placesFound;
	private ArrayList<Activity> actsFound;
	private static Activity actSelected;
	private StackPane lastPlaceBoxSelected;
	private StackPane lastActBoxSelected;
	private static Text cadenceSelText;
	private static HBox pHBox;
	private static VBox periodicBox;
	private static VBox expiringBox;
	private static TextField actDescriptionText;
	private static long wPopup = 500;
	private static long hPopup = 50;
	private static final String COLORSTROKE = "000000";
	private static final String PLINFOKEY = "placeInfo";
	private static final String PLNAMEKEY = "placeName";
	private static final String BGCOLORKEY = "ffffff";
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
		SuperView.render(current, TITLE, FILEFXML,true, true);	
		}
	
	@Override
	public synchronized void initialize(URL arg0, ResourceBundle arg1) {

		createActBtn.getStyleClass().add(BTNSRCKEY);
		searchBtn.getStyleClass().add(BTNSRCKEY);
		
		daoPl = DAOPlace.getInstance();
		placeSelected = null;
		actSelected = null;
		placesFound = new ArrayList<>();
		actsFound = new ArrayList<>();
		
		actNameField=actNameTF;
		clTime=closingTime;
		opTime=openingTime;
		pList=placesList;
		aList=actsList;
		rt=root;
		typeBox = tBox;
		actDescriptionText = activityDescriptionText;
		pHBox=prefHBox;

		expiringBox = new VBox();
		periodicBox = new VBox();
		cadBox = new ChoiceBox<>(); 
		eDate = new DatePicker();
		sDate = new DatePicker(); 
		eDate2 = new DatePicker();
		sDate2 = new DatePicker();
		Text sDateText = new Text("When does the activity begin?");
		Text eDateText= new Text("When does it end?");
		Text sDate2Text = new Text("When does the activity begin?");
		Text eDate2Text= new Text("When does it end?");
		Text cadenceText= new Text("How often does the event repeat?");
		cadenceSelText = new Text();

		sDateText.setFill(Paint.valueOf(BGCOLORKEY));		
		eDateText.setFill(Paint.valueOf(BGCOLORKEY));	
		sDate2Text.setFill(Paint.valueOf(BGCOLORKEY));		
		eDate2Text.setFill(Paint.valueOf(BGCOLORKEY));		
		cadenceText.setFill(Paint.valueOf(BGCOLORKEY));		
		cadenceSelText.setFill(Paint.valueOf(BGCOLORKEY));		
		
		cadenceText.setTextAlignment(TextAlignment.CENTER);
		sDateText.setTextAlignment(TextAlignment.CENTER);
		eDateText.setTextAlignment(TextAlignment.CENTER);
		
		expiringBox.setAlignment(Pos.CENTER);
		periodicBox.setAlignment(Pos.CENTER);
		
		periodicBox.getChildren().addAll(cadenceText,cadBox,sDate2Text,sDate2,eDate2Text,eDate2);
		expiringBox.getChildren().addAll(sDateText,sDate,eDateText,eDate);
		
		regionBox.getItems().addAll(REGIONSKEY);
		
		cadBox.getItems().addAll(CADENCEKEY[0],CADENCEKEY[1],CADENCEKEY[2]);
		typeBox.getItems().addAll("Continua","Periodica","Scadenza");
		
		typeBox.setOnAction(new EventHandler<ActionEvent>(){
			

			@Override
			public void handle(ActionEvent event) {
				
				updateDescription();
	
			}
			
		});
		boxSpot.setManaged(false);
		boxSpot.setVisible(false);
		
		spotPlace.setText("Spot your place");
		spotPlace.getStyleClass().add(BTNSRCKEY);
		spotPlace1.getStyleClass().add(BTNSRCKEY);
		spotPlace.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if(boxSpot.isManaged()) {
					boxSpot.setManaged(false);
					boxSpot.setVisible(false);
				} else {
					boxSpot.setManaged(true);
					boxSpot.setVisible(true);
				}
			}
			
		});
	
		createActBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				if(!createActivity()) {
					
					popupGen(wPopup,hPopup,"Activity not created:\nInsert all informations"); 
					
					
				} else {
					
					popupGen(wPopup,hPopup,"Activity created successfully"); 
					
					
				}
			}
		});
		if(Navbar.getUser() instanceof Partner) {
		
			initPartner();
		}
		else {
			elementsVBox.getChildren().get(0).setManaged(false);
			elementsVBox.getChildren().get(0).setVisible(false);
		}
		searchBtn.setText("Search");
		searchBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				
				String searchTerm = searchBar.getText();
				try {
					placesFound  =  (ArrayList<Place>) daoPl.searchPlaces(searchTerm);
				
					if( (placesFound)==null){
						
							popupGen(wPopup,hPopup,"No place found in "+searchTerm); 
							
							
						}
					} catch (Exception e1) {
						Log.getInstance().getLogger().info("Due to DB errors places were not fetched.");
						e1.printStackTrace();
						return;
				}

				updatePlaces();
					
			}
		});
	}
	
	private void initPartner() {
		for(int i=1;i<elementsVBox.getChildren().size();i++) {
			elementsVBox.getChildren().get(i).setManaged(false);
			elementsVBox.getChildren().get(i).setVisible(false);
		}
		createNew.getStyleClass().add("src-btn");
		searchActs.getStyleClass().add("src-btn");
		searchActs.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String[] searchTerm = searchActivites.getText().split(" ");

				try {
					actsFound  =  (ArrayList<Activity>) DAOActivity.getInstance().getNearbyActivities(42.19832, 12.34515,100);
					actsFound = (ArrayList<Activity>)FindActivityController.filterActivitiesByKeyWords((List<Activity>)actsFound, searchTerm);
					if( (actsFound.isEmpty())){
						
							popupGen(wPopup,hPopup,"No activity found for "+searchTerm); 
							
							
						}
					} catch (Exception e1) {
						Log.getInstance().getLogger().info("Due to DB errors places were not fetched.");
						e1.printStackTrace();
						return;
				}
				searchActivites.setText("");
				updateActs();
			}
		});
		createNew.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				VBox partBox = (VBox)elementsVBox.getChildren().get(0);
				partBox.setManaged(false);
				partBox.setVisible(false);
				
				for(int i=1;i<elementsVBox.getChildren().size();i++) {
					elementsVBox.getChildren().get(i).setManaged(true);
					elementsVBox.getChildren().get(i).setVisible(true);
				}
				
			}
			
		});

		reclaim.getStyleClass().add(BTNSRCKEY);
	}
	
	public void updateDescription() {
		String chosenType = typeBox.getValue();
		
		if(chosenType == null) {
			return;
		}
		
		if(ActivityType.valueOf(chosenType.toUpperCase())==ActivityType.CONTINUA) 
		{
			cadenceDescription.setText("Activities open every day of the week, eg: a walk in villa Borghese ");
			cleanBox();
		}
		else if(ActivityType.valueOf(chosenType.toUpperCase())==ActivityType.PERIODICA) 
		{
			cadenceDescription.setText("Activities repeated with a certain frequency, for a period of time.");
			cleanBox();
			elementsVBox.getChildren().add(5,periodicBox);
		}
		else if(ActivityType.valueOf(chosenType.toUpperCase())==ActivityType.SCADENZA) {
			cadenceDescription.setText("Activities that take place just once.");
			cleanBox();
			elementsVBox.getChildren().add(5,expiringBox);
		}
		else {
			cadenceSelText.setText("Select a type and I'll describe it.");
		}
		
	}
	
	private void cleanBox() {
		cadenceDescription.setWrappingWidth(280);

		if(elementsVBox.getChildren().contains(expiringBox))
			elementsVBox.getChildren().remove(expiringBox);
		
		if(elementsVBox.getChildren().contains(periodicBox))
			elementsVBox.getChildren().remove(periodicBox);
			
	}
	
	
	public void updatePlaces() {
		pList.getItems().clear();
		
		for(int j=0;j<placesFound.size();j++) {
					 	
					ImageView plImage = new ImageView();
					Text plName = new Text(placesFound.get(j).getName()+"\n");
					Log.getInstance().getLogger().info("\n\n"+placesFound.get(j).getName()+"\n\n");
					Text plInfo = new Text(placesFound.get(j).getCity()+
							"\n"+placesFound.get(j).getRegion()+
							"\n"+placesFound.get(j).getAddress()+
							"-"+placesFound.get(j).getCivico());

					plImage.setImage(new Image("https://source.unsplash.com/user/erondu/310x180"));
					plImage.getStyleClass().add("place-image");
					
					plInfo.setId(PLINFOKEY);
					plInfo.getStyleClass().add(PLINFOKEY);
					plInfo.setStrokeWidth(1);
					plInfo.setStroke(Paint.valueOf(COLORSTROKE));
			
					plName.setId(PLNAMEKEY);
					plName.getStyleClass().add(PLNAMEKEY);
					plName.setStrokeWidth(1);
					plName.setStroke(Paint.valueOf(COLORSTROKE));
		
					VBox eventText = new VBox(plName,plInfo);
					eventText.setAlignment(Pos.CENTER);
					eventText.getStyleClass().add("eventTextVbox");
					//Preparo un box in cui contenere il nome dell'attivit� e altre sue
					//informazioni; uso uno StackPane per poter mettere scritte su immagini.
					StackPane eventBox = new StackPane();
					eventBox.getStyleClass().add("eventBox");
					
					
					Text placeId = new Text();
					
					Long pID = placesFound.get(j).getId();
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
	
	public void spotPlaceConfirm() {

		if(placeNameTF.getText().isEmpty() || regionBox.getSelectionModel().getSelectedItem()==null || addressTF.getText().isEmpty() || civicoTF.getText().isEmpty() || cityNameTF.getText().isEmpty()) {
			popupGen(wPopup, hPopup, "Not enough info about the place have been inserted");
			return;
		}
		String placeName = placeNameTF.getText();
		String spotRegion = regionBox.getSelectionModel().getSelectedItem().toString();
		String spotAddress = addressTF.getText();
		String spotCivico = civicoTF.getText();
		String spotCity = cityNameTF.getText();
		
		Log.getInstance().getLogger().info(placeName+'\n'+spotRegion+'\n'+spotAddress+'\n'+spotCity+'\n'+spotCivico);
		
		SpotPlaceBean spBean = new SpotPlaceBean();
		spBean.setAddress(spotAddress);
		spBean.setCity(spotCity);
		spBean.setPlaceName(placeName);
		spBean.setRegion(spotRegion);
		spBean.setStreetNumber(spotCivico);
		SpotPlaceController spc = new SpotPlaceController(spBean);
		if(spc.spotPlace()) {
			popupGen(wPopup,hPopup,"Place spotted correctly, search it and select it to continue");

		} else {
			 popupGen(wPopup,hPopup,"Place not spotted");
		}
	}
	
	public synchronized void selectedPlace() {
		
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
		int pID = Integer.parseInt(((Text)placeBox.getChildren().get(0)).getId());
		try {
			placeSelected = daoPl.getPlace(pID);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
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
	
//--------------------------------------INIZIO PARTE DI RECLAIM ACTIVITY----------------------
	
	public void updateActs() {
		aList.getItems().clear();
		
		for(int j=0;j<actsFound.size();j++) {
					if(actsFound.get(j) instanceof CertifiedActivity) continue;
					ImageView acImage = new ImageView();
					Text acName = new Text(actsFound.get(j).getName()+"\n");
					Log.getInstance().getLogger().info("\n\n"+actsFound.get(j).getName()+"\n\n");
					Text acInfo = new Text(actsFound.get(j).getPlayabilityInfo()+
							"\n"+actsFound.get(j).getDescription());

					acImage.setImage(new Image("https://source.unsplash.com/user/erondu/310x180"));
					acImage.getStyleClass().add("place-image");
					
					acInfo.setId(PLINFOKEY);
					acInfo.getStyleClass().add(PLINFOKEY);
					acInfo.setStrokeWidth(1);
					acInfo.setStroke(Paint.valueOf(COLORSTROKE));
			
					acName.setId(PLNAMEKEY);
					acName.getStyleClass().add(PLNAMEKEY);
					acName.setStrokeWidth(1);
					acName.setStroke(Paint.valueOf(COLORSTROKE));
		
					VBox eventText = new VBox(acName,acInfo);
					eventText.setAlignment(Pos.CENTER);
					acName.setWrappingWidth(300);
					acInfo.setWrappingWidth(300);
					eventText.getStyleClass().add("eventTextVbox");
					//Preparo un box in cui contenere il nome dell'attivit� e altre sue
					//informazioni; uso uno StackPane per poter mettere scritte su immagini.
					StackPane eventBox = new StackPane();
					eventBox.getStyleClass().add("eventBox");
					
					
					Text actId = new Text();
					
					Long aID = actsFound.get(j).getId();
					Log.getInstance().getLogger().info("ID ATTIVITA': "+aID);
					actId.setId(aID.toString());
					
					//Aggiungo allo stack pane l'id dell'evento, quello del posto, l'immagine
					//dell'evento ed infine il testo dell'evento.
					eventBox.getChildren().add(actId);
					eventBox.getChildren().add(acImage);
					eventBox.getChildren().add(eventText);
					
					//Stabilisco l'allineamento ed in seguito lo aggiungo alla lista di eventi.
					eventBox.setAlignment(Pos.CENTER_LEFT);
					
					eventBox.setMinWidth(rt.getWidth()/2);
					eventBox.setMaxWidth(rt.getWidth()/2);
					aList.getItems().add(eventBox);
			}
		
	}
	
	
	public synchronized void selectedActivity() {
		
		int lastActSelected=-1;
		
		StackPane actBox = null;
		try {
			actBox = (StackPane) aList.getSelectionModel().getSelectedItem();
		} catch(ClassCastException ce) {
			Log.getInstance().getLogger().info(ce.getMessage());
			return;
		}

		if(lastActBoxSelected == actBox) return;
		
		if(lastActBoxSelected!=null) placeDeselected(lastActBoxSelected);
		
		int itemNumber = aList.getSelectionModel().getSelectedIndex();
		
		lastActSelected = itemNumber;
		Log.getInstance().getLogger().info(String.valueOf(lastActSelected));
		
		//La prossima volta che selezioner� un altro evento oltre questo si resetta il suo eventBox.
		lastActBoxSelected = actBox;
		long aID = Long.parseLong(((Text)actBox.getChildren().get(0)).getId());
		try {
			actSelected = DAOActivity.getInstance().getActivityById(aID);
			Log.getInstance().getLogger().info("ATTIVITA' SELEZIONATA: "+actSelected.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		ImageView actImage = (ImageView) actBox.getChildren().get(1);

		actImage.setScaleX(1.25);
		actImage.setScaleY(1.25);
		
		Log.getInstance().getLogger().info("Activity id found: "+aID+" "+actSelected.getName());
			
	}
	
	public void activityDeselected(StackPane lastActBoxSelected) {
		
		ImageView eventImage = (ImageView) lastActBoxSelected.getChildren().get(1);
		
		eventImage.setScaleX(1);
		eventImage.setScaleY(1);
	}
	
	public void reclaimActivity() {
		if(actSelected==null) popupGen(wPopup,hPopup,"Chose an activity first");
		CreateActivityBean cab = new CreateActivityBean();
		cab.fillBean(cab,(SuperActivity)actSelected,(Partner)Navbar.getUser());
		UpdateCertActController upCac = new UpdateCertActController(cab,(Partner)Navbar.getUser());
		try {
			upCac.claimActivity();
		} catch (ClassNotFoundException | SQLException e) {
			popupGen(wPopup,hPopup,"Due to issues your Activity wasn't claimed!");
			e.printStackTrace();
			return;
		}
		popupGen(wPopup,hPopup,"Your activity was succesfully claimed!");
		
	}
	
//------------------------- FINE PARTE INERENTE AL RECLAIM ----------------------
	
	public boolean createActivity() {
		
		String activityName;
		String cAOpeningTime;
		String cAClosingTime = null;
		LocalDate openingDate;
		LocalDate closingDate;
		
		if(!prevCheck()) {
			return false;
		}
		
		activityName = actNameField.getText();
		cAOpeningTime = (opTime.getText());
		cAClosingTime = (clTime.getText());
		
		CreateActivityBean caBean = new CreateActivityBean();
		caBean.setActivityName(activityName);
		caBean.setType(ActivityType.valueOf(typeBox.getValue().toUpperCase()));

//-----------------------QUI SI FA DISTINZIONE TRA I VARI TIPI DI ATTIVIT�---------------------------------------------------//
//---------IN BASE A QUESTA DISTINZIONE AVREMO L'ATTIVIT� COSTRUITA SECONDO PROCEDIMENTI DIVERSI---------------------------------------------------//
				
		if(ActivityType.valueOf(typeBox.getValue().toUpperCase()).equals(ActivityType.CONTINUA)) 
		{
			caBean.setOpeningTime(cAOpeningTime);
			caBean.setClosingTime(cAClosingTime);
		}
		
		else if(ActivityType.valueOf(typeBox.getValue().toUpperCase()).equals(ActivityType.SCADENZA)) {
			
			openingDate = sDate.getValue();
			String openingDateString = openingDate.toString();
			
			if(eDate.getValue().isBefore(openingDate) || eDate.getValue().isBefore(LocalDate.now()))
				return false;
			closingDate = eDate.getValue();
			
			String closingDateString = closingDate.toString();
			
			caBean.setOpeningTime(cAOpeningTime);
			caBean.setClosingTime(cAClosingTime);
			caBean.setOpeningDate(openingDateString);
			caBean.setEndDate(closingDateString);
			
		}
		
		else if(ActivityType.valueOf(typeBox.getValue().toUpperCase()).equals(ActivityType.PERIODICA)) {
			openingDate = sDate2.getValue();
			String openingDateString = openingDate.toString();
			
			if(eDate2.getValue().isBefore(openingDate) || eDate2.getValue().isBefore(LocalDate.now()))
				return false;
			closingDate = eDate2.getValue();
			
			String closingDateString = closingDate.toString();
			caBean.setOpeningTime(cAOpeningTime);
			caBean.setClosingTime(cAClosingTime);
			caBean.setOpeningDate(openingDateString);
			caBean.setEndDate(closingDateString);
			caBean.setCadence(Cadence.valueOf(cadBox.getValue().toUpperCase()));
		}
		
		else {
			popupGen(wPopup,hPopup,"Activity type not selected yet");
		    
		    
		    Log.getInstance().getLogger().info("Attivit� non aggiunta alla persistenza");
		}
		
		if(Navbar.getUser() instanceof Partner) {
			caBean.setOwner(Navbar.getUser().getUserID().intValue());
			caBean.setPrice("200");	
		}
		
		//--------------------------------------------------------------------------------------------
				//DA AGGIUNGERE UNA LISTA DI BOTTONI DA CUI SCEGLIERE LA PREFERENZA DELL'ATTIVITA'
		//--------------------------------------------------------------------------------------------
		//GRAZIE AL METODO SOTTOSTANTE MI PRENDO UN ARRAY DI BOOLEANI DA CUI POI 
		//POSSO MODIFICARE UNO AD UNO LE PREFERENZE.
		boolean[] activityPrefSet = suppCreateActivity();
		//FINITE TUTTE LE PREFERENZE SI SETTA LA LISTA DI BOOLEANI RAPPRESENTANTE LE PREFERENZE A
		//TRUE SONO STATE SELEZIONATE COME TRUE.
		caBean.setInterestedCategories(activityPrefSet);
		caBean.setActivityDescription(actDescriptionText.getText());
		caBean.setPlace(placeSelected.getId().intValue());

		CreateActivityController caCont = new CreateActivityController(caBean);
		try {
			caCont.createActivity();
			caCont.saveActivity();
		} catch (Exception e) {
			Log.getInstance().getLogger().info("Error in DB");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static boolean[] suppCreateActivity() {
		boolean[] activityPrefSet = {false, false, false, false, false, false, false, false, false, false, false, false, false, false};
		int pset=0;
		VBox prefVBox;
		for(int j=0;j<pHBox.getChildren().size();j++) {
			prefVBox = (VBox) pHBox.getChildren().get(j);
			int vboxPrefContained = prefVBox.getChildren().size();
			for(int i=0;i<vboxPrefContained;i++) {
				//SE IL CHECKBOX DELLA PREFERENZA E' SETTATO ALLORA QUESTA VERR� AGGIUNTA ALLA
				//LISTA DI PREFERENZE.
				activityPrefSet[pset] = ((CheckBox)(prefVBox.getChildren().get(i))).isSelected();

				pset++;
			}
		}
		return activityPrefSet;
	}

// ----------------------- Metodo di supporto per createActivity() ------------------------------

	
	private static boolean prevCheck() {
	
		if(placeSelected==null) {
			return false;
		}
		
		if(actNameField.getText().isEmpty())
			return false;
	
		if(opTime.getText().isEmpty() || !opTime.getText().contains(":") || opTime.getText().length()>5)
			return false;
		
		if(clTime.getText().isEmpty() || !clTime.getText().contains(":") || clTime.getText().length()>5)
			return false;
		if(ActivityType.valueOf(typeBox.getValue().toUpperCase()).equals(ActivityType.SCADENZA)) {
			return !sDate.getValue().isBefore(LocalDate.now());
		}
		if(ActivityType.valueOf(typeBox.getValue().toUpperCase()).equals(ActivityType.PERIODICA)) {
			return !sDate2.getValue().isBefore(LocalDate.now());
		}
		return true;
	}
	
	
}