package logic.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import logic.controller.RegisterController;

public class RegBean {
private String email;
private String password;
private String password2;
private String username;
private String error;
private Pattern patternPsw;
private Pattern patternEmail;
private RegisterController regController;
private String radio;

public RegBean() {
	patternEmail = Pattern.compile(".+@.+\\.[a-z]+");
	patternPsw = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[,.!?&]).{8,20})");
	regController = new RegisterController();
	error = null;
}

public String getRadio() {
    return radio;
}

public void setRadio(String radio) {
    this.radio = radio;
}

public String getUsername() {
    return username;
}

public void setUsername(String username) {
    this.username = username;
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

public String getPassword2() {
	return password2;
}

public void setPassword2(String password2) {
	this.password2 = password2;
}

public String getError() {
    return error;
}

public void setError(String error) {
    this.error = error;
} 


public boolean validate() {
	int partner;
	Log.getInstance().getLogger().info(username +": "+ password2 + "  " + password);	 
	if (radio!=null) {
    	if (radio.equals("yes")) {
    		partner = 1;
    	} else {
    		partner = 0;
    	}
    } else {
    	error = "Devi scegliere un tipo di accesso";
		return false;
    }
	// controllo la mail se e scritta in maniera corretta
	Matcher matchEmail = patternEmail.matcher(email);
	if (!matchEmail.matches()) {
			error = "Mail sintatticamente sbagliata";
			Log.getInstance().getLogger().info("Mail sintatticamente sbagliata");
			
			return false;}
	
	//check sulla password		
	if (!password.equals(password2)) {
			error = "Le password non coincidono";
			return false;
		}
	
	
	Matcher matchPsw = patternPsw.matcher(password);
	if (!matchPsw.matches()) {
		error = "La password deve contenere almeno 8 cratteri e deve contenere numeri, lettere e un carattere tra:{',','.','&','!','?'} ";
		Log.getInstance().getLogger().info("password non conforme");
		return false;}
	
	if (username.length()>15 || username.length()<4 || username.lastIndexOf(" ") != -1) {
	
		error = "Lo username deve essere composto da una sola parola senza l'utilizzo di spazi e deve avere dai 4 ai 15 caratteri";
		Log.getInstance().getLogger().info("Spazi nell'username: " + username + username.length()+ "  ci sono questi spazi:  " + username.lastIndexOf(" "));		
		return false;}
	
	//Chiamo il controller che controlla se eventualmente gia esiste e lo aggiunge in alternativa
	if (!regController.addUser(email, username, password, partner)) {
		error = "Esiste gia un utente associato a questa email";
		return false;
	}
	
	return true;
	
	
}
}
