package logic.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import logic.controller.FindActivityController;

public class DAOCoupon {
	private static DAOCoupon instance;
	private JSONParser parser; 
	private static final  String COUPONJSON = "WebContent/coupon.json" ;
	private static final  String COUPONSKEY = "coupons";
	private static final  String PARTNERKEY = "partner";
	private static final  String DISCOUNTKEY = "discount";
	
	
	//----------database--------------------------------------

	private static String USER = "dovado"; //DA CAMBIARE
	private static String PASSWORD = "dovadogang"; //DA CAMBIARE
	private static String DB_URL = "jdbc:mariadb://localhost:3306/dovado";
	private static String DRIVER_CLASS_NAME = "org.mariadb.jdbc.Driver";

	//------------------------------------------------------------
	
	private DAOCoupon() {
		parser = new JSONParser();
	}
	
	public static DAOCoupon getInstance() {
		if(instance==null)
			instance = new DAOCoupon();
		return instance;
	}
	
	
	public Coupon findCoupon(int code) throws Exception {
		// STEP 1: dichiarazioni
        CallableStatement stmt = null;
        Connection conn = null;
        
        Coupon myCoupon = null;
        
        try {
        	// STEP 2: loading dinamico del driver mysql
            Class.forName(DRIVER_CLASS_NAME);

            // STEP 3: apertura connessione
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Connected database successfully...");

            //STEP4.1: preparo la stored procedure
            String call = "{call get_coupon(?)}";

            stmt = conn.prepareCall(call);

            stmt.setInt(1,code);
            
            if(!stmt.execute()) {
            	Exception e = new Exception("nessun coupon esistente con questo codice");
            	throw e;
            }
            
            //ottengo il resultSet
            ResultSet rs = stmt.getResultSet();
            
            while(rs.next()) {
            	
            	int  utente = rs.getInt("utente");
            	int attivita = rs.getInt("attivita");
            	int codice = rs.getInt("idCoupon");
            	int sconto = rs.getInt("sconto");
            	
            	String scadenzaStr = rs.getString("data_scadenza");
            	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
            	
            	LocalDateTime scadenza = LocalDateTime.parse(scadenzaStr,dtf);
            	
            	myCoupon = new Coupon(utente,attivita,codice,sconto,scadenza);
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
        
        return myCoupon;
	}
	
	public Coupon findCouponPartner(int code) throws Exception {
		// STEP 1: dichiarazioni
        CallableStatement stmt = null;
        Connection conn = null;
        
        Coupon myCoupon = null;
        
        try {
        	// STEP 2: loading dinamico del driver mysql
            Class.forName(DRIVER_CLASS_NAME);

            // STEP 3: apertura connessione
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Connected database successfully...");

            //STEP4.1: preparo la stored procedure
            String call = "{call get_coupon(?)}";

            stmt = conn.prepareCall(call);

            stmt.setInt(1,code);
            
            if(!stmt.execute()) {
            	Exception e = new Exception("nessun coupon esistente con questo codice");
            	throw e;
            }
            
            //ottengo il resultSet
            ResultSet rs = stmt.getResultSet();
            
            while(rs.next()) {
            	
            	String  utente = rs.getString("username_utente");
            	String attivita = rs.getString("nome_attivita");
            	int codice = rs.getInt("idCoupon");
            	int sconto = rs.getInt("sconto");
            	String dataScadenzaStr = rs.getString("data_scadenza");
            	String dataScheduloStr = rs.getString("data_schedulo");
            	
            	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
            	
            	LocalDateTime scadenza = LocalDateTime.parse(dataScadenzaStr,formatter);
            	LocalDateTime schedulo = LocalDateTime.parse(dataScheduloStr,formatter);
            	
            	myCoupon = new Coupon(codice,utente,attivita,sconto,scadenza,schedulo);
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
        
        return myCoupon;
	}
	

	
	
}
