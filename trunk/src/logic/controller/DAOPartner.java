package logic.controller;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import logic.model.Partner;

public class DAOPartner {
	
	//----------database--------------------------------------
	
	private static String USER = "sav"; //DA CAMBIARE
	private static String PASSWORD = "pellegrini"; //DA CAMBIARE
	private static String DB_URL = "jdbc:mariadb://localhost:3306/dovado";
	private static String DRIVER_CLASS_NAME = "org.mariadb.jdbc.Driver";
				
	//------------------------------------------------------------

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

}
