package logic.controller;

import java.lang.reflect.Executable;
import java.sql.SQLException;

public class CertifiedException extends Exception{
private static final long serialVersionUID = 1L;
	
	private final SQLException errorSQL;
	
	public CertifiedException() {
		super("Problemi durante l'autenticazione");
		errorSQL = null;
	}
	
	public CertifiedException(String message) {
		super(message);
		errorSQL= null;
	}
	
	public CertifiedException(SQLException e) {
		super(e.getMessage());
		errorSQL = e;
	}
	
	@Override
	public String getMessage() {
		String excep = super.getMessage();
		if(errorSQL !=null && errorSQL.getErrorCode() == 1644) {
			// Qui isoliamo e individuiamo la specifica eccezione
			excep = errorSQL.getMessage().split("\\) ")[1];
			return excep;
			}
		else if (errorSQL !=null) {
			excep = errorSQL.getMessage();	
			
			return excep;
			}
		return excep;
	}
}
