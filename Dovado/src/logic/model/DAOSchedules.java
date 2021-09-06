package logic.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DAOSchedules {
	private static DAOSchedules INSTANCE;
	private JSONParser parser;
	
	private static final  String SCHEDJSON = "WebContent/schedules.json";
	private static final  String SCHEDULESKEY = "schedules";
	private static final  String SCHEDKEY = "schedule";
	private static final  String UIDKEY = "userID";

	
	private DAOSchedules() {
		parser = new JSONParser();
	}
	
	public static DAOSchedules getInstance() {
		if(INSTANCE == null) INSTANCE = new DAOSchedules();
		return INSTANCE;
	}
	
	public boolean updateScheduleInJSON(Schedule schedule, SuperUser su) {
		try (FileWriter file = new FileWriter(SCHEDJSON)){
			Object schedules = parser.parse(new FileReader(SCHEDJSON));
			JSONObject scheduleObj = (JSONObject) schedules;
			
			JSONArray scheduleArray = (JSONArray) scheduleObj.get(SCHEDULESKEY);
			int i;

			if(scheduleArray==null) {
				Log.getInstance().logger.info("Non ci sono attivita da dover modificare!\n");
				return false;
			}
			
			JSONObject result;
			
			for(i=0;i<scheduleArray.size();i++) {
				result = (JSONObject) scheduleArray.get(i);

				if(((Long)result.get(UIDKEY))==su.getUserID()) {
					ArrayList<ScheduledActivity> scheduleList =(ArrayList<ScheduledActivity>) schedule.getScheduledActivities();
					JSONArray scheduleUpdArr = (JSONArray) result.get(SCHEDKEY);
					
					int j;
					for(j=0;j<scheduleList.size();j++) {
						JSONObject scheduleUpdated = new JSONObject();
					
						scheduleUpdated.put("activityReferenced", scheduleList.get(scheduleList.size()-1).getReferencedActivity().getId());
						scheduleUpdated.put("scheduledTime", scheduleList.get(scheduleList.size()-1).getScheduledTime().toString());
						scheduleUpdated.put("reminderTime", scheduleList.get(scheduleList.size()-1).getReminderTime().toString());
						
						scheduleUpdArr.add(scheduleUpdated);
					}
					
					result.put(SCHEDKEY, scheduleUpdArr);
					
					file.write(scheduleObj.toString());
					file.flush();
					
					return true;
				}
			}
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean addScheduletoJSON(Schedule schedule, SuperUser su) {
		try {
			Object schedules = parser.parse(new FileReader(SCHEDJSON));
			JSONObject scheduleObj = (JSONObject) schedules;
			
			JSONArray scheduleArray = (JSONArray) scheduleObj.get(SCHEDULESKEY);
			Schedule schFound = findSchedule(su.getUserID());
			
			if(schFound==null) {
				JSONObject newSchedule = new JSONObject();
				
				newSchedule.put(UIDKEY, su.getUserID());
				newSchedule.put(SCHEDKEY, new JSONArray());
				
				scheduleArray.add(newSchedule);
				try (FileWriter file = new FileWriter(SCHEDJSON)){
					file.write(scheduleObj.toString());
					file.flush();
				}
				
			} 
				updateScheduleInJSON(schedule,su);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public Schedule findSchedule(long userID) {
		try {		
			Log.getInstance().logger.info("valore code:"+ userID);
			Log.getInstance().logger.info("Working Directory = " + System.getProperty("user.dir"));		

			Object schedules = parser.parse(new FileReader(SCHEDJSON));
			JSONObject scheduleObj = (JSONObject) schedules;
			JSONArray scheduleArray = (JSONArray) scheduleObj.get(SCHEDULESKEY);
			JSONObject result;
			
			for(int i=0; i<scheduleArray.size();i++) {
				result = (JSONObject)scheduleArray.get(i);
				
				Long codeJSON = (Long) result.get(UIDKEY);
				Log.getInstance().logger.info("valore codeJSON:"+ codeJSON);
				
				
				if (codeJSON.equals(Long.valueOf(userID))) {
					DAOActivity daoAc = DAOActivity.getInstance();
					JSONArray schedule = (JSONArray) result.get(SCHEDKEY);
					DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
						
					Log.getInstance().logger.info("schedule trovato");
						
					Schedule schFound = new Schedule();
					ArrayList<ScheduledActivity> scheduledActsArray = new ArrayList<>(); 
						
					for(int j=0;j<schedule.size();j++) {
						JSONObject actSch = (JSONObject) schedule.get(j);
						String remindTime = (String)actSch.get("reminderTime");
						String schedTime = (String)actSch.get("scheduledTime");
							
						ScheduledActivity sa = new ScheduledActivity(daoAc.findActivityByID(DAOSuperUser.getInstance(),(Long)actSch.get("activityReferenced")), LocalDateTime.parse(schedTime,dateFormatter), LocalDateTime.parse(remindTime,dateFormatter));
						scheduledActsArray.add(sa);
					}
						
					schFound.setScheduledActivities(scheduledActsArray);
						
						
					return schFound;
					
				}
			}
				
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return null;
		 
	}
	
	public boolean deleteSchedule(Long userID, int idSched) {
		try {		
			Log.getInstance().logger.info("valore code:"+ userID);
			Log.getInstance().logger.info("Working Directory = " + System.getProperty("user.dir"));		

			Object schedules = parser.parse(new FileReader(SCHEDJSON));
			JSONObject scheduleObj = (JSONObject) schedules;
			JSONArray scheduleArray = (JSONArray) scheduleObj.get(SCHEDULESKEY);
			JSONObject result;
			
			for(int i=0; i<scheduleArray.size();i++) {
				result = (JSONObject)scheduleArray.get(i);
				
				Long codeJSON = (Long) result.get(UIDKEY);
				Log.getInstance().logger.info("valore codeJSON:"+ codeJSON);
				
				
				if (codeJSON.equals(Long.valueOf(userID))) {
					DAOActivity daoAc = DAOActivity.getInstance();
					JSONArray schedule = (JSONArray) result.get(SCHEDKEY);
					DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
						
					Log.getInstance().logger.info("schedule trovato");
						
					Schedule schFound = new Schedule();
					ArrayList<ScheduledActivity> scheduledActsArray = new ArrayList<>(); 
					
					schedule.remove(idSched);
					
					try(FileWriter file = new FileWriter(SCHEDJSON)) {
						file.write(scheduleObj.toString());
						file.flush();
					}
						
					return true;
					
				}
			}
				
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} 

		return false;
	}
	
}

