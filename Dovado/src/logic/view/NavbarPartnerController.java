package logic.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import logic.model.Log;

public class NavbarPartnerController {
	
	private NavbarPartner nav = new NavbarPartner();

    @FXML
    private MenuButton menuButton;
    
    @FXML
    private ImageView imageView;

    @FXML
    private MenuItem logoutInput;
    
    @FXML
    void Logout(ActionEvent event) {
    	Log.getInstance().getLogger().info("Clicked Logout");
    	nav.onHome();
    	Stage current = (Stage)((Node)NavbarPartner.getNavbarPart()).getScene().getWindow();
    	NavbarPartner.loginSetup();
    	LoginView.render(current);
    }

    @FXML
    void goChannels(ActionEvent event) {
    	nav.onChannels();
    	Log.getInstance().getLogger().info("Clicked channels");
    }

    @FXML
    void goHome(ActionEvent event) {
    	nav.onHome();
    	Log.getInstance().getLogger().info("Clicked home");
    	Stage current = (Stage)((Node)NavbarPartner.getNavbarPart()).getScene().getWindow();
    	NavbarPartner.loginSetup();
    	//HomeView hv = new HomeView();
    	HomeView.render(current);
    }

    @FXML
    void goCreateActivity(ActionEvent event) {
    	nav.onCreateActivity();
    	Log.getInstance().getLogger().info("Clicked create activity");
    	Stage current = (Stage)((Node)NavbarPartner.getNavbarPart()).getScene().getWindow();
    	NavbarPartner.loginSetup();
    	//HomeView hv = new HomeView();
    	CreateActivityView.render(current);
    }
 
}
