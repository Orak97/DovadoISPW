package logic.model;

import java.util.ArrayList;
import java.util.List;

public abstract class SuperUser {
	private String username;
	private Long uID;
	private String email;
	private ArrayList<String> preferences;
	
	protected SuperUser(String usr,String email,Long id) {
		this.username = usr;
		this.email = email;
		this.uID = id;
		this.preferences = new ArrayList<>();
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public Long getUserID() {
		return this.uID;
	}

	/*Si aggiungono due nuovi metodi: getPreferences, che restituisce
	* un ArrayList<String> contenente tutte le preferenze dell'istanza
	* di SuperUser, e setPreferences, che invece stabilisce le nuove 
	* preferenze del SuperUser in questione.
	*/
	public List<String> getPreferences() {
		return this.preferences;
	}
	
	public void setPreferences(List<String> newPreferences) {
		this.preferences = (ArrayList<String>) newPreferences;
		DAOSuperUser.getInstance().updateUserPreferences(this);
	}
	
	public SuperUser getUser(String username) {
		if(this.username.equals(username))
			return this;
		return null;
	}
	
}
