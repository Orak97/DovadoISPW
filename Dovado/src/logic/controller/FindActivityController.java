package logic.controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import logic.model.Activity;
import logic.model.Cadence;
import logic.model.CertifiedActivity;
import logic.model.DAOActivity;
import logic.model.FindActivitiesBean;
import logic.model.Log;
import logic.model.NormalActivity;
import logic.model.Place;
import logic.model.PreferenceBean;
import logic.model.Preferences;
import logic.model.SuperActivity;
import java.util.ArrayList;
import java.util.List;

public class FindActivityController {
	
	private FindActivitiesBean beanFind;
	private Preferences beanPref;
	
	public FindActivityController(FindActivitiesBean beanFind,PreferenceBean beanPref) {
		this.beanFind = beanFind;
		this.beanPref = new Preferences(beanPref);
	}
	
	public List<Activity> findActivities() throws ClassNotFoundException, SQLException {
		//controllo che i campi non siano nulli
		if(beanFind.getZone() == null || beanFind.getDate() == null) throw new NullPointerException();
		
		LocalDate date = beanFind.getLocalDate();
		LocalDate today = LocalDate.now();
		
		//controllo che  la data inserita sia => oggi
		if(date.isBefore(today)) throw new IllegalArgumentException("La data in cui vuoi fare l'attività deve almeno successiva ad oggi!");
		
		String[] keywords = null;
		if(beanFind.getKeywords()!= null){
			keywords = beanFind.getKeywords().split(";");
		}
		
		DAOActivity dao = DAOActivity.getInstance();
		
		ArrayList<Activity> searchedActivities = (ArrayList<Activity>) dao.findActivitiesByZone(beanFind.getZone());
		
		//to remove
		Log.getInstance().getLogger().info("dopo find activities by zone");
		for(Activity a : searchedActivities) Log.getInstance().getLogger().info(a.getName());
		
		
		if(keywords != null) searchedActivities = (ArrayList<Activity>) filterActivitiesByKeyWords(searchedActivities, keywords);
		
		Preferences p = beanPref;
		
		searchedActivities = (ArrayList<Activity>) filterActivitiesByPreferences(searchedActivities,p);
		
		//to remove
		Log.getInstance().getLogger().info("dopo find activities by preferences");
		for(Activity a : searchedActivities) Log.getInstance().getLogger().info(a.getName());
		
		searchedActivities = (ArrayList<Activity>) filterActivitiesByDate(searchedActivities,date);
		
		//to remove
		Log.getInstance().getLogger().info("dopo find activities by date");
		for(Activity a : searchedActivities) Log.getInstance().getLogger().info(a.getName());
		
		return searchedActivities;
	}
	
	public List<Activity> findActivitiesByZone() throws ClassNotFoundException, SQLException {
		//controllo che i campi non siano nulli
		if(beanFind.getZone() == null) throw new NullPointerException();
				
		DAOActivity dao = DAOActivity.getInstance();
				
		return dao.findActivitiesByZone(beanFind.getZone());
	}	
	

	public static List<Activity> filterActivitiesByKeyWords(List<Activity> activities,String[] keywords){
		//metodo per filtrare una lista di attività in base alle parole chiave
		ArrayList<Activity> filteredActivities = new ArrayList<>();
		for(Activity curr : activities) {
			String name = curr.getName().toUpperCase();
			String description = curr.getDescription().toUpperCase();
			
			for(String keyword: keywords) {
				String formattedKeyword = keyword.toUpperCase();
				if((name.contains(formattedKeyword) || description.contains(formattedKeyword)) && !filteredActivities.contains(curr) ) filteredActivities.add(curr);
			}	
		}
		return filteredActivities;
	}
	
	public static List<Activity> filterActivitiesByDate(List<Activity> activities, LocalDate date){
		//metodo per filtrare una lista di attività in base alla data (se è aperta in giorno "date" allora è ok)
		ArrayList<Activity> filteredActivities = new ArrayList<>();
		for(Activity curr : activities) {
			if(curr.isPlayableOnThisDate(date)) filteredActivities.add(curr);
		}	
		return filteredActivities;
	}
	
	public static List<Activity> filterActivitiesByPreferences(List<Activity> activities, Preferences preferences){
		//metodo per filtrare una lista di attività in base alle preferenze		
		ArrayList<Activity> filteredActivities = new ArrayList<>();
		for(Activity curr : activities) {
			Preferences currPref = curr.getIntrestedCategories();
			if(preferences.checkCompatibility(currPref) > 0) filteredActivities.add(curr);
		}	
		return filteredActivities;
	}
}
