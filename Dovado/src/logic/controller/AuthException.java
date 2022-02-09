package logic.controller;

import java.sql.SQLException;

import logic.model.Log;

public class AuthException extends Exception{
	private static final long serialVersionUID = 1L;
	
	private final SQLException errorSQL;
	
	public AuthException() {
		super("Problemi durante l'autenticazione");
		errorSQL = null;
	}
	
	public AuthException(String message) {
		super(message);
		errorSQL= null;
	}
	
	public AuthException(SQLException e) {
		super(e.getMessage());
		errorSQL = e;
	}
	
	@Override
	public String getMessage() {
		String excep = super.getMessage();
		if(errorSQL !=null && errorSQL.getErrorCode() == 1062) {
			// Qui isoliamo e individuiamo la specifica eccezione
			excep = errorSQL.getMessage().split(" ")[6];
			
			switch (excep) {
			case "'email_UNIQUE'":
				return "Esiste già un utente con questa mail";
			case "'username_UNIQUE'":
				return "Esiste già un utente con questo Username";
			default:
				return "Errore duplicazione di: " + excep;
			}
			}
		else if (errorSQL !=null && errorSQL.getErrorCode() == 1048) {
			excep = errorSQL.getMessage().split(" ")[2];	
			
			if (excep.equals("'username'")) {
				return "L'utente non può essere NULL";
			} else {
				return "Il campo " + excep + " non può essere vuoto";
			}
		}
		return excep;
	}
}
