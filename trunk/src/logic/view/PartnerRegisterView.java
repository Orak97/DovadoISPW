package logic.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Stage;
import logic.model.DAOSuperUser;
import logic.model.Log;
import logic.model.SuperUser;

public class PartnerRegisterView implements Initializable{
	@FXML
    private TextField usrnameTField;

    @FXML
    private PasswordField pswField;

    @FXML
    private PasswordField pswField2;

    @FXML
    private TextField emailTField;

    @FXML
    private TextField bsnPg1;
   
    @FXML
    private Button morePagesBtn;
    
    @FXML
    private Button signUpBtn;

    @FXML
    private VBox bsnPgVBox;

	@FXML
	private VBox root;

	private DAOSuperUser daoSu;

	private int bsnPages = 1;

	private static ArrayList<String> bsnPges;
	private static ArrayList<TextField> bsnPgExtraTF;
	private static String password;
	private static String passwordCheck;
	private static String username;
	private static String email;
	private static String BGCOLORKEY = "ffffff";
	private static Stage curr;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		signUpBtn.getStyleClass().add("src-btn");
		bsnPgExtraTF = new ArrayList<TextField>();
	}
	
	public void addTextField() {
		if(bsnPages < 5) {
			TextField bsnPage = new TextField();
			
			bsnPages++;
			bsnPage.setPromptText("Business page "+String.valueOf(bsnPages));
			
			//Aggiungo la nuova textField della business page
			//al HBox e anche ad un arraylist di TextField.
			//In questo modo posso recuperarne il testo
			//e aggiungerlo alle pagine business del partner.
			bsnPgVBox.getChildren().add(bsnPage);
			bsnPgExtraTF.add(bsnPage);
			return;
		}
		
		final Popup popup = new Popup(); popup.centerOnScreen();
		 
	    Text tooManyPagesTxt = new Text("Too many"+'\n'+"pages at"+'\n'+"once");
	    tooManyPagesTxt.getStyleClass().add("textEventInfo");
	    tooManyPagesTxt.setTextAlignment(TextAlignment.CENTER);;
	    
	    Circle c = new Circle(0, 0, 50, Color.valueOf("212121"));
	    
	    StackPane popupContent = new StackPane(c,tooManyPagesTxt); 
	    
	    c.setStrokeType(StrokeType.OUTSIDE);
	    c.setStrokeWidth(0.3);
	    c.setStroke(Paint.valueOf(BGCOLORKEY));
	    
	    popup.getContent().add(popupContent);
	    
	    popup.show(curr);
	    popup.setAutoHide(true);
		return;
		
	}
	
	public void register() {
    	
    	password = pswField.getText();
		passwordCheck = pswField2.getText();
		username = usrnameTField.getText();
		email = emailTField.getText();
		bsnPges = new ArrayList<String>();
		
		if(password.isEmpty() || passwordCheck.isEmpty() || username.isEmpty() || email.isEmpty() || bsnPg1.getText().isEmpty()) {
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
		
		if(password.length()<8) {
			
			Log.getInstance().getLogger().info("The password isn't long enough!");
			final Popup popup = new Popup(); popup.centerOnScreen();
			 
		    Text passwordNotEqualTxt = new Text("Password must"+'\n'+"at least be"+'\n'+"8 letters long");
		    passwordNotEqualTxt.getStyleClass().add("textEventInfo");
		    passwordNotEqualTxt.setTextAlignment(TextAlignment.CENTER);;
		    
		    Circle c = new Circle(0, 0, 70, Color.valueOf("212121"));
		    
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
		
		bsnPges.add(bsnPg1.getText());
		for(int i=0;i<bsnPgExtraTF.size();i++) {
			String bsnPageTxt = bsnPgExtraTF.get(i).getText();
			//Mi recupero il testo della TextField della
			//businessPage e se non è vuoto lo aggiungo
			//all'array di pagine.
			if(!bsnPageTxt.isEmpty())
				bsnPges.add(bsnPageTxt);
		}
		
		for(int i=0;i<bsnPges.size();i++)
			Log.getInstance().getLogger().info(bsnPges.get(i));
		
		daoSu.addUserToJSON(email, username, 1, password);
		SuperUser user = daoSu.findSuperUser(email, password, null);
		
		Navbar.setUser(user);
    	HomeView.render(curr);
		
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
			primaryStage.setTitle("Dovado - Register as a Partner");
			primaryStage.setScene(scene);
			VBox home = new VBox();
	
			home = FXMLLoader.load(Main.class.getResource("RegisterPartner.fxml"));
			
			root.getChildren().addAll(navbar,home);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
