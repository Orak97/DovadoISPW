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

public class DAOSchedules extends DAO{
	private static DAOSchedules INSTANCE;

	//----------database--------------------------------------

	private static final DateTimeFormatter formatterDB = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	//------------------------------------------------------------
	
	private DAOSchedules() {
	}

	public static DAOSchedules getInstance() {
		if(INSTANCE == null) INSTANCE = new DAOSchedules();
		return INSTANCE;
	}

	public Schedule getSchedule(Long idUser) throws SQLException, ClassNotFoundException {
		//metodo per prendere dal db lo schedulo di un utente
		
		// STEP 1: dichiarazioni
        Schedule schedule = new Schedule();

        try {
        	resetConnection();
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
	            	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
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
		    disconnRoutine();
		}

        return schedule;

	}

	public void addActivityToSchedule(Long userID, Long activity, LocalDateTime scheduledTime,
			LocalDateTime reminderTime) throws SQLException, ClassNotFoundException {

		//metodo per salvare sul db il fatto che un utente abbia schedulato un'attivit??
		

        try {
        	resetConnection();
            //STEP4.1: preparo la stored procedure
            String call = "{call add_activity_to_schedule(?,?,?,?)}";

            stmt = conn.prepareCall(call);

            stmt.setLong(1, userID);
            stmt.setLong(2, activity);
            stmt.setString(3, scheduledTime.format(formatterDB));
            stmt.setString(4, reminderTime.format(formatterDB));

            stmt.execute();

        }finally {
		    // STEP 5.2: Clean-up dell'ambiente
        	disconnRoutine();
		}

	}

	public void changeSchedule(Long idScheduleActivity, LocalDateTime scheduledTime,
			LocalDateTime reminderTime) throws SQLException, ClassNotFoundException {
		//metodo per salvare sul db il fatto che un utente abbia modificato lo schedulo di un'attivit??
		

        try {
        	resetConnection();

            //STEP4.1: preparo la stored procedure
            String call = "{call change_scheduled_activity(?,?,?)}";

            stmt = conn.prepareCall(call);

            

            stmt.setLong(1, idScheduleActivity);
            stmt.setString(2, scheduledTime.format(formatterDB));
            stmt.setString(3, reminderTime.format(formatterDB));

            stmt.execute();
            
        }finally {
		    // STEP 5.2: Clean-up dell'ambiente
		    disconnRoutine();
		}
	}

	public boolean removeActFromSchedule(Long scheduleToRemove,Long user) throws SQLException, ClassNotFoundException {
		

        try {
        	resetConnection();

            //STEP4.1: preparo la stored procedure
            String call = "{call remove_activity_from_schedule(?,?)}";

            stmt = conn.prepareCall(call);

            stmt.setLong(1, scheduleToRemove);
            stmt.setLong(2, user);

            stmt.execute();

        }finally {
		    // STEP 5.2: Clean-up dell'ambiente
		    disconnRoutine();
		}
		return true;
	}

	public void addCertifiedActivityToSchedule(Long userID, Long activity, LocalDateTime scheduledTime,
			LocalDateTime reminderTime, int percentage, LocalDateTime expiringDate) throws SQLException, ClassNotFoundException{
		
		//metodo per salvare sul db il fatto che un utente abbia schedulato un'attivit??
		

		        try {
		        	resetConnection();

		            //STEP4.1: preparo la stored procedure
		            String call = "{call add_certified_activity_to_schedule(?,?,?,?,?,?)}";

		            stmt = conn.prepareCall(call);

		            stmt.setLong(1, userID);
		            stmt.setLong(2, activity);
		            stmt.setString(3, scheduledTime.format(formatterDB));
		            stmt.setString(4, reminderTime.format(formatterDB));
		            stmt.setInt(5, percentage);
		            stmt.setString(6, expiringDate.format(formatterDB));

		            stmt.execute();

		        }finally {
				    // STEP 5.2: Clean-up dell'ambiente
				    disconnRoutine();
				}
		
	}
}
