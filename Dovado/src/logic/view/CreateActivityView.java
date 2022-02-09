package logic.view;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Stage;
import logic.controller.ActivityType;
import logic.controller.CreateActivityController;
import logic.model.Cadence;
import logic.model.CreateActivityBean;
import logic.model.DAOPlace;
import logic.model.Log;
import logic.model.Partner;
import logic.model.Place;
import logic.model.Preferences;

public class CreateActivityView implements Initializable{

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
//	
//	@FXML
//	private DatePicker startDate;
//	
//	@FXML
//	private DatePicker endDate;

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
	private TextField activityDescriptionText;
	@FXML
	private ChoiceBox<String> tBox;
	@FXML
	private HBox prefHBox;
	
	private static final  String[] CADENCEKEY = {"Weekly","Monthly","Annually"};
	
	private static ListView<Object> pList;
	private static ChoiceBox<String> typeBox;
	private static ChoiceBox<String> cadBox;
	private static TextField clTime;
	private static TextField opTime;
	private static DatePicker sDate;
	private static DatePicker eDate;
	private static DatePicker sDate2;
	private static DatePicker eDate2;
	private static TextField actNameField;
	private static Stage curr;
	private static VBox rt;
	private DAOPlace daoPl;
	private static Place placeSelected;
	private ArrayList<Place> placesFound;
	private static StackPane lastPlaceBoxSelected;
	private static Text cadenceSelText;
	private static HBox pHBox;
	private static VBox periodicBox;
	private static VBox expiringBox;
	private static TextField actDescriptionText;
	private static long wPopup = 500;
	private static long hPopup = 50;
	private static final String BGCOLORKEY = "ffffff";
		
	public static void render(Stage current) {
		try {
			VBox root = new VBox();
			BorderPane navbar = Navbar.getNavbar();
			Navbar.authenticatedSetup();
			
			Scene scene = new Scene(root,Navbar.getWidth(),Navbar.getHeight());
			scene.getStylesheets().add(Main.class.getResource("Dovado.css").toExternalForm());
			current.setTitle("Dovado - events");
			current.setScene(scene);
			VBox createActivity = FXMLLoader.load(Main.class.getResource("createActivity.fxml"));
			VBox.setVgrow(createActivity, Priority.SOMETIMES);
			
			curr=current;
			
			root.getChildren().addAll(navbar,createActivity);
			
			current.show();	
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public synchronized void initialize(URL arg0, ResourceBundle arg1) {

		createActBtn.getStyleClass().add("src-btn");
		searchBtn.getStyleClass().add("src-btn");
		
		daoPl = DAOPlace.getInstance();
		placeSelected = null;
		placesFound = new ArrayList<>();
		
		actNameField=actNameTF;
		clTime=closingTime;
		opTime=openingTime;
		pList=placesList;
		rt=root;
		typeBox = tBox;
		actDescriptionText = activityDescriptionText;
		pHBox=prefHBox;

		expiringBox = new VBox();
		periodicBox = new VBox();
		cadBox = new ChoiceBox<String>(); 
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
		
		cadBox.getItems().addAll(CADENCEKEY[0],CADENCEKEY[1],CADENCEKEY[2]);
		typeBox.getItems().addAll("Continua","Periodica","Scadenza");
		
		typeBox.setOnAction(new EventHandler<ActionEvent>(){
			

			@Override
			public void handle(ActionEvent event) {
				
				updateDescription();
	
			}
			
		});
		
		createActBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				if(!createActivity()) {
					
					final Popup popup = popupGen(wPopup,hPopup,"Activity not created:\nInsert all informations"); 
					popup.centerOnScreen(); 
				    
				    popup.show(curr);
				    popup.setAutoHide(true);
					
				} else {
					
					final Popup popup = popupGen(wPopup,hPopup,"Activity created successfully"); 
					popup.centerOnScreen(); 
				    
				    popup.show(curr);
				    popup.setAutoHide(true);
					
				}
			}
		});
		searchBtn.setText("Search");
		searchBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				
				String searchTerm = searchBar.getText();
				try {
					placesFound  =  (ArrayList<Place>) daoPl.searchPlaces(searchTerm);
				
					if( (placesFound)==null){
						
							final Popup popup = popupGen(wPopup,hPopup,"No place found in "+searchTerm); 
							popup.centerOnScreen(); 
						    
						    popup.show(curr);
						    popup.setAutoHide(true);
							
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
	
	public void updateDescription() {
		String chosenType = typeBox.getValue();
		
		if(chosenType == null) {
			return;
		}
		
		if(ActivityType.valueOf(chosenType.toUpperCase())==ActivityType.CONTINUA) 
		{
			cadenceDescription.setText("Activities open every day of the week, eg: a walk in villa Borghese ");
			cadenceDescription.setWrappingWidth(280);

			if(elementsVBox.getChildren().contains(expiringBox))
				elementsVBox.getChildren().remove(expiringBox);

			if(elementsVBox.getChildren().contains(periodicBox))
				elementsVBox.getChildren().remove(periodicBox);
		}
		else if(ActivityType.valueOf(chosenType.toUpperCase())==ActivityType.PERIODICA) 
		{
			cadenceDescription.setText("Activities repeated with a certain frequency, for a period of time.");
			cadenceDescription.setWrappingWidth(280);
			
			if(elementsVBox.getChildren().contains(expiringBox))
				elementsVBox.getChildren().remove(expiringBox);
			
			if(!elementsVBox.getChildren().contains(periodicBox)) {
				elementsVBox.getChildren().add(3, periodicBox);
			}			
		}
		else if(ActivityType.valueOf(chosenType.toUpperCase())==ActivityType.SCADENZA) {
			cadenceDescription.setText("Activities that take place just once.");
			cadenceDescription.setWrappingWidth(280);

			if(elementsVBox.getChildren().contains(periodicBox))
				elementsVBox.getChildren().remove(periodicBox);
			
			if(!elementsVBox.getChildren().contains(expiringBox)) {
				elementsVBox.getChildren().add(3, expiringBox);
			}			
		}
		else {
			cadenceSelText.setText("Select a type and I'll describe it.");
		}
		
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
					
					plInfo.setId("placeInfo");
					plInfo.getStyleClass().add("placeInfo");
					plInfo.setStrokeWidth(1);
					plInfo.setStroke(Paint.valueOf("000000"));
			
					plName.setId("placeName");
					plName.getStyleClass().add("placeName");
					plName.setStrokeWidth(1);
					plName.setStroke(Paint.valueOf("000000"));
		
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
		int pID = Integer.valueOf(((Text)placeBox.getChildren().get(0)).getId());
		try {
			placeSelected = daoPl.getPlace(pID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
	
	public static boolean createActivity() {
		
		String activityName;
		String openingTime;
		String closingTime = null;
		LocalDate openingDate;
		LocalDate closingDate;
		
		if(!prevCheck()) {
			return false;
		}
		
		activityName = actNameField.getText();
		openingTime = (opTime.getText());
		closingTime = (clTime.getText());
		
		CreateActivityBean caBean = new CreateActivityBean();
		caBean.setActivityName(activityName);
		caBean.setType(ActivityType.valueOf(typeBox.getValue().toUpperCase()));

//-----------------------QUI SI FA DISTINZIONE TRA I VARI TIPI DI ATTIVIT�---------------------------------------------------//
//---------IN BASE A QUESTA DISTINZIONE AVREMO L'ATTIVIT� COSTRUITA SECONDO PROCEDIMENTI DIVERSI---------------------------------------------------//
				
		if(ActivityType.valueOf(typeBox.getValue().toUpperCase()).equals(ActivityType.CONTINUA)) 
		{
			caBean.setOpeningTime(openingTime);
			caBean.setClosingTime(closingTime);
		}
		
		else if(ActivityType.valueOf(typeBox.getValue().toUpperCase()).equals(ActivityType.SCADENZA)) {
			
			openingDate = sDate.getValue();
			String openingDateString = openingDate.toString();
			
			if(eDate.getValue().isBefore(openingDate) || eDate.getValue().isBefore(LocalDate.now()))
				return false;
			closingDate = eDate.getValue();
			
			String closingDateString = closingDate.toString();
			
			caBean.setOpeningTime(openingTime);
			caBean.setClosingTime(closingTime);
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
			caBean.setOpeningTime(openingTime);
			caBean.setClosingTime(closingTime);
			caBean.setOpeningDate(openingDateString);
			caBean.setEndDate(closingDateString);
			caBean.setCadence(Cadence.valueOf(cadBox.getValue().toUpperCase()));
		}
		
		else {
			final Popup popup = new Popup(); popup.centerOnScreen();
			 
		    Text popupText = new Text("Activity"+'\n'+"type not"+'\n'+"selected yet");
		    popupText.getStyleClass().add("textEventInfo");
		    popupText.setTextAlignment(TextAlignment.CENTER);;
		    
		    Circle c = new Circle(0, 0, 50, Color.valueOf("212121"));
		    
		    StackPane popupContent = new StackPane(c,popupText); 
		    
		    c.setStrokeType(StrokeType.OUTSIDE);
		    c.setStrokeWidth(0.3);
		    c.setStroke(Paint.valueOf(BGCOLORKEY));
		    
		    popup.getContent().add(popupContent);
		    
		    popup.show(curr);
		    popup.setAutoHide(true);
		    
		Log.getInstance().getLogger().info("Attivit� non aggiunta alla persistenza");
		}
		
		if(Navbar.getUser() instanceof Partner) {
			caBean.setOwner(Navbar.getUser().getUserID().intValue());
			//TODO::Prezzo per ora scelto fisso
			caBean.setPrice("200");	
		}
		
		//--------------------------------------------------------------------------------------------
				//DA AGGIUNGERE UNA LISTA DI BOTTONI DA CUI SCEGLIERE LA PREFERENZA DELL'ATTIVITA'
		//--------------------------------------------------------------------------------------------
		boolean[] boolPref = {false, false, false, false, false, false, false, false, false, false, false, false, false, false};
		Preferences newActivityPref = new Preferences(boolPref);
		
		//GRAZIE AL METODO SOTTOSTANTE MI PRENDO UN ARRAY DI BOOLEANI DA CUI POI 
		//POSSO MODIFICARE UNO AD UNO LE PREFERENZE.
		boolean[] activityPrefSet = newActivityPref.getSetPreferences();
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
		//FINITE TUTTE LE PREFERENZE SI SETTA LA LISTA DI BOOLEANI RAPPRESENTANTE LE PREFERENZE A
		//TRUE SONO STATE SELEZIONATE COME TRUE.
		caBean.setInterestedCategories(activityPrefSet);
		caBean.setActivityDescription(actDescriptionText.getText());
		caBean.setPlace(placeSelected.getId().intValue());
//		Log.getInstance().getLogger().info(act.toString());
//		act.setId(daoAc.addActivityToJSON(placeSelected,(SuperActivity)act,"no"));
//		int result = Long.compare(act.getId(),0L);
//		if(result>0) {
//			act.setPreferences((List<String>)prefsList);
//			return true;
//		} 
//		else if(result<0) {
//			final Stage dialog = new Stage();
//            dialog.initModality(Modality.NONE);
//            dialog.initOwner(curr);
//            VBox dialogVbox = new VBox(20);
//            dialogVbox.getChildren().add(new Text("Activity already created!"));
//            Scene dialogScene = new Scene(dialogVbox, 300, 200);
//            dialog.setScene(dialogScene);
//            dialog.show();
//			return false;
//		}
//		else if(result==0) {
//			final Stage dialog = new Stage();
//            dialog.initModality(Modality.NONE);
//            dialog.initOwner(curr);
//            VBox dialogVbox = new VBox(20);
//            dialogVbox.getChildren().add(new Text("Activity creation error!"));
//            Scene dialogScene = new Scene(dialogVbox, 300, 200);
//            dialog.setScene(dialogScene);
//            dialog.show();
//			return false;
//		}
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
	    r.setStroke(Paint.valueOf(BGCOLORKEY));
	    
	    popup.getContent().add(popupContent);
	    return popup;
	}
	
}