package logic.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DAOPlace {
	
	private static DAOPlace INSTANCE;
	private static final  String PLACEJSON = "WebContent/places.json" ;
	private static final  String PLACESKEY = "places";
	private static final  String NAMEKEY = "name";
	private static final  String REGIONKEY = "region";
	private static final  String CITYKEY = "city";
	private static final  String ADDRESSKEY = "address";
	private static final  String CIVICOKEY = "civico";
	private static final  String ACTIVITIESKEY = "activities";
	private static final  String IDKEY = "id";
	private static final  String OWNERKEY = "owner";

	
	
	private DAOPlace() {
	}
	
	public static DAOPlace getInstance() {
		if(INSTANCE==null)
			INSTANCE = new DAOPlace();
		return INSTANCE;
	}

	public String getJsonFileName() {
		return PLACEJSON;
	}
	//Aggiunto un metodo per trovare un posto tramite ID, utile durante la reistanzazione da persistenza delle attivita.
	public Place findPlaceById(Long id) {
		return findPlace(null, null, null, id);
	}
	public Place findPlaceInJSON(String name, String city, String region) {
		return findPlace(name, city, region, null);
	}
	
	public Place findPlace (String name, String city, String region,Long id) {
		JSONParser parser = new JSONParser();
		int i;
		DAOSuperUser daoSu = DAOSuperUser.getInstance();
		try 
		{
			Object places = parser.parse(new FileReader(PLACEJSON));
			JSONObject place = (JSONObject) places;
			JSONArray placeArray = (JSONArray) place.get(PLACESKEY);
			JSONObject result;
			boolean expression;

			for(i=0;i<placeArray.size();i++) 
			{
				result = (JSONObject)placeArray.get(i);
				
				Long idJSON = (Long) result.get(IDKEY);
				String namePrint = (String) result.get(NAMEKEY);
				String cityPrint = (String) result.get(CITYKEY);
				String regionPrint = (String) result.get(REGIONKEY);
				
				if (id != null) {
					expression = idJSON.equals(id);
				} else {
					expression = name.equals(namePrint) && city.equals(cityPrint) && region.equals(regionPrint);
				}
				
				if (expression) {
					Place placeFound = new Place(namePrint,(String) result.get(ADDRESSKEY),cityPrint,regionPrint,(String) result.get(CIVICOKEY),(Partner) daoSu.findSuperUserByID((Long)result.get(OWNERKEY)));
					placeFound.setId((Long) result.get(IDKEY));
					return placeFound;
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
			}
		return null;
	}
	
	
	public int addPlaceToJSON(String address, String name, String city, String region,String civico, Partner owner) {
		int i;
		int id=0;
		
		JSONParser parser = new JSONParser();
		JSONObject newPlace = new JSONObject();
		JSONArray newPlaceActivities = new JSONArray();
		
		newPlace.put(ADDRESSKEY, address);
		newPlace.put(CIVICOKEY, civico);
		newPlace.put(NAMEKEY, name);
		newPlace.put(CITYKEY, city);
		newPlace.put(REGIONKEY, region);
		newPlace.put(ACTIVITIESKEY, newPlaceActivities);
		if(owner!=null)
			newPlace.put(OWNERKEY, owner.getUserID());
		else 
			newPlace.put(OWNERKEY, null); 
		
		try 
		{
			Object places = parser.parse(new FileReader(PLACEJSON));
			JSONObject place = (JSONObject) places;
			JSONArray placeArray = (JSONArray) place.get(PLACESKEY);
			JSONObject result;
			
			for(i=0;i<placeArray.size();i++) 
			{
				result = (JSONObject)placeArray.get(i);

				String addressPrint = (String) result.get(ADDRESSKEY);
				String civicoPrint = (String) result.get(CIVICOKEY);
				String namePrint = (String) result.get(NAMEKEY);
				String cityPrint = (String) result.get(CITYKEY);
				String regionPrint = (String) result.get(REGIONKEY);
				
				if(address.equals(addressPrint) && civico.equals(civicoPrint) && name.equals(namePrint) && city.equals(cityPrint) && region.equals(regionPrint))
					return ((Long) result.get(IDKEY)).intValue();
			}
			id=i;
			newPlace.put(IDKEY, id);
			placeArray.add(newPlace);
			
			try (FileWriter file = new FileWriter(PLACEJSON)){
				file.write(place.toString());
				file.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
			}
		return id;
	}

	//Aggiunto per permettere la modifica di qualsiasi attributo in Place
	//salvandola anche in persistenza.
	public boolean updatePlaceJSON(Place p) {
		
		int i;
		JSONParser parser = new JSONParser();
		
		try {
			Object places = parser.parse(new FileReader(PLACEJSON));
			JSONObject place = (JSONObject) places;
			JSONArray placeArray = (JSONArray) place.get(PLACESKEY);
			JSONObject result;
			
			for(i=0;i<placeArray.size();i++) 
			{
				result = (JSONObject)placeArray.get(i);

				Long placeId = ((Long) result.get(IDKEY));
				
				/*--------------------PRINT DI CONTROLLO-----------------------------------------------*/
				Log.getInstance().logger.info("Controllo di comparazione ID posti:");
				Log.getInstance().logger.info(String.valueOf(p.getId()==(placeId)));
				Log.getInstance().logger.info(String.valueOf(p.getOwner().getUserID()));
				/*--------------------PRINT DI CONTROLLO-----------------------------------------------*/				
				
				if(p.getId()==(placeId)) {
					//Salvo nella persistenza il proprietario con il suo nome. In futuro lo si potrebbe salvare in base all'id.
					result.put(OWNERKEY, p.getOwner().getUserID());
					placeArray.set(i, result);
					break;
				}
			}
			try (FileWriter file = new FileWriter(PLACEJSON)) {
				file.write(place.toString());
				file.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			}
		return true;
	}

	
}