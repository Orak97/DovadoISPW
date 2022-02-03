package logic.view;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import logic.model.Partner;
import logic.model.SuperUser;

/*
 * simple singleton implementation of the navbar due to:
 * 1- i need to use navbar from almost every page and there is no point on instance a different navbar for every page
 * 2- i didn't used The multithread safe navbar since i can't use more than one active program on the same computer
 */
public class NavbarPartner {
	private static final  String NAVITEMKEY = ".navbar-item";
	private static final  String ACTSTYLEKEY = "active";
	private static final  String EVNTPARTNER = "My events";
	
	private static BorderPane bPNavbarPartner = null;
	private static SuperUser user;
	
	public static BorderPane getNavbarPart() {
		if(NavbarPartner.bPNavbarPartner == null) 
			try {
				NavbarPartner.bPNavbarPartner = FXMLLoader.load(Main.class.getResource("navbarPartner.fxml"));
				NavbarPartner.loginSetup();
			} catch(IOException e) {
				e.printStackTrace();
			}
		return bPNavbarPartner;
	}
	
	public static double getHeight() {
		try {
		return NavbarPartner.getNavbarPart().getScene().getHeight();
		} catch(Exception e) {
			return 480;
		}
		
	}
	
	public static double getWidth() {
		try{
		return NavbarPartner.getNavbarPart().getScene().getWidth();
		} catch(Exception e) {
			return 640;
		}
	}
	
	public static void loginSetup() {
	  	for(Node curr : bPNavbarPartner.lookupAll(NAVITEMKEY)) {
	  		curr.setDisable(true);
	    	curr.setVisible(false);
	    }
	}
	    
	public static void authenticatedSetup() {
		for(Node curr : bPNavbarPartner.lookupAll(NAVITEMKEY)) {
	       	curr.setDisable(false);
	       	curr.setVisible(true);
	    }
	}

	public void onHome() {
		this.changePage();
		bPNavbarPartner.lookup("#home").getStyleClass().add(ACTSTYLEKEY);
	}
	

	public void onChannels() {
		this.changePage();
		bPNavbarPartner.lookup("#channels").getStyleClass().add(ACTSTYLEKEY);
	}

	public void onCreateActivity() {
		this.changePage();
		bPNavbarPartner.lookup("#createActivity").getStyleClass().add(ACTSTYLEKEY);
	}
	
	private void changePage() {
		for(Node curr : bPNavbarPartner.lookupAll(NAVITEMKEY)) {
	  		curr.getStyleClass().remove(ACTSTYLEKEY);
	    }
	}

	public static void setUser(SuperUser usr) {
		user = usr;

		HBox selections = (HBox)NavbarPartner.getNavbarPart().getChildren().get(2);
		Button activity = (Button)selections.getChildren().get(2);
		activity.setText(EVNTPARTNER);
	}

	public static SuperUser getUser() {
		return user;
	}

	
}
