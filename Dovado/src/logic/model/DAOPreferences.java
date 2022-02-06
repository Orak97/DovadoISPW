package logic.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DAOPreferences {
	
	private static DAOPreferences INSTANCE;
	
	//----------database--------------------------------------
	
	private static String USER = "dovado"; //DA CAMBIARE
	private static String PASSWORD = "dovadogang"; //DA CAMBIARE
	private static String DB_URL = "jdbc:mariadb://localhost:3306/dovado";
	private static String DRIVER_CLASS_NAME = "org.mariadb.jdbc.Driver";
			
	//------------------------------------------------------------
	
	
	
	private DAOPreferences() {
	}
	
	public static DAOPreferences getInstance() {
		if(INSTANCE==null)
			INSTANCE = new DAOPreferences();
		return INSTANCE;
	}

	public void updateUserPreferences(Long id, boolean arte, boolean cibo, boolean musica, boolean sport,
			boolean social, boolean natura, boolean esplorazione, boolean ricorrenze, boolean moda, boolean shopping,
			boolean adrenalina, boolean relax, boolean istruzione, boolean monumenti) throws ClassNotFoundException, SQLException {
		
		CallableStatement stmt = null;
        Connection conn = null;
        
        try {
        	// STEP 2: loading dinamico del driver mysql
            Class.forName(DRIVER_CLASS_NAME);
            
            // STEP 3: apertura connessione
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Connected database successfully...");
            
            //STEP4.1: preparo la stored procedure
            String call =  "{call update_user_Preferences(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            
            stmt = conn.prepareCall(call);
            
            stmt.setLong(1,id);
            stmt.setBoolean(2,arte);
            stmt.setBoolean(3,cibo);
            stmt.setBoolean(4,musica);
            stmt.setBoolean(5,sport);
            stmt.setBoolean(6,social);
            stmt.setBoolean(7,natura);
            stmt.setBoolean(8,esplorazione);
            stmt.setBoolean(9,ricorrenze);
            stmt.setBoolean(10,moda);
            stmt.setBoolean(11,shopping);
            stmt.setBoolean(12,adrenalina);
            stmt.setBoolean(13,monumenti);
            stmt.setBoolean(14,relax);
            stmt.setBoolean(15,istruzione);
            
            stmt.execute();
            
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
