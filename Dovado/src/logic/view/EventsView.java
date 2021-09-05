package logic.view;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import logic.model.DAOActivity;
import logic.model.DAOChannel;
import logic.model.DAOPlace;
import logic.model.DAOPreferences;
import logic.model.DAOSchedules;
import logic.model.DAOSuperUser;
import logic.model.Log;
import logic.model.ScheduledActivity;
import logic.model.SuperActivity;

public class EventsView implements Initializable{
	
	private static ArrayList<ScheduledActivity> schedActivities;
	private static ArrayList<SuperActivity> activities;
	private static DAOPreferences daoPref;
    private static DAOActivity daoAct;
    private static DAOSuperUser daoSU;
    private static DAOChannel daoCH;
    private static DAOPlace daoPlc;
    private static DAOSchedules daoSch;

    @FXML
    private ListView<Object> eventsList;
    
    @FXML
    private VBox root;
    
    @FXML
    private TextField searchBar;
    
    @FXML
    private Button searchBtn;
    
    public static void render(Stage current) {
		try {
			VBox root = new VBox();
			BorderPane navbar = Navbar.getNavbar();
			Navbar.authenticatedSetup();
			
			VBox eventSchedule = new VBox();
			
			try {
				Scene scene = new Scene(root,Navbar.getWidth(),Navbar.getHeight());
				scene.getStylesheets().add(Main.class.getResource("Dovado.css").toExternalForm());
				current.setTitle("Dovado - events");
				current.setScene(scene);
				eventSchedule = FXMLLoader.load(Main.class.getResource("events.fxml"));
				VBox.setVgrow(eventSchedule, Priority.SOMETIMES);
			} catch(IOException e) {
				e.printStackTrace();
			}
			root.getChildren().addAll(navbar,eventSchedule);
			
			current.show();	
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		daoPref = DAOPreferences.getInstance();
		daoAct = DAOActivity.getInstance();
		daoSU = DAOSuperUser.getInstance();
		daoSch = DAOSchedules.getInstance();
		
		schedActivities = new ArrayList<ScheduledActivity>();
    	activities = new ArrayList<SuperActivity>();
		
    	searchBtn.getStyleClass().add("src-btn");    	
    	
    	Log.getInstance().logger.info("Ok \nWorking Directory = " + System.getProperty("user.dir"));		
		try{
			//Apro di default la lista di attività schedulate.
			schedActivities = (ArrayList) (daoSch.findSchedule(Navbar.getUser().getUserID())).getScheduledActivities();

			for(int j=0;j<schedActivities.size();j++) {
				activities.add(daoAct.findActivityByID(daoSU,schedActivities.get(j).getReferencedActivity().getId()));
			}

			for(int j=0;j<activities.size();j++)
			Log.getInstance().logger.info("tutte le attivit� "+activities.get(j).getId());
			Thread newThread = new Thread(() -> {
				int i;
				for(i=0;i<activities.size();i++) {
					ImageView eventImage = new ImageView();
					Text eventName = new Text(activities.get(i).getName()+"\n");
					Log.getInstance().logger.info("\n\n"+activities.get(i).getName()+"\n\n");
					Text eventInfo = new Text(activities.get(i).getPlace().getName()+
							"\n"+activities.get(i).getFrequency().getOpeningTime()+
							"-"+activities.get(i).getFrequency().getClosingTime());
					
					Button deleteSched = new Button();

					HBox eventLine = new HBox();
					eventLine.setAlignment(Pos.CENTER);
					VBox eventButtons = new VBox();
					
					eventButtons.getChildren().add(deleteSched);
					eventButtons.setAlignment(Pos.CENTER_RIGHT);
					
					deleteSched.setText("Delete from schedule");
					deleteSched.getStyleClass().add("src-btn");
					deleteSched.setAlignment(Pos.CENTER);
					
					String width = Double.toString(root.getWidth()/2);
					String height = Double.toString(root.getHeight()/2);
					
					eventImage.setImage(new Image("https://source.unsplash.com/user/erondu/400x100"));
					eventImage.getStyleClass().add("event-image");
					
					eventInfo.setId("eventInfo");
					eventInfo.getStyleClass().add("textEventInfo");
			/*		eventInfo.setTextAlignment(TextAlignment.LEFT);
					eventInfo.setFont(Font.font("Monserrat-Black", FontWeight.EXTRA_LIGHT, 12));
					eventInfo.setFill(Paint.valueOf("#ffffff"));
					eventInfo.setStrokeWidth(0.3);
					eventInfo.setStroke(Paint.valueOf("#000000"));
			*/		
					eventName.setId("eventName");
					eventName.getStyleClass().add("textEventName");
			/*		eventName.setFont(Font.font("Monserrat-Black", FontWeight.BLACK, 20));
					eventName.setFill(Paint.valueOf("#ffffff"));
					eventName.setStrokeWidth(0.3);
					eventName.setStroke(Paint.valueOf("#000000"));
				*/	
					VBox eventText = new VBox(eventName,eventInfo);
					eventText.setAlignment(Pos.CENTER);
					eventText.getStyleClass().add("eventTextVbox");
					//Preparo un box in cui contenere il nome dell'attivit� e altre sue
					//informazioni; uso uno StackPane per poter mettere scritte su immagini.
					StackPane eventBox = new StackPane();
					eventBox.getStyleClass().add("eventBox");
					
					
					Text eventId = new Text();
					Text placeId = new Text();
					
					eventId.setId(activities.get(i).getId().toString());
					placeId.setId(activities.get(i).getPlace().getId().toString());
					
					//Aggiungo allo stack pane l'id dell'evento, quello del posto, l'immagine
					//dell'evento ed infine il testo dell'evento.
					eventBox.getChildren().add(eventId);
					eventBox.getChildren().add(placeId);
					eventBox.getChildren().add(eventImage);
					eventBox.getChildren().add(eventText);
					//Stabilisco l'allineamento ed in seguito lo aggiungo alla lista di eventi.
					eventBox.setAlignment(Pos.CENTER_LEFT);
					
					eventLine.getChildren().addAll(eventBox,eventButtons);
					eventLine.setMinWidth(root.getWidth());
					eventsList.getItems().add(eventLine);
				}
			});
			newThread.start();
			eventsList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			newThread.join();
		}catch(Error e) {	Log.getInstance().logger.warning(e.getMessage());
		} catch (InterruptedException e) {
			e.printStackTrace();
			Log.getInstance().logger.info(e.getMessage());
		}
	}

	public void searchEvent() {
		
		String searchItem = null;
		
		if((searchItem = searchBar.getText())==null) return;
		
		if(daoPref.preferenceIsInJSON(searchItem.toUpperCase())==false) return;
		
		eventsList.getItems().clear();
		
		for(int i=0;i<activities.size();i++) {
			daoAct = DAOActivity.getInstance();
			daoSU = DAOSuperUser.getInstance();
			daoPlc = DAOPlace.getInstance();
			daoPref = DAOPreferences.getInstance();
			
			for(i=0;i<activities.size();i++) {
				SuperActivity act = activities.get(i);
				for(int j=0;j<act.getPreferences().size();j++) {
					if(searchBar.getText().toUpperCase().equals(act.getPreferences().get(j))) {
					 	
					ImageView eventImage = new ImageView();
					Text eventName = new Text(activities.get(i).getName()+"\n");
					Log.getInstance().logger.info("\n\n"+activities.get(i).getName()+"\n\n");
					Text eventInfo = new Text(activities.get(i).getPlace().getName()+
							"\n"+activities.get(i).getFrequency().getOpeningTime()+
							"-"+activities.get(i).getFrequency().getClosingTime());
					Button deleteSched = new Button();

					HBox eventLine = new HBox();
					eventLine.setAlignment(Pos.CENTER);
					VBox eventButtons = new VBox();
					
					eventButtons.getChildren().add(deleteSched);
					eventButtons.setAlignment(Pos.CENTER_RIGHT);
					
					deleteSched.setText("Delete from schedule");
					deleteSched.getStyleClass().add("src-btn");
					deleteSched.setAlignment(Pos.CENTER);
					
					String width = Double.toString(root.getWidth()/2);
					String height = Double.toString(root.getHeight()/2);
					
					eventImage.setImage(new Image("https://source.unsplash.com/user/erondu/400x100"));
					eventImage.getStyleClass().add("event-image");
					
					eventInfo.setId("eventInfo");
					eventInfo.getStyleClass().add("textEventInfo");
			/*		eventInfo.setTextAlignment(TextAlignment.LEFT);
					eventInfo.setFont(Font.font("Monserrat-Black", FontWeight.EXTRA_LIGHT, 12));
					eventInfo.setFill(Paint.valueOf("#ffffff"));
					eventInfo.setStrokeWidth(0.3);
					eventInfo.setStroke(Paint.valueOf("#000000"));
			*/		
					eventName.setId("eventName");
					eventName.getStyleClass().add("textEventName");
			/*		eventName.setFont(Font.font("Monserrat-Black", FontWeight.BLACK, 20));
					eventName.setFill(Paint.valueOf("#ffffff"));
					eventName.setStrokeWidth(0.3);
					eventName.setStroke(Paint.valueOf("#000000"));
				*/	
					VBox eventText = new VBox(eventName,eventInfo);
					eventText.setAlignment(Pos.CENTER);
					eventText.getStyleClass().add("eventTextVbox");
					//Preparo un box in cui contenere il nome dell'attivit� e altre sue
					//informazioni; uso uno StackPane per poter mettere scritte su immagini.
					StackPane eventBox = new StackPane();
					eventBox.getStyleClass().add("eventBox");
					
					
					Text eventId = new Text();
					Text placeId = new Text();
					
					eventId.setId(activities.get(i).getId().toString());
					placeId.setId(activities.get(i).getPlace().getId().toString());
					
					//Aggiungo allo stack pane l'id dell'evento, quello del posto, l'immagine
					//dell'evento ed infine il testo dell'evento.
					eventBox.getChildren().add(eventId);
					eventBox.getChildren().add(placeId);
					eventBox.getChildren().add(eventImage);
					eventBox.getChildren().add(eventText);
					//Stabilisco l'allineamento ed in seguito lo aggiungo alla lista di eventi.
					eventBox.setAlignment(Pos.CENTER_LEFT);
					
					eventLine.getChildren().addAll(eventBox,eventButtons);
					eventLine.setMinWidth(root.getWidth());
					eventsList.getItems().add(eventLine);
					}
				}
			}
		}
	}

}
