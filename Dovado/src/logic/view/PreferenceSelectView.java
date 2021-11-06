package logic.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import logic.model.DAOPreferences;

public class PreferenceSelectView implements Initializable{

	@FXML
	private Button continueBtn;

	@FXML
	private GridPane preferencesGrid;
	
	private static ArrayList<String> preferences;
	private static ArrayList<String> preferencesChosen;

	private static Stage curr;
	private DAOPreferences daoPr;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		daoPr = DAOPreferences.getInstance();
		preferences = (ArrayList<String>) daoPr.getAllPreferencesFromJSON();
		preferencesChosen = new ArrayList<String>();
		ArrayList<Button> prefBtns = new ArrayList<Button>();

		continueBtn.getStyleClass().add("src-btn");
		
		int col = 0;
		int row = 0;
		
		double rows_cols = Math.sqrt(preferences.size());
		int rc = (int) Math.round(rows_cols);
		
		System.out.println("Rows and columns: "+rows_cols+" Square root: "+rc);
		
		
		for(int i=0;i<preferences.size();i++) {
			Button newBtn = new Button();
			
			Circle c = new Circle(0, 0, 50, Color.valueOf("212121"));
			
		    newBtn.setShape(c);
		    
		    newBtn.setText(preferences.get(i));
		    newBtn.setTextFill(Paint.valueOf("000000"));
		    newBtn.getStyleClass().add("src-btn");
		    newBtn.getStyleClass().add("pref-sel-btn");
		    
		    newBtn.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					if(preferencesChosen.contains(newBtn.getText())) {
						preferencesChosen.remove(newBtn.getText());
						return;
					}
					preferencesChosen.add(newBtn.getText());
				}
		    	
		    });
		    //Se sto alla prima preferenza aggiunta alla
		    //griglia, allora non ho bisogno di aggiungere
		    //righe.
		    if(i==0 && col==0) {
		    	preferencesGrid.add(newBtn, col, row);
		    } else {
		    	preferencesGrid.add(newBtn, col, row);
		    }
		    //Se ho finito di inserire bottoni sulla riga
		    //aggiungo una colonna.
		    row++;
		    if(i%rc == 0 && i!=0) {
		    	col++;
		    	row=0;
		    	System.out.println("column updated: "+i%rc+"col: "+col);
		    }
		}
		
	}

	public void goHomeView() {
		Navbar.getUser().setPreferences((List<String>)preferencesChosen);
		
    	HomeView.render(curr);
		
	}

	public static void render(Stage current) {
		Stage primaryStage = current;

		curr=current;
		
		try {
			VBox root = new VBox();
			BorderPane navbar = Navbar.getNavbar();
			Scene scene = new Scene(root,Navbar.getWidth(),Navbar.getHeight());
			scene.getStylesheets().add(Main.class.getResource("Dovado.css").toExternalForm());
			primaryStage.setTitle("Dovado - Select preferences");
			primaryStage.setScene(scene);
			VBox home = new VBox();
	
			home = FXMLLoader.load(Main.class.getResource("PreferenceSelect.fxml"));
			
			root.getChildren().addAll(navbar,home);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
