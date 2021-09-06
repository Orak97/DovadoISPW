package logic.view;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.model.Cadence;
import logic.model.DAOActivity;
import logic.model.DAOPlace;
import logic.model.DAOPreferences;
import logic.model.DAOSuperUser;
import logic.model.Log;
import logic.model.NormalActivity;
import logic.model.Place;
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
	private Button searchBtn;

	@FXML
	private ChoiceBox<String> cadenceBox;

	private static ListView<Object> pList;
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
	
	private static int lastPlaceSelected=-1;
	
	public static void render(Stage current) {
		try {
			VBox root = new VBox();
			BorderPane navbar = Navbar.getNavbar();
			Navbar.authenticatedSetup();
			
			VBox createActivity = new VBox();
			
			try {
				Scene scene = new Scene(root,Navbar.getWidth(),Navbar.getHeight());
				scene.getStylesheets().add(Main.class.getResource("Dovado.css").toExternalForm());
				current.setTitle("Dovado - events");
				current.setScene(scene);
				createActivity = FXMLLoader.load(Main.class.getResource("createActivity.fxml"));
				VBox.setVgrow(createActivity, Priority.SOMETIMES);
			} catch(IOException e) {
				e.printStackTrace();
			}
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
		placesFound = new ArrayList<Place>();
		
		actNameField=actNameTF;
		sDate=startDate;
		eDate=endDate;
		searchField=searchBar;
		clTime=closingTime;
		opTime=openingTime;
		cadBox=cadenceBox;
		pList=placesList;
		rt=root;
		tField=tagField;
		
		cadBox.getItems().addAll("Non-stop","Weekly","Monthly","Annually");
		
		createActBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				if(createActivity()==false) {
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
						return;
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
							return;
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
	
	public void updatePlaces() {
			pList.getItems().clear();
		
		for(int i=0;i<placesFound.size();i++) {
			
			for(i=0;i<placesFound.size();i++) {
					Place pl = placesFound.get(i);
					 	
					ImageView plImage = new ImageView();
					Text plName = new Text(placesFound.get(i).getName()+"\n");
					Log.getInstance().logger.info("\n\n"+placesFound.get(i).getName()+"\n\n");
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
					Log.getInstance().logger.info("ID POSTO: "+pID);
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
		
		StackPane placeBox = null;
		try {
			placeBox = (StackPane) pList.getSelectionModel().getSelectedItem();
		} catch(ClassCastException ce) {
			Log.getInstance().logger.info(ce.getMessage());
			return;
		}

		if(lastPlaceBoxSelected == placeBox) return;
		
		if(lastPlaceBoxSelected!=null) placeDeselected(lastPlaceBoxSelected);
		
		int itemNumber = pList.getSelectionModel().getSelectedIndex();
		
		lastPlaceSelected = itemNumber;
		Log.getInstance().logger.info(String.valueOf(lastPlaceSelected));
		
		//La prossima volta che selezioner� un altro evento oltre questo si resetta il suo eventBox.
		lastPlaceBoxSelected = placeBox;
		Long pID = Long.parseLong(((Text)placeBox.getChildren().get(0)).getId());
		placeSelected = daoPl.findPlaceById(pID);
	
		ImageView placeImage = (ImageView) placeBox.getChildren().get(1);

		placeImage.setScaleX(1.25);
		placeImage.setScaleY(1.25);
		
		Log.getInstance().logger.info("Place id found: "+pID+" "+placeSelected.getName());
			
	}
	
	public void placeDeselected(StackPane lastPlaceBoxSelected2) {
		
		ImageView eventImage = (ImageView) lastPlaceBoxSelected2.getChildren().get(1);
		
		eventImage.setScaleX(1);
		eventImage.setScaleY(1);
	}
	
	public static boolean createActivity() {
		
		String activityName;
		LocalTime openingTime;
		LocalTime closingTime = null;
		LocalDate openingDate;
		LocalDate closingDate;
		Cadence cadence;
		
		if(placeSelected==null) {
			return false;
		}
		
		if(actNameField.getText().isEmpty())
			return false;
		activityName = actNameField.getText();
		
		if(opTime.getText().isEmpty() || !opTime.getText().contains(":") || opTime.getText().length()>5)
			return false;
		openingTime = LocalTime.parse(opTime.getText());
			
		if(clTime.getText().isEmpty() || !clTime.getText().contains(":") || clTime.getText().length()>5)
			return false;
		closingTime = LocalTime.parse(clTime.getText());
			
		if(sDate.getValue().isBefore(LocalDate.now()))
			return false;
		openingDate = sDate.getValue();
			
		if(eDate.getValue().isBefore(openingDate) || eDate.getValue().isBefore(LocalDate.now()))
			return false;
		closingDate = eDate.getValue();
		
		SuperActivity act = null;
		if(cadBox.getValue().equals("Non-stop") && (sDate.getValue().toString().isEmpty() && eDate.getValue().toString().isEmpty())) {
			 act = new NormalActivity(activityName,Navbar.getUser(),placeSelected,openingTime,closingTime);
		} 
		else if(cadBox.getValue().equals("Non-stop") && !(sDate.getValue().toString().isEmpty() && eDate.getValue().toString().isEmpty())) {
			act = new NormalActivity(activityName,Navbar.getUser(),placeSelected,openingTime,closingTime,openingDate,closingDate);
		}
		else if(!cadBox.getValue().equals("Non-stop") && !(sDate.getValue().toString().isEmpty() && eDate.getValue().toString().isEmpty())) {
			act = new NormalActivity(activityName,Navbar.getUser(),placeSelected,openingTime,closingTime,openingDate,closingDate);
		}
		else {
			return false;
		}
		String[] prefs = tField.getText().toString().split(",");
		ArrayList<String> prefsList = new ArrayList<String>();
		
		for(int i=0;i<prefs.length;i++) {
			prefsList.add(prefs[i]);
		}
		
		Log.getInstance().logger.info(act.toString());
		act.setId(daoAc.addActivityToJSON(placeSelected,(SuperActivity)act,"no"));
		int result = Long.compare(act.getId(),0L);
		if(result>0) {
			act.setPreferences((List<String>)prefsList);
			return true;
		} 
		else if(result<0) {
			final Stage dialog = new Stage();
            dialog.initModality(Modality.NONE);
            dialog.initOwner(curr);
            VBox dialogVbox = new VBox(20);
            dialogVbox.getChildren().add(new Text("Activity already created!"));
            Scene dialogScene = new Scene(dialogVbox, 300, 200);
            dialog.setScene(dialogScene);
            dialog.show();
			return false;
		}
		else if(result==0) {
			final Stage dialog = new Stage();
            dialog.initModality(Modality.NONE);
            dialog.initOwner(curr);
            VBox dialogVbox = new VBox(20);
            dialogVbox.getChildren().add(new Text("Activity creation error!"));
            Scene dialogScene = new Scene(dialogVbox, 300, 200);
            dialog.setScene(dialogScene);
            dialog.show();
			return false;
		}
		return false;
	}
}
