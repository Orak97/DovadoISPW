package logic.model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DAO {

	//----------database--------------------------------------
	
	private static final String USER = "dovado"; //DA CAMBIARE
	private static final String PASSWORD = "dovadogang"; //DA CAMBIARE
	private static final String DB_URL = "jdbc:mariadb://localhost:3306/dovado";
	private static final String DRIVER_CLASS_NAME = "org.mariadb.jdbc.Driver";
			
	//----------log message-----------------------------------------------
	private static final String LOGDBCONN = "Connected database successfully...";
	private static final String LOGDBDISCONN = "Disconnetted database successfully...";
	
	//--------------------------------------------------------------
	public Connection conn; 
	public CallableStatement stmt;
	
	public void resetConnection() throws ClassNotFoundException, SQLException {
		conn = null;
		stmt = null;

		Class.forName(DRIVER_CLASS_NAME);

		// apertura connessione
        conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        Log.getInstance().getLogger().info(LOGDBCONN);
	}
	
	public void disconnRoutine() throws SQLException {
		
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
