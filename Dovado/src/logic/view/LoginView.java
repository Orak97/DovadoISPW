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
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.model.DAOSuperUser;
import logic.model.Log;
import logic.model.SuperUser;

public class LoginView{
	
    @FXML
    private Button loginInput;

    @FXML
    private Button registerInput;

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
    	if(username.getText().contains("@")) {
    		System.out.println(username);
    		if((user = DAOSuperUser.getInstance().findSuperUser(username.getText(), password.getText(),null))==null) {
    			Log.getInstance().getLogger().info("Email o password incorrette.");
    			return;
    		} 
    	}
    	// Questo else diventa inutile visto che non possiamo cercare gli utenti in base agli Username
    	else {
    		if((user=DAOSuperUser.getInstance().findSuperUser(username.getText(), password.getText(),null))==null) {
    			Log.getInstance().getLogger().info("Username o password incorretti.");
    			return;
    		}
    	}
    	Stage current = (Stage)((Node)event.getSource()).getScene().getWindow();
    	//HomeView hv = new HomeView();
    	Navbar.setUser(user);
    	HomeView.render(current);
    }

    @FXML
    void register(ActionEvent event) {
    	Log.getInstance().getLogger().info("Clicked register");
    	Stage current = (Stage)((Node)event.getSource()).getScene().getWindow();
    	RegisterView.render(current);
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
			try {
				login = FXMLLoader.load(Main.class.getResource("Login.fxml"));
			} catch(IOException e) {
				e.printStackTrace();
			}
			root.getChildren().addAll(navbar,login);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		} 
	}

}