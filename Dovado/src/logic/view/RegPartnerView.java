package logic.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
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
import logic.controller.RegPartnerController;
import logic.model.Log;
import logic.model.RegBean;

public class RegPartnerView implements Initializable{
	
	
	
    @FXML
    private TextField usrnameTField;
    
    @FXML
    private TextField emailTField;

    @FXML
    private TextField pIVATField;
    
    @FXML
    private TextField compNameTField;    
    
    @FXML
    private PasswordField pswField;

    @FXML
    private PasswordField pswField2;

   
    @FXML
    private Button signUpBtn;

    @FXML
    private Hyperlink hyperLinkExplorer;

    @FXML
    private Hyperlink hyperLinkLogin;
    
	@FXML
	private VBox root;
	
	@FXML
	private Label errorLabel;
    
	private VBox rt;
	
	private static String BGCOLORKEY = "ffffff";
	private static long wErrPopup = 500;
	private static long hErrPopup = 50;
	private static Stage curr;
	
	private String password;
	private String passwordCheck;
	private String username;
	private String email;
	private String pIVA;
	private String cName;
	

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
		pIVA = pIVATField.getText();
		cName = compNameTField.getText();
		
		if(password.isEmpty() || passwordCheck.isEmpty() || username.isEmpty() || email.isEmpty()) {
			Log.getInstance().getLogger().info("One of the fields is empty!");
			final Popup popup = popupGen(wErrPopup,hErrPopup, "One of the fields is empty!");
		    
		    popup.show(curr);
		    popup.setAutoHide(true);
			return;
		}
		
		RegPartnerController controller = new RegPartnerController();
		RegBean regBean = new RegBean();
		
		regBean.setUsername(username);
		regBean.setEmail(email);
		regBean.setPassword(password);
		regBean.setPassword2(passwordCheck);
		regBean.setpIVA(pIVA);
		regBean.setCompName(cName);
		
		regBean = controller.validateForm(regBean);
		
		if(regBean.getError() != null) {
			Log.getInstance().getLogger().info(regBean.getError());
			final Popup popup = popupGen(wErrPopup,hErrPopup, regBean.getError());
		    
		    popup.show(curr);
		    popup.setAutoHide(true);
			return;
		}
		
		try {
			regBean = controller.addPartner(regBean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(regBean.getError() != null) {
			Log.getInstance().getLogger().info(regBean.getError());
			final Popup popup = popupGen(wErrPopup,hErrPopup, regBean.getError());
		    
		    popup.show(curr);
		    popup.setAutoHide(true);
			return;
		}
    	LoginView.render(curr);
		
    	Log.getInstance().getLogger().info("Partner registered");
    	
    }
    
    public static void render(Stage current) {
		Stage primaryStage = current;

		curr=current;
		
		try {
			VBox root = new VBox();
			BorderPane navbar = Navbar.getNavbar();
			Scene scene = new Scene(root,Navbar.getWidth(),Navbar.getHeight());
			scene.getStylesheets().add(Main.class.getResource("Dovado.css").toExternalForm());
			primaryStage.setTitle("Dovado - PartnerRegister");
			primaryStage.setScene(scene);
			VBox home = new VBox();
	
			home = FXMLLoader.load(Main.class.getResource("RegPartner.fxml"));
			
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
    
    public void switchToExplorerRegister() {
    	RegisterView.render(curr);
    }
    
    public void switchToLogin() {
    	LoginView.render(curr);
    }
   
    
    public Popup popupGen(double width, double height, String error) {
    	Popup popup = new Popup(); 
    	popup.centerOnScreen();
    	
    	Text passwordNotEqualTxt = new Text(error);
	    passwordNotEqualTxt.getStyleClass().add("textEventInfo");
	    passwordNotEqualTxt.setTextAlignment(TextAlignment.CENTER);;
	    
	    //Circle c = new Circle(0, 0, diameter, Color.valueOf("212121"));
	    Rectangle r = new Rectangle(width, height, Color.valueOf("212121"));
	    StackPane popupContent = new StackPane(r,passwordNotEqualTxt); 
	    
	    r.setStrokeType(StrokeType.OUTSIDE);
	    r.setStrokeWidth(0.3);
	    r.setStroke(Paint.valueOf(BGCOLORKEY));
	    
	    popup.getContent().add(popupContent);
	    return popup;
    }
}
