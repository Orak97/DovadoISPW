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
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.controller.AuthException;
import logic.controller.RegPartnerController;
import logic.model.Log;
import logic.model.RegPartnerBean;

public class RegPartnerView extends SuperView implements Initializable{
	
	private static final String TITLE = "Dovado - PartnerRegister";
	private static final String FILEFXML = "RegPartner.fxml";
	
	
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
		
    @FXML
    void login(ActionEvent event) {
    	Stage current = (Stage)((Node)event.getSource()).getScene().getWindow();
    	LoginView.render(current);
    }
    
    public static void render(Stage current) {
		SuperView.render(current, TITLE, FILEFXML, true, false);
	}
    
    public void register() {
    	
    	String password = pswField.getText();
    	String passwordCheck = pswField2.getText();
    	String username = usrnameTField.getText();
    	String email = emailTField.getText();
    			
		if(password.isEmpty() || passwordCheck.isEmpty() || username.isEmpty() || email.isEmpty()) {
			popupGen("One of the fields is empty!");
			return;
		}
		
		RegPartnerController controller = new RegPartnerController();
		RegPartnerBean regBean = new RegPartnerBean();
		
		regBean.setUsername(username);
		regBean.setEmail(email);
		regBean.setPassword(password);
		regBean.setPassword2(passwordCheck);
		regBean.setpIVA(pIVATField.getText());
		regBean.setCompName(compNameTField.getText());
		
		try {
			controller.validateForm(regBean);
		
			controller.addPartner(regBean);
		}catch (AuthException e) {
			popupGen(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
		}
    	LoginView.render(curr);
		
    	Log.getInstance().getLogger().info("Partner registered");
    	
    }
    
    

    @Override
	public void initialize(URL location, ResourceBundle resources) {
		signUpBtn.getStyleClass().add("src-btn");
		
	}
    
    public void switchToExplorerRegister() {
    	RegisterView.render(curr);
    }
    
    public void switchToLogin() {
    	LoginView.render(curr);
    }
}
