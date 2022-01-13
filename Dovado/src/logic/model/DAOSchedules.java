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
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DAOSchedules {
	private static DAOSchedules INSTANCE;
	
	//----------database--------------------------------------

	private static String USER = "sav"; //DA CAMBIARE
	private static String PASSWORD = "pellegrini"; //DA CAMBIARE
	private static String DB_URL = "jdbc:mariadb://localhost:3306/dovado";
	private static String DRIVER_CLASS_NAME = "org.mariadb.jdbc.Driver";
			
	//------------------------------------------------------------
	

	
	private DAOSchedules() {
	}
	
	public static DAOSchedules getInstance() {
		if(INSTANCE == null) INSTANCE = new DAOSchedules();
		return INSTANCE;
	}
	

	public Schedule getSchedule(Long idUser) throws Exception {
		//metodo per prendere dal db lo schedulo di un utente
		
		// STEP 1: dichiarazioni
        CallableStatement stmt = null;
        Connection conn = null;
        
        Schedule schedule = new Schedule();
        
        try {
        	// STEP 2: loading dinamico del driver mysql
            Class.forName(DRIVER_CLASS_NAME);
            
            // STEP 3: apertura connessione
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Connected database successfully...");
            
            //STEP4.1: preparo la stored procedure
            String call = "{call get_user_schedule(?)}";
            
            stmt = conn.prepareCall(call);
            
            stmt.setLong(1,idUser);
            
            if(stmt.execute()) {
            
	            //ottengo il resultSet
	            ResultSet rs = stmt.getResultSet();
	            
	            while(rs.next()) {
	            	Long idSchedule = rs.getLong("id");
	            	Long activity = rs.getLong("idActivity");
	            	String scheduled_time = rs.getString("data_schedulo");
	            	String reminder_time = rs.getString("data_reminder");
	            	
	            	Activity a = DAOActivity.getInstance().getActivityById(activity);
	        		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	        		LocalDateTime scheduledTime = LocalDateTime.parse(scheduled_time,dtf);
	        		LocalDateTime reminderTime = LocalDateTime.parse(reminder_time,dtf);
	            	
	            	schedule.addActivityToSchedule(idSchedule,a, scheduledTime, reminderTime);
	            }
	            
	            rs.close();
            }
        	
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
            
        return schedule;
		
	}

	public void addActivityToSchedule(Long userID, Long activity, LocalDateTime scheduledTime,
			LocalDateTime reminderTime) throws Exception {
		
		//metodo per salvare sul db il fatto che un utente abbia schedulato un'attività
		
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
            String call = "{call add_activity_to_schedule(?,?,?,?)}";
            
            stmt = conn.prepareCall(call);
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            
            stmt.setLong(1, userID);
            stmt.setLong(2, activity);
            stmt.setString(3, scheduledTime.format(formatter));
            stmt.setString(4, reminderTime.format(formatter));
            
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
	
	public void changeSchedule(Long idScheduleActivity, LocalDateTime scheduledTime,
			LocalDateTime reminderTime) throws Exception {
		//metodo per salvare sul db il fatto che un utente abbia modificato lo schedulo di un'attività
		
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
            String call = "{call change_scheduled_activity(?,?,?)}";
            
            stmt = conn.prepareCall(call);
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            
            stmt.setLong(1, idScheduleActivity);
            stmt.setString(2, scheduledTime.format(formatter));
            stmt.setString(3, reminderTime.format(formatter));
            
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

