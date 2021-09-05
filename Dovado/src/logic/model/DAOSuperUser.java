package logic.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DAOSuperUser {
	
	private static DAOSuperUser INSTANCE;
	private String userFileName;
	
	private String jpPref = "preferences";
	private String jpID = "id";
	private String jpEmail = "email";
	private String jpUsername = "username";
	private String jpPartner = "partner";
	private String jpPassword = "password";
	private String jpWallet = "wallet";
	private String jpUsers = "users"; 
	
	
	
	private DAOSuperUser() {
		userFileName = "WebContent/user.json";
	}
	
	public static DAOSuperUser getInstance() {
		if(INSTANCE==null)
			INSTANCE = new DAOSuperUser();
		return INSTANCE;
	}
	
	public boolean addUserToJSON(String email, String username, int partner, String password) {
		JSONParser parser = new JSONParser();
		try {
			Object users = parser.parse(new FileReader(userFileName));
			JSONObject userObj = (JSONObject) users;
			JSONArray userArray = (JSONArray) userObj.get(jpUsers);
			JSONArray userPref = new JSONArray();
			
			
			if (findSuperUser(email, password, null)==(null)) {				
				JSONObject newUser = new JSONObject();

				newUser.put(jpID, userArray.size());
				newUser.put(jpEmail, email);
				newUser.put(jpUsername, username);
				newUser.put(jpPartner, partner);
				if (partner == 0) {
					newUser.put(jpWallet, 0);
				}
				newUser.put(jpPassword, password);
				newUser.put(jpPref, userPref);
				userArray.add(newUser);

				FileWriter file = new FileWriter(userFileName);
				file.write(userObj.toString());
				file.flush();
				file.close();
			}
			//TODO vale per tutte le eccezioni dei DAO. Non gestendole separatamente ho eliminato le eccezioni ridondanti 
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	//Creiamo un metodo per la modifica delle preferenze sul tipo di attivita collegate ad un utente.
	public boolean updateUserPreferences(SuperUser su) {
		JSONParser parser = new JSONParser();
		ArrayList<String> oldpref = new ArrayList<>();
		int i;
		int j;
		
		try (FileWriter file = new FileWriter(userFileName)){
			Object users = parser.parse(new FileReader(userFileName));
			JSONObject userRes = (JSONObject) users;
			JSONArray userArray = (JSONArray) userRes.get(jpUsers);
			JSONArray preferences = new JSONArray();
			JSONObject result;
			
			//Aggiungo tutte le preferenze al nuovo JSONArray che ho creato.
			preferences.addAll(su.getPreferences());
			
			//Fatto cio vado a cercare all'interno del JSON il SuperUser che ho come istanza.
			for(i=0;i<userArray.size();i++) {
				result = (JSONObject)userArray.get(i);
				
				Long idJson = (Long) result.get(jpID);
				JSONArray oldpreferences;
				//Se trovato l'utente si pone all'interno dell'attributo jpPref
				//il nuovo JSONArray appositamente preparato in precedenza.
				if (su.getUserID().equals(idJson)) {
					oldpreferences = (JSONArray) result.get(jpPref);
					//Si ricostruisce l'arrayList delle preferenze per compararlo con il nuovo
					//che si andra ad inserire; Se sono uguali si esce restituendo falso.
					//Se vero si procede nel salvataggio.
					for(j=0;j<oldpreferences.size();j++) {
						oldpref.add((String)oldpreferences.get(j));
					}
					
					if(!su.getPreferences().equals(oldpref)) {
						result.put(jpPref, preferences);
						
						file.write(userRes.toString());
						file.flush();
												
						return true;
					} 
					else return false;
				}
			}			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	//metodo creato per l'aggiornamento del portafoglio dell'utente partendo dall'ID
	public boolean updateUserWallet(Long id, Long wallet) {
		JSONParser parser = new JSONParser();
		Long daoWallet  =  (long) 0;
		int i;
		
		try (FileWriter file = new FileWriter(userFileName)) {
			Object users = parser.parse(new FileReader(userFileName));
			JSONObject userRes = (JSONObject) users;
			JSONArray userArray = (JSONArray) userRes.get(jpUsers);
			JSONObject result;
			
			
			//Fatto cio vado a cercare all'interno del JSON l'utente il cui id voglio aggiornare il wallet
			for(i=0;i<userArray.size();i++) {
				result = (JSONObject)userArray.get(i);
				
				Long idJson = (Long) result.get(jpID);
				//Se trovato l'utente si pone all'interno dell'attributo "preferences"
				//il nuovo JSONArray appositamente preparato in precedenza.
				if (id.equals(idJson)) {
					daoWallet = (Long) result.get(jpWallet);
					if (daoWallet == null) {
						daoWallet =(long) 0;
					}
					daoWallet = daoWallet + wallet;
					
					if (daoWallet <= 0) {
						return false;
					}
					
						result.put(jpWallet, daoWallet);
						
					
						file.write(userRes.toString());
						file.flush();
						
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
	
	//Creiamo una istanza di una classe a partire dall'id
	public SuperUser findSuperUserByID(Long id ){
		return findSuperUser(null, null, id);
	}
	
	//Creiamo istanza di una classe sapendo la mail-----COME SOTTO ANDREBBE SOSTITUITA DA UN METODO CHE RITORNI L'ID
	public SuperUser findSuperUserByEmail(String email) {
		return findSuperUser(email,null,null);
	}
	
	//qui controlliamo che la mail sia uguale, utile per il login
	public SuperUser findSuperUser (String email, String psw, Long id) {
		JSONParser parser = new JSONParser();
		int i;
		boolean founded = false;
		try 
		{
			Object users = parser.parse(new FileReader(userFileName));
			JSONObject userRes = (JSONObject) users;
			JSONArray userArray = (JSONArray) userRes.get(jpUsers);
			JSONObject result = null;

			for(i=0;i<userArray.size();i++) {
				result = (JSONObject)userArray.get(i);
	
				String emailJSON = (String) result.get(jpEmail);
				String passwordJSON = (String) result.get(jpPassword);
				Long idJson = (Long) result.get(jpID);
			
				//Qui controllo nel caso uso la mail per cercare
				if (email != null && email.equals(emailJSON)) {
					if(psw == null) {
						Log.getInstance().getLogger().warning("PASSWORD NULLA");
						founded = true;
					}
					else if (!psw.equals(passwordJSON)) {
						Log.getInstance().getLogger().info("PASSWORD SBAGLIATA");
						return null;
					} else {
						Log.getInstance().getLogger().warning("PASSWORD CORRETTA");
						founded = true;
					}
				}
				//Qui invece entro se cerco tramite id
				else if( id != null && id.equals(idJson)){
					founded = true;
				}	
			}		
					
			if (!founded) {
				Log.getInstance().getLogger().info("Nessun utente trovato");
				return null;
			}
			
			//Il return viene modificato in modo da tener conto della ISTANZIAZIONE ANCHE DELLE PREFERENZE dell'utente.
			if((Long)result.get(jpPartner)==1) {
				Partner partner = new Partner((String) result.get(jpUsername),(String) result.get(jpEmail),(Long) result.get(jpID));
				partner.setPreferences(((ArrayList<String>)result.get(jpPref)));
				return partner;
			}
			Log.getInstance().getLogger().info(String.valueOf(result.get(jpWallet)));
			User user = new User((String) result.get(jpUsername),(String) result.get(jpEmail),(Long) result.get(jpID), (Long) result.get(jpWallet));
			user.setPreferences(((ArrayList<String>)result.get(jpPref)));
			return user;							
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
