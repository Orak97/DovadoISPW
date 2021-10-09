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
    private MenuItem logoutInput;
    
    @FXML
    void Logout(ActionEvent event) {
    	Log.getInstance().getLogger().info("Clicked Logout");
    	nav.onHome();
    	Stage current = (Stage)((Node)Navbar.getNavbar()).getScene().getWindow();
    	Navbar.loginSetup();
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
    	Stage current = (Stage)((Node)Navbar.getNavbar()).getScene().getWindow();
    	Navbar.loginSetup();
    	//HomeView hv = new HomeView();
    	HomeView.render(current);
    }

    @FXML
    void goCreateActivity(ActionEvent event) {
    	nav.onCreateActivity();
    	Log.getInstance().getLogger().info("Clicked create activity");
    	Stage current = (Stage)((Node)Navbar.getNavbar()).getScene().getWindow();
    	Navbar.loginSetup();
    	//HomeView hv = new HomeView();
    	CreateActivityView.render(current);
    }
    
    @FXML
    void goProfile(ActionEvent event) {
    	nav.onMyProfile();
    	Log.getInstance().getLogger().info("Clicked go profile");
    	Stage current = (Stage)((Node)Navbar.getNavbar()).getScene().getWindow();
    	Navbar.loginSetup();
    	
    	ProfileView.render(current); }

    @FXML
    void goPreferences(ActionEvent event) {
    	nav.onPreferences();
    	Log.getInstance().getLogger().info("Clicked preferences");
    }

    @FXML
    void goEvents(ActionEvent event) {
    	nav.onEvents();
    	Stage current = (Stage)((Node)Navbar.getNavbar()).getScene().getWindow();
    	Navbar.loginSetup();
    	//EventsView ev = new EventsView();
    	EventsView.render(current);
    	Log.getInstance().getLogger().info("Clicked events");
    }
    
    @FXML
    void goSpotPlace(ActionEvent event) {
    	nav.onSpotPlace();
    	Stage current = (Stage)((Node)Navbar.getNavbar()).getScene().getWindow();
    	Navbar.loginSetup();
    	SpotPlaceView sv = new SpotPlaceView();
    	SpotPlaceView.render(current);
    	Log.getInstance().getLogger().info("Clicked Spot place");
    }
    

}
