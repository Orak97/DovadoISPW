package logic.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DAOPreferences {
	
	private static DAOPreferences INSTANCE;
	private static final  String PREFERJSON = "WebContent/preferences.json" ;
	
	private DAOPreferences() {
	}
	
	public static DAOPreferences getInstance() {
		if(INSTANCE==null)
			INSTANCE = new DAOPreferences();
		return INSTANCE;
	}
	
	public String getPreferenceFromJSON(int placeInJSON) {
		String preference;
		JSONParser parser = new JSONParser();
		try 
		{
			Object preferences = parser.parse(new FileReader(PREFERJSON));
			JSONObject preferenceOBJ = (JSONObject) preferences;
			JSONArray prefArray = (JSONArray) preferenceOBJ.get("preferences");
			JSONObject result;
			
			result = (JSONObject)prefArray.get(placeInJSON);
				
			preference = ((String) result.get("name"));
			
			return preference;
			
			//Se uscito dal ciclo for la preferenza non era presente nella persistenza
			//Per un possibile uso futuro quindi la si aggiunge; restituendo il suo id.
			
			// POTREMMO VOLERLO CAMBIARE ( SE NON VOGLIAMO IL JSON INTASATO DI SINONIMI DI UNO STESSO
			// INSERITO DIVERSE VOLTE DA PARTE DI UTENTI ) TOGLIENDO L'AGGIUNTA IN AUTOMATICO DELLA PREFERENZA.
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean preferenceIsInJSON(String preferenceName){
		JSONParser parser = new JSONParser();
		int i;
		try
		{
			Object preferences = parser.parse(new FileReader(PREFERJSON));
			JSONObject preferenceOBJ = (JSONObject) preferences;
			JSONArray prefArray = (JSONArray) preferenceOBJ.get("preferences");
			Log.getInstance().getLogger().info("Ho trovato preferenze");
			JSONObject result;
			preferenceName = preferenceName.toUpperCase();
			
			for(i=0;i<prefArray.size();i++) {
				result = (JSONObject)prefArray.get(i);
				
				String name = ((String) result.get("name")).toUpperCase();
			
				if (preferenceName.equals(name)) {
					Log.getInstance().getLogger().info(name+" uguale a "+preferenceName);
					return true;
				}
				
			}
			
		//TODO: discutere se aggiungere o no le preferenze
		//se non trovate.
			
			/*JSONObject newPref = new JSONObject();
			newPref.put("name", preferenceName);
			prefArray.add(newPref);
			
			try (FileWriter file = new FileWriter(PREFERJSON)){
				file.write(preferenceOBJ.toString());
				file.flush();
			}*/
			Log.getInstance().getLogger().info("Non ho trovato preferenze uguali a "+preferenceName);
			
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
