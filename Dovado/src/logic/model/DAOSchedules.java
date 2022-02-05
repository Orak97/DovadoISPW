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

	private static final String USER = "dovado"; //DA CAMBIARE
	private static final String PASSWORD = "dovadogang"; //DA CAMBIARE
	private static final String DB_URL = "jdbc:mariadb://localhost:3306/dovado";
	private static final String DRIVER_CLASS_NAME = "org.mariadb.jdbc.Driver";

	private static final String LOGDBCONN = "Connected database successfully...";
	private static final String LOGDBDISCONN = "Disconnetted database successfully...";
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	//------------------------------------------------------------
	
	private static Connection conn;
	private static CallableStatement stmt;

	private DAOSchedules() {
	}

	public static DAOSchedules getInstance() {
		conn = null;
		stmt = null;
		if(INSTANCE == null) INSTANCE = new DAOSchedules();
		return INSTANCE;
	}


	public Schedule getSchedule(Long idUser) throws Exception {
		//metodo per prendere dal db lo schedulo di un utente

		// STEP 1: dichiarazioni
        Schedule schedule = new Schedule();

        try {
        	// STEP 2: loading dinamico del driver mysql
            Class.forName(DRIVER_CLASS_NAME);

            // STEP 3: apertura connessione
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println(LOGDBCONN);

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
	            	String schedTime = rs.getString("data_schedulo");
	            	String remindTime = rs.getString("data_reminder");

	            	Activity a = DAOActivity.getInstance().getActivityById(activity);
	        		LocalDateTime scheduledTime = LocalDateTime.parse(schedTime,formatter);
	        		LocalDateTime reminderTime = LocalDateTime.parse(remindTime,formatter);
	        		
	        		if(rs.getInt("coupon") != 0) {
	        			int couponCode = rs.getInt("coupon");
	        			DAOCoupon daoCp = DAOCoupon.getInstance();
	        			Coupon coupon = daoCp.findCoupon(couponCode);
	        			schedule.addActivityToSchedule(idSchedule,a, scheduledTime, reminderTime,coupon);
	        		}else
	        			schedule.addActivityToSchedule(idSchedule,a, scheduledTime, reminderTime);
	            }

	            rs.close();
            }

		}finally {
		    // STEP 5.2: Clean-up dell'ambiente
		    catchRoutine();
		}

        return schedule;

	}

	public void addActivityToSchedule(Long userID, Long activity, LocalDateTime scheduledTime,
			LocalDateTime reminderTime) throws Exception {

		//metodo per salvare sul db il fatto che un utente abbia schedulato un'attività


        try {
        	// STEP 2: loading dinamico del driver mysql
            Class.forName(DRIVER_CLASS_NAME);

            // STEP 3: apertura connessione
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println(LOGDBCONN);

            //STEP4.1: preparo la stored procedure
            String call = "{call add_activity_to_schedule(?,?,?,?)}";

            stmt = conn.prepareCall(call);

            stmt.setLong(1, userID);
            stmt.setLong(2, activity);
            stmt.setString(3, scheduledTime.format(formatter));
            stmt.setString(4, reminderTime.format(formatter));

            stmt.execute();

        }finally {
		    // STEP 5.2: Clean-up dell'ambiente
        	catchRoutine();
		}

	}

	public void changeSchedule(Long idScheduleActivity, LocalDateTime scheduledTime,
			LocalDateTime reminderTime) throws Exception {
		//metodo per salvare sul db il fatto che un utente abbia modificato lo schedulo di un'attività


        try {
        	// STEP 2: loading dinamico del driver mysql
            Class.forName(DRIVER_CLASS_NAME);

            // STEP 3: apertura connessione
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Connected database successfully...");

            //STEP4.1: preparo la stored procedure
            String call = "{call change_scheduled_activity(?,?,?)}";

            stmt = conn.prepareCall(call);

            

            stmt.setLong(1, idScheduleActivity);
            stmt.setString(2, scheduledTime.format(formatter));
            stmt.setString(3, reminderTime.format(formatter));

            stmt.execute();
            
        }finally {
		    // STEP 5.2: Clean-up dell'ambiente
		    catchRoutine();
		}
	}

	public boolean removeActFromSchedule(Long scheduleToRemove,Long user) throws Exception {


        try {
        	// STEP 2: loading dinamico del driver mysql
            Class.forName(DRIVER_CLASS_NAME);

            // STEP 3: apertura connessione
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println(LOGDBCONN);

            //STEP4.1: preparo la stored procedure
            String call = "{call remove_activity_from_schedule(?,?)}";

            stmt = conn.prepareCall(call);

            stmt.setLong(1, scheduleToRemove);
            stmt.setLong(2, user);

            stmt.execute();

        }finally {
		    // STEP 5.2: Clean-up dell'ambiente
		    catchRoutine();
		}
		return true;
	}

	public void addCertifiedActivityToSchedule(Long userID, Long activity, LocalDateTime scheduledTime,
			LocalDateTime reminderTime, int percentage, LocalDateTime expiringDate) throws Exception{
		
		//metodo per salvare sul db il fatto che un utente abbia schedulato un'attività


		        try {
		        	// STEP 2: loading dinamico del driver mysql
		            Class.forName(DRIVER_CLASS_NAME);

		            // STEP 3: apertura connessione
		            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
		            System.out.println(LOGDBCONN);

		            //STEP4.1: preparo la stored procedure
		            String call = "{call add_certified_activity_to_schedule(?,?,?,?,?,?)}";

		            stmt = conn.prepareCall(call);

		            stmt.setLong(1, userID);
		            stmt.setLong(2, activity);
		            stmt.setString(3, scheduledTime.format(formatter));
		            stmt.setString(4, reminderTime.format(formatter));
		            stmt.setInt(5, percentage);
		            stmt.setString(6, expiringDate.format(formatter));

		            stmt.execute();

		        }finally {
				    // STEP 5.2: Clean-up dell'ambiente
				    catchRoutine();
				}
		
	}
	
	private void catchRoutine() throws SQLException {
		try {
	        if (stmt != null)
	            stmt.close();
	    } catch (SQLException se2) {
	    	System.out.println( se2.getErrorCode());
	    	se2.printStackTrace();
	    }
	    try {
	        if (conn != null)
	            conn.close();
	        	System.out.println(LOGDBDISCONN);

	    } catch (SQLException se) {
	        System.out.println( se.getErrorCode());
	    	se.printStackTrace();
	    }
	}
}
