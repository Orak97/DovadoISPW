package logic.model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOPartner extends DAO{
	private static DAOPartner instance;
	
	private DAOPartner() {}
	
	public static DAOPartner getInstance() {
		if(instance == null)
			instance = new DAOPartner();
		return instance;
	}
	
	public Partner getPartnerInfo(int owner) throws SQLException, ClassNotFoundException {
       
        Partner partner = null;
		
        try {
        	resetConnection();
            
            //STEP4.1: preparo la stored procedure
            String call = "{call get_partner_info(?)}";
            
            stmt = conn.prepareCall(call);
            
            stmt.setInt(1, owner);
            
            if(!stmt.execute()) {
            	throw new SQLException("Sembra che questo partner non esista");
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
        	//Clean-up dell'ambiente
            disconnRoutine();
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
            }
     
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
	
}
