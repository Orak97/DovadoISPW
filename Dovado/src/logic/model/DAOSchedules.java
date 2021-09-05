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
					ArrayList<ScheduledActivity> scheduleList = schedule.getScheduledActivities();
					JSONArray scheduleUpdArr = (JSONArray) result.get("schedule");
					
					int j;
					for(j=0;j<scheduleList.size();j++) {
						JSONObject scheduleUpdated = new JSONObject();
					
						scheduleUpdated.put("activityReferenced", scheduleList.get(scheduleList.size()-1).getReferencedActivity().getId());
						scheduleUpdated.put("scheduledTime", scheduleList.get(scheduleList.size()-1).getScheduledTime().toString());
						scheduleUpdated.put("reminderTime", scheduleList.get(scheduleList.size()-1).getReminderTime().toString());
						
						scheduleUpdArr.add(scheduleUpdated);
					}
					
					result.put("schedule", scheduleUpdArr);
					
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
				
				FileWriter file = new FileWriter("WebContent/schedules.json");
				file.write(scheduleObj.toString());
				file.flush();
				file.close();
				
			} 
				updateScheduleInJSON(schedule,su);
			
			
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
						DAOActivity daoAc = DAOActivity.getInstance();
						JSONArray schedule = (JSONArray) result.get("schedule");
						DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
						
						Log.getInstance().logger.info("schedule trovato");
						
						Schedule schFound = new Schedule();
						ArrayList<ScheduledActivity> scheduledActsArray = new ArrayList<ScheduledActivity>(); 
						
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
