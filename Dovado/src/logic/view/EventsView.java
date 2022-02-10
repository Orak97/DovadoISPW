package logic.view;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import logic.controller.ActivityType;
import logic.controller.UpdateCertActController;
import logic.model.Activity;
import logic.model.Cadence;
import logic.model.CertifiedActivity;
import logic.model.ContinuosActivity;
import logic.model.CreateActivityBean;
import logic.model.DAOActivity;
import logic.model.DAOChannel;
import logic.model.DAOPlace;
import logic.model.DAOPreferences;
import logic.model.DAOSchedules;
import logic.model.ExpiringActivity;
import logic.model.Log;
import logic.model.Partner;
import logic.model.PeriodicActivity;
import logic.model.ScheduledActivity;
import logic.model.SuperActivity;
import logic.model.SuperUser;
import logic.model.User;

public class EventsView implements Initializable{
	
	private static final  String BTNSRCKEY = "src-btn";
	private static final  String STYLEINFO = "textEventInfo";
	private static final  String STYLENAME = "textEventName";
	private static final  String STYLETXT = "msstxt";
	private static final  String DATEPATTERN = "yyyy-MM-dd";
	private static final String STARTINFO = "Activity will start from:";
	private static final String ENDINFO = "Activity will end: ";


	private ArrayList<ScheduledActivity> schedActivities;
	private ArrayList<Activity> activities;
    private DAOActivity daoAct;
    private DAOSchedules daoSch;
    private SuperActivity activitySelected = null;
    private StackPane lastEventBoxSelected = null;
    private SuperUser user;
    private DatePicker pickDateOp = null;
    private DatePicker pickDateCl = null;
	private long wPopup = 500;
	private long hPopup = 50;
	private HBox selection;
	private Button deleteSched;
	
	
    private static Stage curr;
    
    private int lastActivitySelected = -1;
    
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
			
			Scene scene = new Scene(root,Navbar.getWidth(),Navbar.getHeight());
			scene.getStylesheets().add(Main.class.getResource("Dovado.css").toExternalForm());
			current.setTitle("Dovado - events");
			current.setScene(scene);
			VBox eventSchedule = FXMLLoader.load(Main.class.getResource("events.fxml"));
			VBox.setVgrow(eventSchedule, Priority.SOMETIMES);
			
			curr=current;

			root.getChildren().addAll(navbar,eventSchedule);
			
			current.show();	
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Thread newThread = null;
		daoAct = DAOActivity.getInstance();
		daoSch = DAOSchedules.getInstance();
		
		schedActivities = new ArrayList<>();
    	activities = new ArrayList<>();
		
    	searchBtn.getStyleClass().add(BTNSRCKEY);    	
    	
    	user = Navbar.getUser();
    	try{
    		if (user instanceof User) {
    			Log.getInstance().getLogger().info("Ok \nWorking Directory = " + System.getProperty("user.dir"));		
				//Apro di default la lista di attività schedulate.
				
				initUser();
				newThread = new Thread(this::fillEventsListUser);
					
			
    		}else{
    			initPartner();
    			newThread = new Thread(this::fillEventsListUser);
	    	
    		}
    	}catch(Error e) {	
			Log.getInstance().getLogger().warning(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
    	try {		
    		newThread.start();
    		eventsList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    		newThread.join();
    		
    	} catch (InterruptedException e) {
    		Thread.currentThread().interrupt();
			e.printStackTrace();
			Log.getInstance().getLogger().info(e.getMessage());
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private void initUser() throws ClassNotFoundException,SQLException{
		schedActivities = (ArrayList<ScheduledActivity>) (daoSch.getSchedule(user.getUserID())).getScheduledActivities();
		
		for(int j=0;j<schedActivities.size();j++) {
			activities.add((Activity)daoAct.getActivityById(schedActivities.get(j).getReferencedActivity().getId()));
		}

		for(int j=0;j<activities.size();j++)
			Log.getInstance().getLogger().info("tutte le attivit� "+activities.get(j).getId());
	}
	private void initPartner() throws ClassNotFoundException,SQLException{
		ArrayList<CertifiedActivity> activitiesPart = (ArrayList<CertifiedActivity>) daoAct.getPartnerActivities(user.getUserID());
    	
		for(int i=0;i<activitiesPart.size();i++) {
			activities.add((Activity)activitiesPart.get(i));
		}
		
		for(int j=0;j<activities.size();j++)
			Log.getInstance().getLogger().info("tutte le attivit� "+activities.get(j).getId());
	
	}
	
	private void fillEventsListUser() {
		int i;
		for(i=0;i<activities.size();i++) {
			ImageView eventImage = new ImageView();
			Text eventName = new Text(activities.get(i).getName()+"\n");
			Log.getInstance().getLogger().info("\n\n"+activities.get(i).getName()+"\n\n");
			Text eventInfo;
			
			if(activities.get(i).getFrequency() instanceof ExpiringActivity) {
				eventInfo = new Text(activities.get(i).getPlace().getName()+
						"\n Expiring activity"+
						"\n"+((ExpiringActivity)(activities.get(i).getFrequency())).getFormattedStartDate()+
						"\n"+((ExpiringActivity)(activities.get(i).getFrequency())).getFormattedEndDate()+
						"\n"+activities.get(i).getFrequency().getOpeningTime()+
						"-"+activities.get(i).getFrequency().getClosingTime());
			}
			else if(activities.get(i).getFrequency() instanceof PeriodicActivity) {
				eventInfo = new Text(activities.get(i).getPlace().getName()+
						"\n Periodic activity"+
						"\n"+((PeriodicActivity)(activities.get(i).getFrequency())).getCadence().toString()+
						"\n"+((PeriodicActivity)(activities.get(i).getFrequency())).getFormattedStartDate()+
						"\n"+((PeriodicActivity)(activities.get(i).getFrequency())).getFormattedEndDate()+
						"\n"+activities.get(i).getFrequency().getOpeningTime()+
						"-"+activities.get(i).getFrequency().getClosingTime());
			}
			else{
				eventInfo = new Text(activities.get(i).getPlace().getName()+
					"\n Continuos activity"+
					"\n"+activities.get(i).getFrequency().getOpeningTime()+
					"-"+activities.get(i).getFrequency().getClosingTime());
			}

			eventImage.setImage(new Image("https://source.unsplash.com/user/erondu/500x200"));
			eventImage.getStyleClass().add("event-image");
			
			eventInfo.setId("eventInfo");
			eventInfo.getStyleClass().add(STYLEINFO);
			eventInfo.setTextAlignment(TextAlignment.LEFT);
			eventInfo.setWrappingWidth(480);
			
			eventName.setId("eventName");
			eventName.getStyleClass().add(STYLENAME);
			eventName.setTextAlignment(TextAlignment.LEFT);
			eventName.setWrappingWidth(480);
		
			VBox eventText = new VBox(eventName,eventInfo);
			eventText.setAlignment(Pos.CENTER_LEFT);
			eventText.getStyleClass().add("eventTextVbox");
			//Preparo un box in cui contenere il nome dell'attivit� e altre sue
			//informazioni; uso uno StackPane per poter mettere scritte su immagini.
			StackPane eventBox = new StackPane();
			eventBox.getStyleClass().add("eventBox");
			
			Text eventId = new Text();
			Text schedId = new Text();
			
			eventId.setId(activities.get(i).getId().toString());
			if(user instanceof User) {
				schedId.setId(schedActivities.get(i).getId().toString());
			} else {
				schedId.setId(Integer.toString(i));
			}
			//Aggiungo allo stack pane l'id dell'evento, quello del posto, l'immagine
			//dell'evento ed infine il testo dell'evento.
			eventBox.getChildren().add(eventId);
			eventBox.getChildren().add(schedId);
			eventBox.getChildren().add(eventImage);
			eventBox.getChildren().add(eventText);
			if(activities.get(i) instanceof CertifiedActivity) {

				eventName.getStyleClass().clear();
				eventName.getStyleClass().add("certEventName");
				eventName.setText(eventName.getText()+'\n'+"CERTIFICATA");
				if(user instanceof User) {

				try {
					eventInfo.setText(eventInfo.getText()+
						"\nPrice set is: "+((CertifiedActivity)activities.get(i)).getPrice()+
						"\nYour Coupon ID: "+schedActivities.get(i).getCoupon().getCouponCode()+
						"\nYour Discount: "+schedActivities.get(i).getCoupon().getDiscount());
				} catch(NullPointerException e) {
					eventInfo.setText(eventInfo.getText()+
							"\nPrice set is: "+((CertifiedActivity)activities.get(i)).getPrice());
				}}
			}	
			//Stabilisco l'allineamento ed in seguito lo aggiungo alla lista di eventi.
			eventBox.setAlignment(Pos.CENTER_LEFT);
			
			eventsList.getItems().add(eventBox);
		}
	}


	public void scheduledActSelected() {

		daoAct = DAOActivity.getInstance();

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
		int schedEventID;
		
		if(user instanceof User)
			schedEventID = Integer.parseInt(((Text)(eventBox.getChildren().get(1))).getId()); 
		else
			schedEventID = Integer.parseInt(((Text)(eventBox.getChildren().get(0))).getId());
			
		ImageView eventImage = (ImageView) eventBox.getChildren().get(2);
		VBox eventInfo = (VBox) eventBox.getChildren().get(3);
		
		Text eventName = (Text) eventInfo.getChildren().get(0);
		Text eventDetails = (Text) eventInfo.getChildren().get(1);
		
		Log.getInstance().getLogger().info(eventName+" "+eventDetails);

		selection = new HBox();
		deleteSched = new Button();
		if(user instanceof User)
			deleteSched.setText("Delete from schedule");
		else
			deleteSched.setText("Delete activity");
		
		deleteSched.getStyleClass().add("evn-btn");
		
		deleteSched.setAlignment(Pos.CENTER);

		Button modifyEvent = new Button();
		if(user instanceof Partner) {
			try {
				activitySelected = (CertifiedActivity) daoAct.getActivityById(activities.get(itemNumber).getId());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			modifyEvent.setText("Modify activity info.");
			
			modifyEvent.getStyleClass().add("evn-btn");
			
			modifyEvent.setAlignment(Pos.CENTER);
			
			modifyEvent.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					modifyEventeHandle();
				}
				
			});
		}
		
		if(user instanceof User) {
			userSchedAct(itemNumber);
				
		} else {
			partnerSchedAct(itemNumber,modifyEvent);
		}
		
		eventsList.getItems().add(itemNumber+1, selection);
		lastActivitySelected = itemNumber;
		eventImage.setScaleX(1.2);
		eventImage.setScaleY(1.2);

		Log.getInstance().getLogger().info("Attivit� trovata: "+activitySelected);

		deleteSched.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
					popupHandle(schedEventID, itemNumber);								
					
				}
			});
		
	}
	
	private void modifyEventeHandle() {
		

		HBox selectedBox = (HBox)eventsList.getItems().get(lastActivitySelected+1);			
		
		if(selectedBox.getChildren().get(selectedBox.getChildren().size()-1).getId()=="dateBox") {
			return;
		}
		//Apro un pop up in cui si può scegliere una
		//Data in cui svolgere l'attività
		
		
		Text columnsOp = new Text(":");
		Text columnsCl = new Text(":");
		ChoiceBox<String> minBox = new ChoiceBox<>();
		ChoiceBox<String> hourBox = new ChoiceBox<>();
		ChoiceBox<String> clhourBox = new ChoiceBox<>();
		ChoiceBox<String> clminBox = new ChoiceBox<>();
		
		int lowerLimit = 0;
		int upperLimit = 23;


		for(int i=lowerLimit;i<=upperLimit;i++) {
			String hr = Integer.toString(i);
			if(i<10) {
				hr = "0"+hr;
			}			

			hourBox.getItems().add(hr);
			clhourBox.getItems().add(hr);
		}
		
		minBox.getItems().clear();
		clminBox.getItems().clear();
		
		for(int j=0;j<60;j++) {
				String min = Integer.toString(j);
				if(j<10) {
					min = "0"+min;
				}
				minBox.getItems().add(min);
				clminBox.getItems().add(min);
		}
				

		Text txtOp = new Text("Select opening time");
		Text txtCl = new Text("Select closing time");
		Button ok = new Button();
		Button close = new Button();
		
		VBox dateBox = new VBox();
		ok.setText("Ok");
		ok.getStyleClass().add(BTNSRCKEY);
		
		HBox buttonBox = new HBox();
		HBox pickTimeOpBox = new HBox();
		HBox pickTimeClBox = new HBox();
		
		buttonBox.getChildren().addAll(close,ok);
		
		CornerRadii cr = new CornerRadii(4);
		BackgroundFill bf = new BackgroundFill(Paint.valueOf("ffffff"), cr, null);
		Background b = new Background(bf);
		
		pickTimeOpBox.getChildren().addAll(hourBox,columnsOp,minBox);
		pickTimeClBox.getChildren().addAll(clhourBox,columnsCl,clminBox);
		
		if(activitySelected.getFrequency() instanceof PeriodicActivity) {
			ChoiceBox<String> pickCadence = new ChoiceBox<>();
			pickCadence.getItems().addAll(Cadence.WEEKLY.toString(),Cadence.MONTHLY.toString(),Cadence.ANNUALLY.toString());
			
			Text txtCadence = new Text("Select new cadence");
			txtCadence.getStyleClass().add(STYLETXT);
			
			dateBox.getChildren().addAll(txtCadence,pickCadence);			
			
		}
		if(activitySelected.getFrequency() instanceof ExpiringActivity || activitySelected.getFrequency() instanceof PeriodicActivity) {
			pickDateOp = new DatePicker();
			pickDateCl = new DatePicker();
			
			Text txtDateOp = new Text("Select opening date");
			Text txtDateCl = new Text("Select closing date");
			
			txtDateOp.getStyleClass().add(STYLETXT);
			txtDateCl.getStyleClass().add(STYLETXT);
			
			dateBox.getChildren().addAll(txtDateOp,pickDateOp,txtDateCl,pickDateCl);						
		}
		dateBox.setBackground(b);
		dateBox.getChildren().addAll(txtOp,pickTimeOpBox,txtCl,pickTimeClBox,buttonBox);
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
				suppSchedModify( minBox, clminBox, hourBox, clhourBox);
			}
		});
	}
	private void suppSchedModify(ChoiceBox<String> minBox,ChoiceBox<String> clminBox,ChoiceBox<String> hourBox,ChoiceBox<String> clhourBox) {

		
		String ophourChosen = hourBox.getValue();
		String opminChosen = minBox.getValue();
		String clhourChosen = clhourBox.getValue();
		String clminChosen = clminBox.getValue();
		
		
		String openingTimeChosen = ophourChosen+':'+opminChosen;
		String closingTimeChosen = clhourChosen+':'+clminChosen;
		//AGGIUNGERE UN METODO CHE AGGIORNI L'ATTIVITA' NEL DATABASE.
		
		// CAPIRE SE SERVE MODIFICARE ANCHE LA DESCRIZIONE
		// O IL NOME DELL'ATTIVITA' O IL POSTO.
		CreateActivityBean cab = new CreateActivityBean();
		
		cab.setActivityDescription(activitySelected.getDescription());
		cab.setActivityName(activitySelected.getName());
		cab.setPlace(activitySelected.getPlace().getId().intValue());
		cab.setOwner(((CertifiedActivity)(activitySelected)).getOwner().getUserID().intValue());
		cab.setIdActivity(activitySelected.getId().intValue());
		
		cab.setInterestedCategories(activitySelected.getIntrestedCategories().getSetPreferences());
		if(activitySelected.getFrequency() instanceof PeriodicActivity) {
			cab.setType(ActivityType.PERIODICA);

			DateTimeFormatter day = DateTimeFormatter.ofPattern(DATEPATTERN);
			String dayStringed = day.format(pickDateOp.getValue());

			DateTimeFormatter dayCl = DateTimeFormatter.ofPattern(DATEPATTERN);
			String dayStringedCl = dayCl.format(pickDateCl.getValue());
			//SE L'ATTIVITA' HA UNA FREQUENZA DI TIPO PERIODICO, ALLORA
			//AVRA' ORARIO DI APERTURA, ORARIO DI CHIUSURA, DATA DI INIZIO,
			//DATA DI FINE ED INFINE LA CADENZA PERTANTO:
			cab.setEndDate(dayStringedCl);
			cab.setOpeningDate(dayStringed);
			cab.setCadence(((PeriodicActivity)(activitySelected.getFrequency())).getCadence());
		}
		if(activitySelected.getFrequency() instanceof ExpiringActivity || activitySelected.getFrequency() instanceof PeriodicActivity) {
			cab.setType(ActivityType.SCADENZA);

			DateTimeFormatter day = DateTimeFormatter.ofPattern(DATEPATTERN);
			String dayStringed = day.format(pickDateOp.getValue());

			DateTimeFormatter dayCl = DateTimeFormatter.ofPattern(DATEPATTERN);
			String dayStringedCl = dayCl.format(pickDateCl.getValue());
			//SE L'ATTIVITA' HA UNA FREQUENZA DI TIPO CONTINUO, ALLORA
			//AVRA' ORARIO DI APERTURA, ORARIO DI CHIUSURA, DATA DI INIZIO
			//E DATA DI FINE PERTANTO:
			cab.setEndDate(dayStringedCl);
			cab.setOpeningDate(dayStringed);
		}
		else if(activitySelected.getFrequency() instanceof ContinuosActivity) {
			cab.setType(ActivityType.CONTINUA);
			//SE L'ATTIVITA' HA UNA FREQUENZA DI TIPO CONTINUO, ALLORA
			//AVRA' SOLO ORARIO DI APERTURA E ORARIO DI CHIUSURA, PERTANTO:	
		}
		cab.setOpeningTime(openingTimeChosen);
		cab.setClosingTime(closingTimeChosen);
		
		
		UpdateCertActController updateController = new UpdateCertActController(cab,(CertifiedActivity)activitySelected);
		
		try {
			if(updateController.updateActivity()) {
				
				final Popup popup = popupGen(wPopup,hPopup,"Activity successfully modified"); 
				popup.centerOnScreen(); 
			    
			    popup.show(curr);
			    popup.setAutoHide(true);
				
			} else {
				final Popup popup = popupGen(wPopup,hPopup,"Activity not modified"); 
				popup.centerOnScreen(); 
			    
			    popup.show(curr);
			    popup.setAutoHide(true);
	
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			Log.getInstance().getLogger().info("Database access was not obtained");
			
		}
		
	}
	
	private void userSchedAct(int itemNumber) {
		String remind = schedActivities.get(itemNumber).getReminderTime().toString().replace('T', ' ');
		String sched = schedActivities.get(itemNumber).getScheduledTime().toString().replace('T', ' ');

		Text reminderTime = new Text("Activity reminder at: "+remind);
		Text scheduledTime = new Text("Activity scheduled for: "+sched);
		
		reminderTime.getStyleClass().add(STYLEINFO);
		scheduledTime.getStyleClass().add(STYLENAME);
		
		VBox schedInfo = new VBox();
		
		schedInfo.setAlignment(Pos.CENTER_RIGHT);
		schedInfo.getChildren().addAll(scheduledTime,reminderTime);
		if(activities.get(itemNumber).getFrequency() instanceof ExpiringActivity) {
			Text startDate = new Text(STARTINFO+((ExpiringActivity)activities.get(itemNumber).getFrequency()).getFormattedStartDate());
			Text endDate = new Text(ENDINFO+(((ExpiringActivity)activities.get(itemNumber).getFrequency()).getFormattedEndDate()));
			schedInfo.getChildren().addAll(startDate,endDate);
		}
		else if(activities.get(itemNumber).getFrequency() instanceof PeriodicActivity) {
			Text startDate = new Text(STARTINFO+((PeriodicActivity)activities.get(itemNumber).getFrequency()).getFormattedStartDate());
			Text endDate = new Text(ENDINFO+((PeriodicActivity)activities.get(itemNumber).getFrequency()).getFormattedEndDate());
			Text cadence = new Text("Cadence of the activity: "+((PeriodicActivity)activities.get(itemNumber).getFrequency()).getCadence().toString());
			schedInfo.getChildren().addAll(cadence,startDate,endDate);
		}

		selection.getChildren().addAll(deleteSched,schedInfo);
	}
	
	private void partnerSchedAct(int itemNumber,Button modifyEvent) {
		String remind = activities.get(itemNumber).getFrequency().getOpeningTime().toString().replace('T', ' ');
		String sched = activities.get(itemNumber).getFrequency().getClosingTime().toString().replace('T', ' ');
		
		Text reminderTime = new Text("Activity opening at: "+remind);
		Text scheduledTime = new Text("Activity closing at: "+sched);
		
		reminderTime.getStyleClass().add(STYLEINFO);
		scheduledTime.getStyleClass().add(STYLEINFO);
		
		VBox eventsInfo = new VBox();
		
		eventsInfo.setAlignment(Pos.TOP_LEFT);
		eventsInfo.getChildren().addAll(scheduledTime,reminderTime);
		if(activities.get(itemNumber).getFrequency() instanceof ExpiringActivity) {
			Text startDate = new Text(STARTINFO+((ExpiringActivity)activities.get(itemNumber).getFrequency()).getFormattedStartDate());
			Text endDate = new Text(ENDINFO+(((ExpiringActivity)activities.get(itemNumber).getFrequency()).getFormattedEndDate()));
			eventsInfo.getChildren().addAll(startDate,endDate);
		}
		else if(activities.get(itemNumber).getFrequency() instanceof PeriodicActivity) {
			Text startDate = new Text(STARTINFO+((PeriodicActivity)activities.get(itemNumber).getFrequency()).getFormattedStartDate());
			Text endDate = new Text(ENDINFO+((PeriodicActivity)activities.get(itemNumber).getFrequency()).getFormattedEndDate());
			Text cadence = new Text("Cadence of the activity: "+((PeriodicActivity)activities.get(itemNumber).getFrequency()).getCadence().toString());
			eventsInfo.getChildren().addAll(cadence,startDate,endDate);
		}
		selection.getChildren().addAll(eventsInfo,modifyEvent,deleteSched);
		
		
	}
	
	
	private void popupHandle(long schedEventID, int itemNumber) {
		if(deleteScheduledEvent(schedEventID)) {
			eventsList.getItems().remove(itemNumber+1);
			eventsList.getItems().remove(itemNumber);
			lastActivitySelected= -1;
			lastEventBoxSelected= null;
			//Messaggio per il dialog box
			popupGen(wPopup,hPopup,"Scheduled activity successfully deleted"); 
			
        }
        else {
        	popupGen(wPopup,hPopup,"Scheduled activity unsuccessfully deleted"); 
	      }
	}
	
	public void activityDeselected(StackPane lastEventBoxSelected2) {
		if(lastActivitySelected>=0) 
			eventsList.getItems().remove(lastActivitySelected+1);
		
		ImageView eventImage = (ImageView) lastEventBoxSelected2.getChildren().get(2);

		
		eventImage.setScaleX(1);
		eventImage.setScaleY(1);
	}
	
	public boolean deleteScheduledEvent(long idSched) {
		
		boolean result = false;
		try {
			result = daoSch.removeActFromSchedule(idSched,Navbar.getUser().getUserID());
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;

	}
	
	public void searchEvent() {
		
		
		if(searchBar.getText()==null) return;
		lastEventBoxSelected=null;
		lastActivitySelected=-1;
		eventsList.getItems().clear();
		activities.clear();
		try { 
			if(user instanceof Partner) {
				ArrayList<CertifiedActivity> activitiesP = (ArrayList<CertifiedActivity>) daoAct.getPartnerActivities(user.getUserID());
				for(CertifiedActivity act:activitiesP)
					activities.add((Activity)act);
				fillEventsListUser();
			} else {
				schedActivities = (ArrayList<ScheduledActivity>) (daoSch.getSchedule(user.getUserID())).getScheduledActivities();
				for(int j=0;j<schedActivities.size();j++) {
					activities.add((Activity)daoAct.getActivityById(schedActivities.get(j).getReferencedActivity().getId()));
				}
				fillEventsListUser();
			}
		}
		catch (Exception e) {
			Log.getInstance().getLogger().info("Error in DB prevented activities from being fetched.");
			e.printStackTrace();
		}		
	}
	
	
	public Popup popupGen(double width, double height, String error) {
		Popup popup = new Popup(); 
		popup.centerOnScreen();
		
		Text errorTxt = new Text(error);
		errorTxt.getStyleClass().add(STYLEINFO);
		errorTxt.setTextAlignment(TextAlignment.CENTER);
		errorTxt.setWrappingWidth(480);
	    
	    Rectangle r = new Rectangle(width, height, Color.valueOf("212121"));
	    StackPane popupContent = new StackPane(r,errorTxt); 
	    
	    r.setStrokeType(StrokeType.OUTSIDE);
	    r.setStrokeWidth(0.3);
	    r.setStroke(Paint.valueOf("ffffff"));
	    
	    popup.getContent().add(popupContent);
	    popup.centerOnScreen(); 
	    
	    popup.show(curr);
	    popup.setAutoHide(true);
	    return popup;
	}
}
