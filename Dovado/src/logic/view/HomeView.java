package logic.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Cell;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
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
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import logic.model.Activity;
import logic.model.Channel;
import logic.model.DAOActivity;
import logic.model.DAOChannel;
import logic.model.DAOPlace;
import logic.model.DAOPreferences;
import logic.model.DAOSuperUser;
import logic.model.SuperActivity;
import logic.model.SuperUser;
import logic.model.User;

public class HomeView implements Initializable{

	private static StackPane lastEventBoxSelected;
	
	private static VBox chatContainer;
	@FXML
	private TextField searchBar;
	
	@FXML
	private VBox root;

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
    
	public static void render(Stage current, SuperUser user2) {
		try {
			VBox root = new VBox();
			BorderPane navbar = Navbar.getNavbar();
			Navbar.authenticatedSetup();
			
			VBox home = new VBox();
			
			try {
				Scene scene = new Scene(root,Navbar.getWidth(),Navbar.getHeight());
				scene.getStylesheets().add(Main.class.getResource("Dovado.css").toExternalForm());
				current.setTitle("Dovado - home");
				current.setScene(scene);
				home = FXMLLoader.load(Main.class.getResource("home.fxml"));
				VBox.setVgrow(home, Priority.SOMETIMES);
			} catch(IOException e) {
				e.printStackTrace();
			}
			root.getChildren().addAll(navbar,home);
			user=user2;
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
		
    	ArrayList<SuperActivity> activities = new ArrayList<SuperActivity>();
		
    	System.out.println("ok");
		System.out.println("Working Directory = " + System.getProperty("user.dir"));		
		try{
			eng = map.getEngine();
			eng.load("file:/home/pgs/eclipse-workspace/DovadoISPW/WebContent/map.html");
			
			// Setting permissions to interact with Js
	        eng.setJavaScriptEnabled(true);
	        
	        searchButton.setText("SEARCH");
			searchButton.getStyleClass().add("src-btn");
	        
	        preference1.setText(daoPref.getPreferenceFromJSON(1));
			preference2.setText(daoPref.getPreferenceFromJSON(2));
			preference3.setText(daoPref.getPreferenceFromJSON(3));

			preference1.getStyleClass().add("pref-btn");
			preference2.getStyleClass().add("pref-btn");
			preference3.getStyleClass().add("pref-btn");
			
			//Apro di default una lista di attivit� che hanno a che fare con Boxe e Tennis.
			activities.addAll(daoAct.findActivityByPreference(daoSU, "BOXE"));
			activities.addAll(daoAct.findActivityByPreference(daoSU, "TENNIS"));

			System.out.println("\nNumero di attivit� trovate: "+activities.size());

			int j;
			for(j=0;j<activities.size();j++)
				System.out.println("tutte le attivit� "+activities.get(j).getId());
			
			Thread newThread = new Thread(() -> {
				int i;
				for(i=0;i<activities.size();i++) {
					ImageView eventImage = new ImageView();
					Text eventName = new Text(activities.get(i).getName()+"\n");
					System.out.println("\n\n"+activities.get(i).getName()+"\n\n");
					Text eventInfo = new Text(activities.get(i).getPlace().getName()+
							"\n"+activities.get(i).getFrequency().getOpeningTime()+
							"-"+activities.get(i).getFrequency().getClosingTime());
					eventImage.setImage(new Image("https://source.unsplash.com/user/erondu/200x100"));
					eventImage.getStyleClass().add("event-image");
					
					eventInfo.setId("eventInfo");
					eventInfo.getStyleClass().add("textEventInfo");
					eventInfo.setTextAlignment(TextAlignment.LEFT);
					eventInfo.setFont(Font.font("Monserrat-Black", FontWeight.EXTRA_LIGHT, 12));
					eventInfo.setFill(Paint.valueOf("#ffffff"));
					eventInfo.setStrokeWidth(0.3);
					eventInfo.setStroke(Paint.valueOf("#000000"));
					
					eventName.setId("eventName");
					eventName.getStyleClass().add("textEventName");
					eventName.setFont(Font.font("Monserrat-Black", FontWeight.BLACK, 20));
					eventName.setFill(Paint.valueOf("#ffffff"));
					eventName.setStrokeWidth(0.3);
					eventName.setStroke(Paint.valueOf("#000000"));
					
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
		}catch(Error e) {
			System.out.println(e);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println(e);
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
		
		System.out.println(lastActivitySelected);

		if(lastEventBoxSelected == eventBox) return;
		
		if(lastEventBoxSelected!=null) activityDeselected(lastEventBoxSelected);
		
		int itemNumber = eventsList.getSelectionModel().getSelectedIndex();
		
		//La prossima volta che selezioner� un altro evento oltre questo si resetta il suo eventBox.
		lastEventBoxSelected = eventBox;
		
		int activityId = Integer.parseInt(eventBox.getChildren().get(0).getId());
		Long placeId = Long.parseLong(eventBox.getChildren().get(1).getId());
		ImageView eventImage = (ImageView) eventBox.getChildren().get(2);
		VBox eventInfo = (VBox) eventBox.getChildren().get(3);
		
		Text eventName = (Text) eventInfo.getChildren().get(0);
		Text eventDetails = (Text) eventInfo.getChildren().get(1);
		
		System.out.println(eventName+" "+eventDetails);

		VBox selection = new VBox();
		Button viewOnMap = new Button();
		Button joinActivityChannel = new Button();
		Button playActivity = new Button();

		viewOnMap.setText("View event");
		joinActivityChannel.setText("Join channel");
		playActivity.setText("Play activity");
		
		viewOnMap.getStyleClass().add("evn-btn");
		joinActivityChannel.getStyleClass().add("evn-btn");
		playActivity.getStyleClass().add("evn-btn");
		
		selection.getChildren().addAll(viewOnMap,joinActivityChannel,playActivity);
		
		eventsList.getItems().add(itemNumber+1, selection);
		lastActivitySelected = itemNumber;
		eventImage.setScaleX(1.2);
		eventImage.setScaleY(1.25);

		activitySelected = daoAct.findActivityByID(daoSU,daoPlc.findPlaceById(placeId),activityId); 
		System.out.println("Attivit� trovata: "+activitySelected);

		viewOnMap.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
					eng.executeScript("spotPlace('"+activitySelected.getPlace().getCivico()+"','"+activitySelected.getPlace().getAddress()+"','"+activitySelected.getPlace().getCity()+"','"+activitySelected.getPlace().getRegion()+"')");
				};
		});
		
		joinActivityChannel.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
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
							System.out.println("\n\nInviando un messaggio...\n");
							System.out.println("\nMessaggi prima dell'invio:\n");
							for(int j=0;j<activitySelected.getChannel().getChat().size();j++) {
								System.out.println(activitySelected.getChannel().getChat().get(j).getMsgText());
							}
							activitySelected.getChannel().addMsg(user.getUserID(), mss.getText());
							
							daoCH.updateChannelInJSON(activitySelected.getChannel(), activitySelected.getChannel().getChat(), activitySelected);

							System.out.println("\nMessaggi dopo l'invio:\n");
							for(int j=0;j<activitySelected.getChannel().getChat().size();j++) {
								System.out.println(activitySelected.getChannel().getChat().get(j).getMsgText());
							}
							updateChat(chat,activitySelected.getChannel());
						}
					});
					
					send.setText("SEND");
					send.getStyleClass().add("src-btn");
					
					textAndSend.getChildren().addAll(mss,send);
					chatContainer.getChildren().addAll(chat,textAndSend);
					chatContainer.setAlignment(Pos.BOTTOM_RIGHT);
					
					root.getChildren().add(chatContainer);
					
				};
		});
		
		playActivity.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
					((User) user).getSchedule().addActivityToSchedule((Activity)activitySelected, null, null, user);;
				};
		});
	}

	private void updateChat(ListView chat, Channel ch) {

		chat.getItems().clear();
		for(int i=0;i<ch.getChat().size();i++) {
			
			VBox chatMss = new VBox();
			TextField msstxt = new TextField();
			TextField username = new TextField();
			TextField dateSent = new TextField();
			String usernameMss = daoSU.findSuperUserByID(ch.getChat().get(i).getUsr()).getUsername();
			
			//Mi trovo lo username da mettere sopra il messaggio:
			username.setText(usernameMss);
			//Mi trovo il testo da mettere al centro del messaggio:
			msstxt.setText(ch.getChat().get(i).getMsgText());
			//Mi trovo il tempo di invio da mettere in basso a destra del messaggio:
			dateSent.setText(ch.getChat().get(i).getMsgSentDate());
			
			username.setAlignment(Pos.TOP_LEFT);
			msstxt.setAlignment(Pos.CENTER_LEFT);
			dateSent.setAlignment(Pos.BOTTOM_RIGHT);
			
			chatMss.getChildren().addAll(username,msstxt,dateSent);
			chatMss.setPrefWidth(chat.getWidth()/2);
			
			if(user.getUsername().equals(usernameMss)) {
				chatMss.setAlignment(Pos.CENTER_RIGHT);
			}
			else {
				chatMss.setAlignment(Pos.CENTER_LEFT);
			}
			chat.getItems().add(chatMss);
		}
	}
	
	//Penso che questo metodo come anche activity selected etc... vadano spostati in un'apposita classe
	//HomeController.
	
	public void activityDeselected(StackPane lastBox) {
		if(lastActivitySelected>=0)
			eventsList.getItems().remove(lastActivitySelected+1);
		
		ImageView eventImage = (ImageView) lastBox.getChildren().get(2);
		VBox eventInfo = (VBox) lastBox.getChildren().get(3);
		
		Text eventName = (Text) eventInfo.getChildren().get(0);
		Text eventDetails = (Text) eventInfo.getChildren().get(1);

		eventImage.setScaleX(1);
		eventImage.setScaleY(1);
		
		root.getChildren().remove(chatContainer);
	}
	
	public void filterActivities() {
		daoAct = DAOActivity.getInstance();
		daoSU = DAOSuperUser.getInstance();
		daoPlc = DAOPlace.getInstance();
		daoPref = DAOPreferences.getInstance();
		
		String searchItem = null;
		
		if((searchItem = searchBar.getText())==null) return;
		
		if(daoPref.preferenceIsInJSON(searchItem.toUpperCase())==false) return;
		
		ArrayList<SuperActivity> activities = new ArrayList<SuperActivity>();
		
		activities.addAll(daoAct.findActivityByPreference(daoSU, searchItem.toUpperCase()));
		eventsList.getItems().clear();
		
		int i;
		for(i=0;i<activities.size();i++) {
			ImageView eventImage = new ImageView();
			Text eventName = new Text(activities.get(i).getName()+"\n");
			System.out.println("\n\n"+activities.get(i).getName()+"\n\n");
			Text eventInfo = new Text(activities.get(i).getPlace().getName()+
					"\n"+activities.get(i).getFrequency().getOpeningTime()+
					"-"+activities.get(i).getFrequency().getClosingTime());
			eventImage.setImage(new Image("https://source.unsplash.com/user/erondu/200x100"));

			eventInfo.setId("eventInfo");
			eventInfo.getStyleClass().add("textEventInfo");
			eventInfo.setFont(Font.font("Monserrat-Black", FontWeight.MEDIUM, 12));
			eventInfo.setTextAlignment(TextAlignment.LEFT);
			eventInfo.setFill(Paint.valueOf("#ffffff"));
			eventInfo.setStroke(Paint.valueOf("#000000"));

			eventName.setId("eventName");
			eventName.getStyleClass().add("textEventName");
			eventName.setFont(Font.font("Monserrat-Black", FontWeight.BLACK, 20));
			eventName.setFill(Paint.valueOf("#ffffff"));
			eventName.setStroke(Paint.valueOf("#000000"));

			
			
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
			
			eventsList.getItems().add(eventBox);
		}
	}
}
