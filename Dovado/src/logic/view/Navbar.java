package logic.view;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import logic.model.SuperUser;

/*
 * simple singleton implementation of the navbar due to:
 * 1- i need to use navbar from almost every page and there is no point on instance a different navbar for every page
 * 2- i didn't used The multithread safe navbar since i can't use more than one active program on the same computer
 */
public class Navbar {
	private static final  String NAVITEMKEY = ".navbar-item";
	private static final  String ACTSTYLEKEY = "active";
	
	private static BorderPane bPNavbar = null;
	private static SuperUser user;
	
	public static BorderPane getNavbar() {
		if(Navbar.bPNavbar == null) 
			try {
				Navbar.bPNavbar = FXMLLoader.load(Main.class.getResource("navbar.fxml"));
				Navbar.loginSetup();
			} catch(IOException e) {
				e.printStackTrace();
			}
		return bPNavbar;
	}
	
	public static double getHeight() {
		try {
		return Navbar.getNavbar().getScene().getHeight();
		} catch(Exception e) {
			return 480;
		}
		
	}
	
	public static double getWidth() {
		try{
		return Navbar.getNavbar().getScene().getWidth();
		} catch(Exception e) {
			return 640;
		}
	}
	
	public static void loginSetup() {
	  	for(Node curr : bPNavbar.lookupAll(NAVITEMKEY)) {
	  		curr.setDisable(true);
	    	curr.setVisible(false);
	    }
	}
	    
	public static void authenticatedSetup() {
		for(Node curr : bPNavbar.lookupAll(NAVITEMKEY)) {
	       	curr.setDisable(false);
	       	curr.setVisible(true);
	    }
	}
	
	public void onHome() {
		this.changePage();
		bPNavbar.lookup("#home").getStyleClass().add(ACTSTYLEKEY);
	}
	
	public void onChannels() {
		this.changePage();
		bPNavbar.lookup("#channels").getStyleClass().add(ACTSTYLEKEY);
	}

	public void onCreateActivity() {
		this.changePage();
		bPNavbar.lookup("#createActivity").getStyleClass().add(ACTSTYLEKEY);
	}
	
	public void onEvents() {
		this.changePage();
		bPNavbar.lookup("#events").getStyleClass().add(ACTSTYLEKEY);
	}
	
	public void onPreferences() {
		this.changePage();
		bPNavbar.lookup("#myProfile").getStyleClass().add(ACTSTYLEKEY);
	}
	
	public void onMyProfile() {
		this.changePage();
		bPNavbar.lookup("#myProfile").getStyleClass().add(ACTSTYLEKEY);
	}
	
	private void changePage() {
		for(Node curr : bPNavbar.lookupAll(NAVITEMKEY)) {
	  		curr.getStyleClass().remove(ACTSTYLEKEY);
	    }
	}

	public static void setUser(SuperUser usr) {
		user = usr;
	}

	public static SuperUser getUser() {
		return user;
	}

	
}
