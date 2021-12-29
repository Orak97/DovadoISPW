package logic.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import logic.controller.ActivityType;
import logic.controller.FindActivityController;

public class DAOActivity {
	
	private static DAOActivity INSTANCE;
	private DAOPlace daoPl;
	
	//----------database--------------------------------------
	
	private static String USER = "sav"; //DA CAMBIARE
	private static String PASSWORD = "pellegrini"; //DA CAMBIARE
	private static String DB_URL = "jdbc:mariadb://localhost:3306/dovado";
	private static String DRIVER_CLASS_NAME = "org.mariadb.jdbc.Driver";
			
	//------------------------------------------------------------
	
	
	private DAOActivity() {
		daoPl = DAOPlace.getInstance();
	}
	
	public static DAOActivity getInstance() {
		if(INSTANCE==null)
			INSTANCE = new DAOActivity();
		return INSTANCE;
	}
	
	public void createNormalActivity(String activityName, String description, String sito, String prezzo, int place, String proprietario, boolean arte, boolean cibo, boolean musica, boolean sport, boolean social, boolean natura, boolean esplorazione, boolean ricorrenza, boolean moda, boolean shopping, boolean adrenalina, boolean monumenti, boolean relax, boolean istruzione, String tipo, String orApertura, String orChiusura, String dataInizio, String dataFine, String cadenza) throws Exception {
		//metodo per creare nel db una classe Activity
		
		// STEP 1: dichiarazioni
        CallableStatement stmt = null;
        Connection conn = null;
        
        System.out.println("sono arrivato qui!");
        
        try {
        	// STEP 2: loading dinamico del driver mysql
            Class.forName(DRIVER_CLASS_NAME);
            
            // STEP 3: apertura connessione
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Connected database successfully...");
            
            //STEP4.1: preparo la stored procedure
            String call =  "{call create_activity(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            
            
            
            stmt = conn.prepareCall(call);
            
            stmt.setString(1,activityName);
            stmt.setString(2,description);
            stmt.setString(3,sito);
            stmt.setString(4,prezzo);
            stmt.setInt(5,place);
            stmt.setString(6,proprietario);
            stmt.setBoolean(7,arte);
            stmt.setBoolean(8,cibo);
            stmt.setBoolean(9,musica);
            stmt.setBoolean(10,sport);
            stmt.setBoolean(11,social);
            stmt.setBoolean(12,natura);
            stmt.setBoolean(13,esplorazione);
            stmt.setBoolean(14,ricorrenza);
            stmt.setBoolean(15,moda);
            stmt.setBoolean(16,shopping);
            stmt.setBoolean(17,adrenalina);
            stmt.setBoolean(18,monumenti);
            stmt.setBoolean(19,relax);
            stmt.setBoolean(20,istruzione);
            stmt.setString(21,tipo);
            stmt.setString(22,orApertura);
            stmt.setString(23,orChiusura);
            stmt.setString(24,dataInizio);
            stmt.setString(25,dataFine);
            stmt.setString(26,cadenza);
            
            stmt.execute();
            
        }catch(Exception e){
        	e.printStackTrace();
        }finally {
            // STEP 5.2: Clean-up dell'ambiente
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            	throw(se2);
            }
            try {
                if (conn != null)
                    conn.close();
                	System.out.println("Disconnetted database successfully...");
                	
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        
		
	}
	
}
