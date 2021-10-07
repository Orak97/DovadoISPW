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
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import logic.model.Channel;
import logic.model.DAOActivity;
import logic.model.DAOChannel;
import logic.model.DAOPlace;
import logic.model.DAOPreferences;
import logic.model.DAOSuperUser;
import logic.model.Log;
import logic.model.Partner;
import logic.model.Place;
import logic.model.SuperActivity;
import logic.model.SuperUser;
import logic.model.User;

public class HomeView implements Initializable{
	private static final  String BGCOLORKEY = "ffffff";
	private static final  String MAPPATHKEY = "file:/home/pgs/Documents/GitHub/DovadoISPW/WebContent/map.html";
	//botton KEYS
	private static final  String BTNPREFKEY = "pref-btn";
	private static final  String BTNSRCKEY = "src-btn";
	private static final  String BTNEVNKEY = "evn-btn";
	
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
    private Button searchButton;
    
    @FXML
    private Button preference1;

    @FXML
    private Button preference2;
    
    @FXML
    private Button preference3;

    @FXML
    private ListView<Object> eventsList;
    
    @FXML
    private WebView map;

    @FXML
    private WebEngine eng;
    
    private static int lastActivitySelected = -1;

    private static DAOPreferences daoPref;
    private static DAOActivity daoAct;
    private static DAOSuperUser daoSU;
    private static DAOChannel daoCH;
    private static DAOPlace daoPlc;
    private static SuperActivity activitySelected;
    private static SuperUser user;
    
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
		daoPref = DAOPreferences.getInstance();
		daoAct = DAOActivity.getInstance();
		daoSU = DAOSuperUser.getInstance();
		
    	user = Navbar.getUser();
    	if(user instanceof Partner) {
    		
	    	try{
	    		//Al posto di scegliere preferenze casuali
				//e mostrarne i risultati prendo le attività del partner e 
				//in base a quello restituisco risultati appropriati.
				
	    		ArrayList<SuperActivity> activitiesPartn = (ArrayList<SuperActivity>) daoAct.findActivitiesByPartner(daoSU,(Partner)user);
				
    			eng = map.getEngine();
    			eng.load(MAPPATHKEY);
    			
    			// Setting permissions to interact with Js
    	        eng.setJavaScriptEnabled(true);
    	        
    	        searchButton.setText("SEARCH");
    			searchButton.getStyleClass().add(BTNSRCKEY);
    	   
    	        preference1.setText(daoPref.getPreferenceFromJSON(1));
    			preference2.setText(daoPref.getPreferenceFromJSON(2));
    			preference3.setText(daoPref.getPreferenceFromJSON(3));

    			preference1.getStyleClass().add(BTNPREFKEY);
    			preference2.getStyleClass().add(BTNPREFKEY);
    			preference3.getStyleClass().add(BTNPREFKEY);
    			
    			if(!activitiesPartn.isEmpty()){
    				
    				int j;
    				for(j=0;j<activitiesPartn.size();j++)
    				Log.getInstance().getLogger().info("tutte le attivit� "+activitiesPartn.get(j).getId());
    				
    				Thread newThread = new Thread(() -> {
    					int i;
    					for(i=0;i<activitiesPartn.size();i++) {
    						ImageView eventImage = new ImageView();
    						Text eventName = new Text(activitiesPartn.get(i).getName()+"\n");
    						Log.getInstance().getLogger().info("\n\n"+activitiesPartn.get(i).getName()+"\n\n");
    						Text eventInfo = new Text(activitiesPartn.get(i).getPlace().getName()+
    								"\n"+activitiesPartn.get(i).getFrequency().getOpeningTime()+
    								"-"+activitiesPartn.get(i).getFrequency().getClosingTime());
    						eventImage.setImage(new Image("https://source.unsplash.com/user/erondu/200x100"));
    						eventImage.getStyleClass().add("event-image");
    						
    						eventInfo.setId("eventInfo");
    						eventInfo.getStyleClass().add("textEventInfo");
    				/*		eventInfo.setTextAlignment(TextAlignment.LEFT);
    						eventInfo.setFont(Font.font("Monserrat-Black", FontWeight.EXTRA_LIGHT, 12));
    						eventInfo.setFill(Paint.valueOf("#BGCOLOR"));
    						eventInfo.setStrokeWidth(0.3);
    						eventInfo.setStroke(Paint.valueOf("#000000"));
    					*/	
    						eventName.setId("eventName");
    						eventName.getStyleClass().add("textEventName");
    						/*eventName.setFont(Font.font("Monserrat-Black", FontWeight.BLACK, 20));
    						eventName.setFill(Paint.valueOf("#BGCOLOR"));
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
    						
    						eventId.setId(activitiesPartn.get(i).getId().toString());
    						placeId.setId(activitiesPartn.get(i).getPlace().getId().toString());
    						
    						//Aggiungo allo stack pane l'id dell'evento, quello del posto, l'immagine
    						//dell'evento ed infine il testo dell'evento.
    						eventBox.getChildren().add(eventId);
    						eventBox.getChildren().add(placeId);
    						eventBox.getChildren().add(eventImage);
    						eventBox.getChildren().add(eventText);
    						//Stabilisco l'allineamento ed in seguito lo aggiungo alla lista di eventi.
    						eventBox.setAlignment(Pos.CENTER);
    						
    						eventsList.getItems().add(eventBox);
    						
    						//eng.executeScript("");
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
    			}
    	}
    	else {
	    	Log.getInstance().getLogger().info("Ok \nWorking Directory = " + System.getProperty("user.dir"));		
			try{
				ArrayList<SuperActivity> activities = new ArrayList<>();
				
				eng = map.getEngine();
				eng.load(MAPPATHKEY);
				
				// Setting permissions to interact with Js
		        eng.setJavaScriptEnabled(true);
		        
		        searchButton.setText("SEARCH");
				searchButton.getStyleClass().add(BTNSRCKEY);
		   
		        preference1.setText(daoPref.getPreferenceFromJSON(1));
				preference2.setText(daoPref.getPreferenceFromJSON(2));
				preference3.setText(daoPref.getPreferenceFromJSON(3));
	
				preference1.getStyleClass().add(BTNPREFKEY);
				preference2.getStyleClass().add(BTNPREFKEY);
				preference3.getStyleClass().add(BTNPREFKEY);
				
				//Al posto di scegliere preferenze casuali
				//e mostrarne i risultati prendo le preferenze dell'utente e 
				//in base a quello restituisco risultati appropriati.
				
				ArrayList<String> preferences = (ArrayList<String>)Navbar.getUser().getPreferences();
				
				if(!preferences.isEmpty()){
						
					for(int i=0;i<preferences.size();i++) {
						activities.addAll(daoAct.findActivityByPreference(daoSU, preferences.get(i)));
					}
					
					int j;
					for(j=0;j<activities.size();j++)
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
							eventImage.setImage(new Image("https://source.unsplash.com/user/erondu/200x100"));
							eventImage.getStyleClass().add("event-image");
							
							eventInfo.setId("eventInfo");
							eventInfo.getStyleClass().add("textEventInfo");
					/*		eventInfo.setTextAlignment(TextAlignment.LEFT);
							eventInfo.setFont(Font.font("Monserrat-Black", FontWeight.EXTRA_LIGHT, 12));
							eventInfo.setFill(Paint.valueOf("#BGCOLOR"));
							eventInfo.setStrokeWidth(0.3);
							eventInfo.setStroke(Paint.valueOf("#000000"));
						*/	
							eventName.setId("eventName");
							eventName.getStyleClass().add("textEventName");
							/*eventName.setFont(Font.font("Monserrat-Black", FontWeight.BLACK, 20));
							eventName.setFill(Paint.valueOf("#BGCOLOR"));
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
							eventBox.setAlignment(Pos.CENTER);
							
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
				}
    	}
	}

public void activitySelected() {
		
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
		
		if(lastEventBoxSelected!=null) activityDeselected(lastEventBoxSelected,false);
		
		int itemNumber = eventsList.getSelectionModel().getSelectedIndex();
		
		//La prossima volta che selezioner� un altro evento oltre questo si resetta il suo eventBox.
		lastEventBoxSelected = eventBox;
		
		int activityId = Integer.parseInt(eventBox.getChildren().get(0).getId());
		Long placeId = Long.parseLong(eventBox.getChildren().get(1).getId());
		ImageView eventImage = (ImageView) eventBox.getChildren().get(2);
		VBox eventInfo = (VBox) eventBox.getChildren().get(3);
		
		Text eventName = (Text) eventInfo.getChildren().get(0);
		Text eventDetails = (Text) eventInfo.getChildren().get(1);
		
		Log.getInstance().getLogger().info(eventName+" "+eventDetails);

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

		activitySelected = daoAct.findActivityByID(daoSU,daoPlc.findPlaceById(placeId),activityId); 
		Log.getInstance().getLogger().info("Attivit� trovata: "+activitySelected);

		viewOnMap.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
					eng.executeScript("spotPlace('"+activitySelected.getPlace().getCivico()+"','"+activitySelected.getPlace().getAddress()+"','"+activitySelected.getPlace().getCity()+"','"+activitySelected.getPlace().getRegion()+"')");
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
							activitySelected.getChannel().addMsg(user.getUserID(), mss.getText());
							
							//TODO Nel metodo chiamato non conviene passare solo l'attivit� selezionata e da l� estrapolare gli altri parametri essendo tutti derivati? P.S:ho gi� levato uno dei parametri
							daoCH.updateChannelInJSON(activitySelected.getChannel().getChat(), activitySelected);

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
					
					close.setOnAction(new EventHandler<ActionEvent>(){
						@Override public void handle(ActionEvent e) {
							root.getChildren().remove(chatContainer);
						}
					});
					
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
						ChoiceBox<String> hourBox = new ChoiceBox<>();
						ChoiceBox<String> minBox = new ChoiceBox<>();
	
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
						
						/*for(int j=0;j<61;j++) {
							minBox.getItems().add(Integer.toString(j));
						}*/
						
						Text txt = new Text("Select date");
						Button ok = new Button();
						Button close = new Button();
						
						VBox dateBox = new VBox();
						ok.setText("Ok");
						ok.getStyleClass().add(BTNSRCKEY);
						
						HBox buttonBox = new HBox();
						HBox pickTimeBox = new HBox();
						
						buttonBox.getChildren().addAll(close,ok);
						
						CornerRadii cr = new CornerRadii(4);
						BackgroundFill bf = new BackgroundFill(Paint.valueOf(BGCOLORKEY), cr, null);
						Background b = new Background(bf);
						
						txt.getStyleClass().add("msstxt");
						
						pickTimeBox.getChildren().addAll(hourBox,minBox);
						
						dateBox.setBackground(b);
						dateBox.getChildren().addAll(txt,pickDate,pickTimeBox,buttonBox);
						dateBox.setId("dateBox");
						
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
								DateTimeFormatter day = DateTimeFormatter.ofPattern("yyyy-MM-dd");
								String dayStringed = day.format(pickDate.getValue());
								
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
								
								((User) user).getSchedule().addActivityToSchedule(activitySelected, dateSelected, remindDate, user);		
								
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
					 final Popup popup = new Popup(); popup.centerOnScreen();
					 
					    Text deletionText = new Text("Activity"+'\n'+"deleted"+'\n'+"correctly");
					    deletionText.getStyleClass().add("textEventInfo");
					    deletionText.setTextAlignment(TextAlignment.CENTER);;
					    
					    Circle c = new Circle(0, 0, 50, Color.valueOf("212121"));
					    
					    StackPane popupContent = new StackPane(c,deletionText); 
					    
					    c.setStrokeType(StrokeType.OUTSIDE);
					    c.setStrokeWidth(0.3);
					    c.setStroke(Paint.valueOf(BGCOLORKEY));
					    
					    popup.getContent().add(popupContent);
					    
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
	for(i=0;i<ch.getChat().size();i++) {
		//TODO qui posso nominarla in altro modo la VBOX?
		VBox chatContainer = new VBox();
		VBox chatMss = new VBox();
		Text msstxt = new Text();
		Text username = new Text();
		Text dateSent = new Text();
		String usernameMss = daoSU.findSuperUserByID(ch.getChat().get(i).getUsr()).getUsername();
		
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
		
		
		if(user.getUsername().equals(usernameMss)) {
			CornerRadii cr = new CornerRadii(4);
		
			BackgroundFill bf = new BackgroundFill(Paint.valueOf(BGCOLORKEY), cr, null);
			Background b = new Background(bf);

			chatContainer.setBackground(b);
			chatContainer.setAlignment(Pos.CENTER_RIGHT);
			chatMss.setAlignment(Pos.CENTER_RIGHT);
		}
		else {
			CornerRadii cr = new CornerRadii(4);
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
	
	if(lastActivitySelected>=0)
		eventsList.getItems().remove(lastActivitySelected+1);
	
	if(delete==true) {
		eventsList.getItems().remove(lastEventBoxSelected);
		daoAct.deleteActivityJSON(activitySelected);
		lastEventBoxSelected=null;
		return;
	}
	
	ImageView eventImage = (ImageView) lastBox.getChildren().get(2);
/**CANCEL	VBox eventInfo = (VBox) lastBox.getChildren().get(3);
	
	Text eventName = (Text) eventInfo.getChildren().get(0);
	Text eventDetails = (Text) eventInfo.getChildren().get(1);**/

	eventImage.setScaleX(1);
	eventImage.setScaleY(1);
	
	root.getChildren().remove(chatContainer);
}

public void filterActivities() {
	daoAct = DAOActivity.getInstance();
	daoSU = DAOSuperUser.getInstance();
	daoPlc = DAOPlace.getInstance();
	daoPref = DAOPreferences.getInstance();
	int searchMode = -1;
	
	String searchItem = null;
	
	eng.executeScript("clearMarkers()");
	
	if((searchItem = searchBar.getText())==null) return;
	
	ArrayList<SuperActivity> activities = new ArrayList<>();
	
	if(daoAct.findActivityByPreference(daoSU, searchItem.toUpperCase())!=null) {
		activities.addAll((ArrayList<SuperActivity>)daoAct.findActivityByPreference(daoSU, searchItem.toUpperCase()));
		searchMode = 1;
	}
	
	else if(daoAct.findActivityByName(daoSU, searchItem.toUpperCase())!=null) {
		activities.addAll((ArrayList<SuperActivity>)daoAct.findActivityByName(daoSU, searchItem.toUpperCase()));
		searchMode = 0;
	}
	
	//else if() {
		
	//}
	
	if(searchMode == -1) return;
	
	if(user instanceof Partner) {
		ArrayList<SuperActivity> partnerAct = new ArrayList<SuperActivity>();
		for(int i=0;i<activities.size();i++) {
			if((activities.get(i).getCreator().getUserID()).equals(user.getUserID()))
				partnerAct.add(activities.get(i));
		}
		activities=partnerAct;
	}
	eventsList.getItems().clear();
	
	int i;
	for(i=0;i<activities.size();i++) {
		ImageView eventImage = new ImageView();
		Text eventName = new Text(activities.get(i).getName()+"\n");
		Log.getInstance().getLogger().info("\n\n"+activities.get(i).getName()+"\n\n");
		Text eventInfo = new Text(activities.get(i).getPlace().getName()+
				"\n"+activities.get(i).getFrequency().getOpeningTime()+
				"-"+activities.get(i).getFrequency().getClosingTime());
		eventImage.setImage(new Image("https://source.unsplash.com/user/erondu/200x100"));

		eventInfo.setId("eventInfo");
		eventInfo.getStyleClass().add("textEventInfo");
	/*	eventInfo.setFont(Font.font("Monserrat-Black", FontWeight.MEDIUM, 12));
		eventInfo.setTextAlignment(TextAlignment.LEFT);
		eventInfo.setFill(Paint.valueOf("#ffffff"));
		eventInfo.setStroke(Paint.valueOf("#000000"));
*/
		eventName.setId("eventName");
		eventName.getStyleClass().add("textEventName");
	/*	eventName.setFont(Font.font("Monserrat-Black", FontWeight.BLACK, 20));
		eventName.setFill(Paint.valueOf("#ffffff"));
		eventName.setStroke(Paint.valueOf("#000000"));
*/
		
		
		VBox eventText = new VBox(eventName,eventInfo);
		eventText.setAlignment(Pos.CENTER);
		
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
		
		//Stabilisco l'allineamento ed in seguito lo aggiungo alla lista di eventi.
		eventBox.setAlignment(Pos.CENTER);
		eng.executeScript("searchPlaces('"+searchItem+"-"+searchMode+"')");
		eventsList.getItems().add(eventBox);
	}
}
}
