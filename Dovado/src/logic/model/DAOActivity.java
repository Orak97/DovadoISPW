package logic.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import logic.controller.FindActivityController;

public class DAOActivity {
	
	private static DAOActivity INSTANCE;
	private DAOPlace daoPl;
	private String activityFileName = "WebContent/activities.json";
	private String placeFileName;
	
	private String jpPlaces = "places";
	private String jpPlace = "place";
	private String jpActName = "name";
	private String jpCreator = "creator";
	private String jpPref = "preferences";
	private String[] jpFreq = {"opening","closing","startdate","enddate","cadence"};
	private String jpCert = "certified";
	private String jpID = "id";
	private String jpResActivity = "activities";
	
	private DAOActivity() {
		daoPl = DAOPlace.getInstance();
		placeFileName = daoPl.getJsonFileName();
		
	}
	
	public static DAOActivity getInstance() {
		if(INSTANCE==null)
			INSTANCE = new DAOActivity();
		return INSTANCE;
	}

	public Long addActivityToJSON(Place p, SuperActivity activity, String cert) {
		JSONParser parser = new JSONParser();
		FindActivityController fac = new FindActivityController();
		
		Long id;
		int i;
		try 
		{
			Object places = parser.parse(new FileReader(placeFileName));
			JSONObject place = (JSONObject) places;
			JSONArray placeArray = (JSONArray) place.get(jpPlaces);
			
			Object activitiesParser = parser.parse(new FileReader(activityFileName));
			JSONObject activitiesJOBJ = (JSONObject) activitiesParser;
			JSONArray activityArray = (JSONArray) activitiesJOBJ.get(jpResActivity);
			
			JSONObject result;
			JSONObject activityToAdd = new JSONObject();
			JSONObject activityIdToAdd = new JSONObject();
			JSONArray newPreferences = new JSONArray();

			activityToAdd.put(jpPlace, p.getId());
			activityToAdd.put(jpActName, activity.getName());
			activityToAdd.put(jpCreator, activity.getCreator().getUserID());
			activityToAdd.put(jpPref, newPreferences);
			if(activity.getFrequency() instanceof ContinuosActivity) {
				activityToAdd.put(jpFreq[0], activity.getFrequency().getOpeningTime().toString());
				activityToAdd.put(jpFreq[1], activity.getFrequency().getClosingTime().toString());
				activityToAdd.put(jpFreq[2], null);
				activityToAdd.put(jpFreq[3], null);
				activityToAdd.put(jpFreq[4], null);
				
			}
			if(activity.getFrequency() instanceof ExpiringActivity) {
				activityToAdd.put(jpFreq[0], activity.getFrequency().getOpeningTime().toString());
				activityToAdd.put(jpFreq[1], activity.getFrequency().getClosingTime().toString());
				activityToAdd.put(jpFreq[2], (((ExpiringActivity) (activity.getFrequency())).getStartDate().toString()));
				activityToAdd.put(jpFreq[3], (((ExpiringActivity) (activity.getFrequency())).getEndDate().toString()));
				activityToAdd.put(jpFreq[4], null);
				
			}
			if(activity.getFrequency() instanceof PeriodicActivity) {
				activityToAdd.put(jpFreq[0], activity.getFrequency().getOpeningTime().toString());
				activityToAdd.put(jpFreq[1], activity.getFrequency().getClosingTime().toString());
				activityToAdd.put(jpFreq[2], (((PeriodicActivity) (activity.getFrequency())).getStartDate().toString()));
				activityToAdd.put(jpFreq[3], (((PeriodicActivity) (activity.getFrequency())).getEndDate().toString()));
				activityToAdd.put(jpFreq[4], (((PeriodicActivity) (activity.getFrequency())).getCadence().toString()));
				
			}
			activityToAdd.put(jpCert, cert);

			if(activityArray!=null) {
				activityToAdd.put(jpID,Integer.toUnsignedLong(activityArray.size()));
				activityIdToAdd.put(jpID,Integer.toUnsignedLong(activityArray.size()));
			} else {
				activityToAdd.put(jpID,0L);
				activityIdToAdd.put(jpID,0L);
			}
			
			id = ((Long)activityIdToAdd.get(jpID));
			
			for(i=0;i<placeArray.size();i++) 
			{
				result = (JSONObject) placeArray.get(i);

				Long idPlace = (Long) result.get(jpID);
					
				if (activity.getPlace().getId()==(idPlace)) {
					
					JSONArray activitiesIdArray = (JSONArray) result.get(jpResActivity);
					
					//Passando il posto in cui sto aggiungendo l'attivita, controllo se l'attivita che voglio aggiungere e gia presente.
					
					if(fac.isInJSON(p, activity.getName(),activity.getCreator().getUserID()))	
						return -1L;	
					activitiesIdArray.add(activityIdToAdd); //Salvo l'id dell'attivita al posto di appartenenza.
					result.put(jpResActivity, activitiesIdArray);
					
					try (FileWriter file = new FileWriter(placeFileName)){
						file.write(place.toString());
						file.flush();
					}
					
					activityArray.add(activityToAdd);
					
					try (FileWriter file2 = new FileWriter(activityFileName)){
						file2.write(activitiesJOBJ.toString());
						file2.flush();
					}
					
					return id;
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1L;
			}
		return -1L;
	
	}

	public boolean updateActivityJSON(SuperActivity sua) {
		return updateActivityPreferences(sua, false);
	}
	
	public boolean updateActivityPreferences(SuperActivity sua) {
		return updateActivityPreferences(sua, true);
	}
	
	public boolean updateActivityPreferences(SuperActivity sua, boolean updatePref) {
		JSONParser parser = new JSONParser();
		ArrayList<String> oldpref = new ArrayList<>();
		int i;
		int j;
		
		try		
		{
			Object activitiesParser = parser.parse(new FileReader(activityFileName));
			JSONObject activitiesJOBJ = (JSONObject) activitiesParser;
			JSONArray activityArray = (JSONArray) activitiesJOBJ.get(jpResActivity);
			JSONArray preferences = new JSONArray();
			JSONArray oldpreferences;
			
			JSONObject result;

			if(activityArray==null) {
				Log.getInstance().logger.info("Non ci sono attivita da dover modificare!\n");
				return false;
			}
			
			if (updatePref) {
				preferences.addAll((ArrayList<String>) sua.getPreferences());
				
			}
			
			for(i=0;i<activityArray.size();i++){
					
				result = (JSONObject) activityArray.get(i);
				if (updatePref) {
					if(((Long)result.get(jpID))==sua.getId()) {
					
						oldpref = funcUpdateActJObj(oldpref, result, sua);
						
						if(!sua.getPreferences().equals(oldpref)) {
							
							result.put(jpPref,preferences);
						
							try (FileWriter file = new FileWriter(activityFileName)){
								file.write(activitiesJOBJ.toString());
								file.flush();
							}						
							return true;
						
						} else return false;
					}
				} else {
					
					funcUpdatePrefJObj(result, sua);
					
					try (FileWriter file = new FileWriter(activityFileName)){
					file.write(activitiesJOBJ.toString());
					file.flush();
					}
					
					return true;
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			}
		return false;
	}
	
	//----------metodi di supporto alla updateActivityPreferences---------
	private ArrayList<String> funcUpdateActJObj(ArrayList<String> oldpref,JSONObject result, SuperActivity sua) {
		int j;
		if(((Long)result.get(jpID))==sua.getId()) {
			JSONArray oldpreferences;
			
			oldpreferences = (JSONArray) result.get(jpPref);
			//Si ricostruisce l'arrayList delle preferenze per compararlo con il nuovo
			//che si andra ad inserire. Se sono uguali si esce restituendo falso.
			//Se vero si procede nel salvataggio.
			for(j=0;j<oldpreferences.size();j++) {
				oldpref.add((String)oldpreferences.get(j));
			}  
		} return oldpref;
	}
	
	private void funcUpdatePrefJObj(JSONObject result, SuperActivity sua) {
		result.put(jpPlace, sua.getPlace().getId());
		result.put(jpCreator, sua.getCreator().getUserID());
		result.put(jpPref, sua.getPreferences());
		result.put(jpActName, sua.getName());
		if(sua.getFrequency() instanceof ContinuosActivity) {
			result.put(jpFreq[0], sua.getFrequency().getOpeningTime().toString());
			result.put(jpFreq[1], sua.getFrequency().getClosingTime().toString());
			result.put(jpFreq[2], null);
			result.put(jpFreq[3], null);
			result.put(jpFreq[4], null);
			
		}
		if(sua.getFrequency() instanceof ExpiringActivity) {
			result.put(jpFreq[0], sua.getFrequency().getOpeningTime().toString());
			result.put(jpFreq[1], sua.getFrequency().getClosingTime().toString());
			result.put(jpFreq[2], (((ExpiringActivity) (sua.getFrequency())).getStartDate().toString()));
			result.put(jpFreq[3], (((ExpiringActivity) (sua.getFrequency())).getEndDate().toString()));
			result.put(jpFreq[4], null);
			
		}
		if(sua.getFrequency() instanceof PeriodicActivity) {
			result.put(jpFreq[0], sua.getFrequency().getOpeningTime().toString());
			result.put(jpFreq[1], sua.getFrequency().getClosingTime().toString());
			result.put(jpFreq[2], (((PeriodicActivity) (sua.getFrequency())).getStartDate().toString()));
			result.put(jpFreq[3], (((PeriodicActivity) (sua.getFrequency())).getEndDate().toString()));
			result.put(jpFreq[4], (((PeriodicActivity) (sua.getFrequency())).getCadence().toString()));	
		}
		
	}
	//----------fine  metodi di supporto alla updateActivityPreferences---------
	
	
	public boolean deleteActivityJSON(SuperActivity sua) {
		JSONParser parser = new JSONParser();
		daoPl = DAOPlace.getInstance();
		Place pl;
		
		int i;
		try 
		{
			Object activitiesParser = parser.parse(new FileReader(activityFileName));
			JSONObject activitiesJOBJ = (JSONObject) activitiesParser;
			JSONArray activityArray = (JSONArray) activitiesJOBJ.get(jpResActivity);
			
			JSONObject result;

			if(activityArray==null) {
				Log.getInstance().logger.info("Non ci sono attivita da dover modificare!\n");
				return false;
			}
			
			 
			for(i=0;i<activityArray.size();i++){
				
				result = (JSONObject) activityArray.get(i);
				
				if(((Long)result.get(jpID))==sua.getId()) {
				
					activityArray.remove(i);
					
					Object places = parser.parse(new FileReader(placeFileName));
					JSONObject place = (JSONObject) places;
					JSONArray placeArray = (JSONArray) place.get(jpPlaces);
					JSONObject resultPlace;
					
					pl = daoPl.findPlaceInJSON(sua.getPlace().getName(), sua.getPlace().getCity(), sua.getPlace().getRegion());
					
					resultPlace = (JSONObject) placeArray.get(pl.getId().intValue());
					JSONArray activityInPlace = (JSONArray) resultPlace.get(jpResActivity);
					for(i=0;i<activityInPlace.size();i++) {
						
						//Prendo un elemento dell'array di eventi nel posto, estraggo il contenuto corrispondente alla chiave id
						//Fatto cio converto a Long l'oggetto risultante ed infine lo confronto con l'id dell'attivita per eliminarlo
						//Anche dall'array di eventi nel json dei places.
						if(((Long)((JSONObject)activityInPlace.get(i)).get(jpID)==sua.getId())){
							activityInPlace.remove(i);
							Log.getInstance().logger.info("L'attivita e stata eliminata anche dalla lista dei places.\n");
						}
					}
					
					try (FileWriter file = new FileWriter(activityFileName)){
					file.write(activitiesJOBJ.toString());
					file.flush();
					}
					
					try (FileWriter file2 = new FileWriter(placeFileName)){
					file2.write(place.toString());
					file2.flush();
					}
					
					return true;
				}
			}
		}  catch (Exception e) {
			e.printStackTrace();
			return false;
			}
		return false;
	}
	
	public List<SuperActivity> findActivityByPreference (DAOSuperUser daoSU, String preference){
		ArrayList<SuperActivity> matchingActivities = new ArrayList<>();
		SuperActivity matchingActivity;
		JSONParser parser = new JSONParser();
		
		int i;
		try 
		{
			//Si parsa il JSON delle attivita, si estrae poi l'array di attivita in esso contenuto.
			Object activitiesParser = parser.parse(new FileReader(activityFileName));
			JSONObject activitiesJOBJ = (JSONObject) activitiesParser;
			JSONArray activityArray = (JSONArray) activitiesJOBJ.get(jpResActivity);
			JSONArray preferenceList;
			//Si prepara l'oggetto result, dove contenere attivita estratte dall'array activityArray.
			JSONObject result;

			//Se nullo si conclude la ricerca restituendo null per indicarne il fallimento.
			if(activityArray==null) {
				Log.getInstance().logger.info("Non ci sono attivita da dover cercare!\n");
				return null;
			}
			
			//Si inizia a scandire l'array di attivita in cerca di quella che contenga almeno una preferenza che combaci con quella cercata.
			for(i=0;i<activityArray.size();i++){
				
				result = (JSONObject) activityArray.get(i);
				preferenceList = (JSONArray) result.get(jpPref);
				
				if(preferenceList.contains(preference.toUpperCase())) {
					
					if(((String)result.get(jpCert)).equals("yes")) {
						matchingActivity = (CertifiedActivity) createActClass(daoSU, result, daoPl.findPlaceById((Long)result.get(jpPlace)));
					} else {	
						matchingActivity = (NormalActivity) createActClass(daoSU, result, daoPl.findPlaceById((Long)result.get(jpPlace)));
					}
					matchingActivities.add(matchingActivity);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
			}
		return matchingActivities;
	}
	
	public SuperActivity findActivityByID (DAOSuperUser daoSU, Place p, int n){
	JSONParser parser = new JSONParser();
	int i;
	int j;
	try 
	{
		Object places = parser.parse(new FileReader(placeFileName));
		JSONObject place = (JSONObject) places;
		JSONArray placeArray = (JSONArray) place.get(jpPlaces);
		JSONObject result;
		
		for(i=0;i<placeArray.size();i++) 
		{
			result = (JSONObject)placeArray.get(i);
			
			//Sfruttando l'id del posto si controlla prima quale posto sia quello in cui cercare una particolare attivita.
			
			Long idPlace = (Long) result.get(jpID);
			
			if (p.getId()==idPlace) {
					JSONArray activities = (JSONArray) result.get(jpResActivity);
					
					//Se non ci sono attivita nel posto c'e poco da cercare.
					if(activities.size()==0)
						return null;
					
					JSONObject activity;
					JSONObject activityJSON;
					
					for(j=0;j<activities.size();j++) {
						
						activity = (JSONObject) activities.get(j);
						if(((Long)activity.get(jpID)).intValue()==n) {
							//Si controlla se certificata o no l'attivita, passato il test si controlla anche che tipo di attivita ricorrente sia:
							
							Object activitiesParser = parser.parse(new FileReader(activityFileName));
							JSONObject activitiesJOBJ = (JSONObject) activitiesParser;
							JSONArray activityArray = (JSONArray) activitiesJOBJ.get(jpResActivity);
							
							activityJSON = (JSONObject)activityArray.get(n);
							
							return createActClass(daoSU,activityJSON, p);
							}
						}
					}
			} 
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	
	
	public SuperActivity findActivityByID(DAOSuperUser daoSU,Long id) {
		JSONParser parser = new JSONParser();
		DAOPlace daoP = DAOPlace.getInstance();
		
		int j;
		try 
		{
			Object activities = parser.parse(new FileReader(activityFileName));
			JSONObject activityJObj = (JSONObject) activities;
			JSONArray activityArray = (JSONArray) activityJObj.get(jpResActivity);
			JSONObject activity;
			
			//Se non ci sono attivita nel posto c'e poco da cercare.
			if(activityArray.size()==0)
				return null;
			
			for(j=0;j<activityArray.size();j++) {
				
				activity = (JSONObject) activityArray.get(j);
				if(Long.compare((Long)activity.get("id"),id)==0) {
					//Si controlla se certificata o no l'attivita, passato il test si controlla anche che tipo di attivita ricorrente sia:
						
					JSONObject activityJSON = (JSONObject)activityArray.get(id.intValue());
					
					return createActClass(daoSU, activityJSON, daoP.findPlaceById((Long)activityJSON.get(jpPlace)));
					}	
			}
			
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	
	}
	
	private SuperActivity createActClass(DAOSuperUser daoSU, JSONObject activityJSON, Place p) {
		SuperActivity resultActivity;
		if(((String)activityJSON.get(jpCert)).equals("yes")) {			
			
			//Se startdate e nulla allora l'attivita sara chiaramente un'attivita Continuos.
			if((activityJSON.get(jpFreq[2]))==null) {
				resultActivity = new CertifiedActivity((String)activityJSON.get(jpActName),daoSU.findSuperUserByID((Long)activityJSON.get(jpCreator)), p,LocalTime.parse((String)activityJSON.get(jpFreq[0])),LocalTime.parse((String)activityJSON.get(jpFreq[1])));
				resultActivity.setId(((Long)activityJSON.get(jpID)));
				resultActivity.setPreferences(((ArrayList<String>)activityJSON.get(jpPref)));
				return resultActivity;
			}
			//Se cadence e nulla allora l'attivita sara chiaramente un'attivita Expiring.
			if((activityJSON.get(jpFreq[4]))==null) {
				resultActivity = new CertifiedActivity((String)activityJSON.get(jpActName),daoSU.findSuperUserByID((Long)activityJSON.get(jpCreator)), p, LocalTime.parse((String)activityJSON.get(jpFreq[0])),LocalTime.parse((String)activityJSON.get(jpFreq[1])), LocalDate.parse((String)activityJSON.get(jpFreq[2])), LocalDate.parse((String)activityJSON.get(jpFreq[3])));
				resultActivity.setId(((Long)activityJSON.get(jpID)));
				resultActivity.setPreferences(((ArrayList<String>)activityJSON.get(jpPref)));
				return resultActivity;
			}
			//A seguito dei check si capisce che l'attivita sara Periodica.
			resultActivity = new CertifiedActivity((String)activityJSON.get(jpActName),daoSU.findSuperUserByID((Long)activityJSON.get(jpCreator)), p,LocalTime.parse((String)activityJSON.get(jpFreq[0])),LocalTime.parse((String)activityJSON.get(jpFreq[1])),LocalDate.parse((String)activityJSON.get(jpFreq[2])), LocalDate.parse((String)activityJSON.get(jpFreq[3])),Cadence.valueOf((String)activityJSON.get(jpFreq[4])));
			resultActivity.setId(((Long)activityJSON.get(jpID)));
			resultActivity.setPreferences(((ArrayList<String>)activityJSON.get(jpPref)));
			return resultActivity;
		}
		else {	
			
			//Se startdate e nulla allora l'attivita sara chiaramente un'attivita Continuos.
			if((activityJSON.get(jpFreq[2]))==null) {
				resultActivity = new NormalActivity((String)activityJSON.get(jpActName),daoSU.findSuperUserByID((Long)activityJSON.get(jpCreator)), p,LocalTime.parse((String)activityJSON.get(jpFreq[0])),LocalTime.parse((String)activityJSON.get(jpFreq[1])));
				resultActivity.setId(((Long)activityJSON.get(jpID)));
				resultActivity.setPreferences(((ArrayList<String>)activityJSON.get(jpPref)));
				return resultActivity;
			}
			//Se cadence e nulla allora l'attivita sara chiaramente un'attivita Expiring.
			if((activityJSON.get(jpFreq[4]))==null) {
				resultActivity = new NormalActivity((String)activityJSON.get(jpActName),daoSU.findSuperUserByID((Long)activityJSON.get(jpCreator)), p,LocalTime.parse((String)activityJSON.get(jpFreq[0])),LocalTime.parse((String)activityJSON.get(jpFreq[1])),LocalDate.parse((String)activityJSON.get(jpFreq[2])), LocalDate.parse((String)activityJSON.get(jpFreq[3])));
				resultActivity.setId(((Long)activityJSON.get(jpID)));
				resultActivity.setPreferences(((ArrayList<String>)activityJSON.get(jpPref)));
				return resultActivity;
				}
			//A seguito dei check si capisce che l'attivita sara Periodica.
				resultActivity = new NormalActivity((String)activityJSON.get(jpActName),daoSU.findSuperUserByID((Long)activityJSON.get(jpCreator)), p,LocalTime.parse((String)activityJSON.get(jpFreq[0])),LocalTime.parse((String)activityJSON.get(jpFreq[1])),LocalDate.parse((String)activityJSON.get(jpFreq[2])), LocalDate.parse((String)activityJSON.get(jpFreq[3])),Cadence.valueOf((String)activityJSON.get(jpFreq[4])));
				resultActivity.setId(((Long)activityJSON.get(jpID)));
				resultActivity.setPreferences(((ArrayList<String>)activityJSON.get(jpPref)));
				return resultActivity;
			}
	}
	
	public boolean isInJSON(DAOSuperUser daoSU, Place p, String activityName, Long creatorId) {
		JSONParser parser = new JSONParser();
		int i;
		int j;
		try 
		{
			Object places = parser.parse(new FileReader(placeFileName));
			JSONObject place = (JSONObject) places;
			JSONArray placeArray = (JSONArray) place.get(jpPlaces);
			JSONObject result;
			
			Object activitiesParser = parser.parse(new FileReader(activityFileName));
			JSONObject activitiesJOBJ = (JSONObject) activitiesParser;
			JSONArray activityArray = (JSONArray) activitiesJOBJ.get(jpResActivity);
			
			for(i=0;i<placeArray.size();i++) 
			{
				result = (JSONObject)placeArray.get(i);
				
				//Sfruttando l'id del posto si controlla prima quale posto sia quello in cui cercare una particolare attivita.
				
				Long idPlace = (Long) result.get(jpID);
				
				if (p.getId()==(idPlace)) {
						JSONArray activities = (JSONArray) result.get(jpResActivity);
						
						//Se non ci sono attivita nel posto c'e poco da cercare.
						if(activities.size()==0)
							return false;
						
						JSONObject activity;
						JSONObject activityJSON;
						
						for(j=0;j<activities.size();j++) {
							
							activity = (JSONObject) activities.get(j);
							//Preso l'oggetto activity dal JSON places, scandisco il JSON activities sfruttando il campo
							//id della lista di attivita nei posti. In questo modo posso vedere quali attivita appartengono
							//al posto che mi interessa per poi eventualmente vedere se tra le attivita e gia presente quella che volevo aggiungere.
							activityJSON = (JSONObject)activityArray.get(((Long)activity.get(jpID)).intValue());
							
							//Se tra le attivita che ho scandito con questo ciclo for ho trovato una con nome utente che ha creato l'attivita e nome dell'attivita
							//UGUALI a quella che stavo per aggiungere restituisco true, indicando che GIA' e presente l'attivita.
							if(((Long)(activityJSON.get(jpCreator))).equals(creatorId) && (((String)activityJSON.get(jpActName)).toUpperCase()).equals(activityName.toUpperCase())) {
								return true;
							}
						}
				} 
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
}
