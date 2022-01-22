package logic.model;

import logic.controller.LogExplorerController;

public class LogBean {
	private String email;
	private String password;
	private String error;


	public LogBean() {
		error = null;
	}

	public String getPassword() {
	    return password;
	}

	public void setPassword(String password) {
	    this.password = password;
	}

	public String getEmail() {
	    return email;
	}

	public void setEmail(String email) {
	    this.email = email;
	} 

	public String getError() {
		Log.getInstance().getLogger().warning("Prendo l'errore: "+ error);

	    return error;
	}

	public void setError(String error) {
	    this.error = error;
	} 


}
