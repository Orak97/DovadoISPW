package logic.model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DAOExplorer {
	private static DAOExplorer INSTANCE;
	
	//----------database--------------------------------------
	
		private static String USER = "login";
		private static String PASSWORD = "login";
	    private static String DB_URL = "jdbc:mariadb://localhost:3306/dovado";
	    private static String DRIVER_CLASS_NAME = "org.mariadb.jdbc.Driver";
		
	 //------------------------------------------------------------
	
	private DAOExplorer() {}
	
	public static DAOExplorer getInstance() {
		if(INSTANCE == null)
			INSTANCE = new DAOExplorer();
		return INSTANCE;
	}
	
	public User login(String email, String password) throws Exception {
		// STEP 1: dichiarazioni
        CallableStatement stmt = null;
        Connection conn = null;
        
        User u = null;
        
        try {
        	
        	System.out.println("here1");
        	// STEP 2: loading dinamico del driver mysql
            Class.forName(DRIVER_CLASS_NAME);
            System.out.println("here");
            // STEP 3: apertura connessione
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Connected database successfully...");
            
            //STEP4.1: preparo la stored procedure
            String call =  "{call login_explorer(?,?)}";
            
            stmt = conn.prepareCall(call);
            
            stmt.setString(1,email);
            stmt.setString(2, password);
            
            if(!stmt.execute()) {
            	System.out.println("user non trovato");
            };
            
            
            ResultSet rs = stmt.getResultSet();
            
            
            while(rs.next()) {
            u = new User(
            		rs.getString("username"),
            		rs.getString("email"),
            		rs.getLong("id"),
            		rs.getLong("wallet"));
            }
            
            rs.close();
            System.out.println(u.getUsername());
            
        }catch(Exception e){e.printStackTrace();}
        finally {
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
            return u;
        }
	}
	
}
