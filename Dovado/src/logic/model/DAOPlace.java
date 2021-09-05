package logic.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DAOPlace {
	
	private static DAOPlace INSTANCE;
	private DAOSuperUser daoSu;
	private String placeFileName = "WebContent/places.json"; 
	
	private String jpPlaces = "places";
	private String jpPlaName = "name";
	private String jpAddress = "address";
	private String jpCity = "city";
	private String jpCivic = "civico";
	private String jpRegion = "region";
	private String jpID = "id";
	private String jpAct = "activities";
	private String jpOwner = "owner";
	
		private DAOPlace() {
	}
	
	public static DAOPlace getInstance() {
		if(INSTANCE==null)
			INSTANCE = new DAOPlace();
		return INSTANCE;
	}
	
	public String getPlaceFileName() {
		return this.placeFileName;
	}
	
	//Aggiunto un metodo per trovare un posto tramite ID, utile durante la reistanzazione da persistenza delle attivita.
	public Place findPlaceById(Long id) {
		JSONParser parser = new JSONParser();
		int i;
		daoSu = DAOSuperUser.getInstance();
		try 
		{
			Object places = parser.parse(new FileReader(placeFileName));
			JSONObject place = (JSONObject) places;
			JSONArray placeArray = (JSONArray) place.get(jpPlaces);
			JSONObject result;

			for(i=0;i<placeArray.size();i++) 
			{
				result = (JSONObject)placeArray.get(i);
				
				Long idJSON = (Long) result.get(jpID);
				
				if (idJSON.equals(id)) {
					Place placeFound = new Place((String)result.get(jpPlaName),(String) result.get(jpAddress),(String)result.get(jpCity),(String)result.get(jpRegion),(String) result.get(jpCivic),(Partner) daoSu.findSuperUserByID((Long)result.get(jpOwner)));
					placeFound.setId((Long) result.get(jpID));
					return placeFound;
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
			}
		return null;
	}
	
	public Place findPlaceInJSON(String name, String city, String region) {
		JSONParser parser = new JSONParser();
		int i;
		daoSu = DAOSuperUser.getInstance();
		try 
		{
			Object places = parser.parse(new FileReader(placeFileName));
			JSONObject place = (JSONObject) places;
			JSONArray placeArray = (JSONArray) place.get(jpPlaces);
			JSONObject result;

			for(i=0;i<placeArray.size();i++) 
			{
				result = (JSONObject)placeArray.get(i);
				
				String namePrint = (String) result.get(jpPlaName);
				String cityPrint = (String) result.get(jpCity);
				String regionPrint = (String) result.get(jpRegion);
				
				if (name.equals(namePrint) && city.equals(cityPrint) && region.equals(regionPrint)) {
					Place placeFound = new Place(namePrint,(String) result.get(jpAddress),cityPrint,regionPrint,(String) result.get(jpCivic),(Partner) daoSu.findSuperUserByID((Long)result.get(jpOwner)));
					placeFound.setId((Long) result.get(jpID));
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
		
		newPlace.put(jpAddress, address);
		newPlace.put(jpCivic, civico);
		newPlace.put(jpPlaName, name);
		newPlace.put(jpCity, city);
		newPlace.put(jpRegion, region);
		newPlace.put(jpAct, newPlaceActivities);
		if(owner!=null)
			newPlace.put(jpOwner, owner.getUserID());
		else 
			newPlace.put(jpOwner, null); 
		
		try (FileWriter file = new FileWriter(placeFileName))
		{
			Object places = parser.parse(new FileReader(placeFileName));
			JSONObject place = (JSONObject) places;
			JSONArray placeArray = (JSONArray) place.get(jpPlaces);
			JSONObject result;
			
			for(i=0;i<placeArray.size();i++) 
			{
				result = (JSONObject)placeArray.get(i);

				String addressPrint = (String) result.get(jpAddress);
				String civicoPrint = (String) result.get(jpCivic);
				String namePrint = (String) result.get(jpPlaName);
				String cityPrint = (String) result.get(jpCity);
				String regionPrint = (String) result.get(jpRegion);
				
				if(address.equals(addressPrint) && civico.equals(civicoPrint) && name.equals(namePrint) && city.equals(cityPrint) && region.equals(regionPrint))
					return ((Long) result.get(jpID)).intValue();
			}
			id=i;
			newPlace.put(jpID, id);
			placeArray.add(newPlace);
			
			file.write(place.toString());
			file.flush();
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
		
		try (FileWriter file = new FileWriter(placeFileName))
		{
			Object places = parser.parse(new FileReader(placeFileName));
			JSONObject place = (JSONObject) places;
			JSONArray placeArray = (JSONArray) place.get(jpPlaces);
			JSONObject result;
			
			for(i=0;i<placeArray.size();i++) 
			{
				result = (JSONObject)placeArray.get(i);

				Long placeId = ((Long) result.get(jpID));
				
				/*--------------------PRINT DI CONTROLLO-----------------------------------------------*/
				Log.getInstance().getLogger().info("Controllo di comparazione ID posti:");
				Log.getInstance().getLogger().info(String.valueOf(p.getId()==(placeId)));
				Log.getInstance().getLogger().info(String.valueOf(p.getOwner().getUserID()));
				/*--------------------PRINT DI CONTROLLO-----------------------------------------------*/				
				
				if(p.getId()==(placeId)) {
					//Salvo nella persistenza il proprietario con il suo nome. In futuro lo si potrebbe salvare in base all'id.
					result.put(jpOwner, p.getOwner().getUserID());
					placeArray.set(i, result);
					break;
				}
			}
			
			file.write(place.toString());
			file.flush();
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			}
		return true;
	}

	
}