package logic.model;

import logic.controller.LoginController;

public class LogBean {
	private String email;
	private String password;
	private String error;
	
	//Qui vanno differenziati partner e user 
	private User user;
	private LoginController logController;

	public LogBean() {
		logController = new LoginController();
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
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public boolean validate() throws Exception {
		user = logController.loginExplorer(email, password);
		if (user == null) {
			error = "Mail o password errate";
			Log.getInstance().getLogger().info("login failed");
			return false;
		}
		Log.getInstance().getLogger().info("login OK!!");

		return true;
		
	}
}
