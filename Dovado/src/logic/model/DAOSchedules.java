package logic.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

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
	
	public boolean addScheduletoJSON(Schedule schedule, SuperUser su) {
		try {
			Object schedules = parser.parse(new FileReader("src/WebContent/schedules.json"));
			JSONObject scheduleObj = (JSONObject) schedules;
			
			JSONArray scheduleArray = (JSONArray) scheduleObj.get("schedules");
			
			if(findSchedule(su.getUserID())==null) {
				JSONObject newSchedule = new JSONObject();
				
				newSchedule.put("userID", su.getUserID());
				newSchedule.put("schedule", new JSONArray());
				
				scheduleArray.add(newSchedule);
				
				FileWriter file = new FileWriter("Webcontent/schedules.json");
				file.write(scheduleObj.toString());
				file.flush();
				file.close();
				
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
			System.out.println("valore code:"+ code);
			System.out.println("Working Directory = " + System.getProperty("user.dir"));		

			Object schedules = parser.parse(new FileReader("eclipse-workspace/Dovado/WebContent/schedules.json"));
			JSONObject scheduleObj = (JSONObject) schedules;
			JSONArray scheduleArray = (JSONArray) scheduleObj.get("schedules");
			JSONObject result;
			
			for(int i=0; i<scheduleArray.size();i++) {
				result = (JSONObject)scheduleArray.get(i);
				
				Long codeJSON = (Long) result.get("userID");
				System.out.println("valore codeJSON:"+ codeJSON);
				
				try {
					if (codeJSON.equals(Long.valueOf(code))) {
						System.out.println("schedule trovato");
						
						
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
