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


//import org.openstreetmap.gui.jmapviewer.JMapViewer;
//import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;
//import org.openstreetmap.gui.jmapviewer.interfaces.TileSource;

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
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
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
import javafx.scene.web.WebView;
import javafx.stage.Popup;
import javafx.stage.Stage;
import logic.controller.AddActivityToScheduleController;
import logic.controller.ChannelController;
import logic.controller.FindActivityController;
import logic.model.Activity;
import logic.model.CertifiedActivity;
import logic.model.NormalActivity;
import logic.model.Channel;
import logic.model.Coupon;
import logic.model.DAOActivity;
import logic.model.DAOChannel;
import logic.model.DAOCoupon;
import logic.model.DAOPlace;
import logic.model.DAOPreferences;
import logic.model.Discount;
import logic.model.ExpiringActivity;
import logic.model.FindActivitiesBean;
import logic.model.Log;
import logic.model.Partner;
import logic.model.PeriodicActivity;
import logic.model.PreferenceBean;
import logic.model.Preferences;
import logic.model.ScheduleBean;
import logic.model.SuperActivity;
import logic.model.SuperUser;
import logic.model.User;

public class HomeView implements Initializable{
	private static final  String BGCOLORKEY = "ffffff";
	private static final  String BGUCOLORKEY = "BC9416";
	private static final  String MAPPATHKEY = "http://localhost:8080/Dovado/MapView.html";
	//botton KEYS
	private static final  String BTNPREFKEY = "pref-btn";
	private static final  String BTNSRCKEY = "src-btn";
	private static final  String BTNEVNKEY = "evn-btn";
	private static long wPopup = 500;
	private static long hPopup = 50;
	
	private static StackPane lastEventBoxSelected;

	private static VBox chatContainer;
	
	private static Stage curr;
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
    private static WebEngine eng;
	
    private static int lastActivitySelected = -1;

    private static DAOActivity daoAct;
    private static DAOChannel daoCH;
    private static SuperActivity activitySelected;
    private static SuperUser user;
    private static double usrLat;
    private static double usrLon;
    private static ArrayList<Activity> activitiesToSpotUsr;
    private ArrayList<CertifiedActivity> activitiesToSpotPart;
    private static boolean searchByPreference;

    
    public static void render(Stage current) {
		try {
			VBox root = new VBox();
			BorderPane navbar = Navbar.getNavbar();
			Navbar.authenticatedSetup();
			
			VBox home = new VBox();
			
			Scene scene = new Scene(root,Navbar.getWidth(),Navbar.getHeight());
			scene.getStylesheets().add(Main.class.getResource("Dovado.css").toExternalForm());
			current.setTitle("Dovado - home");
			current.setScene(scene);
			home = FXMLLoader.load(Main.class.getResource("home.fxml"));
			VBox.setVgrow(home, Priority.SOMETIMES);
		
			root.getChildren().addAll(navbar,home);
			
			user=Navbar.getUser();
			curr=current;
			
			current.show();	
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		lastEventBoxSelected = null;
		daoAct = DAOActivity.getInstance();
		
		usrLat = 41.952928;
		usrLon = 12.518342;
        System.out.println("Coordinate della posizione attuale: "+usrLat+" "+usrLon);

        preference1.getStyleClass().add(BTNPREFKEY);
        preference1.setText("By preferences");
        
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
    	        searchButton.setText("SEARCH");
    			searchButton.getStyleClass().add(BTNSRCKEY); 
    			searchBar.setPromptText("Insert a 6 digit coupon code");    			
    			sliderText.setText("You can validate any coupon up here\nor join a channel from your activities");
    			
    			//Al partner non serve il distance selector, ne il filter by preference.
    			distanceSelected.setVisible(false);
    			distanceSelected.setManaged(false);
    			distanceSelector.setVisible(false);
    			distanceSelector.setManaged(false);
    			preference1.setVisible(false);
    			preference1.setManaged(false);
    			
    			if(!activitiesPartn.isEmpty()){
    				
    				int j;
    				for(j=0;j<activitiesPartn.size();j++)
    				Log.getInstance().getLogger().info("tutte le attivit� "+((SuperActivity)activitiesPartn.get(j)).getId());
    				
    				Thread newThread = new Thread(() -> {
    					int i;
    					for(i=0;i<activitiesPartn.size();i++) {
    						if(!activitiesPartn.get(i).isPlayableOnThisDate(LocalDate.now())) {
    							continue;
    						}
    						ImageView eventImage = new ImageView();
    						Text eventName = new Text(((SuperActivity)activitiesPartn.get(i)).getName()+"\n");
    						Log.getInstance().getLogger().info("\n\n"+((SuperActivity)activitiesPartn.get(i)).getName()+"\n\n");
    						Text eventInfo = new Text(((SuperActivity)activitiesPartn.get(i)).getPlace().getName()+
    								"\n"+((SuperActivity)activitiesPartn.get(i)).getFrequency().getOpeningTime()+
    								"-"+((SuperActivity)activitiesPartn.get(i)).getFrequency().getClosingTime());
    						eventImage.setImage(new Image("https://source.unsplash.com/user/erondu/290x120"));
    						eventImage.getStyleClass().add("event-image");
    						
    						eventInfo.setId("eventInfo");
    						eventInfo.getStyleClass().add("textEventInfo");
    						eventInfo.setWrappingWidth(280);
	
    						eventName.setId("eventName");
    						eventName.getStyleClass().add("textEventName");
    						eventName.setWrappingWidth(280);

    						VBox eventText = new VBox(eventName,eventInfo);
    						eventText.setAlignment(Pos.CENTER_LEFT);
    						eventText.getStyleClass().add("eventTextVbox");
    						//Preparo un box in cui contenere il nome dell'attivit� e altre sue
    						//informazioni; uso uno StackPane per poter mettere scritte su immagini.
    						StackPane eventBox = new StackPane();
    						eventBox.getStyleClass().add("eventBox");
    						
    						Text eventId = new Text();
    						Text placeId = new Text();
    						
    						eventId.setId(((SuperActivity)activitiesPartn.get(i)).getId().toString());
    						placeId.setId(((SuperActivity)activitiesPartn.get(i)).getPlace().getId().toString());
    						
    						//Aggiungo allo stack pane l'id dell'evento, quello del posto, l'immagine
    						//dell'evento ed infine il testo dell'evento.
    						eventBox.getChildren().add(eventId);
    						eventBox.getChildren().add(placeId);
    						eventBox.getChildren().add(eventImage);
    						eventBox.getChildren().add(eventText);
    						//Se l'attivit� � certificata aggiungo un logo in alto a
    						//destra per indicarlo.
    						if(activitiesPartn.get(i) instanceof CertifiedActivity) {
    							eventName.getStyleClass().clear();
    							eventName.getStyleClass().add("certEventName");
    							eventName.setText(eventName.getText()+'\n'+"CERTIFICATA");
    						}	
    						//Stabilisco l'allineamento ed in seguito lo aggiungo alla lista di eventi.
    						eventBox.setAlignment(Pos.CENTER);

    						if(!activitiesPartn.get(i).isOpenOnThisTime(LocalTime.now())) {
    							eventBox.setOpacity(0.4);
    							Text closed = new Text("CLOSED NOW");
    							closed.getStyleClass().add("textEventInfo");
    							closed.setTextAlignment(TextAlignment.CENTER);
    							eventBox.getChildren().add(closed);
    						}
    						eventsList.getItems().add(eventBox);
    						
    					}
    				});
    				newThread.start();
    				eventsList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    				newThread.join();
    			} else {
    				StackPane infoBox = new StackPane();
    				Text noPrefs = new Text("You have not set"+'\n'+"any preferences yet!");
    				noPrefs.setTextAlignment(TextAlignment.CENTER);
    				
    				infoBox.getChildren().add(noPrefs);
    				noPrefs.getStyleClass().add("textEventName");
    				
    				eventsList.getItems().add(infoBox);
    			}
    		}catch(Error e) {	Log.getInstance().getLogger().warning(e.getMessage());
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    				Log.getInstance().getLogger().info(e.getMessage());
    			} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    	}
    	else {
	    	Log.getInstance().getLogger().info("Ok \nWorking Directory = " + System.getProperty("user.dir"));		
			try{
				ArrayList<Activity> activities = new ArrayList<>();
				activitiesToSpotUsr = activities;
				eng = map.getEngine();
				
				// Setting permissions to interact with Js
		        eng.setJavaScriptEnabled(true);
		        eng.load(MAPPATHKEY);
		        
		        searchButton.setText("SEARCH");
				searchButton.getStyleClass().add(BTNSRCKEY);
				int dist = (int) Math.round(distanceSelector.getMin());
				distanceSelected.setText(Integer.toString(dist));
    			
				preference1.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						final Popup popup;
						if(searchByPreference==false) {
							searchByPreference=true;
							popup = popupGen(wPopup,hPopup,"Your searched activities will be filtered by preference!");
							
						}
						else {
							searchByPreference=false;
							popup = popupGen(wPopup,hPopup,"Your searched activities will not be filtered by preference!");
						}
						popup.centerOnScreen();
						
						popup.setHideOnEscape(true);
						popup.show(curr);
					}
					
				});
				//Al posto di scegliere preferenze casuali
				//e mostrarne i risultati prendo le preferenze dell'utente e 
				//in base a quello restituisco risultati appropriati.
				
				Preferences preferences = ((User)Navbar.getUser()).getPreferences();
			
				if(preferences!=null){
						
					activities.addAll(daoAct.getNearbyActivities(usrLat,usrLon,100));
					
					int j;
					for(j=0;j<activities.size();j++)
					Log.getInstance().getLogger().info("tutte le attivit� "+activities.get(j).getId());
					
					Thread newThread = new Thread(() -> {
						int i;
						for(i=0;i<activities.size();i++) {
							if(activities.get(i) instanceof NormalActivity) {
								if(!((NormalActivity)activities.get(i)).isPlayableOnThisDate(LocalDate.now())) {
	    							continue;
	    						}
							} else {
								if(!((CertifiedActivity)activities.get(i)).isPlayableOnThisDate(LocalDate.now())) {
	    							continue;
	    						}
							}
							ImageView eventImage = new ImageView();
							Text eventName = new Text(activities.get(i).getName()+"\n");
							Log.getInstance().getLogger().info("\n\n"+activities.get(i).getName()+"\n\n");
							Text eventInfo;

							if(activities.get(i).getFrequency() instanceof ExpiringActivity) {
								eventInfo = new Text(activities.get(i).getPlace().getName()+
										"\n Expiring activity"+
										"\n"+activities.get(i).getFrequency().getOpeningTime()+
										"-"+activities.get(i).getFrequency().getClosingTime());
							}
							else if(activities.get(i).getFrequency() instanceof PeriodicActivity) {
								eventInfo = new Text(activities.get(i).getPlace().getName()+
										"\n Periodic activity"+
										"\n"+activities.get(i).getFrequency().getOpeningTime()+
										"-"+activities.get(i).getFrequency().getClosingTime());
							}
							else{
								eventInfo = new Text(activities.get(i).getPlace().getName()+
									"\n Continuos activity"+
									"\n"+activities.get(i).getFrequency().getOpeningTime()+
									"-"+activities.get(i).getFrequency().getClosingTime());
							}
							
							eventImage.setImage(new Image("https://source.unsplash.com/user/erondu/290x120"));
							eventImage.getStyleClass().add("event-image");
							
							eventInfo.setId("eventInfo");
							eventInfo.getStyleClass().add("textEventInfo");
							eventInfo.setWrappingWidth(280);
					
							eventName.setId("eventName");
							eventName.getStyleClass().add("textEventName");
							eventName.setWrappingWidth(280);
					
							VBox eventText = new VBox(eventName,eventInfo);
							eventText.setAlignment(Pos.CENTER_LEFT);
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
							if(activities.get(i) instanceof CertifiedActivity) {
								eventName.getStyleClass().clear();
								eventName.getStyleClass().add("certEventName");
								eventName.setText(eventName.getText()+'\n'+"CERTIFICATA");
							}	
							//Stabilisco l'allineamento ed in seguito lo aggiungo alla lista di eventi.
							eventBox.setAlignment(Pos.CENTER);
							if(activities.get(i) instanceof NormalActivity){
								if(!((NormalActivity)(activities.get(i))).isOpenOnThisTime(LocalTime.now())) {
									eventBox.setOpacity(0.4);
	    							Text closed = new Text("CLOSED NOW");
	    							closed.getStyleClass().add("textEventInfo");
	    							closed.setTextAlignment(TextAlignment.CENTER);
	    							eventBox.getChildren().add(closed);
	    						}
								eventsList.getItems().add(eventBox);
							} else {
								if(!((CertifiedActivity)(activities.get(i))).isOpenOnThisTime(LocalTime.now())) {
									eventBox.setOpacity(0.4);
	    							Text closed = new Text("CLOSED NOW");
	    							closed.getStyleClass().add("textEventInfo");
	    							closed.setTextAlignment(TextAlignment.CENTER);
	    							eventBox.getChildren().add(closed);
								}
								eventsList.getItems().add(eventBox);
							}
						}
					});
					newThread.start();
					eventsList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
					newThread.join();
				} else {
					StackPane infoBox = new StackPane();
					Text noPrefs = new Text("You have not set"+'\n'+"any preferences yet!");
					noPrefs.setTextAlignment(TextAlignment.CENTER);
					
					infoBox.getChildren().add(noPrefs);
					noPrefs.getStyleClass().add("textEventName");
					
					eventsList.getItems().add(infoBox);
				}
			}catch(Error e) {	Log.getInstance().getLogger().warning(e.getMessage());
				} catch (InterruptedException e) {
					e.printStackTrace();
					Log.getInstance().getLogger().info(e.getMessage());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    	}
    }

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
			for(Activity curr:activitiesToSpotUsr) {
				eng.executeScript("spotPlace('"+curr.getPlace().getLatitudine()+"','"+curr.getPlace().getLongitudine()+"', \""+curr.getPlace().getName()+"\",'"+curr.getPlace().getId()+"')");
			}
			eng.executeScript("setUser('"+usrLat+"','"+usrLon+"')");
		}
		else {
			for(CertifiedActivity curr:activitiesToSpotPart) {
				eng.executeScript("spotPlace('"+curr.getPlace().getLatitudine()+"','"+curr.getPlace().getLongitudine()+"', \""+curr.getPlace().getName()+"\",'"+curr.getPlace().getId()+"')");
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
		
		Log.getInstance().getLogger().info(eventName+" "+eventDetails);
		
		Text activityDescription= new Text();
		VBox selection = new VBox();
		Button viewOnMap = new Button();
		Button joinActivityChannel = new Button();
		
		viewOnMap.setText("View event");
		joinActivityChannel.setText("Join channel");
		
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
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		Log.getInstance().getLogger().info("Attivit� trovata: "+activitySelected);

		if(activitySelected.getFrequency() instanceof PeriodicActivity) {
			activityDescription.setText("A periodic activity with a: "+
		((PeriodicActivity)(activitySelected.getFrequency())).getCadence().toString()+
			" cadence\n\nOpen from the date:\n"+((PeriodicActivity)(activitySelected.getFrequency())).getFormattedStartDate()+
			"\nTo the date:\n"+((PeriodicActivity)(activitySelected.getFrequency())).getFormattedEndDate()+"\n\n");

			activityDescription.setText(activityDescription.getText()+"Description:\n"+activitySelected.getDescription());
		} 
		else if(activitySelected.getFrequency() instanceof ExpiringActivity) {
				activityDescription.setText("An expiring activity that goes from the date:\n"+
						((ExpiringActivity)(activitySelected.getFrequency())).getFormattedStartDate()+
				"\nTo the date:\n"+((ExpiringActivity)(activitySelected.getFrequency())).getFormattedEndDate()+"\n\n");

				activityDescription.setText(activityDescription.getText()+"Description:\n"+activitySelected.getDescription());
		}
		else {
			activityDescription.setText(activitySelected.getDescription());
		}
		activityDescription.setWrappingWidth(280);
		activityDescription.getStyleClass().add("actInfo");
		
		selection.getChildren().add(0,activityDescription);
		
		viewOnMap.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				eng.executeScript("spotPlace('"+activitySelected.getPlace().getLatitudine()+"','"+activitySelected.getPlace().getLongitudine()+"', \""+activitySelected.getPlace().getName()+"\",'"+activitySelected.getPlace().getId()+"')");
					eng.executeScript("moveView('"+activitySelected.getPlace().getLatitudine()+"','"+activitySelected.getPlace().getLongitudine()+"','"+activitySelected.getId()+"')");
					
				}
		});
		
		joinActivityChannel.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
					if(root.getChildren().get(root.getChildren().size()-1).getId()=="activityCh") {
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
							Log.getInstance().getLogger().info("\n\nInviando un messaggio...\n");
							Log.getInstance().getLogger().info("\nMessaggi prima dell'invio:\n");
							for(int j=0;j<activitySelected.getChannel().getChat().size();j++) {
								Log.getInstance().getLogger().info(activitySelected.getChannel().getChat().get(j).getMsgText());
							}
							activitySelected.getChannel().addMsg(user.getUsername(), mss.getText());
							
							ChannelController c = new ChannelController(user, activitySelected.getId());
							try {
								c.sendMessage(mss.getText());
							} catch (ClassNotFoundException | SQLException e1) {
								final Popup popup = popupGen(wPopup,hPopup,"Message not sent due to DB error");
								popup.centerOnScreen(); 
							    
							    popup.show(curr);
							    popup.setAutoHide(true);
								
								e1.printStackTrace();
								return;
							}
							Log.getInstance().getLogger().info("\nMessaggi dopo l'invio:\n");

							for(int j=0;j<activitySelected.getChannel().getChat().size();j++) {
								Log.getInstance().getLogger().info(activitySelected.getChannel().getChat().get(j).getMsgText());
							}
							updateChat(chat,activitySelected.getChannel());
							mss.clear();
						}
					});
					
					send.setText("Send");
					send.getStyleClass().add(BTNSRCKEY);
					
					close.setText("x");
					close.getStyleClass().add(BTNSRCKEY);					
					close.setAlignment(Pos.TOP_RIGHT);
					
					textAndSend.getChildren().addAll(mss,send);
					chatContainer.getChildren().addAll(close,chat,textAndSend);
					chatContainer.setAlignment(Pos.BOTTOM_RIGHT);
					chatContainer.setId("activityCh");
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
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									Log.getInstance().getLogger().info("Refreshing messages...");
									updateChat(chat,activitySelected.getChannel());
								}
							});
						}
						
					},0, 10000);
				}
		});
		
		if(user instanceof User) {
			Button playActivity = new Button();
			playActivity.setText("Play activity");
			playActivity.getStyleClass().add(BTNEVNKEY);
			
			selection.getChildren().add(playActivity);
			
			playActivity.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent e) {
						VBox selectedBox = (VBox)eventsList.getItems().get(lastActivitySelected+1);			
						
						if(selectedBox.getChildren().get(selectedBox.getChildren().size()-1).getId()=="dateBox") {
							return;
						}
						//Apro un pop up in cui si può scegliere una
						//Data in cui svolgere l'attività
						DatePicker pickDate = new DatePicker();
						DatePicker pickDateReminder = new DatePicker();
						ChoiceBox<String> hourBox = new ChoiceBox<>();
						ChoiceBox<String> minBox = new ChoiceBox<>();
						
						ChoiceBox<String> hourBox2 = new ChoiceBox<>();
						ChoiceBox<String> minBox2 = new ChoiceBox<>();
	
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
							if(i<10) {
								hr = "0"+hr;
							}
							hourBox.getItems().add(hr);
						}
						
						hourBox.setOnAction(new EventHandler<ActionEvent>() {
							@Override public void handle(ActionEvent e) {
								minBox.getItems().clear();
								int selectedHour = Integer.parseInt(hourBox.getValue());
								if(selectedHour==lowerLimit) {
									for(int j=lowerLimMin;j<61;j++) {
										String min = Integer.toString(j);
										if(j<10) {
											min = "0"+min;
										}
										minBox.getItems().add(min);
										
									}
									return;
								}
								if(selectedHour==upperLimit) {
									for(int j=0;j<=upperLimMin;j++) {
										String min = Integer.toString(j);
										if(j<10) {
											min = "0"+min;
										}
										minBox.getItems().add(min);
									}
									
								}
								else {
									for(int j=0;j<61;j++) {
										String min = Integer.toString(j);
										if(j<10) {
											min = "0"+min;
										}
										minBox.getItems().add(min);
									}
									
								}
									
							}
						});
						
						for(int i=lowerLimit;i<=upperLimit;i++) {
							String hr = Integer.toString(i);
							if(i<10) {
								hr = "0"+hr;
							}
							hourBox2.getItems().add(hr);
						}
						
						hourBox2.setOnAction(new EventHandler<ActionEvent>() {
							@Override public void handle(ActionEvent e) {
								minBox2.getItems().clear();
								int selectedHour = Integer.parseInt(hourBox.getValue());
								if(selectedHour==lowerLimit) {
									for(int j=lowerLimMin;j<61;j++) {
										String min = Integer.toString(j);
										if(j<10) {
											min = "0"+min;
										}
										minBox2.getItems().add(min);
										
									}
									return;
								}
								if(selectedHour==upperLimit) {
									for(int j=0;j<=upperLimMin;j++) {
										String min = Integer.toString(j);
										if(j<10) {
											min = "0"+min;
										}
										minBox2.getItems().add(min);
									}
									
								}
								else {
									for(int j=0;j<61;j++) {
										String min = Integer.toString(j);
										if(j<10) {
											min = "0"+min;
										}
										minBox2.getItems().add(min);
									}
									
								}
									
							}
						});
						
						Text txt = new Text("Select date");
						Button ok = new Button();
						Button close = new Button();
						
						VBox dateBox = new VBox();
						ok.setText("Ok");
						ok.getStyleClass().add(BTNSRCKEY);
						
						HBox buttonBox = new HBox();
						HBox pickTimeBox = new HBox();
						HBox pickReminderBox = new HBox();
						
						Text txtTime = new Text("Select scheduled time!");
						Text txtReminder = new Text("|OPTIONAL|"+'\n'+"Select a time reminder"+'\n'+"for the day scheduled or specify the"+'\n'+"day we should remind you.");
						
						buttonBox.getChildren().addAll(close,ok);
						
						CornerRadii cr = new CornerRadii(4);
						BackgroundFill bf = new BackgroundFill(Paint.valueOf(BGCOLORKEY), cr, null);
						Background b = new Background(bf);
						
						txt.getStyleClass().add("msstxt");
						
						pickReminderBox.getChildren().addAll(hourBox2,minBox2);
						pickTimeBox.getChildren().addAll(hourBox,minBox);
						
						dateBox.setBackground(b);
						dateBox.getChildren().addAll(txt,pickDate,txtTime,pickTimeBox,txtReminder,pickDateReminder,pickReminderBox);
						dateBox.setId("dateBox");
						
						ChoiceBox<String> percDiscount = new ChoiceBox<String>();
						
						if(activitySelected instanceof CertifiedActivity) {
							Text activityPrice = new Text(((CertifiedActivity)activitySelected).getPrice());
							Text discountDescription;
							try {
								
								ArrayList<Discount> discList = daoAct.viewDiscounts(activityId);
								if (discList==null || discList.isEmpty()) {
									discountDescription = new Text("No discount available"+'\n'+" for this activity.");
								}else {
									discountDescription = new Text("Pick a discount if you want.");
								}
								percDiscount.getItems().add("0% - 0�");
								for(int i=0;i<discList.size();i++) {
									percDiscount.getItems().add(Integer.toString(discList.get(i).getPercentuale())+"% - "+Integer.toString(discList.get(i).getPrice())+"�");
								}
								dateBox.getChildren().addAll(activityPrice,discountDescription,percDiscount);								
							
							}
							catch(Exception e2) {
								Log.getInstance().getLogger().info("Database error, discounts not found.");
								e2.printStackTrace();
							}
						}
						dateBox.getChildren().add(buttonBox);
						selectedBox.getChildren().add(dateBox);
						
						close.setText("Close");
						close.getStyleClass().add(BTNSRCKEY);					
						
						close.setOnAction(new EventHandler<ActionEvent>(){
							@Override public void handle(ActionEvent e) {
								selectedBox.getChildren().remove(dateBox);
							}
						});
						
						ok.setOnAction(new EventHandler<ActionEvent>(){
							@Override public void handle(ActionEvent e) {
								if(pickDate.getValue().toString().isBlank() || hourBox.getValue().isBlank() || minBox.getValue().isBlank()) {
									Log.getInstance().getLogger().info("Non avendo inserito abbastanza prenotazioni non si effettuano modifiche.");
									final Popup popup = popupGen(wPopup,hPopup,"You haven't specified enough info."); 
									popup.centerOnScreen(); 
								    
								    popup.show(curr);
								    popup.setAutoHide(true);
								}
							    DateTimeFormatter day = DateTimeFormatter.ofPattern("yyyy-MM-dd");
								String dayStringed = day.format(pickDate.getValue());
																								
								dayStringed.split("-");
								
								String hourChosen = hourBox.getValue();
								String minChosen = minBox.getValue();
								String dateReminderChosen;
								String hourReminder;
								if (hourBox2.getValue()==null || minBox2.getValue()==null) {
									int hourReminderInt = Integer.parseInt(hourChosen);
									hourReminder = Integer.toString(hourReminderInt-1);
							
									Log.getInstance().getLogger().info("Non avendo specificato un orario si setta di predefinito un'ora prima della prenotazione");
									final Popup popup = popupGen(wPopup,hPopup,"You haven't specified a time for your reminder... setting to 1 hour before the scheduled event"); 
									popup.centerOnScreen(); 
								    
								    popup.show(curr);
								    popup.setAutoHide(true);
									
									if(hourReminderInt-1<10) {
										hourReminder = "0"+hourReminder;
									}
								} else {
									hourReminder = hourBox2.getValue();
									minBox2.getValue();
								}
								if(pickDateReminder.getValue().toString().isBlank()) {
									Log.getInstance().getLogger().info("Non avendo specificato un orario si setta di predefinito il giorno stesso della prenotazione");
									dateReminderChosen=dayStringed;
									final Popup popup = popupGen(wPopup,hPopup,"You haven't specified a day for your reminder... setting to 1 day before the scheduled event"); 
									popup.centerOnScreen(); 
								    
								    popup.show(curr);
								    popup.setAutoHide(true);
								} else {dateReminderChosen = day.format(pickDateReminder.getValue());}
								String dateChosen = dayStringed;

								ScheduleBean sb = new ScheduleBean();
								
								if(dateBox.getChildren().contains(percDiscount)) {
									String[] percPrice = percDiscount.getValue().split("% - ");
									int percRequested = Integer.valueOf(percPrice[0]);
									
									String priceString = (percPrice[1].split("�"))[0];
									int pricePayed = Integer.valueOf(priceString);
									
									if(pricePayed > ((User)user).getBalance()) {
										Log.getInstance().getLogger().info("Not enough dovado $ for payment");
										final Popup popup = popupGen(wPopup,hPopup,"Not enough Dovado $ for payment"); 
										popup.centerOnScreen(); 
									    
									    popup.show(curr);
									    popup.setAutoHide(true);
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
								
								if(activitySelected instanceof CertifiedActivity) {
									try {
										sc.addCertifiedActivityToSchedule();
									} catch (Exception e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}else {
									try {
										sc.addActivityToSchedule();
									} catch (Exception e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
								final Popup popup = popupGen(wPopup,hPopup,"Activity successfully scheduled"); 
								popup.centerOnScreen(); 
							    
							    popup.show(curr);
							    popup.setAutoHide(true);
				                
							}
						});
						
					}
			});
		}
		else {
			Button deleteActivity = new Button();
			deleteActivity.setText("Delete activity");
			deleteActivity.getStyleClass().add(BTNEVNKEY);
			
			selection.getChildren().add(deleteActivity);
			
			deleteActivity.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					final Popup popup = popupGen(wPopup,hPopup,"Activity deleted correctly!"); 
					popup.centerOnScreen();
						 
				    popup.show(curr);
				    popup.setAutoHide(true);
					    
					Log.getInstance().getLogger().info("Attività cancellata dalla persistenza");
					activityDeselected(lastEventBoxSelected,true);
				}
			});
			
		}
	}

	
	
	private void updateChat(ListView chat, Channel ch) {
		int i;
		chat.getItems().clear();
		try {
			ch = daoCH.getChannel(ch.getActivityReferenced());
		} catch (ClassNotFoundException | SQLException e) {
			final Popup popup = popupGen(wPopup,hPopup,"Unable to get chat.");
			popup.centerOnScreen(); 
		    
		    popup.show(curr);
		    popup.setAutoHide(true);
		    
			e.printStackTrace();
			return;
		}
		//ONCE THE ACTIVITY HAS HIS NEWLY UPDATED CHANNEL THE CHAT WILL BE 
		//UPDATED.
		activitySelected.setChannel(ch);
		for(i=0;i<ch.getChat().size();i++) {
			//TODO qui posso nominarla in altro modo la VBOX?
			VBox chatContainer = new VBox();
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
			
			username.getStyleClass().add("mssusr");
			msstxt.getStyleClass().add("msstxt");
			dateSent.getStyleClass().add("msssent");
			
			chatContainer.getChildren().addAll(username,msstxt,dateSent);
			chatContainer.setMaxWidth(root.getWidth()/2);
			
			chatMss.getChildren().add(chatContainer);
			chatMss.autosize();
			
			System.out.println("Messaggi ricevuti: "+chatMss);
			
			if(user.getUsername().equals(usernameMss)) {
				CornerRadii cr = new CornerRadii(8);
			
				BackgroundFill bf = new BackgroundFill(Paint.valueOf(BGUCOLORKEY ), cr, null);
				Background b = new Background(bf);
				
				chatContainer.setBackground(b);
				chatContainer.setAlignment(Pos.CENTER_RIGHT);
				
				chatMss.setAlignment(Pos.CENTER_RIGHT);
			}
			else {
				CornerRadii cr = new CornerRadii(8);
				BackgroundFill bf = new BackgroundFill(Paint.valueOf(BGCOLORKEY), cr, null);
				Background b = new Background(bf);
				
				chatContainer.setBackground(b);
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
		if(delete==true) {
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
			int couponCode;
			try {
			couponCode = Integer.parseInt(searchBar.getText());
			} catch(NumberFormatException ne) {
				final Popup popup = popupGen(wPopup,hPopup,"Insert a 6 digit NUMBER");
				popup.centerOnScreen(); 
			    
			    popup.show(curr);
			    popup.setAutoHide(true);
			    return;
			}
			try{ 
				//Con il metodo sottostante mi assicuro della presenza del coupon.
				Coupon cToRedeem;
				if((cToRedeem = DAOCoupon.getInstance().findCouponPartner(couponCode))==null) {
					final Popup popup = popupGen(wPopup,hPopup,"No such coupon found!");
					popup.centerOnScreen(); 
				    
				    popup.show(curr);
				    popup.setAutoHide(true);
				    return;
				}
				//Una volta trovato eseguo il codice necessario a redimerlo.
				AddActivityToScheduleController actSched = new AddActivityToScheduleController((Partner)(Navbar.getUser()), searchBar.getText());
				actSched.redeemCoupon();
			} 
			catch(Exception e){
				//Si cattura un'eccezione se trovata e si avverte l'utente				
				final Popup popup = popupGen(wPopup,hPopup,"Due to an issue the coupon was not redeemed.");
				popup.centerOnScreen(); 
			    
			    popup.show(curr);
			    popup.setAutoHide(true);
			    e.printStackTrace();
			    return;
			}
			final Popup popup = popupGen(wPopup,hPopup,"Coupon redeemed correctly");
			popup.centerOnScreen(); 
		    
		    popup.show(curr);
		    popup.setAutoHide(true);
		    searchBar.setText("");
		    searchBar.setPromptText("Insert a 6 digit coupon code");
		    return;
		}
		
		lastActivitySelected = -1;
		String searchItem = null;
		
		ArrayList<Activity> activities = new ArrayList<Activity>();
		if((searchItem = searchBar.getText())==null) return; 

		eng.executeScript("removeAllMarkers()");
		String[] keywords = searchItem.split(";");
		activities = new ArrayList<>();
		try {
			//Eseguo un controllo sulla ricerca delle attività; se il risultato è un'arraylist vuoto, allora
			//segnalo l'errore e esco dal metodo.
			activities = daoAct.getNearbyActivities(usrLat, usrLon, Float.parseFloat(distanceSelected.getText()));
			if(searchByPreference) {
				FindActivityController.filterActivitiesByPreferences(activities,((User)user).getPreferences());
			}
			FindActivityController.filterActivitiesByKeyWords(activities, keywords);
			if(activities.isEmpty() ) {
				Log.getInstance().getLogger().info("Nothing was found!");
				final Popup popup = popupGen(wPopup,hPopup,"Nothing has been found"); 
				popup.centerOnScreen(); 
			    
			    popup.show(curr);
			    popup.setAutoHide(true);
				return;
			}
			
		} catch (Exception e) {
			Log.getInstance().getLogger().info("La ricerca delle attività non è andata a buon fine. \n per colpa di un errore nel metodo del DB.");
			e.printStackTrace();
			return;
		}
		eventsList.getItems().clear();
		
		int i;
		for(i=0;i<activities.size();i++) {
			if(activities.get(i) instanceof NormalActivity) {
				if(!((NormalActivity)activities.get(i)).isPlayableOnThisDate(LocalDate.now())) {
					continue;
				}
			} else {
				if(!((CertifiedActivity)activities.get(i)).isPlayableOnThisDate(LocalDate.now())) {
					continue;
				}
			}
			ImageView eventImage = new ImageView();
			Text eventName = new Text(activities.get(i).getName()+"\n");
			Log.getInstance().getLogger().info("\n\n"+activities.get(i).getName()+"\n\n");
			Text eventInfo;
	
			if(activities.get(i).getFrequency() instanceof ExpiringActivity) {
				eventInfo = new Text(activities.get(i).getPlace().getName()+
						"\nExpiring activity"+
						"\n"+activities.get(i).getFrequency().getOpeningTime()+
						"-"+activities.get(i).getFrequency().getClosingTime());
			}
			else if(activities.get(i).getFrequency() instanceof PeriodicActivity) {
				eventInfo = new Text(activities.get(i).getPlace().getName()+
						"\nPeriodic activity"+
						"\n"+activities.get(i).getFrequency().getOpeningTime()+
						"-"+activities.get(i).getFrequency().getClosingTime());
			}
			else{
				eventInfo = new Text(activities.get(i).getPlace().getName()+
					"\n Continuos activity"+
					"\n"+activities.get(i).getFrequency().getOpeningTime()+
					"-"+activities.get(i).getFrequency().getClosingTime());
			}
			eventImage.setImage(new Image("https://source.unsplash.com/user/erondu/290x120"));
	
			eventInfo.setId("eventInfo");
			eventInfo.getStyleClass().add("textEventInfo");
			eventInfo.setWrappingWidth(280);

			eventName.setId("eventName");
			eventName.getStyleClass().add("textEventName");
			eventName.setWrappingWidth(280);
			
			VBox eventText = new VBox(eventName,eventInfo);
			eventText.setAlignment(Pos.CENTER_LEFT);
			
			//Preparo un box in cui contenere il nome dell'attivit� e altre sue
			//informazioni; uso uno StackPane per poter mettere scritte su immagini.
			StackPane eventBox = new StackPane();
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
			//Se l'attivit� � certificata aggiungo un logo in alto a
			//destra per indicarlo.
			if(activities.get(i) instanceof CertifiedActivity) {
	
				eventName.getStyleClass().clear();
				eventName.getStyleClass().add("certEventName");
				eventName.setText(eventName.getText()+'\n'+"CERTIFICATA");
				
			}	
			//Stabilisco l'allineamento ed in seguito lo aggiungo alla lista di eventi.
			eventBox.setAlignment(Pos.CENTER);
			if(activities.get(i) instanceof NormalActivity){
				if(!((NormalActivity)(activities.get(i))).isOpenOnThisTime(LocalTime.now())) {
					eventBox.setOpacity(0.4);
					Text closed = new Text("CLOSED NOW");
					closed.getStyleClass().add("textEventInfo");
					closed.setTextAlignment(TextAlignment.CENTER);
					eventBox.getChildren().add(closed);
				}
			} else {
				if(!((CertifiedActivity)(activities.get(i))).isOpenOnThisTime(LocalTime.now())) {
					eventBox.setOpacity(0.4);
					Text closed = new Text("CLOSED NOW");
					closed.getStyleClass().add("textEventInfo");
					closed.setTextAlignment(TextAlignment.CENTER);
					eventBox.getChildren().add(closed);
				}
			}
			eng.executeScript("spotPlace("+activities.get(i).getPlace().getLatitudine()+","+activities.get(i).getPlace().getLongitudine()+", \""+activities.get(i).getPlace().getName()+"\","+activities.get(i).getPlace().getId()+")");;
			eventsList.getItems().add(eventBox);
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
	    r.setStroke(Paint.valueOf(BGCOLORKEY));
	    
	    popup.getContent().add(popupContent);
	    return popup;
	}
}
