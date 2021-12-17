package logic.model;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class DAOPlace {
	
	private static DAOPlace INSTANCE;
	
	//----------database--------------------------------------
	
	private static String USER = "sav"; //DA CAMBIARE
	private static String PASSWORD = "pellegrini"; //DA CAMBIARE
	private static String DB_URL = "jdbc:mariadb://localhost:3306/dovado";
	private static String DRIVER_CLASS_NAME = "org.mariadb.jdbc.Driver";
			
	//------------------------------------------------------------
	
	
	private DAOPlace() {
	}
	
	public static DAOPlace getInstance() {
		if(INSTANCE==null)
			INSTANCE = new DAOPlace();
		return INSTANCE;
	}
	
	public List<Place> searchPlaces(String str) throws Exception {
		// STEP 1: dichiarazioni
        CallableStatement stmt = null;
        Connection conn = null;
        
        ArrayList<Place> places = new ArrayList<Place>();
        
        try {
        	// STEP 2: loading dinamico del driver mysql
            Class.forName(DRIVER_CLASS_NAME);
            
            // STEP 3: apertura connessione
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Connected database successfully...");
            
            //STEP4.1: preparo la stored procedure
            String call =  "{call search_places(?)}";
            
            stmt = conn.prepareCall(call);
            
            stmt.setString(1,str);
            
            if(!stmt.execute()) {
            	//NOTA: restituisce true solo è un result set, quindi non usare per le operazioni CURD!!!
            	Exception e = new Exception("Sembra che non esista nessun luogo per: "+str);
            	throw e;
            };
            
            ResultSet rs = stmt.getResultSet();
            
            
            while(rs.next()) {
            	Place p = new Place(
            			rs.getLong("idLuogo"),
            			rs.getString("nome"),
            			rs.getString("indirizzo"),
            			rs.getString("citta"),
            			rs.getString("regione"),
            			rs.getString("civico"),
            			rs.getString("cap")
            			);
            	places.add(p);
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
        
        return places;
	}

	public int spotPlace(String address, String placeName, String city, String region, String civico, String cap) throws Exception{
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
            String call =  "{call create_place(?,?,?,?,?,?)}";
            
            
            
            stmt = conn.prepareCall(call);
            
            stmt.setString(1,placeName);
            stmt.setString(2,address);
            stmt.setString(3,city);
            stmt.setString(4,region);
            stmt.setString(5,civico);
            stmt.setString(6,cap);
              
            
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
        
        
		return 1;
	}
	

	
	
}