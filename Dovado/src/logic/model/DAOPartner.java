package logic.model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOPartner {
	private static DAOPartner INSTANCE;
	//----------database--------------------------------------
	
	private static final String USER = "dovado"; //DA CAMBIARE
	private static final String PASSWORD = "dovadogang"; //DA CAMBIARE
	private static final String DB_URL = "jdbc:mariadb://localhost:3306/dovado";
	private static final String DRIVER_CLASS_NAME = "org.mariadb.jdbc.Driver";
				
	//------------------------------------------------------------
	private static final String LOGDBCONN = "Connected database successfully...";
	private static final String LOGDBDISCONN = "Disconnetted database successfully...";
		
	private  Connection conn ;
	private  CallableStatement stmt;
		
	
	private DAOPartner() {}
	
	public static DAOPartner getInstance() {
		if(INSTANCE == null)
			INSTANCE = new DAOPartner();
		return INSTANCE;
	}
	
	//TODO PERCHÉ STATICO QUESTO METODO?
	public static Partner getPartnerInfo(int owner) throws SQLException, ClassNotFoundException {
		// STEP 1: dichiarazioni
        CallableStatement stmt = null;
        Connection conn = null;
        
        Partner partner = null;
		
        try {
        	// STEP 2: loading dinamico del driver mysql
            Class.forName(DRIVER_CLASS_NAME);
            
            // STEP 3: apertura connessione
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Connected database successfully...");
            
            //STEP4.1: preparo la stored procedure
            String call = "{call get_partner_info(?)}";
            
            stmt = conn.prepareCall(call);
            
            stmt.setInt(1, owner);
            
            if(!stmt.execute()) {
            	SQLException e = new SQLException("Sembra che questo partner non esista");
            	throw e;
            }
            
          //ottengo il resultSet
          ResultSet rs = stmt.getResultSet();
          
          while(rs.next()) {
        	  Long id = Long.valueOf(rs.getInt("idPartner"));
        	  String nomeAzienda = rs.getString("nome_azienda");
        	  String piva = rs.getString("partita_iva");
        	  partner = new Partner(id,nomeAzienda,piva);
          }
           
          rs.close();
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
            	Log.getInstance().getLogger().info(LOGDBDISCONN);
                	
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
		return partner;
	}
	
	//Ha senso NON  ritornare un booleano?
	public void registerPartner(String username, String email, String password, String partitaIva, String nomeAzienda) throws ClassNotFoundException, SQLException {
        
        try {
        	resetConnection();
            
            //STEP4.1: preparo la stored procedure
            String call =  "{call create_partner(?,?,?,?,?)}";
            
            stmt = conn.prepareCall(call);
            
            stmt.setString(1,username);
            stmt.setString(2,email);
            stmt.setString(3,password);
            stmt.setString(4,partitaIva);
            stmt.setString(5,nomeAzienda);
            
            stmt.execute();
        }finally {
        	//Clean-up dell'ambiente
        	disconnRoutine();
        }
	}
	
	public Partner login(String email,String password) throws ClassNotFoundException, SQLException {
        
        Partner partner = null;
        
        try {
        	resetConnection();
            
            //STEP4.1: preparo la stored procedure
            String call =  "{call login_partner(?,?)}";
            
            stmt = conn.prepareCall(call);
            
            stmt.setString(1,email);
            stmt.setString(2, password);
            
            if(!stmt.execute()) {
            	Log.getInstance().getLogger().info("Partner non trovato");
            };
     
            ResultSet rs = stmt.getResultSet();
                        
            while(rs.next()) {
	            partner = new Partner(
	            			rs.getString("username"),
	            			rs.getString("email"),
	            			rs.getLong("idPartner"),
	            			rs.getString("nome_azienda"),
	            			rs.getString("partita_iva")
	            		  );
            }
            rs.close();
        }
        finally {
            //Clean-up dell'ambiente
        	disconnRoutine();
           
        }
        return partner;
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
