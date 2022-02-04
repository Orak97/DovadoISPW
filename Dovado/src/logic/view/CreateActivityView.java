package logic.view;

import java.io.IOException;
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
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import logic.controller.ActivityType;
import logic.controller.CreateActivityController;
import logic.model.Cadence;
import logic.model.CreateActivityBean;
import logic.model.DAOActivity;
import logic.model.DAOPlace;
import logic.model.DAOPreferences;
import logic.model.DAOSuperUser;
import logic.model.Log;
import logic.model.NormalActivity;
import logic.model.Partner;
import logic.model.Place;
import logic.model.Preferences;
import logic.model.SuperActivity;

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
	
	@FXML
	private DatePicker startDate;
	
	@FXML
	private DatePicker endDate;

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
	private ChoiceBox<String> cadenceBox;
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
	private static TextField searchField;
	private static TextField actNameField;
	private static TextField tField;
	private static Stage curr;
	private static VBox rt;
	private static DAOActivity daoAc;
	private static DAOPlace daoPl;
	private static Place placeSelected;
	private static ArrayList<Place> placesFound;
	private static StackPane lastPlaceBoxSelected;
	private static Text cadenceDescText;
	private static HBox pHBox;

	private static String BGCOLORKEY = "ffffff";
		
	public static void render(Stage current) {
		try {
			VBox root = new VBox();
			BorderPane navbar = Navbar.getNavbar();
			Navbar.authenticatedSetup();
			
			VBox createActivity = new VBox();
			
			
			Scene scene = new Scene(root,Navbar.getWidth(),Navbar.getHeight());
			scene.getStylesheets().add(Main.class.getResource("Dovado.css").toExternalForm());
			current.setTitle("Dovado - events");
			current.setScene(scene);
			createActivity = FXMLLoader.load(Main.class.getResource("createActivity.fxml"));
			VBox.setVgrow(createActivity, Priority.SOMETIMES);
			
			root.getChildren().addAll(navbar,createActivity);
			
			current.show();	
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		createActBtn.getStyleClass().add("src-btn");
		searchBtn.getStyleClass().add("src-btn");
		
		daoAc = DAOActivity.getInstance();
		daoPl = DAOPlace.getInstance();
		placeSelected = null;
		placesFound = new ArrayList<>();
		
		actNameField=actNameTF;
		sDate=startDate;
		eDate=endDate;
		searchField=searchBar;
		clTime=closingTime;
		opTime=openingTime;
		cadBox=cadenceBox;
		pList=placesList;
		rt=root;
		typeBox = tBox;
		tField=tagField;
		cadenceDescText = cadenceDescription;
		prefHBox=pHBox;
		
		cadBox.getItems().addAll(CADENCEKEY[0],CADENCEKEY[1],CADENCEKEY[2]);
		
		createActBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				if(!createActivity()) {
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
					try {
						if( (placesFound =  (ArrayList<Place>) daoPl.searchPlaces(placeAttr[0]))==null){
							final Stage dialog = new Stage();
						    dialog.initModality(Modality.NONE);
						    dialog.initOwner(curr);
						    VBox dialogVbox = new VBox(20);
						    dialogVbox.getChildren().add(new Text("No place found in "+placeAttr[0]));
						    Scene dialogScene = new Scene(dialogVbox, 300, 200);
						    dialog.setScene(dialogScene);
						    dialog.show();
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
//					if( (placesFound =  (ArrayList<Place>) daoPl.findPlacesByNameOrCity(placeAttr[0],1))==null){
//						final Stage dialog = new Stage();
//		                dialog.initModality(Modality.NONE);
//		                dialog.initOwner(curr);
//		                VBox dialogVbox = new VBox(20);
//		                dialogVbox.getChildren().add(new Text("No place found named "+placeAttr[0]));
//		                Scene dialogScene = new Scene(dialogVbox, 300, 200);
//		                dialog.setScene(dialogScene);
//		                dialog.show();
//					}
				}
//				else if(placeAttr.length==3) {
//						placesFound.add(daoPl.findPlace(placeAttr[1], placeAttr[0], placeAttr[2], null));
//						if(placesFound.contains(null)){
//							final Stage dialog = new Stage();
//			                dialog.initModality(Modality.NONE);
//			                dialog.initOwner(curr);
//			                VBox dialogVbox = new VBox(20);
//			                dialogVbox.getChildren().add(new Text("No place found in: "+placeAttr[0]+'\n'+"in region: "+placeAttr[2]+'\n'+"named: "+ placeAttr[1]));
//			                Scene dialogScene = new Scene(dialogVbox, 300, 200);
//			                dialog.setScene(dialogScene);
//			                dialog.show();
//						}
//				}
				else {
					updatePlaces();
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
	
	public void updateDescription() {
		String chosenType = typeBox.getValue();
		
		if(ActivityType.valueOf(chosenType)==ActivityType.CONTINUA) 
		{
			cadenceDescText.setText("Activities open every day of the week, eg: a walk in villa Borghese ");
			cadenceDescText.setWrappingWidth(280);
		}
		else if(ActivityType.valueOf(chosenType)==ActivityType.PERIODICA) 
		{
			cadenceDescText.setText("Activities repeated with a certain frequency, for a period of time.");
			cadenceDescText.setWrappingWidth(280);
		}
		else if(ActivityType.valueOf(chosenType)==ActivityType.SCADENZA) {
			cadenceDescText.setText("Activities that take place just once.");
			cadenceDescText.setWrappingWidth(280);
		}
		else {
			cadenceDescText.setText("Select a type and I'll describe it.");
		}
		
	}
	
	public void updatePlaces() {
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
		caBean.setType(ActivityType.valueOf(typeBox.getValue()));

//-----------------------QUI SI FA DISTINZIONE TRA I VARI TIPI DI ATTIVIT�---------------------------------------------------//
//---------IN BASE A QUESTA DISTINZIONE AVREMO L'ATTIVIT� COSTRUITA SECONDO PROCEDIMENTI DIVERSI---------------------------------------------------//
				
		if(ActivityType.valueOf(typeBox.getValue()).equals(ActivityType.CONTINUA)) 
		{
			caBean.setOpeningTime(openingTime);
			caBean.setClosingTime(closingTime);
		}
		
		else if(ActivityType.valueOf(typeBox.getValue()).equals(ActivityType.SCADENZA)) {
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
		
		else if(ActivityType.valueOf(typeBox.getValue()).equals(ActivityType.PERIODICA)) {
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
			caBean.setCadence(Cadence.valueOf(cadBox.getValue()));
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
		
		//SE L'UTENTE CHE CREA L'ATTIVITA' E' UN PARTNER LA SUA ATTIVITA' SARA' CERTIFICATA DALLA 
		//NASCITA.
		
		if(Navbar.getUser() instanceof Partner) {
			caBean.setOwner(Navbar.getUser().getUserID().intValue());
		}
		
		
//		SuperActivity act = null;
//		if(cadBox.getValue().equals(CADENCEKEY[0]) && (sDate.getValue().toString().isEmpty() && eDate.getValue().toString().isEmpty())) {
//			 act = new NormalActivity(activityName,Navbar.getUser(),placeSelected,openingTime,closingTime);
//		} 
//		else if(cadBox.getValue().equals(CADENCEKEY[0]) && !(sDate.getValue().toString().isEmpty() && eDate.getValue().toString().isEmpty())) {
//			act = new NormalActivity(activityName,Navbar.getUser(),placeSelected,openingTime,closingTime,openingDate,closingDate);
//		}
//		else if(!cadBox.getValue().equals(CADENCEKEY[0]) && !(sDate.getValue().toString().isEmpty() && eDate.getValue().toString().isEmpty())) {
//			act = new NormalActivity(activityName,Navbar.getUser(),placeSelected,openingTime,closingTime,openingDate,closingDate);
//		}
//		else {
//			return false;
//		}
		

		//--------------------------------------------------------------------------------------------
				//DA AGGIUNGERE UNA LISTA DI BOTTONI DA CUI SCEGLIERE LA PREFERENZA DELL'ATTIVITA'
		//--------------------------------------------------------------------------------------------
		Preferences newActivityPref = new Preferences(false, false, false, false, false, false, false, false, false, false, false, false, false, false);
		
		//GRAZIE AL METODO SOTTOSTANTE MI PRENDO UN ARRAY DI BOOLEANI DA CUI POI 
		//POSSO MODIFICARE UNO AD UNO LE PREFERENZE.
		boolean[] activityPrefSet = newActivityPref.getSetPreferences();
		
		for(int j=0;j<pHBox.getChildren().size();j++) {
			VBox prefVBox = (VBox) pHBox.getChildren().get(j);
			int vboxPrefContained = prefVBox.getChildren().size();
			for(int i=0;i<vboxPrefContained;i++) {
				//SE IL CHECKBOX DELLA PREFERENZA E' SETTATO ALLORA QUESTA VERR� AGGIUNTA ALLA
				//LISTA DI PREFERENZE.
				activityPrefSet[i] = ((CheckBox)(prefVBox.getChildren().get(i))).isSelected();
			}
		}
		//FINITE TUTTE LE PREFERENZE SI SETTA LA LISTA DI BOOLEANI RAPPRESENTANTE LE PREFERENZE A
		//TRUE SONO STATE SELEZIONATE COME TRUE.
		caBean.setInterestedCategories(activityPrefSet);
		
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
			SuperActivity act = (SuperActivity)caCont.createActivity();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
				
		return !sDate.getValue().isBefore(LocalDate.now());
	}
}