package logic.model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

public class DAOExplorer {
	private static DAOExplorer instance;
	
	//----------database--------------------------------------
	
		private static final String USER = "dovado";
		private static final String PASSWORD = "dovadogang";
	    private static final String DB_URL = "jdbc:mariadb://localhost:3306/dovado";
	    private static final String DRIVER_CLASS_NAME = "org.mariadb.jdbc.Driver";
		
	 //------------------------------------------------------------
	    private static final String LOGDBCONN = "Connected database successfully...";
		private static final String LOGDBDISCONN = "Disconnetted database successfully...";
		
		private  Connection conn ;
		private  CallableStatement stmt;
		
	private DAOExplorer() {}
	
	public static DAOExplorer getInstance() {
		if(instance == null)
			instance = new DAOExplorer();
		return instance;
	}
	
	public User login(String email, String password) throws SQLException, ClassNotFoundException  {

        User u = null;
        
        try {
        	resetConnection();
            
            //STEP4.1: preparo la stored procedure
            String call =  "{call login_explorer(?,?)}";
            
            stmt = conn.prepareCall(call);
            
            stmt.setString(1,email);
            stmt.setString(2, password);
            
            if(!stmt.execute()) {
            	Log.getInstance().getLogger().warning("user non trovato");
            }
            
            
            ResultSet rs = stmt.getResultSet();
            
            
            while(rs.next()) {
	            u = new User(
	            		rs.getString("username"),
	            		rs.getString("email"),
	            		rs.getLong("id"),
	            		rs.getLong("wallet"));
	                      
	            boolean[] boolPref= {
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
	            	};
	            Preferences p = new Preferences(boolPref);
	            
	            
	            u.setPreferences(p);
            }
            
            
            
            rs.close();
            
        } finally {
        	//Clean-up dell'ambiente
        	disconnRoutine();
        } return u;
	}
	
	public void registerExplorer(String username, String email, String password, boolean[] pref) throws ClassNotFoundException, SQLException{
        
        try {
        	resetConnection();
            
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
        	//Clean-up dell'ambiente
        	disconnRoutine();
        }
	}
	
	public int getUserWallet(Long userID) throws ClassNotFoundException, SQLException{
		
        int wallet = 0;
        
        try {
        	resetConnection();
            
            //STEP4.1: preparo la stored procedure
            String call =  "{call get_wallet(?)}";
            
            stmt = conn.prepareCall(call);
            
            stmt.setLong(1,userID);
            
            if(!stmt.execute()) {   	
            	throw new SQLException("Nessun portafogli per questo utente");
            	}
            
            //ottengo il resultSet
            ResultSet rs = stmt.getResultSet();
            
            while(rs.next()) {
            	wallet = rs.getInt("wallet");
            }
            
            rs.close();
        }finally{
            //Clean-up dell'ambiente
        	disconnRoutine();
        }
    
        return wallet;
	}
	
	private void resetConnection() throws ClassNotFoundException, SQLException {
		conn = null;
		stmt = null;

		Class.forName(DRIVER_CLASS_NAME);

		// apertura connessione
        conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        Log.getInstance().getLogger().info(LOGDBCONN);
	}
	
	private void disconnRoutine() throws SQLException {
		
		try {
	        if (stmt != null)
	            stmt.close();
	    } catch (SQLException se2) {
	    	Log.getInstance().getLogger().warning("Errore di codice: "+ se2.getErrorCode() + " e mesaggio: " + se2.getMessage());
	    	se2.printStackTrace();
	    	throw se2;
	    }
	    try {
	        if (conn != null)
	            conn.close();
	    	Log.getInstance().getLogger().info(LOGDBDISCONN);

	    } catch (SQLException se) {
	    	Log.getInstance().getLogger().warning("Errore di codice: "+ se.getErrorCode() + " e mesaggio: " + se.getMessage());
	    	se.printStackTrace();
	    }
	}
	
}
