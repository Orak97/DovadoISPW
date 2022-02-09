package logic.view;


import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Stage;
import logic.controller.LogExplorerController;
import logic.controller.LogPartnerController;
import logic.model.Log;
import logic.model.LogBean;
import logic.model.Partner;
import logic.model.SuperUser;
import logic.model.User;

public class LoginView{
	
	private static final String BGCOLORKEY = "ffffff";
	private static long wErrPopup = 500;
	private static long hErrPopup = 50;
	private static Stage curr;
	

    @FXML
    private Button loginInput;

    @FXML
    private Button registerInput;
    
    @FXML
    private RadioButton radioPartner;
    
    @FXML
    private RadioButton radioExplorer;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Label fail;

    @FXML
    private Label labelRegister;

    @FXML
    void login(ActionEvent event) {
    	Log.getInstance().getLogger().info("Clicked login");
    	SuperUser user = null;
    	String passw = password.getText();
    	String email = username.getText();
    	if(passw.isEmpty() || email.isEmpty() ) {
			Log.getInstance().getLogger().info("One of the fields is empty!");
			final Popup popup = popupGen(wErrPopup,hErrPopup, "One of the fields is empty!");
		    
		    popup.show(curr);
		    popup.setAutoHide(true);
			return;
		}
    	if(radioPartner.isSelected()) {
    		LogPartnerController logPartner = new LogPartnerController();
    		user =logAppend(logPartner, null);
    		Log.getInstance().getLogger().info("voglio accedere come partner");
    	} 
    	else if(radioExplorer.isSelected()) {
    		LogExplorerController logExp = new LogExplorerController();
    		user = logAppend(null,logExp);
			Log.getInstance().getLogger().info("voglio accedere come explorer");
    	} else {
    		final Popup popup = popupGen(wErrPopup,hErrPopup, "Select Partner or Explorer");
		    
		    popup.show(curr);
		    popup.setAutoHide(true);
    	}
    	if (user != null) {
    	Stage current = (Stage)((Node)event.getSource()).getScene().getWindow();
    	//HomeView hv = new HomeView()
    	HomeView.render(current);
    	}
    }
    private SuperUser logAppend(LogPartnerController logPartner, LogExplorerController logExp) {
    	SuperUser user = null;
    	LogBean  logbean = new LogBean();
    	logbean.setEmail(username.getText());
    	logbean.setPassword(password.getText());
    	Log.getInstance().getLogger().info("Lo username è: " +username.toString());
    	try {
    		if(logPartner != null) {
    			user = logPartner.loginPartner(logbean);
    		} else {
    			user = logExp.loginExplorer(logbean);
    		}
    		
			if(user == null) {
				final Popup popup = popupGen(wErrPopup,hErrPopup, "Wrong email or password");
			    
			    popup.show(curr);
			    popup.setAutoHide(true);
	    		
				Log.getInstance().getLogger().info("Email o password incorrette.");
				return user;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return user;
		}
    	
    	Navbar.setUser(user);
    	return user;
    }
    
   
    @FXML
    void register(ActionEvent event) {
    	Log.getInstance().getLogger().info("Clicked register");
    	Stage current = (Stage)((Node)event.getSource()).getScene().getWindow();
    	TotemView.render(current);
    }
    
    public static void render(Stage current) {
		Stage primaryStage = current;
		
		curr = current;
		
		try {
			VBox root = new VBox();
			BorderPane navbar = Navbar.getNavbar();
			Scene scene = new Scene(root,Navbar.getWidth(),Navbar.getHeight());
			scene.getStylesheets().add(Main.class.getResource("Dovado.css").toExternalForm());
			primaryStage.setTitle("Dovado - login");
			primaryStage.setScene(scene);
			GridPane login = FXMLLoader.load(Main.class.getResource("Login.fxml"));
			
			root.getChildren().addAll(navbar,login);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		} 
	}
    
    public Popup popupGen(double width, double height, String error) {
    	Popup popup = new Popup(); 
    	popup.centerOnScreen();
    	
    	Text passwordNotEqualTxt = new Text(error);
    	passwordNotEqualTxt.setWrappingWidth(wErrPopup - 10);
	    passwordNotEqualTxt.getStyleClass().add("textEventName");
	    passwordNotEqualTxt.setTextAlignment(TextAlignment.CENTER);
	    
	    Rectangle r = new Rectangle(width, height, Color.valueOf("212121"));
	    StackPane popupContent = new StackPane(r,passwordNotEqualTxt); 
	    
	    r.setStrokeType(StrokeType.OUTSIDE);
	    r.setStrokeWidth(0.3);
	    r.setStroke(Paint.valueOf(BGCOLORKEY));
	    
	    popup.getContent().add(popupContent);
	    return popup;
    }
}