package logic.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
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
	
	private static final  String BGCOLORKEY = "ffffff";
	private static final  String STYLENAME = "textEventName";

	protected static Stage curr;

	
	
	
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
