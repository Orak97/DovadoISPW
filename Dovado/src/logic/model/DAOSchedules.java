package logic.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DAOSchedules {
	private static DAOSchedules INSTANCE;
	private JSONParser parser;
	private DAOSchedules() {
		parser = new JSONParser();
	}
	
	public static DAOSchedules getInstance() {
		if(INSTANCE == null) INSTANCE = new DAOSchedules();
		return INSTANCE;
	}
	
	public boolean updateScheduleInJSON(Schedule schedule, SuperUser su) {
		try {
			Object schedules = parser.parse(new FileReader("WebContent/schedules.json"));
			JSONObject scheduleObj = (JSONObject) schedules;
			
			JSONArray scheduleArray = (JSONArray) scheduleObj.get("schedules");
			int i;

			if(scheduleArray==null) {
				Log.getInstance().logger.info("Non ci sono attivita da dover modificare!\n");
				return false;
			}
			
			JSONObject result;
			
			for(i=0;i<scheduleArray.size();i++) {
				result = (JSONObject) scheduleArray.get(i);

				if(((Long)result.get("userID"))==su.getUserID()) {
					JSONObject scheduleUpdated = new JSONObject();
					ArrayList<ScheduledActivity> scheduleList = schedule.getScheduledActivities();

					int j;
					for(j=0;j<scheduleList.size();j++) {
						scheduleUpdated.put("activityReferenced", scheduleList.get(j).getReferencedActivity());
						scheduleUpdated.put("scheduledTime", scheduleList.get(j).getScheduledTime());
						scheduleUpdated.put("remiderTime", scheduleList.get(j).getReminderTime());
						scheduleUpdated.put("timer", scheduleList.get(j).getTimer());
					}
					
					result.put("schedule", scheduleUpdated);
					
					FileWriter file = new FileWriter("WebContent/schedules.json");
					file.write(scheduleObj.toString());
					file.flush();
					file.close();
					
					return true;
				}
			}
				
		}catch(NullPointerException e) {
			e.printStackTrace();
			return false;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean addScheduletoJSON(Schedule schedule, SuperUser su) {
		try {
			Object schedules = parser.parse(new FileReader("WebContent/schedules.json"));
			JSONObject scheduleObj = (JSONObject) schedules;
			
			JSONArray scheduleArray = (JSONArray) scheduleObj.get("schedules");
			Schedule schFound = findSchedule(su.getUserID());
			
			if(schFound==null) {
				JSONObject newSchedule = new JSONObject();
				
				newSchedule.put("userID", su.getUserID());
				newSchedule.put("schedule", new JSONArray());
				
				scheduleArray.add(newSchedule);
				
				FileWriter file = new FileWriter("Webcontent/schedules.json");
				file.write(scheduleObj.toString());
				file.flush();
				file.close();
				
			} 
			else {
				updateScheduleInJSON(schedule,su);
			}
			
		}catch(NullPointerException e) {
			e.printStackTrace();
			return false;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public Schedule findSchedule(long code) {
		try {		
			Log.getInstance().logger.info("valore code:"+ code);
			Log.getInstance().logger.info("Working Directory = " + System.getProperty("user.dir"));		

			Object schedules = parser.parse(new FileReader("WebContent/schedules.json"));
			JSONObject scheduleObj = (JSONObject) schedules;
			JSONArray scheduleArray = (JSONArray) scheduleObj.get("schedules");
			JSONObject result;
			
			for(int i=0; i<scheduleArray.size();i++) {
				result = (JSONObject)scheduleArray.get(i);
				
				Long codeJSON = (Long) result.get("userID");
				Log.getInstance().logger.info("valore codeJSON:"+ codeJSON);
				
				try {
					if (codeJSON.equals(Long.valueOf(code))) {
						Log.getInstance().logger.info("schedule trovato");
						
						
						return null;
					}
				} catch (NullPointerException e) {
					e.printStackTrace();
					return null;
				} catch (ClassCastException e) {
					e.printStackTrace();
					return null;
				}
			}
				
			
		} catch(NullPointerException e) {
			e.printStackTrace();
			return null;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return null;
		
	}
	
}
