package logic.view;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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
import javafx.stage.Window;
import logic.model.DAOPlace;
import logic.model.DAOSuperUser;
import logic.model.Log;
import logic.model.Partner;
import logic.model.Place;
import logic.model.SuperUser;
import logic.model.User;

public class RegisterView implements Initializable{

    @FXML
    private TextField usrnameTField;

    @FXML
    private PasswordField pswField;

    @FXML
    private PasswordField pswField2;

    @FXML
    private TextField emailTField;

    @FXML
    private Button signUpBtn;

    @FXML
    private Hyperlink hyperLinkPartner;

	@FXML
	private VBox root;
    
	private DAOPlace daoPl;
	private VBox rt;

	private static DAOSuperUser daoSu;

	private static String password;
	private static String passwordCheck;
	private static String username;
	private static String email;
	private static String BGCOLORKEY = "ffffff";
	private static Stage curr;

    @FXML
    void login(ActionEvent event) {
    	Stage current = (Stage)((Node)event.getSource()).getScene().getWindow();
    	LoginView.render(current);
    }

    public void register() {
    	
    	password = pswField.getText();
		passwordCheck = pswField2.getText();
		username = usrnameTField.getText();
		email = emailTField.getText();
		
		if(password.isEmpty() || passwordCheck.isEmpty() || username.isEmpty() || email.isEmpty()) {
			Log.getInstance().getLogger().info("One of the four fields is empty!");
			final Popup popup = new Popup(); popup.centerOnScreen();
			 
		    Text passwordNotEqualTxt = new Text("Not enough"+'\n'+"info"+'\n'+"provided");
		    passwordNotEqualTxt.getStyleClass().add("textEventInfo");
		    passwordNotEqualTxt.setTextAlignment(TextAlignment.CENTER);;
		    
		    Circle c = new Circle(0, 0, 60, Color.valueOf("212121"));
		    
		    StackPane popupContent = new StackPane(c,passwordNotEqualTxt); 
		    
		    c.setStrokeType(StrokeType.OUTSIDE);
		    c.setStrokeWidth(0.3);
		    c.setStroke(Paint.valueOf(BGCOLORKEY));
		    
		    popup.getContent().add(popupContent);
		    
		    popup.show(curr);
		    popup.setAutoHide(true);
			return;
		}

		if(!password.equals(passwordCheck)) {
			Log.getInstance().getLogger().info("The passwords don't match!");
			final Popup popup = new Popup(); popup.centerOnScreen();
			 
		    Text passwordNotEqualTxt = new Text("Passwords"+'\n'+"don't"+'\n'+"match");
		    passwordNotEqualTxt.getStyleClass().add("textEventInfo");
		    passwordNotEqualTxt.setTextAlignment(TextAlignment.CENTER);;
		    
		    Circle c = new Circle(0, 0, 50, Color.valueOf("212121"));
		    
		    StackPane popupContent = new StackPane(c,passwordNotEqualTxt); 
		    
		    c.setStrokeType(StrokeType.OUTSIDE);
		    c.setStrokeWidth(0.3);
		    c.setStroke(Paint.valueOf(BGCOLORKEY));
		    
		    popup.getContent().add(popupContent);
		    
		    popup.show(curr);
		    popup.setAutoHide(true);
			return;
    	}
		daoSu = DAOSuperUser.getInstance();
		
		if(daoSu.findSuperUserByEmail(email) != null) {
			Log.getInstance().getLogger().info("The passwords don't match!");
			final Popup popup = new Popup(); popup.centerOnScreen();
			 
		    Text passwordNotEqualTxt = new Text("The email is already"+'\n'+"linked to"+'\n'+"another account");
		    passwordNotEqualTxt.getStyleClass().add("textEventInfo");
		    passwordNotEqualTxt.setTextAlignment(TextAlignment.CENTER);;
		    
		    Circle c = new Circle(0, 0, 85, Color.valueOf("212121"));
		    
		    StackPane popupContent = new StackPane(c,passwordNotEqualTxt); 
		    
		    c.setStrokeType(StrokeType.OUTSIDE);
		    c.setStrokeWidth(0.3);
		    c.setStroke(Paint.valueOf(BGCOLORKEY));
		    
		    popup.getContent().add(popupContent);
		    
		    popup.show(curr);
		    popup.setAutoHide(true);
			return;
    	}
		
		daoSu.addUserToJSON(email, username, 0, password);
		SuperUser user = daoSu.findSuperUser(email, password, null);
		
		Navbar.setUser(user);
    	PreferenceSelectView.render(curr);
		
    	Log.getInstance().getLogger().info("User registered");
    	
    }
    
    public static void render(Stage current) {
		Stage primaryStage = current;

		curr=current;
		
		try {
			VBox root = new VBox();
			BorderPane navbar = Navbar.getNavbar();
			Scene scene = new Scene(root,Navbar.getWidth(),Navbar.getHeight());
			scene.getStylesheets().add(Main.class.getResource("Dovado.css").toExternalForm());
			primaryStage.setTitle("Dovado - Register");
			primaryStage.setScene(scene);
			VBox home = new VBox();
	
			home = FXMLLoader.load(Main.class.getResource("Register.fxml"));
			
			root.getChildren().addAll(navbar,home);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

    @Override
	public void initialize(URL location, ResourceBundle resources) {
		
		signUpBtn.getStyleClass().add("src-btn");
		
		rt=root;
		
	}
    
    public void switchToRegisterPartner() {
    	
    }
}
