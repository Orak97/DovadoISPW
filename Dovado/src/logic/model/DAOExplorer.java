package logic.model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

public class DAOExplorer {
	private static DAOExplorer INSTANCE;
	
	//----------database--------------------------------------
	
		private static String USER = "dovado";
		private static String PASSWORD = "dovadogang";
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
        	
        	
        	// STEP 2: loading dinamico del driver mysql
            Class.forName(DRIVER_CLASS_NAME);
            
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
	            
	            
	            Preferences p = new Preferences(
	            rs.getBoolean("Arte"),
	        	rs.getBoolean("Cibo"),
	        	rs.getBoolean("Musica"),
	        	rs.getBoolean("Sport"),
	        	rs.getBoolean("Social"),
	        	rs.getBoolean("Natura"),
	        	rs.getBoolean("Esplorazione"),
	        	rs.getBoolean("Ricorrenze_locali"),
	        	rs.getBoolean("Moda"),
	        	rs.getBoolean("Shopping"),
	        	rs.getBoolean("Adrenalina"),
	        	rs.getBoolean("Relax"),
	        	rs.getBoolean("Istruzione"),
	        	rs.getBoolean("Monumenti")
	            );
	            
	            
	            u.setPreferences(p);
            }
            
            
            
            rs.close();
            
            
        }catch(Exception e){
        	e.printStackTrace();
        }
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
	
	public void registerExplorer(String username, String email, String password, boolean[] pref) throws Exception{
		// STEP 1: dichiarazioni
        CallableStatement stmt = null;
        Connection conn = null;
        
        try {
        	// STEP 2: loading dinamico del driver mysql
            Class.forName(DRIVER_CLASS_NAME);
            
            // STEP 3: apertura connessione
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Connected database successfully...");
            
            //STEP4.1: preparo la stored procedure
            String call =  "{call create_explorer(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            
            stmt = conn.prepareCall(call);
            
            stmt.setString(1,username);
            stmt.setString(2,email);
            stmt.setString(3,password);
            for (int i = 0; i < pref.length; i++) {
				stmt.setBoolean(i+4, pref[i]);
			}
            
            
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
