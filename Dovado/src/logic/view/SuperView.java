package logic.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Stage;

public abstract class SuperView {
	
	protected static final  String BTNPREFKEY = "pref-btn";
	protected static final  String BTNSRCKEY = "src-btn";
	protected static final  String BTNEVNKEY = "evn-btn";
	private static final long WPOPUP = 500;
	private static final long HPOPUP = 50;
	private static final  String BGCOLORKEY = "ffffff";
	private static final  String STYLENAME = "textEventName";
	

	protected static Stage curr;

	
	
	
	public static void render(Stage current,String title, String fileFXML, boolean isVertical, boolean isAuth) {
		try {
			VBox root = new VBox();
			BorderPane navbar = Navbar.getNavbar();
			
			if(isAuth)Navbar.authenticatedSetup();
			
			curr=current;
			
			Scene scene = new Scene(root,Navbar.getWidth(),Navbar.getHeight());
			scene.getStylesheets().add(Main.class.getResource("Dovado.css").toExternalForm());
			current.setTitle(title);
			current.setScene(scene);
			if(isVertical) {
				VBox view = FXMLLoader.load(Main.class.getResource(fileFXML));
				VBox.setVgrow(view, Priority.SOMETIMES);
				root.getChildren().addAll(navbar,view);
				
			} else {
				HBox view = FXMLLoader.load(Main.class.getResource(fileFXML));
				VBox.setVgrow(view, Priority.SOMETIMES);
				root.getChildren().addAll(navbar,view);
			}
			
			
			current.show();	
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public Popup popupGen(String error) {
		return popupGen(WPOPUP, HPOPUP, error);
	}
	
	public Popup popupGen(double width, double height, String error) {
		Popup popup = new Popup(); 
		popup.centerOnScreen();
		
		Text errorTxt = new Text(error);
    	errorTxt.setWrappingWidth(width - 10);

		errorTxt.getStyleClass().add(STYLENAME);
		errorTxt.setTextAlignment(TextAlignment.CENTER);
		errorTxt.setWrappingWidth(480);
	    
	    Rectangle r = new Rectangle(width, height, Color.valueOf("212121"));
	    StackPane popupContent = new StackPane(r,errorTxt); 
	    
	    r.setStrokeType(StrokeType.OUTSIDE);
	    r.setStrokeWidth(0.3);
	    r.setStroke(Paint.valueOf(BGCOLORKEY));
	    
	    popup.getContent().add(popupContent);
	    popup.centerOnScreen(); 
	    
	    popup.show(curr);
	    popup.setAutoHide(true);
	    return popup;
	}
}
