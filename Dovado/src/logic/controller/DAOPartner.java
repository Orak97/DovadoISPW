package logic.controller;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import logic.model.DAOExplorer;
import logic.model.Partner;
import logic.model.Preferences;
import logic.model.User;

public class DAOPartner {
	private static DAOPartner INSTANCE;
	//----------database--------------------------------------
	
	private static String USER = "dovado"; //DA CAMBIARE
	private static String PASSWORD = "dovadogang"; //DA CAMBIARE
	private static String DB_URL = "jdbc:mariadb://localhost:3306/dovado";
	private static String DRIVER_CLASS_NAME = "org.mariadb.jdbc.Driver";
				
	//------------------------------------------------------------

	private DAOPartner() {}
	
	public static DAOPartner getInstance() {
		if(INSTANCE == null)
			INSTANCE = new DAOPartner();
		return INSTANCE;
	}
	public static Partner getPartnerInfo(int owner) throws Exception {
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
            	Exception e = new Exception("Sembra che questo partner non esista");
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
                	System.out.println("Disconnetted database successfully...");
                	
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
		return partner;
	}
	
	public void registerPartner(String username, String email, String password, String partitaIva, String nomeAzienda) throws Exception{
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
            String call =  "{call create_partner(?,?,?,?,?)}";
            
            stmt = conn.prepareCall(call);
            
            stmt.setString(1,username);
            stmt.setString(2,email);
            stmt.setString(3,password);
            stmt.setString(4,partitaIva);
            stmt.setString(5,nomeAzienda);
            
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
	
	public Partner login(String email,String password) throws Exception {
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
            String call =  "{call login_partner(?,?)}";
            
            stmt = conn.prepareCall(call);
            
            stmt.setString(1,email);
            stmt.setString(2, password);
            
            if(!stmt.execute()) {
            	System.out.println("Partner non trovato");
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
        return partner;
	}

}
