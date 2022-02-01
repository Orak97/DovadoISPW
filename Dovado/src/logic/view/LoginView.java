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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.controller.LogExplorerController;
import logic.controller.LogPartnerController;
import logic.model.Log;
import logic.model.LogBean;
import logic.model.Partner;
import logic.model.SuperUser;
import logic.model.User;

public class LoginView{
	
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
    	
    	if(radioPartner.isSelected()) {
    		user =logPartner(event);
    		Log.getInstance().getLogger().info("voglio accedere come partner");
    	} 
    	else if(radioExplorer.isSelected()) {
    		user = logExplorer(event);
			Log.getInstance().getLogger().info("voglio accedere come explorer");
    	} else {
    		fail.setText("Selezionare una modalità \n di accesso");
    		fail.setVisible(true);
    	}
    	if (user != null) {
    	Stage current = (Stage)((Node)event.getSource()).getScene().getWindow();
    	//HomeView hv = new HomeView();
    	Navbar.setUser(user);
    	HomeView.render(current);
    	}
    }
    
    private User logExplorer(ActionEvent event) {
    	User user = null;
    	LogExplorerController logExp = new LogExplorerController();
    	LogBean  logbean = new LogBean();
    	logbean.setEmail(username.getText());
    	logbean.setPassword(password.getText());
    	Log.getInstance().getLogger().info("Lo username dell'esploratore è: " +username.toString());
    	try {
			if( (user = logExp.loginExplorer(logbean)) ==null) {
				fail.setText("Mail o password non corrette");
	    		fail.setVisible(true);
	    		
				Log.getInstance().getLogger().info("Email o password incorrette.");
				return user;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return user;
		}
    	return user;
    }
    
    private Partner logPartner(ActionEvent event) {
    	Partner user = null;
    	LogPartnerController logPartner = new LogPartnerController();
    	LogBean  logbean = new LogBean();
    	logbean.setEmail(username.getText());
    	logbean.setPassword(password.getText());
    	Log.getInstance().getLogger().info("Lo username del partner è: " +username.toString());
    	try {
			if( (user = logPartner.loginPartner(logbean)) ==null) {
				fail.setText("Mail o password non corrette");
	    		fail.setVisible(true);
	    		
				Log.getInstance().getLogger().info("Email o password incorrette.");
				return user;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return user;
		}
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
		try {
			VBox root = new VBox();
			BorderPane navbar = Navbar.getNavbar();
			Scene scene = new Scene(root,Navbar.getWidth(),Navbar.getHeight());
			scene.getStylesheets().add(Main.class.getResource("Dovado.css").toExternalForm());
			primaryStage.setTitle("Dovado - login");
			primaryStage.setScene(scene);
			GridPane login = new GridPane();
			
			login = FXMLLoader.load(Main.class.getResource("Login.fxml"));
			
			root.getChildren().addAll(navbar,login);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		} 
	}

}