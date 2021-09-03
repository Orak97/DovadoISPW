package logic.view;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import logic.model.Log;
import javafx.event.ActionEvent;

public class NavbarController {
	
	private Navbar nav= new Navbar();

    @FXML
    private MenuButton menuButton;
    
    @FXML
    private ImageView imageView;

    @FXML
    private MenuItem logout_input;
    
    @FXML
    void Logout(ActionEvent event) {
    	Log.getInstance().logger.info("Clicked Logout");
    	nav.onHome();
    	Stage current = (Stage)((Node)Navbar.getNavbar()).getScene().getWindow();
    	Navbar.loginSetup();
    	LoginView.render(current);
    }

    @FXML
    void goChannels(ActionEvent event) {
    	nav.onChannels();
    	Log.getInstance().logger.info("Clicked channels");
    }

    @FXML
    void goHome(ActionEvent event) {
    	nav.onHome();
    	Log.getInstance().logger.info("Clicked home");
    }

    @FXML
    void goProfile(ActionEvent event) {
    	nav.onMyProfile();
    	Log.getInstance().logger.info("Clicked My profile");
    }

    @FXML
    void goPreferences(ActionEvent event) {
    	nav.onPreferences();
    	Log.getInstance().logger.info("Clicked preferences");
    }

    @FXML
    void goEvents(ActionEvent event) {
    	nav.onEvents();
    	Log.getInstance().logger.info("Clicked vote");
    }
    

}
