package logic.controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalTime;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import logic.model.Activity;
import logic.model.Cadence;
import logic.model.CertifiedActivity;
import logic.model.DAOActivity;
import logic.model.DAOSuperUser;
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
	private PreferenceBean beanPref;
	
	public FindActivityController(FindActivitiesBean beanFind,PreferenceBean beanPref) {
		this.beanFind = beanFind;
		this.beanPref = beanPref;
	}
	
	public ArrayList<Activity> FindActivities() throws Exception{
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
		ArrayList<Activity> searchedActivities = new ArrayList<Activity>();
		
		DAOActivity dao = DAOActivity.getInstance();
		
		searchedActivities = dao.findActivitiesByZone(beanFind.getZone());
		
		//to remove
		System.out.println("dopo find activities by zone");
		for(Activity a : searchedActivities) System.out.println(a.getName());
		
		
		if(keywords != null) searchedActivities = filterActivitiesByKeyWords(searchedActivities, keywords);
		
		Preferences p = SetPreferencesController.getPreferencesFromBean(beanPref);
		
		searchedActivities = filterActivitiesByPreferences(searchedActivities,p);
		
		//to remove
		System.out.println("dopo find activities by preferences");
		for(Activity a : searchedActivities) System.out.println(a.getName());
		
		searchedActivities = filterActivitiesByDate(searchedActivities,date);
		
		//to remove
		System.out.println("dopo find activities by date");
		for(Activity a : searchedActivities) System.out.println(a.getName());
		
		return searchedActivities;
	}
	

	public ArrayList<Activity> filterActivitiesByKeyWords(ArrayList<Activity> activities,String[] keywords){
		//metodo per filtrare una lista di attività in base alle parole chiave
		ArrayList<Activity> filteredActivities = new ArrayList<Activity>();
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
	
	public ArrayList<Activity> filterActivitiesByDate(ArrayList<Activity> activities, LocalDate date){
		//metodo per filtrare una lista di attività in base alla data (se è aperta in giorno "date" allora è ok)
		ArrayList<Activity> filteredActivities = new ArrayList<Activity>();
		for(Activity curr : activities) {
			if(curr.isPlayableOnThisDate(date)) filteredActivities.add(curr);
		}	
		return filteredActivities;
	}
	
	public ArrayList<Activity> filterActivitiesByPreferences(ArrayList<Activity> activities, Preferences preferences){
		//metodo per filtrare una lista di attività in base alle preferenze		
		ArrayList<Activity> filteredActivities = new ArrayList<Activity>();
		for(Activity curr : activities) {
			Preferences currPref = curr.getIntrestedCategories();
			if(preferences.checkCompatibility(currPref) > 0) filteredActivities.add(curr);
		}	
		return filteredActivities;
	}
}
