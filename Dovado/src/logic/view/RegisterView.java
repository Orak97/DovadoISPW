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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;
import logic.controller.AuthException;
import logic.controller.RegExplorerController;
import logic.controller.RegPartnerController;
import logic.model.DAOPlace;
import logic.model.Log;
import logic.model.Partner;
import logic.model.Place;
import logic.model.RegExpBean;
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
	
	@FXML
	private Label errorLabel;
    
	//---------------- INIZIO PREFERENZE -------------------------------------------
	
	@FXML
	private RadioButton btArte;
	@FXML
	private RadioButton btCibo;
	@FXML
	private RadioButton btMusica;
	@FXML
	private RadioButton btSport;
	@FXML
	private RadioButton btSocial;
	@FXML
	private RadioButton btNatura;
	@FXML
	private RadioButton btEsplorazione;
	@FXML
	private RadioButton btRicorr;
	@FXML
	private RadioButton btModa;
	@FXML
	private RadioButton btShopping;
	@FXML
	private RadioButton btAdrenalina;
	@FXML
	private RadioButton btMonumenti;
	@FXML
	private RadioButton btRelax;
	@FXML
	private RadioButton btIstruzione;
	
	//------------------------ FINE PREFERENZE -----------------------------------------------------
	
	private static final String BGCOLORKEY = "ffffff";
	private static long wErrPopup = 500;
	private static long hErrPopup = 50;
	private static Stage curr;

    @FXML
    void login(ActionEvent event) {
    	Stage current = (Stage)((Node)event.getSource()).getScene().getWindow();
    	LoginView.render(current);
    }

    public void register() {
    	
    	String password = pswField.getText();
		String passwordCheck = pswField2.getText();
		String username = usrnameTField.getText();
		String email = emailTField.getText();
		
		if(password.isEmpty() || passwordCheck.isEmpty() || username.isEmpty() || email.isEmpty()) {
			Log.getInstance().getLogger().info("One of the four fields is empty!");
			popupGen(wErrPopup,hErrPopup, "One of the four fields is empty!");
		    
		    return;
		}
		
		RegExplorerController controller = new RegExplorerController();
		RegExpBean regBean = new RegExpBean();
		
		regBean.setUsername(username);
		regBean.setEmail(email);
		regBean.setPassword(password);
		regBean.setPassword2(passwordCheck);
		
		//----AGGIORNO LE PREFERENZE AL BEAN DA DARE AL CONTROLLER--------
		
		regBean.setArte(btArte.isSelected());
		regBean.setCibo(btCibo.isSelected());
		regBean.setMusica(btMusica.isSelected());
		regBean.setSport(btSport.isSelected());
		regBean.setSocial(btSocial.isSelected());
		regBean.setNatura(btNatura.isSelected());
		regBean.setEsplorazione(btEsplorazione.isSelected());
		regBean.setRicorrenze(btRicorr.isSelected());
		regBean.setModa(btModa.isSelected());
		regBean.setShopping(btShopping.isSelected());
		regBean.setAdrenalina(btAdrenalina.isSelected());
		regBean.setMonumenti(btMonumenti.isSelected());
		regBean.setRelax(btRelax.isSelected());
		regBean.setIstruzione(btIstruzione.isSelected());
		
		try {
			regBean = controller.validateForm(regBean);

			regBean = controller.addExplorer(regBean);
		}catch (AuthException e) {
			popupGen(wErrPopup,hErrPopup, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    	LoginView.render(curr);
		
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
			VBox home = FXMLLoader.load(Main.class.getResource("Register.fxml"));
			
			root.getChildren().addAll(navbar,home);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

    @Override
	public void initialize(URL location, ResourceBundle resources) {
		signUpBtn.getStyleClass().add("src-btn");
				
	}
    
    public void switchToRegisterPartner() {
    	RegPartnerView.render(curr);
    }
    public void switchToLogin() {
    	LoginView.render(curr);
    }
    
    public void popupGen(double width, double height, String error) {
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

	    popup.show(curr);
	    popup.setAutoHide(true);
    }
}
