package logic.view;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import javafx.stage.Modality;
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
    private static SuperActivity activitySelected;
    private static StackPane lastEventBoxSelected;

    private static Stage curr;
    
    private static int lastActivitySelected = -1;
    
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
			
			
			Scene scene = new Scene(root,Navbar.getWidth(),Navbar.getHeight());
			scene.getStylesheets().add(Main.class.getResource("Dovado.css").toExternalForm());
			current.setTitle("Dovado - events");
			current.setScene(scene);
			eventSchedule = FXMLLoader.load(Main.class.getResource("events.fxml"));
			VBox.setVgrow(eventSchedule, Priority.SOMETIMES);
		
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
		
		schedActivities = new ArrayList<>();
    	activities = new ArrayList<>();
		
    	searchBtn.getStyleClass().add("src-btn");    	
    	
    	Log.getInstance().getLogger().info("Ok \nWorking Directory = " + System.getProperty("user.dir"));		
		try{
			//Apro di default la lista di attività schedulate.
			schedActivities = (ArrayList) (daoSch.findSchedule(Navbar.getUser().getUserID())).getScheduledActivities();

			for(int j=0;j<schedActivities.size();j++) {
				activities.add(daoAct.findActivityByID(daoSU,schedActivities.get(j).getReferencedActivity().getId()));
			}

			for(int j=0;j<activities.size();j++)
				Log.getInstance().getLogger().info("tutte le attivit� "+activities.get(j).getId());
			Thread newThread = new Thread(() -> {
				int i;
				for(i=0;i<activities.size();i++) {
					ImageView eventImage = new ImageView();
					Text eventName = new Text(activities.get(i).getName()+"\n");
					Log.getInstance().getLogger().info("\n\n"+activities.get(i).getName()+"\n\n");
					Text eventInfo = new Text(activities.get(i).getPlace().getName()+
							"\n"+activities.get(i).getFrequency().getOpeningTime()+
							"-"+activities.get(i).getFrequency().getClosingTime());
					

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
					Text schedId = new Text();
					
					eventId.setId(activities.get(i).getId().toString());
					schedId.setId(schedActivities.get(i).toString());
					
					//Aggiungo allo stack pane l'id dell'evento, quello del posto, l'immagine
					//dell'evento ed infine il testo dell'evento.
					eventBox.getChildren().add(eventId);
					eventBox.getChildren().add(schedId);
					eventBox.getChildren().add(eventImage);
					eventBox.getChildren().add(eventText);
					//Stabilisco l'allineamento ed in seguito lo aggiungo alla lista di eventi.
					eventBox.setAlignment(Pos.CENTER_LEFT);
					
					eventsList.getItems().add(eventBox);
				}
			});
			newThread.start();
			eventsList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			newThread.join();
		}catch(Error e) {	Log.getInstance().getLogger().warning(e.getMessage());
		} catch (InterruptedException e) {
			e.printStackTrace();
			Log.getInstance().getLogger().info(e.getMessage());
		}
	}

	public void scheduledActSelected() {

		daoAct = DAOActivity.getInstance();
		daoSU = DAOSuperUser.getInstance();
		daoPlc = DAOPlace.getInstance();

		StackPane eventBox = null;
		try {
			eventBox = (StackPane) eventsList.getSelectionModel().getSelectedItem();
		} catch(ClassCastException ce) {
			return;
		}
		
		Log.getInstance().getLogger().info(String.valueOf(lastActivitySelected));

		if(lastEventBoxSelected == eventBox) return;
		
		if(lastEventBoxSelected!=null) activityDeselected(lastEventBoxSelected);
		
		int itemNumber = eventsList.getSelectionModel().getSelectedIndex();
		
		//La prossima volta che selezioner� un altro evento oltre questo si resetta il suo eventBox.
		lastEventBoxSelected = eventBox;
		int schedEventID = itemNumber; 
		ImageView eventImage = (ImageView) eventBox.getChildren().get(2);
		VBox eventInfo = (VBox) eventBox.getChildren().get(3);
		
		Text eventName = (Text) eventInfo.getChildren().get(0);
		Text eventDetails = (Text) eventInfo.getChildren().get(1);
		
		Log.getInstance().getLogger().info(eventName+" "+eventDetails);

		HBox selection = new HBox();
		Button deleteSched = new Button();

		deleteSched.setText("Delete from schedule");
		
		deleteSched.getStyleClass().add("evn-btn");
		
		deleteSched.getStyleClass().add("evn-btn");
		
		deleteSched.setAlignment(Pos.CENTER);
		
		String remind = schedActivities.get(itemNumber).getReminderTime().toString().replace('T', ' ');
		String sched = schedActivities.get(itemNumber).getScheduledTime().toString().replace('T', ' ');
		
		Text reminderTime = new Text("Activity reminder at: "+remind);
		Text scheduledTime = new Text("Activity scheduled for: "+sched);

		reminderTime.getStyleClass().add("textEventInfo");
		scheduledTime.getStyleClass().add("textEventName");
		
		VBox schedInfo = new VBox();
		
		schedInfo.setAlignment(Pos.CENTER_RIGHT);
		schedInfo.getChildren().addAll(scheduledTime,reminderTime);
		

		selection.getChildren().addAll(deleteSched,schedInfo);
		eventsList.getItems().add(itemNumber+1, selection);
		lastActivitySelected = itemNumber;
		eventImage.setScaleX(1.2);
		eventImage.setScaleY(1.25);

		Log.getInstance().getLogger().info("Attivit� trovata: "+activitySelected);

		deleteSched.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
													
					final Stage dialog = new Stage();
					
					if(deleteScheduledEvent(schedEventID)) {
						eventsList.getItems().remove(itemNumber+1);
						eventsList.getItems().remove(itemNumber);
						lastActivitySelected= -1;
						lastEventBoxSelected= null;
						//Messaggio per il dialog box
						dialog.initModality(Modality.NONE);
				        dialog.initOwner(curr);
				        VBox dialogVbox = new VBox(20);
				                
				        Text success = (new Text("Scheduled activity successfully deleted"));
				        success.setTextAlignment(TextAlignment.CENTER);
				               
				        dialogVbox.getChildren().add(success);
				        Scene dialogScene = new Scene(dialogVbox, 100, 50);
				        dialog.setScene(dialogScene);
				        dialog.show();
			        }
			        else {
				        dialog.initModality(Modality.NONE);
				        dialog.initOwner(curr);
				        VBox dialogVboxFail = new VBox(20);
				                
				        Text fail = (new Text("Scheduled activity successfully deleted"));
				        fail.setTextAlignment(TextAlignment.CENTER);
				                
				        dialogVboxFail.getChildren().add(fail);
				        Scene dialogSceneFail = new Scene(dialogVboxFail, 100, 50);
				        dialog.setScene(dialogSceneFail);
				        dialog.show();    
			         }
				}
			});
			/*viewSchedInfo.setOnAction(new EventHandler<ActionEvent>(){
				@Override public void handle(ActionEvent e) {
					DateTimeFormatter day = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					String dayStringed = day.format();
							
					String hourChosen = hourBox.getValue();
					String minChosen = minBox.getValue();
							
					int hourReminderInt = Integer.parseInt(hourChosen);
					String hourReminder;
					hourReminder = Integer.toString(hourReminderInt-1);
					
					if(hourReminderInt-1<10) {
						hourReminder = "0"+hourReminder;
					}
							
					String dateChosen = dayStringed+' '+hourChosen+':'+minChosen;
					String dateReminder = dayStringed+' '+hourReminder+':'+minChosen;
							
					DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
					LocalDateTime dateSelected = LocalDateTime.parse(dateChosen,dateFormatter);
					LocalDateTime remindDate = LocalDateTime.parse(dateReminder,dateFormatter);
						
					final Stage dialog = new Stage();
			        dialog.initModality(Modality.NONE);
			        dialog.initOwner(curr);
			        VBox dialogVbox = new VBox(20);
			        dialogVbox.getChildren().add(new Text("Activity successfully scheduled"));
			        Scene dialogScene = new Scene(dialogVbox, 300, 200);
			        dialog.setScene(dialogScene);
			        dialog.show();
			                
				}
			});
				*/	
	}
	
	public void activityDeselected(StackPane lastEventBoxSelected2) {
		if(lastActivitySelected>=0) 
			eventsList.getItems().remove(lastActivitySelected+1);
		
		ImageView eventImage = (ImageView) lastEventBoxSelected2.getChildren().get(2);
/**CANCEL		VBox eventInfo = (VBox) lastEventBoxSelected2.getChildren().get(3);
		
		Text eventName = (Text) eventInfo.getChildren().get(0);
		Text eventDetails = (Text) eventInfo.getChildren().get(1);**/
		
		eventImage.setScaleX(1);
		eventImage.setScaleY(1);
	}
	
	public boolean deleteScheduledEvent(int idSched) {
		
		return daoSch.deleteSchedule(Navbar.getUser().getUserID(),idSched);

	}
	
	public void searchEvent() {
		
		String searchItem = null;
		
		if((searchItem = searchBar.getText())==null) return;
		
		if(!daoPref.preferenceIsInJSON(searchItem.toUpperCase())) return;
		
		eventsList.getItems().clear();
		
		for(int i=0;i<activities.size();i++) {
			daoAct = DAOActivity.getInstance();
			daoSU = DAOSuperUser.getInstance();
			daoPlc = DAOPlace.getInstance();
			daoPref = DAOPreferences.getInstance();
			//TODO Qui siamo sicuri sia corrretto?? nel for non dovremmo usare una diversa variabile?
			for(i=0;i<activities.size();i++) {
				SuperActivity act = activities.get(i);
				for(int j=0;j<act.getPreferences().size();j++) {
					if(searchBar.getText().toUpperCase().equals(act.getPreferences().get(j))) {
					 	
					ImageView eventImage = new ImageView();
					Text eventName = new Text(activities.get(i).getName()+"\n");
					Log.getInstance().getLogger().info("\n\n"+activities.get(i).getName()+"\n\n");
					Text eventInfo = new Text(activities.get(i).getPlace().getName()+
							"\n"+activities.get(i).getFrequency().getOpeningTime()+
							"-"+activities.get(i).getFrequency().getClosingTime());
					HBox eventLine = new HBox();
					eventLine.setAlignment(Pos.CENTER);
					VBox eventButtons = new VBox();
					
					Button deleteSched = new Button();
					
					eventButtons.getChildren().add(deleteSched);
					eventButtons.setAlignment(Pos.CENTER_RIGHT);
					
					deleteSched.setText("Delete from schedule");
					deleteSched.getStyleClass().add("src-btn");
					deleteSched.setAlignment(Pos.CENTER);
					
/**CANCEL					String width = Double.toString(root.getWidth()/2)
					String height = Double.toString(root.getHeight()/2)**/
					
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
