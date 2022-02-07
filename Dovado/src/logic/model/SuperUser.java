package logic.model;

public abstract class SuperUser {
	private String username;
	private Long uID;
	private String email;
	
	
	protected SuperUser(String usr,String email,Long id) {
		this.username = usr;
		this.email = email;
		this.uID = id;
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
	
	public SuperUser getUser(String username) {
		if(this.username.equals(username))
			return this;
		return null;
	}
	
}
