package logic.model;

import logic.controller.LoginController;

public class LogBean {
	private String email;
	private String password;
	private String error;
	
	//Qui vanno differenziati partner e user 
	private SuperUser sUser;
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
		System.err.println("\n"+"Prendo l'errore: "+ error);

	    return error;
	}

	public void setError(String error) {
	    this.error = error;
	} 
	
	public SuperUser getUser() {
		return sUser;
	}
	
	public void setUser(SuperUser sUser) {
		this.sUser = sUser;
	}
	
	public boolean validate() {
		sUser = logController.findUser(email, password);
		if (sUser == null) {
			error = "Mail o password errate";
			System.err.println("\n"+"login failed");
			return false;
		}
		System.err.println("\n"+"login OK!!");

		return true;
		
	}
}
