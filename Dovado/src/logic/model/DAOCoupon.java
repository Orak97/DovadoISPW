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

public class DAOCoupon extends DAO{
	private static DAOCoupon instance;
	
	private DAOCoupon() {
	}
	
	public static DAOCoupon getInstance() {
		if(instance==null)
			instance = new DAOCoupon();
		return instance;
	}
	
	
	public Coupon findCoupon(int code) throws SQLException, ClassNotFoundException {
        return findCoupon(code, false);
	}
	
	public Coupon findCouponPartner(int code) throws ClassNotFoundException, SQLException {     
        return findCoupon(code, true);
	}
	
	private Coupon findCoupon(int code, boolean isPartner) throws ClassNotFoundException, SQLException {

        Coupon myCoupon = null;
        
        try {
        	resetConnection();

            //STEP4.1: preparo la stored procedure
            String call = "{call get_coupon(?)}";
            stmt = conn.prepareCall(call);

            stmt.setInt(1,code);
            
            if(!stmt.execute()) {
            	throw new SQLException("nessun coupon esistente con questo codice");
            }
            
            //ottengo il resultSet
            ResultSet rs = stmt.getResultSet();
            
            while(rs.next()) {
            	int codice = rs.getInt("idCoupon");
        		int sconto = rs.getInt("sconto");
            	
        		String dataScadenzaStr = rs.getString("data_scadenza");            	
            	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");           	
            	LocalDateTime scadenza = LocalDateTime.parse(dataScadenzaStr,formatter);
            	
            	if(isPartner) {
            		String  utente = rs.getString("username_utente");
            		String attivita = rs.getString("nome_attivita");
            		
            		
            		String dataScheduloStr = rs.getString("data_schedulo");
            	
            		
            		LocalDateTime schedulo = LocalDateTime.parse(dataScheduloStr,formatter);
            	
            		myCoupon = new Coupon(codice,utente,attivita,sconto,scadenza,schedulo);
            	} else {
            		int  utente = rs.getInt("utente");
                	int attivita = rs.getInt("attivita");
                	
                	myCoupon = new Coupon(utente,attivita,codice,sconto,scadenza);
            	}
            }
            
            rs.close();
            }finally {
            	//Clean-up dell'ambiente
                disconnRoutine();
            }
        
        return myCoupon;
	}
	
	public void redeemCoupon(int coupon, Long partner) throws ClassNotFoundException, SQLException {
		        
        try {
        	resetConnection();

            //STEP4.1: preparo la stored procedure
            String call = "{call redeem_coupon(?,?)}";

            stmt = conn.prepareCall(call);

            stmt.setInt(1,coupon);
            stmt.setLong(2, partner);
            
            stmt.execute();
        }finally {
            //Clean-up dell'ambiente
            disconnRoutine();
        }
	}
	
}
