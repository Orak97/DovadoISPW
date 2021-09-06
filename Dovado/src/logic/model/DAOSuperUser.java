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
	private static final String USERJSON= "WebContent/user.json";
	
	private static final  String USERSKEY = "users";
	private static final  String EMAILKEY = "email";
	private static final  String USERNAMEKEY = "username";
	private static final  String WALLETKEY = "wallet";
	private static final  String PREFKEY = "preferences";
	private static final  String IDKEY = "id";
	private static final  String PARTNERKEY = "partner";
	private static final  String PASSKEY = "password";

	
	private DAOSuperUser() {
	}
	
	public static DAOSuperUser getInstance() {
		if(INSTANCE==null)
			INSTANCE = new DAOSuperUser();
		return INSTANCE;
	}
	
	public boolean addUserToJSON(String email, String username, int partner, String password) {
		JSONParser parser = new JSONParser();
		try {
			Object users = parser.parse(new FileReader(USERJSON));
			JSONObject userObj = (JSONObject) users;
			JSONArray userArray = (JSONArray) userObj.get(USERSKEY);
			JSONArray userPref = new JSONArray();
			
			
			if (findSuperUser(email, password, null)==(null)) {				
				JSONObject newUser = new JSONObject();

				newUser.put(IDKEY, userArray.size());
				newUser.put(EMAILKEY, email);
				newUser.put(USERNAMEKEY, username);
				newUser.put(PARTNERKEY, partner);
				if (partner == 0) {
					newUser.put(WALLETKEY, 0);
				}
				newUser.put(PASSKEY, password);
				newUser.put(PREFKEY, userPref);
				userArray.add(newUser);

			try(FileWriter file = new FileWriter(USERJSON)) {	
				file.write(userObj.toString());
				file.flush();
				}
			}
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
		
		try {
			Object users = parser.parse(new FileReader(USERJSON));
			JSONObject userRes = (JSONObject) users;
			JSONArray userArray = (JSONArray) userRes.get(USERSKEY);
			JSONArray preferences = new JSONArray();
			JSONObject result;
			
			//Aggiungo tutte le preferenze al nuovo JSONArray che ho creato.
			preferences.addAll(su.getPreferences());
			
			//Fatto cio vado a cercare all'interno del JSON il SuperUser che ho come istanza.
			for(i=0;i<userArray.size();i++) {
				result = (JSONObject)userArray.get(i);
				
				Long idJson = (Long) result.get(IDKEY);
				JSONArray oldpreferences;
				//Se trovato l'utente si pone all'interno dell'attributo PREFKEY
				//il nuovo JSONArray appositamente preparato in precedenza.
				if (su.getUserID().equals(idJson)) {
					oldpreferences = (JSONArray) result.get(PREFKEY);
					//Si ricostruisce l'arrayList delle preferenze per compararlo con il nuovo
					//che si andra ad inserire; Se sono uguali si esce restituendo falso.
					//Se vero si procede nel salvataggio.
					for(j=0;j<oldpreferences.size();j++) {
						oldpref.add((String)oldpreferences.get(j));
					}
					
					if(!su.getPreferences().equals(oldpref)) {
						result.put(PREFKEY, preferences);
					try (FileWriter file = new FileWriter(USERJSON)){
						file.write(userRes.toString());
						file.flush();
					}
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
		
		try {
			Object users = parser.parse(new FileReader(USERJSON));
			JSONObject userRes = (JSONObject) users;
			JSONArray userArray = (JSONArray) userRes.get(USERSKEY);
			JSONObject result;
			
			
			//Fatto cio vado a cercare all'interno del JSON l'utente il cui id voglio aggiornare il wallet
			for(i=0;i<userArray.size();i++) {
				result = (JSONObject)userArray.get(i);
				
				Long idJson = (Long) result.get(IDKEY);
				//Se trovato l'utente si pone all'interno dell'attributo "preferences"
				//il nuovo JSONArray appositamente preparato in precedenza.
				if (id.equals(idJson)) {
					daoWallet = (Long) result.get(WALLETKEY);
					if (daoWallet == null) {
						daoWallet =(long) 0;
					}
					daoWallet = daoWallet + wallet;
					
					if (daoWallet <= 0) {
						return false;
					}
					
						result.put(WALLETKEY, daoWallet);
						
					try (FileWriter file = new FileWriter(USERJSON)){
							
						file.write(userRes.toString());
						file.flush();
						
						return true;
					}
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
			Object users = parser.parse(new FileReader(USERJSON));
			JSONObject userRes = (JSONObject) users;
			JSONArray userArray = (JSONArray) userRes.get(USERSKEY);
			JSONObject result = null;

			for(i=0;i<userArray.size();i++) {
				result = (JSONObject)userArray.get(i);
	
				String emailJSON = (String) result.get(EMAILKEY);
				String passwordJSON = (String) result.get(PASSKEY);
				Long idJson = (Long) result.get(IDKEY);
			
				//Qui controllo nel caso uso la mail per cercare. In caso di psw = null si pu�  creare un'eccezione apposita 
				//per distinguere quando � lecita, nel caso di richieste del sistema, da quando non lo �
				if (email != null && email.equals(emailJSON)) {
					if(psw == null) {
						Log.getInstance().logger.warning("PASSWORD NULLA");
						founded = true;
						
					}
					else if (psw.equals(passwordJSON)) {				
						Log.getInstance().logger.info("PASSWORD CORRETTA");
						founded = true;
					} else {
					
					Log.getInstance().logger.info("PASSWORD SBAGLIATA");
					return null;
					}	
				}
				//Qui invece entro se cerco tramite id
				else if( id != null && Long.compare(id, idJson)==0){
					founded = true;
				}
				
				if (founded) {
					return getSuperUserByJSONObj(result);
				}
			}		
					
			Log.getInstance().logger.info("Nessun utente trovato");
			return null;
						
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private SuperUser getSuperUserByJSONObj(JSONObject result) {
		if(Long.compare((Long)result.get(PARTNERKEY),1L)==0) {

			Log.getInstance().logger.info("Partner Trovato");
			Partner partner = new Partner((String) result.get(USERNAMEKEY),(String) result.get(EMAILKEY),(Long) result.get(IDKEY));
			partner.setPreferences(((ArrayList<String>)result.get(PREFKEY)));
			return partner;
		}
		Log.getInstance().logger.info(String.valueOf(result.get(WALLETKEY)));
		User user = new User((String) result.get(USERNAMEKEY),(String) result.get(EMAILKEY),(Long) result.get(IDKEY), (Long) result.get(WALLETKEY));
		user.setPreferences(((ArrayList<String>)result.get(PREFKEY)));
		return user;	
	}
}
