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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.controller.AuthException;
import logic.controller.RegExplorerController;
import logic.model.Log;
import logic.model.RegExpBean;

public class RegisterView extends SuperView implements Initializable{

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
			popupGen("One of the four fields is empty!");
		    
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
			controller.validateForm(regBean);

			controller.addExplorer(regBean);
		}catch (AuthException e) {
			popupGen(e.getMessage());
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
    
    
}
