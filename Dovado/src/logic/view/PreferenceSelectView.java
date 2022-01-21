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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import logic.model.DAOPreferences;
import logic.model.Preferences;
import logic.model.User;

public class PreferenceSelectView implements Initializable{

	@FXML
	private Button continueBtn;

	@FXML
	private GridPane preferencesGrid;
	
	private static Preferences preferences;
	private static boolean[] preferencesChosen;

	private static Stage curr;
	private DAOPreferences daoPr;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		daoPr = DAOPreferences.getInstance();
		preferences = new Preferences(false, false, false, false, false, false, false, false, false, false, false, false, false, false);
		String[] prefsNames = preferences.getPreferencesName();
		preferencesChosen = preferences.getSetPreferences();
		ArrayList<Button> prefBtns = new ArrayList<Button>();

		continueBtn.getStyleClass().add("src-btn");
		
		int col = 0;
		int row = 0;
		
		double rows_cols = Math.sqrt(prefsNames.length);
		int rc = (int) Math.round(rows_cols);
		
		System.out.println("Rows and columns: "+rows_cols+" Square root: "+rc);
		
		
		for(int i=0;i<prefsNames.length;i++) {
			Button newBtn = new Button();
			
			Circle c = new Circle(0, 0, 50, Color.valueOf("212121"));
			
		    newBtn.setShape(c);
		    
		    newBtn.setText(prefsNames[i]);
		    newBtn.setTextFill(Paint.valueOf("000000"));
		    newBtn.getStyleClass().add("pref-btn");
		    //Nell'id del bottone nascondo informazioni che mi servono per capire 1 se è
		    //Stato cliccato e in più a quale preferenza faccia riferimento.
		    newBtn.setId(String.valueOf(0)+"-"+String.valueOf(i));
		    
		    newBtn.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					
					String btnInfo = newBtn.getId();
					String[] btnSplit = btnInfo.split("-");
					
					int i=Integer.parseInt(btnSplit[1]);
					//Se il booleano di questa specifica preferenza è già true nel bottone,
					//questo significa che è stato selezionato due volte il bottone; pertanto lo si 
					//converte a false.
					if(preferencesChosen[i]==(Boolean.valueOf(newBtn.getId())) && preferencesChosen[i]==true) {
						preferencesChosen[i]=false;
						
//						CornerRadii cr = new CornerRadii(4);
//						BackgroundFill bf = new BackgroundFill(Paint.valueOf("BC9416"), cr, null);
//						Background b = new Background(bf);
//						newBtn.setBackground(b);
						newBtn.getStyleClass().clear();
						
						int k = (Integer.parseInt(btnSplit[0])+1)%2;
						newBtn.setId(String.valueOf(k)+"-"+btnSplit[1]);
						
						newBtn.getStyleClass().add("pref-btn");
					    System.out.println("Hai già scelto questa preferenza, deselezionando...");
						return;
					}
					preferencesChosen[i]=true;
					
//					CornerRadii cr = new CornerRadii(4);
//					BackgroundFill bf = new BackgroundFill(Paint.valueOf("212121"), cr, null);
//					Background b = new Background(bf);
//					newBtn.setBackground(b);
					
					int k = (Integer.parseInt(btnSplit[0])+1)%2;
					newBtn.setId(String.valueOf(k)+"-"+btnSplit[1]);

					newBtn.getStyleClass().clear();
					
					newBtn.getStyleClass().add("pref-sel-btn");
					System.out.println("Hai scelto questa preferenza, selezionando...");
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

		preferences = new Preferences(preferencesChosen[0], preferencesChosen[1], preferencesChosen[2], preferencesChosen[3], preferencesChosen[4], preferencesChosen[5], preferencesChosen[6], preferencesChosen[7], preferencesChosen[8], preferencesChosen[9], preferencesChosen[10], preferencesChosen[11], preferencesChosen[12],preferencesChosen[13]);
		
		((User)Navbar.getUser()).setPreferences(preferences);
		
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
