package logic.view;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import logic.controller.AddActivityToScheduleController;
import logic.controller.ChannelController;
import logic.controller.FindActivityController;
import logic.model.Activity;
import logic.model.CertifiedActivity;
import logic.model.NormalActivity;
import logic.model.Channel;
import logic.model.DAOActivity;
import logic.model.DAOChannel;
import logic.model.DAOCoupon;
import logic.model.Discount;
import logic.model.ExpiringActivity;
import logic.model.Log;
import logic.model.Partner;
import logic.model.PeriodicActivity;
import logic.model.Preferences;
import logic.model.ScheduleBean;
import logic.model.SuperActivity;
import logic.model.SuperUser;
import logic.model.User;

public class HomeView extends SuperView implements Initializable{
	private static final String TITLE = "Dovado - home";
	private static final String FILEFXML = "home.fxml";

	
	private static final  String BGCOLORKEY = "ffffff"; //$NON-NLS-1$
	private static final  String BGUCOLORKEY = "BC9416"; //$NON-NLS-1$
	private static final  String MAPPATHKEY = Messages.getString("HomeView.mapView"); //$NON-NLS-1$
	private static final  String IMAGES = "https://source.unsplash.com/user/erondu/290x120"; //$NON-NLS-1$
	private static final  String STYLEINFO = "textEventInfo"; //$NON-NLS-1$
	private static final  String STYLENAME = "textEventName"; //$NON-NLS-1$
	private static final  String EVENTINFO = "eventInfo"; //$NON-NLS-1$
	private static final  String EVENTNAME = "eventName"; //$NON-NLS-1$
	private static final  String CERTEVENTNAME = "certEventName"; //$NON-NLS-1$
	private static final  String CLOSEDKEY = "CLOSED NOW"; //$NON-NLS-1$
	private static final  String SPOTPLACESCRIPT = "spotPlace"; //$NON-NLS-1$
	private static final  String CERTIFIED = "CERTIFICATA"; //$NON-NLS-1$

	
	private StackPane lastEventBoxSelected;

	private VBox chatContainer;
	
	@FXML
	private TextField searchBar;
	
	@FXML
	private VBox root;

	@FXML
	private Slider distanceSelector;
	
	@FXML
	private Text distanceSelected;

    @FXML
    private Button searchButton;
    
    @FXML
    private Button preference1;

    @FXML
    private Button preference2;
    
    @FXML
    private Button preference3;
    
    @FXML
    private Text sliderText;

    @FXML
    private ListView<Object> eventsList;
    
    @FXML
    private WebView map;
	
    @FXML
    private WebEngine eng;
	
    private int lastActivitySelected = -1;

    private DAOActivity daoAct;
    private DAOChannel daoCH;
    private SuperActivity activitySelected;
    private SuperUser user;
    private double usrLat;
    private double usrLon;
    private ArrayList<Activity> activitiesToSpotUsr;
    private ArrayList<CertifiedActivity> activitiesToSpotPart;
    private boolean searchByPreference;
    private ChoiceBox<String> hourBox;
	private ChoiceBox<String> minBox;
	
	private ChoiceBox<String> hourBox2;
	private ChoiceBox<String> minBox2;
    
    
    //---------------------- INIZIO Metodo Initialize e relativi metodi di supporto ---------------------------
	public static void render(Stage current) {
		SuperView.render(current, TITLE, FILEFXML, true, true);
	}
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		lastEventBoxSelected = null;
		daoAct = DAOActivity.getInstance();
		
		usrLat = 41.952928;
		usrLon = 12.518342;
        Log.getInstance().getLogger().info("Coordinate della posizione attuale: "+usrLat+" "+usrLon); //$NON-NLS-1$ //$NON-NLS-2$

        preference1.getStyleClass().add(BTNPREFKEY);
        preference1.setText("By preferences"); //$NON-NLS-1$
        
        new ArrayList<Activity>();
        new ArrayList<CertifiedActivity>();
        
        user = Navbar.getUser();
    	if(user instanceof Partner) {
    		
	    	try{
	    		//Al posto di scegliere preferenze casuali
				//e mostrarne i risultati prendo le attività del partner e 
				//in base a quello restituisco risultati appropriati.
				
	    		ArrayList<CertifiedActivity> activitiesPartn = (ArrayList<CertifiedActivity>) daoAct.getPartnerActivities(user.getUserID());
	    		activitiesToSpotPart = activitiesPartn;
    			eng = map.getEngine();
    			eng.setJavaScriptEnabled(true);
    	        
    	        eng.load(MAPPATHKEY);
    			
    			// Setting permissions to interact with Js
    	        searchButton.setText("SEARCH"); //$NON-NLS-1$
    			searchButton.getStyleClass().add(BTNSRCKEY); 
    			searchBar.setPromptText("Insert a 6 digit coupon code");    			 //$NON-NLS-1$
    			sliderText.setText("You can validate any coupon up here\nor join a channel from your activities"); //$NON-NLS-1$
    			
    			//Al partner non serve il distance selector, ne il filter by preference.
    			distanceSelected.setVisible(false);
    			distanceSelected.setManaged(false);
    			distanceSelector.setVisible(false);
    			distanceSelector.setManaged(false);
    			preference1.setVisible(false);
    			preference1.setManaged(false);
    			
    			suppInitAct(null, null, activitiesPartn, true);
    			
    		} catch (InterruptedException e) {
    			Thread.currentThread().interrupt();
    			e.printStackTrace();
    			Log.getInstance().getLogger().info(e.getMessage());
    		} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	else {
	    	Log.getInstance().getLogger().info("Ok \nWorking Directory = " + System.getProperty("user.dir"));		 //$NON-NLS-1$ //$NON-NLS-2$
			try{
				ArrayList<Activity> activities = new ArrayList<>();
				activitiesToSpotUsr = activities;
				eng = map.getEngine();
				
				// Setting permissions to interact with Js
		        eng.setJavaScriptEnabled(true);
		        
		        eng.load(MAPPATHKEY);
		        
		        searchButton.setText("SEARCH"); //$NON-NLS-1$
				searchButton.getStyleClass().add(BTNSRCKEY);
				searchBar.setPromptText("Search activities"); //$NON-NLS-1$
				int dist = (int) Math.round(distanceSelector.getMin());
				distanceSelected.setText(Integer.toString(dist));
    			
				preference1.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						if(!searchByPreference) {
							searchByPreference=true;
							popupGen("Your searched activities will be filtered by preference!"); //$NON-NLS-1$
							
						}
						else {
							searchByPreference=false;
							popupGen("Your searched activities will not be filtered by preference!"); //$NON-NLS-1$
						}
						
					}
					
				});
				//Al posto di scegliere preferenze casuali
				//e mostrarne i risultati prendo le preferenze dell'utente e 
				//in base a quello restituisco risultati appropriati.
				
				Preferences preferences = ((User)Navbar.getUser()).getPreferences();
			
				suppInitAct(preferences, activities, null, false);
				
			} catch (InterruptedException e) {
    				Thread.currentThread().interrupt();
					e.printStackTrace();
					Log.getInstance().getLogger().info(e.getMessage());
				} catch (Exception e) {
					e.printStackTrace();
				}
    	}
    }
	
	private void suppInitAct(Preferences preferences , ArrayList<Activity> activities,ArrayList<CertifiedActivity> activitiesPartn, boolean isPartner) throws InterruptedException, ClassNotFoundException, SQLException {
		if(isPartner && !activitiesPartn.isEmpty()){
			suppInitPartAct(activitiesPartn);

		} else if(preferences!=null){
			activities.addAll(daoAct.getNearbyActivities(usrLat,usrLon,100));
			suppInitUserAct(activities);
			
		} else {
			StackPane infoBox = new StackPane();
			Text noPrefs = new Text("You have not set"+'\n'+"any preferences yet!"); //$NON-NLS-1$ //$NON-NLS-2$
			noPrefs.setTextAlignment(TextAlignment.CENTER);
			
			infoBox.getChildren().add(noPrefs);
			noPrefs.getStyleClass().add(STYLENAME);
			
			eventsList.getItems().add(infoBox);
		}
	}
	
	private void suppInitPartAct(ArrayList<CertifiedActivity> activitiesPartn) throws InterruptedException {
		Thread newThread = new Thread(() -> {
			int j;
			for(j=0;j<activitiesPartn.size();j++)
				Log.getInstance().getLogger().info("tutte le attivit� "+((SuperActivity)activitiesPartn.get(j)).getId()); //$NON-NLS-1$
			
			int i;
			for(i=0;i<activitiesPartn.size();i++) {
				if(!activitiesPartn.get(i).isPlayableOnThisDate(LocalDate.now())) {
					continue;
				}
				Text eventInfo = new Text(((SuperActivity)activitiesPartn.get(i)).getPlace().getName()+
						"\n"+((SuperActivity)activitiesPartn.get(i)).getFrequency().getOpeningTime()+ //$NON-NLS-1$
						"-"+((SuperActivity)activitiesPartn.get(i)).getFrequency().getClosingTime()); //$NON-NLS-1$
				Text eventName = new Text(((SuperActivity)activitiesPartn.get(i)).getName()+"\n"); //$NON-NLS-1$
				Log.getInstance().getLogger().info("\n\n"+((SuperActivity)activitiesPartn.get(i)).getName()+"\n\n"); //$NON-NLS-1$ //$NON-NLS-2$
				
				
				StackPane eventBox = setView(eventInfo, eventName, true , i, null, activitiesPartn);
				
				eventsList.getItems().add(eventBox);
				
			}
		});
		newThread.start();
		eventsList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		newThread.join();
	
	}
	
	private void suppInitUserAct(ArrayList<Activity> activities) throws InterruptedException {		
		
		int j;
		for(j=0;j<activities.size();j++)
			Log.getInstance().getLogger().info("tutte le attivit� "+activities.get(j).getId()); //$NON-NLS-1$
		
		Thread newThread = new Thread(() -> {
			int i;
			for(i=0;i<activities.size();i++) {
				if(!(activities.get(i)).isPlayableOnThisDate(LocalDate.now())) {
						continue;
					}
				Text eventInfo;

				if(activities.get(i).getFrequency() instanceof ExpiringActivity) {
					eventInfo = new Text(activities.get(i).getPlace().getName()+
							"\n Expiring activity"+ //$NON-NLS-1$
							"\n"+activities.get(i).getFrequency().getOpeningTime()+ //$NON-NLS-1$
							"-"+activities.get(i).getFrequency().getClosingTime()); //$NON-NLS-1$
				}
				else if(activities.get(i).getFrequency() instanceof PeriodicActivity) {
					eventInfo = new Text(activities.get(i).getPlace().getName()+
							"\n Periodic activity"+ //$NON-NLS-1$
							"\n"+activities.get(i).getFrequency().getOpeningTime()+ //$NON-NLS-1$
							"-"+activities.get(i).getFrequency().getClosingTime()); //$NON-NLS-1$
				}
				else{
					eventInfo = new Text(activities.get(i).getPlace().getName()+
						"\n Continuos activity"+ //$NON-NLS-1$
						"\n"+activities.get(i).getFrequency().getOpeningTime()+ //$NON-NLS-1$
						"-"+activities.get(i).getFrequency().getClosingTime()); //$NON-NLS-1$
				}
				
				
				Text eventName = new Text(activities.get(i).getName()+"\n"); //$NON-NLS-1$
				Log.getInstance().getLogger().info("\n\n"+activities.get(i).getName()+"\n\n"); //$NON-NLS-1$ //$NON-NLS-2$
				
				StackPane eventBox = setView(eventInfo, eventName, false, i, activities, null);
				
				eventsList.getItems().add(eventBox);
			}
		});
		newThread.start();
		eventsList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		newThread.join();
	
	}
	
	private StackPane setView(Text eventInfo, Text eventName, boolean isPartner, int i, ArrayList<Activity> activities, ArrayList<CertifiedActivity> activitiesPartn) {
		
		ImageView eventImage = new ImageView();
		eventImage.setImage(new Image(IMAGES));
		eventImage.getStyleClass().add("event-image"); //$NON-NLS-1$
		
		eventInfo.setId(EVENTINFO);
		eventInfo.getStyleClass().add(STYLEINFO);
		eventInfo.setWrappingWidth(280);

		eventName.setId(EVENTNAME);
		eventName.getStyleClass().add(STYLENAME);
		eventName.setWrappingWidth(280);

		VBox eventText = new VBox(eventName,eventInfo);
		eventText.setAlignment(Pos.CENTER_LEFT);
		eventText.getStyleClass().add("eventTextVbox"); //$NON-NLS-1$
		//Preparo un box in cui contenere il nome dell'attivit� e altre sue
		//informazioni; uso uno StackPane per poter mettere scritte su immagini.
		StackPane eventBox = new StackPane();
		eventBox.getStyleClass().add("eventBox"); //$NON-NLS-1$
		
		Text eventId = new Text();
		Text placeId = new Text();
		
		//Aggiungo allo stack pane l'id dell'evento, quello del posto, l'immagine
		//dell'evento ed infine il testo dell'evento.
		
		
		
		if(isPartner) {
			eventId.setId(((SuperActivity)activitiesPartn.get(i)).getId().toString());
			placeId.setId(((SuperActivity)activitiesPartn.get(i)).getPlace().getId().toString());
			
			eventBox.getChildren().add(eventId);
			eventBox.getChildren().add(placeId);
			eventBox.getChildren().add(eventImage);
			eventBox.getChildren().add(eventText);
			
			if(!activitiesPartn.get(i).isOpenOnThisTime(LocalTime.now())) {
				eventBox.setOpacity(0.4);
				Text closed = new Text(CLOSEDKEY);
				closed.getStyleClass().add(STYLEINFO);
				closed.setTextAlignment(TextAlignment.CENTER);
				eventBox.getChildren().add(closed);
			}
		} else {
			eventId.setId(activities.get(i).getId().toString());
			placeId.setId(activities.get(i).getPlace().getId().toString());
			
			eventBox.getChildren().add(eventId);
			eventBox.getChildren().add(placeId);
			eventBox.getChildren().add(eventImage);
			eventBox.getChildren().add(eventText);
			
			if(activities.get(i) instanceof CertifiedActivity) {
				eventName.getStyleClass().clear();
				eventName.getStyleClass().add(CERTEVENTNAME);
				eventName.setText(eventName.getText()+'\n'+CERTIFIED);
			}
			
			if(activities.get(i) instanceof NormalActivity){
				if(!((NormalActivity)(activities.get(i))).isOpenOnThisTime(LocalTime.now())) {
					eventBox.setOpacity(0.4);
					Text closed = new Text(CLOSEDKEY);
					closed.getStyleClass().add(STYLEINFO);
					closed.setTextAlignment(TextAlignment.CENTER);
					eventBox.getChildren().add(closed);
				}
				
			} else {
				if(!((CertifiedActivity)(activities.get(i))).isOpenOnThisTime(LocalTime.now())) {
					eventBox.setOpacity(0.4);
					Text closed = new Text(CLOSEDKEY);
					closed.getStyleClass().add(STYLEINFO);
					closed.setTextAlignment(TextAlignment.CENTER);
					eventBox.getChildren().add(closed);
				}
			}
		}
		//Stabilisco l'allineamento ed in seguito lo aggiungo alla lista di eventi.
		eventBox.setAlignment(Pos.CENTER);
		return eventBox;
			
	}
	
	//--------------------------FINE METODO Initialize e funzioni di supporto relative----------------------------
	
	
	public void updateDistance() {
		
		int dist = (int) Math.round(distanceSelector.getValue());
		distanceSelected.setText(Integer.toString(dist));
	}
	
	public void activitySelected() {
		daoAct = DAOActivity.getInstance();
		StackPane eventBox = null;
		try {
			eventBox = (StackPane) eventsList.getSelectionModel().getSelectedItem();
		} catch(ClassCastException ce) {
			return;
		}

		if(user instanceof User) {
			if(lastEventBoxSelected==null) {
				engExecActSel();
				eng.executeScript("setUser("+usrLat+","+usrLon+")");	 //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
		}
		else {
			for(CertifiedActivity cAct:activitiesToSpotPart) {
				eng.executeScript("spotPlace("+cAct.getPlace().getLatitudine()+","+cAct.getPlace().getLongitudine()+", \""+cAct.getPlace().getName()+"\","+cAct.getPlace().getId()+")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			}	
		}
		Log.getInstance().getLogger().info(String.valueOf(lastActivitySelected));

		if(lastEventBoxSelected == eventBox) return;
		
		if(lastEventBoxSelected!=null) activityDeselected(lastEventBoxSelected,false);
		
		int itemNumber = eventsList.getSelectionModel().getSelectedIndex();
		
		//La prossima volta che selezioner� un altro evento oltre questo si resetta il suo eventBox.
		lastEventBoxSelected = eventBox;
		
		Long activityId = Long.parseLong(eventBox.getChildren().get(0).getId());
		
		Long.parseLong(eventBox.getChildren().get(1).getId());
		ImageView eventImage = (ImageView) eventBox.getChildren().get(2);
		VBox eventInfo = (VBox) eventBox.getChildren().get(3);
		
		Text eventName = (Text) eventInfo.getChildren().get(0);
		Text eventDetails = (Text) eventInfo.getChildren().get(1);
		
		Log.getInstance().getLogger().info(eventName+" "+eventDetails); //$NON-NLS-1$
		
		Text activityDescription= new Text();
		VBox selection = new VBox();
		Button viewOnMap = new Button();
		Button joinActivityChannel = new Button();
		
		viewOnMap.setText("View event"); //$NON-NLS-1$
		joinActivityChannel.setText("Join channel"); //$NON-NLS-1$
		
		viewOnMap.getStyleClass().add(BTNEVNKEY);
		joinActivityChannel.getStyleClass().add(BTNEVNKEY);
		
		selection.getChildren().addAll(viewOnMap,joinActivityChannel);
		
		eventsList.getItems().add(itemNumber+1, selection);
		lastActivitySelected = itemNumber;
		eventImage.setScaleX(1.2);
		eventImage.setScaleY(1.25);

		try {
			activitySelected = (SuperActivity) daoAct.getActivityById(activityId);
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
		Log.getInstance().getLogger().info("Attivit� trovata: "+activitySelected); //$NON-NLS-1$

		if(activitySelected.getFrequency() instanceof PeriodicActivity) {
			activityDescription.setText("A periodic activity with a: "+ //$NON-NLS-1$
		((PeriodicActivity)(activitySelected.getFrequency())).getCadence().toString()+
			" cadence\n\nOpen from the date:\n"+((PeriodicActivity)(activitySelected.getFrequency())).getFormattedStartDate()+ //$NON-NLS-1$
			"\nTo the date:\n"+((PeriodicActivity)(activitySelected.getFrequency())).getFormattedEndDate()+"\n\n"); //$NON-NLS-1$ //$NON-NLS-2$

			activityDescription.setText(activityDescription.getText()+"Description:\n"+activitySelected.getDescription()); //$NON-NLS-1$
		} 
		else if(activitySelected.getFrequency() instanceof ExpiringActivity) {
				activityDescription.setText("An expiring activity that goes from the date:\n"+ //$NON-NLS-1$
						((ExpiringActivity)(activitySelected.getFrequency())).getFormattedStartDate()+
				"\nTo the date:\n"+((ExpiringActivity)(activitySelected.getFrequency())).getFormattedEndDate()+"\n\n"); //$NON-NLS-1$ //$NON-NLS-2$

				activityDescription.setText(activityDescription.getText()+"Description:\n"+activitySelected.getDescription()); //$NON-NLS-1$
		}
		else {
			activityDescription.setText(activitySelected.getDescription());
		}
		activityDescription.setWrappingWidth(280);
		activityDescription.getStyleClass().add("actInfo"); //$NON-NLS-1$
		
		selection.getChildren().add(0,activityDescription);
		
		viewOnMap.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
					eng.executeScript("moveViewDesktop("+activitySelected.getPlace().getLatitudine()+","+activitySelected.getPlace().getLongitudine()+","+activitySelected.getId()+")");	 //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				}
		});
		
		joinActivityChannel.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
						handleJoinChActUp();
			}
		});
		
		if(user instanceof User) {
			Button playActivity = new Button();
			playActivity.setText("Play activity"); //$NON-NLS-1$
			playActivity.getStyleClass().add(BTNEVNKEY);
			
			selection.getChildren().add(playActivity);
			
			playActivity.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent e) {
					
						handlePlayAct(activityId);			
				}
			});
		}
		else {
			Button deleteActivity = new Button();
			deleteActivity.setText("Delete activity"); //$NON-NLS-1$
			deleteActivity.getStyleClass().add(BTNEVNKEY);
			
			selection.getChildren().add(deleteActivity);
			
			deleteActivity.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					popupGen("Activity deleted correctly!");  //$NON-NLS-1$
					    
					Log.getInstance().getLogger().info("Attivit\u00E0 cancellata dalla persistenza"); //$NON-NLS-1$
					activityDeselected(lastEventBoxSelected,true);
				}
			});
			
		}
	}
	
	
	private void engExecActSel() {
		for(Activity current:activitiesToSpotUsr) {
			eng.executeScript(SPOTPLACESCRIPT+"(" //$NON-NLS-1$
				+ ""+(current.getPlace().getLatitudine())+"" //$NON-NLS-1$ //$NON-NLS-2$
				+ ","+(current.getPlace().getLongitudine())+", " //$NON-NLS-1$ //$NON-NLS-2$
				+ "\""+current.getPlace().getName()+"\"," //$NON-NLS-1$ //$NON-NLS-2$
				+ ""+(current.getPlace().getId())+")"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	
	private void handleJoinChActUp() {

		if(root.getChildren().get(root.getChildren().size()-1).getId()=="activityCh") { //$NON-NLS-1$
			return;
		}
	//Cliccato il pulsante si deve aprire una chat e comparire 
	//tutto ciò che è stato scritto.
		daoCH = DAOChannel.getInstance();
		chatContainer = new VBox();
		Button send = new Button();
		Button close = new Button();
		ListView chat = new ListView();
		HBox textAndSend = new HBox();
		TextField mss = new TextField();
		
		updateChat(chat,activitySelected.getChannel());
		
		send.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				Log.getInstance().getLogger().info("\n\nInviando un messaggio...\n"); //$NON-NLS-1$
				Log.getInstance().getLogger().info("\nMessaggi prima dell'invio:\n"); //$NON-NLS-1$
				for(int j=0;j<activitySelected.getChannel().getChat().size();j++) {
					Log.getInstance().getLogger().info(activitySelected.getChannel().getChat().get(j).getMsgText());
				}
				activitySelected.getChannel().addMsg(user.getUsername(), mss.getText());
				
				ChannelController c = new ChannelController(user, activitySelected.getId());
				try {
					c.sendMessage(mss.getText());
				} catch (ClassNotFoundException | SQLException e1) {
					popupGen("Message not sent due to DB error"); //$NON-NLS-1$
				
					
					e1.printStackTrace();
					return;
				}
				Log.getInstance().getLogger().info("\nMessaggi dopo l'invio:\n"); //$NON-NLS-1$

				for(int j=0;j<activitySelected.getChannel().getChat().size();j++) {
					Log.getInstance().getLogger().info(activitySelected.getChannel().getChat().get(j).getMsgText());
				}
				updateChat(chat,activitySelected.getChannel());
				mss.clear();
			}
		});
		
		send.setText("Send"); //$NON-NLS-1$
		send.getStyleClass().add(BTNSRCKEY);
		
		close.setText("x"); //$NON-NLS-1$
		close.getStyleClass().add(BTNSRCKEY);					
		close.setAlignment(Pos.TOP_RIGHT);
		
		textAndSend.getChildren().addAll(mss,send);
		chatContainer.getChildren().addAll(close,chat,textAndSend);
		chatContainer.setAlignment(Pos.BOTTOM_RIGHT);
		chatContainer.setId("activityCh"); //$NON-NLS-1$
		root.getChildren().add(chatContainer);
		Timer chatRefreshTimer = new Timer();
		
		close.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e) {
				root.getChildren().remove(chatContainer);
				chatRefreshTimer.cancel();
			}
		});
		
		
		chatRefreshTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(()->{Log.getInstance().getLogger().info("Refreshing messages...");updateChat(chat,activitySelected.getChannel());}); //$NON-NLS-1$
			}
			
		},0, 10000);
	
	}
	
	
	private void handlePlayAct(Long activityId) {

		VBox selectedBox = (VBox)eventsList.getItems().get(lastActivitySelected+1);			
		
		if(selectedBox.getChildren().get(selectedBox.getChildren().size()-1).getId()=="dateBox") { //$NON-NLS-1$
			return;
		}
		//Apro un pop up in cui si può scegliere una
		//Data in cui svolgere l'attività
		DatePicker pickDate = new DatePicker();
		DatePicker pickDateReminder = new DatePicker();
		hourBox = new ChoiceBox<>();
		minBox = new ChoiceBox<>();
		
		hourBox2 = new ChoiceBox<>();
		minBox2 = new ChoiceBox<>();

		int upperLimit;
		int lowerLimit;
		int upperLimMin;
		int lowerLimMin;
		
		lowerLimit = activitySelected.getFrequency().getOpeningTime().getHour();
		upperLimit = activitySelected.getFrequency().getClosingTime().getHour();
		
		lowerLimMin = activitySelected.getFrequency().getOpeningTime().getMinute();
		upperLimMin = activitySelected.getFrequency().getClosingTime().getMinute();
		
		for(int i=lowerLimit;i<=upperLimit;i++) {
			String hr = Integer.toString(i);
			minCheckHandleHour(hr, i);
			hourBox.getItems().add(hr);
		}
		
		hourBox.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				handle2HourBox(lowerLimit, lowerLimMin, upperLimit, upperLimMin);
			}
		});
		
		for(int i=lowerLimit;i<=upperLimit;i++) {
			String hr = Integer.toString(i);
			minCheckHandleHour(hr, i);
			hourBox2.getItems().add(hr);
		}
		
		hourBox2.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				handle2HourBox2( lowerLimit, lowerLimMin, upperLimit,  upperLimMin);
			}
		});
		
		Text txt = new Text("Select date"); //$NON-NLS-1$
		Button ok = new Button();
		Button close = new Button();
		
		VBox dateBox = new VBox();
		ok.setText("Ok"); //$NON-NLS-1$
		ok.getStyleClass().add(BTNSRCKEY);
		
		HBox buttonBox = new HBox();
		HBox pickTimeBox = new HBox();
		HBox pickReminderBox = new HBox();
		
		Text txtTime = new Text("Select scheduled time!"); //$NON-NLS-1$
		Text txtReminder = new Text("|OPTIONAL|"+'\n'+"Select a time reminder"+'\n'+"for the day scheduled or specify the"+'\n'+"day we should remind you."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		
		buttonBox.getChildren().addAll(close,ok);
		
		CornerRadii cr = new CornerRadii(4);
		BackgroundFill bf = new BackgroundFill(Paint.valueOf(BGCOLORKEY), cr, null);
		Background b = new Background(bf);
		
		txt.getStyleClass().add("msstxt"); //$NON-NLS-1$
		
		pickReminderBox.getChildren().addAll(hourBox2,minBox2);
		pickTimeBox.getChildren().addAll(hourBox,minBox);
		
		dateBox.setBackground(b);
		dateBox.getChildren().addAll(txt,pickDate,txtTime,pickTimeBox,txtReminder,pickDateReminder,pickReminderBox);
		dateBox.setId("dateBox"); //$NON-NLS-1$
		
		ChoiceBox<String> percDiscount = new ChoiceBox<>();
		
		if(activitySelected instanceof CertifiedActivity) {
			Text activityPrice = new Text(((CertifiedActivity)activitySelected).getPrice());
			Text discountDescription;
			try {
				
				ArrayList<Discount> discList = (ArrayList<Discount>) daoAct.viewDiscounts(activityId);
				if (discList==null || discList.isEmpty()) {
					discountDescription = new Text("No discount available"+'\n'+" for this activity."); //$NON-NLS-1$ //$NON-NLS-2$
				}else {
					discountDescription = new Text("Pick a discount if you want."); //$NON-NLS-1$
				}
				percDiscount.getItems().add("0% - 0$"); //$NON-NLS-1$
				for(int i=0;i<discList.size();i++) {
					percDiscount.getItems().add(Integer.toString(discList.get(i).getPercentuale())+"% - "+Integer.toString(discList.get(i).getPrice())+"$"); //$NON-NLS-1$ //$NON-NLS-2$
				}
				dateBox.getChildren().addAll(activityPrice,discountDescription,percDiscount);		
				percDiscount.setValue("0% - 0$"); //$NON-NLS-1$
			
			}catch(NullPointerException exc){
				Log.getInstance().getLogger().info("discList.file() ha fatto partire il null"); //$NON-NLS-1$
				exc.printStackTrace();
			}
			catch(Exception e2) {
				Log.getInstance().getLogger().info("Database error, discounts not found."); //$NON-NLS-1$
				e2.printStackTrace();
			}
		}
		dateBox.getChildren().add(buttonBox);
		selectedBox.getChildren().add(dateBox);
		
		close.setText("Close"); //$NON-NLS-1$
		close.getStyleClass().add(BTNSRCKEY);					
		
		close.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e) {
				selectedBox.getChildren().remove(dateBox);
			}
		});
		
		ok.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e) {
				handle2Ok(pickDate,pickDateReminder, dateBox, percDiscount,  activityId);
				
			}
		});
		
	
	}
	private void handle2HourBox(int lowerLimit, int lowerLimMin,int upperLimit, int upperLimMin) {

		minBox.getItems().clear();
		int selectedHour = Integer.parseInt(hourBox.getValue());
		if(selectedHour==lowerLimit) {
			for(int j=lowerLimMin;j<61;j++) {
				String min = Integer.toString(j);
				min = minCheckHandleHour( min, j);
				minBox.getItems().add(min);
				
			}
			return;
		}
		if(selectedHour==upperLimit) {
			for(int j=0;j<=upperLimMin;j++) {
				String min = Integer.toString(j);
				min = minCheckHandleHour( min, j);
				minBox.getItems().add(min);
			}
			
		}
		else {
			for(int j=0;j<61;j++) {
				String min = Integer.toString(j);
				min = minCheckHandleHour( min, j);
				minBox.getItems().add(min);
			}
		}
	}
	
	private void handle2HourBox2(int lowerLimit, int lowerLimMin,int upperLimit, int upperLimMin) {

		minBox2.getItems().clear();
		int selectedHour = Integer.parseInt(hourBox.getValue());
		if(selectedHour==lowerLimit) {
			for(int j=lowerLimMin;j<61;j++) {
				String min = Integer.toString(j);
				min = minCheckHandleHour( min, j);
				minBox2.getItems().add(min);
				
			}
			return;
		}
		if(selectedHour==upperLimit) {
			for(int j=0;j<=upperLimMin;j++) {
				String min = Integer.toString(j);
				min = minCheckHandleHour( min, j);
				minBox2.getItems().add(min);
			}
			
		}
		else {
			for(int j=0;j<61;j++) {
				String min = Integer.toString(j);
				min = minCheckHandleHour( min, j);
				minBox2.getItems().add(min);
			}
		}
	}
	private String minCheckHandleHour(String min, int j) {
		if(j<10) {
			min = "0"+min; //$NON-NLS-1$
		}
		return min;
		
	}
	
	private void handle2Ok(DatePicker pickDate,DatePicker pickDateReminder, VBox dateBox, ChoiceBox<String> percDiscount, Long activityId) {

		if(pickDate.getValue().toString().isBlank() || hourBox.getValue().isBlank() || minBox.getValue().isBlank()) {
			Log.getInstance().getLogger().info("Non avendo inserito abbastanza prenotazioni non si effettuano modifiche."); //$NON-NLS-1$
			popupGen("You haven't specified enough info.");  //$NON-NLS-1$
		
		}
	    DateTimeFormatter day = DateTimeFormatter.ofPattern("yyyy-MM-dd"); //$NON-NLS-1$
		String dayStringed = day.format(pickDate.getValue());
																		
		dayStringed.split("-"); //$NON-NLS-1$
		
		String hourChosen = hourBox.getValue();
		String minChosen = minBox.getValue();
		String dateReminderChosen;
		String hourReminder;
		if (hourBox2.getValue()==null || minBox2.getValue()==null) {
			int hourReminderInt = Integer.parseInt(hourChosen);
			hourReminder = Integer.toString(hourReminderInt-1);
	
			Log.getInstance().getLogger().info("Non avendo specificato un orario si setta di predefinito un'ora prima della prenotazione"); //$NON-NLS-1$
			popupGen("You haven't specified a time for your reminder... setting to 1 hour before the scheduled event");  //$NON-NLS-1$
			
			
			if(hourReminderInt-1<10) {
				hourReminder = "0"+hourReminder; //$NON-NLS-1$
			}
		} else {
			hourReminder = hourBox2.getValue();
			minBox2.getValue();
		}
		if(pickDateReminder.getValue().toString().isBlank()) {
			Log.getInstance().getLogger().info("Non avendo specificato un orario si setta di predefinito il giorno stesso della prenotazione"); //$NON-NLS-1$
			dateReminderChosen=dayStringed;
			popupGen("You haven't specified a day for your reminder... setting to 1 day before the scheduled event");  //$NON-NLS-1$
			
		} else {dateReminderChosen = day.format(pickDateReminder.getValue());}
		String dateChosen = dayStringed;

		ScheduleBean sb = new ScheduleBean();
		
		if(dateBox.getChildren().contains(percDiscount)) {
			String[] percPrice = percDiscount.getValue().split("% - "); //$NON-NLS-1$
			int percRequested = Integer.parseInt(percPrice[0]);
			
			String priceString = (percPrice[1].split("$"))[0]; //$NON-NLS-1$
			int pricePayed = Integer.parseInt(priceString);
			
			if(pricePayed > ((User)user).getBalance()) {
				Log.getInstance().getLogger().info("Not enough dovado $ for payment"); //$NON-NLS-1$
				popupGen("Not enough Dovado $ for payment");  //$NON-NLS-1$
			
				return;
			} else {
				sb.setSelectedCoupon(percRequested);
			}
		}
						
		sb.setIdActivity(activityId);
		sb.setScheduledDate(dateChosen);
		sb.setScheduledTime(hourChosen+':'+minChosen);
		sb.setReminderDate(dateReminderChosen);
		sb.setReminderTime(hourReminder+':'+minChosen);
		
		AddActivityToScheduleController sc = new AddActivityToScheduleController((User) user, sb);
		
		checkActivity(sc);
		
		popupGen("Activity successfully scheduled");  //$NON-NLS-1$
	}
	
	private void checkActivity(AddActivityToScheduleController sc) {
		if(activitySelected instanceof CertifiedActivity) {
			try {
				sc.addCertifiedActivityToSchedule();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}else {
			try {
				sc.addActivityToSchedule();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	//-------------------------------Fine                            -----------------------------------------
	private void updateChat(ListView chat, Channel ch) {
		int i;
		chat.getItems().clear();
		try {
			ch = daoCH.getChannel(ch.getActivityReferenced());
		} catch (ClassNotFoundException | SQLException e) {
			popupGen("Unable to get chat."); //$NON-NLS-1$
			
			e.printStackTrace();
			return;
		}
		//ONCE THE ACTIVITY HAS HIS NEWLY UPDATED CHANNEL THE CHAT WILL BE 
		//UPDATED.
		activitySelected.setChannel(ch);
		for(i=0;i<ch.getChat().size();i++) {
			VBox chatChContainer = new VBox();
			VBox chatMss = new VBox();
			Text msstxt = new Text();
			Text username = new Text();
			Text dateSent = new Text();
			String usernameMss = ch.getChat().get(i).getUsr();
			
			//Mi trovo lo username da mettere sopra il messaggio:
			username.setText(usernameMss);
			//Mi trovo il testo da mettere al centro del messaggio:
			msstxt.setText(ch.getChat().get(i).getMsgText());
			//Mi trovo il tempo di invio da mettere in basso a destra del messaggio:
			dateSent.setText(ch.getChat().get(i).getMsgSentDate());
			
			username.setTextAlignment(TextAlignment.LEFT);
			msstxt.setTextAlignment(TextAlignment.LEFT);
			dateSent.setTextAlignment(TextAlignment.RIGHT);
			
			username.getStyleClass().add("mssusr"); //$NON-NLS-1$
			msstxt.getStyleClass().add("msstxt"); //$NON-NLS-1$
			dateSent.getStyleClass().add("msssent"); //$NON-NLS-1$
			
			chatChContainer.getChildren().addAll(username,msstxt,dateSent);
			chatChContainer.setMaxWidth(root.getWidth()/2);
			
			chatMss.getChildren().add(chatChContainer);
			chatMss.autosize();
			
			Log.getInstance().getLogger().info("Messaggi ricevuti: "+chatMss); //$NON-NLS-1$
			
			if(user.getUsername().equals(usernameMss)) {
				CornerRadii cr = new CornerRadii(8);
			
				BackgroundFill bf = new BackgroundFill(Paint.valueOf(BGUCOLORKEY ), cr, null);
				Background b = new Background(bf);
				
				chatChContainer.setBackground(b);
				chatChContainer.setAlignment(Pos.CENTER_RIGHT);
				
				chatMss.setAlignment(Pos.CENTER_RIGHT);
			}
			else {
				CornerRadii cr = new CornerRadii(8);
				BackgroundFill bf = new BackgroundFill(Paint.valueOf(BGCOLORKEY), cr, null);
				Background b = new Background(bf);
				
				chatChContainer.setBackground(b);
				chatMss.setAlignment(Pos.CENTER_LEFT);
			}
			chat.getItems().add(chatMss);
		}
		if(i>0) {
			chat.scrollTo(i-1);
		}
	}

	//Penso che questo metodo come anche activity selected etc... vadano spostati in un'apposita classe
	//HomeController.
	
	public void activityDeselected(StackPane lastBox,boolean delete) {
		
		if(lastActivitySelected>=0) {
			eventsList.getItems().remove(lastActivitySelected+1);
		}
		if(delete) {
			eventsList.getItems().remove(lastEventBoxSelected);
		
			lastEventBoxSelected=null;
			return;
		}
		
		ImageView eventImage = (ImageView) lastBox.getChildren().get(2);
	
		eventImage.setScaleX(1);
		eventImage.setScaleY(1);
		
		root.getChildren().remove(chatContainer);
	}
	
	public void filterActivities() {
		daoAct = DAOActivity.getInstance();

		if(Navbar.getUser() instanceof Partner) {
			filterActPartner();
		} else {
			searchBar.setPromptText("Search activities"); //$NON-NLS-1$
		}
		
		lastActivitySelected = -1;
		String searchItem = null;
		
		ArrayList<Activity> activities = null;
		if((searchItem = searchBar.getText())==null) return; 

		eng.executeScript("removeAllMarkers()"); //$NON-NLS-1$
		String[] keywords = searchItem.split(";"); //$NON-NLS-1$
		
		try {
			//Eseguo un controllo sulla ricerca delle attività; se il risultato è un'arraylist vuoto, allora
			//segnalo l'errore e esco dal metodo.
			activities = (ArrayList<Activity>) daoAct.getNearbyActivities(usrLat, usrLon, Float.parseFloat(distanceSelected.getText()));
			if(searchByPreference) {
				activities = (ArrayList<Activity>) FindActivityController.filterActivitiesByPreferences(activities,((User)user).getPreferences());
			}
			activities = (ArrayList<Activity>) FindActivityController.filterActivitiesByKeyWords(activities, keywords);
			if(activities.isEmpty() ) {
				Log.getInstance().getLogger().info("Nothing was found!"); //$NON-NLS-1$
				popupGen("Nothing has been found");  //$NON-NLS-1$
				return;
			}
			
		} catch (Exception e) {
			Log.getInstance().getLogger().info("La ricerca delle attivita non \u00E8 andata a buon fine. \n per colpa di un errore nel metodo del DB."); //$NON-NLS-1$
			e.printStackTrace();
			return;
		}
		eventsList.getItems().clear();
		
		int i;
		for(i=0;i<activities.size();i++) {
				filterActUserSupport(activities, i);
		}
	}
	
	private void filterActPartner() {
		int couponCode;
		try {
		couponCode = Integer.parseInt(searchBar.getText());
		} catch(NumberFormatException ne) {
			popupGen("Insert a 6 digit NUMBER"); //$NON-NLS-1$
			
		    return;
		}
		try{ 
			//Con il metodo sottostante mi assicuro della presenza del coupon.
			if((DAOCoupon.getInstance().findCouponPartner(couponCode))==null) {
				popupGen("No such coupon found!"); //$NON-NLS-1$
			    return;
			}
			//Una volta trovato eseguo il codice necessario a redimerlo.
			AddActivityToScheduleController actSched = new AddActivityToScheduleController((Partner)(Navbar.getUser()), searchBar.getText());
			actSched.redeemCoupon();
		} 
		catch(Exception e){
			//Si cattura un'eccezione se trovata e si avverte l'utente				
			popupGen("Due to an issue the coupon was not redeemed."); //$NON-NLS-1$
			
		    e.printStackTrace();
		    return;
		}
		popupGen("Coupon redeemed correctly"); //$NON-NLS-1$

	    searchBar.setText(""); //$NON-NLS-1$
	    searchBar.setPromptText("Insert a 6 digit coupon code"); //$NON-NLS-1$
	}
	
	private void filterActUserSupport(ArrayList<Activity> activities, int i) {

		if(!(activities.get(i)).isPlayableOnThisDate(LocalDate.now())) {
			return;
		}
		
		
		Text eventName = new Text(activities.get(i).getName()+"\n"); //$NON-NLS-1$
		Log.getInstance().getLogger().info("\n\n"+activities.get(i).getName()+"\n\n"); //$NON-NLS-1$ //$NON-NLS-2$
		Text eventInfo;

		if(activities.get(i).getFrequency() instanceof ExpiringActivity) {
			eventInfo = new Text(activities.get(i).getPlace().getName()+
					"\nExpiring activity"+ //$NON-NLS-1$
					"\n"+activities.get(i).getFrequency().getOpeningTime()+ //$NON-NLS-1$
					"-"+activities.get(i).getFrequency().getClosingTime()); //$NON-NLS-1$
		}
		else if(activities.get(i).getFrequency() instanceof PeriodicActivity) {
			eventInfo = new Text(activities.get(i).getPlace().getName()+
					"\nPeriodic activity"+ //$NON-NLS-1$
					"\n"+activities.get(i).getFrequency().getOpeningTime()+ //$NON-NLS-1$
					"-"+activities.get(i).getFrequency().getClosingTime()); //$NON-NLS-1$
		}
		else{
			eventInfo = new Text(activities.get(i).getPlace().getName()+
				"\n Continuos activity"+ //$NON-NLS-1$
				"\n"+activities.get(i).getFrequency().getOpeningTime()+ //$NON-NLS-1$
				"-"+activities.get(i).getFrequency().getClosingTime()); //$NON-NLS-1$
		}
		///////////////////////////////////////////////////////////////////////////////////////////
			StackPane eventBox = setView(eventInfo, eventName, false, i, activities, null);
			
		eng.executeScript(SPOTPLACESCRIPT+"("+activities.get(i).getPlace().getLatitudine()+","+activities.get(i).getPlace().getLongitudine()+", \""+activities.get(i).getPlace().getName()+"\","+activities.get(i).getPlace().getId()+")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		eventsList.getItems().add(eventBox);
	}
}
